package com.gcit.utility;

import java.util.Scanner;

public class ScannerUtils {
	Scanner sc;

	public ScannerUtils(Scanner sc) {
		this.sc = sc;
	}

	public String getNextLine() {
		return sc.nextLine();
	}
	
	/**
	 * get integer input
	 * 
	 * @param maxRange
	 * @return
	 */
	public int getIntegerInput(Integer maxRange) {
		boolean isCorrect = false;
		int inputNumber = 0;
		while (!isCorrect) {
			try {
				inputNumber = Integer.parseInt(sc.next());
				if(maxRange != null) {
					if (inputNumber <= maxRange && inputNumber > 0) {
						isCorrect = true;
					} else {
						System.out.println("Chose only numbers between 1 and " + maxRange);
					}
				}
				
				isCorrect = true;

			} catch (NumberFormatException e) {
				System.out.println("I accept only integer input, try again!");
			}
		}

		return inputNumber;
	}
	
	/**
	 * get yes or no from user
	 * 
	 * @return
	 */
	public String getYesOrNo() {
		String answer;
		while (true) {
			answer = sc.next().trim().toLowerCase();
			if (answer.equals("y") || answer.equals("n")) {
				break;
			}  else {
				System.out.println("Sorry, I didn't catch that. Please answer with y/n");
			}
		}
		return answer;
	}
}
