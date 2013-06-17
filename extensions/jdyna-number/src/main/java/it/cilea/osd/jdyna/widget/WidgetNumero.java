/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.widget;

import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.NumberValue;

import java.beans.PropertyEditor;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.propertyeditors.CustomNumberEditor;



/**
 * Widget to manage double value
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name="jdyna_widget_number")
@NamedQueries( {  
	@NamedQuery(name = "WidgetNumero.findAll", query = "from WidgetNumero order by id")
 } )
public class WidgetNumero extends AWidget {

	/** valore minimo del numero */
	private Double min;

	/** valore massimo */
	private Double max;
	
	/** numero di cifre decimali*/
	private int precisionDef;
	
	/** size of input box */
	@Column(name="widgetSize")
	private int size = 20;
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {
		String decimali = "";
		for (int i = 0; i < precisionDef; i++)
		{
			decimali += "0";
		}
		String pattern = "0" + (precisionDef > 0?"."+decimali:"");
		NumberFormat formatter = new DecimalFormat(pattern);
		
		CustomNumberEditor propertyEditor = new CustomNumberEditor(Double.class, formatter, true);
		return propertyEditor;
	}
	
	@Override
	public AValue getInstanceValore() {
		return new NumberValue();
	}

    @Override
	public Class<? extends AValue> getValoreClass() {
		return NumberValue.class;
	}
    
	@Override
	public String getTriview() {
		return "numero";
	}

//    public Object toObject(String valore)
//    {
//        return Double.parseDouble(valore);
//    }
//	
	@Deprecated
	public String getConfiguration() {
		return "min:"+min+";max:"+max+";decimali:"+precisionDef;
	}


	public int getPrecisionDef() {
		return precisionDef;
	}

	public void setPrecisionDef(int cifreDecimali) {
		this.precisionDef = cifreDecimali;
	}

    public Double getMax()
    {
        return max;
    }

    public void setMax(Double max)
    {
        this.max = max;
    }

    public Double getMin()
    {
        return min;
    }

    public void setMin(Double min)
    {
        this.min = min;
    }

	@Override
	public ValidationMessage valida(Object valore) {
		if (valore == null) {
			return null;
		}
		Double num = (Double) valore;
		if (min != null && num < min) {
			return new ValidationMessage("numero.mustbegt",new Object[]{min});
		}
		if (max != null && num > max) {
			return new ValidationMessage("numero.mustbelt",new Object[]{max});
		}
		return null;
	}
}
