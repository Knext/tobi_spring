package com.tobi.spring.dao.learningtest.template;

import com.tobi.spring.dao.Level;
import com.tobi.spring.dao.User;
import com.tobi.spring.dao.UserDao;
import com.tobi.spring.service.UserService;
import com.tobi.spring.service.UserServiceImpl;
import com.tobi.spring.service.UserServiceTx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
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
    @Autowired UserServiceImpl userServiceImpl;
    @Autowired UserDao dao;
    //@Autowired DataSource dataSource;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired MailSender mailSender;

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<User>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return this.updated;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }
    }

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
                new User("bj", "aaa", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "aaa@xxx.com"),
                new User("jj", "bbb", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "bbb@xxx.com"),
                new User("cj", "ccc", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1,"ccc@xxx.com"),
                new User("kj", "ddd", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "ddd@xxx.com"),
                new User("hj", "eee", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "eee@xxx.com")
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevels(updated.get(0), "jj", Level.SILVER);
        checkUserAndLevels(updated.get(1), "kj", Level.GOLD);

        /*
        List<String> request = mockMailSender.getRequests();
        */
        /*
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
        */
    }

    private void checkUserAndLevels(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
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
    public void upgradeAllOrNothing() throws Exception {

        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.dao);
        testUserService.setMailSender(mailSender);
        //testUserService.setDataSource(this.dataSource);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {UserService.class},
                txHandler
        );
        /*
        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(transactionManager);
        userServiceTx.setUserService(testUserService);
        */

        dao.deleteAll();
        for (User user : users) {
            dao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            //userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException te) {

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
