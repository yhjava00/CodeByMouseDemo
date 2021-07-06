package test;

import java.util.Scanner;

public class Main {
 	public static void main(String[] args) {
 		Scanner 키위 = new Scanner(System.in);
 		int 사과 = 키위.nextInt();
 		if(사과 < 2) {
 			System.out.println("소수가 아닙니다");
 			return;
 		}
 		if(사과 == 2) {
 			System.out.println("소수입니다");
 			return;
 		}
 		for(int 귤=2; 귤<사과; 귤++) {
 			if(사과 % 귤 == 0) {
 				System.out.println("소수가 아닙니다");
				return;
 			}
 		}
 		System.out.println("소수입니다");
 	}
 }
