package domain.subscription;

import java.io.Serializable;

/**
 * SubscriptionId class
 * To make a composite primary key for Subscription entity
 * 
 * @author Laura Juan Galmes
 *
 */

public class SubscriptionId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2737887467021964805L;

	protected Long userId;
	
	protected Long eventId;

}
