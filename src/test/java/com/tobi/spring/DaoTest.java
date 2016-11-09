package com.tobi.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.tobi.spring.dao.Level;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tobi.spring.dao.User;
import com.tobi.spring.dao.UserDao;
import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "/applicationContext.xml")
public class DaoTest {
//	@Autowired
//	ApplicationContext context;
	
	@Autowired 	UserDao dao;
	@Autowired 	DataSource dataSource;
	private User user1;
	private User user2;
	private User user3;
	public static void main(String [] args) {
		JUnitCore.main("com.tobi.spring.DaoTest");
	}
	
	@Before
	public void setup() {
//		dao = context.getBean("userDao", UserDaoJdbc.class);
		user1 = new User("a", "aaa", "aaaa", Level.BASIC, 1, 0, "aaa@xxx.com");
		user2 = new User("b", "bbb", "bbbb", Level.SILVER, 55, 10, "bbb@xxx.com");
		user3 = new User("c", "ccc", "cccc", Level.GOLD, 100, 40,"ccc@xxx.com");
	}

	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
        /*
		try {
			dao.add(user1);
			dao.add(user2);
		} catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)	ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
		}
		*/
	}
	@Test(expected = DataAccessException.class)
	public void addDuplicate() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		dao.add(user1);
		dao.add(user1);
		assertThat(dao.getCount(), is(2));

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	}

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		User userGet1 = dao.get(user1.getId());
		assertThat(userGet1.getName(), is(user1.getName()));
		assertThat(userGet1.getPassword(), is(user1.getPassword()));
		
		User userGet2 = dao.get(user2.getId());
		assertThat(userGet2.getName(), is(user2.getName()));
		assertThat(userGet2.getPassword(), is(user2.getPassword()));

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));

		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		dao.add(user3);
		assertThat(dao.getCount(), is(3));
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown id");
	}
	
	@Test
	public void getAll() throws ClassNotFoundException, SQLException {
		dao.deleteAll();

		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));

		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));

		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
	}

	@Test
	public void update() {
		dao.deleteAll();
		dao.add(user1);

		user1.setName("test_update");
		user1.setPassword("spring");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
        user1.setRecommend(999);
		user1.setEmail("ddd@xxx.com");
		dao.update(user1);

		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
	}

	private void checkSameUser(User user1, User user2) {
		// TODO Auto-generated method stub
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
		assertThat(user1.getEmail(), is(user2.getEmail()));
	}
}
