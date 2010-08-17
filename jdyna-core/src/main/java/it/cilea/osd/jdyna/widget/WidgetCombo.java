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


import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.MultiValue;

import java.beans.PropertyEditor;
import java.util.List;

/**
 * Oggetto che rappresenta un oggetto combinato, al suo interno mantiene un lista di tipologie 
 * di proprieta. Con il metodo getInstanceValore crea il valore adatto per mantenere 
 * le informazioni dei sotto elementi.
 * 
 * @see MultiValue
 * @author pascarelli
 *
 */
public abstract class WidgetCombo <P extends Property<TP>, TP extends PropertiesDefinition>
	extends AWidget {

	public abstract List<TP> getSottoTipologie();
	
	//FIXME verificare come viene utilizzato il propertyEditor!!!
	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {
		return null; 
	}
	
	@Override
	public String getTriview() {
		return "combo";
	}

	public void setSottoTipologie(List<TP> sottoTipologie) {
		for(TP tp : sottoTipologie) {
			tp.setTopLevel(false);
		}
		getSottoTipologie().clear();
		getSottoTipologie().addAll(sottoTipologie);
	}
	
	/**
	 * Restituisce true se tutte le sottotipologie possono essere mostrate in linea
	 * @return true se tutte le sottotipologie possono essere mostrate in linea
	 * @see PropertiesDefinition#isNewline()
	 */
	public boolean isInline() {
		List<TP> sottoTP = getSottoTipologie();
		if (sottoTP != null) {
			for (TP tp : sottoTP) {
				if (tp.isNewline()) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString(Object valore) {
		if (valore != null){
			List<P> props = (List<P>) valore;
			StringBuffer result = new StringBuffer();
			for (P prop : props) {
				result.append(prop.getTypo().getShortName()+": "+prop.getTypo().getRendering().toString(prop.getObject())+"|");
			}
			return result.toString();
		}
		return super.toString(valore);
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
//		ValidationMessage vm = null;
//		if(valore!=null) {
//			List<P> componenti = (List<P>)valore;
//			for(P p : componenti) {
//				vm = p.getTipologia().getRendering().valida(p);
//				//restituisco il primo errore trovato
//				if(vm!=null) {
//					return new ValidationMessage("combo.error",new Object[]{p.getTipologia().getShortName(),vm.getI18nKey()});
//				}
//			}
//		}
		return null;
	}
	
	@Override
	public boolean isNull(Object valore) {
		if (valore == null) {
			return true;
		}
		List<P> subprops = (List<P>) valore;
		for (P sp : subprops) {
			if (sp != null) {
				if (!sp.hasNullValue()) {
					return false;
				}
			}
		}
		return true;
	}

}