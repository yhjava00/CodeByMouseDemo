package demo02;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Setting02 {
    
	public static void settingCodeInWriting(Map<String, Integer> keyWordMap) {
		setKeyWordMap(keyWordMap);
		setConvertToNumberMap(CodeCommend02.convertToNumberMap);
	}
	
	private static void setKeyWordMap(Map<String, Integer> keyWordMap) {
		
		keyWordMap.put("줄", 0);
		
    	keyWordMap.put("스캐너", 0);
    	keyWordMap.put("입력", 0);
    	
    	keyWordMap.put("종료", 0);

    	keyWordMap.put("출력", 0);
    	
    	keyWordMap.put("반복", 2);
    	keyWordMap.put("번", 2);
    	
    	keyWordMap.put("조건", 2);
    	keyWordMap.put("면", 2);
    	keyWordMap.put("으면", 2);
    	
    	keyWordMap.put("변수", 1);
    	
    	keyWordMap.put("넣", 1);
    	keyWordMap.put("담아주", 1);
    	keyWordMap.put("담", 1);
    	
    	keyWordMap.put("더하", 1);
    	keyWordMap.put("빼주", 1);
    	keyWordMap.put("곱", 1);
    	keyWordMap.put("나누", 1);
    	keyWordMap.put("나눠줘", 1);
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
