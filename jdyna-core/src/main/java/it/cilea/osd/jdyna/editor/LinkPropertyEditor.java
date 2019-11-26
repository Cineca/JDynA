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
package it.cilea.osd.jdyna.editor;

import it.cilea.osd.jdyna.value.EmbeddedLinkValue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LinkPropertyEditor extends AdvancedPropertyEditorSupport {
	
	/** The logger */
	private final static Log log = LogFactory.getLog(LinkPropertyEditor.class);
		
    public LinkPropertyEditor(String service)
    {
        setMode(service);
    }
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("LinkPropertyEditor - setAsText: "+text);
		// text: 'description|||link'
		if (StringUtils.isBlank(text) || StringUtils.equals(text, "|||"))
		    setValue(null);
		else
		{
		    String[] splitted = text.split("\\|\\|\\|");
		    if (splitted.length != 2 || StringUtils.isBlank(splitted[1]))
		    {
		        throw new IllegalArgumentException("the URL is required");
		    }
		    EmbeddedLinkValue link = new EmbeddedLinkValue();
		    link.setDescriptionLink(splitted.length > 0?splitted[0]:null);
		    link.setValueLink(splitted.length == 2?splitted[1]:null);
			setValue(link);
		}
	}

	@Override
	public String getAsText() {
		log.debug("send call to LinkConverter - getAsText");
		EmbeddedLinkValue valore = (EmbeddedLinkValue) getValue();
		if (valore == null) return "";
		log.debug("value = "+valore.getValueLink());
		log.debug("description = "+valore.getDescriptionLink());
	    if(MODE_CSV.equals(getMode())) {
	           return valore == null ? "": "[URL="+(valore.getValueLink() != null ? valore
	                   .getValueLink() : "")+"]"+valore.getDescriptionLink();
	    }
		return valore==null?"|||":
		    (valore.getDescriptionLink()!=null?valore.getDescriptionLink():"")
			+"|||"+
			(valore.getValueLink()!=null?valore.getValueLink():"");
	}
}
