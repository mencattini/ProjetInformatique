package service.credential;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import domain.credential.Credential;

/**
 * Provides a set of services for the Credential class.
 * 
 * @author aurelien_coet
 *
 */
@Stateless
public class CredentialServiceImpl implements CredentialService {
	
	@PersistenceContext
	private transient EntityManager entityManager;

	@Override
	public boolean credentialsAlreadyExist(String email) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Credential> cq = qb.createQuery(Credential.class);
		cq.where(qb.equal(cq.from(Credential.class).get("email"), email));
		if (!(entityManager.createQuery(cq).getResultList().isEmpty())){
			return true;
		}
		return false;
	}

	@Override
	public Credential getCredentialsForEmail(String email) {
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Credential> cq = qb.createQuery(Credential.class);
		cq.where(qb.equal(cq.from(Credential.class).get("email"), email));
		return entityManager.createQuery(cq).getSingleResult();
	}

	@Override
	public void addCredentials(Credential credential) throws CredentialsAlreadyExist {
		if (!this.credentialsAlreadyExist(credential.getEmail())){
			entityManager.persist(credential);
		}
		else {
			throw new CredentialsAlreadyExist();
		}
	}

	@Override
	public void updateCredentials(String email, Credential update) {
		Credential cred = this.getCredentialsForEmail(email);
		entityManager.remove(cred);
		cred.copyBusinessFieldsFrom(update);	
		entityManager.persist(cred);
	}

	@Override
	public void removeCredentials(String email) {
		Credential cred = this.getCredentialsForEmail(email);
		entityManager.remove(cred);
	}

	@Override
	public String hashPasswd(String passwd) throws NoSuchAlgorithmException {
		final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(passwd.getBytes());
        byte data[] = sha512.digest();
        
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++){
        	hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));
        }
        
        return hexData.toString();
	}
}
