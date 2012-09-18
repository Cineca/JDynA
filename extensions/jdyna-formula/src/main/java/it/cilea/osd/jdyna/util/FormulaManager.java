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

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.event.ISubscriber;
import it.cilea.osd.jdyna.event.JPAEvent;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceFormulaService;
import it.cilea.osd.jdyna.widget.WidgetFormula;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * Manager delle formule, sul save or update di un oggetto verifica se in
 * qualche formula l'oggetto e' implicato, verifica se e' possibile calcolarlo e
 * fa il parser della formula con OGNL,trasforma l'espressione del WidgetFormula
 * seguendo delle regole.
 * TODO spostare la logica di calcolo delle formule su un altra classe poiche' condivisa da molti servizi
 *     
 * 
 * @author pascarelli
 * 
 */
@Deprecated
public class FormulaManager implements ISubscriber<JPAEvent> {
	/** Logger for this class and subclasses */
    protected static final Log log = LogFactory.getLog(FormulaManager.class);
    
	private IPersistenceFormulaService applicationService;
	
//	private UtilService utilService;

//	public void setUtilService(UtilService utilService) {
//		this.utilService = utilService;
//	}

	public FormulaManager(IPersistenceFormulaService applicationService) {
		this.applicationService = applicationService;
		applicationService.addSubscriber(this, JPAEvent.class);
	}
	
	/**
	 * Metodo che ascolta i messaggi che arrivano dal saveOrUpdate per ricalcolare le formule riferite
	 * alla variabile che si e' editata.
	 * 
	 * @param variabileId l'id dell'oggetto da cercare nella lista di oggetti Variabile di ProprietaDaRicalcolare 
	 * @param classe  la classe dell'oggetto da cercare nella lista di oggetti Variabile di ProprietaDaRicalcolare
	 * @author pascarelli
	 * @throws OgnlException
	 * @throws ClassNotFoundException 
	 */
	public <TP extends PropertiesDefinition, P extends Property<TP>> void ricalcoloFormula(
			JPAEvent evento) throws OgnlException {
		//prendo gli oggetti che hanno proprieta da ricalcolare 
		Set<AnagraficaSupport<P,TP>> oggettoConFormule = applicationService
				.findOggettoDaRicalcolareByEvento(evento);
		
		//per ogni oggetto devo ricalcolare le formule 
		for (AnagraficaSupport<P,TP> oggetto : oggettoConFormule) {
			ricalcolaFormule(oggetto);
			registraFormule(oggetto);
		}			
	}
		

	
	/** Trasforma l'expression scritta dall'utente (salvata su db) in una regola comprensibile a OGNL; 
	 *  REGOLE:
	 *  -   l'utente inserira' una espressione del tipo '${costo}[1]', questa espressione verra' trasformata in un formato
	 *  	leggibile a OGNL quindi diventera' 'anagrafica4view['costo'][1].object';
	 *  -   la stringa '${costo}[n]' diventera' 'anagrafica4view['costo'][n].object';
	 *  -   ATT!!! se ho una combo bisogna valutarla in modo
	 *  	tale che se la stringa immessa dall'utente sia '${combo[${idx}].costo[1]*IVA}' bisogna 
	 *  	valutare per prima la combo con il suo idx passato come parametro per 
	 *  	poi prendere il giusto valore dell'elemento costo;
	 *  -   se non voglio utilizzare idx lo metto a null (ad esempio quando voglio solamente parserizzare una stringa)
	 *  - 	Per la ripetibilita' nella formula bisogna inserire un resultIdx in modo tale da sostituirlo con quello passato come parametro
	 *      Es. se l'utente chiama questo metodo con un'espressione del tipo opera.${test}[${resultIdx}] con parametro resultIdx uguale a 4
	 *      allora il metodo cerchera' la 4 proprieta ripetibile nell'anagrafica 4 view  
	 *  -   Il metodo deve essere chiamato sempre con parametro resultIdx uguale a 0 in modo tale da verificare se 
	 *      la formula contiene o meno la stringa resultIdx nell'espressione e quindi poter valutare o meno la ripetibilita'
	 *  
	 * @param idx indice di posizione della proprieta 
	 * @param resultIdx indice di posizione della proprieta ripetibile (se null cerca di restituire la lista di proprieta dell'anagrafica)
	 * @return l'espressione da valutare
	 */ 
	public static String getOgnlExpression(String formula,Integer idx,Integer resultIdx) {
		log.debug("Elaborazione della formula: "+formula+" con idx: "+idx +" in posizione: "+resultIdx);
		String ognlExp = formula;
		if (idx != null) {			
			ognlExp = ognlExp.replaceAll(
					"\\$\\{([A-z_]*)\\}\\[\\$\\{idx\\}\\]\\.\\$\\{([A-z_]*)\\}",
					"anagrafica4view['$1'][" + idx + "].valore.\\$\\{$2\\}");
			ognlExp = ognlExp.replaceAll(
					"\\$\\{([A-z_]*)\\}\\[\\$\\{idx\\}\\]",
					"anagrafica4view['$1'][" + idx + "].object");		
		}
		if (resultIdx != null) {
			if(ognlExp.contains("resultIdx")) {
					ognlExp = ognlExp.replaceAll("\\$\\{([A-z_]*)\\}\\[\\$\\{resultIdx\\}\\]",
							"anagrafica4view['$1'][" + resultIdx + "].object");
			}
			else {
					ognlExp = ognlExp.replaceAll("\\$\\{([A-z_]*)\\}\\[(\\d*)\\]", "anagrafica4view['$1'][$2].object");
			}		 
		}
		ognlExp = ognlExp.replaceAll("\\$\\{([A-z_]*)\\}",
						"anagrafica4view['$1']");
		log.debug("Restituita l'espressione OGNL: "+ognlExp);
		return ognlExp;		
	}
	
