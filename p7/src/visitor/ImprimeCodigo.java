package visitor;

// package <nombre paquete>;

import ast.*;

/*
 Plantilla para Visitors.
 Para crear un nuevo Visitor cortar y pegar este código y ya se tendrá un visitor que compila y 
 que al ejecutarlo recorrerá todo el árbol (sin hacer nada aún en él).
 Solo quedará añadir a cada método visit aquello adicional que tenga que realizar sobre su nodo del AST.
 */

public class ImprimeCodigo extends DefaultVisitor {

	// ---------------------------------------------------------
	// Tareas a realizar en cada método visit:
	//
	// Si en algún método visit NO SE QUIERE HACER NADA más que recorrer los
	// hijos entonces se puede
	// borrar (dicho método se heredará de DefaultVisitor con el código de
	// recorrido).
	//
	// Lo siguiente es para cuando se quiera AÑADIR alguna funcionalidad
	// adicional a un visit:
	//
	// - El código que aparece en cada método visit es aquel que recorre los
	// hijos. Es el mismo código
	// que está implementado en el padre (DefaultVisitor). Por tanto la llamada
	// a 'super.visit' y el
	// resto del código del método hacen lo mismo (por ello 'super.visit' está
	// comentado).
	//
	// - Lo HABITUAL será borrar todo el código de recorrido dejando solo la
	// llamada a 'super.visit'. De esta
	// manera cada método visit se puede centrar en la tarea que tiene que
	// realizar sobre su nodo del AST.
	//
	// - La razón de que aparezca el código de recorrido de los hijos es por si
	// se necesita realizar alguna
	// tarea DURANTE el mismo (por ejemplo ir comprobando su tipo). En este caso
	// ya se tiene implementado
	// dicho recorrido y solo habrá que incrustar las acciones adicionales en el
	// mismo. En este caso
	// la llamada a 'super.visit' deberá ser borrada.
	// ---------------------------------------------------------

	// class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {

		nivel = 0;
		// super.visit(node, param);

		if (node.getDefiniciones() != null)
			for (Definicion child : node.getDefiniciones())
				child.accept(this, param);

