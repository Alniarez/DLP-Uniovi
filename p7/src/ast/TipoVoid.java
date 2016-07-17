package ast;

//import main.GestorErrores;
import visitor.*;

//	TipoVoid:Tipo ->  ;

public class TipoVoid extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	@Override
	public int size() {
		//GestorErrores.get().error("generacion de codigo", "void no tiene tamaño.", null);
		return 0;
	}
	
	@Override
	public String sufijo() {
		return "";
	}

}

