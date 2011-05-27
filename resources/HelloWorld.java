package misha.test;

import misha.Fake;

public class Point{
	public static void main(String args[]){
		int a = 4;
		double d = 1.2;
		/*
		  Block comment
		 */

		// Cycle
		for(double di = 0; di < 10; di++){
			d+=square(di);	// Plus all squares
		}
	}
	
	private double square(double value){
		return value*value;
	}
}
int a = 4// Close comment
int a = 4// Close comment