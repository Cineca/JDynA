package it.cilea.osd.jdyna.service;

import it.cilea.osd.common.model.Selectable;
import it.cilea.osd.jdyna.model.Classificazione;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;

public interface IAjaxDynaService {
	
	public Classificazione getClassificazioneByCodice(String codice,String nomeAlbero);
	
	public String getInfoOnDeleteTipologiaProprieta(String idTipologia);
		
	public List<Classificazione> getClassificazioni(String nomeAlbero,String codice);

	public <T extends Selectable> List<String> searchCollisioni(String field, String classCollisione,String inputValue,String rootPath) throws IOException, ParseException, ClassNotFoundException;

}
