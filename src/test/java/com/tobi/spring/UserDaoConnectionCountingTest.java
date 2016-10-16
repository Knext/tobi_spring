package com.tobi.spring;

import java.sql.SQLException;

import com.tobi.spring.dao.UserDao;
import com.tobi.spring.dao.UserDaoJdbc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.tobi.spring.dao.dbconn.CountingConnectionMaker;

public class UserDaoConnectionCountingTest {
	public static void main(String [] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//		UserDaoJdbc dao = new DaoFactory().userDao();
		UserDao dao = context.getBean("userDao", UserDaoJdbc.class);
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter:" + ccm.getCounter());
	}
}
