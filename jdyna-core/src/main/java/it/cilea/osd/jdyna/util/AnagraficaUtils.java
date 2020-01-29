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
package it.cilea.osd.jdyna.util;

import it.cilea.osd.jdyna.dto.AValoreDTOFactory;
import it.cilea.osd.jdyna.dto.AnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.AnagraficaObjectWithTypeDTO;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectWithTypeDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.editor.AdvancedPropertyEditorSupport;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.model.TypeSupport;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.beans.PropertyEditor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.LazyList;

/**
 * Classe di supporto per la gestione di oggetti dotati di anagrafica
 * 
 * @author bollini
 * 
 * @param <P>
 *            la classe delle proprieta' presenti nell'anagrafica dell'oggetto
 * @param <TP>
 *            la classe delle tipologie di proprieta' presenti nell'anagrafica
 *            dell'oggetto
 * @param <A>
 *            la classe delle area applicabili all'anagrafica dell'oggetto
 */
public class AnagraficaUtils
{

    private static IPersistenceDynaService applicationService;

    private Map<Class<? extends AValue>, PropertyEditor> valorePropertyEditors;

    public AnagraficaUtils(IPersistenceDynaService applicationService)
    {
        this.applicationService = applicationService;
    }

    public <P extends Property<TP>, TP extends PropertiesDefinition> void toXML(
            PrintWriter writer, AnagraficaSupport<P, TP> anagraficaObject)
    {
        List<P> anagrafica = anagraficaObject.getAnagrafica();
        if (anagrafica != null)
        {
            writer.print("          <property name=\"anagrafica\">\n");
            writer.print("              <list>\n");
            toXML(writer, anagraficaObject.getAnagrafica(),
                    anagraficaObject.getClassProperty());
            writer.print("              </list>\n");
            writer.print("          </property>\n");
        }
    }

    private <P extends Property<TP>, TP extends PropertiesDefinition> void toXML(
            PrintWriter writer, List<P> proprietaList, Class<P> proprietaClass)
    {
        for (P proprieta : proprietaList)
        {
            writer.print("                 <bean id=\""
                    + proprietaClass.getSimpleName() + proprieta.getId()
                    + "\" class=\"" + proprietaClass.getCanonicalName()
                    + "\">\n");
            writer.print("                     <property name=\"tipologia\" value=\""
                    + proprieta.getTypo().getShortName() + "\" />\n");
            writer.print("                     <property name=\"posizione\" value=\""
                    + proprieta.getPositionDef() + "\" />\n");

            Class valoreClass = proprieta.getTypo().getRendering()
                    .getValoreClass();
            writer.print("                     <property name=\"valore\">\n");
            writer.print("                     <bean class=\""
                    + valoreClass.getCanonicalName() + "\">\n");
            writer.print("                        <property name=\"real\">\n");

            PropertyEditor propertyEditor = valorePropertyEditors.get(proprieta
                    .getTypo().getRendering().getValoreClass());

            propertyEditor.setValue(proprieta.getValue());
            writer.print("                           <value><![CDATA["
                    + propertyEditor.getAsText() + "]]></value>\n");

            writer.print("                         </property>\n");
            writer.print("                     </bean>\n");
            writer.print("                     </property>\n");

            writer.print("                 </bean>\n");
        }
    }

    public void setValorePropertyEditors(
            Map<Class<? extends AValue>, PropertyEditor> valorePropertyEditors)
    {
        this.valorePropertyEditors = valorePropertyEditors;
    }

