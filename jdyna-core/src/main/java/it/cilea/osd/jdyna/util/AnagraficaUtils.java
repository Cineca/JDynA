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
package it.cilea.osd.jdyna.util;

import it.cilea.osd.jdyna.dto.AValoreDTOFactory;
import it.cilea.osd.jdyna.dto.AnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.value.MultiValue;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.beans.PropertyEditor;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.LazyList;

/**
 * Classe di supporto per la gestione di oggetti dotati di anagrafica
 * 
 * @author bollini
 * 
 * @param
 * <P>
 * la classe delle proprieta' presenti nell'anagrafica dell'oggetto
 * @param <TP>
 *            la classe delle tipologie di proprieta' presenti nell'anagrafica
 *            dell'oggetto
 * @param <A>
 *            la classe delle area applicabili all'anagrafica dell'oggetto
 */
public class AnagraficaUtils {

	private IPersistenceDynaService applicationService;

	private Map<Class<? extends AValue>, PropertyEditor> valorePropertyEditors;

	public AnagraficaUtils(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public <P extends Property<TP>, TP extends PropertiesDefinition> 
	void toXML(PrintWriter writer,
			AnagraficaSupport<P, TP> anagraficaObject) {
		List<P> anagrafica = anagraficaObject.getAnagrafica();
		if (anagrafica != null) {
			writer.print("          <property name=\"anagrafica\">\n");
			writer.print("              <list>\n");
			toXML(writer, anagraficaObject.getAnagrafica(), anagraficaObject
					.getClassProperty());
			writer.print("              </list>\n");
			writer.print("          </property>\n");
		}
	}

	private <P extends Property<TP>, TP extends PropertiesDefinition>
	void toXML(PrintWriter writer, List<P> proprietaList,
			Class<P> proprietaClass) {
		for (P proprieta : proprietaList) {
			writer.print("                 <bean id=\""
					+ proprietaClass.getSimpleName() + proprieta.getId()
					+ "\" class=\"" + proprietaClass.getCanonicalName()
					+ "\">\n");
			writer
					.print("                     <property name=\"tipologia\" value=\""
							+ proprieta.getTypo().getShortName()
							+ "\" />\n");
			writer
					.print("                     <property name=\"posizione\" value=\""
							+ proprieta.getPosition() + "\" />\n");
			
			Class valoreClass = proprieta.getTypo().getRendering().getValoreClass();
			writer
			.print("                     <property name=\"valore\">\n");
	writer.print("                     <bean class=\""
			+ valoreClass.getCanonicalName() + "\">\n");
	writer
			.print("                        <property name=\"real\">\n");

			if (proprieta.getTypo().getRendering() instanceof WidgetCombo) {
				writer.print("                       <list>\n");
				toXML(writer, (List<P>) proprieta.getValue().getObject(),
						proprietaClass);
				writer.print("                       </list>\n");
			}
			 else {
				PropertyEditor propertyEditor = valorePropertyEditors
						.get(proprieta.getTypo().getRendering()
								.getValoreClass());

				propertyEditor.setValue(proprieta.getValue());
				writer
						.print("                           <value><![CDATA["
								+ propertyEditor.getAsText()
								+ "]]></value>\n");
			}
			
			writer.print("                         </property>\n");
			writer.print("                     </bean>\n");
			writer.print("                     </property>\n");


			writer.print("                 </bean>\n");
		}
	}

	public void setValorePropertyEditors(
			Map<Class<? extends AValue>, PropertyEditor> valorePropertyEditors) {
		this.valorePropertyEditors = valorePropertyEditors;
	}
	
	public static <P extends Property<TP>, TP extends PropertiesDefinition> void fillDTO(IAnagraficaObjectDTO dto, AnagraficaSupport<P, TP> anagraficaSupport, List<TP> tipologie) {
		for (TP tipProprieta : tipologie) {
			dto.getAnagraficaProperties().put(
					tipProprieta.getShortName(),
					LazyList.decorate(new LinkedList<ValoreDTO>(),
							new AValoreDTOFactory(tipProprieta.getRendering())));
//					LazyList.decorate(new LinkedList<Object>(),
//							new AValoreDTOFactory(tipProprieta.getRendering())));
			if (tipProprieta.getRendering() instanceof WidgetCombo){
				//devo predisporre almeno la prima riga
				AnagraficaObjectDTO subDTO = new AnagraficaObjectDTO();
				WidgetCombo<P,TP> combo = (WidgetCombo<P, TP>) tipProprieta.getRendering();
				for (TP subtp : combo.getSottoTipologie()) {
					subDTO.getAnagraficaProperties().put(
							subtp.getShortName(),
							LazyList.decorate(new LinkedList<ValoreDTO>(),
									new AValoreDTOFactory(subtp.getRendering())));
				}
				dto.getAnagraficaProperties().get(tipProprieta.getShortName()).add(new ValoreDTO(subDTO));
			}
		}

		if (anagraficaSupport != null) {
			for (TP tipProprieta : tipologie) {
					List<ValoreDTO> avalori = new LinkedList<ValoreDTO>();
		//			List<Object> avalori = new LinkedList<Object>();
					for (P proprieta : anagraficaSupport
							.getProprietaDellaTipologia(tipProprieta)) {
						if (tipProprieta.getRendering() instanceof WidgetCombo){
							AnagraficaObjectDTO subDTO = new AnagraficaObjectDTO();
							WidgetCombo<P,TP> combo = (WidgetCombo<P, TP>) tipProprieta.getRendering();
							for (TP subtp : combo.getSottoTipologie()) {
								subDTO.getAnagraficaProperties().put(
										subtp.getShortName(),
										LazyList.decorate(new LinkedList<ValoreDTO>(),
												new AValoreDTOFactory(subtp.getRendering())));
								List<ValoreDTO> subavalori = new LinkedList<ValoreDTO>();
								for (P subprop : anagraficaSupport
									.getProprietaDellaTipologiaInValoreMulti(
											(MultiValue<P, TP>) proprieta
													.getValue(),
											subtp)) {
									
									subavalori.add(new ValoreDTO(subprop.getValue().getObject(),(subprop.getVisibility()==0)?false:true));
									
								}
								
								subDTO.getAnagraficaProperties().get(
										subtp.getShortName()).addAll(subavalori);								
							}
							avalori.add(new ValoreDTO(subDTO));
						}
						else {
							avalori.add(new ValoreDTO(proprieta.getValue().getObject(), (proprieta.getVisibility()==0)?false:true));

									
						}
					}
					if (avalori.size() != 0) {
						dto.getAnagraficaProperties().get(
								tipProprieta.getShortName()).clear();
						dto.getAnagraficaProperties().get(
								tipProprieta.getShortName()).addAll(avalori);
					}
			}
		}
	}
	
	public static <P extends Property<TP>, TP extends PropertiesDefinition> void reverseDTO(IAnagraficaObjectDTO dto, AnagraficaSupport<P, TP> anagraficaSupport, List<TP> tipologie) {
		for (TP tipProprieta : tipologie) {
			List<ValoreDTO> avaloriDTO = dto
					.getAnagraficaProperties().get(tipProprieta.getShortName());
//			List<Object> avaloriDTO = anagraficaObjectDTO
//					.getAnagraficaProperties().get(tipProprieta.getShortName());
			List<P> proprieta = anagraficaSupport
					.getProprietaDellaTipologia(tipProprieta);
			final int avaloriDTOsize;
			if (avaloriDTO == null) {
				avaloriDTOsize = 0;
			}
			else {
				if (tipProprieta.getRendering() instanceof WidgetCombo) {
					WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tipProprieta.getRendering();
					int tmp = 0;
					for (ValoreDTO aval : avaloriDTO) {
						AnagraficaObjectDTO subDTO = aval != null?(AnagraficaObjectDTO) aval.getObject():null;
						if (subDTO != null && !AnagraficaUtils.<P, TP>checkIsAllNull(subDTO, combo.getSottoTipologie())) {
							tmp++;					
						}
					}
					avaloriDTOsize = tmp;
				} else {
					int tmp = 0;
					for (ValoreDTO aval : avaloriDTO) {
						if (aval != null && aval.getObject() != null) tmp++;					
					}
					avaloriDTOsize = tmp;
				}
			}
			int propDaCreare = (avaloriDTOsize - proprieta.size() > 0) ? avaloriDTOsize
					- proprieta.size()
					: 0;
			int propDaEliminare = (proprieta.size() - avaloriDTOsize > 0) ? proprieta
					.size()
					- avaloriDTOsize
					: 0;
			for (int i = 0; i < propDaCreare; i++) {
				anagraficaSupport.createProprieta(tipProprieta);
			}
			for (int i = 0; i < propDaEliminare; i++) {
				// devo eliminare sempre l'ultima proprieta' perche' la lista e'
				// una referenza
				// alla lista mantenuta dalla cache a4v che viene alterata dal
				// removeProprieta
				anagraficaSupport.removeProprieta(proprieta.get(proprieta.size() - 1));
			}
			
			if (avaloriDTOsize > 0){
				if (tipProprieta.getRendering() instanceof WidgetCombo){
					WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tipProprieta.getRendering();
					int i = 0;
					for (ValoreDTO valoreDTO : avaloriDTO) {
						AnagraficaObjectDTO subDTO = valoreDTO != null?(AnagraficaObjectDTO) valoreDTO.getObject():null;
						if (subDTO != null && !AnagraficaUtils.<P, TP>checkIsAllNull(subDTO, combo.getSottoTipologie())) {
							MultiValue<P, TP> multi = (MultiValue) proprieta.get(i).getValue();
//							int oldSubSize = multi.getObject().size();
//							for (int k = 0; k < oldSubSize; k++) {
//								anagraficaSupport.removeProprieta(multi.getObject().get(0));
//							}
//							for (TP subtp : ((WidgetCombo<P, TP>) tipProprieta
//									.getRendering()).getSottoTipologie()) {						
//								List<ValoreDTO> subavaloriDTO = (List<ValoreDTO>) subDTO
//										.getAnagraficaProperties().get(
//												subtp.getShortName());
//								
//								
//								for (ValoreDTO subValoreDTO : subavaloriDTO) {
//									if (subValoreDTO != null && subValoreDTO.getObject() != null) {
//										P property = anagraficaSupport.createProprieta(proprieta.get(i), subtp);
//										property.getValue().setOggetto(
//														subValoreDTO.getObject());										
//										property.setVisibility(subValoreDTO.getVisibility()==false?0:1);
//									}
//								}								
//								
//							}
							reverseDTO(subDTO, multi, ((WidgetCombo<P, TP>) tipProprieta.getRendering()).getSottoTipologie());
							
							i++;					//3.90 M33-3
						}
					}
				}
				else {
					int i = 0;
					for (ValoreDTO valoreDTO : avaloriDTO) {
						if (valoreDTO != null && valoreDTO.getObject() != null) {
							proprieta.get(i).getValue().setOggetto(valoreDTO.getObject());
							proprieta.get(i).setVisibility(valoreDTO.getVisibility()==false?0:1);
							//				proprieta.get(i).getValore().setOggetto(avaloriDTO.get(i));
							i++;
						}
					}
				}
			}
		}
	}

	public static <P extends Property<TP>, TP extends PropertiesDefinition> boolean checkIsAllNull(AnagraficaObjectDTO valore,
			List<TP> sottoTipologie) {
		boolean isAllNull = true;
		for (TP tp : sottoTipologie) {
			for (Object subvalore : valore.getAnagraficaProperties().get(
					tp.getShortName())) {
				if (tp.getRendering() instanceof WidgetCombo) {
					WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tp
							.getRendering();
					if (!AnagraficaUtils.<P, TP>checkIsAllNull((AnagraficaObjectDTO) subvalore,
							sottoTipologie)) {
						return false;
					}
				} else {
					if (subvalore != null) {
						return false;
					}
				}
			}
		}
		return isAllNull;
	}

//	/**
//	 * Cerca, nella lista passata come secondo argomento, la tipologia di
//	 * proprieta' che referenzia la combo in cui e' contenuta la TP figlia.
//	 * 
//	 * @param child
//	 *            la tipologia di proprieta' figlia (i.e. isTopLevel() == false)
//	 * @param allTP
//	 *            la lista di tutte le tipologie di proprieta' in cui cercare
//	 * @return la tipologia di proprieta' padre o null se non e' presente nella
//	 *         lista
//	 * @throws IllegalArgumentException
//	 *             se la tipologia di proprieta' indicata come figlia e' topLevel
//	 */
//	public <TP extends PropertiesDefinition> TP findTipologiaProprietaParent(TP child, List<TP> allTP){
//		return null;
//	}
	
	/**
	 * Importa sull'oggetto anagraficaObject passato come parametro i dati contenuti nell'importBean.
	 * 
	 * @param anagraficaObject - l'oggetto su cui importare i dati e metadati contenuti nel importBean passato come parametro
	 * @param importBean {@see ImportPropertyAnagraficaUtil}
	 * @return
	 */
	public <P extends Property<TP>, TP extends PropertiesDefinition,AV extends AValue> void importProprieta(AnagraficaSupport<P, TP> anagraficaObject, ImportPropertyAnagraficaUtil importBean) {
		
		String shortName = importBean.getShortname();
		Object oggetto = importBean.getValore();
		
		//recupero da db la tipologia di proprieta
		TP tipologiaDaImportare = (TP)applicationService.findPropertiesDefinitionByShortName(anagraficaObject.getClassPropertiesDefinition(), shortName);
		
		if (tipologiaDaImportare == null){
			throw new IllegalArgumentException("Lo shortname indicato: "
					+ shortName
					+ " non corrisponde a nessuna TP della classe "
					+ anagraficaObject.getClassPropertiesDefinition()
							.getCanonicalName());
		}
		//recupero dal widget il property editor per l'import
		PropertyEditor pe = tipologiaDaImportare.getRendering()
					.getImportPropertyEditor(applicationService);
		
		P proprieta = null;
		//se e' una combo la tipologia devo creare la proprieta' per il valore multi e cancellare le sotto proprieta create di default per ogni sotto tipologia
		//in modo tale che quando capitero' nel caso della combo creo le sole sotto proprieta' descritte dall'xml
		if(tipologiaDaImportare.getRendering() instanceof WidgetCombo) {
			 proprieta = anagraficaObject.createProprieta(tipologiaDaImportare);
			 //((MultiValue<P, TP>)proprieta.getValore()).getOggetto().clear();
//			 for(P p : ((MultiValue<P, TP>)proprieta.getValore()).getOggetto()) {
//				 anagraficaObject.removeProprieta(p);
//			 }
			 ((MultiValue<P, TP>)proprieta.getValue()).setOggetto(new LinkedList<P>());
		}
		
		if (oggetto instanceof String) {
				pe.setAsText((String) oggetto);
				proprieta = anagraficaObject
						.createProprieta(tipologiaDaImportare);
				AV valore = (AV) proprieta.getValue();
				valore.setOggetto(pe.getValue());
				proprieta.setValue(valore);
			}
		
		if (oggetto instanceof List) {
				List lista = (List) oggetto;
				
				for (int w = 0; w < lista.size(); w++) {
					Object elementList = lista.get(w);		
					
					if (elementList instanceof String) {	
						proprieta = anagraficaObject
						.createProprieta(tipologiaDaImportare);
						pe.setAsText((String) elementList);
						AV valore = (AV) proprieta.getValue();
						valore.setOggetto(pe.getValue());
						//proprieta.setValore(valore);
					} else {
						// e' una combo...							
						importSottoProprieta(proprieta, anagraficaObject, (ImportPropertyAnagraficaUtil)elementList);				}
				}


		}			
			
		
	}
	
	
	/**
	 *	Metodo interno che importa i dati delle combo.
	 *
	 * @param proprietaParent - la proprieta parent della combo
	 * @param anagraficaObject
	 * @param importBean
	 */
	private <P extends Property<TP>, TP extends PropertiesDefinition,AV extends AValue> void importSottoProprieta(P proprietaParent, AnagraficaSupport<P, TP> anagraficaObject, ImportPropertyAnagraficaUtil importBean) {
	
		String shortName = importBean.getShortname();
		Object oggetto = importBean.getValore();
		
		TP tipologiaDaImportareInCombo = (TP)applicationService.findPropertiesDefinitionByShortName(anagraficaObject.getClassPropertiesDefinition(), shortName);
		PropertyEditor pe = tipologiaDaImportareInCombo.getRendering()
		.getImportPropertyEditor(applicationService);
		
		P proprieta = null;
		if(tipologiaDaImportareInCombo.getRendering() instanceof WidgetCombo) {
			 proprieta = anagraficaObject.createProprieta(proprietaParent, tipologiaDaImportareInCombo);
			 ((MultiValue<P, TP>)proprieta.getValue()).getObject().clear();
		}
		
		// caso base
		if (oggetto instanceof String) {
			pe.setAsText((String) oggetto);
			proprieta =anagraficaObject.createProprieta(proprietaParent, tipologiaDaImportareInCombo);		
			AV valore = (AV) proprieta.getValue();
			valore.setOggetto(pe.getValue());
			//proprieta.setValore(valore);
		}
		if (oggetto instanceof List) {
			List lista = (List) oggetto;
			
			for (int w = 0; w < lista.size(); w++) {
				Object elementList = lista.get(w);
				//caso base
				if (elementList instanceof String) {
					proprieta = anagraficaObject.createProprieta(proprietaParent, tipologiaDaImportareInCombo);
					pe.setAsText((String) elementList);
					AV valore = (AV) proprieta.getValue();
					valore.setOggetto(pe.getValue());
					//proprieta.setValore(valore);
				//caso ricorsivo
				} else {
					// e' una combo...
					importSottoProprieta(proprieta, anagraficaObject, (ImportPropertyAnagraficaUtil)elementList);
				}
			}

		}
	}			
		
}
