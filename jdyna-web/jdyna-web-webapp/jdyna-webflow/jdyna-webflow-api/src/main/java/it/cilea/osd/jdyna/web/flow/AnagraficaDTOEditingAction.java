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
package it.cilea.osd.jdyna.web.flow;

import java.util.Map;


import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.Errors;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class AnagraficaDTOEditingAction	extends FormAction {	
	
	private Map<String,RealAnagraficaDTOEditingAction> realActions;

	public void setRealActions(
			Map<String, RealAnagraficaDTOEditingAction> realActions) {
		this.realActions = realActions;
	}

	public Event referenceData(RequestContext context) throws Exception {
		return getRealAction(context).referenceData(context);
	}
	
	protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry registry) {
		getRealAction(context).registerPropertyEditors(context, registry);
	}
	
	protected Object createFormObject(RequestContext context) throws Exception {
		return getRealAction(context).createFormObject(context);		
	}
	
	@Override
	protected void doValidate(RequestContext context, Object formObject,
			Errors errors) throws Exception {
		getRealAction(context).doValidate(context, formObject, errors);
	}
	
	@Override
	public Event bindAndValidate(RequestContext context) throws Exception {
		return getRealAction(context).bindAndValidate(context);
	}
	

	public Event getBaseDetailURL(RequestContext context) {
		context.getFlowScope().put("baseURL",
				getRealAction(context).getBaseDetailURL());
		return success();
	}
	public Event persisti(RequestContext context) throws Exception {
		return getRealAction(context).persisti(context);
	}
	
	private RealAnagraficaDTOEditingAction getRealAction(RequestContext context){
		String modelName = context.getFlowScope().getRequiredString("model");
		RealAnagraficaDTOEditingAction realAction = realActions.get(modelName);
		if (realAction == null){
			throw new IllegalStateException("Nessuna action configurata per il model: "+modelName);
		}
		return realAction;
	}
}
