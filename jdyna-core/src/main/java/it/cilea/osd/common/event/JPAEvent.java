package it.cilea.osd.common.event;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JPAEvent implements IEvent {

	public static final String UPDATE = "UPDATE";
	public static final String CREATE = "CREATE";
	public static final String DELETE = "DELETE";

	@Column(name="jpaevent_clazz")
	private String clazz;
	
	@Column(name="jpaevent_optype")
	private String operationType;
	
	@Column(name="jpaevent_id")
	private Integer id;

	public JPAEvent() {
	}
	
	public String getClazz() {
		return clazz;
	}

	public String getOperationType() {
		return operationType;
	}

	public Integer getId() {
		return id;
	}

	public JPAEvent(String clazz, Integer id, String operationType) {
		if (clazz != null && clazz.indexOf("$$") != -1)
		{
			clazz = clazz.substring(0,clazz.indexOf("$$"));
		}
		this.clazz = clazz;
		this.id = id;
		this.operationType = operationType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDetail() {
		return operationType + " " + clazz + " " + id;
	}

}
