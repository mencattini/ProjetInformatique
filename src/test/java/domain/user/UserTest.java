package domain.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * Test entity user.
 * 
 * @author romain_mencattini
 *
 */

public class UserTest {
	
	/**
	 * we test the constructor and the equality between two instances.
	 * Using hashcode and equals
	 */
	@Test
	public void shouldBeEqual() {
		Date birthDate = new Date();
		
		// on crée le même user 
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		User u2 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		// on regarde si les hash code sont les mêmes, et on regarde si l'override du equals fonctionne
		Assert.assertEquals("u1=u2", u1.hashCode(),u2.hashCode());
		Assert.assertEquals("u1=u2", u1,u2);
		// on change la description de l'un
		u1.setDescription("Description Romain Mnecattini");
		// on vérifie que le hashcode et le equals régaissent bien
		Assert.assertEquals("u1=u2",u1.hashCode(),u2.hashCode());
		Assert.assertEquals("u1=u2",u1,u2);
		
		// on vérifie si on change le flags des messages
		u1.setUnreadMsg(false);
		Assert.assertEquals("u1=u2",u1.hashCode(),u2.hashCode());
		Assert.assertEquals("u1=u2",u1,u2);
		
		// on vérifie si on change l'image
		u1.setProfilePicture("http://www.une.adresse.ch");
		Assert.assertEquals("u1=u2",u1.hashCode(),u2.hashCode());
		Assert.assertEquals("u1=u2",u1,u2);
	}
	
	/**
	 * test in some cases, that entities aren't the same.
	 */
	@Test
	public void shouldBeNotEqual() {
		
		Date birthDate = new Date();
		
		// on crée le même user
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		User u2 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		
		u1.setUserName("Romain Bencattini");
		
		Assert.assertFalse("u1 = u2", u1.equals(u2));
		Assert.assertFalse("u1 = u2", u1.hashCode() == u2.hashCode());
	
		u1.setUserName("Romain Mencattini");
		u1.setEmail("malks@bluewin.ch");
		
		Assert.assertFalse("u1 = u2", u1.equals(u2));
		Assert.assertFalse("u1 = u2", u1.hashCode() == u2.hashCode());
		
		Assert.assertFalse("u1.equals(null) == false ", u1.equals(new Long(10)));
	}
	
	/**
	 * it checks if an entity has the right string (json) format
	 * @throws Exception
	 */
	@Test
	public void shouldToStringHaveTheRightForm() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = dateFormat.parse("2020-12-5");
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",date,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);

		// on vérifie que le retour en json est le même que celui espréré
		Assert.assertEquals("s1.toString()", 
				"{\"userId\":null, \"userName\":\"Romain Mencattini\", \"email\":\"mrl@bluewin.ch\", \"birthDate\":"
				+ "\"Sat Dec 05 00:00:00 CET 2020\", "
						+ "\"description\":\"Description de Romain Mencattini\""
						+ ", \"unreadMsg\":true}",
						u1.toString());
		
	}
	
	/**
	 * test the behavior of copy methode. (the equality)
	 */
	@Test
	public void shouldCopyFromStudentBeEqualToSource() {
		
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",new Date(),"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		User u2 = new User("Jean René","1234@bluewin.ch",new Date(),"http://une.addresse.au.bol.2.ch","Description de Jean René",false);
		User u3 = new User();

		u1.copyBusinessFieldsFrom(u2);
		u3.copyBusinessFieldsFrom(u2);
		
		Assert.assertEquals("u1=u2", u1, u2);
		Assert.assertEquals("u1 = u2", u1.hashCode(),u2.hashCode());
		
		Assert.assertEquals("u1 = u2, equals", u3, u2);
		Assert.assertEquals("u1 = u2, hashcode", u3.hashCode(),u2.hashCode());
	}
	
	/**
	 * test some particular getter/setter (userId)
	 */
	@Test
	public void shouldGetSetUserIdWorks(){
		
		/**
		 * Partie de test get/set pour user
		 */
		User u1 = new User();
		
		Assert.assertEquals("u1.getId() == null ", u1.getUserId(), null);
		
		u1.setUserId((long) 1000);
		
		Assert.assertTrue("u1.getId() == 1000", u1.getUserId().equals((long) 1000));
	}
	
	/**
	 * test some particular getter/setter (profil/picture)
	 */
	@Test
	public void shouldGetSetProfilPicturesdWorks(){
		
		/**
		 * Partie de test get/set pour user
		 */
		User u1 = new User();
		
		Assert.assertEquals("u1.getProfilePicture() == null ", u1.getProfilePicture(), null);
		
		u1.setProfilePicture("anticonstitutionnellement");
		
		Assert.assertTrue("u1.getProfilePicture() == anticonstitutionnellement", u1.getProfilePicture().equals("anticonstitutionnellement"));	
	}
	
}