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
	

	/** Crea un marcatore xml (tag) settandogli un id passato come parametro */	
	public static Element createTagXML(Element root, String tag,String tagId) {
		Element marcatore = new Element(tag);
		marcatore.setAttribute("id", tagId);		
		root.addContent(marcatore);
		return marcatore;
	}

	/** Crea un marcatore xml */
	public static Element createTagXML(Element root, String tag) {
		Element marcatore = new Element(tag);		
		root.addContent(marcatore);
		return marcatore;
	}

	/** Crea una proprietà all'interno del marcatore settando anche il valore 
	 * 
	 * @return l'elemento appena creato */
	public static Element createProperty(Element root, String tag, String name, String value) {
		Element prop = new Element(tag);
		prop.setAttribute("name", name);
		Element valueElement = new Element("value");
		valueElement.addContent(value);
		prop.addContent(valueElement);
		root.addContent(prop);
		return prop;
	}
	
	/** Crea una proprietà all'interno del marcatore con nomi e valori degli attributi passati come parametri  
	 * {@code 
	 * Esempio: 
	 * <example>
	 * 		<test message="hello" messaggeTwo="hello people">
	 * 			
	 * 		</test>	  	 	
	 * </example>
	 * }
	 * @return l'elemento appena creato */
	public static Element createCustomPropertyWithCustomAttributes(Element root, String tag,List<String> attributes, List<String> valuesAttributes) {
		Element prop = new Element(tag);
		int i = 0;
		for(String attribute : attributes) {
			prop.setAttribute(attribute, valuesAttributes.get(i));	
			i++;
		}				
		root.addContent(prop);
		return prop;
	}
	
	
	/** Crea una proprietà all'interno di un marcatore(root) settando anche un insieme di valori 
	 * 
	 * {@code 
	 * Esempio: 
	 * <example>
	 * 	<test message="hello">
	 * 			
	 * 	</test>	  	 	
	 * </example>
	 * }
	 * @return l'elemento appena creato */
	public static Element createCustomPropertyWithCustomAttribute(Element root, String tag, String attribute, String valuesAttribute) {
		Element prop = new Element(tag);		
		prop.setAttribute(attribute, valuesAttribute);						
		root.addContent(prop);
		return prop;
	}
	
	/** Crea una proprietà all'interno del marcatore settando anche un insieme di valori 
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
	 * @return l'elemento appena creato */
	public static Element createCustomPropertyWithCustomValues(Element root, String tag, List<String> tagValues, List<String> values) {
		Element prop = new Element(tag);
		int i = 0;
		for(String tagValue : tagValues) {
			Element valueElement = new Element(tagValue);
			valueElement.addContent(values.get(i));
			prop.addContent(valueElement);
			i++;
		}				
		root.addContent(prop);
		return prop;
	}
	
	/** Crea un valore custom all'interno del marcatore passato come parametro 
	 * 
	 * {@code 
	 * Esempio: root = test ; tagValue = "test1" ; value ="Hello!"
	 * <test>
	 * 		<test1>
	 * 			Hello!
	 * 		</test1>	 	 	
	 * </test>
	 * }
	 * @return l'elemento appena creato */
	public static Element createCustomValue(Element root, String tagValue, String value) {
						
		Element valueElement = new Element(tagValue);
		valueElement.addContent(value);
		root.addContent(valueElement);		
		return root;
	}
	
}
