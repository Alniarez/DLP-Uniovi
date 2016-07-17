%{
import java.io.*;
import java.util.*;
import ast.*;
import main.*;
import lexico.*;
@SuppressWarnings("all")
%}
%TOKEN RETURN WHILE IF ELSE MENOSELSE ID STRUCT CHAR INT REAL VAR WRITE READ CAST AND
%TOKEN LITCHAR LITENT LITREAL MENOSUNARIO IGUALDAD DESIGUALDAD MENORIGUAL MAYORIGUAL OR 

%nonassoc MENOSELSE
%nonassoc ELSE
%left '!' AND OR
%left '>' '<' MENORIGUAL MAYORIGUAL DESIGUALDAD IGUALDAD
%left '+' '-'
%left '*' '/' '%'
%right MENOSUNARIO
%left '['']''.'
%nonassoc '('')' 
%%

programa	
	:  ldef 			{ raiz = new Programa($1); }
	;

ldef
	:	def				{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); definiciones.add((Definicion)$1); $$ = definiciones; }
	|	ldef def		{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>)$1; definiciones.add((Definicion)$2); $$ = definiciones;}
	;

def
	:	defVar			{ $$ = $1; }
	|	defStruct		{ $$ = $1; }
	|	defFun			{ $$ = $1; }
	;

defVars
	:	ldefVar			{ $$ = $1; }
	|					{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); $$ = definiciones; }
	;	

ldefVar
	:	defVar			{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>();
							definiciones.add((Definicion)$1); 
							$$ = definiciones; }
	|	ldefVar defVar	{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) $1;
							definiciones.add((DefVariable)$2);
							$$ = definiciones; }
	;

defVar
	:	VAR ID ':' tipo ';'		{ $$ = new DefVariable($2, $4); }
	;

tipo
	:	ID						{ $$ = new TipoComplejo($1); }
	|	tipoSimple				{ $$ = $1; }
	|	'[' LITENT ']' tipo		{ $$ = new TipoArray($2, $4); }
	;

tipoSimple
	:	REAL					{ $$ = new TipoReal(); }
	|	INT						{ $$ = new TipoInt(); }
	|	CHAR					{ $$ = new TipoChar(); }
	;

tipoRetorno
	:	':' tipoSimple			{ $$ = $2; }
	|							{ $$ = new TipoVoid();}
	;

defStruct
	:	STRUCT ID '{' lcamposStruct '}' ';'		{ $$ = new DefVariable($2, new TipoStruct($4)); }
	;

lcamposStruct
	:	camposStruct				{ ArrayList<Campo> campos = new ArrayList<Campo>(); campos.add((Campo)$1); $$ = campos; }
	|	lcamposStruct camposStruct	{ ArrayList<Campo> campos = (ArrayList<Campo>) $1; campos.add((Campo)$2); $$ = campos; }
	;

camposStruct
	:	ID ':' tipo ';'						{ $$ = new Campo($1, $3); }
	;

defFun
	:	ID '(' parametros ')'  tipoRetorno '{' defVars lsentencia '}'	{ $$ = new DefVariable($1, new TipoFuncion($3, $5, $7, $8)); }
	;

parametros
	:	lparametro					{ $$ = (ArrayList<Definicion>)$1; }
	|								{ $$ = new ArrayList<Definicion>(); }
	;

lparametro
	:	parametro								{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); 
													definiciones.add((DefVariable)$1); 
													$$ = definiciones; 
												}
	|	lparametro ',' parametro				{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) $1;
													definiciones.add((DefVariable)$3); 
													$$ = definiciones;
												}
	;

parametro
	:	ID ':' tipoSimple							{ $$ = new DefVariable($1, $3); }
	;

lsentencia
	: 												{ $$ = new ArrayList<Sentencia>(); }
	| lsentencia sentencia							{
														List<Sentencia> lista = (ArrayList<Sentencia>) $1;
														Sentencia sent = (Sentencia)$2;
														lista.add(sent);
														$$ = lista;
													}
	;

sentencia
	:	exp		'=' exp ';'												{ $$ = new SentenciaAsignacion( $1,  $3); }
	|	ID		'(' argumentos ')' ';'									{ $$ = new SentenciaProcedimiento( $1, $3); }
	|	WRITE 	lexp ';'												{ $$ = new SentenciaWrite( $2); }
	|	READ 	exp ';'													{ $$ = new SentenciaRead( $2); }
	|	RETURN  retorno ';'												{ $$ = new SentenciaReturn( $2); }
	|	WHILE 	'(' exp ')' '{' lsentencia '}'							{ $$ = new SentenciaWhile( $3, $6); }
	|	IF 		'(' exp ')' '{' lsentencia '}' %prec MENOSELSE			{ $$ = new SentenciaIf( $3, $6, new ArrayList<Sentencia>()); }
	|	IF 		'(' exp ')' '{' lsentencia '}' ELSE '{' lsentencia '}'	{ $$ = new SentenciaIf( $3, $6, $10); }
	;

