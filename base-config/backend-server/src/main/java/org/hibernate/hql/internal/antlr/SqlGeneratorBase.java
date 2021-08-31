// $ANTLR : "sql-gen.g" -> "SqlGeneratorBase.java"$

package org.hibernate.hql.internal.antlr;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


/**
 * SQL Generator Tree Parser, providing SQL rendering of SQL ASTs produced by the previous phase, HqlSqlWalker.  All
 * syntax decoration such as extra spaces, lack of spaces, extra parens, etc. should be added by this class.
 * <br>
 * This grammar processes the HQL/SQL AST and produces an SQL string.  The intent is to move dialect-specific
 * code into a sub-class that will override some of the methods, just like the other two grammars in this system.
 * @author Joshua Davis (joshua@hibernate.org)
 */
public class SqlGeneratorBase extends antlr.TreeParser       implements SqlTokenTypes
 {


   /** the buffer resulting SQL statement is written to */
	private StringBuilder buf = new StringBuilder();

	private boolean captureExpression = false;
	protected java.util.List<StringBuilder> exprs = new java.util.ArrayList<StringBuilder>(java.util.Arrays.asList(new StringBuilder()));

	protected void out(String s) {
		getStringBuilder().append( s );
	}

	/**
	 * Returns the last character written to the output, or -1 if there isn't one.
	 */
	protected int getLastChar() {
		int len = buf.length();
		if ( len == 0 )
			return -1;
		else
			return buf.charAt( len - 1 );
	}

	/**
	 * Add a aspace if the previous token was not a space or a parenthesis.
	 */
	protected void optionalSpace() {
		// Implemented in the sub-class.
	}

	protected void out(AST n) {
		out(n.getText());
	}

	protected void separator(AST n, String sep) {
		if (n.getNextSibling() != null)
			out(sep);
	}

	protected boolean hasText(AST a) {
		String t = a.getText();
		return t != null && t.length() > 0;
	}

	protected void fromFragmentSeparator(AST a) {
		// moved this impl into the subclass...
	}

	protected void nestedFromFragment(AST d,AST parent) {
		// moved this impl into the subclass...
	}

	protected StringBuilder getStringBuilder() {
		return captureExpression ? exprs.get(exprs.size() - 1) : buf;
	}

	protected void nyi(AST n) {
		throw new UnsupportedOperationException("Unsupported node: " + n);
	}

	protected void beginFunctionTemplate(AST m,AST i) {
		// if template is null we just write the function out as it appears in the hql statement
		out(i);
		out("(");
	}

	protected void endFunctionTemplate(AST m) {
	      out(")");
	}

	protected void betweenFunctionArguments() {
		out( ", " );
	}

	protected void captureExpressionStart() {
	    if ( captureExpression ) {
            exprs.add( new StringBuilder() );
	    } else {
		    captureExpression = true;
		}
	}

	protected void captureExpressionFinish() {
	    // Capturing will only stop when we leave the last capture context
	    if ( exprs.size() == 1 ) {
		    captureExpression = false;
		}
	}

	protected String resetCapture() {
	    StringBuilder sb = exprs.remove( exprs.size() - 1 );
		final String expression = sb.toString();
		if ( exprs.isEmpty() ) {
		    sb.setLength(0);
		    exprs.add( sb );
		}
		return expression;
	}

	/**
	 * Implementation note: This is just a stub. SqlGenerator contains the effective implementation.
	 */
	protected String renderOrderByElement(String expression, String order, String nulls) {
		throw new UnsupportedOperationException("Concrete SQL generator should override this method.");
	}

	protected void renderCollectionSize(AST collectionSizeNode) {
		throw new UnsupportedOperationException( "Concrete SQL generator should override this method." );
	}
public SqlGeneratorBase() {
	tokenNames = _tokenNames;
}

	public final void statement(AST _t) throws RecognitionException {
		
		AST statement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT:
			{
				selectStatement(_t);
				_t = _retTree;
				break;
			}
			case UPDATE:
			{
				updateStatement(_t);
				_t = _retTree;
				break;
			}
			case DELETE:
			{
				deleteStatement(_t);
				_t = _retTree;
				break;
			}
			case INSERT:
			{
				insertStatement(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectStatement(AST _t) throws RecognitionException {
		
		AST selectStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t488 = _t;
			AST tmp1_AST_in = (AST)_t;
			match(_t,SELECT);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("select ");
			}
			selectClause(_t);
			_t = _retTree;
			from(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				AST __t490 = _t;
				AST tmp2_AST_in = (AST)_t;
				match(_t,WHERE);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" where ");
				}
				whereExpr(_t);
				_t = _retTree;
				_t = __t490;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case GROUP:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case GROUP:
			{
				AST __t492 = _t;
				AST tmp3_AST_in = (AST)_t;
				match(_t,GROUP);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" group by ");
				}
				groupExprs(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case HAVING:
				{
					AST __t494 = _t;
					AST tmp4_AST_in = (AST)_t;
					match(_t,HAVING);
					_t = _t.getFirstChild();
					if ( inputState.guessing==0 ) {
						out(" having ");
					}
					booleanExpr(_t,false);
					_t = _retTree;
					_t = __t494;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t492;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ORDER:
			{
				AST __t496 = _t;
				AST tmp5_AST_in = (AST)_t;
				match(_t,ORDER);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" order by ");
				}
				orderExprs(_t);
				_t = _retTree;
				_t = __t496;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t488;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void updateStatement(AST _t) throws RecognitionException {
		
		AST updateStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t498 = _t;
			AST tmp6_AST_in = (AST)_t;
			match(_t,UPDATE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("update ");
			}
			AST __t499 = _t;
			AST tmp7_AST_in = (AST)_t;
			match(_t,FROM);
			_t = _t.getFirstChild();
			fromTable(_t);
			_t = _retTree;
			_t = __t499;
			_t = _t.getNextSibling();
			setClause(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				whereClause(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t498;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void deleteStatement(AST _t) throws RecognitionException {
		
		AST deleteStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t502 = _t;
			AST tmp8_AST_in = (AST)_t;
			match(_t,DELETE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("delete");
			}
			from(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				whereClause(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t502;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void insertStatement(AST _t) throws RecognitionException {
		
		AST insertStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST i = null;
		
		try {      // for error handling
			AST __t505 = _t;
			AST tmp9_AST_in = (AST)_t;
			match(_t,INSERT);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out( "insert " );
			}
			i = (AST)_t;
			match(_t,INTO);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				out( i ); out( " " );
			}
			selectStatement(_t);
			_t = _retTree;
			_t = __t505;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectClause(AST _t) throws RecognitionException {
		
		AST selectClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t536 = _t;
			AST tmp10_AST_in = (AST)_t;
			match(_t,SELECT_CLAUSE);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case DISTINCT:
			{
				distinctOrAll(_t);
				_t = _retTree;
				break;
			}
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case SELECT:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CONSTRUCTOR:
			case CASE2:
			case CAST:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			case NAMED_PARAM:
			case SQL_NODE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			int _cnt539=0;
			_loop539:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_0.member(_t.getType()))) {
					selectColumn(_t);
					_t = _retTree;
				}
				else {
					if ( _cnt539>=1 ) { break _loop539; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt539++;
			} while (true);
			}
			_t = __t536;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void from(AST _t) throws RecognitionException {
		
		AST from_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST f = null;
		
		try {      // for error handling
			AST __t555 = _t;
			f = _t==ASTNULL ? null :(AST)_t;
			match(_t,FROM);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" from ");
			}
			{
			_loop557:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
					fromTable(_t);
					_t = _retTree;
				}
				else {
					break _loop557;
				}
				
			} while (true);
			}
			_t = __t555;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whereExpr(AST _t) throws RecognitionException {
		
		AST whereExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FILTERS:
			{
				filters(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case THETA_JOINS:
				{
					if ( inputState.guessing==0 ) {
						out(" and ");
					}
					thetaJoins(_t);
					_t = _retTree;
					break;
				}
				case 3:
				case AND:
				case BETWEEN:
				case EXISTS:
				case IN:
				case LIKE:
				case NOT:
				case OR:
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case EQ:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case SQL_TOKEN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case AND:
				case BETWEEN:
				case EXISTS:
				case IN:
				case LIKE:
				case NOT:
				case OR:
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case EQ:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case SQL_TOKEN:
				{
					if ( inputState.guessing==0 ) {
						out(" and ");
					}
					booleanExpr(_t, true );
					_t = _retTree;
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				break;
			}
			case THETA_JOINS:
			{
				thetaJoins(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case AND:
				case BETWEEN:
				case EXISTS:
				case IN:
				case LIKE:
				case NOT:
				case OR:
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case EQ:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case SQL_TOKEN:
				{
					if ( inputState.guessing==0 ) {
						out(" and ");
					}
					booleanExpr(_t, true );
					_t = _retTree;
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				break;
			}
			case AND:
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case SQL_TOKEN:
			{
				booleanExpr(_t,false);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void groupExprs(AST _t) throws RecognitionException {
		
		AST groupExprs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			expr(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case ANY:
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case SELECT:
			case SOME:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				if ( inputState.guessing==0 ) {
					out(" , ");
				}
				groupExprs(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case HAVING:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanExpr(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST booleanExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST st = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			case NOT:
			case OR:
			{
				booleanOp(_t, parens );
				_t = _retTree;
				break;
			}
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				comparisonExpr(_t, parens );
				_t = _retTree;
				break;
			}
			case SQL_TOKEN:
			{
				st = (AST)_t;
				match(_t,SQL_TOKEN);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(st);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void orderExprs(AST _t) throws RecognitionException {
		
		AST orderExprs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		AST dir = null;
		String ordExp = null; String ordDir = null; String ordNul = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				captureExpressionStart();
			}
			{
			e = _t==ASTNULL ? null : (AST)_t;
			expr(_t);
			_t = _retTree;
			}
			if ( inputState.guessing==0 ) {
				captureExpressionFinish(); ordExp = resetCapture();
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASCENDING:
			case DESCENDING:
			{
				dir = _t==ASTNULL ? null : (AST)_t;
				orderDirection(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					ordDir = dir.getText();
				}
				break;
			}
			case 3:
			case ALL:
			case ANY:
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case SELECT:
			case SOME:
			case TRUE:
			case NULLS:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NULLS:
			{
				ordNul=nullOrdering(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case ALL:
			case ANY:
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case SELECT:
			case SOME:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				out( e.getType() == SQL_TOKEN && ordDir == null && ordNul == null ? ordExp : renderOrderByElement( ordExp, ordDir, ordNul ) );
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case ANY:
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case SELECT:
			case SOME:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				if ( inputState.guessing==0 ) {
					out(", ");
				}
				orderExprs(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void fromTable(AST _t) throws RecognitionException {
		
		AST fromTable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST a = null;
		AST b = null;
		AST c = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FROM_FRAGMENT:
			{
				AST __t559 = _t;
				a = _t==ASTNULL ? null :(AST)_t;
				match(_t,FROM_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(a);
				}
				{
				_loop561:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
						tableJoin(_t, a );
						_t = _retTree;
					}
					else {
						break _loop561;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					fromFragmentSeparator(a);
				}
				_t = __t559;
				_t = _t.getNextSibling();
				break;
			}
			case JOIN_FRAGMENT:
			{
				AST __t562 = _t;
				b = _t==ASTNULL ? null :(AST)_t;
				match(_t,JOIN_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(b);
				}
				{
				_loop564:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
						tableJoin(_t, b );
						_t = _retTree;
					}
					else {
						break _loop564;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					fromFragmentSeparator(b);
				}
				_t = __t562;
				_t = _t.getNextSibling();
				break;
			}
			case ENTITY_JOIN:
			{
				AST __t565 = _t;
				c = _t==ASTNULL ? null :(AST)_t;
				match(_t,ENTITY_JOIN);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(c);
				}
				{
				_loop567:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
						tableJoin(_t, c );
						_t = _retTree;
					}
					else {
						break _loop567;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					fromFragmentSeparator(c);
				}
				_t = __t565;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void setClause(AST _t) throws RecognitionException {
		
		AST setClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t507 = _t;
			AST tmp11_AST_in = (AST)_t;
			match(_t,SET);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" set ");
			}
			comparisonExpr(_t,false);
			_t = _retTree;
			{
			_loop509:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_1.member(_t.getType()))) {
					if ( inputState.guessing==0 ) {
						out(", ");
					}
					comparisonExpr(_t,false);
					_t = _retTree;
				}
				else {
					break _loop509;
				}
				
			} while (true);
			}
			_t = __t507;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whereClause(AST _t) throws RecognitionException {
		
		AST whereClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t511 = _t;
			AST tmp12_AST_in = (AST)_t;
			match(_t,WHERE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" where ");
			}
			whereClauseExpr(_t);
			_t = _retTree;
			_t = __t511;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void comparisonExpr(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST comparisonExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				binaryComparisonExpression(_t);
				_t = _retTree;
				break;
			}
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			{
				if ( inputState.guessing==0 ) {
					if (parens) out("(");
				}
				exoticComparisonExpression(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					if (parens) out(")");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whereClauseExpr(AST _t) throws RecognitionException {
		
		AST whereClauseExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			boolean synPredMatched514 = false;
			if (_t==null) _t=ASTNULL;
			if (((_t.getType()==SQL_TOKEN))) {
				AST __t514 = _t;
				synPredMatched514 = true;
				inputState.guessing++;
				try {
					{
					AST tmp13_AST_in = (AST)_t;
					match(_t,SQL_TOKEN);
					_t = _t.getNextSibling();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched514 = false;
				}
				_t = __t514;
inputState.guessing--;
			}
			if ( synPredMatched514 ) {
				conditionList(_t);
				_t = _retTree;
			}
			else if ((_tokenSet_2.member(_t.getType()))) {
				booleanExpr(_t, false );
				_t = _retTree;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void conditionList(AST _t) throws RecognitionException {
		
		AST conditionList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			sqlToken(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SQL_TOKEN:
			{
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				conditionList(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void expr(AST _t) throws RecognitionException {
		
		AST expr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				simpleExpr(_t);
				_t = _retTree;
				break;
			}
			case VECTOR_EXPR:
			{
				tupleExpr(_t);
				_t = _retTree;
				break;
			}
			case SELECT:
			{
				parenSelect(_t);
				_t = _retTree;
				break;
			}
			case ANY:
			{
				AST __t613 = _t;
				AST tmp14_AST_in = (AST)_t;
				match(_t,ANY);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("any ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t613;
				_t = _t.getNextSibling();
				break;
			}
			case ALL:
			{
				AST __t614 = _t;
				AST tmp15_AST_in = (AST)_t;
				match(_t,ALL);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("all ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t614;
				_t = _t.getNextSibling();
				break;
			}
			case SOME:
			{
				AST __t615 = _t;
				AST tmp16_AST_in = (AST)_t;
				match(_t,SOME);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("some ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t615;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void orderDirection(AST _t) throws RecognitionException {
		
		AST orderDirection_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASCENDING:
			{
				AST tmp17_AST_in = (AST)_t;
				match(_t,ASCENDING);
				_t = _t.getNextSibling();
				break;
			}
			case DESCENDING:
			{
				AST tmp18_AST_in = (AST)_t;
				match(_t,DESCENDING);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final String  nullOrdering(AST _t) throws RecognitionException {
		String nullOrdExp = null;
		
		AST nullOrdering_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST fl = null;
		
		try {      // for error handling
			AST tmp19_AST_in = (AST)_t;
			match(_t,NULLS);
			_t = _t.getNextSibling();
			fl = _t==ASTNULL ? null : (AST)_t;
			nullPrecedence(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				nullOrdExp = fl.getText();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
		return nullOrdExp;
	}
	
	public final void nullPrecedence(AST _t) throws RecognitionException {
		
		AST nullPrecedence_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FIRST:
			{
				AST tmp20_AST_in = (AST)_t;
				match(_t,FIRST);
				_t = _t.getNextSibling();
				break;
			}
			case LAST:
			{
				AST tmp21_AST_in = (AST)_t;
				match(_t,LAST);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void filters(AST _t) throws RecognitionException {
		
		AST filters_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t530 = _t;
			AST tmp22_AST_in = (AST)_t;
			match(_t,FILTERS);
			_t = _t.getFirstChild();
			conditionList(_t);
			_t = _retTree;
			_t = __t530;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void thetaJoins(AST _t) throws RecognitionException {
		
		AST thetaJoins_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t532 = _t;
			AST tmp23_AST_in = (AST)_t;
			match(_t,THETA_JOINS);
			_t = _t.getFirstChild();
			conditionList(_t);
			_t = _retTree;
			_t = __t532;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void sqlToken(AST _t) throws RecognitionException {
		
		AST sqlToken_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST t = null;
		
		try {      // for error handling
			t = (AST)_t;
			match(_t,SQL_TOKEN);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				out(t);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void distinctOrAll(AST _t) throws RecognitionException {
		
		AST distinctOrAll_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DISTINCT:
			{
				AST tmp24_AST_in = (AST)_t;
				match(_t,DISTINCT);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("distinct ");
				}
				break;
			}
			case ALL:
			{
				AST tmp25_AST_in = (AST)_t;
				match(_t,ALL);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("all ");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectColumn(AST _t) throws RecognitionException {
		
		AST selectColumn_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST p = null;
		AST sc = null;
		
		try {      // for error handling
			p = _t==ASTNULL ? null : (AST)_t;
			selectExpr(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT_COLUMNS:
			{
				sc = (AST)_t;
				match(_t,SELECT_COLUMNS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(sc);
				}
				break;
			}
			case 3:
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case SELECT:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CONSTRUCTOR:
			case CASE2:
			case CAST:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			case NAMED_PARAM:
			case SQL_NODE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				separator( (sc != null) ? sc : p,", ");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectExpr(AST _t) throws RecognitionException {
		
		AST selectExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		AST mcr = null;
		AST c = null;
		AST sn = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			{
				e = _t==ASTNULL ? null : (AST)_t;
				selectAtom(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(e);
				}
				break;
			}
			case KEY:
			case VALUE:
			case ENTRY:
			{
				mcr = _t==ASTNULL ? null : (AST)_t;
				mapComponentReference(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(mcr);
				}
				break;
			}
			case COUNT:
			{
				count(_t);
				_t = _retTree;
				break;
			}
			case CONSTRUCTOR:
			{
				AST __t543 = _t;
				AST tmp26_AST_in = (AST)_t;
				match(_t,CONSTRUCTOR);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case DOT:
				{
					AST tmp27_AST_in = (AST)_t;
					match(_t,DOT);
					_t = _t.getNextSibling();
					break;
				}
				case IDENT:
				{
					AST tmp28_AST_in = (AST)_t;
					match(_t,IDENT);
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				int _cnt546=0;
				_loop546:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_tokenSet_0.member(_t.getType()))) {
						selectColumn(_t);
						_t = _retTree;
					}
					else {
						if ( _cnt546>=1 ) { break _loop546; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt546++;
				} while (true);
				}
				_t = __t543;
				_t = _t.getNextSibling();
				break;
			}
			case CAST:
			case METHOD_CALL:
			case COLL_SIZE:
			{
				methodCall(_t);
				_t = _retTree;
				break;
			}
			case AGGREGATE:
			{
				aggregate(_t);
				_t = _retTree;
				break;
			}
			case FALSE:
			case TRUE:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case QUOTED_STRING:
			case NUM_INT:
			{
				c = _t==ASTNULL ? null : (AST)_t;
				constant(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(c);
				}
				break;
			}
			case CASE:
			case CASE2:
			case UNARY_MINUS:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			{
				arithmeticExpr(_t);
				_t = _retTree;
				break;
			}
			case AND:
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				selectBooleanExpr(_t,false);
				_t = _retTree;
				break;
			}
			case PARAM:
			case NAMED_PARAM:
			{
				parameter(_t);
				_t = _retTree;
				break;
			}
			case SQL_NODE:
			{
				sn = (AST)_t;
				match(_t,SQL_NODE);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(sn);
				}
				break;
			}
			case SELECT:
			{
				if ( inputState.guessing==0 ) {
					out("(");
				}
				selectStatement(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectAtom(AST _t) throws RecognitionException {
		
		AST selectAtom_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			{
				AST tmp29_AST_in = (AST)_t;
				match(_t,DOT);
				_t = _t.getNextSibling();
				break;
			}
			case SQL_TOKEN:
			{
				AST tmp30_AST_in = (AST)_t;
				match(_t,SQL_TOKEN);
				_t = _t.getNextSibling();
				break;
			}
			case ALIAS_REF:
			{
				AST tmp31_AST_in = (AST)_t;
				match(_t,ALIAS_REF);
				_t = _t.getNextSibling();
				break;
			}
			case SELECT_EXPR:
			{
				AST tmp32_AST_in = (AST)_t;
				match(_t,SELECT_EXPR);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void mapComponentReference(AST _t) throws RecognitionException {
		
		AST mapComponentReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case KEY:
			{
				AST tmp33_AST_in = (AST)_t;
				match(_t,KEY);
				_t = _t.getNextSibling();
				break;
			}
			case VALUE:
			{
				AST tmp34_AST_in = (AST)_t;
				match(_t,VALUE);
				_t = _t.getNextSibling();
				break;
			}
			case ENTRY:
			{
				AST tmp35_AST_in = (AST)_t;
				match(_t,ENTRY);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void count(AST _t) throws RecognitionException {
		
		AST count_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t548 = _t;
			AST tmp36_AST_in = (AST)_t;
			match(_t,COUNT);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("count(");
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case DISTINCT:
			{
				distinctOrAll(_t);
				_t = _retTree;
				break;
			}
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case ROW_STAR:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			countExpr(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				out(")");
			}
			_t = __t548;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void methodCall(AST _t) throws RecognitionException {
		
		AST methodCall_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST m = null;
		AST i = null;
		AST c = null;
		AST cs = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case METHOD_CALL:
			{
				AST __t656 = _t;
				m = _t==ASTNULL ? null :(AST)_t;
				match(_t,METHOD_CALL);
				_t = _t.getFirstChild();
				i = (AST)_t;
				match(_t,METHOD_NAME);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					beginFunctionTemplate(m,i);
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case EXPR_LIST:
				{
					AST __t658 = _t;
					AST tmp37_AST_in = (AST)_t;
					match(_t,EXPR_LIST);
					_t = _t.getFirstChild();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ALL:
					case ANY:
					case AND:
					case BETWEEN:
					case COUNT:
					case DOT:
					case EXISTS:
					case FALSE:
					case IN:
					case LIKE:
					case NOT:
					case NULL:
					case OR:
					case SELECT:
					case SOME:
					case TRUE:
					case CASE:
					case KEY:
					case VALUE:
					case ENTRY:
					case AGGREGATE:
					case CASE2:
					case CAST:
					case INDEX_OP:
					case IS_NOT_NULL:
					case IS_NULL:
					case METHOD_CALL:
					case NOT_BETWEEN:
					case NOT_IN:
					case NOT_LIKE:
					case COLL_SIZE:
					case UNARY_MINUS:
					case VECTOR_EXPR:
					case CONSTANT:
					case NUM_DOUBLE:
					case NUM_FLOAT:
					case NUM_LONG:
					case NUM_BIG_INTEGER:
					case NUM_BIG_DECIMAL:
					case JAVA_CONSTANT:
					case EQ:
					case IDENT:
					case NE:
					case LT:
					case GT:
					case LE:
					case GE:
					case PLUS:
					case MINUS:
					case STAR:
					case DIV:
					case MOD:
					case QUOTED_STRING:
					case PARAM:
					case NUM_INT:
					case ALIAS_REF:
					case SQL_TOKEN:
					case NAMED_PARAM:
					case RESULT_VARIABLE_REF:
					{
						arguments(_t);
						_t = _retTree;
						break;
					}
					case 3:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					_t = __t658;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				if ( inputState.guessing==0 ) {
					endFunctionTemplate(m);
				}
				_t = __t656;
				_t = _t.getNextSibling();
				break;
			}
			case CAST:
			{
				AST __t660 = _t;
				c = _t==ASTNULL ? null :(AST)_t;
				match(_t,CAST);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					beginFunctionTemplate(c,c);
				}
				castExpression(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					betweenFunctionArguments();
				}
				castTargetType(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					endFunctionTemplate(c);
				}
				_t = __t660;
				_t = _t.getNextSibling();
				break;
			}
			case COLL_SIZE:
			{
				cs = (AST)_t;
				match(_t,COLL_SIZE);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
							renderCollectionSize( cs );
						
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void aggregate(AST _t) throws RecognitionException {
		
		AST aggregate_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST a = null;
		
		try {      // for error handling
			AST __t654 = _t;
			a = _t==ASTNULL ? null :(AST)_t;
			match(_t,AGGREGATE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				beginFunctionTemplate( a, a );
			}
			expr(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				endFunctionTemplate( a );
			}
			_t = __t654;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void constant(AST _t) throws RecognitionException {
		
		AST constant_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NUM_DOUBLE:
			{
				AST tmp38_AST_in = (AST)_t;
				match(_t,NUM_DOUBLE);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_FLOAT:
			{
				AST tmp39_AST_in = (AST)_t;
				match(_t,NUM_FLOAT);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_INT:
			{
				AST tmp40_AST_in = (AST)_t;
				match(_t,NUM_INT);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_LONG:
			{
				AST tmp41_AST_in = (AST)_t;
				match(_t,NUM_LONG);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_BIG_INTEGER:
			{
				AST tmp42_AST_in = (AST)_t;
				match(_t,NUM_BIG_INTEGER);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_BIG_DECIMAL:
			{
				AST tmp43_AST_in = (AST)_t;
				match(_t,NUM_BIG_DECIMAL);
				_t = _t.getNextSibling();
				break;
			}
			case QUOTED_STRING:
			{
				AST tmp44_AST_in = (AST)_t;
				match(_t,QUOTED_STRING);
				_t = _t.getNextSibling();
				break;
			}
			case CONSTANT:
			{
				AST tmp45_AST_in = (AST)_t;
				match(_t,CONSTANT);
				_t = _t.getNextSibling();
				break;
			}
			case JAVA_CONSTANT:
			{
				AST tmp46_AST_in = (AST)_t;
				match(_t,JAVA_CONSTANT);
				_t = _t.getNextSibling();
				break;
			}
			case TRUE:
			{
				AST tmp47_AST_in = (AST)_t;
				match(_t,TRUE);
				_t = _t.getNextSibling();
				break;
			}
			case FALSE:
			{
				AST tmp48_AST_in = (AST)_t;
				match(_t,FALSE);
				_t = _t.getNextSibling();
				break;
			}
			case IDENT:
			{
				AST tmp49_AST_in = (AST)_t;
				match(_t,IDENT);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arithmeticExpr(AST _t) throws RecognitionException {
		
		AST arithmeticExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PLUS:
			case MINUS:
			{
				additiveExpr(_t);
				_t = _retTree;
				break;
			}
			case STAR:
			case DIV:
			case MOD:
			{
				multiplicativeExpr(_t);
				_t = _retTree;
				break;
			}
			case UNARY_MINUS:
			{
				AST __t626 = _t;
				AST tmp50_AST_in = (AST)_t;
				match(_t,UNARY_MINUS);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("-");
				}
				nestedExprAfterMinusDiv(_t);
				_t = _retTree;
				_t = __t626;
				_t = _t.getNextSibling();
				break;
			}
			case CASE:
			case CASE2:
			{
				caseExpr(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectBooleanExpr(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST selectBooleanExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			case NOT:
			case OR:
			{
				booleanOp(_t, parens );
				_t = _retTree;
				break;
			}
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				comparisonExpr(_t, parens );
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void parameter(AST _t) throws RecognitionException {
		
		AST parameter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		AST p = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NAMED_PARAM:
			{
				n = (AST)_t;
				match(_t,NAMED_PARAM);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(n);
				}
				break;
			}
			case PARAM:
			{
				p = (AST)_t;
				match(_t,PARAM);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(p);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void countExpr(AST _t) throws RecognitionException {
		
		AST countExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ROW_STAR:
			{
				AST tmp51_AST_in = (AST)_t;
				match(_t,ROW_STAR);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("*");
				}
				break;
			}
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				simpleExpr(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void simpleExpr(AST _t) throws RecognitionException {
		
		AST simpleExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST c = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FALSE:
			case TRUE:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case QUOTED_STRING:
			case NUM_INT:
			{
				c = _t==ASTNULL ? null : (AST)_t;
				constant(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(c);
				}
				break;
			}
			case NULL:
			{
				AST tmp52_AST_in = (AST)_t;
				match(_t,NULL);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("null");
				}
				break;
			}
			case DOT:
			case KEY:
			case VALUE:
			case ENTRY:
			case INDEX_OP:
			case ALIAS_REF:
			case RESULT_VARIABLE_REF:
			{
				addrExpr(_t);
				_t = _retTree;
				break;
			}
			case SQL_TOKEN:
			{
				sqlToken(_t);
				_t = _retTree;
				break;
			}
			case AGGREGATE:
			{
				aggregate(_t);
				_t = _retTree;
				break;
			}
			case CAST:
			case METHOD_CALL:
			case COLL_SIZE:
			{
				methodCall(_t);
				_t = _retTree;
				break;
			}
			case COUNT:
			{
				count(_t);
				_t = _retTree;
				break;
			}
			case PARAM:
			case NAMED_PARAM:
			{
				parameter(_t);
				_t = _retTree;
				break;
			}
			case CASE:
			case CASE2:
			case UNARY_MINUS:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			{
				arithmeticExpr(_t);
				_t = _retTree;
				break;
			}
			case AND:
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				selectBooleanExpr(_t,false);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void tableJoin(AST _t,
		 AST parent 
	) throws RecognitionException {
		
		AST tableJoin_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST d = null;
		AST e = null;
		AST f = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case JOIN_FRAGMENT:
			{
				AST __t569 = _t;
				d = _t==ASTNULL ? null :(AST)_t;
				match(_t,JOIN_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" "); out(d);
				}
				{
				_loop571:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
						tableJoin(_t, d );
						_t = _retTree;
					}
					else {
						break _loop571;
					}
					
				} while (true);
				}
				_t = __t569;
				_t = _t.getNextSibling();
				break;
			}
			case FROM_FRAGMENT:
			{
				AST __t572 = _t;
				e = _t==ASTNULL ? null :(AST)_t;
				match(_t,FROM_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					nestedFromFragment(e,parent);
				}
				{
				_loop574:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
						tableJoin(_t, e );
						_t = _retTree;
					}
					else {
						break _loop574;
					}
					
				} while (true);
				}
				_t = __t572;
				_t = _t.getNextSibling();
				break;
			}
			case ENTITY_JOIN:
			{
				AST __t575 = _t;
				f = _t==ASTNULL ? null :(AST)_t;
				match(_t,ENTITY_JOIN);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" "); out(f);
				}
				{
				_loop577:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT||_t.getType()==ENTITY_JOIN)) {
						tableJoin(_t, f );
						_t = _retTree;
					}
					else {
						break _loop577;
					}
					
				} while (true);
				}
				_t = __t575;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanOp(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST booleanOp_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			{
				AST __t579 = _t;
				AST tmp53_AST_in = (AST)_t;
				match(_t,AND);
				_t = _t.getFirstChild();
				booleanExpr(_t,true);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				booleanExpr(_t,true);
				_t = _retTree;
				_t = __t579;
				_t = _t.getNextSibling();
				break;
			}
			case OR:
			{
				AST __t580 = _t;
				AST tmp54_AST_in = (AST)_t;
				match(_t,OR);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					if (parens) out("(");
				}
				booleanExpr(_t,false);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" or ");
				}
				booleanExpr(_t,false);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					if (parens) out(")");
				}
				_t = __t580;
				_t = _t.getNextSibling();
				break;
			}
			case NOT:
			{
				AST __t581 = _t;
				AST tmp55_AST_in = (AST)_t;
				match(_t,NOT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" not (");
				}
				booleanExpr(_t,false);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
				_t = __t581;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void binaryComparisonExpression(AST _t) throws RecognitionException {
		
		AST binaryComparisonExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EQ:
			{
				AST __t586 = _t;
				AST tmp56_AST_in = (AST)_t;
				match(_t,EQ);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("=");
				}
				expr(_t);
				_t = _retTree;
				_t = __t586;
				_t = _t.getNextSibling();
				break;
			}
			case NE:
			{
				AST __t587 = _t;
				AST tmp57_AST_in = (AST)_t;
				match(_t,NE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("<>");
				}
				expr(_t);
				_t = _retTree;
				_t = __t587;
				_t = _t.getNextSibling();
				break;
			}
			case GT:
			{
				AST __t588 = _t;
				AST tmp58_AST_in = (AST)_t;
				match(_t,GT);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(">");
				}
				expr(_t);
				_t = _retTree;
				_t = __t588;
				_t = _t.getNextSibling();
				break;
			}
			case GE:
			{
				AST __t589 = _t;
				AST tmp59_AST_in = (AST)_t;
				match(_t,GE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(">=");
				}
				expr(_t);
				_t = _retTree;
				_t = __t589;
				_t = _t.getNextSibling();
				break;
			}
			case LT:
			{
				AST __t590 = _t;
				AST tmp60_AST_in = (AST)_t;
				match(_t,LT);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("<");
				}
				expr(_t);
				_t = _retTree;
				_t = __t590;
				_t = _t.getNextSibling();
				break;
			}
			case LE:
			{
				AST __t591 = _t;
				AST tmp61_AST_in = (AST)_t;
				match(_t,LE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("<=");
				}
				expr(_t);
				_t = _retTree;
				_t = __t591;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void exoticComparisonExpression(AST _t) throws RecognitionException {
		
		AST exoticComparisonExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LIKE:
			{
				AST __t593 = _t;
				AST tmp62_AST_in = (AST)_t;
				match(_t,LIKE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" like ");
				}
				expr(_t);
				_t = _retTree;
				likeEscape(_t);
				_t = _retTree;
				_t = __t593;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_LIKE:
			{
				AST __t594 = _t;
				AST tmp63_AST_in = (AST)_t;
				match(_t,NOT_LIKE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" not like ");
				}
				expr(_t);
				_t = _retTree;
				likeEscape(_t);
				_t = _retTree;
				_t = __t594;
				_t = _t.getNextSibling();
				break;
			}
			case BETWEEN:
			{
				AST __t595 = _t;
				AST tmp64_AST_in = (AST)_t;
				match(_t,BETWEEN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" between ");
				}
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				expr(_t);
				_t = _retTree;
				_t = __t595;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_BETWEEN:
			{
				AST __t596 = _t;
				AST tmp65_AST_in = (AST)_t;
				match(_t,NOT_BETWEEN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" not between ");
				}
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				expr(_t);
				_t = _retTree;
				_t = __t596;
				_t = _t.getNextSibling();
				break;
			}
			case IN:
			{
				AST __t597 = _t;
				AST tmp66_AST_in = (AST)_t;
				match(_t,IN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" in");
				}
				inList(_t);
				_t = _retTree;
				_t = __t597;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_IN:
			{
				AST __t598 = _t;
				AST tmp67_AST_in = (AST)_t;
				match(_t,NOT_IN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" not in ");
				}
				inList(_t);
				_t = _retTree;
				_t = __t598;
				_t = _t.getNextSibling();
				break;
			}
			case EXISTS:
			{
				AST __t599 = _t;
				AST tmp68_AST_in = (AST)_t;
				match(_t,EXISTS);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					optionalSpace(); out("exists ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t599;
				_t = _t.getNextSibling();
				break;
			}
			case IS_NULL:
			{
				AST __t600 = _t;
				AST tmp69_AST_in = (AST)_t;
				match(_t,IS_NULL);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				_t = __t600;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(" is null");
				}
				break;
			}
			case IS_NOT_NULL:
			{
				AST __t601 = _t;
				AST tmp70_AST_in = (AST)_t;
				match(_t,IS_NOT_NULL);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				_t = __t601;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(" is not null");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void likeEscape(AST _t) throws RecognitionException {
		
		AST likeEscape_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ESCAPE:
			{
				AST __t604 = _t;
				AST tmp71_AST_in = (AST)_t;
				match(_t,ESCAPE);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" escape ");
				}
				expr(_t);
				_t = _retTree;
				_t = __t604;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void inList(AST _t) throws RecognitionException {
		
		AST inList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t606 = _t;
			AST tmp72_AST_in = (AST)_t;
			match(_t,IN_LIST);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" ");
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT:
			{
				parenSelect(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				simpleExprList(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t606;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void quantified(AST _t) throws RecognitionException {
		
		AST quantified_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				out("(");
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SQL_TOKEN:
			{
				sqlToken(_t);
				_t = _retTree;
				break;
			}
			case SELECT:
			{
				selectStatement(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				out(")");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void parenSelect(AST _t) throws RecognitionException {
		
		AST parenSelect_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				out("(");
			}
			selectStatement(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				out(")");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void simpleExprList(AST _t) throws RecognitionException {
		
		AST simpleExprList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				out("(");
			}
			{
			_loop610:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_3.member(_t.getType()))) {
					e = _t==ASTNULL ? null : (AST)_t;
					simpleOrTupleExpr(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						separator(e," , ");
					}
				}
				else {
					break _loop610;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				out(")");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void simpleOrTupleExpr(AST _t) throws RecognitionException {
		
		AST simpleOrTupleExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case NULL:
			case OR:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			case RESULT_VARIABLE_REF:
			{
				simpleExpr(_t);
				_t = _retTree;
				break;
			}
			case VECTOR_EXPR:
			{
				tupleExpr(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void tupleExpr(AST _t) throws RecognitionException {
		
		AST tupleExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		
		try {      // for error handling
			AST __t617 = _t;
			AST tmp73_AST_in = (AST)_t;
			match(_t,VECTOR_EXPR);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("(");
			}
			{
			_loop619:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_4.member(_t.getType()))) {
					e = _t==ASTNULL ? null : (AST)_t;
					expr(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						separator(e," , ");
					}
				}
				else {
					break _loop619;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				out(")");
			}
			_t = __t617;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void addrExpr(AST _t) throws RecognitionException {
		
		AST addrExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST r = null;
		AST i = null;
		AST j = null;
		AST v = null;
		AST mcr = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			{
				AST __t668 = _t;
				r = _t==ASTNULL ? null :(AST)_t;
				match(_t,DOT);
				_t = _t.getFirstChild();
				AST tmp74_AST_in = (AST)_t;
				if ( _t==null ) throw new MismatchedTokenException();
				_t = _t.getNextSibling();
				AST tmp75_AST_in = (AST)_t;
				if ( _t==null ) throw new MismatchedTokenException();
				_t = _t.getNextSibling();
				_t = __t668;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(r);
				}
				break;
			}
			case ALIAS_REF:
			{
				i = (AST)_t;
				match(_t,ALIAS_REF);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(i);
				}
				break;
			}
			case INDEX_OP:
			{
				j = (AST)_t;
				match(_t,INDEX_OP);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(j);
				}
				break;
			}
			case RESULT_VARIABLE_REF:
			{
				v = (AST)_t;
				match(_t,RESULT_VARIABLE_REF);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(v);
				}
				break;
			}
			case KEY:
			case VALUE:
			case ENTRY:
			{
				mcr = _t==ASTNULL ? null : (AST)_t;
				mapComponentReference(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(mcr);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void additiveExpr(AST _t) throws RecognitionException {
		
		AST additiveExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PLUS:
			{
				AST __t628 = _t;
				AST tmp76_AST_in = (AST)_t;
				match(_t,PLUS);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("+");
				}
				expr(_t);
				_t = _retTree;
				_t = __t628;
				_t = _t.getNextSibling();
				break;
			}
			case MINUS:
			{
				AST __t629 = _t;
				AST tmp77_AST_in = (AST)_t;
				match(_t,MINUS);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("-");
				}
				nestedExprAfterMinusDiv(_t);
				_t = _retTree;
				_t = __t629;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void multiplicativeExpr(AST _t) throws RecognitionException {
		
		AST multiplicativeExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STAR:
			{
				AST __t631 = _t;
				AST tmp78_AST_in = (AST)_t;
				match(_t,STAR);
				_t = _t.getFirstChild();
				nestedExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("*");
				}
				nestedExpr(_t);
				_t = _retTree;
				_t = __t631;
				_t = _t.getNextSibling();
				break;
			}
			case DIV:
			{
				AST __t632 = _t;
				AST tmp79_AST_in = (AST)_t;
				match(_t,DIV);
				_t = _t.getFirstChild();
				nestedExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("/");
				}
				nestedExprAfterMinusDiv(_t);
				_t = _retTree;
				_t = __t632;
				_t = _t.getNextSibling();
				break;
			}
			case MOD:
			{
				AST __t633 = _t;
				AST tmp80_AST_in = (AST)_t;
				match(_t,MOD);
				_t = _t.getFirstChild();
				nestedExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" % ");
				}
				nestedExprAfterMinusDiv(_t);
				_t = _retTree;
				_t = __t633;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nestedExprAfterMinusDiv(AST _t) throws RecognitionException {
		
		AST nestedExprAfterMinusDiv_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			boolean synPredMatched639 = false;
			if (_t==null) _t=ASTNULL;
			if (((_tokenSet_5.member(_t.getType())))) {
				AST __t639 = _t;
				synPredMatched639 = true;
				inputState.guessing++;
				try {
					{
					arithmeticExpr(_t);
					_t = _retTree;
					}
				}
				catch (RecognitionException pe) {
					synPredMatched639 = false;
				}
				_t = __t639;
inputState.guessing--;
			}
			if ( synPredMatched639 ) {
				if ( inputState.guessing==0 ) {
					out("(");
				}
				arithmeticExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
			}
			else if ((_tokenSet_4.member(_t.getType()))) {
				expr(_t);
				_t = _retTree;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void caseExpr(AST _t) throws RecognitionException {
		
		AST caseExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CASE:
			{
				AST __t641 = _t;
				AST tmp81_AST_in = (AST)_t;
				match(_t,CASE);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("case");
				}
				{
				int _cnt644=0;
				_loop644:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==WHEN)) {
						AST __t643 = _t;
						AST tmp82_AST_in = (AST)_t;
						match(_t,WHEN);
						_t = _t.getFirstChild();
						if ( inputState.guessing==0 ) {
							out( " when ");
						}
						booleanExpr(_t,false);
						_t = _retTree;
						if ( inputState.guessing==0 ) {
							out(" then ");
						}
						expr(_t);
						_t = _retTree;
						_t = __t643;
						_t = _t.getNextSibling();
					}
					else {
						if ( _cnt644>=1 ) { break _loop644; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt644++;
				} while (true);
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ELSE:
				{
					AST __t646 = _t;
					AST tmp83_AST_in = (AST)_t;
					match(_t,ELSE);
					_t = _t.getFirstChild();
					if ( inputState.guessing==0 ) {
						out(" else ");
					}
					expr(_t);
					_t = _retTree;
					_t = __t646;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				if ( inputState.guessing==0 ) {
					out(" end");
				}
				_t = __t641;
				_t = _t.getNextSibling();
				break;
			}
			case CASE2:
			{
				AST __t647 = _t;
				AST tmp84_AST_in = (AST)_t;
				match(_t,CASE2);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("case ");
				}
				expr(_t);
				_t = _retTree;
				{
				int _cnt650=0;
				_loop650:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==WHEN)) {
						AST __t649 = _t;
						AST tmp85_AST_in = (AST)_t;
						match(_t,WHEN);
						_t = _t.getFirstChild();
						if ( inputState.guessing==0 ) {
							out( " when ");
						}
						expr(_t);
						_t = _retTree;
						if ( inputState.guessing==0 ) {
							out(" then ");
						}
						expr(_t);
						_t = _retTree;
						_t = __t649;
						_t = _t.getNextSibling();
					}
					else {
						if ( _cnt650>=1 ) { break _loop650; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt650++;
				} while (true);
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ELSE:
				{
					AST __t652 = _t;
					AST tmp86_AST_in = (AST)_t;
					match(_t,ELSE);
					_t = _t.getFirstChild();
					if ( inputState.guessing==0 ) {
						out(" else ");
					}
					expr(_t);
					_t = _retTree;
					_t = __t652;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				if ( inputState.guessing==0 ) {
					out(" end");
				}
				_t = __t647;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nestedExpr(AST _t) throws RecognitionException {
		
		AST nestedExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			boolean synPredMatched636 = false;
			if (_t==null) _t=ASTNULL;
			if (((_t.getType()==PLUS||_t.getType()==MINUS))) {
				AST __t636 = _t;
				synPredMatched636 = true;
				inputState.guessing++;
				try {
					{
					additiveExpr(_t);
					_t = _retTree;
					}
				}
				catch (RecognitionException pe) {
					synPredMatched636 = false;
				}
				_t = __t636;
inputState.guessing--;
			}
			if ( synPredMatched636 ) {
				if ( inputState.guessing==0 ) {
					out("(");
				}
				additiveExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
			}
			else if ((_tokenSet_4.member(_t.getType()))) {
				expr(_t);
				_t = _retTree;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arguments(AST _t) throws RecognitionException {
		
		AST arguments_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			expr(_t);
			_t = _retTree;
			{
			_loop663:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_4.member(_t.getType()))) {
					if ( inputState.guessing==0 ) {
						betweenFunctionArguments();
					}
					expr(_t);
					_t = _retTree;
				}
				else {
					break _loop663;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void castExpression(AST _t) throws RecognitionException {
		
		AST castExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			case BETWEEN:
			case COUNT:
			case DOT:
			case EXISTS:
			case FALSE:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case SELECT:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CONSTRUCTOR:
			case CASE2:
			case CAST:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case COLL_SIZE:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case PARAM:
			case NUM_INT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			case NAMED_PARAM:
			case SQL_NODE:
			{
				selectExpr(_t);
				_t = _retTree;
				break;
			}
			case NULL:
			{
				AST tmp87_AST_in = (AST)_t;
				match(_t,NULL);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("null");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void castTargetType(AST _t) throws RecognitionException {
		
		AST castTargetType_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST i = null;
		
		try {      // for error handling
			i = (AST)_t;
			match(_t,IDENT);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				out(i);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"all\"",
		"\"any\"",
		"\"and\"",
		"\"as\"",
		"\"asc\"",
		"\"avg\"",
		"\"between\"",
		"\"class\"",
		"\"count\"",
		"\"delete\"",
		"\"desc\"",
		"DOT",
		"\"distinct\"",
		"\"elements\"",
		"\"escape\"",
		"\"exists\"",
		"\"false\"",
		"\"fetch\"",
		"\"from\"",
		"\"full\"",
		"\"group\"",
		"\"having\"",
		"\"in\"",
		"\"indices\"",
		"\"inner\"",
		"\"insert\"",
		"\"into\"",
		"\"is\"",
		"\"join\"",
		"\"left\"",
		"\"like\"",
		"\"max\"",
		"\"min\"",
		"\"new\"",
		"\"not\"",
		"\"null\"",
		"\"or\"",
		"\"order\"",
		"\"outer\"",
		"\"properties\"",
		"\"right\"",
		"\"select\"",
		"\"set\"",
		"\"some\"",
		"\"sum\"",
		"\"true\"",
		"\"update\"",
		"\"versioned\"",
		"\"where\"",
		"\"nulls\"",
		"FIRST",
		"LAST",
		"\"case\"",
		"\"end\"",
		"\"else\"",
		"\"then\"",
		"\"when\"",
		"\"on\"",
		"\"with\"",
		"\"both\"",
		"\"empty\"",
		"\"leading\"",
		"\"member\"",
		"\"object\"",
		"\"of\"",
		"\"trailing\"",
		"KEY",
		"VALUE",
		"ENTRY",
		"AGGREGATE",
		"ALIAS",
		"CONSTRUCTOR",
		"CASE2",
		"CAST",
		"COLL_PATH",
		"EXPR_LIST",
		"FILTER_ENTITY",
		"IN_LIST",
		"INDEX_OP",
		"IS_NOT_NULL",
		"IS_NULL",
		"METHOD_CALL",
		"NOT_BETWEEN",
		"NOT_IN",
		"NOT_LIKE",
		"ORDER_ELEMENT",
		"QUERY",
		"RANGE",
		"ROW_STAR",
		"SELECT_FROM",
		"COLL_SIZE",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"VECTOR_EXPR",
		"WEIRD_IDENT",
		"CONSTANT",
		"NUM_DOUBLE",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_BIG_INTEGER",
		"NUM_BIG_DECIMAL",
		"JAVA_CONSTANT",
		"COMMA",
		"EQ",
		"OPEN",
		"CLOSE",
		"IDENT",
		"\"by\"",
		"\"ascending\"",
		"\"descending\"",
		"NE",
		"SQL_NE",
		"LT",
		"GT",
		"LE",
		"GE",
		"CONCAT",
		"PLUS",
		"MINUS",
		"STAR",
		"DIV",
		"MOD",
		"OPEN_BRACKET",
		"CLOSE_BRACKET",
		"QUOTED_STRING",
		"COLON",
		"PARAM",
		"NUM_INT",
		"ID_START_LETTER",
		"ID_LETTER",
		"ESCqs",
		"WS",
		"HEX_DIGIT",
		"EXPONENT",
		"FLOAT_SUFFIX",
		"FROM_FRAGMENT",
		"IMPLIED_FROM",
		"JOIN_FRAGMENT",
		"ENTITY_JOIN",
		"SELECT_CLAUSE",
		"LEFT_OUTER",
		"RIGHT_OUTER",
		"ALIAS_REF",
		"PROPERTY_REF",
		"SQL_TOKEN",
		"SELECT_COLUMNS",
		"SELECT_EXPR",
		"THETA_JOINS",
		"FILTERS",
		"METHOD_NAME",
		"NAMED_PARAM",
		"BOGUS",
		"RESULT_VARIABLE_REF",
		"SQL_NODE"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 72657120001561664L, 4536334256447175616L, 609484813L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 17247503360L, 68688690441355264L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 1391637038144L, 68688690441355264L, 1048576L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 72622485385286720L, 4536334265037370304L, 336855053L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 72798407245730928L, 4536334265037370304L, 336855053L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 72057594037927936L, 4467570832499019776L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	}
	
