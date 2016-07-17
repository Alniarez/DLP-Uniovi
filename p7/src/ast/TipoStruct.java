package ast;

import java.util.*;

import visitor.*;

//	TipoStruct:Tipo -> campos:Campo* ;

public class TipoStruct extends AbstractTipo {

	public TipoStruct(List<Campo> campos) {
		this.campos = campos;

		searchForPositions(campos);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public TipoStruct(Object campos) {
		this.campos = (List<Campo>) campos;

		searchForPositions(campos);	// Obtener linea/columna a partir de los hijos
	}

	public List<Campo> getCampos() {
		return campos;
	}
	
	public Campo getCampo(String id){
		for(Campo c : getCampos())
			if(c.getIdentificador().equals(id))
				return c;
		return null;
	}
	
	public void setCampos(List<Campo> campos) {
		this.campos = campos;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<Campo> campos;

	@Override
	public int size() {
		int aux = 0;
		for(Campo c : campos)
			aux+=c.getTipo().size();
		return aux;
	}
	
	@Override
	public String sufijo() {
		return "";
	}
	
	
}

