package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class CodeInWritingCopy {
	static public class Morpheme {
		final double id;
		final String text;
        final String type;
        public Morpheme (double id, String text, String type) {
            this.id = id;
        	this.text = text;
            this.type = type;
        }
        @Override
        public String toString() {
        	return text;
        }
    }
	
	static public class Dependency {
		final double id;
		final String text;
		final double head;
		final String label;
		final List<Double> mod;
		public Dependency(double id, String text, double head, String label, List<Double> mod) {
			this.id = id;
			this.text = text;
			this.head = head;
			this.label = label;
			this.mod = mod;
		}
		@Override
        public String toString() {
        	return text;
        }
	}

	private static Set<String> keyWord = new HashSet<>();
	
	private static int tapLev = 2;
	
    public static void main(String[] args) {

    	settingCodeInWriting();
    	
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";         
        String accessKey = "";
        String analysisCode = "srl";
        
        StringBuilder requestText = new StringBuilder();
//        text.append("안녕 세계를 출력하세요");
        requestText.append("안녕하세요 세계를 출력하세요. 오뎅을 출력 하는 것을 스물두 번 반복하세요. 바나나를 출력하세요");
//        text.append("안녕 안녕 안녕 세상아 세상아 안녕");
        
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
                System.out.println("[error] " + responBodyJson);
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
                    
                    morpList.add(new Morpheme(id, lemma, type));
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
    
    private static void codeFor(List<StringBuilder> code, List<Morpheme> words, int idx) {
    	
		int repeat = 0;
		
    	for(int i=idx-1; i>=0; i--) {
    		Morpheme morpheme = words.get(i);
    		if(morpheme.type.equals("NR")||morpheme.type.equals("MM")) {
    			
    			String repeatStr = morpheme.toString();
    			
    			if(i>0&&words.get(i-1).type.equals("NR")) {
    				repeatStr = words.get(i-1).text + repeatStr;
    			}
    			
    			repeat = convertToNumber(repeatStr);
    			break;
    		}
    	}
    	
    	int size = code.size();
    	
    	code.add(size, buildLine().append("for(int i=0; i<").append(repeat).append("; i++) {\n"));
    	tapLev++;
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
    
    private static void codePrint(List<StringBuilder> code, List<Morpheme> words, int idx, StringBuilder text) {
    	
    	StringBuilder print = buildLine();
    	
    	int start=0, end=0, endWordIdx=0;

    	for(int i=0; i<words.size(); i++) {
    		Morpheme morpheme = words.get(i);
    		if(morpheme.type.equals("JKO")) {
    			endWordIdx = i;
    			break;
    		}
    	}
    	
    	end = text.indexOf(words.get(endWordIdx).toString());

    	print.append("System.out.println(\"").append(text.substring(start, end).trim()).append("\");\n");
    	
    	code.add(print);
    }
    
    private static void buildCode(List<Map<String, Object>> sentenceInfoList) {
    	List<StringBuilder> code = new LinkedList<StringBuilder>();
        
    	code.add(new StringBuilder().append("public class Main {").append("\n"));
    	code.add(new StringBuilder().append("\tpublic static void main(String[] args) {").append("\n"));
    	
    	for(Map<String, Object> sentenceInfo : sentenceInfoList) {
    		List<Morpheme> morpList = (List<Morpheme>) sentenceInfo.get("morpList");
    		String text = (String) sentenceInfo.get("text");
    		
    		for(int i=0; i<morpList.size(); i++) {
    			if(keyWord.contains(morpList.get(i).toString())) {
    				switch (morpList.get(i).text) {
    				case "출력":
    					codePrint(code, morpList, i, new StringBuilder().append(text));
    					break;
    				case "반복":
    					codeFor(code, morpList, i);
    					break;
    				}
    			}
    		}
    		while(tapLev>2) {
    			tapLev--;
    			StringBuilder line = buildLine();
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
    
    private static void prepareToCode(Map<String, Object> sentenceInfo) {
    	
    }
    
    private static void showProcessingSentence(List<Map<String, Object>> sentenceInfoList) {
    	
    	for(Map<String, Object> sentenceInfo : sentenceInfoList) {
        	
        	System.out.println("morp ->");
        	List<Morpheme> morpList = (List<Morpheme>) sentenceInfo.get("morpList");
        	for(Morpheme morp : morpList) {
        		System.out.print("(" + morp.id + ")"+ morp.text + "/" + morp.type + " ");
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
    
    private static StringBuilder buildLine() {
    	StringBuilder line = new StringBuilder();
    	for(int i=0; i<tapLev; i++) {
			line.append("\t");
		}
    	return line;
    }
    
    private static void setKeyWords() {

        keyWord.add("출력");
        keyWord.add("반복");
        
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
    
    private static void settingCodeInWriting() {
    	setKeyWords();
    	setConvertToNumberMap();
    }
    
}
