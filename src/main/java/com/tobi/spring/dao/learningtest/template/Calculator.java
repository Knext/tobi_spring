package com.tobi.spring.dao.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer fileReaderTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			int ret = callback.doSomethingWithReader(br);
			return ret;
		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					
				}
			}
		}
		
	}

	public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			// TODO Auto-generated method stub
			T res = initVal;
			String line = null;
			while((line = br.readLine()) != null) {
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					
				}
			}
		}
	}

	public Integer calcSum(String filePath) throws IOException {
		LineCallback<Integer> callback = new LineCallback<Integer>() {
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				// TODO Auto-generated method stub
				return value += Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filePath, callback, 0);
	}
	
	public Integer calcMul(String filePath) throws IOException {
		BufferedReaderCallback callback = new BufferedReaderCallback() {
			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
				// TODO Auto-generated method stub
				Integer sum = 0;
				String line = null;
				while((line = br.readLine()) != null) {
					sum *= Integer.valueOf(line);
				}
				return sum;
			}
		};
		return fileReaderTemplate(filePath, callback);
	}
	
}
