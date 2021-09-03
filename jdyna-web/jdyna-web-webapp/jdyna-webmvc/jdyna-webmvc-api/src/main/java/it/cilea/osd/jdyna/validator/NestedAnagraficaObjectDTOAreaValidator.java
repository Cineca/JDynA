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
package it.cilea.osd.jdyna.validator;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

import it.cilea.osd.jdyna.dto.AnagraficaObjectWithTypeDTO;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;

public class NestedAnagraficaObjectDTOAreaValidator <P extends Property<TP>, TP extends PropertiesDefinition, I extends IPropertyHolder<Containable>, T extends Tab<I>, EO extends AnagraficaObject<P, TP>, TN extends AType<PropertiesDefinition>>
		extends AnagraficaObjectDTOValidator<P, TP, I, EO> {

	private static Log log = LogFactory.getLog(NestedAnagraficaObjectDTOAreaValidator.class);

	protected Class<TN> clazzTypeNestedObject;

	public void validate(Object commandObject, Errors errors) {
		AnagraficaObjectWithTypeDTO dto = (AnagraficaObjectWithTypeDTO) commandObject;
		List tp = applicationService.get(clazzTypeNestedObject, dto.getTipologiaId())
				.getMask();

		validate(dto, errors, tp, "");
	}

	public void setClazzTypeNestedObject(Class<TN> clazzTypeNestedObject) {
		this.clazzTypeNestedObject = clazzTypeNestedObject;
	}

}
