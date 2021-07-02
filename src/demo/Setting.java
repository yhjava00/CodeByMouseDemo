package demo;

import java.util.Map;
import java.util.Set;

public class Setting {

    public static void settingCodeInWriting(Map<String, Integer> keyWordSetMap, Set<String> advanceCheckSet, Map<String, Integer> convertToNumberMap) {
    	setKeyWordsSetMap(keyWordSetMap);
    	setAdvanceCheckSet(advanceCheckSet);
    	setConvertToNumberMap(convertToNumberMap);
    }
    
    private static void setKeyWordsSetMap(Map<String, Integer> keyWordSetMap) {

    	keyWordSetMap.put("출력", 0);
    	keyWordSetMap.put("변수", 1);
    	keyWordSetMap.put("넣", 1);
    	keyWordSetMap.put("블록", 1);
    	keyWordSetMap.put("반복", 2);
    	
    	keyWordSetMap.put("더하", 1);
    	keyWordSetMap.put("빼주", 1);
    	keyWordSetMap.put("곱", 1);
    	keyWordSetMap.put("나누", 1);
    }
    
    private static void setAdvanceCheckSet(Set<String> advanceCheckSet) {
    	advanceCheckSet.add("변수");
    }
    
    private static void setConvertToNumberMap(Map<String, Integer> convertToNumberMap) {
    	convertToNumberMap.put("만", 10000);
    	convertToNumberMap.put("천", 1000);
    	convertToNumberMap.put("백", 100);
    	convertToNumberMap.put("십", 10);
    	
    	convertToNumberMap.put("일", 1);
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
