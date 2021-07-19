package demo02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import common.ConnectAI;
import vo.Morpheme;

public class CodeCommend02 {

	private static Scanner sc = new Scanner(System.in);
	
	private static int tapLev = 2;
	private static boolean scannerOpen = false;
	
	public static Map<String, String> variableMap = new HashMap<String, String>();
	
	public static Map<String, Integer> convertToNumberMap = new HashMap<>();
	public static Map<String, String> operatorMap = new HashMap<String, String>();
	public static Map<String, String> variableTypeMap = new HashMap<String, String>();
	
	public static Set<String> comparisonOperatorSet = new HashSet<String>();
	
	public static void codeArrangement(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		String type = null;
		String name = null;
		int length = -1;
		
		if(type==null)
			type = setVariableType("지니 : 배열의 타입을 골라주세요.").trim();
		
		while(name==null||variableMap.containsKey(name)) {
			System.out.println("지니 : 배열의 이름을 정해주세요.");
			System.out.print("사용자 : ");
			name = sc.nextLine();
		}
		
		variableMap.put(name, type + "[]");
		
		while(length==-1) {
			System.out.println("지니 : 배열의 길이를 정해주세요.");
			System.out.print("사용자 : ");
			length = convertToNumber(sc.nextLine());
		}
		
		StringBuilder line = buildLine().append(type).append("[] ").append(name).append(" = ").append(type).append("[").append(length).append("];\n");
		
		code.add(line);
	}
	
	public static void codeMethod() {
		
	}

	public static void codeScanner(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		StringBuilder line = buildLine();
		
		Morpheme morp = null;
		
		for(int i=0; i<originMorpList.size(); i++) {
			if(originMorpList.get(i).text.equals("스캐너")&&i+2<originMorpList.size()) {
				morp = originMorpList.get(i+2);
				break;
			}
		}
		
		if(morp==null)
			return;
		
		if(morp.text.equals("열")||morp.text.equals("만들")) {
			line.append("Scanner 키위 = new Scanner(System.in);\n");
			variableMap.put("키위", "Scanner");
			scannerOpen = true;
		}else {
			line.append("키위.close();\n");
			scannerOpen = false;
		}
		
		code.add(line);
	}
	
	private static void scannerInput(StringBuilder line, String type) {
		switch (type) {
		case "int":
			line.append(" 키위.nextInt()");
			break;
		case "String":
			line.append(" 키위.sc.nextLine()");
			break;
		}
	}
	
	private static String scannerInput(String type) {
		switch (type) {
		case "int":
			return " 키위.nextInt()";
		case "String":
			return " 키위.sc.nextLine()";
		}
		
		return "키위.next()";
	}
	
	public static void codeCalculate(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		
		StringBuilder line = buildLine();
		
		String varToInclude = null;
		
		while(!variableMap.containsKey(varToInclude)) {
			System.out.println("지니 : 연산 결과를 담을 변수를 설정해주세요.");
			System.out.print("사용자02 : ");
			varToInclude = sc.nextLine();
		}
		
		String type = variableMap.get(varToInclude);
		
		line.append(varToInclude).append(" =");
		
		if(scannerOpen&&yesOrNo("스캐너에서 값을 입력받으시겠습니까?")) {
			scannerInput(line, type);
		}else {
			do {
				calculationTarget(line);
			}while(calculateOperator(line));
		}
		
		line.append(";\n");
		code.add(line);
	}
	
	private static void calculationTarget(StringBuilder calculation) {
		
		String calTarget = null;
		
		System.out.println("지니 : 연산할 값을 말해주세요.");
		System.out.print("사용자02 : ");
		calTarget = sc.nextLine();
		
		List<Morpheme> morpList = ConnectAI.morphemeSeparation(calTarget);
		for(int i=0; i<morpList.size(); i++) {
			if(morpList.get(i).text.equals("괄호")) {
				calculation.append(" (");
				System.out.println("작업 중 > " + calculation.toString().trim());
				calculationTarget(calculation);
				return;
			}
		}
		
		int isNum = convertToNumber(calTarget);
		
		if(isNum!=-1) {
			calculation.append(" ").append(isNum);
		}else if(variableMap.containsKey(calTarget)) {
			calculation.append(" ").append(calTarget);
		}else {
			calculation.append(" \"").append(calTarget).append("\"");
		}
		
		System.out.println("작업 중 > " + calculation.toString().trim());
	}
	
