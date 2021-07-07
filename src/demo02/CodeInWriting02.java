package demo02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.ConnnectAI;
import vo.Morpheme;

public class CodeInWriting02 {
	
	static List<StringBuilder> code = new LinkedList<>();
	
	static Map<String, Integer> keyWordMap = new HashMap<String, Integer>();
	
	private static StringBuilder requestText;
    public static void main(String[] args) {
    	
    	Setting02.settingCodeInWriting(keyWordMap);
    	
    	code.add(new StringBuilder().append("public class Main {").append("\n"));
    	code.add(new StringBuilder().append("\tpublic static void main(String[] args) {").append("\n"));

        Scanner sc = new Scanner(System.in);
        
        while(true) {

        	requestText = new StringBuilder();
        	
        	System.out.print("명령(demo02) : ");
        	requestText.append(sc.nextLine());
        	
        	if(requestText.toString().equals("종료")) {
        		break;
        	}

        	List<Map<String, Object>> sentenceInfoList = ConnnectAI.connect(requestText.toString());
        	
        	for(Map<String, Object> sentenceInfo : sentenceInfoList) {
        		
        		List<Morpheme> morpList = (List<Morpheme>) sentenceInfo.get("morpList");
        		
        		relationshipCreation(morpList);
        	}
        }
        
        sc.close();
        
    }
    
    private static void relationshipCreation(List<Morpheme> morpList) {
    	
    	
    	
    }
    
}
