grammar Php;

options {
}

tokens{
}

@header {
import com.naver.mage4j.php.*;
import com.naver.mage4j.php.code.*;
import com.naver.mage4j.php.lang.*;
import java.util.*;
import java.util.stream.*;
}

@lexer::members{
private String consumeBefore(String sep, boolean consumeSep) {
	int firstCh = sep.charAt(0);
	for(int index=1, ch=EOF; (ch = _input.LA(index)) != EOF; index++) {
		if (ch != firstCh) {
			continue;
		}
		
		if (match(index, sep)) {
			String result = consumeAndGet(index-1);
			if (consumeSep) {
				consumeAndGet(sep.length());
			}
			
			return result;
		}
	}

	return null;
}

private boolean match(int beginIndex, String value) {
	for (int i = 0, n = value.length(); i < n; i++) {
		if (_input.LA(beginIndex + i) != value.charAt(i)) {
			return false;
		}
	}

	return true;
}

private void consumeWhitespace() {
	int ch;
	while ((ch = _input.LA(1)) != EOF) {
		if(Character.isWhitespace(ch)) {
			_input.consume();
		} else {
			break;
		}
	}
}

private String consumeAndGet(int count) {
	StringBuilder sb = new StringBuilder(count);
	for (int i = 0; i < count; i++) {
		sb.append((char)_input.LA(1));
		_input.consume();
	}
	
	return sb.toString();
}

private String consumeAll() {
	StringBuilder sb = new StringBuilder();
	int ch;
	while ((ch = _input.LA(1)) != EOF) {
		sb.append((char)ch);
		_input.consume();
	}

	return sb.toString();
}

private String getPhpBodyString() {
	String result = consumeBefore("<?", true);
	if (result != null) {
		if (match(1, "php")) {
			consumeAndGet(3);
		}
	}

	return result;
}

@Override
public Token nextToken() {
	//The following code was pulled out from super.nextToken()
	if (_input.index() == 0) {
		_token = null;
		_channel = Token.DEFAULT_CHANNEL;
		_tokenStartCharIndex = _input.index();
		_tokenStartCharPositionInLine = getInterpreter().getCharPositionInLine();
		_tokenStartLine = getInterpreter().getLine();
		_text = getPhpBodyString();
		if (_text != null && !_text.isEmpty()) {
			_type = BodyString;
			emit();
			
			return _token;
		}
	}

	return super.nextToken();
}
}

@parser::members{
static List<String> toStringList(List<Token> tokens) {
	return tokens.stream().map(s->s.getText()).collect(Collectors.toList());
}
}

SemiColon 			: ';';
Comma 				: ',';
OpenBrace 			: '(';
CloseBrace 			: ')';
OpenSquareBrace 	: '[';
CloseSquareBrace 	: ']';
OpenCurlyBrace		: '{';
CloseCurlyBrace		: '}';
ArrayAssign 		: '=>';
LogicalOr 			: '||';
LogicalAnd 			: '&&';
ClassMember 		: '::';
InstanceMember 		: '->';
SuppressWarnings 	: '@';
QuestionMark 		: '?';
Dollar 			: '$';
Colon 			: ':';
Dot 			: '.';
Ampersand 		: '&';
Pipe 			: '|';
Caret			: '^';
Bang 			: '!';
Plus 			: '+';
Minus 			: '-';
Asterisk 		: '*';
Percent 		: '%';
Forwardslash 	: '/'; 
Tilde 			: '~';
Equals 			: '=';
New 			: 'new';
Clone 			: 'clone';
Echo 			: 'echo';
Print 			: 'print';
If 				: 'if';
Else 			: 'else';
ElseIf 			: 'elseif';
For 			: 'for';
Foreach 		: 'foreach';
While 			: 'while';
Do 				: 'do';
Switch 			: 'switch';
Case 			: 'case';
Default 		: 'default';
Function 		: 'function';
Break 			: 'break';
Continue 		: 'continue';
Return 			: 'return';
Global 			: 'global';
Static 			: 'static';
And 			: 'and' | 'AND';	//('a'|'A')('n'|'N')('d'|'D');
Or 				: 'or' | 'OR'; 		//('o'|'O')('r'|'R');
Xor 			: 'xor' | 'XOR';	 //('x'|'X')('o'|'O')('r'|'R');
Instanceof 		: 'instanceof' | 'instanceOf';

