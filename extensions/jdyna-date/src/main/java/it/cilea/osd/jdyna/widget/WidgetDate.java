/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.jdyna.widget;


import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.DateValue;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.propertyeditors.CustomDateEditor;


/**
 * Classe puntatore
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name="jdyna_widget_date")
@NamedQueries( {  
	@NamedQuery(name = "WidgetDate.findAll", query = "from WidgetDate order by id")
    })
public class WidgetDate extends AWidget {
		
	private Integer minYear;
	private Integer maxYear;
	
	/** se devono inserire l'ora o meno*/
	private boolean time;
		
	
	public long toMilliSeconds(int anno, int mese, int giorno) {
		
		GregorianCalendar data = new GregorianCalendar(anno,mese,giorno);
		Date d1 = data.getTime();
	    long l1 = d1.getTime();
	    
	    return l1;
		
	}
	
	public String getConfiguration() {
		return "minYear="+minYear+";maxYear="+maxYear+";time="+time;
	}


    @Transient
	public String getTriview() {
		return "calendar";
	}

		
	public Integer getMaxYear() {
		return maxYear;
	}



	public void setMaxYear(Integer maxYear) {
		this.maxYear = maxYear;
	}



	public Integer getMinYear() {
		return minYear;
	}



	public void setMinYear(Integer minYear) {
		this.minYear = minYear;
	}


    public boolean isTime()
    {
        return time;
    }


    public void setTime(boolean time)
    {
        this.time = time;
    }

	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {
		CustomDateEditor propertyEditor = new CustomDateEditor(
				new SimpleDateFormat(time?"dd-MM-yyyy HH:mm":"dd-MM-yyyy"),true); 
		return propertyEditor;
	}

	@Override
	public AValue getInstanceValore() {
		return new DateValue();
	}
	
	@Override
	public Class<? extends AValue> getValoreClass() {
		return DateValue.class;
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		if (valore == null) {
			return null;
		} else {
			Date date = (Date) valore;
			Date max = null;
			Date min = null;
			if (minYear != null) {
				min = new GregorianCalendar(minYear,1,1).getTime();
			}
			if (maxYear != null) {
				max = new GregorianCalendar(maxYear+1,1,1).getTime();
			}
			
			if (minYear != null && date.compareTo(min) == -1) {
				return new ValidationMessage("date.mustbeafter",new Object[]{min});
			}
			if (maxYear != null && date.compareTo(max) == 1) {
				return new ValidationMessage("date.mustbebefor",new Object[]{max});
			}
			return null;
		}
		
	}
}