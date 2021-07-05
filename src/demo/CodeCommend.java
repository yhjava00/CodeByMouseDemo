package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import vo.KeyWord;
import vo.Morpheme;
import vo.Variable;

public class CodeCommend {

	public static Map<String, Integer> convertToNumberMap = new HashMap<>();  

	public static int tapLev = 2;
	
	public static void codeScanner(List<StringBuilder> code, KeyWord keyWord) {
		
		Morpheme actionMorp = (Morpheme) keyWord.info;
		
		StringBuilder line = null;
		
		if(actionMorp.text.equals("열")) {
			line = buildLine().append("Scanner 키위 = new Scanner(System.in);\n");			
		}else {
			line = buildLine().append("키위.close();\n");
		}
    	code.add(line);
	}
	
	public static void codeInput(List<StringBuilder> code, KeyWord keyWord, List<KeyWord> keyWordList) {
		
		boolean initialization = false;

		Morpheme[] inputInfo = (Morpheme[]) keyWord.info;
		
		Morpheme variableMorp = inputInfo[0];
		Morpheme typeMorp = inputInfo[1];
		
		for(KeyWord key : keyWordList) {
			if(key.text.equals("변수")) {
				initialization = true;
				break;
			}
		}
		
		if(initialization) {
			StringBuilder line = code.get(code.size()-1);

			line.append(variableMorp.text).append(" = ");
			
			if(typeMorp.text.equals("정수")) {
				line.append("키위.nextInt();\n");
			}else {
				line.append("키위.nextLine();\n");
			}
			return;
		}
		
		StringBuilder line = buildLine();
		
		line.append(variableMorp.text).append(" = ");
		
		if(typeMorp.text.equals("정수")) {
			line.append("키위.nextInt();\n");
		}else {
			line.append("키위.nextLine();\n");
		}
		
		code.add(line);
	}
	
	public static void codeConditional(List<StringBuilder> code, KeyWord keyWord, String[] selectedLine) {

    	int startLine = convertToNumber(selectedLine[0])-1;
    	int endLine = convertToNumber(selectedLine[1]);
    	
    	if(startLine>endLine) {
    		int temp = startLine;
    		startLine = endLine;
    		endLine = temp;
    	}
    	
    	Map<String, Object> conditionalInfo = (Map<String, Object>) keyWord.info;
    	
    	String targetValue = (String) conditionalInfo.get("targetValue");
    	String compareValue = (String) conditionalInfo.get("compareValue");
    	String compareOP = (String) conditionalInfo.get("compareOP");
    	boolean negative = (boolean) conditionalInfo.get("negative");
    	
    	int temp = convertToNumber(targetValue);
    	if(temp>0) {
    		targetValue = Integer.toString(temp);
    	}
    	
    	temp = convertToNumber(compareValue);
    	if(temp>0) {
    		compareValue = Integer.toString(temp);
    	}
    	
    	switch (compareOP) {
		case "작":
			compareOP = " < ";
			break;
		case "크":
			compareOP = " > ";
			break;
		case "같":
			compareOP = " == ";
			break;
		case "같작":
			compareOP = " <= ";
			break;
		case "같크":
			compareOP = " >= ";
			break;
		}
    	
    	StringBuilder line = buildLine().append("if(");
    	
    	if(negative) {
    		line.append("!");
    	}
    	
    	line.append(targetValue).append(compareOP).append(compareValue).append(") {\n");

		for(int i=startLine; i<endLine; i++) {
			code.get(i).insert(0, "\t");
		}
		
    	code.add(startLine, line);
    	code.add(endLine+1, buildLine().append("}\n"));
	}
	
	public static void codeReturn(List<StringBuilder> code) {
		StringBuilder line = buildLine().append("return;\n");
		code.add(line);
	}
	
    public static void codeVariable(List<StringBuilder> code, KeyWord keyWord, Variable variable) {

    	StringBuilder line = buildLine();

    	if(variable.value.equals("정수")) {
    		line.append("int ");
    		code.add(line);
    		return;
    	}else if(variable.value.equals("문장")) {
    		line.append("String ");
    		code.add(line);
    		return;    		
    	}
    	
    	String type = "int ";
    	
    	if(convertToNumber(variable.value)==-1) {
    		type = "String ";
    	}else {
    		variable.value = convertToNumber(variable.value) + "";
    	}
    	
    	line.append(type).append(variable.name).append(" = ");
    	
    	switch (type) {
		case "String ":
			line.append("\"").append(variable.value).append("\";\n");
			break;
		default:
			line.append(variable.value).append(";\n");
			break;
		}
    	
    	code.add(line);
    }

    public static void codePrint(List<StringBuilder> code, KeyWord keyWord, Map<String, Variable> variableMap) {
    	
    	StringBuilder line = buildLine();
    	
    	if(variableMap.containsKey(keyWord.info)) {
    		line.append("System.out.println(").append(keyWord.info).append(");\n");    		
    	}else {    		
    		line.append("System.out.println(\"").append(keyWord.info).append("\");\n");
    	}
    	
    	code.add(line);
    }

