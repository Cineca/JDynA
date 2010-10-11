<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="tree" required="true" type="it.cilea.osd.jdyna.model.AlberoClassificatorio" %>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="display" required="false"%>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>

<%-- eventi js non gestiti 
<%@ attribute name="onclick" required="false"%>
<%@ attribute name="onblur" required="false"%> 
--%>
<%@ attribute name="onchange" required="false"%>
<%-- eventi js non gestiti
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

<c:if test="${display == null}">
	<c:set var="display" value="nome" />
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

<c:set var="treeName" value="${tree.nome}" />
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
		<c:set var="displayValueClassificazione" value="display${inputName}" />
		<c:set var="selectNameClassificazione" value="classificazione${inputName}" />
		
		<c:choose>
			<c:when test="${not empty inputValue}">
				<%-- FIXME gestire meglio il valoreDTO! nuova funzione di supporto like getTargetClass??? --%>
				<c:catch var="valoreDTOEx">
					<c:set var="collection" value="${value.object.sottoCategorieAttive}" />
				</c:catch>
				<c:if test="${valoreDTOEx}">
					<c:set var="collection" value="${value.sottoCategorieAttive}" />
				</c:if>
			</c:when>
			<c:otherwise>
				<c:set var="collection" value="${tree.topClassificazioniAttive}" />
			</c:otherwise>
		</c:choose>
		
		<c:set var="onchangeJS" 
				value="DWRUtil.useLoadingMessage(); makeSelection('${dyna:escapeApici(inputName)}','${dyna:escapeApici(displayValueClassificazione)}','${dyna:escapeApici(selectNameClassificazione)}','${dyna:escapeApici(treeName)}',this.selectedIndex);${onchange}"/>
		<input type="hidden" name="${inputName}" 
				id="${inputName}" value="${inputValue}" />			
		<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
		
		<span id="${displayValueClassificazione}" class="${cssClass}">${dyna:getDisplayValue(value,display)}</span>
		
		<dyna:simpleselect
			parameter="${selectNameClassificazione}"
			required="true" 
			collection="${collection}"
			labelKey="none"
			labelSeperator=""
			fieldSeperator=""						
			onchange="${onchangeJS}" />
		
		<c:if test="${fn:length(collection) == 0}">
		<script type="text/javascript">
			document.getElementById('${selectNameClassificazione}').hide();
		</script>
		</c:if>
		
		<c:set var="onclickButton">reloadSelect('${dyna:escapeApici(inputName)}','${dyna:escapeApici(displayValueClassificazione)}','${dyna:escapeApici(selectNameClassificazione)}','${dyna:escapeApici(treeName)}')</c:set>
			
		<input id="button${inputName}" type="button" name="button" value="X"
			onclick="${onclickButton}" />	
	</spring:bind>
	
	<c:if test="${repeatable}">
	<c:if test="${iterationStatus.count == 1}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	<script type="text/javascript">
		var tmpOpts = new Array();
		var tmpValues = new Array();
		tmpOpts[0] = '';
		tmpValues[0] = '...scegli opzione...';
		<c:forEach var="opt" items="${tree.topClassificazioniAttive}" varStatus="optStatus">
			tmpOpts[${optStatus.count}] = '${dyna:escapeApici(tree.nome)}:${dyna:escapeApici(opt.codice)}';
			tmpValues[${optStatus.count}]  = '${dyna:escapeApici(opt.nome)}';
		</c:forEach>
		
		var ${dynajs_var} = new DynaClassificazioneInput('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',
									${fn:length(values)},'${cssClass}','${dyna:escapeApici(treeName)}',tmpOpts,tmpValues);
	</script>
	</c:if>

	<c:choose>
	<c:when test="${iterationStatus.count == fn:length(values)}">
	<img src="${root}/images/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create()" />
	</c:when>
	<c:otherwise>
	<img src="${root}/images/icons/delete_icon.gif" class="deleteButton"
		onclick="${dynajs_var}.remove(${iterationStatus.count - 1},this)" />
	</c:otherwise>
	</c:choose>
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
			<c:set var="displayValueClassificazione" value="display${inputName}" />
			<c:set var="selectNameClassificazione" value="classificazione${inputName}" />	
			<c:set var="onchangeJS" 
				value="DWRUtil.useLoadingMessage(); makeSelection('${dyna:escapeApici(inputName)}','${dyna:escapeApici(displayValueClassificazione)}','${dyna:escapeApici(selectNameClassificazione)}','${dyna:escapeApici(treeName)}',this.selectedIndex);${onchange}"/>
			
			<c:set var="collection" value="${tree.topClassificazioniAttive}" />
			</spring:bind>
		</c:catch>
		<c:set var="validation" value="${propertyPath}[0]" />
	</c:if>
	<c:if test="${exNoIndexedValue != null}">
		<spring:bind path="${propertyPath}">
			<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
			<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
			<c:set var="displayValueClassificazione" value="display${inputName}" />
			<c:set var="selectNameClassificazione" value="classificazione${inputName}" />
			<c:set var="onchangeJS" 
				value="DWRUtil.useLoadingMessage(); makeSelection('${dyna:escapeApici(inputName)}','${dyna:escapeApici(displayValueClassificazione)}','${dyna:escapeApici(selectNameClassificazione)}','${dyna:escapeApici(treeName)}',this.selectedIndex);${onchange}"/>
			<c:choose>
				<c:when test="${not empty inputValue}">			
				 	<c:set var="objectBehindPropertyPath" value="${dyna:getReferencedObject(commandObject,propertyPath)}" />  
					<c:set var="labelValue" value="${dyna:getDisplayValue(objectBehindPropertyPath, display)}" />				
					<c:set var="collection" value="${objectBehindPropertyPath.sottoCategorie}" />					
				</c:when>
				<c:otherwise>
					<c:set var="collection" value="${tree.topClassificazioniAttive}" />
				</c:otherwise>
			</c:choose>
		
		</spring:bind>
		<c:set var="validation" value="${propertyPath}" /> 
	</c:if>
		
	<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
				
	<input type="hidden" name="${inputName}" 
			id="${inputName}" value="${inputValue}" />			
		
	
	<span id="${displayValueClassificazione}" class="${cssClass}">${labelValue}</span>
		
	<dyna:simpleselect
		parameter="${selectNameClassificazione}"
		required="true" 
		collection="${collection}"
		labelKey="none"		
		labelSeperator=""
		fieldSeperator=""								
		onchange="${onchangeJS}" />

	<c:if test="${fn:length(collection) == 0}">
	<script type="text/javascript">
		document.getElementById("${selectNameClassificazione}").hide();;
	</script>		
	</c:if>

	<c:set var="onclickButton">reloadSelect('${dyna:escapeApici(inputName)}','${dyna:escapeApici(displayValueClassificazione)}','${dyna:escapeApici(selectNameClassificazione)}','${dyna:escapeApici(treeName)}')</c:set>
			
	<input id="button${inputName}" type="button" name="button" value="X"
		onclick="${onclickButton}" />
		
	<c:if test="${repeatable}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	<script type="text/javascript">
		var tmpOpts = new Array();
		var tmpValues = new Array();
		tmpOpts[0] = '';
		tmpValues[0] = '...scegli opzione...';
		<c:forEach var="opt" items="${tree.topClassificazioniAttive}" varStatus="optStatus">
			tmpOpts[${optStatus.count}] = '${dyna:escapeApici(tree.nome)}:${dyna:escapeApici(opt.codice)}';
			tmpValues[${optStatus.count}]  = '${dyna:escapeApici(opt.nome)}';
		</c:forEach>
		
		var ${dynajs_var} = new DynaClassificazioneInput('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',
									${fn:length(values)},'${cssClass}','${dyna:escapeApici(treeName)}',tmpOpts,tmpValues);
	</script>

	<img src="${root}/images/main_plus.gif" class="addButton"
		onclick="${dynajs_var}.create()" />

	</c:if>
	<dyna:validation propertyPath="${validation}" /> 			 				
</c:if>
