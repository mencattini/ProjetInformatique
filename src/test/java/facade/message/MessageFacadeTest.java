package facade.message;

import java.net.URISyntaxException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import domain.message.Message;
import domain.user.User;
import facade.message.MessageFacade;
import service.message.MessageService;
import service.user.UserService;

/**
 * 
 * Test class for facade message
 * 
 * @author Yen
 *
 */
@Stateless
@RunWith(Arquillian.class)
public class MessageFacadeTest{

	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,Message.class.getPackage(),User.class.getPackage())
				.addPackage(MessageService.class.getPackage())
				.addPackages(true,MessageFacade.class.getPackage(),UserService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}

	@Inject
	private MessageService messageService;
	@Inject
	private MessageFacade messageFacade;

	/**
	 * 
	 * We check add & getNbReceived  
	 * 
	 * We create 1 message. We add it to the data base. We check the status of 2 messages. 
	 * @throws URISyntaxException
	 * @throws ParseException 
	 */
	@Test
	@InSequence(1)
	public void testAddMessageAndGetMessageById() throws ParseException, URISyntaxException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-10");
		Time time = Time.valueOf("00:00:00");

		Message m1 = new Message("Hello World Message !",time,date,new Long(1),new Long(2));

		messageFacade.addMessage(m1);

		Assert.assertEquals("messageFacade.getById(0).getStatus() == 200", messageFacade.getMessageById(new Long(0)).getStatus(),200);
		Assert.assertEquals("messageFacade.getById(12).getStatus() == 404", messageFacade.getMessageById(new Long(12)).getStatus(),404);


	}

	/**
	 * 
	 * We check getMessagesBySender  
	 * 
	 * We create 3 more message. We add it to the data base. We check the list of message by sender. 
	 * @throws ParseException 
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(2)
	public void testGetMessagesBySender() throws ParseException, URISyntaxException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-10");
		Time time = Time.valueOf("00:00:00");

		Message m1 = new Message("Hello World 2!",time,date,new Long(3),new Long(2));

		SimpleDateFormat input2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = input2.parse("2016-05-10");
		Time time2 = Time.valueOf("00:00:00");

		Message m2 = new Message("Funny :)",time2,date2,new Long(4),new Long(1));

		SimpleDateFormat input3 = new SimpleDateFormat("yyyy-MM-dd");
		Date date3 = input3.parse("2016-05-10");
		Time time3 = Time.valueOf("00:00:00");

		Message m3 = new Message("XD :)",time3,date3,new Long(4),new Long(3));

		messageFacade.addMessage(m1);
		messageFacade.addMessage(m2);
		messageFacade.addMessage(m3);

		List<Message> l1 = (List<Message>) messageFacade.getMessagesBySender(new Long(1)).getEntity();
		List<Message> l2 = (List<Message>) messageFacade.getMessagesBySender(new Long(2)).getEntity();
		List<Message> l3 = (List<Message>) messageService.getAllSent(new Long(1));
		List<Message> l4 = (List<Message>) messageFacade.getMessagesBySender(new Long(4)).getEntity();

		Assert.assertEquals("l1.size == l3.size()", l1.size(), l3.size());
		Assert.assertEquals("l1.size() == 1", l1.size(), 1);
		Assert.assertEquals("l4.get(0).getId() == 2", l4.get(0).getMessageId(), new Long(2));
		Assert.assertEquals("l4.get(1).getId == 3", l4.get(1).getMessageId(), new Long(3));
		Assert.assertEquals("l2.size() == 0", l2.size(), 0);

	}

	/**
	 * 
	 * We check getMessagesByReceive  
	 * 
	 *  We check the list of message by Receive.
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(3)
	public void testGetMessagesByReceiver(){

		List<Message> l1 = messageService.getAllReceived(new Long(3));
		List<Message> l2 = (List<Message>) messageFacade.getMessagesByReceiver(new Long(2)).getEntity();
		List<Message> l3 = (List<Message>) messageFacade.getMessagesByReceiver(new Long(3)).getEntity();
		List<Message> l4 = (List<Message>) messageFacade.getMessagesByReceiver(new Long(4)).getEntity();

		Assert.assertEquals("l1.size == l3.size()", l1.size(), l3.size());
		Assert.assertEquals("l3.size() == 1", l3.size(), 1);
		Assert.assertEquals("l2.get(0).getId() == 0", l2.get(0).getMessageId(), new Long(0));
		Assert.assertEquals("l2.get(1).getId == 1", l2.get(1).getMessageId(), new Long(1));
		Assert.assertEquals("l4.size() == 0", l4.size(), 0);

	}

	/**
	 * 
	 * We check getAllMessage  
	 * 
	 *  We check the number of message. We add a message. We check again the number of message.
	 * @throws ParseException 
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(4)
	public void testGetAllMessage() throws ParseException, URISyntaxException{

		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-10");
		Time time = Time.valueOf("00:00:00");

		Message m1 = new Message("It's a new message from Geneva (OoO)",time,date,new Long(2),new Long(6));

		List<Message> l1 = messageService.getAll();
		List<Message> l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 4", l2.size(), 4);

		messageFacade.addMessage(m1);

		l1 = messageService.getAll();
		l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 5", l2.size(), 5);

	}

	/**
	 * 
	 * We check remove  
	 * 
	 *  We remove all the message 1 by 1 and check the size each time.
	 * @throws ParseException 
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(5)
	public void testRemove() throws ParseException, URISyntaxException{

		List<Message> l1 = messageService.getAll();
		List<Message> l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 5", l2.size(), 5);

		Long removeId = l1.get(2).getMessageId();

		messageFacade.remove(removeId);

		l1 = messageService.getAll();
		l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 4", l2.size(), 4);

		removeId = l2.get(3).getMessageId();

		messageFacade.remove(removeId);

		l1 = messageService.getAll();
		l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 3", l2.size(), 3);

		removeId = l1.get(1).getMessageId();

		messageFacade.remove(removeId);

		l1 = messageService.getAll();
		l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 2", l2.size(), 2);

		removeId = l2.get(0).getMessageId();

		messageFacade.remove(removeId);

		l1 = messageService.getAll();
		l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 1", l2.size(), 1);

		removeId = l1.get(0).getMessageId();

		messageFacade.remove(removeId);

		l1 = messageService.getAll();
		l2 = (List<Message>) messageFacade.getAllMessages().getEntity();

		Assert.assertEquals("l1.size == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l2.size() == 0", l2.size(), 0);


	}

	/**
	 * 
	 * we check the showSender
	 * @throws ParseException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(6)
	public void testShowSender(){
		// create two message
		Date date = new Date();
		Time time = Time.valueOf("00:00:00");
		Message m1 = new Message("Test 1",time,date,new Long(1),new Long(3));
		Message m2 = new Message("Test 8",time,date,new Long(2),new Long(3));

		this.messageService.addMessage(m1);
		this.messageService.addMessage(m2);

		int code = this.messageFacade.showSender(new Long(3)).getStatus();
		List<User> u = (List<User>) this.messageFacade.showSender(new Long(3)).getEntity();

		// code must be 200 & u.size == 2
		// we check the validity
		Assert.assertEquals("u.size() == 2", u.size(), 2);
		Assert.assertEquals("u.get(0).getId() == 1", u.get(0).getUserId(), new Long(1));
		Assert.assertEquals("u.get(1).getId() == 2", u.get(1).getUserId(), new Long(2));

		Assert.assertEquals("code == 200",code,200);

		// check for empty
		// we delete the message
		m1 = this.messageService.getByText("Test 1").get(0);
		m2 = this.messageService.getByText("Test 8").get(0);

		this.messageService.remove(m1.getMessageId());
		this.messageService.remove(m2.getMessageId());

		// list must be empty
		u = (List<User>) this.messageFacade.showSender(new Long(-1)).getEntity();
		Assert.assertEquals("u == []", u, new ArrayList<User>());
	}

	/**
	 * 
	 * we check the showDiscussion
	 * @throws ParseException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(7)
	public void testShowDiscussion() throws ParseException{
		// create two message
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("2016-05-13");
		Time time1 = Time.valueOf("00:03:00");
		Time time2 = Time.valueOf("01:00:00");
		Message m1 = new Message("Test 1 2 3 4 5 6 7 8",time1,date,new Long(1),new Long(3));
		Message m2 = new Message("Test 8 7 6 5 4 3 2 1",time2,date,new Long(3),new Long(1));

		this.messageService.addMessage(m1);
		this.messageService.addMessage(m2);

		int code = this.messageFacade.showDiscussion(new Long(3),new Long(1)).getStatus();
		List<Message> u = (List<Message>) this.messageFacade.showDiscussion(new Long(3),new Long(1)).getEntity();

		// code must be 200 & u.size == 2
		// we check the validity
		Assert.assertEquals("u.size() == 2", u.size(), 2);
		Assert.assertEquals("u.get(0) == m1", u.get(0),m1);
		Assert.assertEquals("u.get(1) == m2", u.get(1),m2);

		Assert.assertEquals("code == 200",code,200);

		// check for empty
		// we delete the message
		m1 = this.messageService.getByText("Test 1 2 3 4 5 6 7 8").get(0);
		m2 = this.messageService.getByText("Test 8 7 6 5 4 3 2 1").get(0);

		this.messageService.remove(m1.getMessageId());
		this.messageService.remove(m2.getMessageId());

		// list must be empty
		u = (List<Message>) this.messageFacade.showDiscussion(new Long(-1),new Long(-9)).getEntity();
		Assert.assertEquals("u == []", u, new ArrayList<User>());
	}	
}