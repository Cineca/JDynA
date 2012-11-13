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
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * SimpleDynaController per l'export delle configurazioni dei soggettari. Se la request
 * contiene un soggettario_id viene esportato solo il soggettario con quell'ID
 * specifico.
 * 
 * @author bollini
 */
public class ExportConfigurazioneSoggettari extends BaseFormController {

	private IPersistenceDynaService applicationService;

	/**
	 * Esporta la configurazione corrente di tutti i soggettari o,
	 * se la request contiene un soggettario_id, solo il soggettario con
	 * quell'ID specifico.
	 * 
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws RuntimeException, IOException {
		String soggettarioIDStr = request.getParameter("soggettario_id");
		Integer soggettarioID = null;
		if (soggettarioIDStr != null) {
			soggettarioID = Integer.parseInt(soggettarioIDStr);
		}
		Soggettario soggettario = null;
		if (soggettarioID != null) {
			soggettario = applicationService.get(Soggettario.class,
					soggettarioID);
		}

		String filename;
		if (soggettario == null) {
			filename = "configurazione-soggettari.xml";
		} else {
			filename = "configurazione-soggettario_"
					+ soggettario.getNome().replaceAll(" ", "_") + ".xml";
		}
		response.setContentType("application/xml");
		response.addHeader("Content-Disposition", "attachment; filename="
				+ filename);

		PrintWriter writer = response.getWriter();
		writer
				.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\"\n"
						+ "\"http://www.springframework.org/dtd/spring-beans.dtd\">\n\n\n");
		writer.print("<beans>\n");
		writer
				.print("   <!-- Configurazioni per il tool di import (NON MODIFICARE) -->\n");
		writer
				.print("		<bean id=\"customEditorConfigurer\" class=\"org.springframework.beans.factory.config.CustomEditorConfigurer\">\n");
		writer.print("			<property name=\"customEditors\">\n");
		writer.print("    	       <map>\n");
		writer
				.print("      	      <entry key=\"it.cilea.osd.jdyna.model.Soggettario\">\n");
		writer
				.print("                    <ref bean=\"configurazioneSoggettarioEditor\"/>\n");
		writer.print("                </entry>\n");
		writer.print("             </map>\n");
		writer.print("          </property>\n");
		writer.print(" 		</bean>\n");

		if (soggettario != null) {
			toXML(writer, soggettario);
		} else {
			List<Soggettario> allSoggettari = applicationService
					.getList(Soggettario.class);
			for (Soggettario soggettarioIter : allSoggettari) {
				toXML(writer, soggettarioIter);
			}
		}
		response.getWriter().print("</beans>");
		return null;
	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * Metodo di supporto per esportare un soggettario nella sua
	 * "bean definition". 
	 * 
	 * @param writer
	 * @param soggettario
	 *            l'albero da esportare
	 */
	//FIXME valutare la possibilita' di utilizzare il polimorfismo implementando un'interfaccia IExportableBeanDefinition
	private void toXML(PrintWriter writer, Soggettario soggettario) {
		writer.print("    <bean id=\""
				+ Soggettario.class.getSimpleName() + soggettario.getId()
				+ "\" class=\""
				+ Soggettario.class.getCanonicalName() + "\">\n");
		writer.print("					<property name=\"nome\" value=\"" + soggettario.getNome()
				+ "\" />\n");
		writer.print("					<property name=\"attivo\" value=\""
				+ soggettario.isAttivo() + "\" />\n");
		writer.print("					<property name=\"chiuso\" value=\""
				+ soggettario.isChiuso() + "\" />\n");

		if (soggettario.getDescrizione() != null) {
			writer.print("					<property name=\"descrizione\"><value><![CDATA["
					+ soggettario.getDescrizione() + "]]></value></property>\n");
		}

		writer.print("    </bean>\n\n");
		writer.print("    <!-- Soggetti del soggettario:" + soggettario.getNome()
				+ " -->\n\n");

		for (Soggetto classificazione : soggettario.getSoggetti()) {
				toXML(writer, classificazione);
		}
		writer.print("    <!-- FINE Soggetti del soggettario:"
				+ soggettario.getNome() + " -->\n\n");

	}

	/**
	 * Metodo di supporto per esportare un soggetto nella sua "bean
	 * definition". //FIXME valutare la possibilita' di utilizzare il
	 * polimorfismo implementando un'interfaccia IExportableBeanDefinition
	 * 
	 * @param writer
	 * @param soggetto
	 */
	private void toXML(PrintWriter writer, Soggetto soggetto) {
		writer.print("         <bean id=\""
				+ Soggetto.class.getSimpleName()
				+ soggetto.getId() + "\" class=\""
				+ Soggetto.class.getCanonicalName() + "\">\n");
		writer.print("					<property name=\"soggettario\" ref=\""
				+ Soggettario.class.getSimpleName()
				+ soggetto.getSoggettario().getId()
				+ "\" />\n");
		writer.print("					<property name=\"voce\"><value><![CDATA["
					+ soggetto.getVoce() + "]]></value></property>\n");
		writer.print("         </bean>\n\n");
	}
}
