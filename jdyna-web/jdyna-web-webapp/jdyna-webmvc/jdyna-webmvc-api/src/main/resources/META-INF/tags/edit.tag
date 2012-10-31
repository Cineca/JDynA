<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="tipologia" required="true" type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="visibility" required="true"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>
<%@ attribute name="isCreation" required="false" type="java.lang.Boolean" description="active all functionality only for creation of object"%>
<%@ attribute name="hideLabel" required="false" type="java.lang.Boolean" %>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:if test="${tipologia.help != null}">
	<c:set var="helpHTML" value="${dyna:escapeHTMLtoJavascript(tipologia.help)}" />
</c:if>		

<c:if test="${tipologia.mandatory != false}">
	<c:set var="required" value="true" />
</c:if>		

<c:set var="repetable" value="false" />
<c:if test="${tipologia.repeatable != false}">
	<c:set var="repetable" value="true" />
</c:if>		

<c:if test="${tipologia.rendering.triview eq 'file'}">
	<c:set var="isFile" value="true" />
</c:if>


<c:if test="${tipologia.rendering.triview eq 'link'}">
	<c:set var="isLink" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'testo' && !tipologia.rendering.multilinea}">
	<c:set var="isText" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'testo' && tipologia.rendering.multilinea}">
	<c:set var="isTextArea" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'numero'}">
	<c:set var="isNumero" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'soggettari'}">
	<c:set var="isSoggettario" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'alberoClassificatorio'}">	
	<c:set var="isClassificazione" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && repetable}">
	<c:set var="isCheckbox" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && !repetable}">
	<c:set var="isRadio" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'formula'}">
	<c:set var="isFormula" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'pointer'}">
	<c:set var="isPuntatore" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'boolean'}">
	<c:set var="isBoolean" value="true" />
</c:if>
<%-- se collisioni allora si gestisce una struttura fatta di una textbox e una tabella dove inserire i primi 5
     risultati trovati da una funzione Ajax con HibernateSearch (questo lato server) --%>
<c:choose>
<c:when test="${tipologia.rendering.triview eq 'collisioni' && isCreation}">
	<c:set var="isTextWithCollisioni" value="true" />	
</c:when>
<c:otherwise>
 	<c:if test="${tipologia.rendering.triview eq 'collisioni'}">		
	 	<c:set var="isText" value="true" />
	</c:if> 
</c:otherwise>
</c:choose>

<c:if test="${tipologia.rendering.triview eq 'email'}">
	<c:set var="isEmail" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'calendar'}">
	<c:set var="isCalendar" value="true" />
</c:if>

<c:if test="${!subElement}">
<c:set var="fieldMinWidth" value="" />
<c:set var="fieldMinHeight" value="" />
<c:set var="fieldStyle" value="" />
<c:if test="${tipologia.fieldMinSize.col > 1}">
	<c:set var="fieldMinWidth" value="min-width:${tipologia.fieldMinSize.col}em;" />
</c:if>
<c:if test="${tipologia.fieldMinSize.row > 1}">
	<c:set var="fieldMinHeight" value="min-height:${tipologia.fieldMinSize.row}em;" />
</c:if>
<c:if test="${!empty fieldMinHeight || !empty fieldMinWidth}">
	<c:set var="fieldStyle" value="style=\"${fieldMinHeight}${fieldMinWidth}\"" />
</c:if>

<div class="dynaField" ${fieldStyle}>
<c:set var="labelMinWidth" value="" />
<c:set var="labelStyle" value="" />
<c:if test="${tipologia.labelMinSize > 1}">
	<c:set var="labelMinWidth" value="width:${tipologia.labelMinSize}em;" />
</c:if>
<c:if test="${!empty labelMinWidth}">
	<c:set var="labelStyle" value="style=\"${labelMinWidth}\"" />
</c:if>
<c:if test="${!empty tipologia.help}">
	<c:set var="help">
	&nbsp;<img class="help" src="${root}/images/help.gif" onclick="showHelp('help${tipologia.shortName}')" />
	</c:set>
	<div class="helpTip" id="help${tipologia.shortName}">
	<img class="close" src="${root}/images/delete.gif" onclick="hideHelp('help${tipologia.shortName}')" />
	${tipologia.help}
	</div>
</c:if>	
<c:if test="${!hideLabel}">
	<span class="dynaLabel${tipologia.mandatory?'Required':''}" ${labelStyle}>${tipologia.label} ${help}</span>
</c:if>	
<div id="${tipologia.shortName}Div" class="dynaFieldValue">
</c:if>




