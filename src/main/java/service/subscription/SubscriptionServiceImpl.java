package service.subscription;

import java.util.ArrayList;
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
import domain.invitation.Invitation;
import domain.subscription.Subscription;
import domain.user.User;
import service.event.EventService;
import service.invitation.InvitationService;
import service.user.UserService;

/**
 * Implementation of SubscriptionService for injection. 
 * 
 * @author aurelien_coet
 *
 */
@Stateless
public class SubscriptionServiceImpl implements SubscriptionService {
	
	/**
	 * The entity manager that manages the persistence. As there is only one
	 * persistence unit, it takes it by default.
	 */
	@PersistenceContext
	private transient EntityManager entityManager;
	
	@Inject
	private EventService evtService;
	
	@Inject
	private UserService userService;

	@Inject
	private InvitationService invitService;
	
	@Override
	public boolean subscriptionsAlreadyExist(Subscription subscription) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Subscription> c = qb.createQuery(Subscription.class);
		
		//We create a vector of predicates 
		final List<Predicate> predicates = new ArrayList<Predicate>();
		Root<Subscription> sub = c.from(Subscription.class);
		predicates.add(qb.equal(sub.get("userId"),subscription.getUserId()));
		predicates.add(qb.equal(sub.get("eventId"),subscription.getEventId()));
		
		//We create an and predicate 
		Predicate p1 = qb.and(predicates.toArray(new Predicate[predicates.size()]));
						
		c.where(p1);
		if (!(entityManager.createQuery(c).getResultList().isEmpty())){
			return true;
		}
		return false;
	}
	
	@Override
	public List<Subscription> getSubscriptionsForUserId(Long id) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Subscription> c = qb.createQuery(Subscription.class);
		Root<Subscription> from = c.from(Subscription.class);
		Predicate condition = qb.equal(from.get("userId"), id);
		c.where(condition);
		TypedQuery<Subscription> query = entityManager.createQuery(c);
		return query.getResultList();
	}

	@Override
	public List<Subscription> getSubscriptionsForEvtId(Long id) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Subscription> c = qb.createQuery(Subscription.class);
		Root<Subscription> from = c.from(Subscription.class);
		Predicate condition = qb.equal(from.get("eventId"), id);
		c.where(condition);
		TypedQuery<Subscription> query = entityManager.createQuery(c);
		return query.getResultList();
	}

	@Override
	public List<Long> getEventIdsForUser(Long id) {
		List<Subscription> subs = this.getSubscriptionsForUserId(id);
		List<Long> ids = new ArrayList<Long>();
		for (Subscription s : subs){
			ids.add(s.getEventId());
		}
		return ids;
	}

	@Override
	public List<Long> getUserIdsForEvent(Long id) {
		List<Subscription> subs = this.getSubscriptionsForEvtId(id);
		List<Long> ids = new ArrayList<Long>();
		for (Subscription s : subs){
			ids.add(s.getUserId());
		}
		return ids;
	}
	
	@Override
	public List<Event> getEventsForUser(Long id){
		List<Long> evtIds = this.getEventIdsForUser(id);
		List<Event> events = new ArrayList<Event>();
		for (Long l : evtIds){
			events.add(evtService.getById(l));
		}
		return events;
	}

	@Override
	public List<User> getUsersForEvent(Long id){
		List<Long> userIds = this.getUserIdsForEvent(id);
		List<User> users = new ArrayList<User>();
		for (Long l : userIds){
			users.add(userService.getById(l));
		}
		return users;
	}

	@Override
	public void subscribe(Subscription subscription) throws SubscriptionAlreadyExists {
		if (!(this.subscriptionsAlreadyExist(subscription))){
			if (invitService.invitationsAlreadyExist(new Invitation(subscription.getUserId(), subscription.getEventId())))
				invitService.remove(new Invitation(subscription.getUserId(), subscription.getEventId()));
			entityManager.persist(subscription);
		}
		else
			throw new SubscriptionAlreadyExists();
	}

	@Override
	public void unsubscribe(Subscription subscription) {
		entityManager.remove(entityManager.contains(subscription) ? subscription : entityManager.merge(subscription));
	}

	@Override
	public void removeAllSubscriptionsForEvent(Long eventId) {
		List<Subscription> subs = this.getSubscriptionsForEvtId(eventId);
		for (Subscription s : subs){
			this.unsubscribe(s);
		}
	}

}
