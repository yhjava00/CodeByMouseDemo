package common;

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
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import demo02.CodeCommend02;
import vo.Dependency;
import vo.Morpheme;

public class ConnectAI {
	
	static String accessKey = "";
	
	public static String openDialog(String domain) {
		String openApiURL = "http://aiopen.etri.re.kr:8000/Dialog";
        String access_method = "internal_data";
        String method = "open_dialog";
        Gson gson = new Gson();
 
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
 
        ////////////////////////// OPEN DIALOG //////////////////////////
 
        argument.put("name", domain);
        argument.put("access_method", access_method);
        argument.put("method", method);
 
        request.put("access_key", accessKey);
        request.put("argument", argument);
 
 
        URL url;
        Integer responseCode = null;
        String responBodyJson = null;
        Map<String, Object> responeBody = null;
        try {
                url = new URL(openApiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
 
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(gson.toJson(request).getBytes("UTF-8"));
                wr.flush();
                wr.close();
 
                responseCode = con.getResponseCode();
                InputStream is = con.getInputStream();
                byte[] buffer = new byte[is.available()];
                int byteRead = is.read(buffer);
                responBodyJson = new String(buffer);
                
                responeBody = gson.fromJson(responBodyJson, Map.class);
        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        Map<String, Object> return_object = (Map<String, Object>) responeBody.get("return_object");
        
        String uuid = (String) return_object.get("uuid");
        return uuid;
	}
	
	public static Map<String, Object> dialog(String uuid) {
		String openApiURL = "http://aiopen.etri.re.kr:8000/Dialog";
        String method = "dialog";
        
        Scanner sc = new Scanner(System.in);
        
        String text = CodeCommend02.commendToken.nextToken();
        
        Gson gson = new Gson();
 
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
 
        ////////////////////////// OPEN DIALOG //////////////////////////
 
        argument.put("uuid", uuid);
        argument.put("method", method);
        argument.put("text", text);
 
        request.put("access_key", accessKey);
        request.put("argument", argument);
 
 
        URL url;
        Integer responseCode = null;
        String responBodyJson = null;
        Map<String, Object> responeBody = null;
        try {
                url = new URL(openApiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
 
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(gson.toJson(request).getBytes("UTF-8"));
                wr.flush();
                wr.close();
 
                responseCode = con.getResponseCode();
                InputStream is = con.getInputStream();
                byte[] buffer = new byte[is.available()];
                int byteRead = is.read(buffer);
                responBodyJson = new String(buffer);
                
                responeBody = gson.fromJson(responBodyJson, Map.class);
        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        Map<String, Object> return_object = (Map<String, Object>) responeBody.get("return_object");
        Map<String, Object> result = (Map<String, Object>) return_object.get("result");
        
        return result;
	}
	
	public static List<Morpheme> morphemeSeparation(String requestText) {
		String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";         
        String analysisCode = "srl";
        
        Gson gson = new Gson();
        
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
 
        argument.put("analysis_code", analysisCode);
        argument.put("text", requestText);
 
        request.put("access_key", accessKey);
        request.put("argument", argument);
 
        URL url;
        Integer responseCode = null;
        String responBodyJson = null;
        Map<String, Object> responeBody = null;
        
        List<Map<String, Object>> sentenceInfoList = null;
        
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
                return null;
            }
            
            responeBody = gson.fromJson(responBodyJson, Map.class);
            Integer result = ((Double) responeBody.get("result")).intValue();
            Map<String, Object> returnObject;
            List<Map> sentences;
 
            // 분석 요청 오류 시 처리
            if ( result != 0 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responeBody.get("result"));
                return null;
            }
            
            // 분석 결과 활용
            returnObject = (Map<String, Object>) responeBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");
            
            sentenceInfoList = new ArrayList<>();
            
            //demo01
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

//            System.out.println(requestText);
//            System.out.println();
            
//            System.out.println(responBodyJson);
//            System.out.println();
            
//            showProcessingSentence(sentenceInfoList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return (List<Morpheme>) sentenceInfoList.get(0).get("morpList");
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
        	
//        	System.out.println("dependency ->");
//        	List<Dependency> dependencyList = (List<Dependency>) sentenceInfo.get("dependencyList");
//        	for(Dependency dependency : dependencyList) {
//        		System.out.print("(" + dependency.id + ")" + dependency.text + "/" + dependency.label
//        				+ "(" + dependency.head + ")[");
//        		
//        		List<Double> mod = dependency.mod;
//        		for(double modChild : mod) {
//        			System.out.print(modChild + " ");
//        		}
//        		System.out.print("] ");
//        	}
//        	
//        	System.out.println();
//        	System.out.println();
        }
    }
}
