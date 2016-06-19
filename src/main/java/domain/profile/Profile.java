package domain.profile;

import java.util.Date;
import java.util.Objects;

/**
 * Intermediate class for the UserFacade to add new users.
 * 
 * @author aurelien_coet
 *
 */
public class Profile {
	
	private String userName;
	private String email;
	private Date birthDate;
	private String passwd;
	
	
	public Profile(){	
		// Empty builder for injection.
	}
	
	/**
	 * Instantiates a new Profile object. 
	 * @param userName the name of the new user.
	 * @param email the email of the new user.
	 * @param birthDate the birthdate of the new user.
	 * @param passwd the password of the new user on the website.
	 */
	public Profile(String userName, String email, Date birthDate, String passwd){
		this.userName = userName;
		this.email = email;
		this.birthDate = birthDate;
		this.passwd = passwd;
	}
	
	/**
	 * Getter for userName.
	 * @return the userName of the object.
	 */
	public String getUserName(){
		return this.userName;
	}
	
	/**
	 * Getter for email.
	 * @return the email of the object.
	 */
	public String getEmail(){
		return this.email;
	}
	
	/**
	 * Getter for birthDate. 
	 * @return the birthDate in the object.
	 */
	public Date getBirthDate(){
		return this.birthDate;
	}
	
	/**
	 * Getter for password.
	 * @return the password of the Profile object.
	 */
	public String getPasswd(){
		return this.passwd;
	}

	@Override
	public boolean equals(final Object obj){
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Profile other = (Profile) obj;
		boolean result = Objects.equals(this.userName, other.userName) && Objects.equals(this.email,other.email);
		result = result && Objects.equals(this.birthDate, other.birthDate);
		result = result && Objects.equals(this.passwd, other.passwd);
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.email,this.passwd);
	}
	
}
