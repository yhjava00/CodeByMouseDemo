package test;

import java.util.Map;

import common.ConnectAI;

public class Main {

	public static void main(String[] args) {

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
		
		System.out.println(systemText);
	}
	
}
