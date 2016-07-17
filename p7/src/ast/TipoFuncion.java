package ast;

import java.util.*;

//import main.GestorErrores;
import visitor.*;

//	TipoFuncion:Tipo -> parametros:Definicion*  tipoRetorno:Tipo  defLocales:Definicion*  sentencias:Sentencia* ;

public class TipoFuncion extends AbstractTipo {

	public TipoFuncion(List<Definicion> parametros, Tipo tipoRetorno, List<Definicion> defLocales, List<Sentencia> sentencias) {
		this.parametros = parametros;
		this.tipoRetorno = tipoRetorno;
		this.defLocales = defLocales;
		this.sentencias = sentencias;

		searchForPositions(parametros, tipoRetorno, defLocales, sentencias);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public TipoFuncion(Object parametros, Object tipoRetorno, Object defLocales, Object sentencias) {
		this.parametros = (List<Definicion>) parametros;
		this.tipoRetorno = (Tipo) tipoRetorno;
		this.defLocales = (List<Definicion>) defLocales;
		this.sentencias = (List<Sentencia>) sentencias;

		searchForPositions(parametros, tipoRetorno, defLocales, sentencias);	// Obtener linea/columna a partir de los hijos
	}

	public List<Definicion> getParametros() {
		return parametros;
	}
	public void setParametros(List<Definicion> parametros) {
		this.parametros = parametros;
	}

	public Tipo getTipoRetorno() {
		return tipoRetorno;
	}
	public void setTipoRetorno(Tipo tipoRetorno) {
		this.tipoRetorno = tipoRetorno;
	}

	public List<Definicion> getDefLocales() {
		return defLocales;
	}
	public void setDefLocales(List<Definicion> defLocales) {
		this.defLocales = defLocales;
	}

	public List<Sentencia> getSentencias() {
		return sentencias;
	}
	public void setSentencias(List<Sentencia> sentencias) {
		this.sentencias = sentencias;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	public Map<String, Definicion> getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(Map<String, Definicion> simbolos) {
		this.simbolos = simbolos;
	}

	private List<Definicion> parametros;
	private Tipo tipoRetorno;
	private List<Definicion> defLocales;
	private List<Sentencia> sentencias;
	private Map<String, Definicion> simbolos;
	
	public int[] tam(){
		int cte1 = this.tipoRetorno.size(),cte2 = 0,cte3 = 0;
		for(Definicion local:defLocales)
			cte2+=local.getTipo().size();
		for(Definicion par:parametros)
			cte3+=par.getTipo().size();
		int[] aux = {cte1,cte2,cte3};
		return aux;
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