<c:choose>
	<c:when test="${isFile}">	
		<dyna:file propertyPath="${propertyPath}" size="${tipologia.rendering.size}"						
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}" showPreview="${tipologia.rendering.showPreview}" labelAnchor="${tipologia.rendering.labelAnchor}"
				servletPath="${tipologia.rendering.servletPath}" fileDescription="${tipologia.rendering.fileDescription}"/>		
	</c:when>
	<c:when test="${isLink}">	
		<dyna:link propertyPath="${propertyPath}" size="${tipologia.rendering.size}" 
				labelHeadSx="${tipologia.rendering.labelHeaderLabel}" labelHeadDx="${tipologia.rendering.labelHeaderURL}"		
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>		
	</c:when>
	<c:when test="${isText}">	
		<dyna:text propertyPath="${propertyPath}" size="${tipologia.rendering.dimensione.col}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>		
	</c:when>	
	<c:when test="${isTextWithCollisioni}">		
		<dyna:text propertyPath="${propertyPath}" size="${tipologia.rendering.dimensione.col}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" collision="true" collisionClass="${tipologia.anagraficaHolderClass}" collisionField="${tipologia.shortName}" visibility="${visibility}"/>
	</c:when>	
	<c:when test="${isNumero}">	
		<dyna:text propertyPath="${propertyPath}" size="${tipologia.rendering.size}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				cssClass="number"
				validationParams="${validationParams}" visibility="${visibility}"/>
	</c:when>
	<c:when test="${isEmail}">	
		<dyna:text propertyPath="${propertyPath}" size="20"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" visibility="${visibility}"/>
	</c:when>
	<c:when test="${isTextArea}">
		<dyna:textarea propertyPath="${propertyPath}" toolbar="${tipologia.rendering.htmlToolbar}"
				rows="${tipologia.rendering.dimensione.row}" cols="${tipologia.rendering.dimensione.col}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isPuntatore}">

		<spring:bind path="${propertyPath}">
			<c:set var="values" value="${status.value}" />
		</spring:bind>
		
		<span id="pointer_${tipologia.id}_repeatable" class="spandatabind">${repetable}</span>			 
		<span id="pointer_${tipologia.id}_path" class="spandatabind">${propertyPath}</span>
		<span id="pointer_${tipologia.id}_tot" class="spandatabind">${fn:length(values)}</span>
		<span class="spandatabind pointerinfo">${tipologia.id}</span>
		<input class="searchboxpointer" id="searchboxpointer_${tipologia.id}" />
			
		
		<div id="pointer_${tipologia.id}_selected">	
		
		
			
			<c:forEach var="value" items="${values}" varStatus="iterationStatus">	
				<spring:bind path="${propertyPath}[${iterationStatus.count - 1}]">
					<c:if test="${iterationStatus.count > 1}">
					<br/>
					</c:if>
					<%-- Se sono riuscito a fare il bind allora è una proprietà indicizzata --%>
					<c:set var="inputShowed" value="true" />
					<c:set var="inputValue"><c:out value="${status.value}" escapeXml="true"></c:out></c:set>
					<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>
						
								
					<input type="hidden" name="${inputName}" 
							id="${inputName}" value="${inputValue}" />			
					<input name="_${inputName}" id="_${inputName}" value="true" type="hidden" />
										
				
					<span>${dyna:getDisplayValue(value,tipologia.rendering.display)}</span>
					
				</spring:bind>
				<img src="${root}/image/jdyna/delete_icon.gif" class="jdyna-icon jdyna-icon-action jdyna-delete-button"/>



				
				<dyna:validation propertyPath="${propertyPath}[${iterationStatus.count - 1}]" />
		</c:forEach>
		
		
		</div>
	</c:when>
	<c:when test="${isClassificazione}">
		<dyna:classificazione propertyPath="${propertyPath}" 
				required="${required}" repeatable="${repetable}"
				tree="${tipologia.rendering.alberoClassificatorio}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isSoggettario}">
		<dyna:subject propertyPath="${propertyPath}" 
				collection="${tipologia.rendering.soggettari}" 
				size="${tipologia.rendering.size}" required="${required}" repeatable="${repetable}" onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isCalendar}">
		<dyna:date propertyPath="${propertyPath}" isTime="${tipologia.rendering.time}" dateMin="${tipologia.rendering.minYear}" dateMax="${tipologia.rendering.maxYear}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>		
	</c:when>
	<c:when test="${isCheckbox}">
		<dyna:checkbox propertyPath="${propertyPath}" 
				collection="${tipologia.rendering.alberoClassificatorio.topClassificazioni}" 
				required="${required}" option4row="${tipologia.rendering.option4row}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isRadio}">
		<dyna:radio propertyPath="${propertyPath}"
				collection="${tipologia.rendering.alberoClassificatorio.topClassificazioni}" 
				required="${required}" option4row="${tipologia.rendering.option4row}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isBoolean}">
		<dyna:boolean propertyPath="${propertyPath}" required="${required}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isFormula}">
		<dyna:formula propertyPath="${propertyPath}" tipologia="${tipologia}" />
	</c:when>

</c:choose>
<c:if test="${!subElement}">
</div>
</div>
<c:if test="${tipologia.newline}">
	<div class="dynaClear">&nbsp;</div>
</c:if>
</c:if>