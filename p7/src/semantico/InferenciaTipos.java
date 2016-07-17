package semantico;

import ast.*;
import main.GestorErrores;
import visitor.*;

/**
 * Created by Jorge on 27/03/2015.
 */
public class InferenciaTipos extends DefaultVisitor {

	
	public Object visit(TipoFuncion node, Object param) {
		for (Definicion child : node.getParametros())
			child.accept(this, param);
		node.getTipoRetorno().accept(this, param);
		for (Definicion child : node.getDefLocales())
			child.accept(this, param);
		for (Sentencia child : node.getSentencias())
			child.accept(this, node);
		return null;
	}

	public Object visit(SentenciaAsignacion node, Object param) {
		node.getLeft().accept(this, param);
		node.getRight().accept(this, param);
		if (!node.getLeft().isDireccion())
			GestorErrores
					.get()
					.error("Semantico",
							"Esta expresión no puede usarse a la izquierda de una asignación.",
							node.getStart());
		if (!mismaClase(node.getLeft().getTipado(), node.getRight().getTipado()))
			GestorErrores
					.get()
					.error("Semantico",
							"Los tipos de ambas expresiones no son compatibles en esta asignación.",
							node.getStart());
		return null;
	}
	
	public Object visit(SentenciaRead node, Object param) {
		node.getExpresion().accept(this, param);
			if(!node.getExpresion().isDireccion())
				GestorErrores.get().error("Semnatico", "No se puede hacer lecturas de esta expresion.", node.getStart());
		return null;
	}

	public Object visit(SentenciaWrite node, Object param) {
		for (Expresion child : node.getExpresion())
			child.accept(this, param);
		for (Expresion child : node.getExpresion())
			if(!sonTiposSimples(child.getTipado()))
				GestorErrores.get().error("Semantico", "Sólo se pueden hacer escrituras de tipos simples.", node.getStart());
		return null;
	}

	public Object visit(SentenciaReturn node, Object param) {
		TipoFuncion tf = (TipoFuncion) param;
		
		for (Expresion child : node.getExpresion())
			child.accept(this, param);
		for (Expresion child : node.getExpresion())
			if (!mismaClase(child.getTipado(), tf.getTipoRetorno()))
				GestorErrores.get().error("Semantico",
						"Tipos incompatibles en el retorno.", node.getStart());
		if(node.getExpresion().size() == 0 && !(tf.getTipoRetorno() instanceof TipoVoid))
			GestorErrores.get().error("Semantico",
					"Falta valor de retorno de tipo "+tf.getTipoRetorno().getClass(), node.getStart());
		return null;
	}

	public Object visit(SentenciaWhile node, Object param) {
		node.getCondicion().accept(this, param);
		if (!mismaClase(node.getCondicion().getTipado(), new TipoInt()))
			GestorErrores
					.get()
					.error("Semantico",
							"El tipo de la condición no es un entero o resultado de expresiones lógicas.",
							node.getStart());
		for (Sentencia child : node.getSentencias())
			child.accept(this, param);

		return null;
	}

	public Object visit(SentenciaIf node, Object param) {
		node.getCondicion().accept(this, param);
		if (!mismaClase(node.getCondicion().getTipado(), new TipoInt()))
			GestorErrores
					.get()
					.error("Semantico",
							"El tipo de la condición no es un entero o resultado de expresiones lógicas.",
							node.getStart());
		for (Sentencia child : node.getSentenciasIf())
			child.accept(this, param);
		for (Sentencia child : node.getSentenciasElse())
			child.accept(this, param);
		return null;
	}

	public Object visit(SentenciaProcedimiento node, Object param) {
		TipoFuncion tf = (TipoFuncion) node.getDefinicion().getTipo();
		for (Expresion child : node.getEntrada())
			child.accept(this, param);
		if (node.getEntrada().size() != tf.getParametros().size())
			GestorErrores.get().error("Semantico",
					"El número de parametros no coincide.", node.getStart());
		for (int i = 0; i < node.getEntrada().size(); i++)
			if (!mismaClase(node.getEntrada().get(i).getTipado(), tf
					.getParametros().get(i).getTipo()))
				GestorErrores.get().error("Semantico",
						"El tipo de parametro no coincide.", node.getStart());
		return null;
	}

	public Object visit(ExpresionCast node, Object param) {
		node.getTipo().accept(this, param);
		node.getExpresion().accept(this, param);
		if (!sonTiposSimples(node.getExpresion().getTipado()))
			GestorErrores.get().error("Semantico",
					"Sólo se puede hacer cast de y a tipos simples [char, int, float].", node.getStart());
		if(mismaClase(node.getExpresion().getTipado(), node.getTipo()))
			GestorErrores.get().error("Semantico",
					"No se puede hacer cast a su propia clase.", node.getStart());
		node.setTipado(node.getTipo());
		return null;
	}

	public Object visit(ExpresionChar node, Object param) {
		node.getTipo().accept(this, param);
		node.setTipado(node.getTipo());
		return null;
	}

	public Object visit(ExpresionInt node, Object param) {
		node.getTipo().accept(this, param);
		
		node.setTipado(node.getTipo());
		return null;
	}

	public Object visit(ExpresionReal node, Object param) {
		node.getTipo().accept(this, param);
		node.setTipado(node.getTipo());
		return null;
	}

