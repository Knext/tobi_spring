package com.tobi.spring.dao.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SimpleConnectionMaker implements ISimpleConnectionMaker {

	/* (non-Javadoc)
	 * @see com.tobi.spring.dao.dbconn.ISimpleConnectionMaker#makeNewConnection()
	 */
	@Override
	public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/springboard", "root", "123456");
		return c;
	}

}
