/* Generated By:JavaCC: Do not edit this line. TurtleParser.java */
package uk.ac.manchester.cs.owl.turtle.parser;

import org.coode.owl.rdfxml.parser.AnonymousNodeChecker;
import org.coode.string.EscapeUtils;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;
import org.semanticweb.owl.vocab.XSDVocabulary;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TurtleParser implements AnonymousNodeChecker, TurtleParserConstants {

    private OWLOntology ontology;

    private OWLDataFactory dataFactory;

    private Map<String, URI> string2URI;

    private Map<String, String> prefix2NamespaceMap;

    private boolean ignoreAnnotationsAndDeclarations = false;

    private String base;

    private int blankNodeId;

    private TripleHandler handler;


    public TurtleParser(Reader reader, TripleHandler handler, String base) {
        this(reader);
        this.handler = handler;
        this.base = base;
        string2URI = new HashMap<String, URI>();
        blankNodeId = 0;
        prefix2NamespaceMap = new HashMap<String, String>();
        prefix2NamespaceMap.put("", "");
        prefix2NamespaceMap.put(null, "");
        prefix2NamespaceMap.put("_", "");
    }

    public TurtleParser(InputStream is, TripleHandler handler, String base) {
        this(is);
        this.handler = handler;
        this.base = base;
        string2URI = new HashMap<String, URI>();
        blankNodeId = 0;
        prefix2NamespaceMap = new HashMap<String, String>();
        prefix2NamespaceMap.put("", "");
        prefix2NamespaceMap.put("_", "");
    }

    public void setTripleHandler(TripleHandler handler) {
        this.handler = handler;
    }

    public boolean isAnonymousNode(String uri) {
        return uri.indexOf("genid") != -1;
    }


    public boolean isAnonymousNode(URI uri) {
        String frag = uri.getFragment();
        return frag != null && frag.indexOf("genid") != -1;
    }

    protected URI getNextBlankNode() {
        URI uri = getURI("genid" + blankNodeId);
        blankNodeId++;
        return uri;
    }

    protected URI getURIFromQName(String qname) {
        int sepIndex = qname.indexOf(':');
        String prefix = "";
        String localName = qname;
        if(sepIndex != -1) {
            prefix = qname.substring(0, sepIndex);
            localName = qname.substring(sepIndex + 1, qname.length());
        }
        String ns = prefix2NamespaceMap.get(prefix);
        return getURI(ns + localName);
    }

    protected void addAxiom(OWLAxiom ax) throws TurtleParserException {
        try {
            ((OWLMutableOntology) ontology).applyChange(new AddAxiom(ontology, ax));
        }
        catch(OWLOntologyChangeException e) {
            throw new TurtleParserException(e);
        }
    }

    public URI getURI(String s) {
         if(s.charAt(0) == '<') {
            s = s.substring(1, s.length() - 1);
        }

        URI uri = string2URI.get(s);
        if(uri == null) {
            try {
                uri = new URI(s);

                if(!uri.isAbsolute()) {
                    String uriString = uri.toString();
                    if(uri.getFragment() != null) {
                        uri = new URI(base + uriString);
                    }
                    else {
                        if(base.endsWith("/")) {
                            uri = new URI(base + uriString);
                        }
                        else {
                            uri = new URI(base + "#" + uriString);
                        }
                    }

                }
                string2URI.put(s, uri);
            }
            catch(URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return uri;
    }

    public void setIgnoreAnnotationsAndDeclarations(boolean b) {
        ignoreAnnotationsAndDeclarations = b;
    }

/////////////////////////////////////////////////////////////////////////////////////////////

//TOKEN:
//{
//    <LONG_STRING: (<QUOTE><QUOTE><QUOTE>~["\""]<QUOTE><QUOTE><QUOTE>)>
//}
  final public void parseDocument() throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PREFIX:
      case BASE:
        parseDirective();
        break;
      case OPENPAR:
      case EMPTY_BLANK_NODE:
      case OPEN_SQUARE_BRACKET:
      case URITOKEN:
      case QNAME:
      case BLANK_NODE:
      case VAR:
        parseStatement();
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OPENPAR:
      case PREFIX:
      case BASE:
      case EMPTY_BLANK_NODE:
      case OPEN_SQUARE_BRACKET:
      case URITOKEN:
      case QNAME:
      case BLANK_NODE:
      case VAR:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(0);
                                                 handler.handleEnd();
  }

  final public void parseDirective() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PREFIX:
      parsePrefixDirective();
      break;
    case BASE:
      parseBaseDirective();
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void parsePrefixDirective() throws ParseException {
    Token t;
    String prefix = "";
    URI ns;
    jj_consume_token(PREFIX);
    t = jj_consume_token(QNAME);
                       prefix=t.image.substring(0, t.image.indexOf(':'));
    ns = parseURIRef();
    jj_consume_token(DOT);
        prefix2NamespaceMap.put(prefix, ns.toString());
        handler.handlePrefixDirective(prefix, ns.toString());
  }

  final public void parseBaseDirective() throws ParseException {
    URI baseURI;
    Token t;
    jj_consume_token(BASE);
    t = jj_consume_token(URITOKEN);
                         base = t.image.substring(1, t.image.length() - 1);
    jj_consume_token(DOT);
        handler.handleBaseDirective(base);
  }

  final public void parseStatement() throws ParseException {
    parseTriples();
  }

  final public void parseTriples() throws ParseException {
    URI subject;
    subject = parseSubject();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case A:
    case URITOKEN:
    case QNAME:
    case VAR:
      parsePredicateObjectList(subject);
      jj_consume_token(DOT);
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
  }

  final public URI parseSubject() throws ParseException {
    URI uri;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case URITOKEN:
    case QNAME:
    case VAR:
      uri = parseResource();
      break;
    case OPENPAR:
    case EMPTY_BLANK_NODE:
    case OPEN_SQUARE_BRACKET:
    case BLANK_NODE:
      uri = parseBlankNode();
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return uri;}
    throw new Error("Missing return statement in function");
  }

  final public URI parseQName() throws ParseException {
    URI uri;
    Token t;
    t = jj_consume_token(QNAME);
        {if (true) return getURIFromQName(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public URI parseURIRef() throws ParseException {
    Token t;
    URI uri;
    t = jj_consume_token(URITOKEN);
                  {if (true) return getURI(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public URI parseBlankNode() throws ParseException {
    URI uri;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BLANK_NODE:
      uri = parseNodeID();
      break;
    case EMPTY_BLANK_NODE:
      jj_consume_token(EMPTY_BLANK_NODE);
                       uri = getNextBlankNode();
      break;
    case OPEN_SQUARE_BRACKET:
      jj_consume_token(OPEN_SQUARE_BRACKET);
                           uri = getNextBlankNode();
      parsePredicateObjectList(uri);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOT:
        jj_consume_token(DOT);
        break;
      default:
        jj_la1[5] = jj_gen;
        ;
      }
      jj_consume_token(CLOSE_SQUARE_BRACKET);
      break;
    case OPENPAR:
      uri = parseCollection();
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return uri;}
    throw new Error("Missing return statement in function");
  }

  final public URI parseNodeID() throws ParseException {
    Token t;
    t = jj_consume_token(BLANK_NODE);
        {if (true) return getURIFromQName(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public void parsePredicateObjectList(URI subject) throws ParseException {
    URI predicate;
    predicate = parseVerb();
    parseObjectList(subject, predicate);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SEMICOLON:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_2;
      }
      jj_consume_token(SEMICOLON);
      predicate = parseVerb();
      parseObjectList(subject, predicate);
    }
  }

  final public URI parseVerb() throws ParseException {
    URI uri;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case A:
      jj_consume_token(A);
         uri = OWLRDFVocabulary.RDF_TYPE.getURI();
      break;
    case URITOKEN:
    case QNAME:
    case VAR:
      uri = parsePredicate();
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return uri;}
    throw new Error("Missing return statement in function");
  }

  final public URI parsePredicate() throws ParseException {
    URI uri;
    uri = parseResource();
        {if (true) return uri;}
    throw new Error("Missing return statement in function");
  }

  final public URI parseResource() throws ParseException {
    URI uri;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case URITOKEN:
      uri = parseURIRef();
      break;
    case QNAME:
      uri = parseQName();
      break;
    case VAR:
      uri = parseVar();
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return uri;}
    throw new Error("Missing return statement in function");
  }

  final public URI parseVar() throws ParseException {
    Token t;
    t = jj_consume_token(VAR);
        {if (true) return getURI(t.image.substring(0, t.image.length()));}
    throw new Error("Missing return statement in function");
  }

  final public void parseObjectList(URI subject, URI predicate) throws ParseException {
    parseObject(subject, predicate);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_3;
      }
      jj_consume_token(COMMA);
      parseObject(subject, predicate);
    }
  }

  final public void parseObject(URI subject, URI predicate) throws ParseException {
    URI resObject;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING:
    case LONG_STRING:
    case DIGIT:
    case INTEGER:
    case DOUBLE:
    case DECIMAL:
    case TRUE:
    case FALSE:
      parseLiteral(subject, predicate);
      break;
    case OPENPAR:
    case EMPTY_BLANK_NODE:
    case OPEN_SQUARE_BRACKET:
    case URITOKEN:
    case QNAME:
    case BLANK_NODE:
    case VAR:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case URITOKEN:
      case QNAME:
      case VAR:
        resObject = parseResource();
        break;
      case OPENPAR:
      case EMPTY_BLANK_NODE:
      case OPEN_SQUARE_BRACKET:
      case BLANK_NODE:
        resObject = parseBlankNode();
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
        handler.handleTriple(subject, predicate, resObject);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public URI parseCollection() throws ParseException {
    URI uri;
    jj_consume_token(OPENPAR);
    uri = parseItemList();
    jj_consume_token(CLOSEPAR);
        {if (true) return uri;}
    throw new Error("Missing return statement in function");
  }

  final public URI parseItemList() throws ParseException {
    //  _x  rdf:type rdf:List
    //  _x  rdf:first
    //  _x  rdf:next
    URI firstSubject = null;
    URI subject = null;
    URI type = OWLRDFVocabulary.RDF_TYPE.getURI();
    URI first = OWLRDFVocabulary.RDF_FIRST.getURI();
    URI rest = OWLRDFVocabulary.RDF_REST.getURI();
    URI list = OWLRDFVocabulary.RDF_LIST.getURI();
    URI nil = OWLRDFVocabulary.RDF_NIL.getURI();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING:
      case LONG_STRING:
      case DIGIT:
      case INTEGER:
      case DOUBLE:
      case DECIMAL:
      case OPENPAR:
      case EMPTY_BLANK_NODE:
      case OPEN_SQUARE_BRACKET:
      case TRUE:
      case FALSE:
      case URITOKEN:
      case QNAME:
      case BLANK_NODE:
      case VAR:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_4;
      }
        URI prevSubject = subject;
        subject=getNextBlankNode();
        if(prevSubject != null) {
            handler.handleTriple(prevSubject, rest, subject);
        }
        else {
            firstSubject = subject;
        }
        handler.handleTriple(subject, type, list);
      parseObject(subject, first);
    }
        // Terminate list
        handler.handleTriple(subject, rest, nil);
        {if (true) return firstSubject;}
    throw new Error("Missing return statement in function");
  }

  final public String parseName() throws ParseException {
    Token t;
    t = jj_consume_token(QNAME);
        {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public void parseLiteral(URI subject, URI predicate) throws ParseException {
    String literal;
    String lang = null;
    URI datatype = null;
    Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING:
    case LONG_STRING:
      literal = parseQuotedString();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOUBLE_CARET:
      case AT:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DOUBLE_CARET:
          jj_consume_token(DOUBLE_CARET);
          datatype = parseResource();
          break;
        case AT:
          jj_consume_token(AT);
          label_5:
          while (true) {
            t = jj_consume_token(NCNAME1);
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case NCNAME1:
              ;
              break;
            default:
              jj_la1[14] = jj_gen;
              break label_5;
            }
          }
                                                                                                    lang=t.image;
          break;
        default:
          jj_la1[15] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[16] = jj_gen;
        ;
      }
            if(datatype != null) {
                handler.handleTriple(subject, predicate, literal, datatype);
            }
            else if(lang != null) {
                handler.handleTriple(subject, predicate, literal, lang);
            }
            else {
                handler.handleTriple(subject, predicate, literal);
            }
      break;
    case DIGIT:
    case INTEGER:
      literal = parseInteger();
                            handler.handleTriple(subject, predicate, literal, XSDVocabulary.INT.getURI());
      break;
    case DOUBLE:
      literal = parseDouble();
                           handler.handleTriple(subject, predicate, literal, XSDVocabulary.DOUBLE.getURI());
      break;
    case DECIMAL:
      literal = parseDecimal();
                            handler.handleTriple(subject, predicate, literal, XSDVocabulary.DECIMAL.getURI());
      break;
    case TRUE:
    case FALSE:
      literal = parseBoolean();
                            handler.handleTriple(subject, predicate, literal, XSDVocabulary.BOOLEAN.getURI());
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public String parseInteger() throws ParseException {
    Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER:
      t = jj_consume_token(INTEGER);
        {if (true) return t.image;}
      break;
    case DIGIT:
      t = jj_consume_token(DIGIT);
        {if (true) return t.image;}
      break;
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public String parseDouble() throws ParseException {
    Token t;
    t = jj_consume_token(DOUBLE);
        {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String parseDecimal() throws ParseException {
    Token t;
    t = jj_consume_token(DECIMAL);
        {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String parseBoolean() throws ParseException {
    Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      t = jj_consume_token(TRUE);
      break;
    case FALSE:
      t = jj_consume_token(FALSE);
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String parseQuotedString() throws ParseException {
    String s;
    s = parseString();
        {if (true) return s;}
    throw new Error("Missing return statement in function");
  }

  final public String parseString() throws ParseException {
    Token t;
    String rawString = "";
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING:
      t = jj_consume_token(STRING);
        rawString = t.image.substring(1, t.image.length() - 1);
      break;
    case LONG_STRING:
      t = jj_consume_token(LONG_STRING);
        rawString = t.image.substring(3, t.image.length() - 3);
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return EscapeUtils.unescapeString(rawString);}
    throw new Error("Missing return statement in function");
  }

  public TurtleParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[21];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_0();
      jj_la1_1();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x65080000,0x65080000,0x5000000,0x10000000,0x60080000,0x400000,0x60080000,0x800000,0x10000000,0x0,0x200000,0x60080000,0x600be200,0x600be200,0x0,0xa000000,0xa000000,0x3e200,0xc000,0x0,0x2200,};
   }
   private static void jj_la1_1() {
      jj_la1_1 = new int[] {0x8d0,0x8d0,0x0,0x850,0x8d0,0x0,0x80,0x0,0x850,0x850,0x0,0x8d0,0x8d6,0x8d6,0x100,0x0,0x0,0x6,0x0,0x6,0x0,};
   }

  public TurtleParser(java.io.InputStream stream) {
     this(stream, null);
  }
  public TurtleParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new TurtleParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public TurtleParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TurtleParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public TurtleParser(TurtleParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public void ReInit(TurtleParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
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

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[45];
    for (int i = 0; i < 45; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 21; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 45; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