	private static boolean calculateOperator(StringBuilder calculation) {
		
		while(true) {
			
			System.out.println("지니 : 어떤 연산을 할까요?");
			System.out.print("사용자02 : ");
			List<Morpheme> morpList = ConnectAI.morphemeSeparation(sc.nextLine());
			
			for(int i=morpList.size()-1; i>=0; i--) {
				Morpheme morp = morpList.get(i);
				
				if(morp.text.equals("종료")||morp.text.equals("반영")) {
					return false;
				}
				
				if(morp.text.equals("괄호")) {
					calculation.append(" )");
					System.out.println("작업 중 > " + calculation.toString().trim());
					return calculateOperator(calculation);
				}
				
				if(operatorMap.containsKey(morp.text)) {
					calculation.append(" ").append(operatorMap.get(morp.text));
					
					System.out.println("작업 중 > " + calculation.toString().trim());
					return true;
				}
			}
		}
	}
	
	public static void codeCondition(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		
		String comparisonOperator = null;
		StringBuilder leftCondition = new StringBuilder();
		StringBuilder rightCondition = new StringBuilder();
		
		System.out.println("지니 : 왼쪽 조건을 설정해주세요.");

		do {
			calculationTarget(leftCondition);
		}while(calculateOperator(leftCondition));
		
		while(comparisonOperator==null) {
			
			String comparisonOperatorStr = "";
			
			System.out.println("지니 : 비교 연산자를 설정해주세요.");
			System.out.print("사용자02 : ");
			List<Morpheme> morpList = ConnectAI.morphemeSeparation(sc.nextLine());
			
			for(int i=0; i<morpList.size(); i++) {
				Morpheme morp = morpList.get(i);
				if(comparisonOperatorSet.contains(morp.text)) {
					comparisonOperatorStr += morp.text;
				}
			}
			
			switch (comparisonOperatorStr) {
			case "작":
			case "크같않":
			case "같크않":
				comparisonOperator = " <";
				break;
			case "크":
			case "작같않":
			case "같작않":
				comparisonOperator = " >";
				break;
			case "작같":
			case "같작":
			case "크않":
				comparisonOperator = " <=";
				break;
			case "크같":
			case "같크":
			case "작않":
				comparisonOperator = " >=";
				break;
			case "같":
				comparisonOperator = " ==";
				break;
			case "같않":
				comparisonOperator = " !=";
				break;
			}
		}
		
		System.out.println("지니 : 오른쪽 조건을 설정해주세요.");
		
		do {
			calculationTarget(rightCondition);
		}while(calculateOperator(rightCondition));
		

		StringBuilder line = buildLine().append("if (").append(leftCondition).append(comparisonOperator).append(rightCondition).append(") {\n");
		
		setLinePosition(code, line);
	}
	
	public static void codeVariable(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		
		Map<String, String> varInfo = varPretreatment(originalText, originMorpList);
		
		String type = varInfo.get("type");
		String name = varInfo.get("name");
		String value = varInfo.get("value");
		
		if(type==null) 
			type = setVariableType("지니 : 변수의 타입을 골라주세요.");
		
		if(name==null) {
			System.out.println("지니 : 변수의 이름을 정해주세요.");
			System.out.print("사용자02 : ");
			name = sc.nextLine();
		}

		StringBuilder line = buildLine().append(type).append(name);
		
		variableMap.put(name, type.trim());
		
		if(value==null) {
			String text = "변수의 값을 설정하시겠습니까?";
			
			if(yesOrNo(text)) {

				line.append(" =");
				
				if(scannerOpen&&yesOrNo("스캐너에서 값을 입력받으시겠습니까?")) {
					scannerInput(line, type.trim());
				}else {

					do {
						calculationTarget(line);
					}while(calculateOperator(line));
					
				}
			}
		}else {
			line.append(" = ").append(value);
		}
		
		line.append(";\n");
		
		code.add(line);
		
	}
	
