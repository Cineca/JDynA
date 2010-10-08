package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.IdentifiableObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="jdyna_containables")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class Containable<P> extends IdentifiableObject implements IContainable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTAINABLE_SEQ")
    @SequenceGenerator(name = "CONTAINABLE_SEQ", sequenceName = "CONTAINABLE_SEQ")
	private Integer id;	
		
	public abstract void setReal(P object);
	
	@Transient
	public abstract P getObject();
	
	@Transient
	public P getReal(){
		return getObject();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}
