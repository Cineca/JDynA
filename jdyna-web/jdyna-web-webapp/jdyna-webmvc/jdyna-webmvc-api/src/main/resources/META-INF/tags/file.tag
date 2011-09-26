<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="visibility" required="true"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="labelMinSize" required="false" type="java.lang.Integer" %>
<%@ attribute name="size" required="false" type="java.lang.Integer" %>
<%@ attribute name="cssClass" required="false"%>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>
<%@ attribute name="showPreview" required="false" type="java.lang.Boolean" %>
<%@ attribute name="servletPath" required="false"%>
<%@ attribute name="fileDescription" required="false"%>
<%@ attribute name="labelAnchor" required="false"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:if test="${label != null || labelKey != null}">
	<dyna:label label="${label}" labelKey="${labelKey}" 
		help="${help}" labelMinSize="${labelMinSize}" 
		propertyPath="${propertyPath}" required="${required}" 
		
		  helpKey='${helpKey}'
	
		/>
		 <c:if test='${!empty helpKey || !empty help}'>
			<div class="dynaFieldValue" >
			</c:if>
		
</c:if>
	
<c:set var="cssClassAttribute" value="" />
<c:if test="${!empty cssClass}">
	<c:set var="cssClassAttribute" value="class=\"${cssClass}\"" />
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
<span style="width='${size}%'">${fileDescription}</span>
<table>	
<c:forEach var="value" items="${values}" varStatus="iterationStatus">
		
	<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
		
	<tr style="width='${size}%'">	
	<td>	
			<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
			<c:set var="inputShowed" value="true" />
			<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
			<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
			<c:set var="idfilevalue" value="filevalue${status.expression}" />
			<c:set var="idfiledelete" value="filedelete${status.expression}" />
			
			<c:set var="dynajs_funct" value="_dyna_${dyna:md5(propertyPath)}_${dyna:md5(inputName)}()" />
		
			<script type="text/javascript">
			function ${dynajs_funct}{
				dynaFileValueMarkAsToDeleted('${inputName}','${inputValue}', '${idfiledelete}');
			}
			</script>
		
			
			<input id="${inputName}" name="${inputName}" type="hidden" value="${inputValue}" ${disabled}/>
			<c:if test="${disabled}">		
				<input name="_${inputName}" id="_${inputName}" value="true" type="hidden"/>		
			</c:if>
			<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
			<c:set var="functionValidation" value="" />
			<c:if test="${!empty ajaxValidation}">
				<c:set var="functionValidation" value="${ajaxValidation}('${inputName}'${!empty parametersValidation?',':''}${!empty parametersValidation?parametersValidation:''});" />
			</c:if>
	
			<c:set var="onchange" value="${onchange}" />
			
		<c:choose>
		<c:when test="${dyna:getFileIsOnServer(inputValue)}">
			<c:choose>
			<c:when test="${showPreview}">
					<img id="${idfilevalue}" name="${idfilevalue}" alt="${dyna:getFileName(inputValue)} picture" src="<%=request.getContextPath() %>/${servletPath}/${dyna:getFileFolder(inputValue)}?filename=${dyna:getFileName(inputValue)}"
						title="A preview ${dyna:getFileName(inputValue)} picture" />
			</c:when>
			<c:otherwise>
					<a href="<%=request.getContextPath() %>/${servletPath}/${dyna:getFileFolder(inputValue)}?filename=${dyna:getFileName(inputValue)}">${labelAnchor}</a>				
			</c:otherwise>
			</c:choose>
			<br/>		
			<div style="font-size: xx-small">Check this box to delete (OR replace) the image from the server: <input type="checkbox" id="${idfiledelete}" value="false" onclick="${dynajs_funct}"/></div>
			
		</c:when>
		<c:otherwise>		
			<input name="${inputName}" id="${inputName}" type="file" ${disabled} />			
		</c:otherwise>
		</c:choose>
	</td>
		
	<td>
		
	<c:if test="${visibility}">
		<dyna:boolean propertyPath="${inputName}.visibility"/>
	</c:if>	
	
	
	
	
	<c:if test="${empty disabled}">
	<c:if test="${repeatable}">
	<c:if test="${iterationStatus.count == 1}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	
	<script type="text/javascript">
		var ${dynajs_var} = new AddFileInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',${fn:length(values)},
									'${dyna:escapeApici(functionValidation)};${dyna:escapeApici(functionValidation)}',
									 ${size},${repeatable},									 
									 '${cssClass}','${visibility}');
	</script>	
	</c:if>

	<c:choose>
	<c:when test="${iterationStatus.count == fn:length(values)}">
	<img id="_dyna_${dyna:md5(propertyPath)}_addbutton" src="${root}/image/jdyna/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create();" />
	</c:when>
	<c:otherwise>
	<img src="${root}/image/jdyna/delete_icon.gif" class="deleteButton"
		onclick="${dynajs_var}.remove(${iterationStatus.count - 1},this);" />
	</c:otherwise>
	</c:choose>
	</c:if>
	</c:if>
	<dyna:validation propertyPath="${propertyPath}[${iterationStatus.count - 1}]" />
	</td>
