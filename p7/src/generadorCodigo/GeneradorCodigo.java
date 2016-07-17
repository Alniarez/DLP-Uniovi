package generadorCodigo;

import ast.*;

import java.io.*;

public class GeneradorCodigo {
	
	private static FileWriter fw;
	
	static Ejecutar ejecutar;
	static Valor valor;
	static Direccion direccion;
	static int contador = 0;
	
	public static void run(String fichero, Programa programa){
		
		ejecutar = new Ejecutar();
		valor = new Valor();
		direccion = new Direccion();
		
		ejecutar.valor=valor;
		ejecutar.direccion=direccion;
		
		valor.direccion=direccion;
		
		direccion.valor = valor;
		
		programa.accept(new Offset(), null);
		try {
			
			fw = new FileWriter(fichero+".out.txt");
			//System.out.println(fichero);
			out("#source \""+new File(fichero).getName()+"\"");
			ln();
			programa.accept(ejecutar, null);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void out(String s){
		try {
			fw.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void ln(){
		try {
			fw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static private int etiquetas = 1;
	static public int getEtiquetas(int numero) {
	int temp = etiquetas;
	etiquetas += numero;
	return temp;
	}

}
