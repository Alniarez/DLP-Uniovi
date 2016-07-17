//### This file created by BYACC 1.8(/Java extension  1.14)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package sintactico;



//#line 2 "..\sinj.y"
import java.io.*;
import java.util.*;
import ast.*;
import main.*;
import lexico.*;
@SuppressWarnings("all")
//#line 24 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short RETURN=257;
public final static short WHILE=258;
public final static short IF=259;
public final static short ELSE=260;
public final static short MENOSELSE=261;
public final static short ID=262;
public final static short STRUCT=263;
public final static short CHAR=264;
public final static short INT=265;
public final static short REAL=266;
public final static short VAR=267;
public final static short WRITE=268;
public final static short READ=269;
public final static short CAST=270;
public final static short AND=271;
public final static short LITCHAR=272;
public final static short LITENT=273;
public final static short LITREAL=274;
public final static short MENOSUNARIO=275;
public final static short IGUALDAD=276;
public final static short DESIGUALDAD=277;
public final static short MENORIGUAL=278;
public final static short MAYORIGUAL=279;
public final static short OR=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    6,    6,    7,    7,
    3,    8,    8,    8,    9,    9,    9,   10,   10,    4,
   11,   11,   12,    5,   13,   13,   15,   15,   16,   14,
   14,   17,   17,   17,   17,   17,   17,   17,   17,   21,
   21,   19,   19,   20,   20,   18,   18,   18,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   18,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    1,    1,    0,    1,    2,
    5,    1,    1,    4,    1,    1,    1,    2,    0,    6,
    1,    2,    4,    9,    1,    0,    1,    3,    3,    0,
    2,    4,    5,    3,    3,    3,    7,    7,   11,    1,
    0,    1,    0,    3,    1,    3,    7,    1,    1,    1,
    2,    3,    3,    3,    3,    3,    1,    3,    4,    4,
    3,    3,    3,    3,    3,    3,    2,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    2,    4,    5,    6,    0,
    0,    0,    3,    0,    0,    0,   27,    0,    0,    0,
    0,    0,    0,    0,   21,   12,   17,   16,   15,    0,
    0,   13,   29,    0,    0,   28,    0,    0,   22,    0,
   11,   18,    0,    0,   20,    0,    9,   30,    0,   23,
   14,    0,   10,    0,    0,    0,    0,    0,    0,    0,
   48,   49,   50,    0,    0,    0,   24,   31,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   36,
    0,    0,    0,    0,   34,    0,   35,    0,   46,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   58,    0,    0,    0,    0,    0,    0,
    0,   59,   32,   60,   30,   30,   33,    0,    0,    0,
    0,   37,    0,   47,    0,   30,    0,   39,
};
final static short yydgoto[] = {                          4,
    5,    6,    7,    8,    9,   48,   49,   31,   32,   35,
   24,   25,   15,   52,   16,   17,   68,   69,  103,  104,
   72,
};
final static short yysindex[] = {                      -172,
  -39, -259, -242,    0, -172,    0,    0,    0,    0, -235,
  -92,   -9,    0,   -8,   11,    9,    0, -203,  -79, -162,
   19, -235,   20, -123,    0,    0,    0,    0,    0, -189,
   33,    0,    0, -162,  -30,    0,  -79,   37,    0,   12,
    0,    0, -159,   53,    0,  -79,    0,    0, -159,    0,
    0,  256,    0,  190,   74,   75,   77,  190,  190,   56,
    0,    0,    0,  190,  190,  190,    0,    0,   64,   87,
  389,   69,  190,  190,  190,  389,  -18,   76, -162,  411,
  -33,  100,  190,  190,  190,  190,  190,  190,  190,  190,
  190,  190,  190,  190,  190,  190, -133,  190,  190,    0,
  111,  133,   89,   88,    0,  190,    0,   71,    0,  411,
  173,  173,  173,  173,  411,  173,  173,  185,  185,  -33,
  -33,  -33,  166,    0,  228,   93,   17,   21,   90,  389,
  110,    0,    0,    0,    0,    0,    0,  190,  280,  305,
  323,    0, -109,    0,   36,    0,  434,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  161,    0,    0,    0,    0,  122,
    0,    0,    0,    0,    0,  123,    0,    0,    0,    0,
   42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  455,    0,    0,    0,    0,    0,  487,    0,
    0,    0,    0,   33,    0,    0,  356,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -37,
  107,    0,    0,    0,  127,  -12,    0,    0,    0,  241,
  -26,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  127,    0,
    0,    0,    0,  128,    0,    0,    0,    0,    0,  450,
  569,  638,  674,  709,  602,  744,  768,  545,  580,    1,
   27,   38,    0,    0,    0,    0,    0,    0,  378,   -4,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  525,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  167,    8,    0,    0,    0,    0,   -7,   -6,    0,
    0,  153,    0,  -70,    0,  159,    0,  837,   83,  126,
    0,
};
final static int YYTABLESIZE=1048;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   10,   38,   11,   57,   57,   57,   57,   57,   57,   57,
   51,   30,   97,   33,   51,   51,   51,   51,   51,   12,
   51,   57,   57,   57,   57,  106,   14,   42,   45,   44,
   18,   45,   51,   51,   51,   51,   44,   54,   51,   44,
  105,   54,   54,   54,   54,   54,   45,   54,   19,   20,
   47,   21,   22,   57,   44,   57,   53,   96,   23,   54,
   54,   54,   54,   55,  139,  140,   51,   55,   55,   55,
   55,   55,  108,   55,   56,  147,   34,   37,   56,   56,
   56,   56,   56,   40,   56,   55,   55,   55,   55,    1,
    2,   41,   43,   54,    3,   45,   56,   56,   56,   56,
   95,   27,   28,   29,   46,   93,   91,    3,   92,   97,
   94,   50,   95,   73,   74,   79,   75,   93,   91,   55,
   92,   97,   94,   90,   98,   89,   99,  100,  124,  129,
   56,  106,  131,  134,  107,   90,   95,   89,   23,  135,
  109,   93,   91,  136,   92,   97,   94,   95,  137,  138,
  145,  127,   93,   91,   96,   92,   97,   94,  146,   90,
    1,   89,   26,   25,   19,   40,   96,   43,   42,   95,
   90,   13,   89,  128,   93,   91,   39,   92,   97,   94,
   36,  126,   26,   77,   27,   28,   29,    0,    0,    0,
   96,    0,   90,    0,   89,    0,    0,    0,    0,    0,
    0,   96,   95,    0,    0,    0,    0,   93,   91,   95,
   92,   97,   94,    0,   93,   91,    0,   92,   97,   94,
    0,   95,   64,   96,    0,   90,   93,   89,    0,   66,
   97,   94,    0,   57,   65,    0,    0,    0,   57,   57,
   57,   57,   57,    0,   51,    0,    0,    0,    0,   51,
   51,   51,   51,   51,    0,    0,   96,    0,  132,    0,
    0,    0,    0,   96,   95,    0,    0,    0,    0,   93,
   91,   54,   92,   97,   94,   96,   54,   54,   54,   54,
   54,   67,    0,    0,   67,    0,  133,   90,   64,   89,
    0,    0,    0,    0,    0,   66,    0,   55,    0,   67,
   65,   67,   55,   55,   55,   55,   55,    0,   56,    0,
    0,    0,   64,   56,   56,   56,   56,   56,   96,   66,
    0,    0,    0,    0,   65,    0,    0,    0,    0,    0,
    0,    0,    0,   67,   83,    0,    0,   64,    0,   84,
   85,   86,   87,   88,   66,    0,   83,    0,    0,   65,
    0,   84,   85,   86,   87,   88,    0,    0,    0,   95,
    0,    0,    0,  144,   93,   91,    0,   92,   97,   94,
   83,    0,    0,    0,    0,   84,   85,   86,   87,   88,
   67,   83,   90,    0,   89,    0,   84,   85,   86,   87,
   88,    0,   57,    0,    0,    0,    0,   57,   57,    0,
   57,   57,   57,   83,  142,    0,    0,    0,   84,   85,
   86,   87,   88,   96,   60,   57,   57,   57,    0,   60,
   60,    0,   60,   60,   60,   95,    0,    0,    0,  143,
   93,   91,    0,   92,   97,   94,   83,   60,   60,   60,
    0,   84,   85,   86,   87,   88,   57,   95,   90,    0,
   89,   70,   93,   91,    0,   92,   97,   94,    0,   60,
    0,   61,   62,   63,    0,    0,   64,    0,   60,    0,
   90,    0,   89,   66,    0,    0,    0,    0,   65,   96,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    0,
   68,    0,    0,   68,    8,    0,    0,    0,   83,    8,
    0,   96,    0,   84,   85,   86,   87,   88,   68,    0,
   68,   67,   54,   55,   56,    0,    0,   57,    0,    7,
   67,    0,    0,   58,   59,   60,    7,   61,   62,   63,
    0,    7,    0,    0,    0,    0,   54,   55,   56,    0,
    0,   57,   68,    0,    0,    0,    0,   58,   59,   60,
    0,   61,   62,   63,    0,    0,    0,   38,  148,    0,
    0,   54,   55,   56,   38,    0,   57,    0,    0,   38,
    0,    0,   58,   59,   60,    0,   61,   62,   63,    8,
    0,    0,    0,    0,    0,   52,    0,   52,   52,   52,
    0,    0,    0,   83,    0,    0,    0,    0,   84,   85,
   86,   87,   88,   52,   52,   52,   52,    0,    0,   66,
    0,    7,   66,    0,    0,    0,    0,    0,    0,    0,
   53,    0,   53,   53,   53,    0,   57,   66,   66,   66,
   66,   57,   57,   57,   57,   57,    0,   52,   53,   53,
   53,   53,   69,    0,    0,   69,    0,    0,   60,   38,
    0,    0,    0,   60,   60,   60,   60,   60,    0,   83,
   69,   66,   69,    0,   84,   85,   86,   87,   88,    0,
    0,    0,   53,    0,    0,    0,    0,    0,   65,    0,
    0,   65,    0,    0,    0,    0,   84,   85,   86,   87,
   54,   55,   56,    0,   69,   57,   65,   65,   65,   65,
    0,   58,   59,   60,    0,   61,   62,   63,    0,    0,
    0,    8,    8,    8,   63,    0,    8,   63,    0,    0,
   68,    0,    8,    8,    8,    0,    8,    8,    8,   68,
   65,    0,   63,   63,   63,   63,    0,    0,    0,    0,
    0,    0,    0,    7,    7,    7,    0,    0,    7,   64,
    0,    0,   64,    0,    7,    7,    7,    0,    7,    7,
    7,    0,    0,    0,    0,    0,   63,   64,   64,   64,
   64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   38,   38,   38,   62,    0,   38,   62,    0,    0,
    0,    0,   38,   38,   38,    0,   38,   38,   38,    0,
    0,   64,   62,   62,   62,   62,    0,    0,   61,    0,
    0,   61,    0,    0,    0,   52,    0,    0,    0,    0,
   52,   52,   52,   52,   52,    0,   61,   61,   61,   61,
    0,    0,    0,    0,    0,    0,   62,    0,    0,   66,
    0,    0,    0,    0,   66,   66,   66,   66,   66,    0,
   53,    0,    0,    0,    0,   53,   53,   53,   53,   53,
   61,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   69,    0,    0,    0,    0,    0,    0,    0,
    0,   69,    0,    0,    0,    0,    0,    0,    0,    0,
   71,    0,    0,    0,   76,   78,    0,    0,    0,    0,
   80,   81,   82,    0,    0,    0,    0,    0,   65,  101,
  102,   76,    0,   65,   65,   65,   65,   65,    0,  110,
  111,  112,  113,  114,  115,  116,  117,  118,  119,  120,
  121,  122,  123,    0,  125,   76,    0,    0,    0,    0,
    0,    0,  130,    0,   63,    0,    0,    0,    0,   63,
   63,   63,   63,   63,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  141,    0,    0,    0,    0,   64,
    0,    0,    0,    0,   64,   64,   64,   64,   64,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   62,    0,    0,    0,    0,   62,
   62,   62,   62,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,   61,   61,   61,   61,   61,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   40,  125,  262,   41,   42,   43,   44,   45,   46,   47,
   37,   91,   46,   20,   41,   42,   43,   44,   45,  262,
   47,   59,   60,   61,   62,   44,  262,   34,   41,   37,
  123,   44,   59,   60,   61,   62,   41,   37,   46,   44,
   59,   41,   42,   43,   44,   45,   59,   47,   58,   58,
   43,   41,   44,   91,   59,   93,   49,   91,  262,   59,
   60,   61,   62,   37,  135,  136,   93,   41,   42,   43,
   44,   45,   79,   47,   37,  146,   58,   58,   41,   42,
   43,   44,   45,  273,   47,   59,   60,   61,   62,  262,
  263,   59,  123,   93,  267,   59,   59,   60,   61,   62,
   37,  264,  265,  266,   93,   42,   43,  267,   45,   46,
   47,   59,   37,   40,   40,   60,   40,   42,   43,   93,
   45,   46,   47,   60,   61,   62,   40,   59,  262,   41,
   93,   44,   62,   41,   59,   60,   37,   62,  262,  123,
   41,   42,   43,  123,   45,   46,   47,   37,   59,   40,
  260,   41,   42,   43,   91,   45,   46,   47,  123,   60,
    0,   62,   41,   41,  123,   59,   91,   41,   41,   37,
   60,    5,   62,   41,   42,   43,   24,   45,   46,   47,
   22,   99,  262,   58,  264,  265,  266,   -1,   -1,   -1,
   91,   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   37,   -1,   -1,   -1,   -1,   42,   43,   37,
   45,   46,   47,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   37,   33,   91,   -1,   60,   42,   62,   -1,   40,
   46,   47,   -1,  271,   45,   -1,   -1,   -1,  276,  277,
  278,  279,  280,   -1,  271,   -1,   -1,   -1,   -1,  276,
  277,  278,  279,  280,   -1,   -1,   91,   -1,   93,   -1,
   -1,   -1,   -1,   91,   37,   -1,   -1,   -1,   -1,   42,
   43,  271,   45,   46,   47,   91,  276,  277,  278,  279,
  280,   41,   -1,   -1,   44,   -1,   59,   60,   33,   62,
   -1,   -1,   -1,   -1,   -1,   40,   -1,  271,   -1,   59,
   45,   61,  276,  277,  278,  279,  280,   -1,  271,   -1,
   -1,   -1,   33,  276,  277,  278,  279,  280,   91,   40,
   -1,   -1,   -1,   -1,   45,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   93,  271,   -1,   -1,   33,   -1,  276,
  277,  278,  279,  280,   40,   -1,  271,   -1,   -1,   45,
   -1,  276,  277,  278,  279,  280,   -1,   -1,   -1,   37,
   -1,   -1,   -1,   41,   42,   43,   -1,   45,   46,   47,
  271,   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,
  125,  271,   60,   -1,   62,   -1,  276,  277,  278,  279,
  280,   -1,   37,   -1,   -1,   -1,   -1,   42,   43,   -1,
   45,   46,   47,  271,  125,   -1,   -1,   -1,  276,  277,
  278,  279,  280,   91,   37,   60,   61,   62,   -1,   42,
   43,   -1,   45,   46,   47,   37,   -1,   -1,   -1,  125,
   42,   43,   -1,   45,   46,   47,  271,   60,   61,   62,
   -1,  276,  277,  278,  279,  280,   91,   37,   60,   -1,
   62,  262,   42,   43,   -1,   45,   46,   47,   -1,  270,
   -1,  272,  273,  274,   -1,   -1,   33,   -1,   91,   -1,
   60,   -1,   62,   40,   -1,   -1,   -1,   -1,   45,   91,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,
   41,   -1,   -1,   44,   40,   -1,   -1,   -1,  271,   45,
   -1,   91,   -1,  276,  277,  278,  279,  280,   59,   -1,
   61,  271,  257,  258,  259,   -1,   -1,  262,   -1,   33,
  280,   -1,   -1,  268,  269,  270,   40,  272,  273,  274,
   -1,   45,   -1,   -1,   -1,   -1,  257,  258,  259,   -1,
   -1,  262,   93,   -1,   -1,   -1,   -1,  268,  269,  270,
   -1,  272,  273,  274,   -1,   -1,   -1,   33,  125,   -1,
   -1,  257,  258,  259,   40,   -1,  262,   -1,   -1,   45,
   -1,   -1,  268,  269,  270,   -1,  272,  273,  274,  125,
   -1,   -1,   -1,   -1,   -1,   41,   -1,   43,   44,   45,
   -1,   -1,   -1,  271,   -1,   -1,   -1,   -1,  276,  277,
  278,  279,  280,   59,   60,   61,   62,   -1,   -1,   41,
   -1,  125,   44,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   41,   -1,   43,   44,   45,   -1,  271,   59,   60,   61,
   62,  276,  277,  278,  279,  280,   -1,   93,   59,   60,
   61,   62,   41,   -1,   -1,   44,   -1,   -1,  271,  125,
   -1,   -1,   -1,  276,  277,  278,  279,  280,   -1,  271,
   59,   93,   61,   -1,  276,  277,  278,  279,  280,   -1,
   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,   41,   -1,
   -1,   44,   -1,   -1,   -1,   -1,  276,  277,  278,  279,
  257,  258,  259,   -1,   93,  262,   59,   60,   61,   62,
   -1,  268,  269,  270,   -1,  272,  273,  274,   -1,   -1,
   -1,  257,  258,  259,   41,   -1,  262,   44,   -1,   -1,
  271,   -1,  268,  269,  270,   -1,  272,  273,  274,  280,
   93,   -1,   59,   60,   61,   62,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  259,   -1,   -1,  262,   41,
   -1,   -1,   44,   -1,  268,  269,  270,   -1,  272,  273,
  274,   -1,   -1,   -1,   -1,   -1,   93,   59,   60,   61,
   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,  259,   41,   -1,  262,   44,   -1,   -1,
   -1,   -1,  268,  269,  270,   -1,  272,  273,  274,   -1,
   -1,   93,   59,   60,   61,   62,   -1,   -1,   41,   -1,
   -1,   44,   -1,   -1,   -1,  271,   -1,   -1,   -1,   -1,
  276,  277,  278,  279,  280,   -1,   59,   60,   61,   62,
   -1,   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,  271,
   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,   -1,
  271,   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,
   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   54,   -1,   -1,   -1,   58,   59,   -1,   -1,   -1,   -1,
   64,   65,   66,   -1,   -1,   -1,   -1,   -1,  271,   73,
   74,   75,   -1,  276,  277,  278,  279,  280,   -1,   83,
   84,   85,   86,   87,   88,   89,   90,   91,   92,   93,
   94,   95,   96,   -1,   98,   99,   -1,   -1,   -1,   -1,
   -1,   -1,  106,   -1,  271,   -1,   -1,   -1,   -1,  276,
  277,  278,  279,  280,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  138,   -1,   -1,   -1,   -1,  271,
   -1,   -1,   -1,   -1,  276,  277,  278,  279,  280,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  271,   -1,   -1,   -1,   -1,  276,
  277,  278,  279,  280,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  271,   -1,
   -1,   -1,   -1,  276,  277,  278,  279,  280,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"RETURN","WHILE","IF","ELSE",
