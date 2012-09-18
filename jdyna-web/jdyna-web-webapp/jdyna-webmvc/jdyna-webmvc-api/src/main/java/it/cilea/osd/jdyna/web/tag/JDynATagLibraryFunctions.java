package it.cilea.osd.jdyna.web.tag;

import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.utils.HashUtil;
import it.cilea.osd.jdyna.value.PointerValue;
import it.cilea.osd.jdyna.widget.WidgetPointer;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class JDynATagLibraryFunctions
{

    private static final String PROPERTY_SEPERATOR = ".";

    private static Log log = LogFactory.getLog(JDynATagLibraryFunctions.class);

    public static String escapeHTMLtoJavascript(String html)
    {
        if (html == null)
            return null;

        String jsString = html.replaceAll("&", "&amp;");
        jsString = html.replaceAll("<", "&lt;");
        jsString = jsString.replaceAll(">", "&gt;");
        jsString = jsString.replaceAll("\"", "&#034;");
        jsString = jsString.replaceAll("'", " &#039;");
        jsString = jsString.replaceAll("\n", "");
        jsString = jsString.replaceAll("\r", "");
        return jsString;
    }

    public static String escapeApici(String html)
    {
        if (html == null)
            return null;

        String jsString = html.replaceAll("\'", "\\\\'");
        return jsString;
    }

    public static String replaceApiciDoppi(String display)
    {
        String result = "";
        result = display.replaceAll("\"", "'");
        return result;
    }

    public static String getDisplayValue(Object obj, String display)
    {
        if (obj == null)
        {
            return "";
        }
        String result;
        try
        {
            if (obj instanceof ValoreDTO)
            {
                ValoreDTO valoreDTO = (ValoreDTO) obj;
                if (valoreDTO.getObject() != null)
                {
                    result = FormulaManager.calcoloValore(display,
                            valoreDTO.getObject(), null, 0).toString();
                }
                else
                {
                    return "";
                }
            }
            else
            {
                result = FormulaManager.calcoloValore(display, obj, null, 0)
                        .toString();
            }
        }
        catch (RuntimeException e)
        {
            result = "";
        }
        return result;
    }

    public static String getTargetClass(WidgetPointer wpointer)
    {
        PointerValue vp;
        try
        {
            vp = wpointer.getTargetValoreClass().newInstance();
            return vp.getTargetClass().getName();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Object getReferencedObject(Object root, String propertyPath)
            throws OgnlException
    {

        /*
         * considero anche il caso in cui le stringhe mi arrivino gia' con gli
         * apici
         */
        String pattern = "\\[([^\\]']*[^'0-9\\]]{1,}[^\\]']*[^'])\\]";
        String propertyPathWithOutRoot = propertyPath.substring(
                propertyPath.indexOf(".") + 1).replaceAll(pattern, "['$1']");

        if (root == null)
        {
            return "";
        }
        Object result;
        try
        {
            if (root instanceof ValoreDTO)
            {
                ValoreDTO valoreDTO = (ValoreDTO) root;
                if (valoreDTO.getObject() != null)
                {
                    result = Ognl.getValue(propertyPathWithOutRoot,
                            valoreDTO.getObject());
                }
                else
                {
                    return "";
                }
            }
            else
            {
                result = Ognl.getValue(propertyPathWithOutRoot, root);
            }
        }
        catch (RuntimeException e)
        {
            result = "";
        }
        return result;
    }

    /**
     * Return the ID of the subject from the "soggetto" string id:value
     * 
     * @param inputValue
     * @return
     */
    public static String getSubject(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split(":", 2);
            return result[0];
        }
        else
        {
            return "";
        }
    }

    /**
     * Return the value in the subject from the "soggetto" string id:value
     * 
     * @param inputValue
     * @return
     */
    public static String getSubjectValue(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split(":", 2);
            return result.length > 1 ? result[1] : "";
        }
        else
        {
            return "";
        }
    }

    /**
     * Restituisce la stringa corrispondente al valore di object passato come
     * argomento utilizzando il property editor associato alla tipologia di
     * proprieta' passata come primo argomento
     * 
     * @param tp
     * @param object
     * @return
     */
    public static String display(PropertiesDefinition tp, Object object)
    {
        if (object == null)
        {
            return "";
        }
        // Passiamo un application service null ma il property editor lo usa
        // solo per il passaggio da text -> object e non viceversa
        PropertyEditor propertyEditor = tp.getRendering().getPropertyEditor(
                null);
        propertyEditor.setValue(object);
        return propertyEditor.getAsText();
    }

    /**
     * Estrae da una collection i suoi elementi, restituendo una stringa con gli
     * elementi separati da una virgola
     * 
     * @param parameters
     *            : la collection contenente gli elementi per creare la stringa
     * @return
     */
    public static String extractParameters(Collection<String> parameters)
    {
        String result = "";
        if (parameters != null)
        {
            int size = parameters.size();
            int i = 0;
            for (String parameter : parameters)
            {
                result += "'" + parameter + "'";
                if (i < size - 1)
                {
                    result += ",";
                }
                i++;
            }
        }
        return result;
    }

    public static String md5(String text)
    {
        return HashUtil.hashMD5(text);
    }

    /**
     * Restituisce la stinga di ricerca da utilizzare per le collisioni,
     * spezzettando l'input utente per token in AND aggiungendo la tilde della
     * ricerca fuzzy
     * 
     * @param inputValue
     * @return
     * @throws IOException
     */
    public static String getCollisioniQuery(String inputValue)
            throws IOException
    {
        StringBuffer inputToken = new StringBuffer();
        Tokenizer tokenizer = new StandardTokenizer(
                new StringReader(inputValue));
        Token token = tokenizer.next();
        while (token != null)
        {
            inputToken.append(new String(token.termBuffer(), 0, token
                    .termLength()));
            inputToken.append("~");
            token = tokenizer.next();
        }
        return inputToken.toString();
    }

    public static String getObjectFromPropertyPath(String propertyPath)
    {
        // Path is "customer.contact.fax.number", need to return
        // "customer.contact.fax"
        log.debug("propertyPath = " + propertyPath);
        int lastSeperatorPosition = propertyPath
                .lastIndexOf(PROPERTY_SEPERATOR);
        String objectString = propertyPath.substring(0, lastSeperatorPosition);
        log.debug("objectString = " + objectString);
        return objectString;
    }

    public static String getPropertyFromPropertyPath(String propertyPath)
    {
        // Path is "customer.contact.fax.number", need to return "number"
        log.debug("propertyPath = " + propertyPath);
        int lastSeperatorPosition = propertyPath
                .lastIndexOf(PROPERTY_SEPERATOR);
        String propertyString = propertyPath
                .substring(lastSeperatorPosition + 1);
        log.debug("propertyString = " + propertyString);

        // se termina con Id lo rimuovo
        if (propertyString.length() > 2)
        {
            if (propertyString.charAt(propertyString.length() - 1) == 'd'
                    && propertyString.charAt(propertyString.length() - 2) == 'I')
            {
                propertyString = propertyString.substring(0,
                        propertyString.length() - 2);
            }
        }

        return propertyString;
    }

    public static String getPropertyPathNoIdFromPropertyPathId(
            String propertyPathId)
    {
        // Path is "customer.contact.fax.number", need to return "number"
        log.debug("propertyPath = " + propertyPathId);

        // controllo se la property finisce con Id
        if (propertyPathId.length() > 2)
        {
            if (propertyPathId.charAt(propertyPathId.length() - 1) == 'd'
                    && propertyPathId.charAt(propertyPathId.length() - 2) == 'I')
            {
                propertyPathId = propertyPathId.substring(0,
                        propertyPathId.length() - 2);
            }
        }
        propertyPathId = propertyPathId + ".displayValue";
        return propertyPathId;
    }

    public static boolean isContained(Object object, Set list)
    {
        return list == null ? false : list.contains(object);
    }

    public static boolean isContainedList(Object object, List list)
    {
        return list == null ? false : list.contains(object);
    }

    public static boolean emptyMap(Map map)
    {
        if (map == null)
            return true;
        for (Object obj : map.values())
        {
            if ((obj != null && obj instanceof Collection && ((Collection) obj)
                    .size() > 0)
                    || (obj != null && !(obj instanceof Collection)))
                return false;
        }
        return true;
    }

    public static String nl2br(String stringa)
    {
        String strigaRisultato = new String(stringa);
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = CRLF.matcher(stringa);

        if (m.find())
        {
            strigaRisultato = m.replaceAll("<br/>");
        }
        return strigaRisultato;
    }

    /**
     * Determina se il parametro superclasse passato come stringa e' un
     * superclasse di object; Nota chiama internamente
     * {@link Class#isAssignableFrom(Class)}
     * 
     * <code> 
     * if(superclass.isAssignableFrom(object.getClass())) {
     * 	 return true;
     * }
     * </code>
     * 
     * @param object
     * @param superClassName
     * @return true se object1 se e' sottoclasse di superclass
     * @throws ClassNotFoundException
     */
    public static boolean instanceOf(Object object, String superClassName)
            throws ClassNotFoundException
    {
        Class classe = Class.forName(superClassName);
        if (classe.isAssignableFrom(object.getClass()))
        {
            return true;
        }
        return false;
    }

    /**
     * Return the description from the "link" string description|||target
     * 
     * @param inputValue
     * @return
     */
    public static String getLinkDescription(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split("\\|\\|\\|", 2);
            return result[0];
        }
        else
        {
            return "";
        }
    }

    /**
     * Return the target from the "link" string description|||target
     * 
     * @param inputValue
     * @return
     */
    public static String getLinkValue(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split("\\|\\|\\|", 2);
            return result.length > 1 ? result[1] : "";
        }
        else
        {
            return "";
        }
    }

 
    /**
     * Sort a list (make attention that parameter list elements must be
     * implements Comparable)
     * 
     * @param list
     * @return
     */
    public static List sortList(List list)
    {
        Collections.sort(list);
        return list;
    }

    /**
     * Return the information for isDeleteFile checkbox from the "file" string
     * existOnServer|||valueFile|||ext|||mime|||deleteOnServer
     * 
     * @param inputValue
     * @return
     */
    public static boolean getFileIsOnServer(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split("\\|\\|\\|", 0);
            return result.length > 0 ? Boolean.parseBoolean(result[0]) : false;
        }
        else
        {
            return false;
        }
    }

    /**
     * Return the target from the "file" string
     * existOnServer|||valueFile|||ext|||mime|||deleteOnServer
     * 
     * @param inputValue
     * @return
     */
    public static String getFileName(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split("\\|\\|\\|");
            return result.length > 4 ? result[3]
                    + ((result[4] != null && !result[4].isEmpty()) ? "."
                            + result[4] : "") : "";
        }
        else
        {
            return "";
        }
    }

    /**
     * Return the target from the "file" string
     * existOnServer|||basePath|||servletPath
     * |||valueFile|||ext|||mime|||folderFile
     * 
     * @param inputValue
     * @return
     */
    public static String getFileFolder(String inputValue)
    {
        if (inputValue != null)
        {
            String[] result = inputValue.split("\\|\\|\\|");
            return result.length > 5 ? result[6] + "/" : "";
        }
        else
        {
            return "";
        }
    }
}
