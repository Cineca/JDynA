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
package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.utils.FileUploadConfiguration;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * SimpleDynaController per l'import di configurazioni anagrafiche degli oggetti. 
 * 
 * @author bollini, pascarelli
 *
 * @param <TP> la tipologia proprieta dell'oggetto
 * @param <A>  il  tipo di area dell'oggetto
 */
public class ImportConfigurazione<TY extends AType<TP>,TP extends PropertiesDefinition, H extends IPropertyHolder, A extends Tab<H>> extends BaseFormController {
	private Class<TP> tpClass;
	private Class<A> areaClass;
	private Class<TY> typeClass;
	private IPersistenceDynaService applicationService;
	private String path;
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
	/** Performa l'import da file xml di configurazioni di oggetti;
	 *  Sull'upload del file la configurazione dell'oggetto viene caricato come contesto di spring
	 *  e salvate su db.
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws RuntimeException, IOException {
		
				    
			FileUploadConfiguration bean = (FileUploadConfiguration)object;
			MultipartFile file = (CommonsMultipartFile)bean.getFile();		
			File a = null;

			//creo il file temporaneo che sara' caricato come contesto di spring per caricare la configurazione degli oggetti
			a = File.createTempFile("jdyna", ".xml", new File(path));
			file.transferTo(a);
			
			ApplicationContext context = null;			
			try {			
				context = new FileSystemXmlApplicationContext(new String [] {"file:"+a.getAbsolutePath()}, getApplicationContext());
				
			}
			catch(XmlBeanDefinitionStoreException exc) {
				//cancello il file dalla directory temporanea
				logger.warn("Errore nell'importazione della configurazione dal file: "+ file.getOriginalFilename(), exc);
				a.delete();
				saveMessage(request, getText(
						"action.file.nosuccess.upload", new Object[]{exc.getMessage()}, request
								.getLocale()));		
				return new ModelAndView(getErrorView());
			}
			//cancello il file dalla directory temporanea
			a.delete();
			String[] tpDefinitions = context.getBeanNamesForType(tpClass);
			//la variabile i conta le tipologie caricate con successo
			int i = 0;
			//la variabile j conta le tipologie non caricate
			int j = 0;
			for (String tpNameDefinition : tpDefinitions) {
				try {
					TP tipologiaDaImportare = (TP) context.getBean(tpNameDefinition);
					applicationService.saveOrUpdate(tpClass, tipologiaDaImportare);
				}
				catch(Exception ecc) {
					saveMessage(request, getText(
							"action.file.nosuccess.metadato.upload", new Object[]{ecc.getMessage()}, request
									.getLocale()));					
					j++;
					i--;
				}
				i++;
			}
		
			String[] areeDefinitions = context.getBeanNamesForType(areaClass);
			int w = 0;
			int v = 0;
			for (String areaNameDefinition : areeDefinitions) {
				try {
				A areaDaImportare = (A) context.getBean(areaNameDefinition);
				applicationService.saveOrUpdate(areaClass, areaDaImportare);
				}
				catch(Exception ecc) {
					saveMessage(request, getText(
							"action.file.nosuccess.metadato.upload", new Object[]{ecc.getMessage()}, request
									.getLocale()));			
					v++;
					w--;
				}
				w++;
			}
			// check sulla tipologia poiche' ci sono oggetti che non hanno la tipologia tipo Opera e Parte
			if (typeClass != null) {
			String[] typeDefinitions = context.getBeanNamesForType(typeClass);
			int r = 0;
			int t = 0;
			for (String typeDefinition : typeDefinitions) {
				try {
					TY tipologiaDaImportare = (TY) context.getBean(typeDefinition);
					applicationService.saveOrUpdate(typeClass, tipologiaDaImportare);
				} catch (Exception ecc) {
					saveMessage(request, getText(
							"action.file.nosuccess.metadato.upload", new Object[]{ecc.getMessage()}, request
									.getLocale()));			
					t++;
					r--;
				}
				r++;
			}
			
				saveMessage(request, getText(
						"action.file.success.upload.tipologie",				
						new Object[]{new String("Totale Tipologie:"+(t+r)+" ["+r+"caricate con successo/"+t+" fallito caricamento]")},request.getLocale()));
			
		}
			
		
			

			saveMessage(request, getText("action.file.success.upload",
				new Object[] {
						new String("Totale TP:" + (i + j) + "" + "[" + i
								+ " caricate con successo/" + j
								+ " fallito caricamento]"),
						new String("Totale Aree:" + (w + v) + " [" + w
								+ "caricate con successo/" + v
								+ " fallito caricamento]") }, request
						.getLocale()));


		
		return new ModelAndView(getDetailsView());
	}

		
	public void setTpClass(Class<TP> tpClass) {
		this.tpClass = tpClass;
	}

	public void setAreaClass(Class<A> areaClass) {
		this.areaClass = areaClass;
	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public void setTypeClass(Class<TY> typeClass) {
		this.typeClass = typeClass;
	}
}
