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
<c:forEach var="value" items="${values}" varStatus="iterationStatus">
	<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
		<c:if test="${iterationStatus.count > 1}">
		<br/>
		</c:if>
		<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
		<c:set var="inputShowed" value="true" />
		<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
		<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
		<c:set var="idlinkvalue" value="linkvalue${status.expression}" />
		<c:set var="idlinkdescription" value="linkdescription${status.expression}" />
		
		<c:set var="dynajs_funct" value="_dyna_${dyna:md5(inputName)}()" />
		
		<script type="text/javascript">
			function ${dynajs_funct}{
				dynaLinkValueUpdate('${inputName}','${idlinkdescription}','${idlinkvalue}');
			}
		</script>
		
		<input id="${inputName}" name="${inputName}" type="hidden" value="${inputValue}" />
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
		
		<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
	<c:set var="functionValidation" value="" />
	<c:if test="${!empty ajaxValidation}">
		<c:set var="functionValidation" value="${ajaxValidation}('${inputName}'${!empty parametersValidation?',':''}${!empty parametersValidation?parametersValidation:''});" />
	</c:if>
	
	<c:set var="onchange" value="${dynajs_funct};${onchange}" />
	
	
	<input name="${idlinkdescription}" id="${idlinkdescription}" type="text" ${disabled} size="${size}%"
		value="${dyna:getLinkDescription(inputValue)}" onchange="${functionValidation}${onchange}" ${cssClassAttribute}/>
		
	<input name="${idlinkvalue}" id="${idlinkvalue}" size="${size}%" type="text" ${disabled}
		value="${dyna:getLinkValue(inputValue)}" onchange="${functionValidation}${onchange}" ${cssClassAttribute}/>
	
	<c:if test="${visibility}">
		<dyna:boolean propertyPath="${inputName}.visibility"/>
	</c:if>	
	
	</spring:bind>
	
	<c:if test="${empty disabled}">
	<c:if test="${repeatable}">
	<c:if test="${iterationStatus.count == 1}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	
	<script type="text/javascript">
		var ${dynajs_var} = new AddLinkInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',${fn:length(values)},
									'${dyna:escapeApici(functionValidation)};${dyna:escapeApici(functionValidation)}',
									 ${size},'${cssClass}','${visibility}');
	</script>	
	</c:if>

	<c:choose>
	<c:when test="${iterationStatus.count == fn:length(values)}">
	<img src="${root}/image/jdyna/main_plus.gif" class="addButton"
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
</c:forEach>
</c:catch>
<c:if test="${!inputShowed}">	
	<c:if test="${exNoIndexedValue == null}">
		<c:catch var="exNoIndexedValue">
			<spring:bind path="${propertyPath}[0]">
				<c:set var="inputValue"><c:out value="${status.value == null?'':status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
				<c:set var="idlinkvalue" value="linkvalue${status.expression}" />
				<c:set var="idlinkdescription" value="linkdescription${status.expression}" />
			</spring:bind>
		</c:catch>		
		<c:set var="validation" value="${propertyPath}[0]"/>	
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
			<spring:bind path="${propertyPath}">
				<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
				<c:set var="idlinkvalue" value="linkvalue${status.expression}" />
				<c:set var="idlinkdescription" value="linkdescription${status.expression}" />
			</spring:bind>
			<c:set var="validation" value="${propertyPath}"/>	
	
	</c:if>
		
		<input id="${inputName}" name="${inputName}" type="hidden" value="${inputValue}" />
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />

		<c:set var="dynajs_funct" value="_dyna_${dyna:md5(inputName)}()" />
		
		<script type="text/javascript">
			function ${dynajs_funct}{
				dynaLinkValueUpdate('${inputName}','${idlinkdescription}','${idlinkvalue}');
			}
		</script>
		
		<c:set var="onchange" value="${dynajs_funct};${onchange}" />
		<input name="${idlinkdescription}" id="${idlinkdescription}" type="text" ${disabled} size="${size}%"
			value="${dyna:getLinkDescription(inputValue)}" onchange="${functionValidation}${onchange}" ${cssClassAttribute}/>
		<input name="${idlinkvalue}" id="${idlinkvalue}" size="${size}%" type="text" ${disabled}
			value="${dyna:getLinkValue(inputValue)}" onchange="${functionValidation}${onchange}" ${cssClassAttribute}/>
			
					
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
		var ${dynajs_var} = new AddLinkInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',${fn:length(values)},
									'${dyna:escapeApici(functionValidation)};${dyna:escapeApici(functionValidation)}',
									 ${size},'${cssClass}','${visibility}');		
	</script>
	<img src="${root}/image/jdyna/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create();" alt="add button"/>
	
	</c:if>
	</c:if>
	
	<dyna:validation propertyPath="${validation}" />
	
</c:if>
	 <c:if test='${!empty helpKey || !empty help}'>

</div></div>
</c:if>