	/** Valuta la regola di calcolo con il getValue di OGNL;
	 * La stringa passata come parametro viene prima trasformata 
	 * dal metodo <em>getOgnlExpression</em> 
	 * 
	 * @see getOgnlExpression
	 * @param expression e' la formula
	 * @param rootObject e' l'oggetto su cui valutare la formula
	 * @param idx indice di posizione della proprieta 
	 * @author pascarelli
	 * @throws OgnlException 
	 * */
	public static boolean valutaRegolaDiCalcolo(String expression, Object rootObject, Integer idx) throws OgnlException {		
		return (Boolean) Ognl.getValue(getOgnlExpression(expression,idx,0), rootObject);		
	}
	
	/** Calcola il valore dell'espressione con il getValue di OGNL;
	 * La stringa passata come parametro viene prima trasformata 
	 * dal metodo <em>getOgnlExpression</em> 
	 * 
	 * @see getOgnlExpression
	 * @param expression e' la formula
	 * @param rootObject e' l'oggetto su cui valutare la formula
	 * @author pascarelli
	 * @throws OgnlException */
	public static Object calcoloValore(String expression, Object rootObject,
			Integer idx, Integer resultIdx) {
		//OgnlRuntime.setNullHandler(Property.class, new ProprietaNullHandler());
		//OgnlRuntime.setNullHandler(Anagrafica4View.class, new Anagrafica4ViewNullHandler(applicationService));
		//OgnlRuntime.setNullHandler(Identifiable.class, new HibernateProxyNullHandler());
		String ognlExp = getOgnlExpression(expression, idx, resultIdx);
		// Object preParser = null;
		try {
			Object result = null;
			// verifica dell'espressione
			// preParser = Ognl.parseExpression(ognlExp);
			// //pre valutazione dell'espressione ... prove tecniche per la
			// trasmissione...in modo tale da gestire subito l'eventuale
			// espressione malformata o qualunque altro tipo di eccezione
			// Ognl.getValue(preParser,rootObject);
			// try {
			if (rootObject != null) {
				result = Ognl.getValue(ognlExp, rootObject);
			}
			log.debug("Valore calcolato: " + result);
			//log.warn("Valore calcolato: " + result);
			return result;
			// }
			// catch (OgnlException e)
			// {
			// log.error("Errore nella valutazione dell'espressione
			// "+expression+" -> " +
			// ognlExp +" sull'oggetto root "+rootObject);
			// throw new RuntimeException(e);
			//			
			// }
			
		} catch (OgnlException e) {
			log.error("Errore nella valutazione dell'espressione " + expression
					+ " -> " + ognlExp + " sull'oggetto root " + rootObject);
			// log.error("Errore nel pre parsering dell'espressione "+ ognlExp +
			// "---> oggetto pre parser :"+preParser);
			throw new RuntimeException(e);
		}

		catch (Exception e) {
			// log.error("Errore nel pre parsering dell'espressione "+ ognlExp +
			// "---> oggetto pre parser :"+preParser);
			log.error("Errore nella valutazione dell'espressione " + expression
					+ " -> " + ognlExp + " sull'oggetto root " + rootObject);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Metodo di utilita' per la registrazione di formule appena create,
	 * itera sull'anagrafica verificando se l'oggetto e' di tipo WidgetFormula,
	 * una volta verificato questo registra la formula nel ProprietaDaRicalcolare.
	 * 
	 * @param oggetto di tipo AnagraficaSupport quindi contenente un anagrafica;
	 * @throws OgnlException;
	 * @author pascarelli;
	 */
	public <P extends Property<TP>, TP extends
	PropertiesDefinition> void registraFormule(AnagraficaSupport<P,TP> oggetto) {
		log.debug("Registro le formule relative all'oggetto: "+oggetto);
		Integer id = ((Identifiable)oggetto).getId();
		if(id!=null) {
			applicationService.deleteOggettoDaRicalcolareByOggetto(id,oggetto.getClass().getCanonicalName());
		}
		List<TP> tipologieConRenderingFormula = applicationService.
			getAllTipologieProprietaWithWidgetFormula(oggetto.getClassPropertiesDefinition());
		
		Set<ModelEventoString> variabili = new HashSet<ModelEventoString>();
		
		for(TP tip : tipologieConRenderingFormula) {
			if (!tip.isTopLevel()){	
				for(P p: oggetto.getProprietaDellaTipologia(tip)) {				
					log.debug("estraggo le variabili dalla proprieta': "+p);
					variabili.addAll(getEventiVariabili(p));
				}
			} else {
				variabili.addAll(
						getEventiVariabili(((WidgetFormula) tip
						.getRendering()).getVariabili(), oggetto, null));
			}
		}
		
		//creo un model event per l'oggetto su cui si sta lavorando se ha almeno una formula
		if (tipologieConRenderingFormula.size() > 0){
			ModelEventoString mes = new ModelEventoString(
					oggetto.getClass()
							.getCanonicalName(),
					((Identifiable)oggetto).getId());
			variabili.add(mes);
		}
		
		Set<OggettoDaRicalcolare> oggettiDaRicalcolare = new HashSet<OggettoDaRicalcolare>();
		
		for(ModelEventoString variabile : variabili) {
			JPAEvent event = new JPAEvent(variabile.getEvento(),variabile.getId(),JPAEvent.UPDATE);
			OggettoDaRicalcolare fe = 
				new OggettoDaRicalcolare(id,oggetto.getClass().getCanonicalName(),event);			
			oggettiDaRicalcolare.add(fe);
		}
		
		for(OggettoDaRicalcolare ogg : oggettiDaRicalcolare) {
			applicationService.saveOrUpdate(OggettoDaRicalcolare.class, ogg);
		}
		log.debug("...formule registrate");
	}
	
	/**
	 * Metodo che calcola le formule sull'oggetto con anagrafica passato come
	 * parametro, verificando la eventuale ripetibilita' delle proprieta di cui
	 * si vogliono calcolare le formule e valutando la regola di ricalcolo.
	 * 
	 * @param oggetto
	 *            : anagrafica di cui si vogliono aggiornare le formule *
	 * @throws OgnlException
	 * @throws ClassNotFoundException
	 */
	public <P extends Property<TP>, TP extends PropertiesDefinition> void ricalcolaFormule(
			AnagraficaSupport<P, TP> oggetto) throws OgnlException {
		// FIXME bisogna verificare se va in loop
					
		log.debug("Chiamato il RICALCOLAFORMULE in FORMULAMANAGER...");
		
		List<TP> tipologieConRenderingFormula = applicationService.getAllTipologieProprietaWithWidgetFormula(oggetto.getClassPropertiesDefinition());
		
		for(TP tip : tipologieConRenderingFormula) {
			log.debug("ciclando sulle tipologie di proprieta per trovare tp con rendering widgetformula in ricalcolaFormule");		
							
			log.debug("la tipologia di proprieta': " + tip.getShortName()
						+ " e' una formula");
				WidgetFormula widgetFormula = (WidgetFormula) tip.getRendering();
			if (tip.isTopLevel()) {
				boolean result = valutaRegolaDiCalcolo(
						widgetFormula.getRegolaDiRicalcolo(), oggetto, null);

				if (result) {
					// cancello tutte le proprieta del metadato con rendering
					// formula
					
					if(((Identifiable)oggetto).getId()!=null) {
						List<P> proprietaDaCancellare = applicationService.<P, TP>getProprietaByParentAndTipologia((Identifiable)oggetto,tip);
								
						for(P prop : proprietaDaCancellare) {
							oggetto.removeProprieta(prop);
						}
					}
//					List<P> proprietaDaCancellare = oggetto
//					.getProprietaDellaTipologia(tip);
//					
//					int size = proprietaDaCancellare.size();
//					
//					for (int w = 0; w < size; w++) {
//						
//						P pro = proprietaDaCancellare
//						.get(size - 1 - w);
//						oggetto.removeProprieta(pro);
//						
//					}				
//										
					//se non e' null calcolo il result number della formula e itero per ricalcolare le formule
					//altrimenti devo se e' null vuol dire che non e' ripetibile
					String resultNumberString = widgetFormula.getResultNumber();
					Integer resultNumber = 1;
					if(resultNumberString!=null && resultNumberString.length()!=0){
						try {
						resultNumber = (Integer)calcoloValore(resultNumberString, oggetto,null,null);
						}
						catch (RuntimeException e) {
							resultNumber = 0;
							log.warn("non e' stato possibile calcolare il resultNumber" + e.getStackTrace());
						}
					}
						for(int i = 0; i<resultNumber; i++) {
							

							log.debug("valuto la regola di ricalcolo... " + result);

							Object valoreObject = null;
							try {
								valoreObject = FormulaManager.calcoloValore(					
										widgetFormula.getExpression(), oggetto, null, i);
							}
							catch(RuntimeException e) {
								log.warn("Errore nel ricalcolo formula: "+e.getMessage());
							}					

							if (valoreObject != null) {
								// set del nuovo valore della proprieta che
								// contiene il
								// risultato della formula
								log.debug("nuovo valore: " + valoreObject + " class: "
										+ valoreObject.getClass());

								if (valoreObject instanceof BigInteger) {
									log.debug("trasformo il risultato in Double");
									BigInteger bi = (BigInteger) valoreObject;
									valoreObject = new Double(bi.doubleValue());
									log.debug("nuovo valore: " + valoreObject
											+ " class: " + valoreObject.getClass());
								}
								if (valoreObject instanceof BigDecimal) {
									log.debug("trasformo il risultato in Double");
									BigDecimal bi = (BigDecimal) valoreObject;
									valoreObject = new Double(bi.doubleValue());
									log.debug("nuovo valore: " + valoreObject
											+ " class: " + valoreObject.getClass());
								}
								
								P proprieta = oggetto.createProprieta(tip);
								proprieta.getValue().setOggetto(valoreObject);
								log.debug("creata proprieta in posizione "+ i);
							}									
						}
					}
				}

		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void receive(Class<JPAEvent> classeEvento, JPAEvent evento){
		try {
			if (evento.getClazz().equals(OggettoDaRicalcolare.class.getCanonicalName())) {
				return;
			}
			
			if (evento.getOperationType().equals(JPAEvent.DELETE)) {
				//non fare niente
			}
			else {
				if (evento.getOperationType().equals(JPAEvent.CREATE)) {
					Object object;
					try {
						object = applicationService.get(Class.forName(evento
							.getClazz()), evento.getId());
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
					if (object != null && object instanceof AnagraficaSupport) {
						AnagraficaSupport oggetto = (AnagraficaSupport) object;
						registraFormule(oggetto);
					}
				}
				
//				ricalcoloFormula(evento);
			}
		} catch (Exception e) {
			log
					.error(
							"Errore OGNL nel calcolo di una formula o nella registrazione di una variabile",
							e);
		}
	}
	
	/**
	 * Registra gli eventi sulle variabili settate in configurazione (le
	 * variabili dei widget contengono solo gli oggetti padre e non le singole
	 * proprieta)
	 * 
	 * @param p
	 *            la proprieta su cui e' registrata la formula
	 * @param mem
	 *            il gestore per l'evento ModelEvento
	 * 
	 * */
	private Set<ModelEventoString> getEventiVariabili(Property p) {
		WidgetFormula wf = (WidgetFormula) p.getTypo().getRendering();
		// evita il lazy su variabili... non si dovrebbe usare!!!
		//List<String> variabiliWidget = getVariabili(wf);
		List<String> variabiliWidget = wf.getVariabili();
		
//		Integer idx = p.getPropertyParent() != null ? p
//				.getPropertyParent().getPosition() : null;

//		return getEventiVariabili(variabiliWidget, p.getParent(), idx);
		return null;
	}
	
	private Set<ModelEventoString> getEventiVariabili(List<String> variabiliWidget, Object rootObject, Integer idx){
		Set<ModelEventoString> results = new HashSet<ModelEventoString>();

		if (variabiliWidget != null && !variabiliWidget.isEmpty()) {
			for (String variabile : variabiliWidget) {

				// si valutano le stringhe contenute nella lista variabili
				// del widget formula
				// l'oggetto su cui vengono valutate e' il padre della
				// proprieta
				if (StringUtils.hasText(variabile)) {

					try {
						Object result = FormulaManager
						.calcoloValore(variabile, rootObject,
								idx, null);
						
							if (result instanceof Identifiable) {
								Identifiable resultValutazione = (Identifiable) result;
								ModelEventoString e = new ModelEventoString(
										resultValutazione.getClass()
												.getCanonicalName(),
										resultValutazione.getId()
										);
								//e = mem.getEvento(e);
								results.add(e);
							} else if (result instanceof Collection) {
								for (Identifiable resultValutazione : (Collection<Identifiable>) result) {
									ModelEventoString e = new ModelEventoString(
											resultValutazione.getClass()
													.getCanonicalName(),
											resultValutazione.getId());
									//e = mem.getEvento(e);
									results.add(e);
								}
							} else {
								log
										.warn("Errore nel calcolo formula in metodo getEventiVariabili: il risultato della valutazione non e' Identifiable o una Collection");
							}
						
					} catch (RuntimeException e) {
						log.warn("Errore nel calcolo formula in metodo getEventiVariabili: "
								+ e.getMessage());
					}
				
				}
			}
		}
		return results;
	}
	
	public void setApplicationService(IPersistenceFormulaService applicationService) {
		this.applicationService = applicationService;		
	}
	
   


	class ModelEventoString {

	String evento;
	Integer id;
	
	public ModelEventoString(String evento, Integer id) {
		if (evento != null && evento.indexOf("$$") != -1)
		{
			evento = evento.substring(0,evento.indexOf("$$"));
		}
		this.evento = evento;
		this.id = id;
	}
	
	public String getEvento() {
		return evento;
	}
	public void setEvento(String evento) {
		this.evento = evento;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder(17,37); 
		if(evento!=null) hash.append(evento.hashCode());
		if(id!=null) hash.append(id.hashCode());		
		return hash.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		ModelEventoString mes = (ModelEventoString)obj;
		if(this.evento.equals(mes.evento) && this.id.equals(mes.id)) {
			return true;
		}
		return false;
	}
	
 }

}

