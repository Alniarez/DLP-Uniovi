package lexico;
import java.io.*;
import ast.Position;
import sintactico.*;
import lexico.*;
import main.*;

@SuppressWarnings("all")/*Una parcela en la calle de la piruleta.*/
%%
// % debug // para depurar
//%class Lexico
%byaccj
%unicode
%line
%column
%public

%{


	public int line() { return yyline + 1; }
	public int column() { return yycolumn + 1; }
	public String lexeme() { return yytext(); }

	// Traza para probar el lÈxico de manera independiente al sint·ctico
	public static void main(String[] args) throws Exception {
		Yylex lex = new Yylex(new FileReader(Main.sourceFile));
		int token;
		while ((token = lex.yylex()) != 0)
			System.out.println("\t[" + lex.line() + ":" + lex.column() + "] Token: " + token + ". Lexema: " + lex.lexeme());
	}

%}
digito 			= [0-9]
letra 			= [a-zA-Z·ÈÌÛ˙¡…Õ”⁄—Ò]
alfanumerico	= {digito}|{letra}
identificador	= {letra}{alfanumerico}*
punto			= \.
%%
// Operadores	
[%+\-\*/<>:;\[\]{}()=!,\.]	{ return yytext().charAt(0); }
"||"						{ return Parser.OR; }
"&&"						{ return Parser.AND; }
"=="						{ return Parser.IGUALDAD; 	}
"!="						{ return Parser.DESIGUALDAD; 	}
">="						{ return Parser.MAYORIGUAL; }
"<="						{ return Parser.MENORIGUAL; }
write|print					{ return Parser.WRITE; }
read						{ return Parser.READ; }
return						{ return Parser.RETURN; }
cast						{ return Parser.CAST; }
if							{ return Parser.IF; }
else						{ return Parser.ELSE ;}
while						{ return Parser.WHILE ;}
float|real					{ return Parser.REAL; }
int							{ return Parser.INT; }
char						{ return Parser.CHAR; }
struct						{ return Parser.STRUCT;}
var							{ return Parser.VAR;}
'.'							{ return Parser.LITCHAR; }
{digito}+    				{ return Parser.LITENT; }
{digito}+{punto}{digito}+	{ return Parser.LITREAL;}
{identificador}				{ return Parser.ID;}
"/*"~"*/"					{ }
"//".*						{ }
[ \n\r\t]     				{ }
.							{ GestorErrores.get().error("LÈxico", "Cadena \"" + yytext() +"\" no reconocida.", new Position(line(), column())); }