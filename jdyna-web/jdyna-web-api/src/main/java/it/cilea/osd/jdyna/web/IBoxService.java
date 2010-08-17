package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import java.util.List;

public interface IBoxService extends ITabService {

	
	public List<IContainable> getContainableOnCreation();
	
    /**
     * Restituisce le tipologie di proprietà non mascherate nell'area individuata da areaId correttamente
     * ordinate. Se l'areaId non è presente o non è valida viene utilizzato l'id della prima area disponibile.
     * Per conoscere le tipologie di proprietà applicabili ad un determinato oggetto in una certa area <b>utilizzare</b>
     * il metodo <code>findTipologieProprietaAssegnabiliAndArea</code>
     * 
     * @see #findTipologieProprietaAssegnabiliAndArea(AnagraficaSupport, Integer)
     * @see PropertiesDefinition#compareTo(PropertiesDefinition)
     * @param <A> la classe di Area 
     * @param <TP> la classe delle tipologie di proprietà
     * @param areaClass la classe di Area
     * @param areaId l'id dell'area da considerare
     * @return lista ordinata di tipologie di proprietà utilizzabili nell'area
     */
	public <H extends IPropertyHolder> List<IContainable> findContainableInBox(
			Class<H> boxClass, Integer boxId);
	
	
	/** Restituisce tutte le tipologie proprieta in una certa area che hanno rendering WidgetFormula 
	 * 
	 * @param areaClass la classe dell'area da cui riprendere il dao
	 * @param areaId l'id dell'area in cui riprendere le tipologia di proprietà
	 * */
	public List<IContainable> findContainableInBoxWithRenderingFormula(
			Class<IPropertyHolder> boxClass, Integer boxId);
	
	/** 
	 * 	Elimina l'associazione tra le aree e la tipologia proprieta se mascherata in qualche area; 
	 *  rimuove dalla lista di tipologia proprieta mascherate nell'area la tipologia (se mascherata)
	 *  e salva l'area per far scattare.
	 *  
	 * @param <TP>
	 * @param <A>
	 * @param tip
	 */
	public <H extends IPropertyHolder> void deleteContainableInBox(
			IContainable tip);
	
	

	public <H extends IPropertyHolder> Class<H> getPropertyHolderClass();
}
