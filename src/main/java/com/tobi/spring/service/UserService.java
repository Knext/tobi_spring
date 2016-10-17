package com.tobi.spring.service;

import com.tobi.spring.dao.User;

/**
 * Created by 김영환 on 2016-10-17.
 */
public interface UserService {
    void upgradeLevels();
    void add(User userWLevel);
}