	public Object visit(ExpresionAritmetica node, Object param) {
		node.getLeft().accept(this, param);

		node.getRight().accept(this, param);

		if (!mismaClase(node.getLeft().getTipado(), node.getRight().getTipado()))
			GestorErrores.get().error("Semantico", "No son del mismo tipo.",node.getStart());
		else
			if(node.getLeft().getTipado() instanceof TipoChar)
				GestorErrores.get().error("Semantico", "No se pueden realizar operaciones aritméticas de carácteres.",node.getStart());

		if(node.getSymbol().equals("%"))
			if(!(node.getLeft().getTipado() instanceof TipoInt) || !(node.getRight().getTipado() instanceof TipoInt))
				GestorErrores.get().error("Semantico", "Sólo se puede realizar la operacon módulo (%) de números enteros.",node.getStart());
		
		node.setTipado(node.getLeft().getTipado());
		
		return null;
	}

	public Object visit(ExpresionVariable node, Object param) {
		if(!(node.getDefinicion().getTipo() instanceof TipoComplejo))
			node.setDireccion(true);
		node.setTipado(node.getDefinicion().getTipo());
		return null;
	}

	public Object visit(ExpresionAccesoArray node, Object param) {
		node.getExpresion1().accept(this, param);
		if (!(node.getExpresion1().getTipado() instanceof TipoArray))
			GestorErrores.get().error("Semantico", "No es tipo array.",
					node.getStart());
		
		node.getExpresion2().accept(this, param);
		
		if (!(node.getExpresion2().getTipado() instanceof TipoInt))
			GestorErrores.get().error("Semantico",
					"Sólo se pueden acceder a posiciones enteras.",
					node.getStart());
		
		node.setDireccion(true);
		
		TipoArray aux = (TipoArray) node.getExpresion1().getTipado();
		
		node.setTipado(aux.getTipo());
		
		return null;
	}

	public Object visit(ExpresionAccesoCampo node, Object param) {
		
		node.getExpresion().accept(this, param);
		
		if (!(node.getExpresion().getTipado() instanceof TipoComplejo))
			GestorErrores.get().error("Semantico",
					"["+node.getNombre() + "] non es una estructura.",
					node.getStart());
		
		node.setDireccion(true);
		
		TipoComplejo aux = (TipoComplejo) node.getExpresion().getTipado();
		TipoStruct aux2 = aux.getStruct();
		
		Campo campo = aux2.getCampo(node.getNombre());
		
		if (campo == null)
			GestorErrores.get().error(
					"Semantico",
					"La estructura no tiene el campo <" + node.getNombre()
							+ ">.", node.getStart());
		else
			node.setTipado(aux2.getCampo(node.getNombre()).getTipo());
		return null;
	}

	public Object visit(ExpresionLlamadaFuncion node, Object param) {
		TipoFuncion tf = (TipoFuncion) node.getDefinicion().getTipo();
		for (Expresion child : node.getEntrada())
			child.accept(this, param);
		if (node.getEntrada().size() != tf.getParametros().size())
			GestorErrores.get().error("Semantico",
					"El número de parametros no coincide.", node.getStart());
		for (int i = 0; i < node.getEntrada().size(); i++)
			if (!mismaClase(node.getEntrada().get(i).getTipado(), tf
					.getParametros().get(i).getTipo()))
				GestorErrores.get().error("Semantico",
						"El tipo de parametro no coincide.", node.getStart());
		node.setTipado(tf.getTipoRetorno());
		return null;
	}

	public Object visit(ExpresionComparacion node, Object param) {
		node.getLeft().accept(this, param);
		node.getRight().accept(this, param);
		
		if (!mismaClase(node.getLeft().getTipado(), node.getRight().getTipado()))
			GestorErrores.get().error("Semantico",
					"Los tipos no son comparables.", node.getStart());
		if(node.getLeft().getTipado() instanceof TipoChar)
			GestorErrores.get().error("Semantico",
					"No se pueden compar caracteres.", node.getStart());

		node.setTipado(new TipoInt());
		return null;
	}


	public Object visit(ExpresionLogica node, Object param) {
		node.getLeft().accept(this, param);
		node.getRight().accept(this, param);
		if (!mismaClase(node.getLeft().getTipado(), node.getRight().getTipado()))
			GestorErrores.get().error("Semantico","Los tipos no son comparables.", node.getStart());
		if(!mismaClase(node.getLeft().getTipado(), new TipoInt()))
			GestorErrores.get().error("Semantico","Las expresiones de una expresión logica deben ser numeros enteros o resultado de operaciones lógicas.", node.getStart());
		node.setTipado(new TipoInt());
		return null;
	}

	public Object visit(ExpresionNegacion node, Object param) {
		node.getExpresion().accept(this, param);
		if (!mismaClase(node.getExpresion().getTipado(), new TipoInt()))
			GestorErrores.get().error("Semantico",
					"Los tipos no son comparables.", node.getStart());
		node.setTipado(node.getExpresion().getTipado());
		return null;
	}

	public Object visit(ExpresionMenosUnario node, Object param) {
		node.getExpresion().accept(this, param);
		if (!(mismaClase(node.getExpresion().getTipado(), new TipoInt())))
			GestorErrores.get().error("Semantico",
					"Los tipos no son comparables.", node.getStart());
		node.setTipado(node.getExpresion().getTipado());
		return null;
	}

	public boolean mismaClase(Tipo t1, Tipo t2) {
		if(t1 instanceof TipoChar && t2 instanceof TipoChar)
			return true;
		if(t1 instanceof TipoInt && t2 instanceof TipoInt)
			return true;
		if(t1 instanceof TipoReal && t2 instanceof TipoReal)
			return true;
		return false;
	}

	public boolean sonTiposSimples(Tipo... tipos) {
		boolean simple = true;
		for (Tipo t : tipos)
			if ((t instanceof TipoChar) || (t instanceof TipoInt)
					|| (t instanceof TipoReal))
				continue;
			else
				simple = false;
		return simple;
	}

}
