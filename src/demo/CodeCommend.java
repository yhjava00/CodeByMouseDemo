package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import vo.KeyWord;
import vo.Variable;

public class CodeCommend {

	public static Map<String, Integer> convertToNumberMap = new HashMap<>();  

	public static int tapLev = 2;

	public static int codeBlock(List<StringBuilder> code, KeyWord keyWord) {
		StringBuilder line = null;
		
		String openOrClose = (String)keyWord.info;
		
		if(openOrClose.equals("열")) {
			line = buildLine().append("{\n");
			tapLev++;
			code.add(line);
			return code.size()-1;
		}else {
			tapLev--;
			line = buildLine().append("}\n");
			code.add(line);
			return -1;
		}
	}
	
    public static void codeVariable(List<StringBuilder> code, KeyWord keyWord, Variable variable) {
    	
    	String type = "int ";
    	
    	if(convertToNumber(variable.value)==-1) {
    		type = "String ";
    	}else {
    		variable.value = convertToNumber(variable.value) + "";
    	}
    	
    	StringBuilder line = buildLine();

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

    public static void codeFor(List<StringBuilder> code, KeyWord keyWord, Stack<Integer> blockStack) {
    	
    	int blockLine = blockStack.pop();
    	
		int repeat = convertToNumber((String) keyWord.info);
		
		StringBuilder line = buildLine().append("for(int i=0; i<").append(repeat).append("; i++) {\n");
		
    	code.remove(blockLine);
    	code.add(blockLine, line);
    }
    
    public static void codeOperator(List<StringBuilder> code, KeyWord keyWord) {
    	String[] opValues = (String[]) keyWord.info;
    	
    	StringBuilder line = buildLine();
    	
    	String op = " + ";
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
