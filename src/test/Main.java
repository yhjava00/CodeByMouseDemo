package test;

import java.util.Map;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import common.ConnectAI;

public class Main {
	public static void main(String[] args) {

		ScriptEngineManager s = new ScriptEngineManager();
		ScriptEngine engine = s.getEngineByName("JavaScript");
		String code = "for(var i=0; i<10; i++) {}";
		boolean res = false;
		try {
			System.out.println((double) engine.eval(code));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(res);
	}
}