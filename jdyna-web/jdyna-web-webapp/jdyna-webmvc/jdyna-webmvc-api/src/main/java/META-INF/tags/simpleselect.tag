<%-- 

	This tag library is based on tutorial by Ted Bergeron.
	Tutorial link:
	http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html

--%>
<%@ attribute name="parameter" required="true" %>
<%@ attribute name="collection" required="true" type="java.util.Collection" %>
<%@ attribute name="size" required="false" type="java.lang.Integer" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="labelKey" required="false" %>
<%@ attribute name="labelSeperator" required="false" %>
<%@ attribute name="fieldSeperator" required="false" %>
<%@ attribute name="value" required="false" %>
<%@ attribute name="onchange" required="false" %>



<%--per abilitare/disabilitare la select --%>
<%@ attribute name="stato" required="false" %> 


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="jdynatags" prefix="dyna"%>


<%-- Attributo personalizzato --%>
<c:if test="${stato == 'disabled'}">
	<c:set var="stato" value="disabled" />
</c:if>


<c:if test="${labelSeperator == null}">
    <c:set var="labelSeperator" value="td"/>
</c:if>

<c:if test="${fieldSeperator == null}">
    <c:set var="fieldSeperator" value="td"/>
</c:if>



<c:choose>
    <c:when test="${required == null || required == false}">
        <c:set var="labelClass" value="optional"/>
    </c:when>
    <c:otherwise>
        <c:set var="labelClass" value="required"/>
    </c:otherwise>
</c:choose>

<c:if test="${size == null}">
    <c:set var="size" value="1"/>
</c:if>

<c:choose>
    <c:when test="${labelKey == null}">
        <${labelSeperator}><label for="${parameter}" class="${labelClass}"><fmt:message key="prompt.${parameter}"/></label></${labelSeperator}>
    </c:when>
    <c:when test="${labelKey == 'none'}">
            <c:if test="${!labelSeperator == 'none'}">
                <${labelSeperator}></${labelSeperator}>
            </c:if>
        </c:when>
    <c:otherwise>
        <${labelSeperator}><label for="${parameter}" class="${labelClass}"><fmt:message key="${labelKey}"/></label></${labelSeperator}>
    </c:otherwise>
</c:choose>
<${fieldSeperator}>
<select
  <c:if test="${stato == 'disabled'}"> disabled="disabled"</c:if> 
  name="${parameter}" id="${parameter}" size="${size}"<c:if test="${onchange != null}"> onchange="${onchange}"</c:if>>

    <c:if test="${collection != null}">
     <option value="scegli">...scegli opzione...</option> 	  
	    <c:forEach var="item" items="${collection}" varStatus="loop">	    	
	    	<c:catch var="eccezione">    	
	            <option value="${item.identifyingValue}"<c:if test="${value != null && item.identifyingValue == value}"> selected="selected"</c:if>>${item.displayValue}</option>     	
          	</c:catch>
          	<c:if test="${eccezione != null}">
            	<option value="${item}"<c:if test="${value != null && item == value}"> selected="selected"</c:if>>${item}</option>
            </c:if>
        </c:forEach>
    </c:if>
</select>
<jsp:doBody/>
</${fieldSeperator}>

<%-- TODO: Do we want to insert a Select a value/null entry here, or rely on the passed collection containing that?  Use NotNull annotation for required="true" --%>

