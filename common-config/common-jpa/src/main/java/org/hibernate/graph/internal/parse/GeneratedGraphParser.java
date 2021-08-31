// $ANTLR : "graph.g" -> "GeneratedGraphParser.java"$

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.graph.internal.parse;

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
 * Antlr grammar describing the Hibernate EntityGraph Language.
 */
public class GeneratedGraphParser extends antlr.LLkParser       implements HEGLTokenTypes
 {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// semantic actions/callouts

	protected void startAttribute(Token attributeName) {
	}

	protected void startQualifiedAttribute(Token attributeName, Token qualifier) {
	}

	protected void finishAttribute() {
	}

	protected void startSubGraph(Token subType) {
	}

	protected void finishSubGraph() {
	}

protected GeneratedGraphParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public GeneratedGraphParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected GeneratedGraphParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public GeneratedGraphParser(TokenStream lexer) {
  this(lexer,2);
}

public GeneratedGraphParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void graph() throws RecognitionException, TokenStreamException {
		
		traceIn("graph");
		try { // debugging
			
			try {      // for error handling
				attributeNode();
				{
				_loop756:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						attributeNode();
					}
					else {
						break _loop756;
					}
					
				} while (true);
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			}
		} finally { // debugging
			traceOut("graph");
		}
	}
	
	public final void attributeNode() throws RecognitionException, TokenStreamException {
		
		traceIn("attributeNode");
		try { // debugging
			
			try {      // for error handling
				attributePath();
				{
				switch ( LA(1)) {
				case LPAREN:
				{
					subGraph();
					break;
				}
				case EOF:
				case COMMA:
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				finishAttribute();
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
		} finally { // debugging
			traceOut("attributeNode");
		}
	}
	
	public final void attributePath() throws RecognitionException, TokenStreamException {
		
		traceIn("attributePath");
		try { // debugging
			Token  path = null;
			Token  qualifier = null;
			
			try {      // for error handling
				path = LT(1);
				match(NAME);
				{
				switch ( LA(1)) {
				case DOT:
				{
					match(DOT);
					qualifier = LT(1);
					match(NAME);
					break;
				}
				case EOF:
				case COMMA:
				case LPAREN:
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				
						if ( qualifier == null ) {
							startAttribute( path );
						}
						else {
							startQualifiedAttribute( path, qualifier );
						}
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			}
		} finally { // debugging
			traceOut("attributePath");
		}
	}
	
	public final void subGraph() throws RecognitionException, TokenStreamException {
		
		traceIn("subGraph");
		try { // debugging
			Token  subtype = null;
			
			try {      // for error handling
				match(LPAREN);
				{
				if ((LA(1)==NAME) && (LA(2)==COLON)) {
					subtype = LT(1);
					match(NAME);
					match(COLON);
				}
				else if ((LA(1)==NAME) && (_tokenSet_3.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				startSubGraph( subtype );
				attributeNode();
				{
				_loop764:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						attributeNode();
					}
					else {
						break _loop764;
					}
					
				} while (true);
				}
				match(RPAREN);
				
						finishSubGraph();
					
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
		} finally { // debugging
			traceOut("subGraph");
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"COMMA",
		"NAME",
		"DOT",
		"LPAREN",
		"COLON",
		"RPAREN",
		"WHITESPACE",
		"NAME_START",
		"NAME_CONTINUATION"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 530L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 658L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 720L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	
	}
