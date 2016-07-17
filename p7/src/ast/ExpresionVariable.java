package ast;

import visitor.*;

//	ExpresionVariable:Expresion -> variable:String ;

public class ExpresionVariable extends AbstractExpresion {

	public ExpresionVariable(String variable) {
		this.variable = variable;
	}

	public ExpresionVariable(Object variable) {
		this.variable = (variable instanceof Token) ? ((Token)variable).getLexeme() : (String) variable;

		searchForPositions(variable);	// Obtener linea/columna a partir de los hijos
	}

	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	public Definicion getDefinicion() {
		return definicion;
	}

	public void setDefinicion(Definicion definicion) {
		this.definicion = definicion;
	}

	private String variable;
	private Definicion definicion;
}

