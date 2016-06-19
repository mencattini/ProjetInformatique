package service.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import domain.event.Event;
import domain.user.User;
import service.user.UserService;


/**
 * 
 * Provides a set of services for the event objects.
 * 
 * @author laura_juan, aurelien_coet
 *
 */
@Stateless
public class EventServiceImpl implements EventService{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5169399064860818669L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Inject
	private UserService userService;

	@Override
	public List<Event> getAll() {
		List<Event> liste;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq = qb.createQuery(Event.class);
		cq.select(cq.from(Event.class));
		cq.orderBy(qb.asc(cq.from(Event.class).get("evtId")));
		liste = new ArrayList<Event>(new LinkedHashSet<Event>(entityManager.createQuery(cq).getResultList()));
		Collections.sort(liste);
		return liste;
	}

	@Override
	public int getNbEvents() {
		return getAll().size();
	}

	@Override
	public Long addEvent(Event event) {
		event.setEvtId(null);
		event.setEvtId(getNextEventId());
		entityManager.persist(event);
		return event.getEvtId();
	}

	@Override
	public Event getById(Long id) {
		Event event;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq = qb.createQuery(Event.class);
		cq.where(qb.equal(cq.from(Event.class).get("evtId"), id));
		event = entityManager.createQuery(cq).getSingleResult();
		event.setEvtOwnerName(userService.getById(event.getEvtOwnerId()).getUserName());
		return event;
	}

	@Override
	public Event getByName(String name) {
		Event event;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq = qb.createQuery(Event.class);
		cq.where(qb.equal(cq.from(Event.class).get("evtName"), name));
		event = entityManager.createQuery(cq).getSingleResult();
		return event;
	}

	@Override
	public void update(Long eventId, Event event) {
		Event e = getById(eventId);
		entityManager.remove(e);
		e.copyBusinessFieldsFrom(event);	
		entityManager.persist(e);
	}

	@Override
	public void remove(Long eventId) {
		Event e = this.getById(eventId);
		entityManager.remove(e);
	}

	@Override
	public List<Event> searchByName(String name){
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq = qb.createQuery(Event.class);
		Root<Event> from = cq.from(Event.class);
		Predicate condition = qb.like(from.<String>get("evtName"), "%"+name+"%");
		cq.where(condition);
		TypedQuery<Event> query = entityManager.createQuery(cq);
		return query.getResultList();
	}

	/**
	 * Returns the next available event id in the database.
	 * 
	 * @return a new event id.
	 */
	private Long getNextEventId() {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> c = qb.createQuery(Long.class);
		Root<Event> from = c.from(Event.class);
		c.select(qb.max(from.<Long>get("evtId")));		
		TypedQuery<Long> query = entityManager.createQuery(c);
		System.out.println(query);

		Long nb = query.getSingleResult(); 
		if (nb == null) {
			return new Long(0);
		} else {
			return nb + 1;	
		}	
	}

	@Override
	public List<Event> getEventAtPos(double lngMin, double lngMax, double latMin, double latMax) {
		List<Event> events;
		List<Predicate> predicates = new ArrayList<Predicate>();

		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq = qb.createQuery(Event.class);

		Root<Event> root = cq.from(Event.class);
		predicates.add(qb.between(root.<Double>get("lng"), lngMin, lngMax));
		predicates.add(qb.between(root.<Double>get("lat"), latMin, latMax));
		Predicate p = qb.and(predicates.toArray(new Predicate[predicates.size()]));
		cq.where(p);
		events = entityManager.createQuery(cq).getResultList();
		for (Event e : events){
			e.setEvtOwnerName(userService.getById(e.getEvtOwnerId()).getUserName());
		}

		return events;
	}

	@Override
	public List<Event> getEventBySport(String sport){		
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);

		Root<Event> from = cq.from(Event.class);
		Predicate condition = cb.like(from.<String>get("sport"), "%"+sport+"%");
		cq.where(condition);

		TypedQuery<Event> query = entityManager.createQuery(cq);
		return query.getResultList();
	}

	@Override
	public List<Event> getEventByOwner(Long id){
		// first we take a user by name
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = qb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		Predicate condition = qb.equal(from.<Long>get("userId"), id);
		cq.where(condition);
		TypedQuery<User> query = entityManager.createQuery(cq);
		
		if (!(query.getResultList().isEmpty())){
		User u1 = query.getSingleResult();
		CriteriaBuilder qb2 = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cq2 = qb2.createQuery(Event.class);
		cq2.where(qb2.equal(cq2.from(Event.class).get("evtOwnerId"), u1.getUserId()));

		TypedQuery<Event> event= this.entityManager.createQuery(cq2);
		return event.getResultList();
		}
		else{
			return new ArrayList<Event>();
		}
	}
	
	@Override
	public 	List<Event> searchEvent(String eventName, String sport, Float lngMin, Float lngMax, Float latMin, Float latMax, String eventOwner, Integer level){
		
		// on définit les éléments de bases
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> criteria = builder.createQuery(Event.class);
		Root<Event> eRoot = criteria.from(Event.class);
		//on crée un vecteur de prédicat qu'on va remplire
		final List<Predicate> predicates = new ArrayList<Predicate>();
		
		// une liste de cas, et on ajoute les prédicats valides
		if(eventName != null){
			predicates.add(builder.like(eRoot.<String>get("evtName"), "%"+eventName+"%"));
		}
		if(sport != null){
			predicates.add(builder.like(eRoot.<String>get("sport"), "%"+sport+"%"));
		}
		if (lngMin != null && lngMax != null){
			predicates.add(builder.greaterThanOrEqualTo(eRoot.<Float>get("lng"), lngMin));
			predicates.add(builder.lessThanOrEqualTo(eRoot.<Float>get("lng"), lngMax));
		}
		if (latMin != null && latMax != null){
			predicates.add(builder.greaterThanOrEqualTo(eRoot.<Float>get("lat"), latMin));
			predicates.add(builder.lessThanOrEqualTo(eRoot.<Float>get("lat"), latMax));
		}
		if (eventOwner != null){
			// first we take a user by name
			CriteriaBuilder qb = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> cq = qb.createQuery(User.class);
			Root<User> from = cq.from(User.class);
			Predicate condition = qb.like(from.<String>get("userName"), "%"+eventOwner+"%");
			cq.where(condition);
			TypedQuery<User> query = entityManager.createQuery(cq);
			if (query.getResultList().isEmpty()){
				return new ArrayList<Event>();
			}
			else{
				User u1 = query.getResultList().get(0);
				predicates.add(builder.equal(eRoot.<Long>get("evtOwnerId"), u1.getUserId()));
			}
		}
		if (level != null){
			predicates.add(builder.equal(eRoot.<Integer>get("evtLevel"), level));
		}
		
		// on crée le prédicat qui est une conjonction 
		Predicate p = builder.and(predicates.toArray(new Predicate[predicates.size()]));
		criteria.where(p);

		
		TypedQuery<Event> event = this.entityManager.createQuery(criteria);
		return event.getResultList();
	}
}
