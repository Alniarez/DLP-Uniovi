package generadorCodigo;

import ast.*;
import visitor.DefaultVisitor;

public class Direccion extends DefaultVisitor {

	public Valor valor;
	
	public Object visit(ExpresionVariable node, Object param) {
		DefVariable def = (DefVariable) node.getDefinicion();
		if(def.getAmbito()!=0){
			out("pusha BP");
			ln();
			out("push "+def.offset());
			ln();
			out("add");
			ln();
		}
		else{
			out("push "+def.offset());
			ln();
		}
		return null;
	}
	
	public Object visit(ExpresionAccesoArray node, Object param) {
		node.getExpresion1().accept(this, param);
		node.getExpresion2().accept(valor, param);
		TipoArray ta = (TipoArray) node.getExpresion1().getTipado();
		out("push "+ta.getTipo().size());
		ln();
		out("mul");
		ln();
		out("add");
		ln();
		return null;
	}

	public Object visit(ExpresionAccesoCampo node, Object param) {
		node.getExpresion().accept(this, param);
		TipoComplejo tc = (TipoComplejo) node.getExpresion().getTipado();
		TipoStruct ts = tc.getStruct();
		out("push "+ts.getCampo(node.getNombre()).offset);
		ln();
		out("add");
		ln();
		return null;
	}

	

	void out(String s) {
		GeneradorCodigo.out(s);
	}
	void ln() {
		GeneradorCodigo.ln();
	}
	
	
}
