package generadorCodigo;

import ast.*;
import visitor.DefaultVisitor;

public class Valor extends DefaultVisitor {
	
	Direccion direccion = GeneradorCodigo.direccion;

	public Object visit(ExpresionChar node, Object param) {
		out("pushb ");
		char value = node.getValue().charAt(1);
		out((int)value+"");
		ln();
		return null;
	}
	
	public Object visit(ExpresionInt node, Object param) {
		out("pushi ");
		out(node.getValue());
		ln();
		return null;
	}
	
	public Object visit(ExpresionReal node, Object param) {
		out("pushf ");
		out(node.getValue());
		ln();
		return null;
	}
	
	public Object visit(ExpresionVariable node, Object param) {
		node.accept(direccion, param);
		out("load");
		out(node.getTipado().sufijo());
		ln();
		return null;
	}
	
	public Object visit(ExpresionCast node, Object param) {
		node.getExpresion().accept(this, param);
		out(conversion(node.getExpresion().getTipado(), node.getTipo()));
		ln();
		return null;
	}
	
	public Object visit(ExpresionComparacion node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		out(simbolo(node.getSymbol()));
		out(node.getTipado().sufijo());
		ln();
		return null;
	}

	public Object visit(ExpresionAritmetica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		out(simbolo(node.getSymbol()));
		out(node.getTipado().sufijo());
		ln();
		return null;
	}

	public Object visit(ExpresionAccesoArray node, Object param) {
		if (node.getExpresion1() != null)
			node.accept(direccion, param);
		out("load");
		out(node.getTipado().sufijo());
		ln();
		return null;
	}

public Object visit(ExpresionAccesoCampo node, Object param) {

		if (node.getExpresion() != null)
			node.accept(direccion, param);
		out("load"+node.getTipado().sufijo());
		ln();
		return null;
	}

	//	class ExpresionLlamadaFuncion { String identificador;  List<Expresion> entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);
		out("call "+node.getIdentificador());
		ln();

		return null;
	}



	//	class ExpresionLogica { Expresion left;  String symbol;  Expresion right; }
	public Object visit(ExpresionLogica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		String s = "";
		switch (node.getSymbol()) {
		case "||":
			s="or";
			break;
		case "&&":
			s="and";
			break;
		}
		out(s);
		ln();
		return null;
	}

	//	class ExpresionNegacion { Expresion expresion; }
	public Object visit(ExpresionNegacion node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		out("not");
		ln();

		return null;
	}

	public Object visit(ExpresionMenosUnario node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		out("dup");
		out(node.getTipado().sufijo());
		ln();
		out("dup");
		out(node.getTipado().sufijo());
		ln();
		out("add");
		out(node.getTipado().sufijo());
		ln();
		out("sub");
		out(node.getTipado().sufijo());
		ln();
		return null;
	}
	
	String simbolo(String s){					
		switch (s) {
		case "==":
			s="eq";
			break;
		case "!=":
			s="ne";
			break;
		case ">=":
			s="ge";
			break;
		case "<=":
			s="le";
			break;
		case ">":
			s="gt";
			break;
		case "<":
			s="lt";
			break;
		case "+":
			s="add";
			break;
		case "-":
			s="sub";
			break;
		case "*":
			s="mul";
			break;
		case "/":
			s="div";
			break;
		case "%":
			s="mod";
			break;
		default:
			s="";
			break;
		}
		return s;
	}
	
	String conversion(Tipo t1, Tipo t2){
		String s="";
		if(t1 instanceof TipoChar){
			if( t2 instanceof TipoInt)
				//char a int
				s="b2i";
			if(t2 instanceof TipoReal)
				//char a float
				s="b2i\ni2f";
		}else if( t1 instanceof TipoInt){
			if(t2 instanceof TipoChar)
				//int a char
				s="i2b";
			if( t2 instanceof TipoReal)
				//int a real
				s="i2f";
		}else if ( t1 instanceof TipoReal ){
			if( t2 instanceof TipoInt)
				//real a int
				s= "f2i";
			if( t2 instanceof TipoChar)
				//real a char
				s="f2i\ni2b";
		}
		return s;
	}
	

	void out(String s) {
		GeneradorCodigo.out(s);
	}
	
	void ln() {
		GeneradorCodigo.ln();
	}
}
