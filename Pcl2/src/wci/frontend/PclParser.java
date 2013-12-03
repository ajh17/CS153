/* Generated By:JJTree&JavaCC: Do not edit this line. PclParser.java */
package wci.frontend;

import java.util.ArrayList;
import java.io.*;

import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;
import wci.backend.*;
import wci.util.*;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;
import static wci.intermediate.symtabimpl.DefinitionImpl.*;
import static wci.intermediate.symtabimpl.RoutineCodeImpl.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;

public class PclParser/*@bgen(jjtree)*/implements PclParserTreeConstants, PclParserConstants {/*@bgen(jjtree)*/
  protected static JJTPclParserState jjtree = new JJTPclParserState();private static final String SOURCE_SUFFIX = ".pcl";
    private static final String OUTPUT_SUFFIX = ".j";

    private static SymTabStack symTabStack;
    private static SymTabEntry programId;

    public static void main(String[] args)
        throws Exception
    {
        // Create and initialize the symbol table stack.
        symTabStack = SymTabFactory.createSymTabStack();
        Predefined.initialize(symTabStack);

        // Process the source file path which ends in .pcl
        // and create the output file path which ends in .j
        String sourceFilePath = args[0];
        int truncatedLength = sourceFilePath.length() - SOURCE_SUFFIX.length();
        int suffixIndex = sourceFilePath.lastIndexOf(SOURCE_SUFFIX);
        String objectFilePath = (suffixIndex == truncatedLength)
            ? sourceFilePath.substring(0, truncatedLength) + OUTPUT_SUFFIX
            : sourceFilePath + OUTPUT_SUFFIX;

        // Parse a Pcl program.
        Reader    reader = new FileReader(sourceFilePath);
        PclParser parser = new PclParser(reader);
        SimpleNode rootNode = parser.program();

        // Print the cross-reference table.
        CrossReferencer crossReferencer = new CrossReferencer();
        crossReferencer.print(symTabStack);

        // Visit the parse tree nodes to decorate them with type information.
        TypeSetterVisitor typeVisitor = new TypeSetterVisitor();
        rootNode.jjtAccept(typeVisitor, null);

        // Create and initialize the ICode wrapper for the parse tree.
        ICode iCode = ICodeFactory.createICode();
        iCode.setRoot(rootNode);
        programId.setAttribute(ROUTINE_ICODE, iCode);

        // Print the parse tree.
        ParseTreePrinter treePrinter = new ParseTreePrinter(System.out);
        treePrinter.print(symTabStack);

        // Create the compiler backend and generate code.
        Backend backend = BackendFactory.createBackend("compile");
        backend.process(iCode, symTabStack, objectFilePath);
    }

  static final public SimpleNode program() throws ParseException {
                              SimpleNode rootNode;
    try {
      programHeader();
      rootNode = block();
      jj_consume_token(DOT);
      jj_consume_token(0);
            {if (true) return rootNode;}
    } catch (ParseException ex) {
        handleError(ex);
        {if (true) return null;}
    }
    throw new Error("Missing return statement in function");
  }

  static final public void programHeader() throws ParseException {
    jj_consume_token(PROGRAM);
    jj_consume_token(IDENTIFIER);
        programId = symTabStack.enterLocal(token.image);
        programId.setDefinition(DefinitionImpl.PROGRAM);
        programId.setAttribute(ROUTINE_SYMTAB, symTabStack.push());
        symTabStack.setProgramId(programId);
    jj_consume_token(SEMICOLON);
  }

  static final public SimpleNode block() throws ParseException {
                            SimpleNode rootNode;
    variableDeclarations();
    rootNode = compoundStatement();
        {if (true) return rootNode;}
    throw new Error("Missing return statement in function");
  }

  static final public void variableDeclarations() throws ParseException {
                                     ArrayList<SymTabEntry> variableList;
                                         int index = 0;
    jj_consume_token(VAR);
    label_1:
    while (true) {
      jj_consume_token(IDENTIFIER);
            variableList = new ArrayList<SymTabEntry>();
            processVariableDecl(token, index++, variableList);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_2;
        }
        jj_consume_token(COMMA);
        jj_consume_token(IDENTIFIER);
                processVariableDecl(token, index++, variableList);
      }
      jj_consume_token(COLON);
      jj_consume_token(IDENTIFIER);
            SymTabEntry typeId = symTabStack.lookup(token.image);
            typeId.appendLineNumber(token.beginLine);
            TypeSpec type = typeId.getTypeSpec();

