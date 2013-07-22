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
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.widget.WidgetBoolean;
import it.cilea.osd.jdyna.widget.WidgetCheckRadio;
import it.cilea.osd.jdyna.widget.WidgetClassificazione;
import it.cilea.osd.jdyna.widget.WidgetDate;
import it.cilea.osd.jdyna.widget.WidgetEmail;
import it.cilea.osd.jdyna.widget.WidgetFormula;
import it.cilea.osd.jdyna.widget.WidgetFormulaClassificazione;
import it.cilea.osd.jdyna.widget.WidgetFormulaNumero;
import it.cilea.osd.jdyna.widget.WidgetNumero;
import it.cilea.osd.jdyna.widget.WidgetPointer;
import it.cilea.osd.jdyna.widget.WidgetSoggettario;
import it.cilea.osd.jdyna.widget.WidgetTesto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * SimpleDynaController per l'export di configurazioni anagrafiche degli
 * oggetti.
 * 
 * @author bollini
 * 
 * @param <TP>
 *            la tipologia di proprieta dell'oggetto
 * @param <A>
 *            l'area dell'oggetto
 * @param <TY>
 *            la tipologia dell'oggetto
 */
public class ExportConfigurazioneAnagrafica<TY extends AType<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>>
        extends BaseFormController
{

    private Class<TP> tpClass;

    private Class<TY> typeClass;

    private Class<H> boxClass;

    private String filename;

    private ITabService applicationService;

    /**
     * Esporta la configurazione anagrafica corrente di un oggetto in un file
     * xml
     * 
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object object, BindException errors)
            throws RuntimeException, IOException
    {
        response.setContentType("application/xml");
        response.addHeader("Content-Disposition", "attachment; filename="
                + filename);
        PrintWriter writer = response.getWriter();
        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\"\n"
                + "\"http://www.springframework.org/dtd/spring-beans.dtd\">\n\n\n");
        writer.print("<beans>\n");
        writer.print("   <!-- Configurazioni per il tool di import (NON MODIFICARE) -->\n");
        writer.print("		<bean id=\"customEditorConfigurer\" class=\"org.springframework.beans.factory.config.CustomEditorConfigurer\">\n");
        writer.print("			<property name=\"customEditors\">\n");
        writer.print("    	       <map>\n");
        writer.print("      	      <entry key=\"" + tpClass.getCanonicalName()
                + "\">\n");
        writer.print("                    <ref bean=\"configurazione"
                + tpClass.getSimpleName() + "Editor\"/>\n");
        writer.print("                </entry>\n");
        writer.print("      	      <entry key=\"it.cilea.osd.jdyna.model.Soggettario\">\n");
        writer.print("                    <ref bean=\"configurazioneSoggettarioEditor\"/>\n");
        writer.print("                </entry>\n");
        writer.print("      	      <entry key=\"it.cilea.osd.jdyna.model.AlberoClassificatorio\">\n");
        writer.print("                    <ref bean=\"configurazioneAlberoClassificatorioEditor\"/>\n");
        writer.print("                </entry>\n");
        writer.print("             </map>\n");
        writer.print("          </property>\n");
        writer.print(" 		</bean>\n");

        // Tipologie di proprieta'
        List<TP> allTP = applicationService.getList(tpClass);
        for (TP tp : allTP)
        {

            toXML(writer, tp);

        }

        // Aree
        Class<H> areaClass = boxClass;
        List<H> allAree = applicationService.getList(areaClass);
        for (H area : allAree)
        {
            toXML(writer, area);
        }

        // Tipologia di oggetto
        if (typeClass != null)
        {
            List<TY> allType = applicationService.getList(typeClass);
            for (TY type : allType)
            {
                toXML(writer, type);
            }
        }

        response.getWriter().print("</beans>");
        return null;
    }

    public void setTpClass(Class<TP> tpClass)
    {
        this.tpClass = tpClass;
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public void setTypeClass(Class<TY> typeClass)
    {
        this.typeClass = typeClass;
    }

    public void setBoxClass(Class<H> boxClass)
    {
        this.boxClass = boxClass;
    }

    /**
     * Imposta il nome che sara' assegnato al file di export
     * 
     * @param filename
     *            nome del file di export
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    /**
     * Metodo di supporto per esportare un widget nella sua "bean definition".
     * //FIXME valutare la possibilita' di utilizzare il pattern decorator e/o
     * il polimorfismo implementando un'interfaccia IExportableBeanDefinition
     * 
     * @param writer
     * @param widget
     */
    private void toXML(PrintWriter writer, AWidget widget)
    {
        if (widget instanceof WidgetBoolean)
        {
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetBoolean\" />\n");
        }
        else if (widget instanceof WidgetCheckRadio)
        {
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetCheckRadio\">\n");
            writer.print("                            <property name=\"option4row\" value=\""
                    + ((WidgetCheckRadio) widget).getOption4row() + "\" />\n");

            if (((WidgetCheckRadio) widget).getAlberoClassificatorio() != null)
            {
                writer.print("                            <property name=\"alberoClassificatorio\" value=\""
                        + ((WidgetCheckRadio) widget)
                                .getAlberoClassificatorio().getNome()
                        + "\" />\n" + "                        </bean>\n\n");
            }
            else
            {
                writer.print("                            <property name=\"soggettario\" value=\""
                        + ((WidgetCheckRadio) widget).getSoggettario()
                                .getNome()
                        + "\" />\n"
                        + "                        </bean>\n\n");
            }
        }
        else if (widget instanceof WidgetClassificazione)
        {
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetClassificazione\">\n"
                    + "                            <property name=\"alberoClassificatorio\" value=\""
                    + ((WidgetClassificazione) widget)
                            .getAlberoClassificatorio().getNome()
                    + "\" />\n"
                    + "                        </bean>\n\n");
        }
        else if (widget instanceof WidgetSoggettario)
        {
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetSoggettario\">\n"
                    + "                            <property name=\"soggettari\">\n"
                    + "                                  <list>\n");
            for (Soggettario soggettario : ((WidgetSoggettario) widget)
                    .getSoggettari())
            {
                writer.print("                                       <value>"
                        + soggettario.getNome() + "</value>\n");
            }
            writer.print("                                  </list>\n");
            writer.print("                            </property>\n");
            writer.print("                        </bean>\n\n");
        }
        else if (widget instanceof WidgetTesto)
        {
            WidgetTesto widgetTesto = (WidgetTesto) widget;
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetTesto\">\n");
            writer.print("                            <property name=\"collisioni\" value=\""
                    + widgetTesto.isCollisioni() + "\" />\n");
            if (widgetTesto.getHtmlToolbar() != null)
            {
                writer.print("                            <property name=\"htmlToolbar\" value=\""
                        + widgetTesto.getHtmlToolbar() + "\" />\n");
            }
            writer.print("                            <property name=\"multilinea\" value=\""
                    + widgetTesto.isMultilinea() + "\" />\n");
            if (widgetTesto.getRegex() != null)
            {
                writer.print("                            <property name=\"regex\"><value><![CDATA["
                        + widgetTesto.getRegex() + "]]></value></property>\n");
            }
            writer.print("                            <property name=\"dimensione\">\n");
            writer.print("                                <bean class=\"it.cilea.osd.jdyna.widget.Size\">\n");
            writer.print("                                    <property name=\"row\" value=\""
                    + widgetTesto.getDimensione().getRow() + "\" />\n");
            writer.print("                                    <property name=\"col\" value=\""
                    + widgetTesto.getDimensione().getCol() + "\" />\n");
            writer.print("                                </bean>\n");
            writer.print("                            </property>\n");
            writer.print("                        </bean>\n\n");
        }
        else if (widget instanceof WidgetDate)
        {
            WidgetDate widgetData = (WidgetDate) widget;
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetData\">\n");
            if (widgetData.getMaxYear() != null)
            {
                writer.print("                            <property name=\"maxYear\" value=\""
                        + widgetData.getMaxYear() + "\" />\n");
            }
            if (widgetData.getMinYear() != null)
            {
                writer.print("                            <property name=\"minYear\" value=\""
                        + widgetData.getMinYear() + "\" />\n");
            }
            writer.print("                            <property name=\"time\" value=\""
                    + widgetData.isTime() + "\" />\n");
            writer.print("                        </bean>\n\n");
        }
        else if (widget instanceof WidgetFormula)
        {
            if (widget instanceof WidgetFormulaNumero)
            {
                WidgetFormulaNumero widgetFormula = (WidgetFormulaNumero) widget;
                writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetFormulaNumero\">\n");
                writer.print("                            <property name=\"cifreDecimali\" value=\""
                        + widgetFormula.getCifreDecimali() + "\" />\n");
                writer.print("                            <property name=\"variabili\">\n"
                        + "                                  <list>\n");
                for (String variabile : widgetFormula.getVariabili())
                {

                    writer.print("                                       <value>"
                            + variabile + "</value>\n");

                }
                writer.print("                                  </list>\n");
                writer.print("                            </property>\n");
                writer.print("                            <property name=\"regolaDiRicalcolo\"><value><![CDATA["
                        + widgetFormula.getRegolaDiRicalcolo()
                        + "]]></value></property>\n");
                writer.print("                            <property name=\"expression\"><value><![CDATA["
                        + widgetFormula.getExpression()
                        + "]]></value></property>\n");

                writer.print("                        </bean>\n\n");

            }

            if (widget instanceof WidgetFormulaClassificazione)
            {
                WidgetFormulaClassificazione widgetFormula = (WidgetFormulaClassificazione) widget;
                writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetFormulaClassificazione\">\n");
                writer.print("                            <property name=\"variabili\">\n"
                        + "                                  <list>\n");
                for (String variabile : widgetFormula.getVariabili())
                {

                    writer.print("                                       <value>"
                            + variabile + "</value>\n");

                }
                writer.print("                                  </list>\n");
                writer.print("                            </property>\n");
                writer.print("                            <property name=\"regolaDiRicalcolo\"><value><![CDATA["
                        + widgetFormula.getRegolaDiRicalcolo()
                        + "]]></value></property>\n");
                writer.print("                            <property name=\"expression\"><value><![CDATA["
                        + widgetFormula.getExpression()
                        + "]]></value></property>\n");

                writer.print("                        </bean>\n\n");

            }

        }
        else if (widget instanceof WidgetEmail)
        {
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetEmail\" />\n");
        }
        else if (widget instanceof WidgetNumero)
        {
            WidgetNumero widgetNumero = (WidgetNumero) widget;
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetNumero\">\n");
            if (widgetNumero.getMax() != null)
            {
                writer.print("                            <property name=\"max\" value=\""
                        + widgetNumero.getMax() + "\" />\n");
            }
            if (widgetNumero.getMin() != null)
            {
                writer.print("                            <property name=\"min\" value=\""
                        + widgetNumero.getMin() + "\" />\n");
            }

            writer.print("                            <property name=\"cifreDecimali\" value=\""
                    + widgetNumero.getPrecisionDef() + "\" />\n");
            writer.print("                        </bean>\n\n");

        }
        else if (widget instanceof WidgetPointer)
        {
            WidgetPointer widgetPointer = (WidgetPointer) widget;
            writer.print("                        <bean class=\"it.cilea.osd.jdyna.widget.WidgetPointer\">\n");

            writer.print("                            <property name=\"display\"><value><![CDATA["
                    + widgetPointer.getDisplay() + "]]></value></property>\n");
            if (widgetPointer.getFiltro() != null)
            {
                writer.print("                            <property name=\"filtro\"><value><![CDATA["
                        + widgetPointer.getFiltro()
                        + "]]></value></property>\n");
            }
            writer.print("                            <property name=\"target\" value=\""
                    + widgetPointer.getTarget() + "\" />\n");
            writer.print("                        </bean>\n\n");
        }
        return;
    }

    /**
     * Metodo di supporto per esportare una tipologia di proprieta' nella sua
     * "bean definition".
     * 
     * @param writer
     * @param widget
     */
    // FIXME valutare la possibilita' di introdurre un'interfaccia
    // IExportableBeanDefinition
    private void toXML(PrintWriter writer, TP tp)
    {
        writer.print("    <bean id=\"" + tp.getShortName()
                + tpClass.getSimpleName() + "\" class=\""
                + tpClass.getCanonicalName() + "\">\n");
        writer.print("					<property name=\"shortName\" value=\""
                + tp.getShortName() + "\" />\n");
        writer.print("					<property name=\"newline\" value=\""
                + tp.isNewline() + "\" />\n");
        writer.print("					<property name=\"labelMinSize\" value=\""
                + tp.getLabelMinSize() + "\" />\n");
        writer.print("					<property name=\"fieldMinSize\">\n");
        writer.print("    <bean class=\"it.cilea.osd.jdyna.widget.Size\">\n");
        writer.print("					<property name=\"row\" value=\""
                + tp.getFieldMinSize().getRow() + "\" />\n");
        writer.print("					<property name=\"col\" value=\""
                + tp.getFieldMinSize().getCol() + "\" />\n");
        writer.print("    </bean>\n");
        writer.print("					</property>\n");
        writer.print("					<property name=\"newline\" value=\""
                + tp.isNewline() + "\" />\n");
        writer.print("					<property name=\"obbligatorieta\" value=\""
                + tp.isMandatory() + "\" />\n");
        writer.print("					<property name=\"ripetibile\" value=\""
                + tp.isRepeatable() + "\" />\n");
        writer.print("					<property name=\"showInList\" value=\""
                + tp.isShowInList() + "\" />\n");
        writer.print("					<property name=\"onCreation\" value=\""
                + tp.isOnCreation() + "\" />\n");
        writer.print("					<property name=\"label\" value=\"" + tp.getLabel()
                + "\" />\n");
        writer.print("					<property name=\"priorita\" value=\""
                + tp.getPriority() + "\" />\n");
        if (tp.getHelp() != null)
        {
            writer.print("					<property name=\"help\"><value><![CDATA["
                    + tp.getHelp() + "]]></value></property>\n");
        }
        writer.print("					<property name=\"simpleSearch\" value=\""
                + tp.isSimpleSearch() + "\" />\n");
        writer.print("					<property name=\"advancedSearch\" value=\""
                + tp.isAdvancedSearch() + "\" />\n");
        writer.print("					<property name=\"rendering\">\n");
        toXML(writer, tp.getRendering());
        writer.print("					</property>\n");

        // if (tp instanceof TipologiaProprietaSchedaCatalogo) {
        // TipologiaProprietaSchedaCatalogo tpScheda =
        // (TipologiaProprietaSchedaCatalogo) tp;
        // if (tpScheda.getRegolaPubblicazione() != null) {
        // writer
        // .print("					<property name=\"regolaPubblicazione\"><value><![CDATA["
        // + tpScheda.getRegolaPubblicazione()
        // + "]]></value></property>\n");
        // }
        // if (tpScheda.getRegolaParte() != null) {
        // writer
        // .print("					<property name=\"regolaParte\"><value><![CDATA["
        // + tpScheda.getRegolaParte()
        // + "]]></value></property>\n");
        // }
        // }
        //
        // else if (tp instanceof TipologiaProprietaSchedaOrdineStampa) {
        // TipologiaProprietaSchedaOrdineStampa tpOrdine =
        // (TipologiaProprietaSchedaOrdineStampa) tp;
        // if (tpOrdine.getRegolaPubblicazione() != null) {
        // writer
        // .print("					<property name=\"regolaPubblicazione\"><value><![CDATA["
        // + tpOrdine.getRegolaPubblicazione()
        // + "]]></value></property>\n");
        // }
        // }

        writer.print("    </bean>\n\n");
    }

    /**
     * Metodo di supporto per esportare un'area nella sua "bean definition". Le
     * tipologie di proprieta' mascherate sono referenziate localmente (bean
     * ref)
     * 
     * @param writer
     * @param area
     */
    private void toXML(PrintWriter writer, H area)
    {
        Class<H> areaClass = boxClass;
        writer.print("    <bean id=\"" + areaClass.getSimpleName()
                + area.getId() + "\" class=\"" + areaClass.getCanonicalName()
                + "\">\n");
        writer.print("					<property name=\"title\" value=\"" + area.getTitle()
                + "\" />\n");
        writer.print("					<property name=\"priorita\" value=\""
                + area.getPriority() + "\" />\n");

        writer.print("                            <property name=\"maschera\">\n"
                + "                                  <list>\n");
        for (Containable tip : area.getMask())
        {

            writer.print("                                       <ref local=\""
                    + tip.getShortName() + tpClass.getSimpleName() + "\" />\n");

        }
        writer.print("                                  </list>\n");
        writer.print("                            </property>\n");
        writer.print("    </bean>\n\n");
    }

    /**
     * Metodo di supporto per esportare una tipologia di oggetto nella sua "bean
     * definition". Le tipologie di proprieta' mascherate sono referenziate
     * localmente (bean ref)
     * 
     * @param writer
     * @param area
     */
    private void toXML(PrintWriter writer, TY type)
    {
        writer.print("    <bean id=\"" + typeClass.getSimpleName()
                + type.getId() + "\" class=\"" + typeClass.getCanonicalName()
                + "\">\n");

        // if (type instanceof TipologiaAllegato) {
        // TipologiaAllegato typeAllegato = (TipologiaAllegato) type;
        // writer
        // .print("			<constructor-arg type=\"java.lang.Integer\" value=\""
        // + typeAllegato.getParent_type() + "\" />\n");
        // } else if (type instanceof TipologiaProgetto) {
        // writer
        // .print("                            <property name=\"tipologiePubblAssoc\">\n"
        // + "                                  <list>\n");
        // for (AType tipologia :
        // ((TipologiaProgetto)type).getTipologiePubblAssoc()) {
        //
        // writer
        // .print("                                       <value>"
        // + tipologia.getNome()
        // + "</value>\n");
        //
        // }
        // writer.print("                                  </list>\n");
        // writer.print("                            </property>\n");
        //
        // }

        writer.print("					<property name=\"nome\" value=\""
                + type.getShortName() + "\" />\n");
        if (type.getLabel() != null)
        {
            writer.print("					<property name=\"descrizione\"><value><![CDATA["
                    + type.getLabel() + "]]></value></property>\n");
        }
        writer.print("                            <property name=\"maschera\">\n"
                + "                                  <list>\n");
        for (TP tip : type.getMask())
        {

            writer.print("                                       <ref local=\""
                    + tip.getShortName() + tpClass.getSimpleName() + "\" />\n");

        }
        writer.print("                                  </list>\n");
        writer.print("                            </property>\n");
        writer.print("    </bean>\n\n");
    }
}
