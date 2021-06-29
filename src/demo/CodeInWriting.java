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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class CodeInWriting {
	static public class Morpheme {
        private final String text;
        private final String type;
        private int count;
        public Morpheme (String text, String type, int count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
        @Override
        public String toString() {
        	return text;
        }
        public String getText() {
        	return text;
        }
        public String getType() {
        	return type;
        }
        public int getCount() {
        	return count;
        }
        public void setCount(int count) {
        	this.count = count;
        }
    }

	private static Set<String> keyWord = new HashSet<>();
	
    public static void main(String[] args) {

    	settingCodeInWriting();
    	
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";         
        String accessKey = "";
        String analysisCode = "morp";
        
        StringBuilder text = new StringBuilder();
//        text.append("안녕 세계를 출력하세요");
        text.append("사과를 출력하는 것을 백삼십이 번 반복 하시고 바나나라고 출력하는 것을 세 번 반복하세요");
//        text.append("안녕 안녕 안녕 세상아 세상아 안녕");
        
        Gson gson = new Gson();
        
        System.out.println(text);
        System.out.println();
        
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
 
        argument.put("analysis_code", analysisCode);
        argument.put("text", text.toString());
 
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
            
            Map<String, Integer> wordsMap = new HashMap<>();
            List<Morpheme> words = new ArrayList<>();
            
            for( Map<String, Object> sentence : sentences ) {
                // 형태소 분석기 결과 수집 및 정렬
                List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
                for( Map<String, Object> morphemeInfo : morphologicalAnalysisResult ) {
                    String lemma = (String) morphemeInfo.get("lemma");
                    String type = (String) morphemeInfo.get("type");
                    
                    Integer count = wordsMap.get(lemma);
                    
                    if(count==null) {
                    	wordsMap.put(lemma, 1);
                    	count = 1;
                    }
                    
                    words.add(new Morpheme(lemma, type, count++));
                    
                    wordsMap.put(lemma, count);
                }
            }
            
            showMorphemes(words);
            
            buildCode(words, text);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void codeFor(List<StringBuilder> code, List<Morpheme> words, int idx, int tapLev) {
    	
		int repeat = 0;
		
    	for(int i=idx-1; i>=0; i--) {
    		Morpheme morpheme = words.get(i);
    		if(morpheme.getType().equals("NR")||morpheme.getType().equals("MM")) {
    			repeat = convertToNumber(morpheme.toString());
    			break;
    		}
    	}
    	
    	int size = code.size();
    	
    	code.get(size-1).insert(0, "\t");
    	
    	code.add(size-1, buildLine(tapLev).append("for(int i=0; i<").append(repeat).append("; i++) {\n"));
    	code.add(buildLine(tapLev).append("}\n"));
    	
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
    
    private static void codePrint(List<StringBuilder> code, List<Morpheme> words, int idx, StringBuilder text, int tapLev) {
    	
    	StringBuilder print = buildLine(tapLev);
    	
    	int startWord=0, endWord=idx-1;

    	for(int i=endWord; i>=0; i--) {
    		Morpheme morpheme = words.get(i);
    		if(morpheme.getType().equals("SF")||(morpheme.getType().equals("EC")&&morpheme.getText().equals("고"))) {
    			startWord = i+1;
    			break;
    		}
    	}
    	
    	int start = text.indexOf(words.get(startWord).toString());
    	int end = text.indexOf(words.get(endWord).toString());

    	print.append("System.out.println(\"").append(text.substring(start, end)).append("\");\n");
    	
    	code.add(print);
    }
    
    private static void buildCode(List<Morpheme> words, StringBuilder text) {
    	List<StringBuilder> code = new LinkedList<StringBuilder>();
        
    	code.add(new StringBuilder().append("public class Main {").append("\n"));
    	code.add(new StringBuilder().append("\tpublic static void main(String[] args) {").append("\n"));
    	
        int tapLev = 2;
        
        for(int i=0; i<words.size(); i++) {
        	if(keyWord.contains(words.get(i).toString())) {
        		switch (words.get(i).text) {
        		case "출력":
        			codePrint(code, words, i, text, tapLev);
        			break;
        		case "반복":
        			codeFor(code, words, i, tapLev);
        			break;
        		}
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
    
    private static void showMorphemes(List<Morpheme> words) {
    	
        StringBuilder morphemes = new StringBuilder();
        
    	for(int i=0; i<words.size(); i++) {
        	morphemes.append(words.get(i).getText()).append("/").append(words.get(i).getType()).append("(")
        		.append(words.get(i).getCount()).append(") ");
        }
    	

        morphemes.append("\n");

        System.out.println(morphemes);
    }
    
    private static StringBuilder buildLine(int tapLev) {
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
