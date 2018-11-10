package com.weather;

import java.util.Scanner;

public class weather {
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the count of days you want to see the weather: ");
		int numberOfDays = sc.nextInt();
		sc.close();
		util.sample(numberOfDays);
	}
}
