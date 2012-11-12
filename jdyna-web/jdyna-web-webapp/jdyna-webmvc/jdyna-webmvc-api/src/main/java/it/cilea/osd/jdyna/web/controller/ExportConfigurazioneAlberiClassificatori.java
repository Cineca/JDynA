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
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.utils.ClassificationExportUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * SimpleDynaController per l'export delle configurazioni degli alberi classificatori. Se
 * la request contiene un albero_id viene esportato solo l'albero
 * classificatorio con quell'ID specifico.
 * 
 * @author bollini
 */
public class ExportConfigurazioneAlberiClassificatori extends
		BaseFormController {

	private IPersistenceDynaService applicationService;	
	private ClassificationExportUtils exportUtils;
	

	/**
	 * Esporta la configurazione corrente di tutti gli alberi classificatori o,
	 * se la request contiene un albero_id, solo del'albero classificatorio con
	 * quell'ID specifico.
	 * 
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws RuntimeException, IOException {
		String alberoIDStr = request.getParameter("albero_id");
		Integer alberoID = null;
		if (alberoIDStr != null) {
			alberoID = Integer.parseInt(alberoIDStr);
		}
		AlberoClassificatorio albero = null;
		if (alberoID != null) {
			albero = applicationService.get(AlberoClassificatorio.class,
					alberoID);
		}

		String filename;
		if (albero == null) {
			filename = "configurazione-alberi_classificatori.xml";
		} else {
			filename = "configurazione-albero_classificatorio_"
					+ albero.getNome().replaceAll(" ", "_") + ".xml";
		}
		response.setContentType("application/xml");
		response.addHeader("Content-Disposition", "attachment; filename="
				+ filename);

		PrintWriter writer = response.getWriter();
		exportUtils.writeDocTypeANDCustomEditorAlberoClassificatore(writer);

		if (albero != null) {
			exportUtils.alberoClassificatorioToXML(writer, albero);
		} else {
			List<AlberoClassificatorio> allAlberi = applicationService
					.getList(AlberoClassificatorio.class);
			for (AlberoClassificatorio alberoIter : allAlberi) {
				exportUtils.alberoClassificatorioToXML(writer, alberoIter);
			}
		}
		response.getWriter().print("</beans>");
		return null;
	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public ClassificationExportUtils getExportUtils() {
		return exportUtils;
	}

	public void setExportUtils(ClassificationExportUtils exportUtils) {
		this.exportUtils = exportUtils;
	}

	
}
