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
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="collection" required="true" type="java.util.Collection" %>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="size" required="false" type="java.lang.Integer" %>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>

<%@ attribute name="onchange" required="false"%>
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

<c:if test="${size == null}">
	<c:set var="size" value="20" />
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

<c:catch var="exNoIndexedValue">
<c:forEach var="value" items="${values}" varStatus="iterationStatus">	
	<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
		<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
		<c:set var="inputShowed" value="true" />
		<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
		<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
		<c:set var="idsuggestbox" value="suggestbox${status.expression}" />
		<c:set var="idsubjectlist" value="subjectlist${status.expression}" />
		<c:set var="idmessagespan" value="message${status.expression}" />

		<c:choose>		
			<c:when test="${fn:length(collection) == 1}">
				<input type="hidden" value="${collection[0].identifyingValue}"
					id="${idsubjectlist}" name="${idsubjectlist}" />
			</c:when>
			<c:otherwise>
				<dyna:simpleselect
					parameter="${idsubjectlist}"
					required="true"
					collection="${collection}"
					labelKey="none"
					onchange="clearSubjectChoise('${idsuggestbox}','${inputName}')"
					value="${dyna:getSubject(inputValue)}" />	
			</c:otherwise>
		</c:choose>		
		<c:set var="dynajs_funct" value="_dyna_${dyna:md5(inputName)}" />
		<input id="${inputName}" name="${inputName}" type="hidden" value="${inputValue}" />
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
		<input id="${idsuggestbox}" name="${idsuggestbox}" 
			type="text" size="${size}" value="${dyna:getSubjectValue(inputValue)}" />
		<span id="${idmessagespan}"></span>

		<script type="text/javascript">
			function ${dynajs_funct}(){
				dynaSubjectUpdateValue('${idsuggestbox}','${inputName}','${idsubjectlist}');
			}
		</script>
		<ajax:autocomplete
       		baseUrl="${pageContext.request.contextPath}/ajaxFrontSubject.htm"
       		source="${idsuggestbox}"
       		target="${inputName}"
       		className="autocomplete"
       		indicator="${idmessagespan}"        				
       		minimumCharacters="3"       				        				
	       	parameters="subject={${idsubjectlist}},query={${idsuggestbox}}"			        		        	
       		parser="new ResponseXmlToHtmlListParser()"
       		preFunction="${dynajs_funct}">
       </ajax:autocomplete> 				 				
	</spring:bind>	
	<dyna:validation propertyPath="${propertyPath}[${iterationStatus.count - 1}]" />
</c:forEach>
</c:catch>
<c:if test="${!inputShowed}">
	<c:if test="${exNoIndexedValue == null}">	
	<%-- Se sono qui l'inputValue è per forza vuoto (altrimenti avrei avuto una lista di 1 elemento) --%>
		<c:catch var="exNoIndexedValue">
			<spring:bind path="${propertyPath}[0]">			
				<c:set var="inputValue" value="" />
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
				<c:set var="idsuggestbox" value="suggestbox${status.expression}" />
				<c:set var="idsubjectlist" value="subjectlist${status.expression}" />
				<c:set var="idmessagespan" value="message${status.expression}" />
			</spring:bind>
		</c:catch>
		<c:set var="validation" value="${propertyPath}[0]"/>	
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
		<spring:bind path="${propertyPath}">
				<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
				<c:set var="idsuggestbox" value="suggestbox${status.expression}" />
				<c:set var="idsubjectlist" value="subjectlist${status.expression}" />
				<c:set var="idmessagespan" value="message${status.expression}" />
		</spring:bind>
		<c:set var="validation" value="${propertyPath}"/>	
	</c:if>
					
	
		<c:choose>		
			<c:when test="${fn:length(collection) == 1}">
				<input type="hidden" value="${collection[0].identifyingValue}"
					id="${idsubjectlist}" name="${idsubjectlist}" />
			</c:when>
			<c:otherwise>
				<dyna:simpleselect
					parameter="${idsubjectlist}"
					required="true"
					collection="${collection}"
					labelKey="none"
					onchange="clearSubjectChoise('${idsuggestbox}','${inputName}')" 
					value="${dyna:getSubject(inputValue)}" />	
			</c:otherwise>
		</c:choose>		
		
		<c:set var="dynajs_funct" value="_dyna_${dyna:md5(inputName)}" />
		<input id="${inputName}" name="${inputName}" type="hidden" value="${inputValue}" />
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
		
		<input id="${idsuggestbox}" name="${idsuggestbox}" 
			type="text" size="${size}" value="${dyna:getSubjectValue(inputValue)}" />
		<span id="${idmessagespan}"></span>
		
		<script type="text/javascript">
			function ${dynajs_funct}(){
				dynaSubjectUpdateValue('${idsuggestbox}','${inputName}','${idsubjectlist}');
			}
		</script>

		<ajax:autocomplete
       		baseUrl="${pageContext.request.contextPath}/ajaxFrontSubject.htm"
       		source="${idsuggestbox}"
       		target="${inputName}"
       		className="autocomplete"
       		indicator="${idmessagespan}"        				
       		minimumCharacters="3"       				        				
	       	parameters="subject={${idsubjectlist}},query={${idsuggestbox}}"			        		        	
       		parser="new ResponseXmlToHtmlListParser()"
       		preFunction="${dynajs_funct}">
       </ajax:autocomplete>
       <dyna:validation propertyPath="${validation}" /> 				 				
</c:if>