	private static Map<String, String> varPretreatment(StringBuilder originalText, List<Morpheme> originMorpList) {
		
		Map<String, String> varInfo = new HashMap<String, String>();
		
		Morpheme startCutMorp = null;
		Morpheme endCutMorp = null;
		
		int i=0;
		for(; i<originMorpList.size(); i++) {
			if(originMorpList.get(i).text.equals("변수")) {
				if(i+1<originMorpList.size()) {
					startCutMorp = originMorpList.get(i+1);
				}
				break;
			}
		}
		
		String type = null;
		
		if(i-2>=0) {
			type = variableTypeMap.get(originMorpList.get(i-2).text);
			varInfo.put("type", type);
		}
		
		String name = null;
		
		for(int j=i+1; j<originMorpList.size(); j++) {
			endCutMorp = originMorpList.get(j);
			if(endCutMorp.type.equals("JKB")||endCutMorp.type.equals("JKO")) {
				endCutMorp = originMorpList.get(j);
				int startByte = (int) startCutMorp.position;
				int byteLength = (int) endCutMorp.position - startByte;
				name = new String(originalText.toString().getBytes(), startByte, byteLength);
				name = name.replace(" ", "");
				i = j+1;
				break;
			}
		}
		
		if(name.equals("")) 
			name = null;
		
		varInfo.put("name", name);
		
		if(type==null&&i>=originMorpList.size())
			return varInfo;
		
		String value = null;
		
		if(originMorpList.get(i).text.equals("입력")&&variableMap.containsKey("키위")) {
			value = scannerInput(type.trim()).trim();
			varInfo.put("value", value);
			return varInfo;
		}
		
		startCutMorp = originMorpList.get(i);
		
		for(int j=i; j<originMorpList.size(); j++) {
			endCutMorp = originMorpList.get(j);
			if(endCutMorp.type.equals("JKO")) {
				endCutMorp = originMorpList.get(j);
				int startByte = (int) startCutMorp.position;
				int byteLength = (int) endCutMorp.position - startByte;
				value = new String(originalText.toString().getBytes(), startByte, byteLength);
				break;
			}
		}
		
		if(value==null)
			return varInfo;
		
		if(type.equals("int ")) {
			int conNum = convertToNumber(value); 
			if(conNum!=-1) {
				value = Integer.toString(conNum);
			}else {
				value = null;
			}
		}
		
		varInfo.put("value", value);
		
		return varInfo;
	}
	
	private static String setVariableType(String text) {
		
		String type = null;
		Map<String, Object> result = null;
		
		String uuid = ConnectAI.openDialog("variableType");
		
		do {
			System.out.println(text);
			System.out.print("사용자02 : ");
			result = ConnectAI.dialog(uuid);
		}while(!((String) result.get("state")).equals("end"));
		
		String systemText = (String) result.get("system_text");
		systemText = systemText.replace("\n", "");
		
		switch (systemText) {
		case "정수":
		case "정수형":
			type = "int ";
			break;
		case "실수":
		case "실수형":
			type = "double ";
			break;
		case "문자":
		case "문자형":
			type = "String ";
			break;
		case "논리":
		case "논리형":
			type = "boolean ";
			break;
		}
		
		return type;
	}
	
	public static void codeBlock(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		int i=0;
		for(; i<originMorpList.size(); i++) {
			Morpheme morp = originMorpList.get(i);
			if(morp.text.equals("블록")) 
				break;
		}
		
		if(i+2>=originMorpList.size())
			return;
		
		Morpheme actionMorpheme = originMorpList.get(i+2);
		
		StringBuilder line = null;
		
		if(actionMorpheme.text.equals("열")||actionMorpheme.text.equals("만들")) {
			line = buildLine().append("{\n");
			tapLev++;
		}else {
			tapLev--;
			line = buildLine().append("}\n");
		}
		
		code.add(line);
	}
	
	public static void codeFor(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		String[] repeatRange = null;
		
		int[] repeatRangeNum = {0, 0};
		
		do {
			repeatRange = new String[] {"", ""};
			System.out.println("지니 : 몇 번 반복할까요?");
			
			System.out.print("사용자02 : ");
			List<Morpheme> morpList = ConnectAI.morphemeSeparation(sc.nextLine());
			
			find : for(int i=0; i<morpList.size(); i++) {
				Morpheme morp = morpList.get(i);
				
				switch (morp.text) {
				case "번":
					repeatRange[1] = cutOutNum(morpList, i);
					
					if(convertToNumber(repeatRange[1])==-1) {
						repeatRange[1] = "";
						continue find;
					} else {
						repeatRange[0] = "null";
						break find;
					}
				case "부터":
					repeatRange[0] = cutOutNum(morpList, i);
					if(convertToNumber(repeatRange[0])==-1) {
						repeatRange[0] = "";
						continue find;
					}else if(!repeatRange[1].equals("")) {
						break find;
					}
					break;
				case "까지":
					repeatRange[1] = cutOutNum(morpList, i);
					if(convertToNumber(repeatRange[1])==-1) {
						repeatRange[1] = "";
						continue find;
					}else if(!repeatRange[0].equals("")) {
						break find;
					}
					break;
				}
			}
		} while(repeatRange[0].equals("")||repeatRange[1].equals(""));

		repeatRangeNum[0] = convertToNumber(repeatRange[0]);
		repeatRangeNum[1] = convertToNumber(repeatRange[1]);

		StringBuilder line = buildLine();
		
		if(repeatRangeNum[0]==-1)
			line.append("for(int 귤=0; 귤<").append(repeatRangeNum[1]).append("; 귤++) {\n");
		else
			line.append("for(int 귤=").append(repeatRangeNum[0]).append("; 귤<=").append(repeatRangeNum[1]).append("; 귤++) {\n");
		
		setLinePosition(code, line);
	}
	
