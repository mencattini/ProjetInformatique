package service.event;

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
import domain.event.Event;
import domain.user.User;
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
 * Test eventserviceImpl.
 * 
 * @author romain_mencattini
 *
 */

@Stateless
@RunWith(Arquillian.class) 
public class EventServiceImplTest {
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,User.class.getPackage())
				.addPackages(true, Event.class.getPackage())
				.addPackage(EventService.class.getPackage())
				.addPackage(UserService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}
	
	@Inject
	private EventService service;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * Check the behavior of getEventById.
	 * @throws ParseException
	 */
	@Test
	@InSequence(1)
	public void testGetEventById() throws ParseException{
		Date date = new Date(2016-05-12);
		Time time = Time.valueOf("17:23:03");
		
		Event e1 = new Event("Basket-ball", "BB", date, time , "21c route de chêne", (float)175.56 , (float) -39.26 , new Long(2));
		e1.setEvtId(new Long(12));
		e1.setDescription("salut");
		e1.setEvtLevel(6);
		e1.setMaxSubscribers(12);
		Event e2 = this.service.getById(new Long(12));

		Assert.assertEquals("u1.equals(u2)",e1.getEvtId(),e2.getEvtId());
		Assert.assertEquals("u1.getName() == u2.getName()", e1.getEvtName() , e2.getEvtName());
	}
	
	/**
	 * Test if we rightly get all events.
	 */
	@Test
	@InSequence(2)
	public void testGetAll(){
		List<Event> l1 = this.service.getAll();
		List<Long> l2 = new ArrayList<Long>();
		List<Long> l3 = new ArrayList<Long>();
		l2.add(new Long(12));
		l2.add(new Long(123));
		l2.add(new Long(1234));
		for( Event e : l1 ){
			l3.add(e.getEvtId());
		}
		Assert.assertEquals("all user id must be equal to [12,123,1234]",l2,l3 );
	}
	
	/**
	 * Check if the update methode works.
	 */
	@Test
	@InSequence(3)
	public void testUpdate() throws ParseException{
		
		Long id = new Long(12);
		
		// do test
		// on crée un event,
		// on update l'event fictif et on compare
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("1993-07-19");
		Time time = Time.valueOf("12:12:12");
		
		Event e2 = new Event("Event test", "uni-hokey", date, time , "21c route de chêne", (float) 175.56 , (float) -39.26 , new Long(1));
		e2.setEvtId(id);
		// on sauvegarde l'user pour rétablire l'état de la bdd
		Event e1Backup = this.service.getById(id);
		
		this.service.update(id, e2);
		Event e1 = this.service.getById(id);
		
		Assert.assertEquals("e1 must be the same as e2 after copy", e1,e2);
		
		// on défait pour que les autres tests fonctionnent
		this.service.update(id, e1Backup);
		// on vérifie que le fait de défaire fonctionne bien
		
		Assert.assertEquals("e1 must be the same as backup", this.service.getById(id),e1Backup);
		
	}

	
	/**
	 * Test the return of getNbEvent. 
	 */
	@Test
	@InSequence(4)
	public void testGetNbEvent() {
		int nb = this.service.getNbEvents();
		
		Assert.assertEquals("nb of user == 3", nb, 3);
	}
	
	/**
	 * Check the methode getByName. This methode return an unique event.
	 */
	@Test
	@InSequence(5)
	public void testGetByName(){
		Event e1 = this.service.getByName("Hokey");
		Assert.assertEquals("e1.getId() == 1234 ", e1.getEvtId(), new Long(1234));
	}
	
	/**
	 * Check the methode searchbyName. This methode return a list of possible event.
	 */
	@Test
	@InSequence(6)
	public void testSearchByName(){
		ArrayList<Event> liste = (ArrayList<Event>) this.service.searchByName("Basket-ball");
		
		Assert.assertEquals("length(liste) must be 2",liste.size(),2);
		
		liste = (ArrayList<Event>) this.service.searchByName("Hokey");
		Assert.assertEquals("length(liste) must be 1", liste.size(),1);
		
		Event e1 = liste.get(0);
		Assert.assertEquals("e1.getId() == 1234 ", e1.getEvtId(), new Long(1234));
	}
	
	
	/**
	 * 
	 * Add and remove events, for testing methode.
	 * @throws ParseException
	 */
	@Test
	@InSequence(7)
	public void testAddEventAndRemove() throws ParseException{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		Date date = input.parse("1993-07-19");
		Time time = Time.valueOf("00:00:00");
		
		Event e1 = new Event("Event test", "uni-hokey", date, time , "21c route de chêne", (float)175.56 , (float)-39.26 , new Long(1));
		this.service.addEvent(e1);
		Event e2 = this.service.getByName("Event test");
		
		Assert.assertEquals("e1.getByName() == e2.getByName()", e1.getEvtName(), e2.getEvtName());
		
		// on les enlève pour garder la bdd propre
		this.service.remove(e2.getEvtId());
		Assert.assertEquals("number of event must be 3", this.service.getNbEvents(), 3);
	}
	
	/**
	 * Test the method which give us the events in the specified square.(give by coordinate).
	 */
	@Test
	@InSequence(8)
	public void testGetEventAtPos() {
		List<Event> l1 = this.service.getEventAtPos(170, 180 , -45 , -30);
		List<Event> l2 = this.service.getEventAtPos(180, 181, 70, 71);
		
		Assert.assertEquals("l1.size() == 1", l1.size(), 1);
		Assert.assertEquals("l2.size() == 0", l2.size(), 0);
	}
	
	/**
	 * We test the methode getEventBySport()
	 */
	@Test
	@InSequence(9)
	public void testGetEventBySport(){
		
		List<Event> l1 = this.service.getEventBySport("BB");
		List<Event> lEmpty1 = this.service.getEventBySport("alskdj");
		
		// size of l1 must be 1
		Assert.assertTrue("l1.size == 1", l1.size() == 1);
		
		// the element must be the same as event n°12
		Event e1 = this.service.getById(new Long(12));
		Assert.assertEquals("l1.get(0) == l2.get(0)", l1.get(0),e1);
		
		// size of emptylist must be equal to 0
		Assert.assertTrue("lempty1.size == 0", lEmpty1.size() == 0);

	}
	
	/**
	 * We check the validity  of getEventByOwner()
	 * 
	 */
	@Test
	@InSequence(10)
	public void testGetEventByOwner(){
		
		List<Event> l2 = this.service.getEventByOwner(new Long(2));
		List<Event> lEmpty = (List<Event>) this.service.getEventByOwner(new Long(100));
		
		// l2 must have size == 2
		Assert.assertEquals("l2.size() == 2", l2.size(), 2);
		
		// the first event of l2, must be the evnet n°12
		Event e1 = this.service.getById(new Long(12));
		Assert.assertEquals("l2.get(0) == e1",l2.get(0), e1);
		
		// lEmpty must be empty
		Assert.assertEquals("lEmpty == []", lEmpty, new ArrayList<Event>());
		
	}
	
}
