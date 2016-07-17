package main;

import ast.*;

public class GestorErrores {
	
	private GestorErrores(){}

	/**
	 * Crear el error.
	 * */
	public void error(String fase, String msg, Position position) {
		String texto = "Error en " + fase + ": ";
		if (position != null)
			texto += "[" + position + "] ";
		error(texto + msg);
	}

	public void error(String msg) {
		errores++;
		System.out.println(msg);
	}

	public boolean hayErrores() {
		return errores > 0;
	}

	private int errores = 0;

	private static GestorErrores gestor = null;
	
	public static GestorErrores get() {
		if(gestor==null)
			gestor= new GestorErrores();
		return gestor;
	}
}
