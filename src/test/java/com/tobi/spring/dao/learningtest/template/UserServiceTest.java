package com.tobi.spring.dao.learningtest.template;

import com.tobi.spring.dao.Level;
import com.tobi.spring.dao.User;
import com.tobi.spring.dao.UserDao;
import com.tobi.spring.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by 김영환 on 2016-10-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
    List<User> users;

    @Autowired UserService userService;
    @Autowired UserDao dao;

    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    @Before
    public void setup() {
        users = Arrays.asList(
                new User("bj", "aaa", "p1", Level.BASIC, 49, 0),
                new User("jj", "bbb", "p2", Level.BASIC, 50, 0),
                new User("cj", "ccc", "p3", Level.SILVER, 60, 29),
                new User("kj", "ddd", "p4", Level.SILVER, 60, 30),
                new User("hj", "eee", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {
        dao.deleteAll();
        for (User user: users) {
            dao.add(user);
        }
        userService.upgradeLevels();
        checkLevels(users.get(0), Level.BASIC);
        checkLevels(users.get(1), Level.SILVER);
        checkLevels(users.get(2), Level.SILVER);
        checkLevels(users.get(3), Level.GOLD);
        checkLevels(users.get(4), Level.GOLD);
    }

    @Test
    public void add() {
        dao.deleteAll();
        User userWOLevel = users.get(4);
        User userWLevel = users.get(0);
        userWOLevel.setLevel(null);

        userService.add(userWOLevel);
        userService.add(userWLevel);

        User userWLevelRead = dao.get(userWLevel.getId());
        User userWOLevelRead = dao.get(userWOLevel.getId());

        assertThat(userWLevelRead.getLevel(), is(userWLevel.getLevel()));
        assertThat(userWOLevelRead.getLevel(), is(Level.BASIC));
    }

    private void checkLevels(User user, Level expectedLevel) {
        User userUpdate = dao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
}
