// $ANTLR : "sql-script.g" -> "GeneratedSqlScriptParser.java"$

package org.hibernate.tool.schema.ast;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import org.hibernate.hql.internal.ast.ErrorReporter;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

/**
 * Lexer and parser used to extract single statements from import SQL script. Supports instructions/comments and quoted
 * strings spread over multiple lines. Each statement must end with semicolon.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class GeneratedSqlScriptParser extends antlr.LLkParser       implements GeneratedSqlScriptParserTokenTypes
 {

    protected void out(String stmt) {
    	// by default, nothing to do
    }

    protected void out(Token token) {
    	// by default, nothing to do
    }

    protected void statementStarted() {
    	// by default, nothing to do
    }

    protected void statementEnded() {
    	// by default, nothing to do
    }

     protected void skip() {
        	// by default, nothing to do
     }


protected GeneratedSqlScriptParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public GeneratedSqlScriptParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected GeneratedSqlScriptParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public GeneratedSqlScriptParser(TokenStream lexer) {
  this(lexer,3);
}

public GeneratedSqlScriptParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final void script() throws RecognitionException, TokenStreamException {
		
		traceIn("script");
		try { // debugging
			
			try {      // for error handling
				blankSpacesToSkip();
				{
				_loop795:
				do {
					if ((LA(1)==QUOTED_TEXT||LA(1)==CHAR)) {
						statement();
						blankSpacesToSkip();
					}
					else {
						break _loop795;
					}
					
				} while (true);
				}
				match(Token.EOF_TYPE);
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			}
		} finally { // debugging
			traceOut("script");
		}
	}
	
	public final void blankSpacesToSkip() throws RecognitionException, TokenStreamException {
		
		traceIn("blankSpacesToSkip");
		try { // debugging
			
			try {      // for error handling
				{
				_loop806:
				do {
					switch ( LA(1)) {
					case NEWLINE:
					{
						newLineToSkip();
						break;
					}
					case SPACE:
					{
						spaceToSkip();
						break;
					}
					case TAB:
					{
						tabToSkip();
						break;
					}
					default:
					{
						break _loop806;
					}
					}
				} while (true);
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
		} finally { // debugging
			traceOut("blankSpacesToSkip");
		}
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		traceIn("statement");
		try { // debugging
			
			try {      // for error handling
				statementStarted();
				statementFirstPart();
				{
				_loop800:
				do {
					if ((_tokenSet_2.member(LA(1)))) {
						statementPart();
						{
						_loop799:
						do {
							if ((LA(1)==NEWLINE)) {
								afterStatementPartNewline();
							}
							else {
								break _loop799;
							}
							
						} while (true);
						}
					}
					else {
						break _loop800;
					}
					
				} while (true);
				}
				match(DELIMITER);
				statementEnded();
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			}
		} finally { // debugging
			traceOut("statement");
		}
	}
	
	public final void statementFirstPart() throws RecognitionException, TokenStreamException {
		
		traceIn("statementFirstPart");
		try { // debugging
			
			try {      // for error handling
				switch ( LA(1)) {
				case QUOTED_TEXT:
				{
					quotedString();
					break;
				}
				case CHAR:
				{
					nonSkippedChar();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			}
		} finally { // debugging
			traceOut("statementFirstPart");
		}
	}
	
	public final void statementPart() throws RecognitionException, TokenStreamException {
		
		traceIn("statementPart");
		try { // debugging
			
			try {      // for error handling
				switch ( LA(1)) {
				case QUOTED_TEXT:
				{
					quotedString();
					break;
				}
				case CHAR:
				{
					nonSkippedChar();
					break;
				}
				case SPACE:
				{
					nonSkippedSpace();
					break;
				}
				case TAB:
				{
					nonSkippedTab();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
		} finally { // debugging
			traceOut("statementPart");
		}
	}
	
	public final void afterStatementPartNewline() throws RecognitionException, TokenStreamException {
		
		traceIn("afterStatementPartNewline");
		try { // debugging
			Token  n = null;
			
			try {      // for error handling
				n = LT(1);
				match(NEWLINE);
				
						out( " " );
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
		} finally { // debugging
			traceOut("afterStatementPartNewline");
		}
	}
	
	public final void quotedString() throws RecognitionException, TokenStreamException {
		
		traceIn("quotedString");
		try { // debugging
			Token  q = null;
			
			try {      // for error handling
				q = LT(1);
				match(QUOTED_TEXT);
				
						out( q );
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
		} finally { // debugging
			traceOut("quotedString");
		}
	}
	
	public final void nonSkippedChar() throws RecognitionException, TokenStreamException {
		
		traceIn("nonSkippedChar");
		try { // debugging
			Token  c = null;
			
			try {      // for error handling
				c = LT(1);
				match(CHAR);
				
						out( c );
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
		} finally { // debugging
			traceOut("nonSkippedChar");
		}
	}
	
	public final void nonSkippedSpace() throws RecognitionException, TokenStreamException {
		
		traceIn("nonSkippedSpace");
		try { // debugging
			Token  s = null;
			
			try {      // for error handling
				s = LT(1);
				match(SPACE);
				
						out( s );
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
		} finally { // debugging
			traceOut("nonSkippedSpace");
		}
	}
	
	public final void nonSkippedTab() throws RecognitionException, TokenStreamException {
		
		traceIn("nonSkippedTab");
		try { // debugging
			Token  t = null;
			
			try {      // for error handling
				t = LT(1);
				match(TAB);
				
						out( t );
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
		} finally { // debugging
			traceOut("nonSkippedTab");
		}
	}
	
	public final void newLineToSkip() throws RecognitionException, TokenStreamException {
		
		traceIn("newLineToSkip");
		try { // debugging
			
			try {      // for error handling
				match(NEWLINE);
				
						skip();
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			}
		} finally { // debugging
			traceOut("newLineToSkip");
		}
	}
	
	public final void spaceToSkip() throws RecognitionException, TokenStreamException {
		
		traceIn("spaceToSkip");
		try { // debugging
			
			try {      // for error handling
				match(SPACE);
				
						skip();	
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			}
		} finally { // debugging
			traceOut("spaceToSkip");
		}
	}
	
	public final void tabToSkip() throws RecognitionException, TokenStreamException {
		
		traceIn("tabToSkip");
		try { // debugging
			
			try {      // for error handling
				match(TAB);
				
						skip();	
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			}
		} finally { // debugging
			traceOut("tabToSkip");
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"DELIMITER",
		"QUOTED_TEXT",
		"NEWLINE",
		"SPACE",
		"TAB",
		"CHAR",
		"ESCqs",
		"LINE_COMMENT",
		"BLOCK_COMMENT"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 546L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 928L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 994L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 944L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 1008L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	
	}
