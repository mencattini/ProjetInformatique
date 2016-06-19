package domain.credential;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity Credential.
 * @author aurelien_coet
 *
 */
@Entity
@Table(name="PASSWORDS")
public class Credential {
	
	@Id
	@NotNull
	@Column(name="EMAIL", nullable=false)
	private String email;
	
	@NotNull
	@Column(name="PASSWD", nullable=false)
	private String passwd;
	
	@NotNull
	@Column(name="ROLE", nullable=false)
	private String role;
	
	/**
	 *  Empty builder for injection.
	 */
	public Credential(){
	}
	
	/**
	 * @param email the email for the credentials to add to the database.
	 * @param passwd the passwd.
	 * @param role the role of the user whose credentials are being added to the database.
	 */
	public Credential(final String email, final String passwd, final String role){
		this.email = email;
		this.passwd = passwd;
		this.role = role;
	}
	/**
	 * @return the email field of the object.
	 */
	public String getEmail(){
		return this.email;
	}
	
	/**
	 * @param email the email to set for the object.
	 */
	public void setEmail(String email){
		this.email = email;
	}
	
	/**
	 * @return the passwd field of the object.
	 */
	public String getPasswd(){
		return this.passwd;
	}
	
	/**
	 * @param passwd to passwd to set for the object.
	 */
	public void setPasswd(String passwd){
		this.passwd = passwd;
	}
	
	/**
	 * @return the role field of the object.
	 */
	public String getRole(){
		return this.role;
	}
	
	/**
	 * @param role the role to set for the object.
	 */
	public void setRole(String role){
		this.role = role;
	}
	
	/**
	 * Copies the fields from a Credential object into the one calling this method.
	 * @param credential a Credential object from which to copy the fields.
	 */
	public void copyBusinessFieldsFrom(Credential credential){
		this.email = credential.getEmail();
		this.passwd = credential.getPasswd();
		this.role = credential.getRole();
	}
	
	// Redefinition of equals between two events.
	@Override
	public boolean equals(final Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Credential other = (Credential) obj;
		// We do it in two step to have a good comprehension.
		boolean result = Objects.equals(this.email, other.email) && Objects.equals(this.passwd, other.passwd);
		result = result && Objects.equals(this.role,other.role);
		return  result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.email,this.role);
	}
	
}