Class 			: 'class';
Interface 		: 'interface';
Extends 		: 'extends';
Implements 		: 'implements';
Abstract 		: 'abstract';
Var 			: 'var';
Const 			: 'const' | 'Const' | 'CONST';	//('c'|'C')('o'|'O')('n'|'N')('s'|'S')('t'|'T');
Throw			: 'throw';
Try				: 'try';
Catch			: 'catch';
Null 			: 'null' | 'NULL';
As				: 'as' | 'AS'; 	// ('a'|'A') ('s' | 'S') ;
Array		    : 'array' | 'Array' | 'ARRAY'; //('a'|'A')('r'|'R')('r'|'R')('a'|'A')('y'|'Y');
//String			: ('s'|'S')('t'|'T')('r'|'R')('i'|'I')('n'|'N')('g'|'G');

prog
	@init { List<PhpStatement> value = new ArrayList<PhpStatement>(); }
	: ( statement { value.add($statement.value); } )*
	;

statement returns [ PhpStatement value ]
    : BodyString { $value= new PhpBodyString($BodyString.text);	}
    | bracketedBlock 				{ $value = $bracketedBlock.value; }
    | classDefinition 				{ $value = $classDefinition.value; }
    | interfaceDefinition 			{ $value = $interfaceDefinition.value; }
    | DocumentComment* complexStatement ';'* DocumentComment* 		{ $value = $complexStatement.value; }
    | DocumentComment* simpleStatement ';'+	DocumentComment* 		{ $value = $simpleStatement.value; }
    ;
    
bracketedBlock returns [ PhpStatementBlock value ]
    : '{' DocumentComment* (statements+=statement)* '}' {
    	$value = new PhpStatementBlock($statements.stream().map(s->s.value).collect(Collectors.toList()));
    }
    ;

interfaceDefinition returns [ PhpInterface value ]
    : interfaceComments+=DocumentComment*  
    	Interface interfaceName=UnquotedString ( x=interfaceExtends )? 
    	OpenCurlyBrace ( 
	    	memberComments+=DocumentComment* interfaceMembers+=interfaceMember { 
	    		$interfaceMember.value.setDocumentComments(toStringList($memberComments)); 
	    	} 
    	)* CloseCurlyBrace {
			$value = new PhpInterface(
				$interfaceName.text
				, $x.ctx != null ? $x.value : null
				, $interfaceMembers.stream().map(s->s.value).collect(Collectors.toList()));
			$value.setDocumentComments(toStringList($interfaceComments)); 
		}
	;

interfaceExtends returns [ List<String> value ]
    : Extends interfaceNames+=UnquotedString ( Comma interfaceNames+=UnquotedString )* {
		$value = toStringList($interfaceNames);
	}
    ;
interfaceMember returns [PhpClassMember value]
    : Const fieldName=UnquotedString (Equals atom)? ';' { $value = new PhpClassField(true, $fieldName.text, $atom.value); }
    | fieldModifiers Function functionName=UnquotedString parametersDefinition ';' { $value = new PhpClassFunction($fieldModifiers.value, $functionName.text, $parametersDefinition.value); }
    ;

classDefinition returns [ PhpClass value ]
	@init { $value=new PhpClass(); }
    : classComments+=DocumentComment* { ((PhpClass)$value).setDocumentComments(toStringList($classComments)); }
    	(classModifier{ $value.setModifier($classModifier.text); } )? 								
    	Class className=UnquotedString { $value.setName($className.text); }
   		(Extends extendsclass=UnquotedString{ $value.setExtendsClassName($extendsclass.text); })? 		
   		(classImplements{ $value.setImplementsInterfaceNames($classImplements.value); })?							
        OpenCurlyBrace (fieldComments+=DocumentComment* classMember{ 
        	$value.addMember($classMember.value);
        	$classMember.value.setDocumentComments(toStringList($fieldComments)); 
        })* CloseCurlyBrace
    ;
    
