package domain.invitation;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity Invitation.
 * 
 * @author Laura_Juan
 *
 */
@Entity @IdClass(value = InvitationId.class)
@Table(name = "INVITATIONS")
public class Invitation implements Serializable {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -8616184535226231172L;

	@Id
	@Column(name = "USER_ID", nullable = false)
	private Long invitedId;

	@Id
	@Column(name = "EVT_ID", nullable = false)
	private Long eventId;

	public Invitation(){
		// Empty builder for injection.
	}

	public Invitation(Long invitedId, Long eventId){
			this.invitedId = invitedId;
			this.eventId = eventId;
	}

	public Long getInvitedId() {
		return this.invitedId;
	}

	public Long getEventId() {
		return this.eventId;
	}

	public void setInvitedId(Long id) {
		this.invitedId = id;
	}

	public void setEventId(Long id) {
		this.eventId = id;
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || (this.getClass() != obj.getClass()))
			return false;
		final Invitation other = (Invitation) obj;
		boolean result = Objects.equals(this.invitedId, other.invitedId);
		result = result && Objects.equals(this.eventId, other.eventId);
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.invitedId, this.eventId);
	}

}
