package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.utils.FileUploadConfiguration;

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
 * SimpleDynaController per l'import di alberi classificatori
 * 
 * @author bollini
 * 
 */
public class ImportConfigurazioneAlberiClassificatori extends
		BaseFormController {
	private IPersistenceDynaService applicationService;
	private String path;

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Effettua l'import da file xml delle configurazioni di alberi
	 * classificatori e/o classificazioni; Il file di configurazione viene
	 * caricato come contesto di spring e salvate su db.
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws RuntimeException, IOException {

		FileUploadConfiguration bean = (FileUploadConfiguration) object;
		MultipartFile file = (CommonsMultipartFile) bean.getFile();
		File a = null;

		// creo il file temporaneo che sarà caricato come contesto di spring
		a = File.createTempFile("jdyna", ".xml", new File(path));
		file.transferTo(a);

		ApplicationContext context = null;
		try {
			context = new FileSystemXmlApplicationContext(new String[] { "file:"+a
					.getAbsolutePath() }, getApplicationContext());

		} catch (XmlBeanDefinitionStoreException exc) {
			// cancello il file dalla directory temporanea
			logger.warn(
					"Errore nell'importazione della configurazione dal file: "
							+ file.getOriginalFilename(), exc);
			a.delete();
			saveMessage(request, getText("action.file.nosuccess.upload",
					new Object[] { exc.getMessage() }, request.getLocale()));
			return new ModelAndView(errorView);
		}
		// cancello il file dalla directory temporanea
		a.delete();


		// recupero le definizioni di classificazioni al di fuori di alberi
		// (integrazioni ad alberi esistenti)
		String[] classificazioniDefinitions = context
				.getBeanNamesForType(Classificazione.class);

		for (String classificazioneDefinition : classificazioniDefinitions) {
				Classificazione classificazioneDaImportare = (Classificazione) context
						.getBean(classificazioneDefinition);
				
				if (classificazioneDaImportare.getPadre() == null){
					classificazioneDaImportare.getAlberoClassificatorio()
							.getTopClassificazioni().add(
									classificazioneDaImportare);
				} else {
					classificazioneDaImportare.getPadre().getSottoCategorie()
							.add(classificazioneDaImportare);
				}
		}

		
		String[] alberiDefinitions = context
			.getBeanNamesForType(AlberoClassificatorio.class);
		
		// la variabile i conta gli alberi caricati con successo
		int i = 0;
		// la variabile j conta gli alberi non caricati
		int j = 0;
		for (String alberoDefinition : alberiDefinitions) {
			try {
				AlberoClassificatorio alberoDaImportare = (AlberoClassificatorio) context
						.getBean(alberoDefinition);
				applicationService.saveOrUpdate(AlberoClassificatorio.class,
						alberoDaImportare);
			} catch (Exception ecc) {
				saveMessage(request, getText(
						"action.file.nosuccess.metadato.upload", new Object[]{ecc.getMessage()}, request
								.getLocale()));	
				j++;
				i--;
			}
			i++;
		}

		
		saveMessage(request, getText(
				"action.file.success.upload.dettagli.alberi", new Object[] {
						new Integer(i), new Integer(j) }, request.getLocale()));
		return new ModelAndView(detailsView);
	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public void setPath(String path) {
		this.path = path;
	}

}