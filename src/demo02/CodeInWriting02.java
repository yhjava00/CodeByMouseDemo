package demo02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import common.ConnnectAI;
import vo.Variable;

public class CodeInWriting02 {
	
	private static List<StringBuilder> code = new LinkedList<StringBuilder>();
	
	private static Map<String, Integer> keyWordSetMap = new HashMap<>();
	private static Map<String, Variable> variableMap = new HashMap<>();
	private static Set<String> compareSet = new HashSet<>();
	
	private static StringBuilder requestText;
    public static void main(String[] args) {
    	
        Scanner sc = new Scanner(System.in);
        
        while(true) {

        	requestText = new StringBuilder();
        	
        	System.out.print("명령(demo02) : ");
        	requestText.append(sc.nextLine());
        	
        	if(requestText.toString().equals("종료")) {
        		break;
        	}

        	List<Map<String, Object>> sentenceInfoList = ConnnectAI.connect(requestText.toString());
        	
//        	buildCode(sentenceInfoList);
        }
        
        sc.close();
        
    }
    
    private static void buildCode(List<Map<String, Object>> sentenceInfoList) {
    	
    }
    
    private static Map<String, Object> prepareToCode(Map<String, Object> sentenceInfo, int beforeSentenceLength) {
    	
        return null;
    }
    
}
