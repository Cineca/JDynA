package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.service.IPersistenceClassificationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ConfigurazioneClassificazioneEditor extends
		java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceClassificationService applicationService;

	/** The logger */
	private final static Log log = LogFactory
			.getLog(ConfigurazioneClassificazioneEditor.class);

	@Override
	/**
	 * Prende in input una stringa del tipo: NOME ALBERO:CODICE CLASSIFICAZIONE
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		log
				.debug("chiamato ConfigurazioneClassificazioneEditor - setAsText text: "
						+ text);
		if (text == null || text.trim().equals("")) {
			setValue(null);
		} else {
			String nomeAlbero = text.split(":", 2)[0];
			String codiceClassificazione = text.split(":", 2)[1];
			AlberoClassificatorio albero = applicationService
					.getAlberoByName(nomeAlbero);
			Classificazione classificazione = applicationService
					.getClassificazioneByCodice(albero,
							codiceClassificazione);
			setValue(classificazione);
		}
	}

	@Override
	/**
	 * Genera una stringa della forma: NOME ALBERO:CODICE CLASSIFICAZIONE
	 */
	public String getAsText() {
		log.debug("chiamato ConfigurazioneClassificazioneEditor - getAsText");
		Classificazione valore = (Classificazione) getValue();
		return valore == null ? "" : valore.getNome();
	}

	public void setApplicationService(IPersistenceClassificationService applicationService) {
		this.applicationService = applicationService;
	}

}
