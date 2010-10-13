<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="visibility" required="true"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="dateMin" required="false" type="java.util.Date" %>
<%@ attribute name="dateMax" required="false" type="java.util.Date" %>
<%@ attribute name="isTime" required="false" type="java.lang.Boolean" %>
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

<%@ taglib uri="jdynatags" prefix="dyna" %>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>



<c:if test="${label != null || labelKey != null}">
	<dyna:label label="${label}" labelKey="${labelKey}" 
		help="${help}" helpKey="${helpKey}" 
		propertyPath="${propertyPath}" required="${required}" />
</c:if>

<c:if test="${isTime == null}">
	<c:set var="isTime" value="false" />
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
		<c:set var="calendarButton" value="calendar${status.expression}" />

		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
		
		<input id="${inputName}" name="${inputName}" type="text" value="${inputValue}" size="10" ${disabled}/>
		
		<c:if test="${empty disabled}">
		<img id="${calendarButton}" 
			src="<c:url value="/image/jdyna/calendar.png"/>" alt="calendar"
				class="calendar" />
		
		<script type="text/javascript">
						Calendar.setup(
							{
							inputField : "${inputName}", // ID of the input field
							<c:choose>
								<c:when test="${isTime}">
									ifFormat : "%d-%m-%Y %H:%M", // the date format
								</c:when>
								<c:otherwise>
									ifFormat : "%d-%m-%Y", // the date format
								</c:otherwise>
							</c:choose>
							button : "${calendarButton}", // ID of the button
							cache: false
							<c:if test="${isTime == true}">
								,
								showsTime : true // show time as well as date
							</c:if>						
							}
						);
		</script>
		</c:if>
		
		<c:if test="${visibility}">
			<dyna:boolean propertyPath="${inputName}.visibility"/>
		</c:if>
	</spring:bind>
	
	<c:if test="${empty disabled}">
	<c:if test="${repeatable}">
	<c:if test="${iterationStatus.count == 1}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	<script type="text/javascript">
		var ${dynajs_var} = new DynaDateInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',${fn:length(values)},
									${isTime},'${visibility}');
	</script>
	</c:if>
	
	<c:choose>
	<c:when test="${iterationStatus.count == fn:length(values)}">
	<img src="${root}/image/jdyna/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create()" />
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
	<%-- Se sono qui l'inputValue è per forza vuoto (altrimenti avrei avuto una lista di 1 elemento) --%>
		<c:catch var="exNoIndexedValue">
			<spring:bind path="${propertyPath}[0]">			
				<c:set var="inputValue" value="" />
				<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
				<c:set var="calendarButton" value="calendar${status.expression}" />
			</spring:bind>
			<c:set var="validation" value="${propertyPath}[0]"/>
		</c:catch>
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
		<spring:bind path="${propertyPath}">
			<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
			<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
			<c:set var="calendarButton" value="calendar${status.expression}" />
		</spring:bind>
		 <c:set var="validation" value="${propertyPath}"/>	
	</c:if>
		<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
		<c:if test="${!empty ajaxValidation}">
			<c:set var="functionValidation" value="${ajaxValidation}('${inputName}',${parametersValidation})" />
		</c:if>
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
		<input id="${inputName}" name="${inputName}" ${disabled} type="text" value="${inputValue}" size="10" onchange="${functionValidation};${onchange}"/>
		
		<c:if test="${empty disabled}">
		<img id="${calendarButton}" 
			src="<c:url value="/image/jdyna/calendar.png"/>" alt="calendar"
		class="calendar" />
	
		<script type="text/javascript">
						Calendar.setup(
							{
							inputField : "${inputName}", // ID of the input field
							<c:choose>
								<c:when test="${isTime}">
									ifFormat : "%d-%m-%Y %H:%M", // the date format
								</c:when>
								<c:otherwise>
									ifFormat : "%d-%m-%Y", // the date format
								</c:otherwise>
							</c:choose>
							button : "${calendarButton}", // ID of the button
							cache: false
							<c:if test="${isTime == true}">
								,
								showsTime : true // show time as well as date
							</c:if>							
							}
						);
		</script>
		</c:if>
		
		<c:if test="${visibility}">
			<dyna:boolean propertyPath="${inputName}.visibility"/>
		</c:if>
			
	<c:if test="${empty disabled}">
	<c:if test="${repeatable}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	<script type="text/javascript">
		var ${dynajs_var} = new DynaDateInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',${fn:length(values)},
									${isTime},'${visibility}'); 
	</script>
	
	
	<img src="${root}/image/jdyna/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create()" />
	</c:if>
	</c:if>
	

		<dyna:validation propertyPath="${validation}" />		 				
</c:if>
