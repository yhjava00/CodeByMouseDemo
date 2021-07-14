package demo02;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Setting02 {
    
	public static void settingCodeInWriting(Set<String> keyWordSet) {
		setKeyWord(keyWordSet);
		setConvertToNumberMap(CodeCommend02.convertToNumberMap);
	}
	
	private static void setKeyWord(Set<String> keyWordSet) {
		
    	keyWordSet.add("출력");
    	
    	keyWordSet.add("반복");
    	
    	keyWordSet.add("블록");
    	
    	keyWordSet.add("변수");
    	
    	keyWordSet.add("조건");
	}
	
	private static void setConvertToNumberMap(Map<String, Integer> convertToNumberMap) {
    	convertToNumberMap.put("만", 10000);
    	convertToNumberMap.put("천", 1000);
    	convertToNumberMap.put("백", 100);
    	convertToNumberMap.put("십", 10);

    	convertToNumberMap.put("영", 0);
    	
    	convertToNumberMap.put("일", 1);
    	convertToNumberMap.put("이", 2);
    	convertToNumberMap.put("삼", 3);
    	convertToNumberMap.put("사", 4);
    	convertToNumberMap.put("오", 5);
    	convertToNumberMap.put("육", 6);
    	convertToNumberMap.put("칠", 7);
    	convertToNumberMap.put("팔", 8);
    	convertToNumberMap.put("구", 9);

    	convertToNumberMap.put("첫", 1);
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
    	convertToNumberMap.put("스무", 20);
    	convertToNumberMap.put("서른", 30);
    	convertToNumberMap.put("마흔", 40);
    	convertToNumberMap.put("쉰", 50);
    	convertToNumberMap.put("예순", 60);
    	convertToNumberMap.put("일흔", 70);
    	convertToNumberMap.put("여든", 80);
    	convertToNumberMap.put("아흔", 90);
    }
	
}