classImplements returns [ List<String> value ]
    :  Implements ( classNames+=UnquotedString (Comma classNames+=UnquotedString)* ) { $value = toStringList($classNames); }
    ;

classMember returns [PhpClassMember value]
    : fieldModifiers Function ref='&'? functionName=UnquotedString parametersDefinition {
			$value = new PhpClassFunction($fieldModifiers.value, $functionName.text, $parametersDefinition.value, null);
			((PhpClassFunction)$value).setReference($ref != null);
		} ( 
			functionBody=bracketedBlock		{ ((PhpClassFunction)$value).setBody($bracketedBlock.value); } 
			| ';' )
    | Var Dollar fieldName=UnquotedString  { $value = new PhpClassField(false, $fieldName.text); } 
		(Equals ( 
			atom							{((PhpClassField)$value).setValue($atom.value);} 
			| staticMemberAccessChain		{((PhpClassField)$value).setValue($staticMemberAccessChain.value);} )
		)? ';'
    | Const fieldName=UnquotedString	  { $value = new PhpClassField(true, $fieldName.text); } 
		(Equals (
			atom							{((PhpClassField)$value).setValue($atom.value);} 
			| staticMemberAccessChain		{((PhpClassField)$value).setValue($staticMemberAccessChain.value);} )
		)? ';'
    | fieldModifiers (Dollar fieldName=UnquotedString)	  { $value = new PhpClassField($fieldModifiers.value, $fieldName.text, null); }  
		(Equals (
			atom						{((PhpClassField)$value).setValue($atom.value);} 
			| staticMemberAccessChain	{((PhpClassField)$value).setValue($staticMemberAccessChain.value);} )
		)? ';'
    ;

classModifier
    : 'abstract';
    
fieldModifiers returns [ List<String> value ]
	@init { $value = new ArrayList<String>(); }
	: ( fieldModifier { $value.add($fieldModifier.text); } )*
	;
    
fieldModifier returns [ String value ]
    : AccessModifier 	{ $value=$AccessModifier.text; } 
    | 'abstract' 		{ $value=$text; } 
    | 'static' 			{ $value=$text; } 
    | 'final' 			{ $value=$text; }
    ;
    
complexStatement returns [ PhpStatement value ]
    : If '(' (ifCondition=expression	{ $value = new PhpControlIf($expression.value); }
			| '!' assignment			{ $value = new PhpControlIf(new PhpExpressionLogicalNot($expression.value)); }
		) ')' 
		ifTrue=statement				{ ((PhpControlIf)$value).setTrue($ifTrue.value); } 
		conditional[(PhpControlIf)$value]?
    | For '(' init=commaList? ';' condition=commaList? ';' update=commaList? ')' body=statement { 
		$value = new PhpControlFor($init.ctx != null ? $init.value : null
		, $condition.ctx != null ? $condition.value : null
		, $update.ctx != null ? $update.value : null
		, $body.value); 
	}
    | Foreach '(' expression As arrayEntry ')' statement		{ $value = new PhpControlForeach($expression.value, $arrayEntry.value, $statement.value); }
    | While '(' expression? ')' body=statement					{ $value = new PhpControlWhile($expression.value, $body.value, false); }
    | Do body=statement While '('expression ')' ';'				{ $value = new PhpControlWhile($expression.value, $body.value, true); }
    | Switch '(' expression ')' { $value = new PhpControlSwitch($expression.value); }'{' cases[(PhpControlSwitch)$value] '}'
    | Try bracketedBlock { $value = new PhpControlTry($bracketedBlock.value);} 
		( Catch '(' typeDeclaration variable ')' bracketedBlock { 
			((PhpControlTry)$value).addCatch(new PhpControlTry.Catch($typeDeclaration.value, $variable.value, $bracketedBlock.value)); } 
		)+
    | DocumentComment* functionDefinition	{ $value = $functionDefinition.value; }
    ;

