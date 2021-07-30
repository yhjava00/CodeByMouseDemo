package demo02;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Setting02 {
    
	public static void settingCodeInWriting(Set<String> keyWordSet) {
		setKeyWord(keyWordSet);
		setConvertToNumberMap(CodeCommend02.convertToNumberMap);
		setOperator(CodeCommend02.operatorMap);
		setComparisonOperator(CodeCommend02.comparisonOperatorSet);
		setVariableType(CodeCommend02.variableTypeMap);
		
		CodeCommend02.commendToken = new StringTokenizer("스캐너를 열어줘/정수형 변수 알파를 만들어줘/응/응/조건/알파/반영/같으면/사/반영/응/또는/알파/반영/같으면/칠/반영/아니/마이너스 일을 출력해줘/블록을 나가/응/응/알파/나눈 나머지/오/반영/같으면/영/반영/아니/출력해줘/응/알파/나누기/오/반영/블록을 나가/응/응/알파/나눈 나머지/오/반영/같으면/일/반영/응/또는/알파/나눈 나머지/오/반영/같으면/삼/반영/아니/출력해줘/응/괄호/알파/나누기/오/괄호/더하기/일/반영/블록을 나가/응/응/알파/나눈 나머지/오/반영/같으면/이/반영/응/또는/알파/나눈 나머지/오/반영/같으면/사/반영/아니/출력/응/괄호/알파/나누기/오/괄호/더하기/이/반영/블록을 나가/아니/종료", "/");
		
		CodeCommend02.inputTokenList.add(new StringTokenizer("18", " "));
		CodeCommend02.inputTokenList.add(new StringTokenizer("4", " "));
		CodeCommend02.inputTokenList.add(new StringTokenizer("6", " "));
		CodeCommend02.inputTokenList.add(new StringTokenizer("9", " "));
		CodeCommend02.inputTokenList.add(new StringTokenizer("11", " "));
	}
	
	private static void setKeyWord(Set<String> keyWordSet) {
		
    	keyWordSet.add("출력");
    	
    	keyWordSet.add("반복");
    	
    	keyWordSet.add("블록");
    	
    	keyWordSet.add("변수");
    	
    	keyWordSet.add("조건");
    	
    	keyWordSet.add("연산");
    	
    	keyWordSet.add("스캐너");
    	
    	keyWordSet.add("배열");
    	
    	keyWordSet.add("함수");
    	keyWordSet.add("메서드");
    	keyWordSet.add("메소드");
    	
	}
	
	private static void setVariableType(Map<String, String> variableTypeMap) {
		variableTypeMap.put("정수", "int ");
		variableTypeMap.put("실수", "double ");
		variableTypeMap.put("문자", "String ");
		variableTypeMap.put("논리", "boolean ");
		
	}
	
	private static void setComparisonOperator(Set<String> comparisonOperatorSet) {
		comparisonOperatorSet.add("작");
		comparisonOperatorSet.add("크");
		comparisonOperatorSet.add("같");
		comparisonOperatorSet.add("않");
	}
	
	private static void setOperator(Map<String, String> operatorMap) {
		operatorMap.put("더하", "+");
		operatorMap.put("빼", "-");
		operatorMap.put("빼주", "-");
		operatorMap.put("나눠줘", "/");
		operatorMap.put("나누", "/");
		operatorMap.put("곱", "*");
		operatorMap.put("나머지", "%");
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
