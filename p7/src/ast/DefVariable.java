package ast;

import visitor.*;

//	DefVariable:Definicion -> identificador:String  tipo:Tipo ;

public class DefVariable extends AbstractDefinicion {

	@Override
	public String toString() {
		return "DefVariable [identificador=" + identificador + ", tipo=" + tipo
				+ ", ambito=" + ambito + ", offset=" + offset + "]";
	}

	public DefVariable(String identificador, Tipo tipo) {
		this.identificador = identificador;
		this.tipo = tipo;
		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public DefVariable(Object identificador, Object tipo) {
		this.identificador = (identificador instanceof Token) ? ((Token)identificador).getLexeme() : (String) identificador;
		this.tipo = (Tipo) tipo;

		searchForPositions(identificador, tipo);	// Obtener linea/columna a partir de los hijos
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String identificador;
	private Tipo tipo;
	private int ambito;
	private int offset;
	
	
	@Override
	public void setOffset(int offset) {
		this.offset = offset;
		
	}

	@Override
	public int offset() {
		return offset;
	}

	public int getAmbito() {
		return ambito;
	}

	public void setAmbito(int ambito) {
		this.ambito = ambito;
	}
}

