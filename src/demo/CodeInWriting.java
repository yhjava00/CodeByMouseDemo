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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class CodeInWriting {
	static public class Morpheme {
        final String text;
        final String type;
        Integer count;
        public Morpheme (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
        @Override
        public String toString() {
        	return text;
        }
    }
    static public class NameEntity {
        final String text;
        final String type;
        Integer count;
        public NameEntity (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
        @Override
        public String toString() {
        	return text;
        }
    }
 
    public static void main(String[] args) {

        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";         
        String accessKey = "";   // 발급받은 API Key
        String analysisCode = "ner";        // 언어 분석 코드
        
        
        StringBuilder text = new StringBuilder();
        text.append("문장 헬로 월드를 출력하시오");           // 분석할 텍스트 데이터
        
        Gson gson = new Gson();
         
        
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
 
            // 분석 결과 활용
            returnObject = (Map<String, Object>) responeBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");
 
            List<String> keyWords = new ArrayList<>();
            
            Set<String> keyWord = new HashSet<>();
            
            keyWord.add("출력");
            
            for( Map<String, Object> sentence : sentences ) {
                // 형태소 분석기 결과 수집 및 정렬
                List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
                for( Map<String, Object> morphemeInfo : morphologicalAnalysisResult ) {
                    String lemma = (String) morphemeInfo.get("lemma");
                    if(keyWord.contains(lemma))
                    	keyWords.add(lemma);
                }
            }
            
            StringBuilder code = new StringBuilder();
            
            code.append("public class Main {").append("\n");
            code.append("\tpublic static void main(String[] args) {").append("\n");
            
            int tapLev = 2;
            
            for(int i=0; i<keyWords.size(); i++) {
            	switch (keyWords.get(i)) {
				case "출력":
					String allWord = text.toString();
					String printWord = allWord.substring(allWord.indexOf("문장 ")+3);
					printWord = printWord.substring(0, printWord.indexOf("를"));
					for(int j=0; j<tapLev; j++) {
						code.append("\t");
					}
					code.append("System.out.println(\"").append(printWord).append("\");\n");
					break;
				}
            }
            
            code.append("\t}").append("\n");
            code.append("}");
            
            System.out.println(code.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
