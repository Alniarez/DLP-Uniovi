package ast;

import visitor.*;

//	TipoChar:Tipo ->  ;

public class TipoChar extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public String sufijo() {
		return "b";
	}
}

