package domain.invitation;

import java.io.Serializable;

/**
 * SubscriptionId class
 * To make a composite primary key for Invitation entity
 * 
 * @author Laura Juan Galmes
 *
 */
public class InvitationId implements Serializable{
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -7473464199622644987L;

	protected Long invitedId;
	
	protected Long eventId;

}