		return null;
	}

	// class DefVariable { Tipo tipo; String identificador; }
	public Object visit(DefVariable node, Object param) {
		if (node.getTipo() == null)
			return null;
		if (node.getTipo() instanceof TipoFuncion) {
			print(nivel,node.getIdentificador() + ":");
			node.getTipo().accept(this, param);
		} else if (node.getTipo() instanceof TipoStruct) {
			print(nivel,"struct " + node.getIdentificador() + "{");
			ln();
			node.getTipo().accept(this, param);
			print(nivel,"}");
			print(";");
		} else {
			print(nivel,"var " + node.getIdentificador() + ":");
			node.getTipo().accept(this, param);
			print(";");
		}
		ln();
		return null;
	}

	// class TipoVoid { }
	public Object visit(TipoVoid node, Object param) {
		print("void");
		return null;
	}

	// class TipoInt { }
	public Object visit(TipoInt node, Object param) {
		print("int");
		return null;
	}

	// class TipoReal { }
	public Object visit(TipoReal node, Object param) {
		print("real");
		return null;
	}

	// class TipoChar { }
	public Object visit(TipoChar node, Object param) {
		print("char");
		return null;
	}

	// class TipoArray { String tam; Tipo tipo; }
	public Object visit(TipoArray node, Object param) {

		// super.visit(node, param);

		if (node.getTipo() != null) {
			print("[" + node.getTam() + "]");
			node.getTipo().accept(this, param);
		}

		return null;
	}

	// class TipoFuncion { Tipo tipoRetorno; List<Definicion> parametros;
	// List<Definicion> defLocales; List<Sentencia> sentencias; }
	public Object visit(TipoFuncion node, Object param) {

		// super.visit(node, param);

		if (node.getTipoRetorno() != null)
			node.getTipoRetorno().accept(this, param);

		print("(");

		if (node.getParametros() != null)
			for (int i = 0; i < node.getParametros().size(); i++) {
				DefVariable child = (DefVariable) node.getParametros().get(i);
				print(child.getIdentificador() + ":");
				child.getTipo().accept(this, param);
				if (i < node.getParametros().size() - 1)
					print(", ");
			}

		print(" ) {");
		nivel++;

		ln();

		if (node.getDefLocales() != null)
			for (Definicion child : node.getDefLocales())
				child.accept(this, param);

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, param);
		nivel--;
		print(nivel,"}");
		return null;
	}

	// class TipoStruct { List<Definicion> campos; }
	public Object visit(TipoStruct node, Object param) {

		// super.visit(node, param);
		nivel++;
		if (node.getCampos() != null)
			for (int i = 0; i < node.getCampos().size(); i++) {
				Campo child = (Campo) node.getCampos().get(i);
				print(nivel,child.getIdentificador() + ":");
				child.getTipo().accept(this, param);
				print(";");
				ln();
			}
		nivel--;
		return null;
	}

	// class TipoComplejo { String nombreTipo; }
	public Object visit(TipoComplejo node, Object param) {
		print(node.getNombreTipo());
		return null;
	}

	// class SentenciaAsignacion { Expresion left; Expresion right; }
	public Object visit(SentenciaAsignacion node, Object param) {

		// super.visit(node, param);

		print(nivel, "");
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);

		print(" = ");

		if (node.getRight() != null)
			node.getRight().accept(this, param);

		print(";");
		ln();
		return null;
	}

	// class SentenciaWrite { List<Expresion> expresion; }
	public Object visit(SentenciaWrite node, Object param) {

		// super.visit(node, param);

		print(nivel,"write ");
		if (node.getExpresion() != null)
			for (int i = 0; i < node.getExpresion().size(); i++) {
				node.getExpresion().get(i).accept(this, param);
				if (i < node.getExpresion().size() - 1)
					print(", ");
			}
		print(";");
		ln();
		return null;
	}

	// class SentenciaRead { Expresion expresion; }
	public Object visit(SentenciaRead node, Object param) {

		// super.visit(node, param);
		print(nivel,"read ");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		print(";");
		ln();
		return null;
	}

	// class SentenciaReturn { List<Expresion> expresion; }
	public Object visit(SentenciaReturn node, Object param) {

		// super.visit(node, param);

		print(nivel,"return ");
		if (node.getExpresion() != null)
			for (Expresion child : node.getExpresion())
				child.accept(this, param);
		print(";");
		ln();
		return null;
	}

	// class SentenciaWhile { Expresion condicion; List<Sentencia> sentencias; }
	public Object visit(SentenciaWhile node, Object param) {

		// super.visit(node, param);
		print(nivel,"while(");
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		print("){");
		ln();
		nivel++;
		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, param);
		nivel--;
		print(nivel,"}");
		ln();

		return null;
	}

	// class SentenciaIf { Expresion condicion; List<Sentencia> sentenciasIf;
	// List<Sentencia> sentenciasElse; }
	public Object visit(SentenciaIf node, Object param) {

		// super.visit(node, param);
		print(nivel,"if (");
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		print("){");
		ln();
		nivel++;
		if (node.getSentenciasIf() != null)
			for (Sentencia child : node.getSentenciasIf())
				child.accept(this, param);
		nivel--;
		print(nivel,"else {");
		ln();
		nivel++;
		if (node.getSentenciasElse() != null)
			for (Sentencia child : node.getSentenciasElse())
				child.accept(this, param);
		nivel--;
		print(nivel,"}");
		ln();
		return null;
	}

	// class SentenciaProcedimiento { String identificador; List<Expresion>
	// entrada; }
	public Object visit(SentenciaProcedimiento node, Object param) {

		// super.visit(node, param);
		print(nivel,node.getIdentificador()+"( ");		
		if (node.getEntrada() != null)
			for (int i = 0; i < node.getEntrada().size(); i++) {
				node.getEntrada().get(i).accept(this, param);
				if (i < node.getEntrada().size() - 1)
					print(nivel,", ");
		}
		print(");");
		ln();
		return null;
	}

	// class ExpresionCast { Tipo tipo; Expresion expresion; }
	public Object visit(ExpresionCast node, Object param) {

		// super.visit(node, param);
		print("cast<");
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		print(">");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class ExpresionChar { Tipo tipo; String value; }
	public Object visit(ExpresionChar node, Object param) {

		// super.visit(node, param);
		
		print(node.getValue());

		return null;
	}

	// class ExpresionInt { Tipo tipo; String value; }
	public Object visit(ExpresionInt node, Object param) {

		// super.visit(node, param);

		print(node.getValue());

		return null;
	}

	// class ExpresionReal { Tipo tipo; String value; }
	public Object visit(ExpresionReal node, Object param) {

		// super.visit(node, param);

		
		print(node.getValue());

		return null;
	}

	// class ExpresionAritmetica { Expresion left; String symbol; Expresion
	// right; }
	public Object visit(ExpresionAritmetica node, Object param) {

		// super.visit(node, param);
		print("(");
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		print(")");
		print(node.getSymbol());
		print("(");
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		print(")");
		return null;
	}

	// class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		print(node.getVariable());
		return null;
	}

	// class ExpresionAccesoArray { Expresion expresion1; Expresion expresion2;
	// }
	public Object visit(ExpresionAccesoArray node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion1() != null)
			node.getExpresion1().accept(this, param);
		print("[");
		if (node.getExpresion2() != null)
			node.getExpresion2().accept(this, param);
		print("]");
		return null;
	}

	// class ExpresionAccesoCampo { Expresion expresion; String nombre; }
	public Object visit(ExpresionAccesoCampo node, Object param) {

		// super.visit(node, param);
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		print("."+node.getNombre());
		

		return null;
	}

	// class ExpresionLlamadaFuncion { String identificador; List<Expresion>
	// entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {

		print(node.getIdentificador()+"(");		
		if (node.getEntrada() != null)
			for (int i = 0; i < node.getEntrada().size(); i++) {
				node.getEntrada().get(i).accept(this, param);
				if (i < node.getEntrada().size() - 1)
					print(", ");
		}
		print(")");
		
		return null;
	}

	// class ExpresionComparacion { Expresion left; String symbol; Expresion
	// right; }
	public Object visit(ExpresionComparacion node, Object param) {

		// super.visit(node, param);

		print("(");
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		print(")");
		print(node.getSymbol());
		print("(");
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		print(")");
		return null;
	}

	// class ExpresionLogica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionLogica node, Object param) {

		print("(");
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		print(")");
		print(node.getSymbol());
		print("(");
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		print(")");

		return null;
	}

	// class ExpresionNegacion { Expresion expresion; }
	public Object visit(ExpresionNegacion node, Object param) {

		// super.visit(node, param);
		print("!");

		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);

		return null;
	}

	// class ExpresionMenosUnario { Expresion expresion; }
	public Object visit(ExpresionMenosUnario node, Object param) {

		print("-");

		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);

		return null;
	}

	private void print(String a) {
		System.out.print(a);
	}
	
	private int nivel;
	
	private void print(int nivel, String a) {
		for(int i = 0; i<nivel; i++)
			System.out.print("\t");
		System.out.print(a);
	}


	private void ln() {
		System.out.println("");
	}
}
