grammar test;

@lexer::header {
	package yeon.query;
}

@parser::header {
	package yeon.query;
	
	import yeon.query.*;
}

fragment A_ :	'a' | 'A';
fragment B_ :	'b' | 'B';
fragment C_ :	'c' | 'C';
fragment D_ :	'd' | 'D';
fragment E_ :	'e' | 'E';
fragment F_ :	'f' | 'F';
fragment G_ :	'g' | 'G';
fragment H_ :	'h' | 'H';
fragment I_ :	'i' | 'I';
fragment J_ :	'j' | 'J';
fragment K_ :	'k' | 'K';
fragment L_ :	'l' | 'L';
fragment M_ :	'm' | 'M';
fragment N_ :	'n' | 'N';
fragment O_ :	'o' | 'O';
fragment P_ :	'p' | 'P';
fragment Q_ :	'q' | 'Q';
fragment R_ :	'r' | 'R';
fragment S_ :	's' | 'S';
fragment T_ :	't' | 'T';
fragment U_ :	'u' | 'U';
fragment V_ :	'v' | 'V';
fragment W_ :	'w' | 'W';
fragment X_ :	'x' | 'X';
fragment Y_ :	'y' | 'Y';
fragment Z_ :	'z' | 'Z';

SELECT	: S_ E_ L_ E_ C_ T_ ;
FROM	: F_ R_ O_ M_  ;
WHERE	: W_ H_ E_ R_ E_  ;
OR		: O_ R_ ;
AND		: A_ N_ D_;
NOT		: N_ O_ T_;
EQ		: '=' | '<=>' ;
NEQ		: '<>' | '!=' | '~='| '^=';
LET		: '<=' ;
GET		: '>=' ;
GTH		: '>' ;
LTH		: '<' ;
COMMA		: ',' ;

query returns [Query val]
	:	selectQuery { $val = $selectQuery.val; }
	;
	
selectQuery returns [Select val]
	:	selectClause fromClause { $val = new Select($selectClause.val, $fromClause.val, null); }
	|	selectClause fromClause whereClause { $val = new Select($selectClause.val, $fromClause.val, $whereClause.val); }
	|	selectClause whereClause { $val = new Select($selectClause.val, null, $whereClause.val); }
	;
	
selectClause returns [List<ResourcePointer> val]
	:	SELECT resourcePointers { $val = $resourcePointers.val; }
	;

resourcePointers returns [List<ResourcePointer> val]
	@init{
		val=new ArrayList<ResourcePointer>();
	}
	: 	x=resourcePointer { $val.add($x.val); } (COMMA y=resourcePointer { $val.add($y.val); })* 
	;
	
resourcePointer returns [ResourcePointer val] 	
	: 	id	{$val = $id.val;}
	| 	alias	{$val = $alias.val;}
	|	name	{$val = $name.val;}
	;

id	 returns [ResourcePointer val] 	
	: 	'#' ID	{ $val=new ResourceIdPointer($ID.text);	}
	;
	
alias	 returns [ResourcePointer val] 	
	: 	'@' ID	{ $val=new ResourceAliasPointer($ID.text);	}
	;
	
name returns [ResourcePointer val] 	
	@init{
		String l=null;
	}
	: (locale ':' { l=$locale.text; })? ID	{ $val=new ResourceNamePointer(l, $ID.text);	}
	;
	
locale 	returns [String val]
	: ID	{	$val = $ID.text;	}
	;

fromClause returns [ResourcePointer val]
	:	FROM resourcePointer { $val=$resourcePointer.val; }
	;

whereClause returns [Condition val]
	:	WHERE (orCondition)+	{$val=$orCondition.val;}
	;

orCondition returns [OrCondition val]
	@init{
		val=new OrCondition();
	}
	:	x=andCondition { $val.add($x.val); } (OR y=andCondition { $val.add($y.val); })*
	;
	
andCondition returns [AndCondition val]
	@init{
		val=new AndCondition();
	}
	:	x=notCondition { $val.add($x.val); } (AND y=notCondition { $val.add($y.val); })*
	; 

notCondition returns [Condition val]
	:	NOT condition_expr { $val= new NotCondition($condition_expr.val); }
	|	condition_expr	{ $val=$condition_expr.val; }
	;	

condition_expr returns [Condition val]
	:	condition_comparison {	$val = $condition_comparison.val;	}
//	| 	(r_exists) => condition_exists
//	|	(LPAREN sql_condition) => condition_paren
//	|	condition_is
//	|	condition_group_comparison
//	|	condition_in
//	|	condition_is_a_set
//	|	condition_is_any
//	|	condition_is_empty
//	|	condition_is_of_type
//	|	condition_is_present
//	|	condition_like
//	|	condition_memeber
//	|	condition_between
//	|	condition_regexp_like
//	|	condition_submultiset
//	|	condition_equals_path
//	|	condition_under_path
	|	condition_paren {	$val = $condition_paren.val;	}
	;	

condition_comparison returns [Condition val]
	:	lhs=resourcePointer ( op=EQ | op=NEQ | op=GTH | op=GET | op=LTH | op=LET ) rhs=sql_expression
	{	$val = new ComparisonCondition(ComparisonType.valueOf($op.text), $lhs.val, $rhs.val.toString());	}
	;

sql_expression returns [Object val]
	:	string_literal { $val = $string_literal.val;}
	;
	
string_literal returns [Object val]
	:	TEXT_STRING { $val=$TEXT_STRING.text; }
	;
	
condition_paren	returns [Condition val]
	: 	'(' orCondition ')' {	$val = $orCondition.val;	}
	;

TEXT_STRING
	:
	( N_ | ('_' U_ T_ F_ '8') )?
	(
		(  '\'' ( ('\\' '\\') | ('\'' '\'') | ('\\' '\'') | ~('\'') )* '\''  )
		|
		(  '\"' ( ('\\' '\\') | ('\"' '\"') | ('\\' '\"') | ~('\"') )* '\"'  ) 
	)
	;

ID	
	:	( 'A'..'Z' | 'a'..'z' | '_') ( 'A'..'Z' | 'a'..'z' | '_' | '0'..'9' )*
	;

INT	: 	'0'..'9'+ 
	;

NEWLINE	:	('\r')? '\n'
	;
	
WS	:	(' ' | '\t' | '\r' | '\n')+  { skip(); }
	;
	
SL_COMMENT	
	: (('--') ~('\n'|'\r')* '\r'? '\n' ) {$channel=HIDDEN;} 
	;
	
ML_COMMENT	
	: '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;} 
	;