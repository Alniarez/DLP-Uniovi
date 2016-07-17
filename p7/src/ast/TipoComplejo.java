package ast;

import visitor.*;

//	TipoComplejo:Tipo -> nombreTipo:String ;

public class TipoComplejo extends AbstractTipo {
	

	private TipoStruct struct;

	public TipoComplejo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	public TipoComplejo(Object nombreTipo) {
		this.nombreTipo = (nombreTipo instanceof Token) ? ((Token)nombreTipo).getLexeme() : (String) nombreTipo;

		searchForPositions(nombreTipo);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombreTipo() {
		return nombreTipo;
	}
	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	public TipoStruct getStruct() {
		return struct;
	}

	public void setStruct(TipoStruct struct) {
		this.struct = struct;
	}

	private String nombreTipo;

	@Override
	public int size() {
		return struct.size();
	}
	
	@Override
	public String sufijo() {
		return "";
	}
}

