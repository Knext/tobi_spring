package com.tobi.spring.service;

import com.tobi.spring.dao.User;
import com.tobi.spring.dao.UserDao;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by 김영환 on 2016-10-17.
 */
public interface UserService {
    void upgradeLevels();
    void add(User userWLevel);
    void setUserDao(UserDao userDao);
    void setTransactionManager(PlatformTransactionManager transactionManager);
}
