<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="labelMinSize" required="false" type="java.lang.Integer" %>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<!-- If there is a tipologia attribute we need to override any other attribute with the info inside it -->
<c:if test="${tipologia != null}">
	<c:set var="help" value="${tipologia.help}" />
	<c:set var="label" value="${tipologia.label}" />	
</c:if>

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

<c:choose>
	<c:when
		test="${required == null || required == false}">
		<c:set var="labelClass" value="optional" />
		<c:set var="labelAfter" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="labelClass" value="required" />
		<c:set var="labelAfter">
			<span class="required"><fmt:message key="prompt.required" /></span>
		</c:set>
	</c:otherwise>
</c:choose>

<c:set var="labelStyle" value="" />
<c:if test="${labelMinSize > 1}">
	<c:set var="labelMinWidth" value="min-width:${labelMinSize}em;" />
</c:if>
<c:if test="${!empty labelMinWidth}">
	<c:set var="labelStyle" value="style=\"${labelMinWidth}\"" />
</c:if>

<spring:bind path="${propertyPath}">
	<c:if test="${empty help && empty helpKey}">
	
	<span id="labelSpan${status.expression}" ${labelStyle} class="dynaLabel">
	<label for="${status.expression}" class="${labelClass}">
	<c:choose>
		<c:when test="${!empty label}">${label}</c:when>
		<c:otherwise><fmt:message key="${labelKey}" /></c:otherwise>
	</c:choose>
    <c:out value="${labelAfter}" escapeXml="false" />
	</label>
	</span>
	</c:if>
	<c:if test="${!empty help || !empty helpKey}">
		<c:choose>
			<c:when test="${!empty help}">
			<%-- <c:set var="helpHTML" value="${dyna:escapeHTMLtoJavascript(help)}" /> --%>
			<c:set var="helpHTML"><c:out value="${helpHTML}" escapeXml="true" /></c:set>  
			<c:set var="messageHelp" value="${help}"></c:set>  
			</c:when>
			<c:otherwise>
				<fmt:message key="${helpKey}" var="messageHelp" />
				<c:set var="helpHTML"><c:out value="${messageHelp}" escapeXml="true" /></c:set>
			<%-- <c:set var="helpHTML" value="${dyna:escapeHTMLtoJavascript(messageHelp)}" /> --%>
			</c:otherwise>
		</c:choose>
		<div class="dynaField">
			<div style="display: hidden;" class="helpTip" id="help_${status.expression}" >
			<img src="${root}/images/delete.gif" onclick="hideHelp('help_${status.expression}')" class="close" alt="hide help"/>
			<p><c:out value="${messageHelp}"/></p>
			</div>
	
	<span id="labelSpan${status.expression}" ${labelStyle} class="dynaLabel">
	
			
	<label for="${status.expression}" class="${labelClass}" >
	
	<c:choose>
		<c:when test="${!empty label}">${label}</c:when>
		<c:otherwise>
		<fmt:message key="${labelKey}" />
		</c:otherwise>
	</c:choose>
    <c:out value="${labelAfter}" escapeXml="false" />

	</label>
	<img class="help" src="<%= request.getContextPath()+"/images/help.gif" %>" 
			onclick="showHelp('help_${status.expression}')" alt="show help"/>
	</span>
	
	</c:if>
</spring:bind>

