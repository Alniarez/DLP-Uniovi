package main;

import generadorCodigo.GeneradorCodigo;

import introspector.model.IntrospectorModel;
import introspector.view.IntrospectorTree;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

import lexico.Yylex;
import semantico.TablaSimbolos;
import semantico.Identificacion;
import semantico.InferenciaTipos;
import sintactico.*;
import visitor.*;
import ast.*;

public class Main {

	public static String sourceFile = "entrada/5";

	public static void main(String[] args) throws Exception {

		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			sourceFile = fc.getSelectedFile().getAbsolutePath();

			AST raiz = compilar(sourceFile);
			mostrarIntrospector(raiz);

			if (GestorErrores.get().hayErrores())
				errores();
			else {
				GeneradorCodigo.run(sourceFile, (Programa)raiz);
				//imprimirCodigo(raiz);
				//escribirHTML(raiz);
			}
		}
		/*
		AST raiz = compilar(sourceFile);
		//mostrarIntrospector(raiz);

		if (GestorErrores.get().hayErrores())
			errores();
		else {
			GeneradorCodigo.run(sourceFile, (Programa)raiz);
			//imprimirCodigo(raiz);
			//escribirHTML(raiz);
			mostrarIntrospector(raiz);
		}*/
	}

	public static AST compilar(String sourceName) throws Exception {
		Yylex lexico = new Yylex(new FileReader(sourceName));
		Parser sintáctico = new Parser(lexico, false);

		sintáctico.parse();

		AST raiz = sintáctico.getAST();

		if (raiz == null)
			return null;

		Programa p = (Programa) raiz;

		p.accept(new Identificacion(new TablaSimbolos()), null);
		if(GestorErrores.get().hayErrores())
			return null;
		p.accept(new InferenciaTipos(), null);

		return p;
	}

	static void mostrarIntrospector(AST raiz) {
		IntrospectorModel modelo = new IntrospectorModel("Programa", raiz);
		new IntrospectorTree("Introspector", modelo);
	}

	static void escribirHTML(AST raiz) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		String name = "salida/" + "Arbol " + (dateFormat.format(date));
		ASTPrinter.toHtml(sourceFile, raiz, name);
	}

	static void imprimirCodigo(AST raiz) {
		raiz.accept(new ImprimeCodigo(), null);
	}

	static void errores() {
		System.out.println("El programa de entrada tiene errores.");
	}

}