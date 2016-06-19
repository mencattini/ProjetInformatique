package service.invitation;

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
import domain.user.User;
import service.event.EventService;
import service.user.UserService;


/**
 * Implementation of InvitationService
 * 
 * @author Laura_Juan
 *
 */
@Stateless
public class InvitationServiceImpl implements InvitationService {
	
	@PersistenceContext
	private transient EntityManager entityManager;
	
	@Inject
	private EventService evtService;
	
	@Inject
	private UserService userService;

	@Override
	public boolean invitationsAlreadyExist(Invitation invitation) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Invitation> c = qb.createQuery(Invitation.class);
		
		//We create a vector of predicates 
		final List<Predicate> predicates = new ArrayList<Predicate>();
		Root<Invitation> in = c.from(Invitation.class);
		predicates.add(qb.equal(in.get("invitedId"),invitation.getInvitedId()));		
		predicates.add(qb.equal(in.get("eventId"),invitation.getEventId()));
		
		//We create an and predicate 
		Predicate p1 = qb.and(predicates.toArray(new Predicate[predicates.size()]));
				
		c.where(p1);
		if (!(entityManager.createQuery(c).getResultList().isEmpty())){
			return true;
		}
		return false;
	}

	
	@Override
	public List<Invitation> getInvitationsForInvitedId(Long id) {
		
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Invitation> c = qb.createQuery(Invitation.class);
		Root<Invitation> from = c.from(Invitation.class);
		Predicate condition = qb.equal(from.get("invitedId"), id);
		c.where(condition);
		TypedQuery<Invitation> query = entityManager.createQuery(c);
		return query.getResultList();
		
	}

	@Override
	public List<Invitation> getInvitationsForEventId(Long id) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Invitation> c = qb.createQuery(Invitation.class);
		Root<Invitation> from = c.from(Invitation.class);
		Predicate condition = qb.equal(from.get("eventId"), id);
		c.where(condition);
		TypedQuery<Invitation> query = entityManager.createQuery(c);
		return query.getResultList();
	}


	@Override
	public List<Long> getEventIdsForInvited(Long id) {
		List<Invitation> invitations = this.getInvitationsForInvitedId(id);
		List<Long> ids = new ArrayList<Long>();
		for (Invitation i : invitations){
			ids.add(i.getEventId());
		}
		return ids;
	}

	@Override
	public List<Long> getInvitedIdsForEvent(Long id) {
		List<Invitation> invitations = this.getInvitationsForEventId(id);
		List<Long> ids = new ArrayList<Long>();
		for (Invitation i : invitations){
			ids.add(i.getInvitedId());
		}
		return ids;
	}
	
	@Override
	public List<Event> getEventsForInvited(Long id) {
		List<Long> eventIds = this.getEventIdsForInvited(id);
		List<Event> events = new ArrayList<Event>();
		for (Long eId : eventIds){
			events.add(evtService.getById(eId));
		}
		return events;
	}

	@Override
	public List<User> getInvitedForEvent(Long id) {
		List<Long> invitedIds = this.getInvitedIdsForEvent(id);
		List<User> invited = new ArrayList<User>();
		for (Long iId : invitedIds){
			invited.add(userService.getById(iId));
		}
		return invited;
	}

	@Override
	public void invite(Invitation invitation) throws InvitationAlreadyExists {
		if (!(this.invitationsAlreadyExist(invitation)))
			entityManager.persist(invitation);
		else
			throw new InvitationAlreadyExists();
	}

	@Override
	public void remove(Invitation invitation) {
		entityManager.remove(entityManager.contains(invitation) ? invitation : entityManager.merge(invitation));
	}

	@Override
	public void removeAllInvitesForEvent(Long eventId) {
		List<Invitation> invites = this.getInvitationsForEventId(eventId);
		for (Invitation i : invites){
			this.remove(i);
		}
	}

}
