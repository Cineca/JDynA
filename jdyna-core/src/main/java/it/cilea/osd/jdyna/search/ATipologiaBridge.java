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
package it.cilea.osd.jdyna.search;

import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;


/**
 * Bridge per indicizzare il nome delle tipologie nei campi di default
 */
public class ATipologiaBridge<TP extends PropertiesDefinition> 
	implements FieldBridge {

	private Log log = LogFactory.getLog(ATipologiaBridge.class);

	@Override
	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		
		set(name, value, document, luceneOptions.getStore(), luceneOptions.getIndex(), luceneOptions.getBoost());
		
	}

	
	private void set(String name, Object obj, Document doc,
			Store store, Index idx, Float boost) {
		log.debug("Chiamato l'ATipologiaBridge");	
		ATipologia<TP> tipologia = (ATipologia<TP>)obj;
		Field field = new Field("default", tipologia.getShortName(), store,idx);
		doc.add(field);
		if (log.isDebugEnabled()) {
			log
					.debug("Aggiunto un field al documento: "
							+ doc);
			log.debug("-- Field name: " + name);
			log.debug("-- Field valore: "
					+ tipologia.getShortName());
			log.debug("-- Idx: " + idx.toString());
		}
	}


		
}
