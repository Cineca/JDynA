package it.cilea.osd.common.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class SingleTimeStampInfo implements Serializable {
	
	/**
	 * Il timestamp 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	public SingleTimeStampInfo() {
		
	}
	
	public SingleTimeStampInfo(Date date) {
		this.timestamp = date;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
