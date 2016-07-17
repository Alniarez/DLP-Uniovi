package ast;

import java.util.*;

import visitor.*;

//	SentenciaProcedimiento:Sentencia -> identificador:String  entrada:Expresion* ;

public class SentenciaProcedimiento extends AbstractSentencia {

	public SentenciaProcedimiento(String identificador, List<Expresion> entrada) {
		this.identificador = identificador;
		this.entrada = entrada;

		searchForPositions(entrada);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public SentenciaProcedimiento(Object identificador, Object entrada) {
		this.identificador = (identificador instanceof Token) ? ((Token)identificador).getLexeme() : (String) identificador;
		this.entrada = (List<Expresion>) entrada;

		searchForPositions(identificador, entrada);	// Obtener linea/columna a partir de los hijos
	}

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<Expresion> getEntrada() {
		return entrada;
	}
	public void setEntrada(List<Expresion> entrada) {
		this.entrada = entrada;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String identificador;
	private List<Expresion> entrada;
	
	private Definicion definicion;
	public Definicion getDefinicion() {
		return definicion;
	}

	public void setDefinicion(Definicion definicion) {
		this.definicion = definicion;
	}
}

