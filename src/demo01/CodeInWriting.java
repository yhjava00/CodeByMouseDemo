package demo01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import common.ConnnectAI;
import vo.KeyWord;
import vo.Morpheme;
import vo.Variable;

public class CodeInWriting {
	
	private static List<StringBuilder> code = new LinkedList<StringBuilder>();
	
	private static Map<String, Integer> keyWordSetMap = new HashMap<>();
	private static Map<String, Variable> variableMap = new HashMap<>();
	private static Set<String> compareSet = new HashSet<>();
	
	private static StringBuilder requestText;
    public static void main(String[] args) {
    	
    	Setting.settingCodeInWriting(code, keyWordSetMap, compareSet, CodeCommend.convertToNumberMap);
    	
        Scanner sc = new Scanner(System.in);
        
//        requestText.append("변수 사과에 백을 담고. 변수 바나나에 천을 넣어줘. 바나나를 사과에 백 번 반복해서 나누어줘. 바나나에 사과를 백 번 반복해서 곱해줘. 바나나에 천이백삼십이에 이백십을 더한 것을 담아줘. 바나나를 출력해줘.");
//        requestText.append("변수 사과에 일을 담고 변수 바나나에 일을 담아줘. 변수 망고에 일을 담아줘. 사과를 출력해줘. 바나나를 출력해줘. 망고에 사과에 바나나를 더해서 넣고. 망고를 출력, 사과에 바나나를 넣고 바나나에 망고를 넣는 것을 여덟 번 반복해줘.");
        
        while(true) {
        	
        	requestText = new StringBuilder();
        	
        	System.out.print("명령(demo01) : ");
        	requestText.append(sc.nextLine());
        	
        	if(requestText.toString().equals("종료")) {
        		break;
        	}

        	List<Map<String, Object>> sentenceInfoList = ConnnectAI.connect(requestText.toString());
        	
        	buildCode(sentenceInfoList);
        }
        
        sc.close();
        
    }
    
    private static void buildCode(List<Map<String, Object>> sentenceInfoList) {
    	
    	int beforeSentenceLength = 0;
    	for(Map<String, Object> sentenceInfo : sentenceInfoList) {

    		Map<String, Object> prepareToCodeInfo = prepareToCode(sentenceInfo, beforeSentenceLength);
    		
    		beforeSentenceLength += (int) prepareToCodeInfo.get("beforeSentenceLength");
    		
    		List<KeyWord> keyWordList = (List<KeyWord>) prepareToCodeInfo.get("keyWordList");
    		int keyWordListSize = keyWordList.size();
    		
    		for(int i=0; i<keyWordListSize; i++) {
    			
    			KeyWord keyWord = keyWordList.get(i);
    			
				switch (keyWord.text) {
				case "스캐너":
					variableMap.put("키위", new Variable("Scanner", "키위", "Scanner"));
					CodeCommend.codeScanner(code, keyWord);
					break;
				case "입력":
					CodeCommend.codeInput(code, keyWord, keyWordList);
					break;
				case "면":
				case "으면":
				{
					if(i!=keyWordListSize-1) {
						String condition = CodeCommend.writeCondition(keyWordList.get(++i));
						Map<String, Object> conditionalInfo = (Map<String, Object>) keyWord.info;
						conditionalInfo.put("targetValue", condition);
					}
					String[] selectedLine = (String[]) prepareToCodeInfo.get("selectedLine");
					CodeCommend.codeConditional(code, keyWord, selectedLine);
				}
					break;
				case "변수":
					CodeCommend.codeVariable(code, keyWord, variableMap.get(keyWord.info));
					break;
				case "출력":
					CodeCommend.codePrint(code, keyWord, variableMap);
					break;
				case "반복":
					String[] selectedLine = (String[]) prepareToCodeInfo.get("selectedLine");
					CodeCommend.codeFor(code, keyWord, selectedLine);
					break;
				case "종료":
					CodeCommend.codeReturn(code);
					break;
				default:
					CodeCommend.codeOperator(code, keyWord);
					break;
				}
    		}
    	}
        
        StringBuilder finalCode = new StringBuilder();
        int i=0;
        for(; i<code.size(); i++) {
        	finalCode.append(i+1).append(". ").append(code.get(i));
        }
        
        finalCode.append(++i).append(". ").append("\t}").append("\n");
        finalCode.append(++i).append(". ").append("}");
        
        System.out.println(finalCode);
    }
    
