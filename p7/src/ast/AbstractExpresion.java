package ast;

public abstract class AbstractExpresion extends AbstractTraceable implements Expresion {
	
	boolean direccion;

    public void setDireccion(boolean direccion){
        this.direccion = direccion;
    }

    public boolean isDireccion(){
        return direccion;
    }

    Tipo tipado;

    public void setTipado(Tipo tipado){
        this.tipado = tipado;
    }

    public Tipo getTipado(){
        return tipado;
    }
}

