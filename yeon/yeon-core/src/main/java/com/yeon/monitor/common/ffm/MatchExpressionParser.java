package com.yeon.monitor.common.ffm;

import com.yeon.monitor.common.ffm.matcher.*;
import org.apache.commons.lang.StringUtils;

/**
 * or_expr : and_expr ( '|' and_expr )*
 *         ; 
 * 
 * and_expr : primary_exp ( '&' primary_exp )*
 *          ;
 *          
 * primary_exp : '(' or_expr ')'
 *             | '!' primary_exp
 *             | literal
 *             ; 
 *             
 * literal : ([^!&|()])*
 *         ;        
 *         
 * @author pulsarang
 */
public class MatchExpressionParser {
	public static final char STARTS = '^';
	public static final char ENDS = '$';
	public static final char ANY = '~';
	public static final char ESCAPE = '\\';

	public static final char NOT = '!';
	public static final char AND = '&';
	public static final char OR = '|';
	public static final char LPARAN = '(';
	public static final char RPARAN = ')';

	public enum MatcherType {
		EQUALS(true, true),
		CONTAINS(false, false),
		STARTS(true, false),
		ENDS(false, true);

		final boolean starts;
		final boolean ends;

		private MatcherType(boolean starts, boolean ends) {
			this.starts = starts;
			this.ends = ends;
		}
	}

	public static FilterFieldMatcher parse(String expression) {
		return parse(expression, MatcherType.CONTAINS);
	}

	public static FilterFieldMatcher parse(String expression, MatcherType defaultMatcherType) {
		if (StringUtils.isEmpty(expression)) {
			return FilterFieldMatcher.TRUE;
		}

		return new MatchExpressionParser(expression, defaultMatcherType).getMatcher();
	}

	private final String expression;
	private final MatcherType defaultMatcherType;
	private int index;

	public MatchExpressionParser(String expression, MatcherType defaultMatcherType) {
		if (StringUtils.isBlank(expression)) {
			throw new IllegalArgumentException("Blank expression.");
		}

		if (defaultMatcherType == null) {
			throw new IllegalArgumentException("Null defaultMatcherType.");
		}

		this.expression = expression;
		this.defaultMatcherType = defaultMatcherType;
	}

	public FilterFieldMatcher getMatcher() {
		return or();
	}

	/**
	 * or_expr : and_expr ( '|' and_expr )*
	 *         ; 
	 * @return
	 */
	private FilterFieldMatcher or() {
		FilterFieldMatcher left = and();
		if (index >= expression.length()) {
			return left;
		}

		OrFilterFieldMatcher orMatcher = new OrFilterFieldMatcher(left);
		while (index < expression.length()) {
			char ch = expression.charAt(index);
			if (ch == OR) {
				index++;
				orMatcher.add(and());
			} else {
				break;
			}
		}

		if (orMatcher.getMatchers().length == 1) {
			return orMatcher.getMatchers()[0];
		}

		return orMatcher;
	}

	/**
	 * and_expr : primary_exp ( '&' primary_exp )*
	 *          ;
	 * @return
	 */
	private FilterFieldMatcher and() {
		FilterFieldMatcher left = primary();
		if (index >= expression.length()) {
			return left;
		}

		AndFilterFieldMatcher andMatcher = new AndFilterFieldMatcher(left);
		while (index < expression.length()) {
			char ch = expression.charAt(index);
			if (ch == AND) {
				index++;
				andMatcher.add(primary());
			} else {
				break;
			}
		}

		if (andMatcher.getMatchers().length == 1) {
			return andMatcher.getMatchers()[0];
		}

		return andMatcher;
	}

	/**
	 * primary_exp : '(' or_expr ')'
	 *             | '!' primary_exp
	 *             | literal
	 *             ;
	 * @return
	 */
	private FilterFieldMatcher primary() {
		consumeWhitespace();

		FilterFieldMatcher matcher;
		char ch = expression.charAt(index);
		switch (ch) {
			case LPARAN:
				int lparanIndex = index++;
				consumeWhitespace();
				matcher = or();
				consumeWhitespace();
				if (current() != RPARAN) {
					String message = String.format("Fail to find matching parentheses of '%d'.(%d)", lparanIndex, index);
					throw new MatchExpressionParserException(message);
				}
				index++;
				break;
			case NOT:
				index++;
				matcher = new NotFilterFieldMatcher(primary());
				break;
			default:
				matcher = literal();
		}

		consumeWhitespace();

		return matcher;
	}

