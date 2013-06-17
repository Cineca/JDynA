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

import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ExportUtils implements ApplicationContextAware {
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
	 * Metodo di supporto per scrivere il DOCTYPE e il bean per il custom editor
	 * dell'albero classificatore
	 * 
	 * @param writer
	 * @param albero
	 *            l'albero da esportare
	 */
	public void writeDocTypeANDCustomEditorAlberoClassificatore(
			PrintWriter writer) {
		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\"\n"
				+ "\"http://www.springframework.org/dtd/spring-beans.dtd\">\n\n\n");
		writer.print("<beans>\n");
		writer.print("   <!-- Configurazioni per il tool di import (NON MODIFICARE) -->\n");
		writer.print("		<bean id=\"customEditorConfigurer\" class=\"org.springframework.beans.factory.config.CustomEditorConfigurer\">\n");
		writer.print("			<property name=\"customEditors\">\n");
		writer.print("    	       <map>\n");
		writer.print("      	      <entry key=\"it.cilea.osd.jdyna.model.AlberoClassificatorio\">\n");
		writer.print("                    <ref bean=\"configurazioneAlberoClassificatorioEditor\"/>\n");
		writer.print("                </entry>\n");
		writer.print("      	      <entry key=\"it.cilea.osd.jdyna.model.Classificazione\">\n");
		writer.print("                    <ref bean=\"configurazioneClassificazioneEditor\"/>\n");
		writer.print("                </entry>\n");

		writer.print("             </map>\n");
		writer.print("          </property>\n");
		writer.print(" 		</bean>\n");

	}

	/** Crea un marcatore xml (tag) settandogli un id passato come parametro */
	public static Element createTagXML(Element root, String tag, String tagId) {
		Element marcatore = new Element(tag);
		marcatore.setAttribute("id", tagId);
		root.addContent(marcatore);
		return marcatore;
	}

	/** Crea un marcatore xml */
	public static Element createTagXML(Element root, String tag) {
		Element marcatore = new Element(tag, root.getNamespacePrefix(), root.getNamespaceURI());
		root.addContent(marcatore);
		return marcatore;
	}

	/**
	 * Crea una proprieta' all'interno del marcatore settando anche il valore
	 * 
	 * @return l'elemento appena creato
	 */
	public static Element createProperty(Element root, String tag, String name,
			String value) {
		Element prop = new Element(tag);
		prop.setAttribute("name", name);
		Element valueElement = new Element("value");
		valueElement.addContent(value);
		prop.addContent(valueElement);
		root.addContent(prop);
		return prop;
	}

	/**
	 * Crea una proprieta' all'interno del marcatore con nomi e valori degli
	 * attributi passati come parametri {@code 
	 * Esempio: 
	 * <example>
	 * 		<test message="hello" messaggeTwo="hello people">
	 * 			
	 * 		</test>	  	 	
	 * </example>
	 * }
	 * 
	 * @return l'elemento appena creato
	 */
	public static Element createCustomPropertyWithCustomAttributes(
			Element root, String tag, List<String> attributes,
			List<String> valuesAttributes) {
		Element prop = new Element(tag, root.getNamespacePrefix(), root.getNamespaceURI());
		int i = 0;
		for (String attribute : attributes) {
		    String value = valuesAttributes.get(i);
			prop.setAttribute(attribute, value==null?"":value);
			i++;
		}
		root.addContent(prop);
		return prop;
	}

	/**
	 * Crea una proprieta' all'interno di un marcatore(root) settando anche un
	 * insieme di valori
	 * 
	 * {@code 
	 * Esempio: 
	 * <example>
	 * 	<test message="hello">
	 * 			
	 * 	</test>	  	 	
	 * </example>
	 * }
	 * 
	 * @return l'elemento appena creato
	 */
	public static Element createCustomPropertyWithCustomAttribute(Element root,
			String tag, String attribute, String valuesAttribute) {
		Element prop = new Element(tag,root.getNamespacePrefix(), root.getNamespaceURI());
		prop.setAttribute(attribute, valuesAttribute);
		root.addContent(prop);
		return prop;
	}

	/**
	 * Crea una proprieta' all'interno del marcatore settando anche un insieme di
	 * valori
	 * 
	 * {@code 
	 * Esempio:
	 * <test>
	 * 		<test1>
	 * 			Hello!
	 * 		</test1>
	 * 	 	<test2>
	 * 			Hello World!
	 * 		</test2>
	 * </test>
	 * }
	 * 
	 * @return l'elemento appena creato
	 */
	public static Element createCustomPropertyWithCustomValues(Element root,
			String tag, List<String> tagValues, List<String> values) {
		Element prop = new Element(tag);
		int i = 0;
		for (String tagValue : tagValues) {
			Element valueElement = new Element(tagValue, root.getNamespacePrefix(), root.getNamespaceURI());
			valueElement.addContent(values.get(i));
			prop.addContent(valueElement);
			i++;
		}
		root.addContent(prop);
		return prop;
	}

	/**
	 * Crea un valore custom all'interno del marcatore passato come parametro
	 * 
	 * {@code 
	 * Esempio: root = test ; tagValue = "test1" ; value ="Hello!"
	 * <test>
	 * 		<test1>
	 * 			Hello!
	 * 		</test1>	 	 	
	 * </test>
	 * }
	 * 
	 * @return l'elemento appena creato
	 */
	public static Element createCustomValue(Element root, String tagValue,
			String value) {
		if (value != null && !value.isEmpty()) {
			Element valueElement = new Element(tagValue, root.getNamespacePrefix(), root.getNamespaceURI());
			valueElement.addContent(value);
			root.addContent(valueElement);
		}
		return root;
	}

	/**
	 * 
	 * Add child element with custom attributes and a value to root element
	 * passed as parameter of method
	 * 
	 * @param root
	 * @param tagValue
	 * @param value
	 * @param attributes
	 * @param valuesAttributes
	 * @return
	 */
	public static Element createCustomValueWithCustomAttributes(Element root,
			String tagValue, String value, List<String> attributes,
			List<String> valuesAttributes) {

		if (value != null && !value.isEmpty()) {
			Element valueElement = new Element(tagValue, root.getNamespacePrefix(), root.getNamespaceURI());
			int i = 0;
			for (String attribute : attributes) {
				String valueAttribute = valuesAttributes.get(i);				
				valueElement.setAttribute(attribute, valueAttribute==null?"":valueAttribute);
				i++;
			}
			valueElement.addContent(value);
			root.addContent(valueElement);
		}
		return root;
	}

    public static void createCoinvestigator(Element root, String real,
            String value, List<String> attributes, List<String> valuesAttributes)
    {
        if (value != null && !value.isEmpty()) {            
            Element coinvestigator = new Element("coInvestigator", root.getNamespacePrefix(), root.getNamespaceURI());
            int i = 0;
            for (String attribute : attributes) {
                String valueAttribute = valuesAttributes.get(i);                
                coinvestigator.setAttribute(attribute, valueAttribute==null?"":valueAttribute);
                i++;
            }
            coinvestigator.addContent(value);            
            root.addContent(coinvestigator);
        }        
    }
}