</tr>	
</spring:bind>
</c:forEach>
</c:catch>
<c:if test="${!inputShowed}">
<tr style="width='${size}%'"><td>	
	<c:if test="${exNoIndexedValue == null}">
		<c:catch var="exNoIndexedValue">
			<spring:bind path="${propertyPath}[0]">
				<c:set var="inputValue"><c:out value="${status.value == null?'':status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
				<c:set var="idfilevalue" value="filevalue${status.expression}" />
				<c:set var="idfiledelete" value="filedelete${status.expression}" />
			</spring:bind>
		</c:catch>		
		<c:set var="validation" value="${propertyPath}[0]"/>	
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
			<spring:bind path="${propertyPath}">
				<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
				<c:set var="idfilevalue" value="filevalue${status.expression}" />
				<c:set var="idfiledelete" value="filedelete${status.expression}" />					
			</spring:bind>
			<c:set var="validation" value="${propertyPath}"/>	
	
	</c:if>		
				
			<c:set var="dynajs_funct" value="_dyna_${dyna:md5(propertyPath)}_${dyna:md5(inputName)}()" />
		
			<script type="text/javascript">
			function ${dynajs_funct}{
				dynaFileValueMarkAsToDeleted('${inputName}','${inputValue}', '${idfiledelete}');
			}
			</script>
		
			
			<input id="${inputName}" name="${inputName}" type="hidden" value="${inputValue}" ${disabled}/>
		<c:if test="${disabled}">		
			<input name="_${inputName}" id="_${inputName}" value="true" type="hidden"/>		
		</c:if>
				
		<c:set var="onchange" value="${onchange}" />	
	
		<c:choose>
		<c:when test="${dyna:getFileIsOnServer(inputValue)}">
			<c:choose>
			<c:when test="${showPreview}">
					<img id="${idfilevalue}" name="${idfilevalue}" alt="${dyna:getFileName(inputValue)} picture" src="<%=request.getContextPath() %>/${servletPath}/${dyna:getFileFolder(inputValue)}?${dyna:getFileName(inputValue)}"
						title="A preview ${dyna:getFileName(inputValue)} picture" />
			</c:when>
			<c:otherwise>
					<a href="<%=request.getContextPath() %>/${servletPath}/${dyna:getFileFolder(inputValue)}?${dyna:getFileName(inputValue)}">${labelAnchor}</a>				
			</c:otherwise>
			</c:choose>
			<br/>				
			<div style="font-size: xx-small">Check this box to delete (OR replace) the image from the server: <input type="checkbox" id="${idfiledelete}" value="false" onclick="${dynajs_funct}"/></div>
			
		</c:when>
		<c:otherwise>		
			<input name="${inputName}" id="${inputName}" type="file" ${disabled} />			
		</c:otherwise>
		</c:choose>		
	</td>
		
	<td>			
	<c:if test="${visibility}">
		<dyna:boolean propertyPath="${inputName}.visibility"/>
	</c:if>
	
	<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
	<c:set var="functionValidation" value="" />
	<c:if test="${!empty ajaxValidation}">
		<c:set var="functionValidation" value="${ajaxValidation}('${inputName}'${!empty parametersValidation?',':''}${!empty parametersValidation?parametersValidation:''})" />
	</c:if>

				
	<c:if test="${empty disabled}">
	<c:if test="${repeatable}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	
	<script type="text/javascript">
		var ${dynajs_var} = new AddFileInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',${fn:length(values)},
									'${dyna:escapeApici(functionValidation)};${dyna:escapeApici(functionValidation)}',
									 ${size},${repeatable},	
									 '${cssClass}','${visibility}');		
	</script>
	<img id="_dyna_${dyna:md5(propertyPath)}_addbutton" src="${root}/image/jdyna/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create(this);" alt="add button"/>
	
	</c:if>
	</c:if>
	
	<dyna:validation propertyPath="${validation}" />
	</td></tr>
</c:if>
</table>
	 <c:if test='${!empty helpKey || !empty help}'>

</div></div>
</c:if>