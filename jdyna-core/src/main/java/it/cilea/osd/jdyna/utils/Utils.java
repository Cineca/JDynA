package it.cilea.osd.jdyna.utils;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Utils {
	
	/** Logger for this class and subclasses */
    protected static final Log log = LogFactory.getLog(Utils.class);
    
	/** Trasforma l'expression scritta dall'utente (salvata su db) in una regola comprensibile a OGNL; 
	 *  REGOLE:
	 *  -   l'utente inserirà una espressione del tipo '${costo}[1]', questa espressione verrà trasformata in un formato
	 *  	leggibile a OGNL quindi diventerà 'anagrafica4view['costo'][1].object';
	 *  -   la stringa '${costo}[n]' diventerà 'anagrafica4view['costo'][n].object';
	 *  -   ATT!!! se ho una combo bisogna valutarla in modo
	 *  	tale che se la stringa immessa dall'utente sia '${combo[${idx}].costo[1]*IVA}' bisogna 
	 *  	valutare per prima la combo con il suo idx passato come parametro per 
	 *  	poi prendere il giusto valore dell'elemento costo;
	 *  -   se non voglio utilizzare idx lo metto a null (ad esempio quando voglio solamente parserizzare una stringa)
	 *  - 	Per la ripetibilità nella formula bisogna inserire un resultIdx in modo tale da sostituirlo con quello passato come parametro
	 *      Es. se l'utente chiama questo metodo con un'espressione del tipo opera.${test}[${resultIdx}] con parametro resultIdx uguale a 4
	 *      allora il metodo cercherà la 4 proprieta ripetibile nell'anagrafica 4 view  
	 *  -   Il metodo deve essere chiamato sempre con parametro resultIdx uguale a 0 in modo tale da verificare se 
	 *      la formula contiene o meno la stringa resultIdx nell'espressione e quindi poter valutare o meno la ripetibilità
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

}
