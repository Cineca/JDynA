package it.cilea.osd.jdyna.utils;

import it.cilea.osd.common.model.Selectable;

import java.io.Serializable;

import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProperty;
@DataTransferObject
public class SimpleSelectableObject implements Selectable, Serializable {
	@Transient
	@RemoteProperty
	private String identifyingValue;
	@Transient
	@RemoteProperty
	private String displayValue;
	
	public SimpleSelectableObject() {
	}
	
	public SimpleSelectableObject(String identifyingValue, String displayValue) {
		this.identifyingValue = identifyingValue;
		this.displayValue = displayValue;
	}

		
	@RemoteMethod
	public String getIdentifyingValue() {
		if (identifyingValue == null)
			return null;
		return String.valueOf(identifyingValue);
	}

	
	@RemoteMethod
	public String getDisplayValue() {
		return displayValue;
	}

	public void setIdentifyingValue(String identifyingValue) {
		this.identifyingValue = identifyingValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
}
