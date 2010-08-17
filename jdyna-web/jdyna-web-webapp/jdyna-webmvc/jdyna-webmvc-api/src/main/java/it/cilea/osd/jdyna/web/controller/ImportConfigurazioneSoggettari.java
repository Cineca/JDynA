package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.utils.FileUploadConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
 * SimpleDynaController per l'import di soggettari
 * 
 * @author bollini
 * 
 */
public class ImportConfigurazioneSoggettari extends BaseFormController {
	private IPersistenceDynaService applicationService;
	private String path;

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Effettua l'import da file xml delle configurazioni dei soggettari e/o
	 * soggetti; Il file di configurazione viene caricato come contesto di
	 * spring e salvato su db.
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws RuntimeException, IOException {

		FileUploadConfiguration bean = (FileUploadConfiguration) object;
		MultipartFile file = (CommonsMultipartFile) bean.getFile();
		File a = null;

		// creo il file temporaneo che sarà caricato come contesto di spring
		a = File.createTempFile("jdyna_soggettari", ".xml", new File(path));
		file.transferTo(a);

		ApplicationContext context = null;
		try {
			context = new FileSystemXmlApplicationContext(new String[] {"file:"+a
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

		Set<Integer> soggettariImportati = new HashSet<Integer>();

		String[] soggettariDefinitions = context
				.getBeanNamesForType(Soggettario.class);
		// la variabile i conta gli alberi caricati con successo
		int i = 0;
		// la variabile j conta gli alberi non caricati
		int j = 0;
		for (String soggettarioDefinition : soggettariDefinitions) {
			try {
				Soggettario soggettarioDaImportare = (Soggettario) context
						.getBean(soggettarioDefinition);
				applicationService.saveOrUpdate(Soggettario.class,
						soggettarioDaImportare);
				soggettariImportati.add(soggettarioDaImportare.getId());
			} catch (Exception ecc) {
				saveMessage(request, getText(
						"action.file.nosuccess.metadato.upload", new Object[]{ecc.getMessage()}, request
								.getLocale()));	
				j++;
				i--;
			}
			i++;
		}

		// recupero le definizioni di soggetti al di fuori di soggettari
		// (integrazioni a soggettari esistenti)
		String[] soggettoDefinitions = context
				.getBeanNamesForType(Soggetto.class);
		int w = 0;
		int v = 0;
		for (String soggettoDefinition : soggettoDefinitions) {
			try {
				Soggetto soggettoDaImportare = (Soggetto) context
						.getBean(soggettoDefinition);
				// la classificazione non fa parte di un soggettario importato ma è
				// un aggiornamento di un soggettario pre-esistente
				if (!soggettariImportati.contains(soggettoDaImportare
						.getSoggettario().getId())) {
					applicationService.saveOrUpdate(
							Soggettario.class,
							soggettoDaImportare
									.getSoggettario());
				}
			} catch (Exception ecc) {
				saveMessage(request, getText(
						"action.file.nosuccess.metadato.upload", new Object[]{ecc.getMessage()}, request
								.getLocale()));	
				v++;
				w--;
			}
			w++;
		}
		saveMessage(request, getText(
				"action.file.success.upload.dettagli.soggettari", new Object[] {
						new Integer(i), new Integer(j) }, request.getLocale()));
		saveMessage(request, getText(
				"action.file.success.upload.dettagli.soggetti",
				new Object[] { new Integer(w), new Integer(v) }, request
						.getLocale()));
		return new ModelAndView(detailsView);
	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public void setPath(String path) {
		this.path = path;
	}

}