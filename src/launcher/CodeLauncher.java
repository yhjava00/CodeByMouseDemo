package launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import vo.Variable;

public class CodeLauncher {

	Map<String, Variable> variableMap = new HashMap<String, Variable>();

	ScriptEngineManager s = new ScriptEngineManager();
	ScriptEngine engine = s.getEngineByName("JavaScript");
	
	public void codeExecution(List<Map<String, Object>> launcherInfoList) {
	
//		for(Map<String, Object> info : launcherInfoList) {
		for(int i=0; i<launcherInfoList.size(); i++) {
			
			Map<String, Object> info = launcherInfoList.get(i);
			
			switch ((String) info.get("action")) {
			case "for":
				try {
					i = actionFor(info, launcherInfoList, i);
				} catch (Exception e) {
					System.out.println("ERR for");
				}
				break;
			case "print":
				try {
					actionPrint(info);
				} catch (Exception e) {
					System.out.println("ERR print");
				}
				break;
			case "createVar":
				try {
					actionCreateVar(info);
				} catch (Exception e) {
					System.out.println("ERR create val");
				}
				break;
			}
		}
	}
	
	private int actionFor(Map<String, Object> info, List<Map<String, Object>> launcherInfoList, int idx) throws Exception {
		
		int start = (int) info.get("start");
		int end = (int) info.get("end");
		
		List<Map<String, Object>> forList = new ArrayList<Map<String,Object>>();
		
		idx++;
		for(;idx<launcherInfoList.size(); idx++) {
			Map<String, Object> forInfo = launcherInfoList.get(idx);
			if(((String) forInfo.get("action")).equals("out for")) {
				break;
			}
			forList.add(forInfo);
		}
		
		for(int i=start; i<=end; i++) {
			codeExecution(forList);
		}
		
		return ++idx;
	}
	
	private void actionCreateVar(Map<String, Object> info) throws Exception {
		Variable var = (Variable) info.get("variable");
		
		if(var.value!=null) {
			switch (var.type) {
			case "int":
				var.value = String.valueOf((int) engine.eval(var.value));
				break;
			case "String":
				var.value = (String) engine.eval(var.value);
				break;
			}
		}
		
		variableMap.put(var.name, var);
	}
	
	private void actionPrint(Map<String, Object> info) throws Exception {
		String value = (String) info.get("value");
		
		if(info.containsKey("cal")) {
			value = String.valueOf((int) engine.eval(value));
		}
		
		if(variableMap.containsKey(value)) {
			System.out.println(variableMap.get(value).value);
		} else {
			System.out.println(value);
		}
	}
	
}
