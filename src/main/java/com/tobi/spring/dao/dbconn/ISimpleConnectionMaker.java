package com.tobi.spring.dao.dbconn;

import java.sql.Connection;
import java.sql.SQLException;

public interface ISimpleConnectionMaker {

	Connection makeNewConnection() throws ClassNotFoundException, SQLException;

}