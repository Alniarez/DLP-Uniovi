package ast;

import java.util.*;
import visitor.*;

//	SentenciaWrite:Sentencia -> expresion:Expresion* ;

public class SentenciaWrite extends AbstractSentencia {

	public SentenciaWrite(List<Expresion> expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public SentenciaWrite(Object expresion) {
		this.expresion = (List<Expresion>) expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public List<Expresion> getExpresion() {
		return expresion;
	}
	public void setExpresion(List<Expresion> expresion) {
		this.expresion = expresion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<Expresion> expresion;
}