simpleStatement returns [ PhpStatement value ]
    : Echo commaList						{ $value = new PhpStatementEcho($commaList.value); }
    | Global names+=name (',' names+=name)*	{ $value = new PhpStatementGlobal($names.stream().map(s->s.value).collect(Collectors.toList())); }
    | Static variable (Equals atom)?		{ $value = new PhpStatementStatic($variable.value, $atom.ctx!=null ? $atom.value : null); }
    | Break label=Integer?					{ $value = new PhpStatementBreak($label.text); }
    | Continue label=Integer?				{ $value = new PhpStatementContinue($label.text); }
    | Return result=expression?				{ $value = new PhpStatementReturn($result.ctx != null ? $result.value : null); }
    | RequireOperator expression			{ $value = new PhpStatementRequire($expression.value); }
    | Throw ex=expression					{ $value = new PhpStatementThrow($ex.value); }
    | expression							{ $value = $expression.value;}
    ;

conditional [ PhpControlIf value ]
    : ElseIf '(' ifCondition=expression ')' ifTrue=statement { value.addConditional($ifCondition.value, $ifTrue.value); } (conditional[value])?
    | Else statement { value.setElse($statement.value); }
    ;

cases [PhpControlSwitch value]
    : casestatement[value]*  defaultcase[value]?
    | defaultcase[value]? casestatement[value]*
    | casestatement[value]+ defaultcase[value] casestatement[value]+
    ;

casestatement [PhpControlSwitch value]
    : DocumentComment* Case expression Colon statements+=statement* {
		PhpControlSwitch.Case item = new PhpControlSwitch.Case($expression.value);
		if(!$statements.isEmpty()) {
			item.setBody(new PhpStatementBlock($statements.stream().map(s->s.value).collect(Collectors.toList())));
		}
		value.addCase(item);
	}
    ;

defaultcase [PhpControlSwitch value] 
    : DocumentComment* (Default Colon statements+=statement*) {
		PhpControlSwitch.DefaultCase item = new PhpControlSwitch.DefaultCase();
		if(!$statements.isEmpty()) {
			item.setBody(new PhpStatementBlock($statements.stream().map(s->s.value).collect(Collectors.toList())));
		}
		value.addCase(item);
	}
    ;

functionDefinition returns [ PhpFunction value ]
    : Function funcName=UnquotedString parametersDefinition bracketedBlock { $value = new PhpFunction($funcName.text, $parametersDefinition.value, $bracketedBlock.value); }
    ;

parametersDefinition returns [ List<PhpVariableDeclaration> value ]
    : OpenBrace (params+=paramDef (Comma params+=paramDef)*)? CloseBrace {
		$value = $params.stream().map(s->s.value).collect(Collectors.toList());
	}
    ;

paramDef returns [ PhpVariableDeclaration value ]
    : type=typeDeclaration? paramName { $value= new PhpVariableDeclaration($type.ctx != null ? $type.value : null, $paramName.value); } 
		( Equals (
			atom						{ $value.setDefaultValue($atom.value); }
			| staticMemberAccessChain	{ $value.setDefaultValue($staticMemberAccessChain.value); }
		))?
    ;
    
typeDeclaration returns [PhpType value]
	: type=UnquotedString	{ $value = PhpTypeFactory.getLazyLoading($type.text); }
	| Array					{ $value = PhpTypeFactory.get("array", true); }
	| type=PrimitiveType	{ $value = PhpTypeFactory.get($type.text, true); }
	;

paramName returns [ PhpAccessVariable value ]
    : variable				{ $value = $variable.value; }
    | Ampersand variable	{ $value = $variable.value; $value.setReference(true); }
    ;

commaList returns [ List value ]
    : expressions+=expression (',' expressions+=expression)* {
		$value = $expressions.stream().map(s->s.value).collect(Collectors.toList());
	}
    ;
    
expression returns [ PhpExpression value ]
	: weakLogicalOr { $value = $weakLogicalOr.value; }
    ;

