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
