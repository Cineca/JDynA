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
package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

//anche nell rigenera indici mettere init
public class CustomAnalyzer<T extends AnagraficaSupport> {
	private Class<T> model;
	private Map<Class<? extends AnagraficaSupport>, Class<? extends PropertiesDefinition>> mappaClasseTipologiaProprieta;
	private IPersistenceDynaService applicationService;
	private Map<Class<? extends AnagraficaSupport>, List<String>> mappaAppoggio = new HashMap<Class<? extends AnagraficaSupport>, List<String>>();

	public void init() {

		for (Class<? extends AnagraficaSupport> classe : mappaClasseTipologiaProprieta
				.keySet()) {

			List<String> listaShortName = new LinkedList<String>();
			mappaAppoggio.put(classe, listaShortName);
		}

	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public Map<Class<? extends AnagraficaSupport>, Class<? extends PropertiesDefinition>> getMappaClasseTipologiaProprieta() {
		return mappaClasseTipologiaProprieta;
	}

	public void setMappaClasseTipologiaProprieta(
			Map<Class<? extends AnagraficaSupport>, Class<? extends PropertiesDefinition>> mappa) {
		this.mappaClasseTipologiaProprieta = mappa;
	}

	public Map<Class<? extends AnagraficaSupport>, List<String>> getMappaAppoggio() {
		return mappaAppoggio;
	}

	public void setMappaAppoggio(
			Map<Class<? extends AnagraficaSupport>, List<String>> mappa2) {
		this.mappaAppoggio = mappa2;
	}

	public Class<? extends AnagraficaSupport> getModel() {
		return model;
	}

	public Analyzer getAnalyzer(Class<T> model) {
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
				new StandardAnalyzer());
		if (mappaAppoggio.get(model) != null) {
			for (String shortName : mappaAppoggio.get(model)) {
				analyzer.addAnalyzer(shortName, new KeywordAnalyzer());
			}
		}
		return analyzer;
	}
}
