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

import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



public class ClassificationExportUtils implements ApplicationContextAware {
	private IPersistenceDynaService applicationService;
	private ApplicationContext appContext;
	
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
	 * Metodo di supporto per scrivere il DOCTYPE e il bean per il custom editor dell'albero classificatore
	 * 
	 * @param writer
	 * @param albero
	 *            l'albero da esportare
	 */
	public void writeDocTypeANDCustomEditorAlberoClassificatore(PrintWriter writer) {
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
				.print("      	      <entry key=\"it.cilea.osd.jdyna.model.AlberoClassificatorio\">\n");
		writer
				.print("                    <ref bean=\"configurazioneAlberoClassificatorioEditor\"/>\n");
		writer.print("                </entry>\n");
		writer
				.print("      	      <entry key=\"it.cilea.osd.jdyna.model.Classificazione\">\n");
		writer
				.print("                    <ref bean=\"configurazioneClassificazioneEditor\"/>\n");
		writer.print("                </entry>\n");

		writer.print("             </map>\n");
		writer.print("          </property>\n");
		writer.print(" 		</bean>\n");

	}
	
	/**
	 * Metodo di supporto per esportare un albero classificatorio nella sua
	 * "bean definition". //FIXME valutare la possibilita' di utilizzare il
	 * polimorfismo implementando un'interfaccia IExportableBeanDefinition
	 * 
	 * @param writer
	 * @param albero
	 *            l'albero da esportare
	 */
	public void alberoClassificatorioToXML(PrintWriter writer, AlberoClassificatorio albero) {
		writer.print("    <bean id=\""
				+ AlberoClassificatorio.class.getSimpleName() + albero.getId()
				+ "\" class=\""
				+ AlberoClassificatorio.class.getCanonicalName() + "\">\n");
		writer.print("					<property name=\"nome\" value=\"" + albero.getNome()
				+ "\" />\n");
		writer.print("					<property name=\"attiva\" value=\""
				+ albero.isAttiva() + "\" />\n");
		if (albero.getDescrizione() != null) {
			writer.print("					<property name=\"descrizione\"><value><![CDATA["
					+ albero.getDescrizione() + "]]></value></property>\n");
		}
		// writer
		// .print(" <property name=\"classificazioni\">\n"
		// + " <list>\n");
		// for (Classificazione classificazione : albero.getClassificazioni()) {
		// // considero solo le classificazioni di primo livello
		// if (classificazione.getPadre() == null) {
		// toXML(writer, classificazione);
		// }
		// }
		// writer.print(" </list>\n");
		// writer.print(" </property>\n");

		writer.print("    </bean>\n\n");
		writer.print("    <!-- Classificazioni dell'albero:" + albero.getNome()
				+ " -->\n\n");

		for (Classificazione classificazione : albero.getTopClassificazioni()) {
			// considero solo le classificazioni di primo livello
			if (classificazione.getPadre() == null) {
				classificazioneToXML(writer, classificazione);
			}
		}
		writer.print("    <!-- FINE Classificazioni dell'albero:" + albero.getNome()
				+ " -->\n\n");

	}

	/**
	 * Metodo di supporto per esportare una classificazione nella sua "bean
	 * definition". //FIXME valutare la possibilita' di utilizzare il
	 * polimorfismo implementando un'interfaccia IExportableBeanDefinition
	 * 
	 * @param writer
	 * @param classificazione
	 */
	private void classificazioneToXML(PrintWriter writer, Classificazione classificazione) {
		writer.print("         <bean id=\""
				+ Classificazione.class.getSimpleName()
				+ classificazione.getId() + "\" class=\""
				+ Classificazione.class.getCanonicalName() + "\">\n");
		writer.print("					<property name=\"alberoClassificatorio\" ref=\""
				+ AlberoClassificatorio.class.getSimpleName()
				+ classificazione.getAlberoClassificatorio().getId()
				+ "\" />\n");
		if (classificazione.getPadre() != null) {
			writer.print("					<property name=\"padre\" ref=\""

			+ Classificazione.class.getSimpleName()
					+ classificazione.getPadre().getId() + "\" />\n");
		}
		writer.print("					<property name=\"codice\" value=\""
				+ classificazione.getCodice() + "\" />\n");
		writer.print("					<property name=\"nome\" value=\""
				+ classificazione.getNome() + "\" />\n");
		writer.print("					<property name=\"selezionabile\" value=\""
				+ classificazione.isSelezionabile() + "\" />\n");
		writer.print("					<property name=\"attivo\" value=\""
				+ classificazione.isAttivo() + "\" />\n");
		// bisogna settare solo i legami "a salire" quelli a scendere vengono
		// dedotti automaticamente
		// writer
		// .print(" <property name=\"sottoCategorie\">\n"
		// + " <list>\n");
		// for (Classificazione subClass : classificazione.getSottoCategorie())
		// {
		//
		// toXML(writer, subClass);
		//
		// }
		// writer.print(" </list>\n");
		// writer.print(" </property>\n");

		writer.print("         </bean>\n\n");
		writer.print("    <!-- SottoClassificazioni di:"
				+ classificazione.getNome() + " -->\n\n");

		for (Classificazione subClass : classificazione.getSottoCategorie()) {
			classificazioneToXML(writer, subClass);
		}
		
		writer.print("    <!-- FINE SottoClassificazioni di:"
				+ classificazione.getNome() + " -->\n\n");

	}
		
}
