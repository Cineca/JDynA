<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>

<%@ attribute name="onchange" required="false"%>
<%@ attribute name="onclick" required="false"%>
<%-- eventi js non gestiti 

<%@ attribute name="onblur" required="false"%> 
<%@ attribute name="ondblclick" required="false"%>
<%@ attribute name="onkeydown" required="false"%>
<%@ attribute name="onkeyup" required="false"%>
<%@ attribute name="onkeypress" required="false"%>
<%@ attribute name="onfocus" required="false"%>
<%@ attribute name="onhelp" required="false"%>
<%@ attribute name="onselect" required="false"%>
<%@ attribute name="onmouseup" required="false"%>
<%@ attribute name="onmousedown" required="false"%>
<%@ attribute name="onmouseout" required="false"%>
<%@ attribute name="onmousemove" required="false"%>
<%@ attribute name="onmouseover" required="false"%>
--%>

<%@ taglib uri="jdynatags" prefix="dyna" %>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:if test="${label != null || labelKey != null}">
	<dyna:label label="${label}" labelKey="${labelKey}" 
		help="${help}" helpKey="${helpKey}" 
		propertyPath="${propertyPath}" required="${required}"
		
		 />
		 <c:if test='${!empty helpKey || !empty help}'>
		 <div class="dynaFieldValue" >
		 </c:if>
</c:if>

<%-- FIXME: CODICE COMUNE A TUTTI I TAG... --%>

<c:set var="objectPath" value="${dyna:getObjectPath(propertyPath)}" />

<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}" />
	<c:if test="${object == null}">
		<%-- Bind ignores the command object prefix, so simple properties of the command object return null above. --%>
		<c:set var="object" value="${commandObject}" />
		<%-- We depend on the controller adding this to request. --%>
	</c:if>
</spring:bind>

<c:set var="propertyName" value="${dyna:getPropertyName(propertyPath)}" />
<%-- FINE CODICE COMUNE A TUTTI I TAG... --%>

<spring:bind path="${propertyPath}">
	<c:set var="values" value="${status.value}" />
</spring:bind>
<c:set var="checked" value="" />


<c:catch var="exNoIndexedValue">
<c:forEach var="value" items="${values}" varStatus="iterationStatus">	
	<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
		
		<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
		<c:set var="inputShowed" value="true" />
		<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
		<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
		<c:if test="${inputValue}">
			<c:set var="checked" value="checked=\"checked\"" />
		</c:if>
		
		<input id="_${inputName}" name="_${inputName}" type="hidden" />
		
		<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
		<c:set var="functionValidation" value="" />
		<c:if test="${!empty ajaxValidation}">
			<c:set var="functionValidation" value="${ajaxValidation}('${inputName}'${!empty parametersValidation?',':''}${!empty parametersValidation?parametersValidation:''});" />
		</c:if>
		<c:set var="onchangeJS" value="cambiaBoolean('${inputName}');${functionValidation};${onchange}" />
		<input id="${inputName}" name="${inputName}" type="hidden" value="${empty inputValue?'false':inputValue}"  />
		<input id="check${inputName}" type="checkbox" value="${inputValue}" ${checked} <dyna:javascriptEvents onchange="${onchangeJS}"/> <dyna:javascriptEvents onclick="${onclick}"/>/>
	</spring:bind>	
	<dyna:validation propertyPath="${propertyPath}[${iterationStatus.count - 1}]" />
</c:forEach>
</c:catch>
<c:if test="${!inputShowed}">
	<c:if test="${exNoIndexedValue == null}">	
	<%-- Se sono qui l'inputValue è per forza vuoto (altrimenti avrei avuto una lista di 1 elemento) --%>
		<c:catch var="exNoIndexedValue">
			<spring:bind path="${propertyPath}[0]">			
				<c:set var="inputValue" ><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
				<c:if test="${inputValue}">
					<c:set var="checked" value="checked=\"checked\"" />
				</c:if>
			</spring:bind>
		</c:catch>
		<c:set var="validation" value="${propertyPath}[0]" />
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
		<spring:bind path="${propertyPath}">
			<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
			<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
			<c:if test="${inputValue}">
				<c:set var="checked" value="checked=\"checked\"" />
			</c:if>
		</spring:bind>
		<c:set var="validation" value="${propertyPath}" />
	</c:if>
	
		<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
		<c:set var="functionValidation" value="" />
		<c:if test="${!empty ajaxValidation}">
			<c:set var="functionValidation" value="${ajaxValidation}('${inputName}'${!empty parametersValidation?',':''}${!empty parametersValidation?parametersValidation:''});" />
		</c:if>
		<c:set var="onchangeJS" value="cambiaBoolean('${inputName}');${functionValidation};${onchange}" />
		<input id="_${inputName}" name="_${inputName}" type="hidden"  />
		<input id="${inputName}" name="${inputName}" type="hidden" value="${empty inputValue?'false':inputValue}"  />
		<input id="check${inputName}" type="checkbox" value="${inputValue}" ${checked} <dyna:javascriptEvents onchange="${onchangeJS}"/> <dyna:javascriptEvents onclick="${onclick}"/>/>
		<dyna:validation propertyPath="${validation}" />
</c:if>
 <c:if test='${!empty helpKey || !empty help}'>
		
</div> </div>
</c:if>