"MENOSELSE","ID","STRUCT","CHAR","INT","REAL","VAR","WRITE","READ","CAST","AND",
"LITCHAR","LITENT","LITREAL","MENOSUNARIO","IGUALDAD","DESIGUALDAD",
"MENORIGUAL","MAYORIGUAL","OR",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ldef",
"ldef : def",
"ldef : ldef def",
"def : defVar",
"def : defStruct",
"def : defFun",
"defVars : ldefVar",
"defVars :",
"ldefVar : defVar",
"ldefVar : ldefVar defVar",
"defVar : VAR ID ':' tipo ';'",
"tipo : ID",
"tipo : tipoSimple",
"tipo : '[' LITENT ']' tipo",
"tipoSimple : REAL",
"tipoSimple : INT",
"tipoSimple : CHAR",
"tipoRetorno : ':' tipoSimple",
"tipoRetorno :",
"defStruct : STRUCT ID '{' lcamposStruct '}' ';'",
"lcamposStruct : camposStruct",
"lcamposStruct : lcamposStruct camposStruct",
"camposStruct : ID ':' tipo ';'",
"defFun : ID '(' parametros ')' tipoRetorno '{' defVars lsentencia '}'",
"parametros : lparametro",
"parametros :",
"lparametro : parametro",
"lparametro : lparametro ',' parametro",
"parametro : ID ':' tipoSimple",
"lsentencia :",
"lsentencia : lsentencia sentencia",
"sentencia : exp '=' exp ';'",
"sentencia : ID '(' argumentos ')' ';'",
"sentencia : WRITE lexp ';'",
"sentencia : READ exp ';'",
"sentencia : RETURN retorno ';'",
"sentencia : WHILE '(' exp ')' '{' lsentencia '}'",
"sentencia : IF '(' exp ')' '{' lsentencia '}'",
"sentencia : IF '(' exp ')' '{' lsentencia '}' ELSE '{' lsentencia '}'",
"retorno : exp",
"retorno :",
"argumentos : lexp",
"argumentos :",
"lexp : lexp ',' exp",
"lexp : exp",
"exp : '(' exp ')'",
"exp : CAST '<' tipoSimple '>' '(' exp ')'",
"exp : LITCHAR",
"exp : LITENT",
"exp : LITREAL",
"exp : '-' exp",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : exp '%' exp",
"exp : ID",
"exp : exp '.' ID",
"exp : exp '[' exp ']'",
"exp : ID '(' argumentos ')'",
"exp : exp '<' exp",
"exp : exp '>' exp",
"exp : exp MENORIGUAL exp",
"exp : exp MAYORIGUAL exp",
"exp : exp DESIGUALDAD exp",
"exp : exp IGUALDAD exp",
"exp : '!' exp",
"exp : exp AND exp",
"exp : exp OR exp",
};

