package semantico;

import java.util.*;

import ast.Definicion;

public class TablaSimbolos {
	
	public int ambito=0;
	private List<Map<String,Definicion>> tabla;
	
	public TablaSimbolos()  {
		tabla = new ArrayList<Map<String, Definicion>>();
		tabla.add(new HashMap<String, Definicion>());
	}

	public void set() {
		tabla.add(new HashMap<String, Definicion>());
		ambito++;
	}
	
	public void reset() {
		tabla.remove(ambito);
		ambito--;
	}
	
	public boolean insertar(Definicion simbolo) {
		String nombe = simbolo.getIdentificador();
		
		if(tabla.get(ambito).containsKey(nombe))
			return false;
		tabla.get(ambito).put(nombe, simbolo);
		return true;
	}
	
	public Definicion buscar(String id) {
		Definicion def = null;
		int tmp = ambito;
		while(def==null){
			def = tabla.get(tmp).get(id);
			tmp--;
			if(tmp<0)
				break;
		}
		return def;
	}

	public Definicion buscarAmbitoActual(String id) {
		return tabla.get(ambito).get(id);
	}
	
	public Map<String,Definicion> getSimbolos(){
		return tabla.get(ambito);
	}
	
}