    /**
     * Fill DTO with properties get off by anagraficaSupport object, those are all property of the properties definition list passed as parameter.     
     * 
     * 
     * @param <P>
     * @param <TP>
     * @param dto
     * @param anagraficaSupport
     * @param tipologie
     * @param nestedObjects
     */
    public static <P extends Property<TP>, TP extends PropertiesDefinition> void fillDTO(
            IAnagraficaObjectDTO dto,
            AnagraficaSupport<P, TP> anagraficaSupport, List<TP> tipologie)
    {
        for (TP tipProprieta : tipologie)
        {
            dto.getAnagraficaProperties()
                    .put(tipProprieta.getShortName(),
                            LazyList.decorate(
                                    new LinkedList<ValoreDTO>(),
                                    new AValoreDTOFactory(tipProprieta
                                            .getRendering())));

        }

        if (anagraficaSupport != null)
        {
            for (TP tipProprieta : tipologie)
            {
                List<ValoreDTO> avalori = new LinkedList<ValoreDTO>();
                // List<Object> avalori = new LinkedList<Object>();
                for (P proprieta : anagraficaSupport
                        .getProprietaDellaTipologia(tipProprieta))
                {
                    ValoreDTO avaloredto = new ValoreDTO(proprieta.getValue().getObject(),
                            (proprieta.getVisibility() == 0) ? false : true);
                    avaloredto.setLock(proprieta.getLockDef() == 0 ? false : true);
                    avalori.add(avaloredto);

                }
                if (avalori.size() != 0)
                {
                    dto.getAnagraficaProperties()
                            .get(tipProprieta.getShortName()).clear();
                    dto.getAnagraficaProperties()
                            .get(tipProprieta.getShortName()).addAll(avalori);
                }
            }
        }

    }

    
    public static <P extends Property<TP>, TP extends PropertiesDefinition> void reverseDTO(
            IAnagraficaObjectDTO dto,
            AnagraficaSupport<P, TP> anagraficaSupport, List<TP> tipologie)
    {
        for (TP tipProprieta : tipologie)
        {
        	List<ValoreDTO> avaloriDTO = dto.getAnagraficaProperties().get(
                    tipProprieta.getShortName());
            // List<Object> avaloriDTO = anagraficaObjectDTO
            // .getAnagraficaProperties().get(tipProprieta.getShortName());
            List<P> proprieta = anagraficaSupport
                    .getProprietaDellaTipologia(tipProprieta);
            final int avaloriDTOsize;
            if (avaloriDTO == null)
            {
                avaloriDTOsize = 0;
            }
            else
            {

                int tmp = 0;
                for (ValoreDTO aval : avaloriDTO)
                {
                    if (aval != null && aval.getObject() != null)
                        tmp++;
                }
                avaloriDTOsize = tmp;

            }
            int propDaCreare = (avaloriDTOsize - proprieta.size() > 0) ? avaloriDTOsize
                    - proprieta.size()
                    : 0;
            int propDaEliminare = (proprieta.size() - avaloriDTOsize > 0) ? proprieta
                    .size() - avaloriDTOsize
                    : 0;
            for (int i = 0; i < propDaCreare; i++)
            {
                anagraficaSupport.createProprieta(tipProprieta);
            }
            for (int i = 0; i < propDaEliminare; i++)
            {
                // devo eliminare sempre l'ultima proprieta' perche' la lista e'
                // una referenza
                // alla lista mantenuta dalla cache a4v che viene alterata dal
                // removeProprieta
                anagraficaSupport.removeProprieta(proprieta.get(proprieta
                        .size() - 1));
            }

            avaloriDTO = dto.getAnagraficaProperties().get(tipProprieta.getShortName());
            proprieta = anagraficaSupport
                    .getProprietaDellaTipologia(tipProprieta);
            int i = 0;
            if (avaloriDTO != null)
                for (ValoreDTO valoreDTO : avaloriDTO)
                {
                    if (valoreDTO != null && valoreDTO.getObject() != null)
                    {
                        proprieta.get(i).getValue()
                                .setOggetto(valoreDTO.getObject());
                        proprieta.get(i).setVisibility(
                                valoreDTO.getVisibility() == false ? 0 : 1);
                        // proprieta.get(i).getValore().setOggetto(avaloriDTO.get(i));
                        i++;
                    }
                }
        }
         
       
    }

    
    public static <P extends Property<TP>, TP extends PropertiesDefinition> boolean checkIsAllNull(
            AnagraficaObjectDTO valore, List<TP> sottoTipologie)
    {
        boolean isAllNull = true;
        for (TP tp : sottoTipologie)
        {
            for (Object subvalore : valore.getAnagraficaProperties().get(
                    tp.getShortName()))
            {

                if (subvalore != null)
                {
                    return false;
                }

            }
        }
        return isAllNull;
    }
    
    public static <P extends Property<TP>, TP extends PropertiesDefinition> boolean checkIsVisible(
            AnagraficaObjectDTO valore, List<TP> sottoTipologie)
    {
        boolean isVisible = false;
        for (TP tp : sottoTipologie)
        {
            for (ValoreDTO subvalore : valore.getAnagraficaProperties().get(
                    tp.getShortName()))
            {

                if (subvalore != null && subvalore.getVisibility())
                {
                    return true;                    
                }

            }
        }
        return isVisible;
    }
    
    
    // /**
    // * Cerca, nella lista passata come secondo argomento, la tipologia di
    // * proprieta' che referenzia la combo in cui e' contenuta la TP figlia.
    // *
    // * @param child
    // * la tipologia di proprieta' figlia (i.e. isTopLevel() == false)
    // * @param allTP
    // * la lista di tutte le tipologie di proprieta' in cui cercare
    // * @return la tipologia di proprieta' padre o null se non e' presente
    // nella
    // * lista
    // * @throws IllegalArgumentException
    // * se la tipologia di proprieta' indicata come figlia e' topLevel
    // */
    // public <TP extends PropertiesDefinition> TP
    // findTipologiaProprietaParent(TP child, List<TP> allTP){
    // return null;
    // }

