package service.credential;

import javax.ejb.Stateless;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import domain.credential.Credential;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * 
 * Test eventserviceImpl.
 * 
 * @author romain_mencattini
 *
 */

@Stateless
@RunWith(Arquillian.class) 
public class CredentialServiceImplTest {

	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,Credential.class.getPackage())
				.addPackages(true,CredentialService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}

	@Inject
	CredentialService service;

	/**
	 * method to check if an credential already exist or not
	 * @throws CredentialsAlreadyExist 
	 */
	@Test
	public void testAlreadyExists(){
		// we create credential
		Credential c1 = new Credential("mencat1907@gmail.com","passwd","user");
		// we add it
		try {
			this.service.addCredentials(c1);
		} catch (CredentialsAlreadyExist e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// true because exists
		Assert.assertTrue("service.get(mencat1907@gmail.com)",this.service.credentialsAlreadyExist("mencat1907@gmail.com"));
		// false because not exists
		Assert.assertFalse("service.get(mencat1907)",this.service.credentialsAlreadyExist("mencat1907"));

		try {
			this.service.addCredentials(c1);
		}catch(CredentialsAlreadyExist e){
			CredentialsAlreadyExist c = new CredentialsAlreadyExist();
			Assert.assertEquals("e == c", e.getClass(), c.getClass());
		}

		// we delete the credential
		this.service.removeCredentials("mencat1907@gmail.com");
		Assert.assertFalse("service.get(mencat1907@gmail.com)",this.service.credentialsAlreadyExist("mencat1907@gmail.com"));		
	}

	/**
	 * We test the update method for credential
	 * @throws CredentialsAlreadyExist 
	 */
	@Test
	public void testgetCredential() throws CredentialsAlreadyExist{
		// we create credential
		Credential c1 = new Credential("mencat1907@gmail.com","passwd","user");
		// we add it
		this.service.addCredentials(c1);

		// we check if it exists
		Assert.assertEquals("service.get(mencat1907@gmail.com)", this.service.getCredentialsForEmail("mencat1907@gmail.com"), c1);

		// we delete the old

		this.service.removeCredentials("mencat1907@gmail.com");
		Assert.assertFalse("service.get(mencat1907@gmail.com)",this.service.credentialsAlreadyExist("mencat1907@gmail.com"));



	}
	
	/**
	 * Check the validty of update methode
	 * @throws CredentialsAlreadyExist 
	 * 
	 */
	@Test
	public void testUpdate() throws CredentialsAlreadyExist {
		// we create credential
		Credential c1 = new Credential("mencat1907@gmail.com","passwd","user");
		// we add it
		this.service.addCredentials(c1);
		
		// we create an new credential
		Credential c2 = new Credential("mencat1907@gmail.com","password1234","user");
		
		// we update it
		this.service.updateCredentials("mencat1907@gmail.com", c2);
		// we search it
		Assert.assertEquals("this.service.get(mencat1907@gmail.com", c2, this.service.getCredentialsForEmail("mencat1907@gmail.com"));
		
		this.service.removeCredentials("mencat1907@gmail.com");
	}
	
}