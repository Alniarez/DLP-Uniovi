package semantico;

import java.util.ArrayList;

import main.GestorErrores;
import visitor.DefaultVisitor;
import ast.*;

public class Identificacion extends DefaultVisitor {
	
	private TablaSimbolos tabla;

	public Identificacion(TablaSimbolos tabla){
		this.tabla = tabla;
	}

	//	class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {

		if (node.getDefiniciones() != null)
			for (Definicion child : node.getDefiniciones())
				child.accept(this, param);

		Definicion main = tabla.getSimbolos().get("main");
		if(main == null)
			GestorErrores.get().error("Debe existir una función main.");
		else if(!(main.getTipo() instanceof TipoFuncion))
			GestorErrores.get().error("Debe existir una función main.");
		
		return tabla.getSimbolos();
	}

	//	class DefVariable { String identificador;  Tipo tipo; }
	public Object visit(DefVariable node, Object param) {
		if (node.getTipo() == null)
			return null;
		if (node.getTipo() instanceof TipoFuncion) {
			if(tabla.buscarAmbitoActual(node.getIdentificador())==null){
				tabla.insertar(node);
				tabla.set();
				node.getTipo().accept(this, param);
				TipoFuncion aux = (TipoFuncion) node.getTipo();
				aux.setSimbolos(tabla.getSimbolos());
				tabla.reset();
			}else{
				GestorErrores.get().error("Semantico", "Funcion <"+node.getIdentificador()+"> ya definida.", node.getStart());
			}
		} else {
			if(tabla.buscarAmbitoActual(node.getIdentificador())==null){
				tabla.insertar(node);
				node.getTipo().accept(this, param);
			}else{
				GestorErrores.get().error("Semantico", "Variable <"+node.getIdentificador()+"> ya definida.", node.getStart());
			}
		}
		node.setAmbito(tabla.ambito);
		return null;
	}
	

	
		// class SentenciaProcedimiento { String identificador; List<Expresion>
		// entrada; }
		public Object visit(SentenciaProcedimiento node, Object param) {
			// super.visit(node, param);
			Definicion d = tabla.buscar(node.getIdentificador());
			if(d == null)
				GestorErrores.get().error("Semantico", "Función <"+node.getIdentificador()+"> no definida.", node.getStart());
			else
				node.setDefinicion(d);
			
			if (node.getEntrada() != null)
				for (Expresion child : node.getEntrada())
					child.accept(this, param);

			return null;
		}


	//	class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		Definicion d = tabla.buscar(node.getVariable());
		if(d == null)
			GestorErrores.get().error("Semantico", "Variable <"+node.getVariable()+"> no definida.", node.getStart());
		else
			node.setDefinicion(d);
		return null;
	}
	
//	class ExpresionLlamadaFuncion { String identificador;  List<Expresion> entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {
		Definicion d = tabla.buscar(node.getIdentificador());
		if(d == null)
			GestorErrores.get().error("Semantico", "Función <"+node.getIdentificador()+"> no definida.", node.getStart());
		else
			node.setDefinicion(d);
		
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);

		return null;
	}

	
//	class TipoStruct { List<Campo> campos; }
	public Object visit(TipoStruct node, Object param) {

		ArrayList<String> nombres = new ArrayList<String>();
		
		for (Campo child : node.getCampos()){
			if(nombres.contains(child.getIdentificador()))
				GestorErrores.get().error("Semantico", "Campo <"+child.getIdentificador()+"> ya definido.", node.getStart());
			else
				nombres.add(child.getIdentificador());
		}
		
		for (Campo child : node.getCampos())
			child.accept(this, param);

		return null;
	}
	
	
	//	class TipoComplejo { String nombreTipo; }
	public Object visit(TipoComplejo node, Object param) {
		Definicion d = tabla.buscar(node.getNombreTipo());
		if(d == null)
			GestorErrores.get().error("Semantico", "Tipo <"+node.getNombreTipo()+"> no definido.", node.getStart());
		else
			node.setStruct((TipoStruct) d.getTipo());
		return null;
	}
}

