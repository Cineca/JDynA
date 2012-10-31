package it.cilea.osd.jdyna.web.tag;
import javax.servlet.jsp.el.ELException;

public class DisplayPointerTagLibrary
{

    
    public static Object evaluate(Object var, String expression) {
        ExpressionEvaluatorImpl eei = new ExpressionEvaluatorImpl(var);
        Object resolvedText = null;
        try
        {
             resolvedText = eei.evaluate(expression, String.class);
        }
        catch (ELException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return resolvedText;
    }
}