//#line 188 "..\sinj.y"
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
//#line 547 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 24 "..\sinj.y"
{ raiz = new Programa(val_peek(0)); }
break;
case 2:
//#line 28 "..\sinj.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); definiciones.add((Definicion)val_peek(0)); yyval = definiciones; }
break;
case 3:
//#line 29 "..\sinj.y"
{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>)val_peek(1); definiciones.add((Definicion)val_peek(0)); yyval = definiciones;}
break;
case 4:
//#line 33 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 5:
//#line 34 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 35 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 7:
//#line 39 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 8:
//#line 40 "..\sinj.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); yyval = definiciones; }
break;
case 9:
//#line 44 "..\sinj.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>();
							definiciones.add((Definicion)val_peek(0)); 
							yyval = definiciones; }
break;
case 10:
//#line 47 "..\sinj.y"
{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) val_peek(1);
							definiciones.add((DefVariable)val_peek(0));
							yyval = definiciones; }
break;
case 11:
//#line 53 "..\sinj.y"
{ yyval = new DefVariable(val_peek(3), val_peek(1)); }
break;
case 12:
//#line 57 "..\sinj.y"
{ yyval = new TipoComplejo(val_peek(0)); }
break;
case 13:
//#line 58 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 14:
//#line 59 "..\sinj.y"
{ yyval = new TipoArray(val_peek(2), val_peek(0)); }
break;
case 15:
//#line 63 "..\sinj.y"
{ yyval = new TipoReal(); }
break;
case 16:
//#line 64 "..\sinj.y"
{ yyval = new TipoInt(); }
break;
case 17:
//#line 65 "..\sinj.y"
{ yyval = new TipoChar(); }
break;
case 18:
//#line 69 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 19:
//#line 70 "..\sinj.y"
{ yyval = new TipoVoid();}
break;
case 20:
//#line 74 "..\sinj.y"
{ yyval = new DefVariable(val_peek(4), new TipoStruct(val_peek(2))); }
break;
case 21:
//#line 78 "..\sinj.y"
{ ArrayList<Campo> campos = new ArrayList<Campo>(); campos.add((Campo)val_peek(0)); yyval = campos; }
break;
case 22:
//#line 79 "..\sinj.y"
{ ArrayList<Campo> campos = (ArrayList<Campo>) val_peek(1); campos.add((Campo)val_peek(0)); yyval = campos; }
break;
case 23:
//#line 83 "..\sinj.y"
{ yyval = new Campo(val_peek(3), val_peek(1)); }
break;
case 24:
//#line 87 "..\sinj.y"
{ yyval = new DefVariable(val_peek(8), new TipoFuncion(val_peek(6), val_peek(4), val_peek(2), val_peek(1))); }
break;
case 25:
//#line 91 "..\sinj.y"
{ yyval = (ArrayList<Definicion>)val_peek(0); }
break;
case 26:
//#line 92 "..\sinj.y"
{ yyval = new ArrayList<Definicion>(); }
break;
case 27:
//#line 96 "..\sinj.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); 
													definiciones.add((DefVariable)val_peek(0)); 
													yyval = definiciones; 
												}
