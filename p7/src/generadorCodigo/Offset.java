package generadorCodigo;

import visitor.DefaultVisitor;
import ast.*;


public class Offset extends DefaultVisitor {
	private int direccion = 0;

	public Object visit(DefVariable node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		if(node.getAmbito()==0 && !(node.getTipo() instanceof TipoStruct) && !(node.getTipo() instanceof TipoFuncion)){
			node.setOffset(this.direccion);
			direccion+=node.getTipo().size();
		}
		return null;
	}
	
	public Object visit(TipoFuncion node, Object param) {
		int num = 0;
		if (node.getDefLocales() != null)
			for(int i=0; i<node.getDefLocales().size(); i++){
				num+= node.getDefLocales().get(i).getTipo().size();
				node.getDefLocales().get(i).setOffset(num*-1);
			}

		if (node.getParametros() != null){
			num = 4;
			for(int i=node.getParametros().size()-1; i>=0; i--){
				node.getParametros().get(i).setOffset(num);
				num+= node.getParametros().get(i).getTipo().size();
			}
		}		
		return null;
	}
	
	public Object visit(TipoStruct node, Object param) {
		int tam = 0;
		if (node.getCampos() != null)
			for (int i = 0; i < node.getCampos().size(); i++) {
				Campo child = (Campo) node.getCampos().get(i);
				child.getTipo().accept(this, param);
				child.offset = tam;
				tam += child.size();
			}
		return null;
	}
	
}
