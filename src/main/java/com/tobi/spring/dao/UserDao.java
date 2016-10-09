package com.tobi.spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.tobi.spring.dao.dbconn.ISimpleConnectionMaker;
import com.tobi.spring.dao.dbconn.SimpleConnectionMaker;

public class UserDao {
	private ISimpleConnectionMaker conn;
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/*public UserDao(ISimpleConnectionMaker simpleConn) {
		// TODO Auto-generated constructor stub
		conn = simpleConn;
	}*/

	public void setConnectionMaker(ISimpleConnectionMaker simpleConn) {
		conn = simpleConn;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		StatementStrategy stmt = new AddStatement(user);
		jdbcContextWithStatementStrategy(stmt);
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
//		Connection c = conn.makeNewConnection();
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement(
				"Select * from users where id = ?");

		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		
		User user = null;
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if (user == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return user;
	}
	
	public void deleteAll() throws SQLException {
		StatementStrategy stmt = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(stmt);
	}

	private void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public int getCount() throws SQLException {
		Connection c  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	
}
