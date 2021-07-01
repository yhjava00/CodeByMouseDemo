package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vo.KeyWord;
import vo.Variable;

public class CodeCommend {

	public static Map<String, Integer> convertToNumberMap = new HashMap<>();  

	public static int tapLev = 2;

    public static void codeVariable(List<StringBuilder> code, KeyWord keyWord, Variable variable) {
    	
    	String type = "int ";
    	switch (variable.type) {
		case "정수":
			type = "int ";
			variable.value = Integer.toString(convertToNumber(variable.value));
			break;
		case "실수":
			type = "double ";
			variable.value = Integer.toString(convertToNumber(variable.value));
			break;
		case "문자":
			type = "String ";
			break;
		case "논리":
			type = "boolean ";
			break;
		}
    	
    	StringBuilder line = buildLine();

    	line.append(type).append(variable.name).append(" = ").append(variable.value).append(";\n");
    	
    	code.add(line);
    }

    public static void codePrint(List<StringBuilder> code, KeyWord keyWord) {
    	
    	StringBuilder line = buildLine();
    	
    	line.append("System.out.println(\"").append(keyWord.info).append("\");\n");
    	
    	code.add(line);
    }

    public static void codeFor(List<StringBuilder> code, KeyWord keyWord) {
    	
		int repeat = convertToNumber(keyWord.info);
		
		StringBuilder line = buildLine().append("for(int i=0; i<").append(repeat).append("; i++) {\n");
		
    	code.add(line);
    	tapLev++;
    }
    
    public static int convertToNumber(String repeatText) {
    	
    	int result = 0;
    	
    	Integer n = 0;
    	
    	List<Integer> pieceOfNumbers = new ArrayList<>();
    	
    	for(int i=0; i<repeatText.length(); i++) {
    		
    		String strNum = repeatText.charAt(i) + "";
    		
    		if(convertToNumberMap.containsKey(strNum)) {
    			n = convertToNumberMap.get(strNum);
    		}else {
    			try {
    				strNum += repeatText.charAt(++i);
    				n = convertToNumberMap.get(strNum);
				} catch (Exception e) {
					n = 1;
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
