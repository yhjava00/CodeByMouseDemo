package demo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import vo.Dependency;
import vo.KeyWord;
import vo.Morpheme;
import vo.Variable;

public class CodeInWriting {
	
	private static Map<String, Integer> keyWordSetMap = new HashMap<>();
	private static Set<String> advanceCheckSet = new HashSet<>();
	private static Map<String, Variable> variableMap = new HashMap<>();
	
	private static StringBuilder requestText;
    public static void main(String[] args) {

    	Setting.settingCodeInWriting(keyWordSetMap, advanceCheckSet, CodeCommend.convertToNumberMap);
    	
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";         
        String accessKey = "";
        String analysisCode = "srl";        
        
        requestText = new StringBuilder();
        requestText.append("나를 찾아줘를 출력해줘.");
//        requestText.append("정수형 변수 사과에 백을 담고. 정수형 변수 바나나에 이천을 담아서 정수형 변수 수박에 사과와 바나나를 더한 것을 담아주고. 바나나를 출력해줘.");

        
        Gson gson = new Gson();
        
        System.out.println(requestText);
        System.out.println();
        
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
 
        argument.put("analysis_code", analysisCode);
        argument.put("text", requestText.toString());
 
        request.put("access_key", accessKey);
        request.put("argument", argument);
 
        URL url;
        Integer responseCode = null;
        String responBodyJson = null;
        Map<String, Object> responeBody = null;
 
        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
 
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();
 
            responseCode = con.getResponseCode();
            
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            StringBuffer sb = new StringBuffer();
 
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            responBodyJson = sb.toString();
            
            // http 요청 오류 시 처리
            if ( responseCode != 200 ) {
                // 오류 내용 출력
                System.out.println("[error http] " + responBodyJson);
                return ;
            }
            
            responeBody = gson.fromJson(responBodyJson, Map.class);
            Integer result = ((Double) responeBody.get("result")).intValue();
            Map<String, Object> returnObject;
            List<Map> sentences;
 
            // 분석 요청 오류 시 처리
            if ( result != 0 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responeBody.get("result"));
                return ;
            }
            
            System.out.println(responBodyJson);
            System.out.println();
            
            // 분석 결과 활용
            returnObject = (Map<String, Object>) responeBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");
            
            List<Map<String, Object>> sentenceInfoList = new ArrayList<>();
            
            for( Map<String, Object> sentence : sentences ) {
            	
            	Map<String, Object> sentenceInfo = new HashMap<>();
            	
            	List<Morpheme> morpList = new ArrayList<>();
                // 형태소 분석기 결과 수집 및 정렬
                List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
                for(Map<String, Object> morpheme : morphologicalAnalysisResult ) {
                    double id = (double) morpheme.get("id");
                	String lemma = (String) morpheme.get("lemma");
                    String type = (String) morpheme.get("type");
                    double position = (double) morpheme.get("position");
                    
                    morpList.add(new Morpheme(id, lemma, type, position));
                }
                
                List<Dependency> dependencyList = new ArrayList<>();
                List<Map<String, Object>> dependencyResult = (List<Map<String,Object>>) sentence.get("dependency");
                for(Map<String, Object> dependency : dependencyResult) {
                	double id = (double) dependency.get("id");
            		String text = (String) dependency.get("text");
            		double head = (double) dependency.get("head");
            		String label = (String) dependency.get("label");
            		List<Double> mod = (List<Double>) dependency.get("mod");
                	
            		dependencyList.add(new Dependency(id, text, head, label, mod));
                }
                
                String sentenceText = (String) sentence.get("text");
                
                sentenceInfo.put("text", sentenceText);
                sentenceInfo.put("morpList", morpList);
                sentenceInfo.put("dependencyList", dependencyList);
                
                sentenceInfoList.add(sentenceInfo);
            }
            
            showProcessingSentence(sentenceInfoList);

