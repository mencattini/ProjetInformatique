package domain.message;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 
 * Entity message.
 * 
 * @author romain_mencattini
 *
 */
@Entity
@Table(name = "MESSAGES")
public class Message implements Serializable , Comparable<Message>{
	
	/**
	 * default serializable number
	 */
	private static final long serialVersionUID = -6146935825517747043L;

	@Id
	@NotNull
	@Column(name = "MSG_ID", nullable=false)
	private Long messageId;
	
	// Content of the message.
	@NotNull
	@Size(min=1,max=2048)
	@Column(name = "MSG_TEXT", nullable=false)
	private String messageText;
	
	@NotNull
	@Column(name = "MSG_TIME", nullable=false, columnDefinition="time")
	private Time messageTime;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "MSG_DATE", nullable=false)
	private Date messageDate;
	
	@NotNull
	@Column(name = "SENDER", nullable=false)
	private Long senderId;
	
	@NotNull
	@Column(name = "RECEIVER", nullable=false)
	private Long receiverId;
	
	public Message() {
		// Empty builder for injection.
	}
	
	/**
	 * 
	 * @param messageText
	 * 		the complete text to send
	 * @param messageTime
	 * 		the hours of sending
	 * @param messageDate
	 * 		the date of sending
	 * @param senderId
	 * 		the id of user sender
	 * @param receiverId
	 * 		the id of user receiver
	 */
	public Message(final String messageText, final Time messageTime, final Date messageDate, final Long senderId, final Long receiverId){
		this.messageText = messageText;
		this.messageTime = messageTime;
		this.messageDate = messageDate;
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
	
	@Override
	public boolean equals(final Object obj){
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Message other = (Message) obj;
		boolean result = Objects.equals(this.messageId, other.messageId) && Objects.equals(this.messageText,other.messageText);
		result = result && Objects.equals(this.senderId, other.senderId) && Objects.equals(this.receiverId, other.receiverId);
		result = result && Objects.equals(this.messageDate, other.messageDate) && Objects.equals(this.messageTime, other.messageTime);
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.messageId,this.messageText);
	}
	
	@Override
	public String toString() { 
		return "{\"messageId\":"+this.messageId+", \"messageText\":\""+this.messageText+"\", \"messageTime\":\""+this.messageTime+"\", \"messageDate\":\""
				+this.messageDate+"\", \"senderId\":"+this.senderId+", \"receiverId\":"+this.receiverId+"}";
	}
	
	/**
	 * Copies the fields from a Message object into the one calling the method.
	 * @param m
	 * 		m is an instance of message from which
	 * 		we want to copy informations
	 */
	public void copyBusinessFieldsFrom(Message m) {
		this.setMessageId(m.getMessageId());
		this.setMessageText(m.getMessageText());
		this.setMessageTime(m.getMessageTime());
		this.setMessageDate(m.getMessageDate());
		this.setSenderId(m.getSenderId());
		this.setReceiverId(m.getReceiverId());
	}

	/**
	 * 
	 * @return the id of message
	 */
	public Long getMessageId() {
		return messageId;
	}

	/**
	 * 
	 * @param messageId
	 * 		set a new id for message
	 */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	/** 
	 * 
	 * @return the text of message
	 */
	public String getMessageText() {
		return messageText;
	}

	/**
	 * 
	 * @param messageText
	 * 		set a new text for the message
	 */
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	/**
	 * 
	 * @return the hours of message
	 */
	public Time getMessageTime() {
		return messageTime;
	}

	/**
	 * 
	 * @param messageTime
	 * 		set a new hours for message
	 */
	public void setMessageTime(Time messageTime) {
		this.messageTime = messageTime;
	}

	/**
	 * 
	 * @return the message date
	 */
	public Date getMessageDate() {
		return messageDate;
	}

	/** 
	 * 
	 * @param messageDate
	 * 		set a new date for message
	 */
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	/**
	 * 
	 * @return the id of sender
	 */
	public Long getSenderId() {
		return senderId;
	}

	/**
	 * 
	 * @param senderId
	 * 		set a new senderId
	 */
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	/**
	 * 
	 * @return the id of receiver
	 */
	public Long getReceiverId() {
		return receiverId;
	}

	/**
	 * 
	 * @param receiverId
	 * 		set a new id for receiver
	 */
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	
	@Override
	public int compareTo(Message o) {
		if (this.getMessageId() > o.getMessageId()) return 1;
		if (this.getMessageId() < o.getMessageId()) return -1;
		return 0;
	}
}