package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.widget.WidgetClassificazione;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
//anche nell rigenera indici mettere init
public  class CustomAnalyzer <T extends AnagraficaSupport> {
	private Class<T> model;
	private Map<Class<? extends AnagraficaSupport>,Class<? extends PropertiesDefinition>> mappaClasseTipologiaProprieta;
	private IPersistenceDynaService applicationService;
	private Map<Class<? extends AnagraficaSupport>,List<String>> mappaAppoggio=new HashMap<Class<? extends AnagraficaSupport>,List<String>>();

	
	public void init(){
	
	for(Class<? extends AnagraficaSupport> classe:mappaClasseTipologiaProprieta.keySet()){
		List<String> listaShortName=new LinkedList<String>();
		List<PropertiesDefinition> tipologieProprieta=(List<PropertiesDefinition>)applicationService.getList(mappaClasseTipologiaProprieta.get(classe));
			for(PropertiesDefinition propertiesDefinition:tipologieProprieta){
				if(propertiesDefinition.getRendering().getClass().isAssignableFrom(WidgetClassificazione.class)){
					listaShortName.add(propertiesDefinition.getShortName());
				}
			}
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
	
	public Analyzer getAnalyzer(Class<T> model){
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer());
		if(mappaAppoggio.get(model)!=null){
			for(String shortName:mappaAppoggio.get(model)){
				analyzer.addAnalyzer(shortName, new KeywordAnalyzer());
			}
		}
		return analyzer;
	}
}
