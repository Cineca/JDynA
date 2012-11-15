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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;

@Embeddable
/** Classe innestata in WidgetTesto per la dimensione programmata della textbox o textarea */
public class Size implements Serializable{
	
    public static final String EM_MEASURE = "em";
    public static final String PERCENT_MEASURE = "%";
    public static final String PIXEL_MEASURE = "px";
    public static final String POINT_MEASURE = "pt";
    
	private int row;
	private int col;
	private String measurementUnitRow;
	private String measurementUnitCol;
	
	public Size() {
		this.row = 1;
		this.col = 30;
		this.measurementUnitRow = EM_MEASURE;
		this.measurementUnitCol = EM_MEASURE;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}

    public String getMeasurementUnitRow()
    {
        return measurementUnitRow;
    }

    public void setMeasurementUnitRow(String measurementUnitRow)
    {
        this.measurementUnitRow = measurementUnitRow;
    }

    public String getMeasurementUnitCol()
    {
        return measurementUnitCol;
    }

    public void setMeasurementUnitCol(String measurementUnitCol)
    {
        this.measurementUnitCol = measurementUnitCol;
    }	
	
    public static List<String> getMeasurementUnits() {
        List<String> results = new ArrayList<String>();
        results.add(EM_MEASURE);
        results.add(PERCENT_MEASURE);
        results.add(POINT_MEASURE);
        results.add(PIXEL_MEASURE);
        return results;
    }
}
