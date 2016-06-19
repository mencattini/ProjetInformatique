package service.credential;

import java.security.NoSuchAlgorithmException;

import domain.credential.Credential;

public interface CredentialService {
	
	/**
	 * Tests if there already are credentials in the database with the given email.
	 * 
	 * @param email the email for which to check if it is already present in the database.
	 * @return a boolean value indicating if the given email is already attributed to credentials.
	 */
	boolean credentialsAlreadyExist(String email);
	
	/**
	 * Retrieves a Credential object from the database with the given email as key.
	 * 
	 * @param email the email of the Credential to retrieve.
	 * @return the Credential object with given email as key.
	 */
	Credential getCredentialsForEmail(String email);
	
	/**
	 * Adds the given credentials to the database.
	 * 
	 * @param credential a Credential object containing the information to add to the database.
	 * @throws CredentialsAlreadyExist 
	 */
	void addCredentials(Credential credential) throws CredentialsAlreadyExist;
	
	/**
	 * Updates the credentials for the given email in the database.
	 * 
	 * @param email the email for which to update the credentials.
	 * @param update a Credential object containing the info for the update.
	 */
	void updateCredentials(String email, Credential update);
	
	/**
	 * Removes the credentials for a given email from the database.
	 * 
	 * @param email the email of the user for which to remove the credentials.
	 */
	void removeCredentials(String email);
	
	/**
	 * Hashes a password with SHA-512 for persistence in the database.
	 * @param passwd the password to hash.
	 * @return a String consisting of the hashed password.
	 * @throws NoSuchAlgorithmException 
	 */
	String hashPasswd(String passwd) throws NoSuchAlgorithmException;

}
