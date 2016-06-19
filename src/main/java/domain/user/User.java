package domain.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 
 * Entity user.
 * 
 * @author romain_mencattini
 *
 */
@Entity
@Table(name= "USERS")
public class User implements Serializable{

	/**
	 * default serializable number
	 */
	private static final long serialVersionUID = -6146935825517747043L;

	@Id
	@NotNull
	@Column(name = "USER_ID", nullable=false)
	private Long userId;

	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "USERNAME", nullable=false)
	private String userName;

	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "EMAIL", nullable=false, unique=true)
	private String email;

	@Temporal(TemporalType.DATE)
	@Column(name = "BRITHDATE")
	private Date birthDate;

	// A link to the profile picture of the user n the website.
	@NotNull
	@Column(name = "PROFILE_PICTURE", nullable=false)
	private String profilePicture;

	@Column(name = "DESCRIPTION")
	@Size(min = 0, max = 2048)
	private String description;

	@Column(name = "UNREAD_MSG")
	private boolean unreadMsg;
		
	public User() {
		// Empty builder for injection.
	}
	
	/**
	 * 
	 * @param userName
	 *            	The username.
	 * @param email
	 *            	The user's email.
	 * @param birthDate
	 *            	The user's birthday.
	 * @param profilePicture
	 * 		  		The profile picture of user.
	 * @param description
	 * 				The user's profil description.
	 * @param unreadMsg
	 * 				A flag about unread message.
	 */
	public User(final String userName, final String email, final Date birthDate, String profilePicture, String description, boolean unreadMsg) {
		this.userName = userName;
		this.email = email;
		this.birthDate = birthDate;
		this.profilePicture = profilePicture;
		this.description = description;
		this.unreadMsg = unreadMsg;
	}

	// Redefinition of the equals() method between two User objects.
	@Override
	public boolean equals(final Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		return Objects.equals(this.userId, other.userId) && Objects.equals(this.userName, other.userName)
				&& Objects.equals(this.email, other.email) && Objects.equals(this.birthDate, other.birthDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.userId, this.userName, this.email);
	}
	
	@Override
	public String toString() { 
		return "{\"userId\":" + this.userId + ", \"userName\":\"" + this.userName + "\", \"email\":\""
				+ this.email + "\", \"birthDate\":\"" + this.birthDate + "\", \"description\":\"" 
				+ this.description + "\", \"unreadMsg\":" + this.unreadMsg +"}";
	}
	
	/**
	 * Copies the fields of a User object into the object calling the method.
	 * @param u a User object.
	 */
	public void copyBusinessFieldsFrom(User u) {
		this.setUserName(u.getUserName());
		this.setBirthDate(u.getBirthDate());
		this.setEmail(u.getEmail());
		this.setDescription(u.getDescription());
		this.setUnreadMsg(u.isUnreadMsg());
		this.setProfilePicture(u.getProfilePicture());
	}

	/**
	 * 
	 * @return the user's id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId 
	 * 			the new userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return the username
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * @param userName
	 * 			the new userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return the user's email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 * 			the new email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 * 			the user's birthday date
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * 
	 * @param birthDate
	 * 			the new birthday date to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * 
	 * @return the profile picture
	 */
	public String getProfilePicture() {
		return profilePicture;
	}

	/**
	 * 
	 * @param profilePicture
	 * 			the new profile picture to set
	 */
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	/**
	 * 
	 * @return the users' description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * 			the new description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return a boolean indicating the presence of unread messages fot the user.
	 */
	public boolean isUnreadMsg() {
		return unreadMsg;
	}

	/**
	 * 
	 * @param unreadMsg
	 * 			the new value for unreadMsg to set
	 */
	public void setUnreadMsg(boolean unreadMsg) {
		this.unreadMsg = unreadMsg;
	}
}