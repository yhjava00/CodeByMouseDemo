package demo02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import common.ConnectAI;
import vo.Morpheme;

public class CodeInWriting02 {
	
	static List<StringBuilder> code = new LinkedList<>();
	
	static Set<String> keyWordSet = new HashSet<String>();
	
	private static StringBuilder requestText;
    public static void main(String[] args) {
    	
    	Setting02.settingCodeInWriting(keyWordSet);
    	
    	code.add(new StringBuilder().append("public class Main {").append("\n"));
    	code.add(new StringBuilder().append("\tpublic static void main(String[] args) {").append("\n"));

        Scanner sc = new Scanner(System.in);
        
        System.out.println("지니 : 안녕하세요. 사용자님.");
        
        while(true) {

        	requestText = new StringBuilder();
        	
        	System.out.println("지니 : 어떤 작업을 하시겠습니까?");
        	System.out.print("사용자02 : ");
        	requestText.append(sc.nextLine());
        	
        	if(requestText.toString().equals("종료")) {
        		break;
        	}

        	List<Morpheme> morpList = ConnectAI.morphemeSeparation(requestText.toString());

    		codeRepeater(morpList);
        }
        
        sc.close();
        
    }
    
    private static void codeRepeater(List<Morpheme> morpList) {
    	
    	for(Morpheme morp : morpList) {
    		
    		if(!keyWordSet.contains(morp.text))
    			continue;
    		
    		switch (morp.text) {
    		case "스캐너":
    			CodeCommend02.codeScanner(code, requestText, morpList);
    			break;
    		case "연산":
    			CodeCommend02.codeCalculate(code, requestText, morpList);
    			break;
    		case "조건":
    			CodeCommend02.codeCondition(code, requestText, morpList);
    			break;
    		case "변수":
    			CodeCommend02.codeVariable(code, requestText, morpList);
    			break;
    		case "블록":
    			CodeCommend02.codeBlock(code, requestText, morpList);
    			break;
			case "반복":
				CodeCommend02.codeFor(code, requestText, morpList);
				break;
			case "출력":
				CodeCommend02.codePrint(code, requestText, morpList);
				break;
			}
    		
    		
    		showCode();
    	}
    }
    
    private static void showCode() {
    	StringBuilder finalCode = new StringBuilder();
        int i=0;
        for(; i<code.size(); i++) {
        	finalCode.append(i+1).append(". ").append(code.get(i));
        }
        
        finalCode.append(++i).append(". ").append("\t}").append("\n");
        finalCode.append(++i).append(". ").append("}");
        
        System.out.println(finalCode);
    }
    
}
