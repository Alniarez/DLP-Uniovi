package ast;

import visitor.*;

//	TipoReal:Tipo ->  ;

public class TipoReal extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	
	@Override
	public int size() {
		return 4;
	}
	
	@Override
	public String sufijo() {
		return "f";
	}

}

