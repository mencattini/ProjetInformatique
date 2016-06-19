package service.user;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import domain.user.User;

/**
 * 
 * Provides a set of services for the user objects.
 * 
 * @author aurelien_coet
 *
 */
@Stateless
public class UserServiceImpl implements UserService{
	
	/** Serial id */
	private static final long serialVersionUID = -1966455317091313119L;
	
	/**
	 * The entity manager that manages the persistence. As there is only one
	 * persistence unit, it takes it by default.
	 */
	@PersistenceContext
	private transient EntityManager entityManager;

	@Override
	public List<User> getAll() {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> c = qb.createQuery(User.class);

		Root<User> variableRoot = c.from(User.class);
		c.select(variableRoot);
		c.orderBy(qb.asc(variableRoot.get("userId")));
		TypedQuery<User> query = entityManager.createQuery(c);
		return query.getResultList();
	}

	@Override
	public int getNbUsers() {
		return getAll().size();
	}

	@Override
	public void addUser(User user) {
		user.setUserId(null);
		user.setUserId(getNextUserId());
		entityManager.persist(user);
	}

	@Override
	public User getById(Long id) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> c = qb.createQuery(User.class);
		Root<User> from = c.from(User.class);
		Predicate condition = qb.equal(from.get("userId"), id);
		c.where(condition);
		TypedQuery<User> query = entityManager.createQuery(c);
		return query.getSingleResult();
	}

	@Override
	public User getByName(String name) {
		User user;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = qb.createQuery(User.class);
		cq.where(qb.equal(cq.from(User.class).get("userName"), name));
		user = entityManager.createQuery(cq).getSingleResult();
		return user;
	}
	
	@Override
	public List<User> searchByName(String name){
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = qb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		Predicate condition = qb.like(from.<String>get("userName"), "%"+name+"%");
		cq.where(condition);
		TypedQuery<User> query = entityManager.createQuery(cq);
		return query.getResultList();
	}

	@Override
	public void update(Long userId, User user) {
		User u = getById(userId);
		entityManager.remove(u);
		u.copyBusinessFieldsFrom(user);	
		entityManager.persist(u);
	}

	/**
	 * Returns the next available user id in the database.
	 * 
	 * @return a new user id.
	 */
	private Long getNextUserId() {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> c = qb.createQuery(Long.class);
		Root<User> from = c.from(User.class);
		c.select(qb.max(from.<Long>get("userId")));		
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
	public void deleteUser(User user){
		entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
	}
	
	@Override
	public User getByEmail(String email) {
		User user;
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = qb.createQuery(User.class);
		cq.where(qb.equal(cq.from(User.class).get("email"), email));
		user = entityManager.createQuery(cq).getSingleResult();
		return user;
	}

	@Override
	public User searchByEmail(String email) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = qb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		Predicate condition = qb.like(from.<String>get("email"), "%"+email+"%");
		cq.where(condition);
		TypedQuery<User> query = entityManager.createQuery(cq);
		return query.getSingleResult();
	}
}
