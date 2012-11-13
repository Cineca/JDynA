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
<%@ attribute name="tipologia" required="true" type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>

<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<%@ taglib uri="jdynatags" prefix="dyna" %>
<%-- 
  --	ATTENZIONE QUESTO TAG FUNZIONA SOLO CON LE DYNAMIC PROPERTIES!!!
  --
  --
  --%>

<c:if test="${label != null || labelKey != null}">
	<dyna:label label="${label}" labelKey="${labelKey}" 
		help="${help}" helpKey="${helpKey}" 
		propertyPath="${propertyPath}" required="${required}" />
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


<c:if test="${tipologia.rendering.resultTriview eq 'numero'}">
	<c:set var="isNumero" value="true" />
</c:if>
<c:if
	test="${tipologia.rendering.resultTriview eq 'testo' && !(tipologia.rendering.dimensione.row >1)}">
	<c:set var="isText" value="true" />
</c:if>
<c:if
	test="${tipologia.rendering.resultTriview eq 'testo' && (tipologia.rendering.dimensione.row > 1)}">
	<c:set var="isTextArea" value="true" />
</c:if>
<c:if
	test="${tipologia.rendering.resultTriview eq 'checkradio' && repetable}">
	<c:set var="isCheckbox" value="true" />
</c:if>
<c:if
	test="${tipologia.rendering.resultTriview eq 'checkradio' && !repetable}">
	<c:set var="isRadio" value="true" />
</c:if>
<c:if test="${tipologia.rendering.resultTriview eq 'alberoClassificatorio'}">	
	<c:set var="isClassificazione" value="true" />
</c:if>

<c:choose>
<c:when test="${isText || isTextArea}">
	<c:forEach var="value" items="${values}">
		<c:if test="${tipologia.rendering.dimensione.row != 1}">
			<c:set var="minheight"
				value="min-height: ${tipologia.rendering.dimensione.row}em;" />
		</c:if>
		<c:set var="minwidth"
			value="min-width: ${tipologia.rendering.dimensione.col}em;" />
		<c:if test="${!empty minheight && !empty minwidth}">
			<c:set var="style" value="style=\" ${minheight}${minwidth}\"" />
		</c:if>
		<span ${style}>${value.object}</span>
	</c:forEach>
</c:when>
<c:when
	test="${isClassificazione|| isSoggettario || isRadio || isCheckbox}">
	<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:if test="${valueStatus.count != 1}">
			<br />
		</c:if>
			${value.object.nome}
		</c:forEach>
</c:when>

<%-- DA QUI IN POI SONO TIPI AL MOMENTO NON SUPPORTATI (mancano i widgetformula relativi) --%>
<c:when test="${isPuntatore}">
	<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:if test="${valueStatus.count != 1}">
			<br />
		</c:if>
			${dyna:getDisplayValue(value.object,tipologia.rendering.display)}
		</c:forEach>
</c:when>
<c:otherwise>
	<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:if test="${valueStatus.count != 1}">
			<br />
		</c:if>
			${dyna:display(tipologia,value.object)}
		</c:forEach>
</c:otherwise>
</c:choose>
<c:if test="${fn:length(values) == 0 || empty values}">
	n.d.
</c:if>
