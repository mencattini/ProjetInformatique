package service.message;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import domain.message.Message;
import domain.user.User;

public interface MessageService extends Serializable{
	
	/**
	 * @return all messages on the system
	 */
	List<Message> getAll();
	
	/**
	 * @return all received messages 
	 */
	List<Message> getAllReceived(Long receiverId);
	
	/**
	 * @return all sent messages
	 */
	List<Message> getAllSent(Long senderId);
	
	/**
	 * @return the number of messages on the system + 1
	 */
	int getNbMessages();
	
	/**
	 * @return the number of received messages
	 */
	int getNbReceived(Long receiverId);
	
	/**
	 * @return the number of sent messages
	 */
	int getNbSent(Long senderId);
	
	/**
	 * Adds a message
	 * 
	 * @param the message to add
	 */
	void addMessage(Message message);
	
	/**
	 * Retrieves a message by its id.
	 * 
	 * @param id of the message
	 * 
	 * @return the message with the required id
	 */
	Message getById(Long id);
	
	/**
	 * Retrieves a message by its text (not sure of usefulness)
	 * 
	 * @param name of the user
	 * 
	 * @return the user with the given name
	 */
	List<Message> getByText(String text);
	
	/**
	 * Get all sender for a given user.
	 * 
	 * @param id of user
	 * 
	 * @return list of user
	 */
	List<User> getSender(Long userId);
	
	/**
	 * Retrieve all the users to whom messages were sent by a given user. 
	 * 
	 * @param userId the id of the user who sent the messages.
	 * @return A list of users who recieved messages from the input user.
	 */
	List<User> getReceiver(Long userId);
	
	/**
	 * Updates the message info with the content of message.
	 * @param messageId
	 * @param message
	 */
	void update(Long messageId, @NotNull Message message);
	
	/**
	 * Removes the message
	 * @param messageId
	 */
	void remove(Long messageId);

	/**
	 * give the discussion between userOne and userTwo
	 */
	List<Message> getDiscussion(Long userIdOne, Long userIdTwo);

}
