package com.tobi.spring.dao.learningtest.template;

import com.tobi.spring.dao.Level;
import com.tobi.spring.dao.User;
import com.tobi.spring.dao.UserDao;
import com.tobi.spring.service.UserService;
import com.tobi.spring.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.tobi.spring.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static com.tobi.spring.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

/**
 * Created by 김영환 on 2016-10-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
    List<User> users;

    @Autowired UserService userService;
    @Autowired UserDao dao;
    //@Autowired DataSource dataSource;
    @Autowired PlatformTransactionManager transactionManager;

    static class TestUserService extends UserServiceImpl {
        private String id;
        private TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    @Before
    public void setup() {
        users = Arrays.asList(
                new User("bj", "aaa", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("jj", "bbb", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("cj", "ccc", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("kj", "ddd", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("hj", "eee", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        dao.deleteAll();
        for (User user: users) {
            dao.add(user);
        }
        userService.upgradeLevels();
        checkLevels(users.get(0), false);
        checkLevels(users.get(1), true);
        checkLevels(users.get(2), false);
        checkLevels(users.get(3), true);
        checkLevels(users.get(4), false);
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

    @Test
    public void upgradeAllOrNothing() throws Exception{
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.dao);
        testUserService.setTransactionManager(this.transactionManager);
        //testUserService.setDataSource(this.dataSource);

        dao.deleteAll();
        for(User user: users) {
            dao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }  catch (TestUserServiceException te) {

        }
        checkLevels(users.get(1), false);
    }

    private void checkLevels(User user, boolean upgraded) {
        User userUpdate = dao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
}
