package com.tobi.spring.dao;

import java.util.List;

/**
 * Created by 김영환 on 2016-10-16.
 */
public interface UserDao {
    void add(final User user);
    User get(String id);
    void deleteAll();
    int getCount();
    List<User> getAll();
}
