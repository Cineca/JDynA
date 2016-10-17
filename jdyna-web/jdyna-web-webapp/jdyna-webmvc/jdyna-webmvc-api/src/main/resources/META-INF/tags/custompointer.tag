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
<%@ attribute name="id" required="true"%>
<%@ attribute name="controllerURL" required="false" description="change method or controller call from ajaxtag autocomplete, use this for specific search"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="size" required="false" type="java.lang.Integer" %>
<%@ attribute name="filtro" required="false"%>
<%@ attribute name="propertydefinition" required="true" type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>
<%-- eventi js non gestiti 
<%@ attribute name="onclick" required="false"%>
<%@ attribute name="onblur" required="false"%> 
<%@ attribute name="onchange" required="false"%>
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

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

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
		
		<span id="custompointer_${id}_repeatable" class="spandatabind">${repeatable == true?'true':'false'}</span>			 
		<span id="custompointer_${id}_path" class="spandatabind">${propertyName}</span>
		<span id="custompointer_${id}_tot" class="spandatabind">${fn:length(values)}</span>
		<span id="custompointer_${id}_type" class="spandatabind">${type}</span>
		<span class="spandatabind custompointerinfo">${id}</span>
		<input class="searchboxcustompointer" id="searchboxcustompointer_${id}" />
		
		<div id="custompointer_${id}_selected">	
			<c:forEach var="value" items="${values}" varStatus="iterationStatus">	
				<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
					<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
					<c:set var="inputShowed" value="true" />
					<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
					<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
					<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />	
					<div id="custompointer_${id}_selected_${iterationStatus.count - 1}">
						<input type="hidden" name="${inputName}" 
								id="${inputName}" value="${inputValue}" />			
					
						<span>${dyna:displayAdvanced(propertydefinition, inputValue)}</span>
						<img src="${root}/image/jdyna/delete_icon.gif" class="jdyna-icon jdyna-icon-action jdyna-delete-button"/>
					</div>
				</spring:bind>
				<dyna:validation propertyPath="${propertyPath}[${iterationStatus.count - 1}]" />
		</c:forEach>
		</div>