break;
case 28:
//#line 100 "..\sinj.y"
{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) val_peek(2);
													definiciones.add((DefVariable)val_peek(0)); 
													yyval = definiciones;
												}
break;
case 29:
//#line 107 "..\sinj.y"
{ yyval = new DefVariable(val_peek(2), val_peek(0)); }
break;
case 30:
//#line 111 "..\sinj.y"
{ yyval = new ArrayList<Sentencia>(); }
break;
case 31:
//#line 112 "..\sinj.y"
{
														List<Sentencia> lista = (ArrayList<Sentencia>) val_peek(1);
														Sentencia sent = (Sentencia)val_peek(0);
														lista.add(sent);
														yyval = lista;
													}
break;
case 32:
//#line 121 "..\sinj.y"
{ yyval = new SentenciaAsignacion( val_peek(3),  val_peek(1)); }
break;
case 33:
//#line 122 "..\sinj.y"
{ yyval = new SentenciaProcedimiento( val_peek(4), val_peek(2)); }
break;
case 34:
//#line 123 "..\sinj.y"
{ yyval = new SentenciaWrite( val_peek(1)); }
break;
case 35:
//#line 124 "..\sinj.y"
{ yyval = new SentenciaRead( val_peek(1)); }
break;
case 36:
//#line 125 "..\sinj.y"
{ yyval = new SentenciaReturn( val_peek(1)); }
break;
case 37:
//#line 126 "..\sinj.y"
{ yyval = new SentenciaWhile( val_peek(4), val_peek(1)); }
break;
case 38:
//#line 127 "..\sinj.y"
{ yyval = new SentenciaIf( val_peek(4), val_peek(1), new ArrayList<Sentencia>()); }
break;
case 39:
//#line 128 "..\sinj.y"
{ yyval = new SentenciaIf( val_peek(8), val_peek(5), val_peek(1)); }
break;
case 40:
//#line 132 "..\sinj.y"
{
														ArrayList<Expresion> retorno = new ArrayList<Expresion>();
														retorno.add((Expresion)val_peek(0));
														yyval = retorno;
													}
