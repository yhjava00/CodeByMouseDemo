package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		 setConvertToNumberMap();
		 
		 System.out.println(convertToNumber("일"));
		 
	}
	private static Map<String, Integer> convertToNumberMap = new HashMap<>(); 
    
    private static int convertToNumber(String num) {
    	
    	int result = 0;
    	
    	Integer n = 0;
    	
    	List<Integer> pieceOfNumbers = new ArrayList<>();
    	
    	for(int i=0; i<num.length(); i++) {
    		
    		String strNum = num.charAt(i) + "";
    		
    		if(convertToNumberMap.containsKey(strNum)) {
    			n = convertToNumberMap.get(strNum);
    		}else {
    			try {
    				strNum += num.charAt(++i);
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
    private static void setConvertToNumberMap() {
    	convertToNumberMap.put("만", 10000);
    	convertToNumberMap.put("천", 1000);
    	convertToNumberMap.put("백", 100);
    	convertToNumberMap.put("십", 10);
    	
    	convertToNumberMap.put("이", 2);
    	convertToNumberMap.put("삼", 3);
    	convertToNumberMap.put("사", 4);
    	convertToNumberMap.put("오", 5);
    	convertToNumberMap.put("육", 6);
    	convertToNumberMap.put("칠", 7);
    	convertToNumberMap.put("팔", 8);
    	convertToNumberMap.put("구", 9);

    	convertToNumberMap.put("한", 1);
    	convertToNumberMap.put("두", 2);
    	convertToNumberMap.put("세", 3);
    	convertToNumberMap.put("네", 4);
    	convertToNumberMap.put("다섯", 5);
    	convertToNumberMap.put("여섯", 6);
    	convertToNumberMap.put("일곱", 7);
    	convertToNumberMap.put("여덟", 8);
    	convertToNumberMap.put("아홉", 9);
    	
    	convertToNumberMap.put("열", 10);
    	convertToNumberMap.put("스물", 20);
    	convertToNumberMap.put("서른", 30);
    	convertToNumberMap.put("마흔", 40);
    	convertToNumberMap.put("쉰", 50);
    	convertToNumberMap.put("예순", 60);
    	convertToNumberMap.put("일흔", 70);
    	convertToNumberMap.put("여든", 80);
    	convertToNumberMap.put("아흔", 90);
    }
}
