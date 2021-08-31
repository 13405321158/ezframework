// $ANTLR : "graph.g" -> "GraphLexer.java"$

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.graph.internal.parse;

public interface HEGLTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int COMMA = 4;
	int NAME = 5;
	int DOT = 6;
	int LPAREN = 7;
	int COLON = 8;
	int RPAREN = 9;
	int WHITESPACE = 10;
	int NAME_START = 11;
	int NAME_CONTINUATION = 12;
}