break;
case 41:
//#line 137 "..\sinj.y"
{ yyval = new ArrayList<Expresion>(); }
break;
case 42:
//#line 141 "..\sinj.y"
{ yyval = val_peek(0); }
break;
case 43:
//#line 142 "..\sinj.y"
{ yyval = new ArrayList<Expresion>(); }
break;
case 44:
//#line 146 "..\sinj.y"
{
														List<Expresion> expresiones = (ArrayList<Expresion>)val_peek(2);
														Expresion expresion = (Expresion)val_peek(0);
														expresiones.add(expresion);
														yyval = expresiones;
													}
break;
case 45:
//#line 152 "..\sinj.y"
{
														Expresion expresion = (Expresion)val_peek(0);
														List<Expresion> expresiones = new ArrayList<Expresion>();
														expresiones.add(expresion);
														yyval = expresiones;
													}
break;
case 46:
//#line 161 "..\sinj.y"
{ yyval = val_peek(1); }
break;
case 47:
//#line 162 "..\sinj.y"
{ yyval = new ExpresionCast			(val_peek(4),  val_peek(1));}
break;
case 48:
//#line 163 "..\sinj.y"
{ yyval = new ExpresionChar			(new TipoChar(), val_peek(0));}
break;
case 49:
//#line 164 "..\sinj.y"
{ yyval = new ExpresionInt				(new TipoInt(),val_peek(0));}
break;
case 50:
//#line 165 "..\sinj.y"
{ yyval = new ExpresionReal			(new TipoReal(), val_peek(0));}
break;
case 51:
//#line 166 "..\sinj.y"
{ yyval = new ExpresionMenosUnario		(val_peek(0));}
break;
case 52:
//#line 167 "..\sinj.y"
{ yyval = new ExpresionAritmetica		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 53:
//#line 168 "..\sinj.y"
{ yyval = new ExpresionAritmetica		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 54:
//#line 169 "..\sinj.y"
{ yyval = new ExpresionAritmetica		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 55:
//#line 170 "..\sinj.y"
{ yyval = new ExpresionAritmetica		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 56:
//#line 171 "..\sinj.y"
{ yyval = new ExpresionAritmetica		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 57:
//#line 172 "..\sinj.y"
{ yyval = new ExpresionVariable		(val_peek(0)); }
break;
case 58:
//#line 173 "..\sinj.y"
{ yyval = new ExpresionAccesoCampo		(val_peek(2), val_peek(0)); }
break;
case 59:
//#line 174 "..\sinj.y"
{ yyval = new ExpresionAccesoArray		(val_peek(3), val_peek(1)); }
break;
case 60:
//#line 175 "..\sinj.y"
{ yyval = new ExpresionLlamadaFuncion	(val_peek(3), val_peek(1)); }
break;
case 61:
//#line 176 "..\sinj.y"
{ yyval = new ExpresionComparacion		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 62:
//#line 177 "..\sinj.y"
{ yyval = new ExpresionComparacion		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 63:
//#line 178 "..\sinj.y"
{ yyval = new ExpresionComparacion		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 64:
//#line 179 "..\sinj.y"
{ yyval = new ExpresionComparacion		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 65:
//#line 180 "..\sinj.y"
{ yyval = new ExpresionComparacion		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 66:
//#line 181 "..\sinj.y"
{ yyval = new ExpresionComparacion		(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 67:
//#line 182 "..\sinj.y"
{ yyval = new ExpresionNegacion		(val_peek(0)); }
break;
case 68:
//#line 183 "..\sinj.y"
{ yyval = new ExpresionLogica			(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 69:
//#line 184 "..\sinj.y"
{ yyval = new ExpresionLogica			(val_peek(2), val_peek(1), val_peek(0)); }
break;
//#line 1000 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
