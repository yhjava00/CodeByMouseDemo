package test;

public class Main {
	public static void main(String[] args) {
		String str = "hi";
		changeStr(str);
		System.out.println(str);
		
	}

	public static void changeStr(String str) {
		str = "hello";
	}
}