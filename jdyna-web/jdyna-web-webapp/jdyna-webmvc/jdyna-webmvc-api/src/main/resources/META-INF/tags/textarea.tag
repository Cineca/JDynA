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
<%@ attribute name="toolbar" required="false"%>
<%@ attribute name="rows" required="false" type="java.lang.Integer" %>
<%@ attribute name="cols" required="false" type="java.lang.Integer" %>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>


<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<%@ attribute name="onchange" required="false"%>

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
<c:catch var="exNoIndexedValue">
<c:forEach var="value" items="${values}" varStatus="iterationStatus">
	<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
		<c:if test="${iterationStatus.count > 1}">
		<br/>
		</c:if>
		<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
		<c:set var="inputShowed" value="true" />
		<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
		<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
		
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
			
		<textarea name="${inputName}" id="${inputName}" rows="${rows}" cols="${cols}"
			onchange="${onchange}">${inputValue}</textarea>
				
		<c:if test="${!empty toolbar && toolbar ne 'nessuna'}">  														
			<script type="text/javascript">
				var oFCKeditor = new FCKeditor('${inputName}') ;
				oFCKeditor.BasePath = '${pageContext.request.contextPath}/fckeditor/';								
				oFCKeditor.ToolbarSet =  '${toolbar}';
				oFCKeditor.ReplaceTextarea();
			</script>
		</c:if>
	</spring:bind>
	
	<c:if test="${repeatable}">
	<c:if test="${iterationStatus.count == 1}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	<script type="text/javascript">
		var ${dynajs_var} = new DynaTextAreaInput('${root}','${dynajs_var}',
									'${dyna:absolutePropertyPath(propertyPath)}',${fn:length(values)},
									'${cols}','${rows}','${toolbar}');
	</script>
	</c:if>
	
		<c:choose>
	<c:when test="${iterationStatus.count == fn:length(values)}">
	<img src="${root}/image/jdyna/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create()" />
	</c:when>
	<c:otherwise>
	<img src="${root}/image/jdyna/delete_icon.gif" class="deleteButton"
		onclick="${dynajs_var}.remove(${iterationStatus.count - 1},this)" />
	</c:otherwise>
	</c:choose>
	</c:if>
	
	<dyna:validation propertyPath="${propertyPath}[${iterationStatus.count - 1}]" />
</c:forEach>
</c:catch>
<c:if test="${!inputShowed}">		
	<c:if test="${exNoIndexedValue == null}">
		<c:catch var="exNoIndexedValue">
			<spring:bind path="${propertyPath}[0]">
				<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
			</spring:bind>
		</c:catch>
		<c:set var="validation" value="${propertyPath}[0]"/>	
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
			<spring:bind path="${propertyPath}">
				<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
			</spring:bind>
		<c:set var="validation" value="${propertyPath}"/>	
	</c:if>
		
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />

		<textarea name="${inputName}" id="${inputName}" rows="${rows}" cols="${cols}"
			onchange="${onchange}">${inputValue}</textarea>
		
		<c:if test="${!empty toolbar && toolbar ne 'nessuna'}">  														
			<script type="text/javascript">
				var oFCKeditor = new FCKeditor('${inputName}') ;
				oFCKeditor.BasePath = '${pageContext.request.contextPath}/fckeditor/';								
				oFCKeditor.ToolbarSet =  '${toolbar}';
				oFCKeditor.ReplaceTextarea();
			</script>
		</c:if>

		<c:if test="${repeatable}">
		<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
		<script type="text/javascript">
			var ${dynajs_var} = new DynaTextAreaInput('${root}','${dynajs_var}',
										'${dyna:absolutePropertyPath(propertyPath)}',${fn:length(values)},
										'${cols}','${rows}','${toolbar}');
		</script>
		
		<img src="${root}/image/jdyna/main_plus.gif" class="addButton"
			onclick="${dynajs_var}.create()" />
		</c:if>
	
		<dyna:validation propertyPath="${validation}" />
</c:if>
