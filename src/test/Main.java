package test;

import java.util.Map;
import java.util.Scanner;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import common.ConnectAI;

public class Main {
	public static void main(String[] args) {
		Scanner 키위 = new Scanner(System.in);
		int 수 = 키위.nextInt();
		if ( 수 == 4 ||  수 == 7 ) {
			System.out.println("-1");
		} else if ( 수 % 5 == 0 ) {
			System.out.println(수 / 5);
		} else if ( 수 % 5 == 1 ||  수 % 5 == 3 ) {
			System.out.println(( 수 / 5 ) + 1);
		} else if ( 수 % 5 == 2 ||  수 % 5 == 4 ) {
			System.out.println(( 수 / 5 ) + 2);
		}
	}
}