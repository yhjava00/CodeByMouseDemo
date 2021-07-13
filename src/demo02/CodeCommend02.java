package demo02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.ConnectAI;
import vo.Morpheme;

public class CodeCommend02 {

	private static Scanner sc = new Scanner(System.in);
	
	public static int tapLev = 2;
	
	public static Map<String, Integer> convertToNumberMap = new HashMap<>();
	
	public static void codePrint(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> morpList) {
		
		String printValue = null;
		
		if(printValue==null) {
			System.out.println("지니 : 어떤 내용을 출력할까요?");
			
			System.out.print("사용자02 : ");
			printValue = sc.nextLine();
		}
		
		StringBuilder line = buildLine().append("System.out.println(\"").append(printValue).append("\");\n");
		
		code.add(line);
	}
	
	public static void codeFor(List<StringBuilder> code, StringBuilder originalText, List<Morpheme> OriginMorpList) {
		String[] repeatRange = null;
		String[] linePosition = null;
		
		int[] repeatRangeNum = {0, 0};
		int[] linePositionNum = {code.size()-1, code.size()-1};
		
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
		
		Map<String, Object> result = null;
		String conversationState = "";
		String uuid = ConnectAI.openDialog();
		
		do {
			System.out.println("지니 : 작업 구역을 따로 설정하시겠습니까?");
			System.out.print("사용자02 : ");
			result = ConnectAI.dialog(uuid);
			conversationState = (String) result.get("state");
		} while(!conversationState.equals("end"));
		
		String systemText = (String) result.get("system_text");
		
		if(systemText.equals(" yes\n")) {
			linePosition = setLinePosition();
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
		}else {
			code.add(line);
			tapLev++;
		}
		
		
		
	}
	
	public static String[] setLinePosition() {
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
		return linePosition;
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
