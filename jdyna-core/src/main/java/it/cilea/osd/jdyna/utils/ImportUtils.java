package it.cilea.osd.jdyna.utils;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ImportUtils implements ApplicationContextAware {
	private IPersistenceDynaService applicationService;
	private ApplicationContext appContext;
//	private FormulaManager formulaManager;
//	
//	public void setFormulaManager(FormulaManager formulaManager) {
//		this.formulaManager = formulaManager;
//	}

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;

	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * Effettua l'import di una specifica tipologia di oggetti a partire dalla
	 * loro definizione xml (Spring)
	 * 
	 * @param <T>
	 *            la classe di oggetti da importare
	 * @param importFilePath
	 *            il path al file (sul server) contenente la definizione xml
	 *            degli oggetti
	 * @param clazz
	 *            la classe di oggetti da importare
	 * @return il risultato dell'importazione (num. successi, num. fallimenti,
	 *         eccezioni occorse)
	 */
	public <T extends Identifiable> ImportResult importSingolaClasseOggetti(
			String importFilePath, Class<T> clazz) {

		ImportResult importResult = new ImportResult();

		ApplicationContext context = new FileSystemXmlApplicationContext(
				new String[] { "file:"+importFilePath }, appContext);

		String[] definitions = context.getBeanNamesForType(clazz);

		for (String definition : definitions) {
			try {
				T oggettoDaImportare = (T) context.getBean(definition);
				
				if(clazz.isAssignableFrom(AnagraficaSupport.class)) {
					AnagraficaObject oggetto = (AnagraficaObject)oggettoDaImportare;					
					applicationService.fitAnagrafica(oggetto);	
		    		//formulaManager.ricalcolaFormule(oggetto);
		    		oggetto.pulisciAnagrafica();
				}
				applicationService.saveOrUpdate(clazz, oggettoDaImportare);
				importResult.addSuccess();
			} catch (Exception ecc) {
				logger.warn("Errore nel salvataggio della definizione di "
						+ clazz.getSimpleName() + " - bean id: " + definition,
						ecc);

				importResult.addFail(ecc);
			}
		}
		return importResult;
	}

	public class ImportResult {
		private int success = 0;
		private int fail = 0;
		private List<Exception> exceptions = new LinkedList<Exception>();

		void addSuccess() {
			success++;
		}

		void addFail(Exception ex) {
			fail++;
			exceptions.add(ex);
		}

		/**
		 * 
		 * @return numero di definizioni importate con successo
		 */
		public int getSuccess() {
			return success;
		}

		/**
		 * 
		 * @return numero di definizioni non importate
		 */
		public int getFail() {
			return fail;
		}

		/**
		 * 
		 * @return lista delle eccezioni sollevate durante l'importazione (eventualmente vuota ma mai <code>null</code>
		 */
		public List<Exception> getExceptions() {
			return exceptions;
		}

	}

}