    private static Map<String, Object> prepareToCode(Map<String, Object> sentenceInfo, int beforeSentenceLength) {
    	
    	boolean inputValue = true;
    	
    	Map<String, Object> prepareToCodeInfo = new HashMap<>();
    	
    	List<KeyWord> keyWordList = new ArrayList<>();
    	
        StringBuilder sentenceText = new StringBuilder().append((String) sentenceInfo.get("text"));
        
        List<Morpheme> morpList = (List<Morpheme>) sentenceInfo.get("morpList");
        for(int i=0; i<morpList.size(); i++) {
        	Morpheme morpheme = morpList.get(i);
        	
			if(!keyWordSetMap.containsKey(morpheme.text))
				continue;
			
			KeyWord keyWord = null;
			
			switch (morpheme.text) {
			case "스캐너":
				Morpheme actionMorp = morpList.get(i+2);
				
				keyWord = new KeyWord(morpheme.text, actionMorp, keyWordSetMap.get(morpheme.text));
				break;
			case "입력":
			{
				int j=i;
				while(j>0&&!morpList.get(--j).type.equals("JKB")) {
				}
				
				Morpheme variableMorp = morpList.get(j-1);
				Morpheme typeMorp = morpList.get(i-2);
				
				Morpheme[] inputInfo = {variableMorp, typeMorp}; 
				
				keyWord = new KeyWord(morpheme.text, inputInfo, keyWordSetMap.get(morpheme.text));
			}
				break;
			case "면":
			case "으면":
			{
				String targetValue = "";
				String compareValue = "";
				String compareOP = "";
				boolean negative = false;
				
				int j = i-1;
				
				Morpheme compareMorp = morpList.get(j);
				
				compareOP = compareMorp.text;
				
				if(compareOP.equals("않")) {
					negative = true;
					j -= 2;
					compareOP = morpList.get(j).text;
				}
				
				compareMorp = morpList.get(j-2);
				if(compareOP.equals("같")&&compareSet.contains(compareMorp.text)) {
					compareOP += compareMorp.text;
				}
				
				while(targetValue.equals("")||compareValue.equals("")) {
					
					compareMorp = morpList.get(j--);
					
					if(compareMorp.type.equals("JKB")||compareMorp.type.equals("MAG")) {
						while(j>0&&morpList.get(j).type.equals("NNG")||morpList.get(j).type.equals("MM")||morpList.get(j).type.equals("NP")) {
							compareValue = morpList.get(j--).text + compareValue;
						}
					}else if(compareMorp.type.equals("JKS")) {
						while(j>0&&morpList.get(j).type.equals("NNG")||morpList.get(j).type.equals("MM")||morpList.get(j).type.equals("NP")) {
							targetValue = morpList.get(j--).text + targetValue;
						}
					}
				}
				
				Map<String, Object> conditionalInfo = new HashMap<>();
				conditionalInfo.put("targetValue", targetValue);
				conditionalInfo.put("compareValue", compareValue);
				conditionalInfo.put("compareOP", compareOP);
				conditionalInfo.put("negative", negative);
				
				keyWord = new KeyWord(morpheme.text, conditionalInfo, keyWordSetMap.get(morpheme.text));
			}
				break;
			case "줄":
			{
				int j = i-2;
				Morpheme lineMorp = morpList.get(j);
				String line = "";
				
				do {
					line = lineMorp.text + line;
					if(j==0)
						break;
					lineMorp = morpList.get(--j);					
				}while(lineMorp.type.equals("NR")||lineMorp.type.equals("MM"));
				
				String[] selectedLine = (String[]) prepareToCodeInfo.get("selectedLine");

				if(selectedLine==null) {
					selectedLine = new String[] {line, ""}; 
				}else {
					selectedLine[1] = line;
				}
				
				prepareToCodeInfo.put("selectedLine", selectedLine);
				
				
			}
				continue;
			case "변수":
				inputValue = false;
				String variableName = morpList.get(i+1).text;
				if(!variableMap.containsKey(variableName)) {
					Morpheme startVariableValue = morpList.get(i+3);
					Morpheme endVariableValue = null;
					for(int j=i+4; j<morpList.size(); j++) {
						endVariableValue = morpList.get(j);
						if(endVariableValue.type.equals("JKO")&&morpList.get(j+1).text.equals("담아주")
								||morpList.get(j+1).text.equals("담")||morpList.get(j+1).text.equals("넣")
								||morpList.get(j+1).text.equals("입력"))
							break;
					}
					
					String subWord = "";
					int startCutIdx = 0;
					do {
						startCutIdx = sentenceText.indexOf(startVariableValue.text, startCutIdx+1);
						subWord = sentenceText.substring(0, startCutIdx);
					} while(startVariableValue.position!=subWord.getBytes().length+beforeSentenceLength);
					
					subWord = "";
					int endCutIdx = 0;
					do {
						endCutIdx = sentenceText.indexOf(endVariableValue.text, endCutIdx+1);
						subWord = sentenceText.substring(0, endCutIdx);
					} while(endVariableValue.position!=subWord.getBytes().length+beforeSentenceLength);
					
					String variableValue = sentenceText.substring(startCutIdx, endCutIdx);
					
					variableMap.put(variableName, new Variable("int", variableName, variableValue.trim()));

					keyWord = new KeyWord(morpheme.text, variableName, keyWordSetMap.get(morpheme.text));
				}
				
				break;
			case "출력":
				Morpheme cutMorp = morpheme;

				for(int j=i-1; j>=0; j--) {
					cutMorp = morpList.get(j);
					if(cutMorp.type.equals("JKO")) 
						break;
				}
				
				String cutWord = cutMorp.text;

				String subWord = "";
				int cutIdx = 0;
				do {
					cutIdx = sentenceText.indexOf(cutWord, cutIdx+1);
					subWord = sentenceText.substring(0, cutIdx);
				} while(cutMorp.position!=subWord.getBytes().length+beforeSentenceLength);

				keyWord = new KeyWord(morpheme.text, subWord.trim(), keyWordSetMap.get(morpheme.text));
				break;
			case "반복":
			{
				String[] forInfo = {"", ""};
				int j = i-1;
				if(morpList.get(j).text.equals("번")) {
					j--;
					Morpheme repeatMorp = morpList.get(j);
					String repeat = "";
					
					do {
						repeat = repeatMorp.text + repeat;
						if(j==0)
							break;
						repeatMorp = morpList.get(--j);					
					}while(repeatMorp.type.equals("NR")||repeatMorp.type.equals("MM"));
					
					forInfo[0] = repeat;
				}else {
					do {
						Morpheme circuitMorp = morpList.get(j--);
						if(circuitMorp.text.equals("부터")) {
							circuitMorp = morpList.get(j);
							
							do {
								forInfo[0] = circuitMorp.text + forInfo[0];
								if(j==0)
									break;
								circuitMorp = morpList.get(--j);					
							}while(circuitMorp.type.equals("NR")||circuitMorp.type.equals("MM"));
						}else if(circuitMorp.text.equals("까지")) {
							circuitMorp = morpList.get(j);
							
							do {
								forInfo[1] = circuitMorp.text + forInfo[1];
								if(j==0)
									break;
								circuitMorp = morpList.get(--j);					
							}while(circuitMorp.type.equals("NR")||circuitMorp.type.equals("MM"));
						}
					}while(j>=0&&forInfo[0].equals("")||forInfo[1].equals(""));
				}
				
				keyWord = new KeyWord(morpheme.text, forInfo, keyWordSetMap.get(morpheme.text));
			}
				break;
			case "종료":
				keyWord = new KeyWord(morpheme.text, null, keyWordSetMap.get(morpheme.text));
				break;
			default:
				
				if((morpheme.text.equals("넣")||morpheme.text.equals("담아주")||morpheme.text.equals("담"))&&!inputValue) {
					keyWord = new KeyWord(morpheme.text, null, keyWordSetMap.get(morpheme.text));
					break;
				}
				
				String op = morpheme.text;
				
				if(op.equals("나누")&&morpList.get(i+2).text.equals("나머지")) {
					op = "나머지";
				}
				
				inputValue = false;
				
				String toVal = "";
				String opVal1 = "";
				String opVal2 = "";
				
				int j = 0;
				
				do {
					Morpheme morp = morpList.get(j++);
					switch (morp.type) {
					case "JKB":
						if(toVal.equals("")&&!morp.text.equals("로")) {
							toVal = morpList.get(j-2).text;
						}else {
							int k = j-2;
							Morpheme opVal1Morp = morpList.get(k);
							opVal1 = opVal1Morp.text;
							if(opVal1Morp.type.equals("NR")||opVal1Morp.type.equals("MM")||opVal1Morp.type.equals("NNB")) {
								opVal1Morp = morpList.get(--k);
								while(opVal1Morp.type.equals("NR")||opVal1Morp.type.equals("MM")) {
									opVal1 = opVal1Morp.text + opVal1;
									opVal1Morp = morpList.get(--k);
								}
							}
						}
						break;
					case "JKO":
						int k = j-2;
						Morpheme opVal2Morp = morpList.get(k);
						opVal2 = opVal2Morp.text;
						if(opVal2Morp.type.equals("NR")||opVal2Morp.type.equals("MM")||opVal2Morp.type.equals("NNB")) {
							opVal2Morp = morpList.get(--k);
							while(opVal2Morp.type.equals("NR")||opVal2Morp.type.equals("MM")) {
								opVal2 = opVal2Morp.text + opVal2;
								opVal2Morp = morpList.get(--k);
							}
						}
						break;
					}
				}while(j<i&&toVal.equals("")||opVal2.equals(""));
				
				if(toVal.equals("")) {
					toVal = opVal2;
					opVal2 = opVal1;
				}
				
				keyWord = new KeyWord(op, new String[] {toVal, opVal1, opVal2}, keyWordSetMap.get(morpheme.text));
				break;
			}

			keyWordList.add(keyWord);
		}
        
        Collections.sort(keyWordList, new Comparator<KeyWord>() {
		   	@Override
        	public int compare(KeyWord o1, KeyWord o2) {
        		return o2.priority - o1.priority;
        	}
		});
        
        prepareToCodeInfo.put("keyWordList", keyWordList);
        prepareToCodeInfo.put("beforeSentenceLength", sentenceText.toString().getBytes().length);
        
//        System.out.println("target > " + prepareToCodeInfo.get("target"));
//        System.out.println("repeat > " + prepareToCodeInfo.get("repeat"));
//        System.out.println("keyWordList > " + prepareToCodeInfo.get("keyWordList"));
        
        return prepareToCodeInfo;
    }
    
    
    
}
