package it.cilea.osd.jdyna.web.tag;

import java.io.Reader;
import java.io.StringReader;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.commons.el.Coercions;
import org.apache.commons.el.Constants;
import org.apache.commons.el.Expression;
import org.apache.commons.el.ExpressionString;
import org.apache.commons.el.Logger;
import org.apache.commons.el.parser.ELParser;
import org.apache.commons.el.parser.ParseException;
import org.apache.commons.el.parser.TokenMgrError;

public class ExpressionEvaluatorImpl extends ExpressionEvaluator
{

    private Object object;

    private Logger pLogger;

    private VariableResolver variableResolver = new VariableResolver()
    {

        @Override
        public Object resolveVariable(String arg0) throws ELException
        {
            if ("displayObject".equals(arg0))
            {
                return object;
            }
            return null;
        }
    };

    // -------------------------------------
    /**
     * 
     * Constructor
     **/
    public ExpressionEvaluatorImpl()
    {
    }

    public ExpressionEvaluatorImpl(Object object)
    {
        this.object = object;
    }

    // -------------------------------------
    /**
     * 
     * Evaluates the given expression String
     * 
     * @param pExpressionString
     *            The expression to be evaluated.
     * @param pExpectedType
     *            The expected type of the result of the evaluation
     * @param pResolver
     *            A VariableResolver instance that can be used at runtime to
     *            resolve the name of implicit objects into Objects.
     * @param functions
     *            A FunctionMapper to resolve functions found in the expression.
     *            It can be null, in which case no functions are supported for
     *            this invocation.
     * @return the expression String evaluated to the given expected type
     **/
    public Object evaluate(String pExpressionString, Class pExpectedType,
            VariableResolver pResolver, FunctionMapper functions)
            throws ELException
    {
        // Check for null expression strings
        if (pExpressionString == null)
        {
            throw new ELException(Constants.NULL_EXPRESSION_STRING);
        }

        // Get the parsed version of the expression string
        Object parsedValue = parseExpressionString(pExpressionString);

        // Evaluate differently based on the parsed type
        if (parsedValue instanceof String)
        {
            // Convert the String, and cache the conversion
            String strValue = (String) parsedValue;
            return convertStaticValueToExpectedType(strValue, pExpectedType,
                    pLogger);
        }

        else if (parsedValue instanceof Expression)
        {
            // Evaluate the expression and convert
            Object value = ((Expression) parsedValue).evaluate(pResolver,
                    functions, pLogger);
            return convertToExpectedType(value, pExpectedType, pLogger);
        }

        else if (parsedValue instanceof ExpressionString)
        {
            // Evaluate the expression/string list and convert
            String strValue = ((ExpressionString) parsedValue).evaluate(
                    pResolver, functions, pLogger);
            return convertToExpectedType(strValue, pExpectedType, pLogger);
        }

        else
        {
            // This should never be reached
            return null;
        }
    }

    // -------------------------------------
    /**
     * 
     * Gets the parsed form of the given expression string. If the parsed form
     * is cached (and caching is not bypassed), return the cached form,
     * otherwise parse and cache the value. Returns either a String, Expression,
     * or ExpressionString.
     **/
    public Object parseExpressionString(String pExpressionString)
            throws ELException
    {
        // See if it's an empty String
        if (pExpressionString.length() == 0)
        {
            return "";
        }

        // See if it's in the cache
        Object ret = null;

        if (ret == null)
        {
            // Parse the expression
            Reader r = new StringReader(pExpressionString);
            ELParser parser = new ELParser(r);
            try
            {
                ret = parser.ExpressionString();

            }
            catch (ParseException exc)
            {
                throw new ELException(exc);
            }
            catch (TokenMgrError exc)
            {
                // Note - this should never be reached, since the parser is
                // constructed to tokenize any input (illegal inputs get
                // parsed to <BADLY_ESCAPED_STRING_LITERAL> or
                // <ILLEGAL_CHARACTER>
                throw new ELException(exc.getMessage());
            }
        }
        return ret;
    }

    // -------------------------------------
    /**
     * 
     * Converts the given value to the specified expected type.
     **/
    Object convertToExpectedType(Object pValue, Class pExpectedType,
            Logger pLogger) throws ELException
    {
        return Coercions.coerce(pValue, pExpectedType, pLogger);
    }

    // -------------------------------------
    /**
     * 
     * Converts the given String, specified as a static expression string, to
     * the given expected type. The conversion is cached.
     **/
    Object convertStaticValueToExpectedType(String pValue, Class pExpectedType,
            Logger pLogger) throws ELException
    {
        // See if the value is already of the expected type
        if (pExpectedType == String.class || pExpectedType == Object.class)
        {
            return pValue;
        }

        // Convert from a String
        Object ret = Coercions.coerce(pValue, pExpectedType, pLogger);

        return ret;

    }

    @Override
    public javax.servlet.jsp.el.Expression parseExpression(String expression, 
            Class expectedType, 
            FunctionMapper fMapper) throws ELException
    {       
        parseExpressionString(expression);
        
        // Create an Expression object that knows how to evaluate this.
        return new CustomExpression(this, expression, expectedType, fMapper);
    }

    public Object evaluate(String pExpressionString, Class pExpectedType)
            throws ELException
    {
        return evaluate(pExpressionString, pExpectedType,
                this.variableResolver, null);
    }

    // -------------------------------------
    
    /**
     * An object that encapsulates an expression to be evaluated by 
     * the evaluator.
     */
    private class CustomExpression
      extends javax.servlet.jsp.el.Expression
    {
      private ExpressionEvaluatorImpl evaluator;
      private String expression;
      private Class expectedType;
      private FunctionMapper fMapper;

      public CustomExpression(ExpressionEvaluatorImpl evaluator, String expression,
                Class expectedType, FunctionMapper fMapper)
      {
        this.evaluator = evaluator;
        this.expression = expression;
        this.expectedType = expectedType;
        this.fMapper = fMapper;
      }
          
      public Object evaluate( VariableResolver vResolver )
        throws ELException
      {
        return evaluator.evaluate(this.expression,
                  this.expectedType,
                  vResolver,
                  this.fMapper);
      }
    }


}