	private static boolean yesOrNo(String text) {
		
		Map<String, Object> result = null;
		String state = "";
		
		String uuid = ConnectAI.openDialog("yesOrNo");
		
		do {
			System.out.println("지니 : " + text);
			System.out.print("사용자02 : ");
			result = ConnectAI.dialog(uuid);
			state = (String) result.get("state");
		} while(!state.equals("end"));
		
		String systemText = (String) result.get("system_text");
		
		if(systemText.equals(" yes\n")) 
			return true;
		else
			return false;
	}
	
	public static void setLinePosition(List<StringBuilder> code, StringBuilder line) {
		
		if(!yesOrNo("작업 구역을 설정하시겠습니까?")) {
			code.add(line);
			tapLev++;
			return;
		}
		
		String[] linePosition = {"", ""};
		
		while(linePosition[0].equals("")) {
			System.out.println("지니 : 어디에서 작업하시겠습니까?");
			
			System.out.print("사용자02 : ");
			List<Morpheme> morpList = ConnectAI.morphemeSeparation(sc.nextLine());
			
			find : for(int i=0; i<morpList.size(); i++) {
				Morpheme morp = morpList.get(i);
				
				switch (morp.text) {
				case "줄":
					int j=i;
					if(j<1)
						break;
					do {
						morp = morpList.get(--j);
					}while(j>0&&!(morp.type.equals("NR")||morp.type.equals("MM")));
					
					String position = cutOutNum(morpList, j+1);
					
					if(convertToNumber(position)==-1)
						continue;
					
					if(linePosition[0].equals("")) {
						linePosition[0] = position;
					}else {
						linePosition[1] = position;
						break find;
					}
					break;
				}
			}
		}
		

		int[] linePositionNum = new int[2];
		
		linePositionNum[0] = convertToNumber(linePosition[0]);
		linePositionNum[1] = convertToNumber(linePosition[1]);
		
		if(linePositionNum[0]>linePositionNum[1]) {
			int temp = linePositionNum[0];
			linePositionNum[0] = linePositionNum[1];
			linePositionNum[1] = temp;
		}

		if(linePositionNum[0]==-1)
			linePositionNum[0] = linePositionNum[1];
		
		linePositionNum[0]--;
		
		for(int i=linePositionNum[0]; i<linePositionNum[1]; i++) {
			code.get(i).insert(0, "\t");
		}
		
		code.add(linePositionNum[0], line);
		code.add(linePositionNum[1]+1, buildLine().append("}\n"));
	}
	
	public static String cutOutNum(List<Morpheme> morpList, int idx) {
		String num = "";
		Morpheme morp = null;
		
		for(int j=idx-1; j>=0; j--) {
			morp = morpList.get(j);
			
			if(!morp.type.equals("NNP")&&!morp.type.equals("NR")&&!morp.type.equals("MM"))
				break;
			
			num = morp.text + num;
		}
		
		return num;
	}

	public static void codePrint(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> originMorpList) {
		
		String printValue = printPretreatment(originalText, originMorpList);
		
		if(printValue==null) {
			System.out.println("지니 : 어떤 내용을 출력할까요?");
			
			System.out.print("사용자02 : ");
			printValue = sc.nextLine();
		}
		
		StringBuilder line = buildLine().append("System.out.println(");
		
		if(variableMap.containsKey(printValue))
			line.append(printValue).append(");\n");
		else
			line.append("\"").append(printValue).append("\");\n");
		
		code.add(line);
	}
	
	private static String printPretreatment(StringBuilder originalText, List<Morpheme> originMorpList) {
		
		String printVal = null;
		
		int i=0;
		for(; i<originMorpList.size(); i++) {
			if(originMorpList.get(i).text.equals("출력"))
				break;
		}
		
		Morpheme morp = null;
		
		if(i==0||!(morp = originMorpList.get(i-1)).type.equals("JKO"))
			return printVal;
		
		int printValByteLen = (int) morp.position;
		
		printVal = new String(originalText.toString().getBytes(), 0, printValByteLen);
		
		return printVal.trim();
		
	}
	
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
	
	public static StringBuilder buildLine() {
    	StringBuilder line = new StringBuilder();
    	for(int i=0; i<tapLev; i++) {
			line.append("\t");
		}
    	return line;
    }
	
}
