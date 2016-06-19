package service.user;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import domain.user.User;

/**
 * Provides a set of service for the User class.
 * @author aurelien_coet
 *
 */
@Local
public interface UserService extends Serializable {
	
	/**
	 * @return all the registered users
	 */
	List<User> getAll();
	
	/**
	 * @return the number of registered users
	 */
	int getNbUsers();
	
	/**
	 * Adds a user to the database.
	 * 
	 * @param user to add
	 */
	void addUser(@NotNull User user);
	
	/**
	 * Retrieves a user by its id.
	 * 
	 * @param id of the user
	 * @return the user with the required id
	 */
	User getById(Long id);
	
	/**
	 * Retrieves a user by its name.
	 * 
	 * @param name of the user
	 * @return the user with the given name
	 */
	User getByName(String name);
	
	/**
	 * Searches in the database for users with a given pattern in their name.
	 * 
	 * @param name the name of the user to search, or part of it.
	 * @return the list of users whose username contains the name (pattern)
	 * 			input as a parameter.
	 */
	List<User> searchByName(String name);
	
	/**
	 * Retrieves a user from the database with its email.
	 * 
	 * @param email the email of the user to retrieve.
	 * @return a user object containing the info about the user with the given email.
	 */
	User getByEmail(String email);
	
	/**
	 * Searches in the database for users with a given pattern in their email.
	 * 
	 * @param email the name of the user to search.
	 * @return the unique user whose email contains the email
	 * 			input as a parameter.
	 */
	User searchByEmail(String email);
	
	/**
	 * Updates the user info with the content of user.
	 * @param userId the id of the user whose information must be updated.
	 * @param user a User object containing the info for the update.
	 */
	void update(Long userId, @NotNull User user);
	
	/**
	 * 
	 * @param user
	 * 		the user that we want to delete.
	 */
	void deleteUser(User user);
}
