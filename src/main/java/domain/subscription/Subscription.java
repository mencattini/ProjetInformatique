package domain.subscription;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity Subscription.
 * 
 * @author aurelien_coet
 *
 */
@Entity @IdClass(value = SubscriptionId.class)
@Table(name = "SUBSCRIPTIONS")
public class Subscription implements Serializable {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -6197672043859482503L;

	@Id
	@Column(name="USER_ID", nullable=false)
	private Long userId;
	
	@Id
	@Column(name="EVT_ID", nullable=false)
	private Long eventId;
	
	public Subscription(){
		// Empty builder for injection.
	}
	
	/**
	 * @param userId the id of the user ho subscribes to an event.
	 * @param evtId the id of the event to which the user subscribes.
	 */
	public Subscription(Long userId, Long evtId){
		this.userId = userId;
		this.eventId = evtId;
	}
	
	/**
	 * @return the userId field of the object.
	 */
	public Long getUserId(){
		return this.userId;
	}
	
	/**
	 * @param id the userId to set for the object.
	 */
	public void setUserId(Long id){
		this.userId = id;
	}
	
	/**
	 * @return the evtId field from the object.
	 */
	public Long getEventId(){
		return this.eventId;
	}
	
	/**
	 * @param id the id to set for the object.
	 */
	public void setEventId(Long id){
		this.eventId = id;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Subscription other = (Subscription) obj;
		// We do it in two steps to have a good comprehension.
		boolean result = Objects.equals(this.eventId, other.eventId);
		result = result && Objects.equals(this.userId, other.userId);
		return  result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.eventId,this.userId);
	}
	
}
