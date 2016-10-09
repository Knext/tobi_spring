package com.tobi.spring.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tobi.spring.dao.dbconn.CountingConnectionMaker;
import com.tobi.spring.dao.dbconn.ISimpleConnectionMaker;
import com.tobi.spring.dao.dbconn.SimpleConnectionMaker;

@Configuration
public class CountingDaoFactory {
	@Bean
	public UserDao userDao() {
		UserDao dao = new UserDao();
		dao.setConnectionMaker(connectionMaker());
		return dao;
	}
	
	@Bean
	public ISimpleConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ISimpleConnectionMaker realConnectionMaker() {
		return new SimpleConnectionMaker();
	}
	
}