    /**
     * Importa sull'oggetto anagraficaObject passato come parametro i dati
     * contenuti nell'importBean.
     * 
     * @param anagraficaObject
     *            - l'oggetto su cui importare i dati e metadati contenuti nel
     *            importBean passato come parametro
     * @param importBean
     *            {@see ImportPropertyAnagraficaUtil}
     * @return
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition, AV extends AValue> void importProprieta(
            AnagraficaSupport<P, TP> anagraficaObject,
            ImportPropertyAnagraficaUtil importBean)
    {

        String shortName = importBean.getShortname();
        Object oggetto = importBean.getValore();

        // recupero da db la tipologia di proprieta
        TP tipologiaDaImportare = (TP) applicationService
                .findPropertiesDefinitionByShortName(
                        anagraficaObject.getClassPropertiesDefinition(),
                        shortName);

        if (tipologiaDaImportare == null)
        {
            throw new IllegalArgumentException("Lo shortname indicato: "
                    + shortName
                    + " non corrisponde a nessuna TP della classe "
                    + anagraficaObject.getClassPropertiesDefinition()
                            .getCanonicalName());
        }
        // recupero dal widget il property editor per l'import
        PropertyEditor pe = tipologiaDaImportare.getRendering()
                .getImportPropertyEditor(applicationService, AdvancedPropertyEditorSupport.MODE_VIEW);

        P proprieta = null;

        if (oggetto instanceof String)
        {
            pe.setAsText((String) oggetto);
            proprieta = anagraficaObject.createProprieta(tipologiaDaImportare);
            AV valore = (AV) proprieta.getValue();
            valore.setOggetto(pe.getValue());
            proprieta.setValue(valore);
        }

        if (oggetto instanceof List)
        {
            List lista = (List) oggetto;

            for (int w = 0; w < lista.size(); w++)
            {
                Object elementList = lista.get(w);

                if (elementList instanceof String)
                {
                    proprieta = anagraficaObject
                            .createProprieta(tipologiaDaImportare);
                    pe.setAsText((String) elementList);
                    AV valore = (AV) proprieta.getValue();
                    valore.setOggetto(pe.getValue());
                    // proprieta.setValore(valore);
                }
                else
                {
                    // e' una combo...
                    proprieta = anagraficaObject
                            .createProprieta(tipologiaDaImportare);
                    importSottoProprieta((AnagraficaObjectDTO) proprieta
                            .getValue().getObject(),
                            (ImportPropertyAnagraficaUtil) elementList,
                            anagraficaObject.getClassPropertiesDefinition());
                }
            }

        }

    }

    /**
     * Metodo interno che importa i dati delle combo.
     * 
     * @param proprietaParent
     *            - la proprieta parent della combo
     * @param anagraficaObject
     * @param importBean
     */
    private <P extends Property<TP>, TP extends PropertiesDefinition> void importSottoProprieta(
            AnagraficaObjectDTO anagraficaObject,
            ImportPropertyAnagraficaUtil importBean, Class<TP> clazzTP)
    {

        String shortName = importBean.getShortname();
        Object oggetto = importBean.getValore();

        TP tipologiaDaImportareInCombo = (TP) applicationService
                .findPropertiesDefinitionByShortName(clazzTP, shortName);
        PropertyEditor pe = tipologiaDaImportareInCombo.getRendering()
                .getImportPropertyEditor(applicationService, AdvancedPropertyEditorSupport.MODE_VIEW);

        P proprieta = null;

        // caso base
        if (oggetto instanceof String)
        {
            pe.setAsText((String) oggetto);
            ArrayList<ValoreDTO> arraylist;
            if (anagraficaObject.getAnagraficaProperties()
                    .get(tipologiaDaImportareInCombo).isEmpty())
            {
                arraylist = new ArrayList<ValoreDTO>();
                arraylist.add(new ValoreDTO(pe.getValue()));
                anagraficaObject.getAnagraficaProperties().put(
                        tipologiaDaImportareInCombo.getShortName(), arraylist);
            }
            else
            {
                anagraficaObject.getAnagraficaProperties()
                        .get(tipologiaDaImportareInCombo)
                        .add(new ValoreDTO(pe.getValue()));
            }

            // proprieta.setValore(valore);
        }
        if (oggetto instanceof List)
        {
            List lista = (List) oggetto;

            for (int w = 0; w < lista.size(); w++)
            {                

                pe.setAsText((String) oggetto);
                ArrayList<ValoreDTO> arraylist;
                if (anagraficaObject.getAnagraficaProperties()
                        .get(tipologiaDaImportareInCombo).isEmpty())
                {
                    arraylist = new ArrayList<ValoreDTO>();
                    arraylist.add(new ValoreDTO(pe.getValue()));
                    anagraficaObject.getAnagraficaProperties().put(
                            tipologiaDaImportareInCombo.getShortName(),
                            arraylist);
                }
                else
                {
                    anagraficaObject.getAnagraficaProperties()
                            .get(tipologiaDaImportareInCombo)
                            .add(new ValoreDTO(pe.getValue()));
                }
            }

        }
    }

    /**
     * Iterate on list passed as parameter and get first value string from
     * anagrafica.
     * 
     * @param anagrafica
     * @param listMetadataShortname
     * @return
     */
    public static <P extends Property<TP>, TP extends PropertiesDefinition> Map<String, AValue<?>> getFirstSingleValue(
            AnagraficaSupport<P, TP> anagrafica, String[] listMetadataShortname)
    {
        Map<String, AValue<?>> result = new HashMap<String, AValue<?>>();
        for (String shortname : listMetadataShortname)
        {
            List<P> pp = anagrafica.getAnagrafica4view().get(shortname);
            if (pp != null && !pp.isEmpty())
            {
                result.put(shortname, pp.get(0).getValue());
                return result;
            }
        }
        return null;
    }

}
