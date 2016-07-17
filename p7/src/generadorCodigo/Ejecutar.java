package generadorCodigo;

import ast.*;
import visitor.DefaultVisitor;

public class Ejecutar extends DefaultVisitor {

	public Object visit(Programa node, Object param) {
		ln();
		out("call main");
		ln();
		out("halt");
		ln();
		ln();
		for (Definicion child : node.getDefiniciones())
			child.accept(this, param);
		return null;
	}

	public Object visit(DefVariable node, Object param) {
		if (node.getTipo() instanceof TipoFuncion) {
			node.getTipo().accept(this, node);
		} else if (node.getTipo() instanceof TipoStruct) {
			node.getTipo().accept(this, node);
		} else {
			out("#var ");
			out(node.getIdentificador());
			out(":");
			node.getTipo().accept(this, param);
			ln();
			com("(" + node.offset() + ")");
		}
		ln();
		return null;
	}

	public Object visit(TipoInt node, Object param) {
		out("int");
		return null;
	}

	public Object visit(TipoVoid node, Object param) {
		out("void");
		return null;
	}

	public Object visit(TipoReal node, Object param) {
		out("float");
		return null;
	}

	public Object visit(TipoChar node, Object param) {
		out("byte");
		return null;
	}

	public Object visit(TipoArray node, Object param) {
		out(node.getTam() + "*");
		node.getTipo().accept(this, param);

		return null;
	}

	public Object visit(TipoFuncion node, Object param) {
		ln();
		out(((DefVariable) param).getIdentificador() + ":");
		ln();
		out("#func " + ((DefVariable) param).getIdentificador());
		ln();

		for (Definicion child : node.getParametros()) {
			out("#param " + child.getIdentificador() + ":");
			child.getTipo().accept(this, param);
			ln();
			com("BP + " + ((DefVariable) child).offset() + "\n");
		}

		out("#ret ");
		node.getTipoRetorno().accept(this, param);
		ln();
		int[] tam = node.tam();
		String ret = "ret " + tam[0] + ", " + tam[1] + ", " + tam[2];
		com("" + ret + "\n");

		for (Definicion child : node.getDefLocales()) {
			out("#local " + child.getIdentificador() + ":");
			child.getTipo().accept(this, param);
			ln();
			com("BP - " + ((DefVariable) child).offset() + "\n");
		}

		out("enter " + tam[1]);
		ln();

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, node);

		if (node.getTipoRetorno() instanceof TipoVoid) {
			out(ret);
			ln();
		}
		return null;
	}

	public Object visit(TipoStruct node, Object param) {
		out("#type " + ((DefVariable) param).getIdentificador() + ":\t{\n");
		Campo child = null;
		if (node.getCampos() != null)
			for (int i = 0; i < node.getCampos().size(); i++) {
				child= (Campo) node.getCampos().get(i);
				out("\t" + child.getIdentificador() + ":");
				child.getTipo().accept(this, param);
				ln();
				com("\t(Offset: " + child.offset() + ")");
			}
		out("}");
		ln();
		com("(tamaño: " + node.size() + ")");

		return null;
	}

	public Object visit(TipoComplejo node, Object param) {
		out(node.getNombreTipo());
		return null;
	}

	public Object visit(SentenciaAsignacion node, Object param) {
		ln();
		line(node);
		node.getLeft().accept(direccion, param);
		node.getRight().accept(valor, param);
		out("store");
		out(node.getLeft().getTipado().sufijo());
		ln();
		return null;
	}

	public Object visit(SentenciaWrite node, Object param) {
		ln();
		line(node);
		for (Expresion child : node.getExpresion()) {
			child.accept(valor, param);
			out("out" + child.getTipado().sufijo());
			ln();
		}
		return null;
	}

	public Object visit(SentenciaRead node, Object param) {
		ln();
		line(node);
		
		node.getExpresion().accept(direccion, param);
		out("in" + node.getExpresion().getTipado().sufijo());
		ln();
		out("store");
		out(node.getExpresion().getTipado().sufijo());
		ln();
		return null;
	}

	public Object visit(SentenciaReturn node, Object param) {
		if(!(node.getExpresion().isEmpty())){
			ln();
			line(node);
		}
		TipoFuncion a = (TipoFuncion) param;
		if (!(a.getTipoRetorno() instanceof TipoVoid))
			line(node);
		int[] tam = ((TipoFuncion) param).tam();
		String ret = "ret " + tam[0] + ", " + tam[1] + ", " + tam[2];
		
		if(node.getExpresion().isEmpty())
			out(ret+"\n");
		for (Expresion child : node.getExpresion()) {
			child.accept(valor, param);
			out(ret);
			ln();
		}
		return null;
	}
	//ejecutar[[SentenciaWhile: sentencia → exp sentencia*]] =
	public Object visit(SentenciaWhile node, Object param) {
//		int etiqueta = gc.getEtiquetas(2);
		int etiqueta = GeneradorCodigo.getEtiquetas(2);
		int aux = etiqueta+1;
//		et etiqueta :
		out("et"+etiqueta+":");	ln();
		
//		valor[[exp]]
		node.getCondicion().accept(valor, param);
		
//		jz etiqueta+1
		out("jz et"+(aux)); ln();
		
//		for(Sentencia sen:sentencia*)
		for (Sentencia child : node.getSentencias())
//			ejecutar[[sen]]
			child.accept(this, param);
		
//		jmp etiqueta
		out("jmp et"+etiqueta); ln();
		
//		et etiqueta+1 :
		out("et"+(aux)+":");
		
		return null;
	}

	//ejecutar[[SentenciaIf: sentencia1 → exp sentencia2* sentenci3*]] =
	public Object visit(SentenciaIf node, Object param) {
		//int etiqueta = gc.getEtiquetas(2);
		int etiqueta = GeneradorCodigo.getEtiquetas(2);
		int aux = etiqueta+1;
		//valor[[exp]]
		node.getCondicion().accept(valor, param);	ln();
		
		//jnz etiqueta
		out("jnz et"+etiqueta);	ln();
		
		//for(Sentencia sen:sentencia2+)
		for (Sentencia child : node.getSentenciasElse())
			//ejecutar[[sen]]
			child.accept(this, param);
		
		//jmp etiqueta+1
		
		out("jmp et"+aux);	ln();
		
		//et etiqueta :
		out("et"+etiqueta+":");	ln();
		
		//for(Sentencia sen:sentencia3*)
		for (Sentencia child : node.getSentenciasIf())
			//ejecutar[[sen]]
			child.accept(this, param);
		
		//et etiqueta+1 :
		out("et"+(aux)+":");	ln();
		
		return null;
	}

	
	public Object visit(SentenciaProcedimiento node, Object param) {
		ln();
		line(node);
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(valor, param);
		out("call "+node.getIdentificador());
		ln();
		if(!(((TipoFuncion) node.getDefinicion().getTipo()).getTipoRetorno() instanceof TipoVoid))
			out("pop"+(((TipoFuncion) node.getDefinicion().getTipo()).getTipoRetorno().sufijo()+"\n"));
		return null;
	}

	Ejecutar ejecutar = GeneradorCodigo.ejecutar;
	Valor valor = GeneradorCodigo.valor;
	Direccion direccion = GeneradorCodigo.direccion;
	boolean comentarios = false;

	void com(String s) {
		if (comentarios)
			out("' " + s);
	}

	void out(String s) {
		GeneradorCodigo.out(s);
	}

	void line(AST ast) {
		out("#line " + ast.getEnd().getLine() + "\n");
	}

	void ln() {
		GeneradorCodigo.ln();
	}
}
