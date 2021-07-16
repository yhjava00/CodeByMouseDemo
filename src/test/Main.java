package test;

import java.util.Map;
import java.util.Scanner;

import common.ConnectAI;

public class Main {

 	public static void main(String[] args) {
 		Scanner 키위 = new Scanner(System.in);
 		int 위 = 키위.nextInt();
 		int 아래 = 키위.nextInt();
 		int 길이 = 키위.nextInt();
 		int 하루 = ( 길이 - 아래 ) / ( 위 - 아래 );
 		if ( ( 길이 - 아래 ) % ( 위 - 아래 ) != 0) {
 			하루 = 하루 + 1;
 		}
 		System.out.println(하루);
 	}
	
}
