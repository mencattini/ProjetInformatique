package domain.event;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 
 * Entity event.
 * 
 * @author romain_mencattini
 *
 */
@Entity
@Table(name = "SPORTS_EVENTS")
public class Event implements Serializable, Comparable<Event> {

	/**
	 * default serializable number
	 */
	private static final long serialVersionUID = -6146935825517747043L;

	@Id
	@Column(name = "EVT_ID", nullable = false)
	private Long evtId;

	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "EVT_NAME", nullable = false)
	private String evtName;

	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "SPORT", nullable = false)
	private String sport;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "EVT_DATE", nullable = false)
	private Date evtDate;

	@NotNull
	@Column(name = "EVT_TIME", nullable = false)
	// une date avec un format : hh:mm:ss
	private Time evtTime;

	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "ADRESS", nullable = false)
	private String adress;

	@NotNull
	@Column(name = "LNG", nullable = false)
	private float lng;

	@NotNull
	@Column(name = "LAT", nullable = false)
	private float lat;

	@Column(name = "DESCRIPTION")
	@Size(min = 0, max = 2048)
	private String description;

	@Column(name = "EVT_LEVEL")
	private int evtLevel;

	@Column(name = "MAX_SUBSCRIBERS")
	private int maxSubscribers;

	@Column(name = "EVALUATION")
	private int evaluation;

	@NotNull
	@Column(name = "EVT_OWNER")
	private Long evtOwnerId;

	@Transient
	private String evtOwnerName;

	@Transient
	private List<Long> participantIds;

	@Transient
	private List<String> participantNames;

	public Event() {
		// Empty builder for injection.
	}

	/**
	 * 
	 * @param evtName
	 *            this is the event Name
	 * @param sport
	 *            this is the type of sport
	 * @param evtDate
	 *            this is the date of the event
	 * @param evtTime
	 *            this is the hour of the event
	 * @param adress
	 *            the adress where there is the event
	 * @param lng
	 *            this is the longitude of event
	 * @param lat
	 *            this is the latitude of event
	 * @param evtOwner
	 *            this is the owner of Event.
	 */
	public Event(final String evtName, final String sport, final Date evtDate, final Time evtTime, final String adress,
			final float lng, final float lat, Long evtOwnerId) {

		this.evtName = evtName;
		this.sport = sport;
		this.evtDate = evtDate;
		this.evtTime = evtTime;
		this.adress = adress;
		this.lng = lng;
		this.lat = lat;
		this.evtOwnerId = evtOwnerId;
	}

	// We override equals between two objects of type Event.
	@Override
	public boolean equals(final Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Event other = (Event) obj;
		// we do it in two step to have a good comprhension
		boolean result = Objects.equals(this.evtId, other.evtId) && Objects.equals(this.evtName, other.evtName)
				&& Objects.equals(this.sport, other.sport);
		result = result && Objects.equals(this.adress, other.adress)
				&& Objects.equals(this.evtOwnerId, other.evtOwnerId);
		return result;
	}

	// We override the hashCode() method, using only what is useful to get a unique hash.
	// we refer to the SQL scheme and JPA model.
	@Override
	public int hashCode() {
		return Objects.hash(this.evtId, this.evtName, this.adress, this.evtDate, this.evtOwnerId);
	}

	@Override
	public String toString() {
		return "{\"evtId\":" + this.evtId + ", \"evtName\":\"" + this.evtName + "\", \"sport\":\"" + this.sport
				+ "\", \"evtDate\":\"" + this.evtDate + "\", \"evtTime\":\"" + this.evtTime + "\", \"adress\":\""
				+ this.adress + "\", \"lng\":" + this.lng + ", \"lat\":" + this.lat + ", \"description\":\""
				+ this.description + "\", \"evtLevel\":" + this.evtLevel + ", \"maxSubscribers\":" + this.maxSubscribers
				+ ", \"evaluation\":" + this.evaluation + "}";
	}

	/**
	 * 
	 * @param e
	 *            this is the event from which we want to copy buisness
	 *            informations
	 */
	public void copyBusinessFieldsFrom(Event e) {
		this.setEvtName(e.getEvtName());
		this.setSport(e.getSport());
		this.setEvtDate(e.getEvtDate());
		this.setAdress(e.getAdress());
		this.setEvtTime(e.getEvtTime());
		this.setEvtOwnerId(e.getEvtOwnerId());
		this.setLat(e.getLat());
		this.setLng(e.getLng());
		this.setDescription(e.getDescription());
		this.setEvtLevel(e.getEvtLevel());
		this.setMaxSubscribers(e.getMaxSubscribers());
		this.setEvaluation(e.getEvaluation());
	}

	/**
	 * 
	 * @return the id of the user who owns the event.
	 */
	public Long getEvtOwnerId() {
		return evtOwnerId;
	}

	/**
	 * 
	 * @param evtOwner
	 *            set a new ownerId.
	 */
	public void setEvtOwnerId(Long evtOwnerId) {
		this.evtOwnerId = evtOwnerId;
	}

	/**
	 * 
	 * @return the name of the user who owns the event.
	 */
	public String getEvtOwnerName() {
		return this.evtOwnerName;
	}

	/**
	 * 
	 * @param evtOwner
	 *            set a new ownerName.
	 */
	public void setEvtOwnerName(String evtOwnerName) {
		this.evtOwnerName = evtOwnerName;
	}

	/**
	 * 
	 * @return the event's id.
	 */
	public Long getEvtId() {
		return evtId;
	}

	/**
	 * 
	 * @param evtId
	 *            set a new event's id.
	 */
	public void setEvtId(Long evtId) {
		this.evtId = evtId;
	}

	/**
	 * 
	 * @return the event's name
	 */
	public String getEvtName() {
		return evtName;
	}

	/**
	 * 
	 * @param evtName
	 *            set a new event's name
	 */
	public void setEvtName(String evtName) {
		this.evtName = evtName;
	}

	/**
	 * 
	 * @return the type of sport
	 */
	public String getSport() {
		return sport;
	}

	/**
	 * 
	 * @param sport
	 *            set a new type of sport
	 */
	public void setSport(String sport) {
		this.sport = sport;
	}

	/**
	 * 
	 * @return the event date
	 */
	public Date getEvtDate() {
		return evtDate;
	}

	/**
	 * 
	 * @param evtDate
	 *            set a new event date
	 */
	public void setEvtDate(Date evtDate) {
		this.evtDate = evtDate;
	}

	/**
	 * 
	 * @return the event's hour
	 */
	public Time getEvtTime() {
		return evtTime;
	}

	/**
	 * 
	 * @param evtTime
	 *            set a new hours for the event
	 */
	public void setEvtTime(Time evtTime) {
		this.evtTime = evtTime;
	}

	/**
	 * 
	 * @return the event's adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * 
	 * @param adress
	 *            set a new adress for the event
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}

	/**
	 * 
	 * @return the longitude of event
	 */
	public float getLng() {
		return lng;
	}

	/**
	 * 
	 * @param lng
	 *            set a new longitude for event
	 */
	public void setLng(float lng) {
		this.lng = lng;
	}

	/**
	 * 
	 * @return the latitude of event
	 */
	public float getLat() {
		return lat;
	}

	/**
	 * 
	 * @param lat
	 *            set a new latitude
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}

	/**
	 * 
	 * @return the event description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            set a new event's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return the quantified level for event. 0 is noob. 9 is best
	 */
	public int getEvtLevel() {
		return evtLevel;
	}

	/**
	 * 
	 * @param evtLevel
	 *            set a new level.
	 */
	public void setEvtLevel(int evtLevel) {
		this.evtLevel = evtLevel;
	}

	/**
	 * 
	 * @return the number maxmimum of subscribers
	 */
	public int getMaxSubscribers() {
		return maxSubscribers;
	}

	/**
	 * 
	 * @param maxSubscribers
	 *            set a new max number of subscribers
	 */
	public void setMaxSubscribers(int maxSubscribers) {
		this.maxSubscribers = maxSubscribers;
	}

	/**
	 * 
	 * @return the evaluation of event
	 */
	public int getEvaluation() {
		return evaluation;
	}

	/**
	 * 
	 * @param evaluation
	 *            set a new evaluation.
	 */
	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	@Override
	public int compareTo(Event o) {
		if (this.getEvtId() > o.getEvtId())
			return 1;
		if (this.getEvtId() < o.getEvtId())
			return -1;
		return 0;
	}
}