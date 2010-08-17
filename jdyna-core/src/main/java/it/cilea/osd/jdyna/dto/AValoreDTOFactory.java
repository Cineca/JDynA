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
package it.cilea.osd.jdyna.dto;

import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.io.Serializable;
import java.util.LinkedList;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;


/**
 * Wrapper della classe AWidget che implementa l'interfaccia Factory di apache commons-collection
 * @author bollini
 *
 */
public class AValoreDTOFactory<P extends Property<TP>, TP extends PropertiesDefinition>
 implements Factory, Serializable {

	private AWidget widget;
	
	
	public AValoreDTOFactory(AWidget widget) {
		super();
		this.widget = widget;
	}

	public ValoreDTO create() {
		ValoreDTO valoreDTO = new ValoreDTO();
		if (widget instanceof WidgetCombo) {
			WidgetCombo<P,TP> combo = (WidgetCombo<P, TP>) widget;
			AnagraficaObjectDTO subDTO = new AnagraficaObjectDTO();
			for (TP subtp : combo.getSottoTipologie()) {
				subDTO.getAnagraficaProperties().put(
						subtp.getShortName(),
						LazyList.decorate(new LinkedList<ValoreDTO>(),
								new AValoreDTOFactory(subtp.getRendering())));
			}
			valoreDTO.setObject(subDTO);
			return valoreDTO;
		}
		else {
		//valoreDTO.setObject(null);
		return valoreDTO;
		}
	}
}
