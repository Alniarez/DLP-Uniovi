package ast;

public interface Definicion extends AST {
	
	public Tipo getTipo();
	public String getIdentificador();
	
	public void setOffset(int d);
	public int offset();

}