            for (SymTabEntry variableId : variableList) {
                variableId.setTypeSpec(type);
            }
      jj_consume_token(SEMICOLON);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
        programId.setAttribute(ROUTINE_LOCALS_COUNT, index);
  }

  static final public void statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      assignmentStatement();
      break;
    case BEGIN:
      compoundStatement();
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public SimpleNode compoundStatement() throws ParseException {
                                  /*@bgen(jjtree) compoundStatement */
  ASTcompoundStatement jjtn000 = new ASTcompoundStatement(JJTCOMPOUNDSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(BEGIN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BEGIN:
      case IDENTIFIER:
        statement();
        label_3:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SEMICOLON:
            ;
            break;
          default:
            jj_la1[3] = jj_gen;
            break label_3;
          }
          jj_consume_token(SEMICOLON);
          statement();
        }
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
      jj_consume_token(END);
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
        {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final public void assignmentStatement() throws ParseException {
                              /*@bgen(jjtree) assignmentStatement */
  ASTassignmentStatement jjtn000 = new ASTassignmentStatement(JJTASSIGNMENTSTATEMENT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      variable();
        SymTabEntry variableId = symTabStack.lookup(token.image);
        variableId.appendLineNumber(token.beginLine);
      jj_consume_token(COLON_EQUALS);
      expression();
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void expression() throws ParseException {
    term();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
              ASTadd jjtn001 = new ASTadd(JJTADD);
              boolean jjtc001 = true;
              jjtree.openNodeScope(jjtn001);
        try {
          term();
        } catch (Throwable jjte001) {
              if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
              } else {
                jjtree.popNode();
              }
              if (jjte001 instanceof RuntimeException) {
                {if (true) throw (RuntimeException)jjte001;}
              }
              if (jjte001 instanceof ParseException) {
                {if (true) throw (ParseException)jjte001;}
              }
              {if (true) throw (Error)jjte001;}
        } finally {
              if (jjtc001) {
                jjtree.closeNodeScope(jjtn001,  2);
              }
        }
        break;
      case MINUS:
        jj_consume_token(MINUS);
              ASTsubtract jjtn002 = new ASTsubtract(JJTSUBTRACT);
              boolean jjtc002 = true;
              jjtree.openNodeScope(jjtn002);
        try {
          term();
        } catch (Throwable jjte002) {
              if (jjtc002) {
                jjtree.clearNodeScope(jjtn002);
                jjtc002 = false;
              } else {
                jjtree.popNode();
              }
              if (jjte002 instanceof RuntimeException) {
                {if (true) throw (RuntimeException)jjte002;}
              }
              if (jjte002 instanceof ParseException) {
                {if (true) throw (ParseException)jjte002;}
              }
              {if (true) throw (Error)jjte002;}
        } finally {
              if (jjtc002) {
                jjtree.closeNodeScope(jjtn002,  2);
              }
        }
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void term() throws ParseException {
    factor();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
      case SLASH:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
        jj_consume_token(STAR);
              ASTmultiply jjtn001 = new ASTmultiply(JJTMULTIPLY);
              boolean jjtc001 = true;
              jjtree.openNodeScope(jjtn001);
        try {
          factor();
        } catch (Throwable jjte001) {
              if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
              } else {
                jjtree.popNode();
              }
              if (jjte001 instanceof RuntimeException) {
                {if (true) throw (RuntimeException)jjte001;}
              }
              if (jjte001 instanceof ParseException) {
                {if (true) throw (ParseException)jjte001;}
              }
              {if (true) throw (Error)jjte001;}
        } finally {
              if (jjtc001) {
                jjtree.closeNodeScope(jjtn001,  2);
              }
        }
        break;
      case SLASH:
        jj_consume_token(SLASH);
              ASTdivide jjtn002 = new ASTdivide(JJTDIVIDE);
              boolean jjtc002 = true;
              jjtree.openNodeScope(jjtn002);
        try {
          factor();
        } catch (Throwable jjte002) {
              if (jjtc002) {
                jjtree.clearNodeScope(jjtn002);
                jjtc002 = false;
              } else {
                jjtree.popNode();
              }
              if (jjte002 instanceof RuntimeException) {
                {if (true) throw (RuntimeException)jjte002;}
              }
              if (jjte002 instanceof ParseException) {
                {if (true) throw (ParseException)jjte002;}
              }
              {if (true) throw (Error)jjte002;}
        } finally {
              if (jjtc002) {
                jjtree.closeNodeScope(jjtn002,  2);
              }
        }
        break;
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void factor() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      variable();
      break;
    case INTEGER:
      integerConstant();
      break;
    case REAL:
      realConstant();
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void variable() throws ParseException {
                   /*@bgen(jjtree) variable */
  ASTvariable jjtn000 = new ASTvariable(JJTVARIABLE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(IDENTIFIER);
                   jjtree.closeNodeScope(jjtn000, true);
                   jjtc000 = false;
        SymTabEntry variableId = symTabStack.lookup(token.image);
        variableId.appendLineNumber(token.beginLine);
        TypeSpec type = variableId.getTypeSpec();
        jjtn000.setTypeSpec(type);
        jjtn000.setAttribute(ID, variableId);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void integerConstant() throws ParseException {
                          /*@bgen(jjtree) integerConstant */
  ASTintegerConstant jjtn000 = new ASTintegerConstant(JJTINTEGERCONSTANT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(INTEGER);
                jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
        jjtn000.setTypeSpec(Predefined.integerType);
        jjtn000.setAttribute(VALUE, Integer.parseInt(token.image));
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void realConstant() throws ParseException {
                       /*@bgen(jjtree) realConstant */
  ASTrealConstant jjtn000 = new ASTrealConstant(JJTREALCONSTANT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(REAL);
             jjtree.closeNodeScope(jjtn000, true);
             jjtc000 = false;
        jjtn000.setTypeSpec(Predefined.realType);
        jjtn000.setAttribute(VALUE, Float.parseFloat(token.image));
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final public void Error() throws ParseException {
    jj_consume_token(ERROR);
  }

  static void processVariableDecl(Token token, int index,
                         ArrayList<SymTabEntry> variableList) throws ParseException {
    SymTabEntry variableId = symTabStack.enterLocal(token.image);
    variableId.setIndex(index);
    variableId.setDefinition(DefinitionImpl.VARIABLE);
    variableId.appendLineNumber(token.beginLine);
    variableList.add(variableId);
  }

  static String handleError(ParseException ex) throws ParseException {
    Token token = ex.currentToken;
    System.out.println(ex.getMessage());

    do {
        token = getNextToken();
    } while (token.kind != SEMICOLON);

        jjtree.popNode();
    return token.image;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public PclParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[10];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x1000,0x100000,0x100040,0x4000,0x100040,0x18000,0x18000,0x60000,0x60000,0xd00000,};
   }

  /** Constructor with InputStream. */
  public PclParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public PclParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new PclParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public PclParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new PclParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public PclParser(PclParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(PclParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[32];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 10; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 32; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}