weakLogicalOr returns [ PhpExpression value ]
    : left=weakLogicalXor (op=Or rights+=weakLogicalXor)* {
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create(
				$left.value,
				$op.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;

weakLogicalXor returns [ PhpExpression value ]
    : left=weakLogicalAnd (op=Xor rights+=weakLogicalAnd)* {
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				$op.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;
    
weakLogicalAnd returns [ PhpExpression value ]
    : print									{ $value = $print.value; }
	| left=print (op=And rights+=print)+	{ $value = PhpExpressionBinary.create($left.value, $op.text, $rights.stream().map(s -> s.value).collect(Collectors.toList()));	}
	;
    
print returns [ PhpExpression value ]
	: Print expression	{ $value = new PhpExpressionPrint($expression.value); }
	| assignment		{ $value = $assignment.value; }
	;

assignment returns [ PhpExpression value ]
	locals [ PhpVariablesDeclaration vars ]
@init{ $vars = new PhpVariablesDeclaration(); }
    : lhs=reference (OpenSquareBrace CloseSquareBrace)* op=(Equals | AsignmentOperator) rhs=assignment	{ $value = new PhpExpressionAssignment($lhs.value, $op.text, $rhs.value); }
	| 'list' '(' name? { $vars.add($name.ctx != null ? $name.value : null); } (Comma name? { $vars.add($name.ctx != null ? $name.value : null); })* ')' Equals rhs=assignment	{ $vars.setValue($rhs.value); $value = $vars; }
    | ternary	{ $value = $ternary.value; }
    ;

ternary returns [ PhpExpression value ]
    : condition=logicalOr QuestionMark t=expression? Colon f=expression? { 
		$value = new PhpExpressionTernary($condition.value, $t.ctx != null ? $t.value : null, $f.ctx != null ? $f.value : null); 
	}
    | logicalOr	{ $value = $logicalOr.value; }
    ;
    
logicalOr returns [ PhpExpression value ]
	: left=logicalAnd (op=LogicalOr rights+=logicalAnd)+	{ $value = PhpExpressionBinary.create($left.value, $op.text, $rights.stream().map(s -> s.value).collect(Collectors.toList())); }
	| logicalAnd											{ $value = $logicalAnd.value; }
    ;

logicalAnd returns [ PhpExpression value ]
    : left=bitwiseOr (op=LogicalAnd rights+=bitwiseOr)*{
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				$op.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;
    
bitwiseOr returns [ PhpExpression value ]
    : left=bitwiseXor (op=Pipe rights+=bitwiseXor)*{
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				$op.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;
    
bitwiseXor returns [ PhpExpression value ]
	: left=bitWiseAnd (op=Caret rights+=bitWiseAnd)* {
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				$op.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
	;

bitWiseAnd returns [ PhpExpression value ]
    : left=equalityCheck (op=Ampersand rights+=equalityCheck)* {
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				$op.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;

equalityCheck returns [ PhpExpression value ]
    : left=comparisionCheck {$value = $left.value;} 
		(op=EqualityOperator right=comparisionCheck {$value = PhpExpressionBinary.create($left.value, $op.text, $right.value); })?
    ;
    
comparisionCheck returns [ PhpExpression value ]
    : left=bitWiseShift {$value = $left.value;} 
		(op=ComparisionOperator right=bitWiseShift {$value = PhpExpressionBinary.create($left.value, $op.text, $right.value); })?
    ;

bitWiseShift returns [ PhpExpression value ]
    : left=addition (ShiftOperator rights+=addition)* {
		if($rights.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				$ShiftOperator.text,
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;
    
addition returns [PhpExpression value]
    : left=multiplication (ops+=(Plus | Minus | Dot) rights+=multiplication)* {
		if($ops.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value, 
				toStringList($ops),
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;

multiplication returns [PhpExpression value]
    : left=logicalNot (ops+=(Asterisk | Forwardslash | Percent) rights+=logicalNot)* {
		if($ops.isEmpty()) {
			$value = $left.value;
		} else {
			$value = PhpExpressionBinary.create($left.value,
				toStringList($ops),
				$rights.stream().map(s -> s.value).collect(Collectors.toList())
			);
		}
	}
    ;

logicalNot returns [PhpExpression value]
    : Bang expression	{ $value = new PhpExpressionLogicalNot($expression.value); }
    | instanceOf		{ $value = $instanceOf.value; }
    ;

instanceOf returns [PhpExpression value]
    : child=negateOrCast{$value=$child.value;} (Instanceof parent=negateOrCast {$value=new PhpExpressionInstanceOf($child.value, $parent.value);})? 
    ;

negateOrCast returns [PhpExpression value]
    : op=(Tilde | Minus | SuppressWarnings) increment	{ $value = new PhpExpressionNegation($increment.value, $op.text); }
    | (OpenBrace typeNames+=(PrimitiveType | Array | 'String') CloseBrace)+ (logicalNot | '(' expression ')') { 
		PhpExpression target = $logicalNot.value;
		if(target == null) {
			target = $expression.value;
		}
		$value = new PhpExpressionCast(target, toStringList($typeNames)); 
	}
    | OpenBrace weakLogicalAnd CloseBrace	{ $value = $weakLogicalAnd.value; }
    | increment								{ $value = $increment.value; }
    ;

increment returns [PhpExpression value]
    : IncrementOperator name	{ $value = new PhpExpressionIncrment($name.value, $IncrementOperator.text, true); }
    | name IncrementOperator	{ $value = new PhpExpressionIncrment($name.value, $IncrementOperator.text, false); }
    | newOrClone				{ $value = $newOrClone.value; }
    ;

newOrClone returns [PhpExpression value]
    : New (staticMemberAccessChain	{ $value = new PhpNew($staticMemberAccessChain.value); } 
    	| memberAccessChain[null]	{ $value = new PhpNew($memberAccessChain.value); } ) 
    | Clone (name | '(' name ')')	{ $value = new PhpClone($name.value); }
    | atomOrReference				{ $value = $atomOrReference.value; }
    ;

atomOrReference returns [PhpExpression value]
    : atom					{ $value=$atom.value; }
    | reference				{ $value=$reference.value; }
    | '(' expression ')'	{ $value=$expression.value; }
    ;

atom returns [PhpAtom value]
	: SingleQuotedString	{ $value = new PhpAtomString($SingleQuotedString.text, false); }
	| DoubleQuotedString	{ $value = new PhpAtomString($DoubleQuotedString.text, false); }
	| HereDoc				{ $value = new PhpAtomString($HereDoc.text, true); }
	| sign=('-' | '+')? Integer		{ $value = new PhpAtomNumber($sign.text, $Integer.text, PhpTypeFactory.INTEGER); }
	| sign=('-' | '+')? Real		{ $value = new PhpAtomNumber($sign.text, $Real.text, PhpTypeFactory.REAL); }
	| Boolean						{ $value = new PhpAtomBoolean($Boolean.text); }
	| arrayDeclaration				{ $value = $arrayDeclaration.value; }
	| Null							{ $value = PhpAtomNull.INSTANCE; }
    ;

arrayDeclaration returns [PhpAtomArray value]
@init{ $value = new PhpAtomArray(); }
    : Array OpenBrace (arrayEntry { $value.add($arrayEntry.value); } (Comma arrayEntry { $value.add($arrayEntry.value); })* Comma?)? CloseBrace
    ;

arrayEntry returns [PhpStatement value]
    : (keyValuePair { $value=$keyValuePair.value;} | expression { $value=$expression.value;})
    ;

keyValuePair returns [ PhpPair value ]
    : (key=expression ArrayAssign val=expression) { $value = new PhpPair($key.value, $val.value); }
    ;

//Need to be smarter with references, they have their own tower of application.
reference returns [ PhpAccess value ]
    : Ampersand name	{ $value = $name.value; $value.setReference(true); }
    | name				{ $value = $name.value; }
    ;

name returns [ PhpAccess value ]
	: staticMemberAccessChain	{ $value = $staticMemberAccessChain.value; }
	| memberAccessChain[null]	{ $value = $memberAccessChain.value; }
    | variable					{ $value = $variable.value; }
    ;
    
staticMemberAccessChain returns [ PhpAccess value ]
    : className=UnquotedString '::' memberAccessChain[$className.text] { $value=$memberAccessChain.value; }
    ;

memberAccessChain [ String className ] returns [ PhpAccess value ]
locals [ PhpAccessBuilder builder = new PhpAccessBuilder(); ]
@init  { $builder = new PhpAccessBuilder(); }
@after { $value = $builder.getExpression(); }
    : variable			{ $builder.addHeader(className, $variable.value); } 
    	(OpenSquareBrace expression CloseSquareBrace	{ $builder.add($expression.value); }
    		| '{' expression '}'		{ $builder.add($expression.value); } 
    		| '->' ('{' expression '}'	{ $builder.add($expression.value); }
				   | variable			{ $builder.add($variable.value); 	 })
			| OpenBrace (args+=expression (Comma args+=expression)* )? CloseBrace { $builder.addFunctionCall($args.stream().map(s->s.value).collect(Collectors.toList())); } 
		)*
    ;
	    
variable returns [ PhpAccessVariable value ]
    : SuppressWarnings? dollars+=Dollar* variableName=(UnquotedString | 'list' |
		PrimitiveType | RequireOperator | AccessModifier | Boolean |
        New | Clone | Echo | Print | If | Else | ElseIf | For | Foreach | While | Do | Switch | Case | Default |
        Function | Break | Continue | Return | Global | Static | And | Or | Xor | Instanceof |
        Class | Interface | Extends | Implements | Abstract | Var | Const | Throw | Try | Catch | Null | As | Array) {
			$value = new PhpAccessVariable($variableName.text, $dollars.size(), $SuppressWarnings != null);
		}
    ;
    
BodyString
    : '?>' { 
    	_text = getPhpBodyString();
    	if(_text == null) {
    		_text = consumeAll();
    	} 
    }
    ;

DocumentComment
	: '/**' .*? '*/'
    ;

MultilineComment    
	: '/*' .*? '*/' -> channel(HIDDEN)
    ;

SinglelineComment
    : '//'  ('?' | ~('\n'|'?'))* -> channel(HIDDEN)
    ;

UnixComment
    : '#' ('?' | ~('\n'|'?'))* -> channel(HIDDEN)
    ;
    
RequireOperator
    : 'require' | 'require_once' | 'include' | 'include_once'
    ;

PrimitiveType
    : 'int'|'integer'|'float'|'double'|'string'|'array'|'object'|'bool'|'boolean'|'BOOL'
    ;
	
AccessModifier
    : 'public' | 'private' | 'protected' 
    ;

fragment
Decimal	
	:('1'..'9' ('0'..'9')*)|'0'
	;
fragment
Hexadecimal
	: '0'('x'|'X')('0'..'9'|'a'..'f'|'A'..'F')+
	;
	
fragment
Octal
	: '0'('0'..'7')+
	;
Integer
	:Octal|Decimal|Hexadecimal
	;
	
fragment
Digits
	: '0'..'9'+
	;
	
fragment
DNum
	:(/*('.' Digits)=>*/('.' Digits)|(Digits '.' Digits?))
	;
	
fragment
Exponent_DNum
	:((Digits|DNum)('e'|'E')('+''-')?Digits)
	;
	
Real
    : DNum|Exponent_DNum
    ;

Boolean
    : 'true' | 'TRUE' | 'false' | 'FALSE'
    ;

SingleQuotedString
	:	'\'' ('\\' . | ~'\'' )*? '\''
    ;

fragment
EscapeCharector
    : 'n' | 'r' | 't' | '\\' | '$' | '"' | Digits | 'x'
    ;

DoubleQuotedString
    : '"' ('\\' . | ~'"' )*? '"' 
    ;

HereDoc 
    : '<<<' {
    	consumeWhitespace();
    	String hereDocName = consumeBefore("\n", true);

		String result = consumeBefore(hereDocName, true);
		if (result != null) {
			_text = result;
		}
    }
    ;

//Todo handle '\x7f' - '\xff'
UnquotedString
   : ('a'..'z' | 'A'..'Z' | '_')  ('a'..'z' | 'A'..'Z' | '0'..'9' | '_')*
   ;
   
AsignmentOperator
    : '+=' | '-=' | '*=' | '/=' | '.=' | '%=' | '&=' | '|=' | '^=' | '<<=' | '>>='
    ;
    
EqualityOperator
    : '==' | '!=' | '===' | '!=='
    ;

ComparisionOperator
    : '<' | '<=' | '>' | '>=' | '<>'
    ;
    
ShiftOperator
    : '<<' | '>>'
    ;

IncrementOperator
    : '--'|'++'
    ;
    

fragment
Eol 
	: '\n'
    ;

WhiteSpace
/*@init {
    $channel=HIDDEN;
}*/
	:	(' '| '\t'| '\n' | '\r')+ -> skip 
	;
	


