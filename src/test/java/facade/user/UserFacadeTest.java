package facade.user;


import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import domain.user.User;
import domain.credential.Credential;
import domain.profile.Profile;
import service.credential.CredentialService;
import service.user.UserService;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * 
 * Test class for facade user
 * 
 * @author romain
 *
 */

@RunWith(Arquillian.class)
public class UserFacadeTest{
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,User.class.getPackage())
				.addPackages(true,Profile.class.getPackage())
				.addPackage(Credential.class.getPackage())
				.addPackage(UserService.class.getPackage())
				.addPackage(CredentialService.class.getPackage())
				.addPackages(true,UserFacade.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}
	
	
	@Inject
	private UserService userService;
	@Inject
	private UserFacade userFacade;

	/**
	 * 
	 * Check the validity of getUserById.
	 * 
	 * We take the user 1 with a query. Then put it to string and compare to our expectation.
	 * Then try a query, with an expectation of 200 answer'scode. And we check the 404 code too.
	 * 
	 */
    @Test
    @InSequence(1)
    public void testGetUserById(){
    	
    	String r1 = userFacade.showUserById(new Long(1)).getEntity().toString();
    	String r2 = userService.getById(new Long(1)).toString();
    	int code = userFacade.showUserById(new Long(1)).getStatus();
    	// the service must find the user, so the answer is 200
    	Assert.assertEquals("userFacade.getUser(1) == userService.getUser(1)", r1,r2);
    	Assert.assertEquals("user.Facade.getUserById(new Long(1)).getStatus == 200", code,200);
    	
    	// the service dosen't find the user, so the answer is 404
    	
    	Assert.assertEquals("userFacade.getUser(10) == 404 ", userFacade.showUserById(new Long(10)).getStatus(), 404);
    }
    
    
    /**
     * 
     * Check the validity of getUserByName.
     * 
     * We get an user, that we transform to string and compare that to our expectation.
     * Then we do a query, with an http answer that must be 200. And the 404 error to.
     * 
     */
    @Test
    @InSequence(2)
    public void testGetUserByName(){
    	
    	String r1 = userFacade.showUserByName("Aurelien Coet").getEntity().toString();
    	String r2 = userService.getByName("Aurelien Coet").toString();
    	
    	int rightCode = userFacade.showUserByName("Aurelien Coet").getStatus();
    	int wrongCode = userFacade.showUserByName(".").getStatus();
    	// the service must find the user, so the answer is 200
    	// the two string must be the same
    	Assert.assertEquals("userFacade.getUser(1) == userService.getUser(1)", r1,r2);
    	Assert.assertEquals("user.Facade.getUserByName(Aurelien coet).getStatus == 200", rightCode,200);
    	Assert.assertEquals("userFacade.getUserByName(.).getstatus == 404",wrongCode , 404);
    }
    
    /**
     * Check the validity of method searchUserByEmail.
     * 
     * We check if the returned user, according to the unique email,
     * is right. We check the error case too.
     * 
     */
    @Test
    @InSequence(4)
    public void testSearchUserByEmail(){
    	
    	User u1 = (User) userFacade.searchUserByEmail("mencat1907@gmail.com").getEntity();
    	User u2 = userService.searchByEmail("mencat1907@gmail.com");
    	int code = userFacade.searchUserByEmail("mencot1907@gmail.com").getStatus();
    	
    	Assert.assertEquals("u1 must be the same as u2", u1,u2);
    	Assert.assertEquals("code == 404", code,404);
    	
    }
    

    /**
     * 
     * Check the validity of method searchUserByName
     * 
     * We check if the list's size is the same as expected, and if
     * the elements in this list are right. We check the error case too.
     * 
     */
    @SuppressWarnings("unchecked")
	@Test
    @InSequence(5)
    public void testSearchUserByName(){
		List<User> l1 = (List<User>) userFacade.searchUserByName("Romain").getEntity();
    	List<User> l2 = userService.searchByName("Romain");
    	List<User> l3 = (List<User>) userFacade.searchUserByName("akfoie").getEntity();
    	
    	Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
    	Assert.assertEquals("l1.size() == 2", l1.size(), 2);
    	Assert.assertEquals("l1.get(0).getId() == 1", l1.get(0).getUserId(), new Long(1));
    	Assert.assertEquals("l1.get(1).getId = 4", l1.get(1).getUserId(), new Long(4));
    	Assert.assertEquals("l3.size() == 0", l3.size(), 0);
    }
    
    /**
     * 
     * Check the validity of adduser
     * 
     * We create an user. We add it to the data base. We do a query on this user
     * and we expect a 200 code.
     * Then we delete this user.
     * 
     * @throws URISyntaxException
     * @throws NoSuchAlgorithmException 
     */
    @Test
    @InSequence(6)
    public void testAddUser() throws URISyntaxException, NoSuchAlgorithmException{
    	Date date = new Date();
    	Profile p1 = new Profile("John Doe","jd@gmail.com", date, "password");
    	
		userFacade.addUser(p1);
    	
    	Assert.assertEquals("userFacade.getUserEmail(jd@gmail.com).status == 200", userFacade.searchUserByEmail("jd@gmail.com").getStatus(),200);
    
    	User u1 = userService.searchByEmail("jd@gmail.com");
    	
    	// we try to add a profile that already exists
    	// response code must be 409
    	int code = this.userFacade.addUser(p1).getStatus();
    	Assert.assertEquals("code == 409", code, 409);
    	
    	userService.deleteUser(u1);
    }
    
    /**
     * Check the validity of updateUser
     * @throws URISyntaxException 
     * @throws NoSuchAlgorithmException 
     */
    @Test
    @InSequence(6)
    public void testUpdateUser() throws NoSuchAlgorithmException, URISyntaxException {
    	// we take a user
		User backup = (User) this.userFacade.showUserById(new Long(1)).getEntity();
		
		// we change some data
		User u1 = (User) this.userFacade.showUserById(new Long(1)).getEntity();
		u1.setEmail("mrl@bluewin.ch");
		u1.setUserName("Jean Marc");
		
		int code = this.userFacade.updateUser(u1).getStatus();
		
		// check if it works
		Assert.assertEquals("u1 == userFacade.get(1)", u1, this.userFacade.showUserById(new Long(1)).getEntity());
		Assert.assertEquals("code == 201", code, 201);
		
		// retablish the data base
		this.userFacade.updateUser(backup);
    }
}