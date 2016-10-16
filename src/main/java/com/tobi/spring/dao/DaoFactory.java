package com.tobi.spring.dao;

import javax.sql.DataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

//@Configuration
public class DaoFactory {
//	@Bean
	public UserDao userDao() {
		UserDaoJdbc dao = new UserDaoJdbc();
		dao.setDataSource(dataSource());
		return dao;
	}

//	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		return dataSource;
	}

	/*@Bean
	public ISimpleConnectionMaker connectionMaker() {
		ISimpleConnectionMaker conn = new SimpleConnectionMaker();
		return conn;
	}*/
	
}
