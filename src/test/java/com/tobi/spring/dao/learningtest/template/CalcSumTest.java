package com.tobi.spring.dao.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	Calculator calculator;
	String numFilePath;

	@Before
	public void setup() {
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("numbers.txt").getPath();
	}

	@Test
	public void sumOfNumbers() throws IOException {
		int sum = calculator.calcSum(numFilePath);
		assertThat(sum, is(15));
	}
	
	@Test
	public void multiplyOfNumbers() throws IOException {
		int sum = calculator.calcMul(numFilePath);
		assertThat(sum, is(0));
	}


}
