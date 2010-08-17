package it.cilea.osd.common.core;

public interface HasTimeStampInfo {
	/**
	 * Restituisce l'oggetto contenente le informazioni sulla creazione/modifica
	 * dell'oggetto. Nel caso non siano presenti informazioni <b>deve</b>
	 * restituire una nuova istanza di TimeStampInfo.<br>
	 * <b>Il parametro di ritorno non è mai nullo</b>
	 * 
	 * @return le informazioni sulla creazione/modifica dell'oggetto: timestamp
	 *         e operatore
	 */
	public ITimeStampInfo getTimeStampInfo();
}
