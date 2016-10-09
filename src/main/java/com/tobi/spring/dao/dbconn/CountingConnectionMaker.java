package com.tobi.spring.dao.dbconn;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ISimpleConnectionMaker {
	int counter = 0;
	private ISimpleConnectionMaker realConnectionMaker;

	public CountingConnectionMaker(ISimpleConnectionMaker realConnectionMaker) {
		this.realConnectionMaker = realConnectionMaker;
	}

	@Override
	public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
		this.counter++;
		return realConnectionMaker.makeNewConnection();
	}
	
	public int getCounter() {
		return counter;
	}

}
