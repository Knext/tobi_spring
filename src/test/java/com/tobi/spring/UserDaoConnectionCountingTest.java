package com.tobi.spring;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.tobi.spring.dao.CountingDaoFactory;
import com.tobi.spring.dao.DaoFactory;
import com.tobi.spring.dao.User;
import com.tobi.spring.dao.UserDao;
import com.tobi.spring.dao.dbconn.CountingConnectionMaker;

public class UserDaoConnectionCountingTest {
	public static void main(String [] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//		UserDao dao = new DaoFactory().userDao();
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter:" + ccm.getCounter());
	}
}
