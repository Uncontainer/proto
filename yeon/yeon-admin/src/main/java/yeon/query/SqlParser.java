package yeon.query;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;

/*
 * FROM : SELECT resource_id FROM class WHERE name=?
 * WHERE : (SELECT resource_id FROM triple WHERE resoruce_id IN ( ${FROM} ) AND property_id=? AND value_cont=?)+
 * SELECT : SELECT resource_id, property_id, value_cont FROM triple WHERE resource_id IN ( ${WHERE} ) AND property_id IN (?)
 * 
 */
public class SqlParser {
	public Select select(String query) {
		return (Select) query(query);
	}

	public Query query(String query) {
		throw new UnsupportedOperationException();
//		ANTLRInputStream is;
//		try {
//			is = new ANTLRInputStream(new ByteArrayInputStream(query.getBytes()));
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		testLexer lexer = new testLexer(is);
//		CommonTokenStream tokens = new CommonTokenStream(lexer);
//
//		testParser parser = new testParser(tokens);
//
//		try {
//			return parser.selectQuery();
//		} catch (RecognitionException e) {
//			throw new RuntimeException(e);
//		}
	}

	public static void main(String[] args) throws RecognitionException, IOException {
		String query = "select #a from #ab";
		String sql = new SqlParser().select(query).toSql();
		System.out.println(sql);
	}
}