	/**
	 * literal : ([^!&|()])*
	 *         ;
	 * @return
	 */
	private FilterFieldMatcher literal() {
		return new Literal().getMathcher();
	}

	private void consumeWhitespace() {
		for (; index < expression.length(); index++) {
			if (!Character.isWhitespace(expression.charAt(index))) {
				break;
			}
		}
	}

	private char la(int count) {
		return index + count < expression.length() ? expression.charAt(index + 1) : 0;
	}

	private char current() {
		return eof() ? 0 : expression.charAt(index);
	}

	private boolean eof() {
		return index >= expression.length();
	}

	/**
	 * 
	 * @author pulsarang
	 */
	class Literal {
		boolean starts = false;
		boolean ends = false;
		boolean matchTypeModified = false;
		String text = null;

		Literal() {
			StringBuilder rawLiteral = getUnescaped();
			if (rawLiteral.length() == 0) {
				return;
			}

			int textStartIndex;
			for (textStartIndex = 0; textStartIndex < rawLiteral.length(); textStartIndex++) {
				char ch = rawLiteral.charAt(textStartIndex);
				if (ch == STARTS) {
					starts = true;
				} else if (ch == ANY) {
					starts = false;
				} else {
					break;
				}
				matchTypeModified = true;
			}

			int textEndIndex;
			for (textEndIndex = rawLiteral.length() - 1; textEndIndex >= textStartIndex; textEndIndex--) {
				char ch = rawLiteral.charAt(textEndIndex);
				if (textEndIndex - 1 < textStartIndex) {
					break;
				}

				if (rawLiteral.charAt(textEndIndex - 1) == ESCAPE) {
					if (textEndIndex - 2 >= textStartIndex && rawLiteral.charAt(textEndIndex - 2) == ESCAPE) {
						textEndIndex--;
						if (ch == ENDS) {
							ends = true;
						} else if (ch == ANY) {
							ends = false;
						}
					}
					break;
				}

				if (ch == ENDS) {
					ends = true;
				} else if (ch == ANY) {
					ends = false;
				} else {
					break;
				}
				matchTypeModified = true;
			}

			if (textStartIndex > textEndIndex) {
				return;
			}

			text = unescape(rawLiteral.substring(textStartIndex, textEndIndex + 1));
		}

		FilterFieldMatcher getMathcher() {
			if (text == null) {
				return null;
			}

			if (!matchTypeModified) {
				starts = defaultMatcherType.starts;
				ends = defaultMatcherType.ends;
			}

			if (starts) {
				if (ends) {
					return new EqualsFilterFieldMatcher(text);
				} else {
					return new StartsFilterFieldMatcher(text);
				}
			} else if (ends) {
				return new EndsFilterFieldMatcher(text);
			} else {
				return new ContainsFilterFieldMatcher(text);
			}
		}

		private StringBuilder getUnescaped() {
			StringBuilder sb = new StringBuilder(10);
			main_loop: //
			while (!eof()) {
				char ch = expression.charAt(index);
				if (Character.isWhitespace(ch)) {
					check_ws_loop: // 
					for (int i = 1;; i++) {
						char la = la(i);
						if (la == 0/*텍스트 끝*/|| isEndSeperator(la)) {
							consumeWhitespace();
							break main_loop;
						} else if (Character.isWhitespace(la)) {
							continue;
						}

						break check_ws_loop;
					}
				} else if (isEndSeperator(ch)) {
					break main_loop;
				} else if (ch == ESCAPE) {
					if (++index == expression.length()) {
						break main_loop;
					}

					sb.append(ch);
					ch = expression.charAt(index);
				}

				sb.append(ch);
				index++;
			}

			return sb;
		}

		private String unescape(String str) {
			StringBuilder sb = new StringBuilder(str.length());

			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				if (ch == ESCAPE) {
					if (++i == str.length()) {
						break;
					}
					ch = str.charAt(i);
				}

				sb.append(ch);
			}

			return sb.toString();
		}

		private boolean isEndSeperator(char ch) {
			switch (ch) {
				case AND:
				case OR:
					return true;
				case RPARAN:
					for (int i = 1;; i++) {
						char la = la(i);
						if (la == 0/*텍스트 끝*/|| la == AND || la == OR || la == NOT) {
							return true;
						} else if (Character.isWhitespace(la)) {
							continue;
						}
						break;
					}

					return false;
				default:
					return false;
			}
		}
	}
}
