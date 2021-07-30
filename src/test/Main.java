package test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Main {
	public static void main(String[] args) {

//		ScriptEngineManager s = new ScriptEngineManager();
//		ScriptEngine engine = s.getEngineByName("JavaScript");
//		try {
//			System.out.println(engine.eval("1 + 1"));
//		} catch (ScriptException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		for(int i=1; i<=5; i++) {
			if(i==1) {
				System.out.println("hello");
			}else if(i==2) {
				System.out.println("nice");
			}else if(i==3) {
				System.out.println("to");
			}else if(i==4) {
				System.out.println("meet");
			}else {
				System.out.println("you");
			}
		}
		
	}

}