    public static void codeFor(List<StringBuilder> code, KeyWord keyWord, String[] selectedLine) {
    	
    	int startLine = convertToNumber(selectedLine[0])-1;
    	int endLine = convertToNumber(selectedLine[1]);
    	
    	if(startLine>endLine) {
    		int temp = startLine;
    		startLine = endLine;
    		endLine = temp;
    	}
    	
    	int repeat = convertToNumber((String) keyWord.info);
    	
    	StringBuilder line = buildLine().append("for(int i=0; i<").append(repeat).append("; i++) {\n");
    	
    	if(startLine<0) {
    		code.add(endLine, line);
    		code.get(endLine+1).insert(0, "\t");
    		code.add(buildLine().append("}\n"));
    		return;
    	}
		
		for(int i=startLine; i<endLine; i++) {
			code.get(i).insert(0, "\t");
		}
		
    	code.add(startLine, line);
    	code.add(endLine+1, buildLine().append("}\n"));
    	
    }
    
    public static void codeOperator(List<StringBuilder> code, KeyWord keyWord) {
    	
    	String[] opValues = (String[]) keyWord.info;
    	
    	if(opValues==null)
    		return;
    	
    	StringBuilder line = buildLine();
    	
    	String op = " = ";
    	if(opValues[1].equals("")) {
    		switch (keyWord.text) {
    		case "더하":
    			op = " += ";
    			break;
    		case "빼주":
    			op = " -= ";
    			break;
    		case "곱":
    			op = " *= ";
    			break;
    		case "나누":
    			op = " /= ";
    			break;
    		case "나머지":
    			op = " %= ";
    			break;
    		}

    		String num = convertToNumber(opValues[2]) + "";
    		if(!num.equals("-1")) {
    			opValues[2] = num;
    		}
    		
    		line.append(opValues[0]).append(op).append(opValues[2]).append(";\n");
    	}else {
    		switch (keyWord.text) {
    		case "더하":
    			op = " + ";
    			break;
    		case "빼주":
    			op = " - ";
    			break;
    		case "곱":
    			op = " * ";
    			break;
    		case "나누":
    			op = " / ";
    			break;
    		case "나머지":
    			op = " % ";
    			break;
    		}
    		String num = convertToNumber(opValues[1]) + "";
    		if(!num.equals("-1")) {
    			opValues[1] = num;
    		}
    		
    		num = convertToNumber(opValues[2]) + "";
    		if(!num.equals("-1")) {
    			opValues[2] = num;
    		}
    		
    		line.append(opValues[0]).append(" = ").append(opValues[1]).append(op).append(opValues[2]).append(";\n");
    	}
    	
    	
    	code.add(line);
    }
    
    public static int convertToNumber(String num) {

    	if(num.equals("")) 
    		return -1;
    	
    	int result = 0;
    	
    	Integer n = 0;
    	
    	List<Integer> pieceOfNumbers = new ArrayList<>();
    	
    	for(int i=0; i<num.length(); i++) {

    		String strNum = num.charAt(i) + "";
    		
    		if(convertToNumberMap.containsKey(strNum)) {
    			if(strNum.equals("일")&&i+1<num.length()) {
    				strNum += num.charAt(i+1);
    				if(convertToNumberMap.containsKey(strNum)) {
    					n = convertToNumberMap.get(strNum);
    					i++;
    				}else {
    					n = 1;
    				}
    			}else {
    				n = convertToNumberMap.get(strNum);    				
    			}
    		}else {
    			if(i+1<num.length()) {
    				strNum += num.charAt(++i);
    				if(convertToNumberMap.containsKey(strNum)) {
    					n = convertToNumberMap.get(strNum);    				
    				}else {
    					return -1;
    				}    				
    			}else {
    				return -1;
    			}
    		}

    		if(n==null)
    			continue;
    		
    		pieceOfNumbers.add(n);
    	}
    	
    	int size = pieceOfNumbers.size();

    	int pieceOfNumber = pieceOfNumbers.get(0);
    	
    	if(pieceOfNumber>9||(size==1)) {
    		result = pieceOfNumber;
    	}
    	
    	int undoPiece = pieceOfNumber;
    	
    	for(int i=1; i<size; i++) {
    		pieceOfNumber = pieceOfNumbers.get(i);
    		if(pieceOfNumber>9&&undoPiece<10) {
    			result += pieceOfNumber * undoPiece;
    		}else if(pieceOfNumber>9||i==size-1){
    			result += pieceOfNumber;
    		}
    		undoPiece = pieceOfNumber;
    		
    	}

    	return result;
    }
    
    public static StringBuilder buildLine() {
    	StringBuilder line = new StringBuilder();
    	for(int i=0; i<tapLev; i++) {
			line.append("\t");
		}
    	return line;
    }
    
}
