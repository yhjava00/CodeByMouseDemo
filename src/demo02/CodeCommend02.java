package demo02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeCommend02 {

	public static Map<String, Integer> convertToNumberMap = new HashMap<>();
	
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
	
	
}