            buildCode(sentenceInfoList);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void buildCode(List<Map<String, Object>> sentenceInfoList) {
    	List<StringBuilder> code = new LinkedList<StringBuilder>();
        
    	code.add(new StringBuilder().append("public class Main {").append("\n"));
    	code.add(new StringBuilder().append("\tpublic static void main(String[] args) {").append("\n"));
    	
    	int beforeSentenceLength = 0;
    	for(Map<String, Object> sentenceInfo : sentenceInfoList) {

			code.add(CodeCommend.buildLine().append("\n"));
			
    		Map<String, Object> prepareToCodeInfo = prepareToCode(sentenceInfo, beforeSentenceLength);
    		
    		beforeSentenceLength += (int) prepareToCodeInfo.get("beforeSentenceLength");
    		
    		List<KeyWord> keyWordList = (List<KeyWord>) prepareToCodeInfo.get("keyWordList");
    		
    		for(KeyWord keyWord : keyWordList) {
				switch (keyWord.text) {
				case "변수":
					CodeCommend.codeVariable(code, keyWord, variableMap.get(keyWord.info));
					break;
				case "출력":
					CodeCommend.codePrint(code, keyWord);
					break;
				case "반복":
					CodeCommend.codeFor(code, keyWord);
					break;
				}
    		}
    		while(CodeCommend.tapLev>2) {
    			CodeCommend.tapLev--;
    			StringBuilder line = CodeCommend.buildLine();
    			line.append("}\n");
    			code.add(line);
    		}
    	}
        
        
        code.add(new StringBuilder().append("\t}").append("\n"));
        code.add(new StringBuilder().append("}"));
        
        StringBuilder finalCode = new StringBuilder();
        for(int i=0; i<code.size(); i++) {
        	finalCode.append(code.get(i));
        }
        
        System.out.println(finalCode);
    }
    
    private static Map<String, Object> prepareToCode(Map<String, Object> sentenceInfo, int beforeSentenceLength) {

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
			case "변수":
				String variableName = morpList.get(i+1).text;
				if(!variableMap.containsKey(variableName)) {
					String variableType = morpList.get(i-2).text;
					Morpheme startVariableValue = morpList.get(i+3);
					Morpheme endVariableValue = null;
					for(int j=i+4; j<morpList.size(); j++) {
						endVariableValue = morpList.get(j);
						if(endVariableValue.type.equals("JKO")&&morpList.get(j+1).text.equals("담")||morpList.get(j+1).text.equals("넣"))
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
					
					variableMap.put(variableName, new Variable(variableType, variableName, variableValue.trim()));

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
				int j = i-2;
				Morpheme repeatMorp = morpList.get(j);
				String repeat = "";
				
				do {
					repeat = repeatMorp.text + repeat;
					repeatMorp = morpList.get(--j);					
				}while(repeatMorp.type.equals("NR")||repeatMorp.type.equals("MM"));
				
				keyWord = new KeyWord(morpheme.text, repeat, keyWordSetMap.get(morpheme.text));
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
    
    private static void showProcessingSentence(List<Map<String, Object>> sentenceInfoList) {
    	
    	for(Map<String, Object> sentenceInfo : sentenceInfoList) {
        	
        	System.out.println("morp ->");
        	List<Morpheme> morpList = (List<Morpheme>) sentenceInfo.get("morpList");
        	for(Morpheme morp : morpList) {
        		System.out.print("(" + morp.id + "/" + morp.position + ")"+ morp.text + "/" + morp.type + " ");
        	}
        	
        	System.out.println();
        	System.out.println();
        	
        	System.out.println("dependency ->");
        	List<Dependency> dependencyList = (List<Dependency>) sentenceInfo.get("dependencyList");
        	for(Dependency dependency : dependencyList) {
        		System.out.print("(" + dependency.id + ")" + dependency.text + "/" + dependency.label
        				+ "(" + dependency.head + ")[");
        		
        		List<Double> mod = dependency.mod;
        		for(double modChild : mod) {
        			System.out.print(modChild + " ");
        		}
        		System.out.print("] ");
        	}
        	
        	System.out.println();
        	System.out.println();
        }
    }
    
}
