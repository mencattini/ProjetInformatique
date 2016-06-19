package service.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import domain.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * 
 * Test for userserviceimpl.
 * 
 * @author romain_mencattini
 *
 */

@Stateless
@RunWith(Arquillian.class) 
public class UserServiceImplTest {
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,User.class.getPackage())
				.addPackage(UserService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}
	
	@Inject
	private UserService service;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * we test if the methode return the rigth user
	 * @throws ParseException
	 */
	@Test
	@InSequence(1)
	public void testGetUserById() throws ParseException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("1993-07-19");
		
		User u1 = this.service.getById(new Long(1));
		User u2 = new User("Romain Mencattini","mencat1907@gmail.com",date,"mon_image.jpg","Bonjour, je m\'appelle Romain et je suis le meilleur sportif admin système au monde :)",false);
		u2.setUserId(new Long(1));
		Assert.assertEquals("u1.equals(u2)",u1,u2);
	}
	
	/**
	 * check if the methode getAll, works .
	 */
	@Test
	@InSequence(2)
	public void testGetAll(){
		List<User> l1 = this.service.getAll();
		List<Long> l2 = new ArrayList<Long>();
		List<Long> l3 = new ArrayList<Long>();
		l2.add(new Long(1));
		l2.add(new Long(2));
		l2.add(new Long(3));
		l2.add(new Long(4));
		for( User u : l1 ){
			l3.add(u.getUserId());
		}
		Assert.assertEquals("all user id must be equal to [1,2,3,4]",l2,l3 );
	}
	
	/**
	 * Test the update methode.
	 * @throws ParseException
	 */
	@Test
	@InSequence(3)
	public void testUpdate() throws ParseException{
		
		Long id = new Long(2);
		User u2 = this.service.getById(id);
		this.service.update(id, u2);
		User u1 = this.service.getById(id);
		
		Assert.assertEquals("u1 == u2", u1, u2);
	}
	
	/**
	 * check if we get the right number of user.
	 */
	@Test
	@InSequence(4)
	public void testGetNbUser() {
		int nb = this.service.getNbUsers();
		
		Assert.assertEquals("nb of user == 4", nb, 4);
	}
	
	
	/**
	 * test if getByName return the right user
	 */
	@Test
	@InSequence(5)
	public void testGetByName(){
		User u1 = this.service.getByName("Aurelien Coet");
		Assert.assertEquals("u1.getId() == 2 ", u1.getUserId(), new Long(2));
	}
	
	/**
	 * check the research by name
	 */
	@Test
	@InSequence(6)
	public void testSearchByName(){
		ArrayList<User> liste = (ArrayList<User>) this.service.searchByName("Romain");
		
		Assert.assertEquals("length(liste) must be 2",liste.size(),2);
		
		liste = (ArrayList<User>) this.service.searchByName("Aurelie");
		Assert.assertEquals("length(liste) must be 1", liste.size(),1);
		
		User u1 = liste.get(0);
		Assert.assertEquals("u1.getId() == 2 ", u1.getUserId(), new Long(2));
	}
	
	
	/**
	 * try to add user and validate this methode
	 * @throws ParseException
	 */
	@Test
	@InSequence(7)
	public void testAddUser() throws ParseException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("1993-07-19");
		
		User u1 = new User("Antoine De Cone","antoine.de.cone@gmail.com",date,"une image","blabl",false);
		this.service.addUser(u1);
		User u2 = this.service.getByName("Antoine De Cone");
		
		Assert.assertEquals("u1.getUserName() == u2.getUserName()", u1.getUserName(),u2.getUserName());
		
		this.service.deleteUser(u2);
		
	}

	/**
	 * test the user update.
	 * @throws ParseException
	 */
	@Test
	@InSequence(9)
	public void testUpdateUser() throws ParseException{
		// on crée et ajout un user, 
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("1993-07-19");
		
		User u1 = new User("Antoine De Cone","antoine.de.cone@gmail.com",date,"une image","blabl",false);
		this.service.addUser(u1);
		
		// on va créer un autre user avec des infos différentes
		User u2 = new User("Antoine de Saint Marie","antoine.de.saint.marie@gmail.com",date,"une image","bl",false);
		u2.setUserId(new Long(5));
		// on update
		this.service.update(new Long(5), u2);
		
		// on récupère le nouveau
		User u3 = this.service.getById(new Long(5));
		
		// on test
		Assert.assertEquals("u3 == u2", u2,u3);
		
		//clean
		this.service.deleteUser(u3);
	}
	
	/**
	 * test the getByemail
	 */
	@Test
	@InSequence(10)
	public void testgetByEmail(){
		User u1 = this.service.getById(new Long(1));
		User u2 = this.service.getByEmail("mencat1907@gmail.com");
		
		Assert.assertEquals("u1 == u2",u1 , u2);
	}
	
	/**
	 * test the search by email.
	 * not the same as above.
	 */
	@Test
	@InSequence(11)
	public void testSearchByEmail(){
		User u1 = this.service.getById(new Long(1));
		User u2 = this.service.searchByEmail("mencat1907@gmail.com");
		
		Assert.assertEquals("u1 == u2", u1,u2);
	}
}
