<%--
-JDynA, Dynamic Metadata Management for Java Domain Object
-
- Copyright (c) 2008, CILEA and third-party contributors as
- indicated by the @author tags or express copyright attribution
- statements applied by the authors.  All third-party contributions are
- distributed under license by CILEA.
-
- This copyrighted material is made available to anyone wishing to use, modify,
- copy, or redistribute it subject to the terms and conditions of the GNU
- Lesser General Public License v3 or any later version, as published 
- by the Free Software Foundation, Inc. <http://fsf.org/>.
-
- This program is distributed in the hope that it will be useful,
- but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
- or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
- for more details.
-
-  You should have received a copy of the GNU Lesser General Public License
-  along with this distribution; if not, write to:
-  Free Software Foundation, Inc.
-  51 Franklin Street, Fifth Floor
-  Boston, MA  02110-1301  USA
--%>
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
			<div class="helpTip" id="help_${status.expression}" >
			<img src="${root}/image/jdyna/close_help.gif" onclick="hideHelp('help_${status.expression}')" class="close" alt="hide help"/>
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
	<img class="help" src="<%= request.getContextPath()+"/image/jdyna/help.gif" %>" 
			onclick="showHelp('help_${status.expression}')" alt="show help"/>
	</span>
	
	</c:if>
</spring:bind>