retorno
	:	exp											{
														ArrayList<Expresion> retorno = new ArrayList<Expresion>();
														retorno.add((Expresion)$1);
														$$ = retorno;
													}
	|												{ $$ = new ArrayList<Expresion>(); }
	;

argumentos
	:	lexp										{ $$ = $1; }
	|												{ $$ = new ArrayList<Expresion>(); }
	;

lexp
	:	lexp ',' exp								{
														List<Expresion> expresiones = (ArrayList<Expresion>)$1;
														Expresion expresion = (Expresion)$3;
														expresiones.add(expresion);
														$$ = expresiones;
													}
	|	exp											{
														Expresion expresion = (Expresion)$1;
														List<Expresion> expresiones = new ArrayList<Expresion>();
														expresiones.add(expresion);
														$$ = expresiones;
													}
	;

exp
	:	'('exp')'									{ $$ = $2; }
	|	CAST '<'tipoSimple'>' '('exp')'				{ $$ = new ExpresionCast			($3,  $6);}
	|	LITCHAR										{ $$ = new ExpresionChar			(new TipoChar(), $1);}
	|	LITENT										{ $$ = new ExpresionInt				(new TipoInt(),$1);}
	|	LITREAL										{ $$ = new ExpresionReal			(new TipoReal(), $1);}
	|	'-' 	exp		%prec MENOSUNARIO			{ $$ = new ExpresionMenosUnario		($2);}
	|	exp		'+' 	exp	   						{ $$ = new ExpresionAritmetica		($1, $2, $3); }
	|	exp 	'-'		exp	 						{ $$ = new ExpresionAritmetica		($1, $2, $3); }
	|	exp 	'*'		exp	  						{ $$ = new ExpresionAritmetica		($1, $2, $3); }
	|	exp 	'/' 	exp	 						{ $$ = new ExpresionAritmetica		($1, $2, $3); }
	|	exp 	'%' 	exp							{ $$ = new ExpresionAritmetica		($1, $2, $3); }
	|	ID											{ $$ = new ExpresionVariable		($1); }
	|	exp		'.'	ID								{ $$ = new ExpresionAccesoCampo		($1, $3); }
	|	exp		'[' exp ']'							{ $$ = new ExpresionAccesoArray		($1, $3); }
	|	ID 		'(' argumentos ')'					{ $$ = new ExpresionLlamadaFuncion	($1, $3); }
	|	exp 	'<' exp								{ $$ = new ExpresionComparacion		($1, $2, $3); }
	|	exp 	'>' exp	 							{ $$ = new ExpresionComparacion		($1, $2, $3); }
	|	exp 	MENORIGUAL 		exp					{ $$ = new ExpresionComparacion		($1, $2, $3); }
	|	exp 	MAYORIGUAL 		exp					{ $$ = new ExpresionComparacion		($1, $2, $3); }
	|	exp 	DESIGUALDAD 	exp					{ $$ = new ExpresionComparacion		($1, $2, $3); }
	|	exp 	IGUALDAD 		exp					{ $$ = new ExpresionComparacion		($1, $2, $3); }
	|	'!' 	exp									{ $$ = new ExpresionNegacion		($2); }
	|	exp 	AND 			exp					{ $$ = new ExpresionLogica			($1, $2, $3); }
	|	exp 	OR 				exp					{ $$ = new ExpresionLogica			($1, $2, $3); }
	;

%%
/* No es necesario modificar esta sección ------------------ */

public Parser(Yylex lex, boolean debug) {
	this(debug);
	this.lex = lex;
}

// Métodos de acceso para el main -------------
public int parse() { return yyparse(); }
public AST getAST() { return raiz; }

// Funciones requeridas por Yacc --------------
void yyerror(String msg) {
	Token lastToken = (Token) yylval;
	GestorErrores.get().error("Sintáctico", "Token = " + lastToken.getToken() + ", lexema = \"" + lastToken.getLexeme() + "\". " + msg, lastToken.getStart());
}

int yylex() {
	try {
		int token = lex.yylex();
		yylval = new Token(token, lex.lexeme(), lex.line(), lex.column());
		return token;
	} catch (IOException e) {
		return -1;
	}
}

private Yylex lex;
private AST raiz;