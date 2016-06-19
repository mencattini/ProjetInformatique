package service.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import domain.message.Message;
import domain.user.User;


/**
 * 
 * Provides a set of services for the user objects.
 * 
 * @author laura_juan & Yan
 *
 */
@Stateless
public class MessageServiceImpl implements MessageService {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The entity manager that manages the persistence. As there is only one
	 * persistence unit, it takes it by default.
	 */
	@PersistenceContext
	private transient EntityManager entityManager;
	
	@Override
	public List<Message> getAll() {
		List<Message> liste;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = qb.createQuery(Message.class);		
		cq.select(cq.from(Message.class));
		cq.orderBy(qb.asc(cq.from(Message.class).get("messageId")));
		liste = new ArrayList<Message>(new LinkedHashSet<Message>(entityManager.createQuery(cq).getResultList()));
		Collections.sort(liste);
		return liste;
	}

	@Override
	public List<Message> getAllReceived(Long receiverId) {
		List<Message> liste;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = qb.createQuery(Message.class);
		cq.where(qb.equal(cq.from(Message.class).get("receiverId"), receiverId));
		liste = entityManager.createQuery(cq).getResultList();
		return liste;
	}

	@Override
	public List<Message> getAllSent(Long senderId) {
		List<Message> liste;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = qb.createQuery(Message.class);
		cq.where(qb.equal(cq.from(Message.class).get("senderId"), senderId));
		liste = entityManager.createQuery(cq).getResultList();
		return liste;
	}

	@Override
	public int getNbMessages() {
		return this.getAll().size();
	}

	@Override
	public int getNbReceived(Long receiverId) {
		return this.getAllReceived(receiverId).size();
	}

	@Override
	public int getNbSent(Long senderId) {
		return this.getAllSent(senderId).size();
	}

	@Override
	public void addMessage(Message message) {
		message.setMessageId(null);
		message.setMessageId(getNextMessageId());
		entityManager.persist(message);
	}

	@Override
	public Message getById(Long id) {
		Message message;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = qb.createQuery(Message.class);
		cq.where(qb.equal(cq.from(Message.class).get("messageId"), id));
		message = entityManager.createQuery(cq).getSingleResult();
		return message;
	}

	@Override
	public List<Message> getByText(String text) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = qb.createQuery(Message.class);
		Root<Message> from = cq.from(Message.class);
		Predicate condition = qb.like(from.<String>get("messageText"), "%"+text+"%");
		cq.where(condition);
		TypedQuery<Message> query = entityManager.createQuery(cq);
		return query.getResultList();
	}

	@Override
	public void update(Long messageId, Message message) {
		Message m1 = getById(messageId);
		entityManager.remove(m1);
		m1.copyBusinessFieldsFrom(message);
		entityManager.persist(m1);
	}
	
	@Override
	public void remove(Long messageId) {
		Message m1 = this.getById(messageId);
		entityManager.remove(m1);
	}
	
	/**
	 * Returns the next available message id in the database.
	 * 
	 * @return a new message id.
	 */
	private Long getNextMessageId() {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> c = qb.createQuery(Long.class);
		Root<Message> from = c.from(Message.class);
		c.select(qb.max(from.<Long>get("messageId")));		
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
	public List<User> getSender(Long userId){
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> c = qb.createQuery(Message.class);
		Root<Message> from = c.from(Message.class);
		c.where(qb.equal(from.<Long>get("receiverId"), userId));
		TypedQuery<Message> query = entityManager.createQuery(c);
		
		List<Message> listMessage = query.getResultList();
		HashSet<User> set = new HashSet<User>();
		if(listMessage.isEmpty()){
			return new ArrayList<User>();
		}
		else{
			for(Message m: listMessage){
				CriteriaBuilder qb1 = entityManager.getCriteriaBuilder();
				CriteriaQuery<User> c1 = qb1.createQuery(User.class);
				Root<User> from1 = c1.from(User.class);
				Predicate condition = qb1.equal(from1.get("userId"), m.getSenderId());
				c1.where(condition);
				TypedQuery<User> query1 = entityManager.createQuery(c1);
				set.add(query1.getResultList().get(0));
			}
			return new ArrayList<User>(set);
		}
	}
	
	@Override
	public List<User> getReceiver(Long userId) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> c = qb.createQuery(Message.class);
		Root<Message> from = c.from(Message.class);
		c.where(qb.equal(from.<Long>get("senderId"), userId));
		TypedQuery<Message> query = entityManager.createQuery(c);
		
		List<Message> listMessage = query.getResultList();
		HashSet<User> set = new HashSet<User>();
		if(listMessage.isEmpty()){
			return new ArrayList<User>();
		}
		else{
			for(Message m: listMessage){
				CriteriaBuilder qb1 = entityManager.getCriteriaBuilder();
				CriteriaQuery<User> c1 = qb1.createQuery(User.class);
				Root<User> from1 = c1.from(User.class);
				Predicate condition = qb1.equal(from1.get("userId"), m.getReceiverId());
				c1.where(condition);
				TypedQuery<User> query1 = entityManager.createQuery(c1);
				set.add(query1.getResultList().get(0));
			}
			return new ArrayList<User>(set);
		}
	}
	
	/**
	 * give the discussion between userOne and userTwo
	 * 
	 */
	@Override
	public List<Message> getDiscussion(Long userIdOne, Long userIdTwo){
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> c = qb.createQuery(Message.class);
		Root<Message> from = c.from(Message.class);
		
		//on crée un vecteur de prédicat qu'on va remplire 
		final List<Predicate> predicates1 = new ArrayList<Predicate>();
		predicates1.add(qb.equal(from.get("senderId"), userIdOne));
		predicates1.add(qb.equal(from.get("receiverId"), userIdTwo));
		
		// on crée le prédicat qui est une conjonction 
		Predicate p1 = qb.and(predicates1.toArray(new Predicate[predicates1.size()]));
		
		final List<Predicate> predicates2 = new ArrayList<Predicate>();
		predicates2.add(qb.equal(from.get("senderId"), userIdTwo));
		predicates2.add(qb.equal(from.get("receiverId"), userIdOne));
		
		Predicate p2 = qb.and(predicates2.toArray(new Predicate[predicates2.size()]));
		
		Predicate or = qb.or(p1,p2);
		c.where(or);
		
		TypedQuery<Message> discussion = this.entityManager.createQuery(c);
		return discussion.getResultList();
	}
	
}
