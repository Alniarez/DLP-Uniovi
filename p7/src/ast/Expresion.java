package ast;

public interface Expresion extends AST {

	boolean isDireccion();
	public void setDireccion(boolean b);
	
	public Tipo getTipado();
	public void setTipado(Tipo tipo2);

}

