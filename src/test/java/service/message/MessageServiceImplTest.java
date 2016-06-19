package service.message;

import java.sql.Time;
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

import domain.message.Message;
import domain.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

@Stateless
@RunWith(Arquillian.class) 
public class MessageServiceImplTest {
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,User.class.getPackage())
				.addPackages(true, Message.class.getPackage())
				.addPackage(MessageService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}
	
	@Inject
	private MessageService service;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * Check the behavior of getMessageById.
	 * @throws ParseException
	 */
	@Test
	@InSequence(1)
	public void testGetMessageById() throws ParseException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-10");
		Time time = Time.valueOf("00:00:00");
		
		Message m1 = new Message("C etait d un nul ton evenement",time,date,new Long(1),new Long(2));
		service.addMessage(m1);
		Message m2 = this.service.getById(new Long(0));

		Assert.assertEquals("m1.equals(m2)",m1.getMessageId(),m2.getMessageId());
	}

	
	/**
	 * Check the behavior of getAll, return all message.
	 * @throws ParseException
	 */
	@Test
	@InSequence(2)
	public void testGetAll() throws ParseException{
		SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = input1.parse("2016-05-11");
		Time time1 = Time.valueOf("00:00:00");
		
		Message m1 = new Message("Alors on se revoit ?",time1,date1,new Long(1),new Long(3));
		service.addMessage(m1);
		
		SimpleDateFormat input2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = input2.parse("2016-05-11");
		Time time2 = Time.valueOf("00:00:00");
		
		Message m2 = new Message("Tu reves mon cher !",time2,date2,new Long(3),new Long(1));
		service.addMessage(m2);
		
		List<Message> l1 = this.service.getAll();
		List<Long> l2 = new ArrayList<Long>();
		List<Long> l3 = new ArrayList<Long>();
		l2.add(new Long(0));
		l2.add(new Long(1));
		l2.add(new Long(2));
		for( Message m : l1 ){
			l3.add(m.getMessageId());
		}
		Assert.assertEquals("all message id must be equal to [0,1,2]",l2,l3 );
	}
		
	/**
	 * We test the messages update.
	 * @throws ParseException
	 */
	@Test
	@InSequence(3)
	public void testUpdate() throws ParseException{
		
		Long id = new Long(1);
		
		// do test
		// on cree un message,
		// on met a jour le message fictif et on compare
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-13");
		Time time = Time.valueOf("00:00:00");
		
		Message m2 = new Message("Facile a dire",time,date,new Long(2),new Long(1));
		m2.setMessageId(id);
		// on sauvegarde le message pour rétablire l'état de la bdd
		Message m1Backup = this.service.getById(id);
		
		this.service.update(id, m2);
		Message m1 = this.service.getById(id);
		
		Assert.assertEquals("m1 must be the same as m2 after copy", m1,m2);
		
		// on défait pour que les autres tests fonctionnent
		this.service.update(id, m1Backup);
		// on vérifie que le fait de défaire fonctionne bien
		
		Assert.assertEquals("m1 must be the same as backup", this.service.getById(id),m1Backup);
		
	}
	
	/**
	 * We get the number of messages, and test this result.
	 */
	@Test
	@InSequence(4)
	public void testGetNbMessage() {
		int nb = this.service.getNbMessages();
		
		Assert.assertEquals("nb of message == 3", nb, 3);
	}
	
	/**
	 * We add and remove some messages to test it.
	 * @throws ParseException
	 */
	@Test
	@InSequence(5)
	public void testAddMessageAndRemove() throws ParseException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("1993-07-19");
		Time time = Time.valueOf("00:00:00");
		
		Message m1 = new Message("Ce messsage doesnt exist !", time, date , new Long(1), new Long(1));
		this.service.addMessage(m1);
		Long id = m1.getMessageId();
		Message m2 = this.service.getById(id);
		
		Assert.assertEquals("my.getMessageId() == m2.getMessageId()", m1.getMessageId(), m2.getMessageId());
		
		// on les enlève pour garder la bdd propre
		this.service.remove(m2.getMessageId());
		Assert.assertEquals("number of messages must be 3", this.service.getNbMessages(), 3);
	}
	
	/**
	 * We get all message received for a given userId. Test the result.
	 * @throws ParseException
	 */
	@Test
	@InSequence(6)
	public void testGetAllReceived() throws ParseException{
		ArrayList<Message> liste = (ArrayList<Message>) this.service.getAllReceived(new Long(2));
		
		Assert.assertEquals("length(liste) must be 1",liste.size(),1);
		
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-13");
		Time time = Time.valueOf("00:00:00");
		
		Message m3 = new Message("My Ghost Message :)",time,date,new Long(1),new Long(2));
		service.addMessage(m3);
		
		liste = (ArrayList<Message>) this.service.getAllReceived(new Long(2));
		Assert.assertEquals("length(liste) must be 2", liste.size(),2);
		
		Message m0 = liste.get(0);
		Assert.assertEquals("m0.getId() == 2 ", m0.getMessageId(), new Long(0));
		
		Message m1 = liste.get(1);
		Assert.assertEquals("m1.getId() == 3 ", m1.getMessageId(), new Long(3));
		Assert.assertEquals("m1.getReceiverId() == 1 ", m1.getReceiverId(), new Long(2));
				
		
		service.remove(m3.getMessageId());
		
		Message m2 = new Message("Ah bon ! Pourquoi ?",time,date,new Long(2),new Long(3));
		service.addMessage(m2);
		
		
		service.remove(m2.getMessageId());
	}

	/**
	 * We get the number of received message for a give user. Test the return.
	 */
	@Test
	@InSequence(7)
	public void testGetNbReceived() {
		int nb = this.service.getNbReceived(new Long(1));
		
		Assert.assertEquals("nb of received Message == 1", nb, 1);
	}
	
	/**
	 * We get all message send by a given user. Test the return.
	 * @throws ParseException
	 */
	@Test
	@InSequence(8)
	public void testgetAllSent() throws ParseException{
		ArrayList<Message> liste = (ArrayList<Message>) this.service.getAllSent(new Long(1));
		
		Assert.assertEquals("length(liste) must be 2",liste.size(),2);
		
		liste = (ArrayList<Message>) this.service.getAllSent(new Long(2));
		Assert.assertEquals("length(liste) must be 0",liste.size(),0);
		
		liste = (ArrayList<Message>) this.service.getAllSent(new Long(3));
		Assert.assertEquals("length(liste) must be 1",liste.size(),1);
		
		Message m1 = liste.get(0);
		Assert.assertEquals("m1.getMessageId() == 3 ", m1.getMessageId(), new Long(2));
		
	}
	
	/**
	 * Test the number of sender.
	 */
	@Test
	@InSequence(9)
	public void testGetNbSent() {
		int nb = this.service.getNbSent(new Long(2));
		
		Assert.assertEquals("nb of send Message == 0", nb, 0);
		
		nb = this.service.getNbSent(new Long(1));
		
		Assert.assertEquals("nb of send Message == 2", nb, 2);
	}
	
	/**
	 * Get the text for a given id. Test the validity.
	 */
	@Test
	@InSequence(10)
	public void testgetByText(){
		Message m1 = this.service.getById(new Long(1));
		List<Message> l1 = this.service.getByText("Alors on se");
		Message m2 = l1.get(0);
		
		Assert.assertEquals("m1.getText() == m2.getText()", m1.getMessageText(), m2.getMessageText());
		Assert.assertEquals("m1 == m2 ", m1,m2);
	}
	
	/**
	 * We check the cleanning of message.
	 */
	// La base de donnees est nettoyee
	@Test
	@InSequence(11)
	public void testClearAllMessage(){
		List<Message> l1 = this.service.getAll();
		Assert.assertEquals("Le nombre de message vaut 3", l1.size(),3);
		long id = l1.get(2).getMessageId();
		service.remove(id);
		l1 = this.service.getAll();
		Assert.assertEquals("Le nombre de message vaut 2", l1.size(),2);
		id = l1.get(1).getMessageId();
		service.remove(id);
		l1 = this.service.getAll();
		Assert.assertEquals("Le nombre de message vaut 1", l1.size(),1);
		id = l1.get(0).getMessageId();
		service.remove(id);
		l1 = this.service.getAll();
		Assert.assertEquals("Le nombre de message vaut 0", l1.size(),0);
	}
	
	/**
	 * 
	 * we check the validity of getSender method
	 * 
	 */
	@Test
	@InSequence(11)
	public void testGetSender(){
		// create two message
		Date date = new Date();
		Time time = Time.valueOf("00:00:00");
		Message m1 = new Message("Test 1 2 3 4 5 6 7 8",time,date,new Long(1),new Long(3));
		Message m2 = new Message("Test 8 7 6 5 4 3 2 1",time,date,new Long(2),new Long(3));
		
		this.service.addMessage(m1);
		this.service.addMessage(m2);
		
		List<User> u = this.service.getSender(new Long(3));
		// we check the validity
		Assert.assertEquals("u.size() == 2", u.size(), 2);
		Assert.assertEquals("u.get(0).getId() == 1", u.get(0).getUserId(), new Long(1));
		Assert.assertEquals("u.get(1).getId() == 2", u.get(1).getUserId(), new Long(2));
		
		// we delete the message
		m1 = this.service.getByText("Test 1 2 3 4 5 6 7 8").get(0);
		m2 = this.service.getByText("Test 8 7 6 5 4 3 2 1").get(0);
		
		this.service.remove(m1.getMessageId());
		this.service.remove(m2.getMessageId());
		
		// list must be empty
		u = this.service.getSender(new Long(-1));
		Assert.assertEquals("u == []", u, new ArrayList<User>());
	}
	
	
	
	/**
	 * 
	 * we check the validity of getDiscussion method
	 * @throws ParseException 
	 * 
	 */
	@Test
	@InSequence(12)
	public void testGetDiscussion() throws ParseException{
		// create two message
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-13");
		Time time1 = Time.valueOf("00:02:03");
		Time time2 = Time.valueOf("00:05:02");
		Message m1 = new Message("Test 1",time1,date,new Long(4),new Long(5));
		Message m2 = new Message("Test 8",time2,date,new Long(5),new Long(4));
		
		this.service.addMessage(m1);
		this.service.addMessage(m2);
		
		List<Message> u = this.service.getDiscussion(new Long(5),new Long(4));
		// we check the validity order by messageId
		Assert.assertEquals("u.size() == 2", u.size(), 2);
		Assert.assertEquals("u.get(0) == m1", u.get(0), m1);
		Assert.assertEquals("u.get(1) == m2", u.get(1), m2);
		
		// we delete the message
		m1 = this.service.getByText("Test 1").get(0);
		m2 = this.service.getByText("Test 8").get(0);
		
		this.service.remove(m1.getMessageId());
		this.service.remove(m2.getMessageId());
		
		// list must be empty
		u = this.service.getDiscussion(new Long(-1),new Long(-9));
		Assert.assertEquals("u == []", u, new ArrayList<User>());
	}
	
	/**
	 * 
	 * we check the validity of getReceiver method
	 * 
	 */
	@Test
	@InSequence(13)
	public void testGetReceiver(){
		// create two message
		Date date = new Date();
		Time time1 = Time.valueOf("00:02:03");
		Time time2 = Time.valueOf("01:02:03");
		Message m1 = new Message("Test 3",time1,date,new Long(1),new Long(3));
		Message m2 = new Message("Test 6",time2,date,new Long(1),new Long(2));
		
		this.service.addMessage(m1);
		this.service.addMessage(m2);
		
		List<User> u = service.getReceiver(new Long(1));
		// we check the validity
		Assert.assertEquals("u.size() == 2", u.size(), 2);
		Assert.assertEquals("u.get(0).getId() == 2", u.get(0).getUserId(), new Long(2));
		Assert.assertEquals("u.get(1).getId() == 3", u.get(1).getUserId(), new Long(3));
		
		// we delete the message
		m1 = this.service.getByText("Test 3").get(0);
		m2 = this.service.getByText("Test 6").get(0);
		
		this.service.remove(m1.getMessageId());
		this.service.remove(m2.getMessageId());
		
		// list must be empty
		u = this.service.getReceiver(new Long(-1));
		Assert.assertEquals("u == []", u, new ArrayList<User>());
	}
}
