<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="tipologia" required="true" type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>
<%@ attribute name="isCreation" required="false" type="java.lang.Boolean" description="active all functionality only for creation of object"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:if test="${tipologia.help != null}">
	<c:set var="helpHTML" value="${dyna:escapeHTMLtoJavascript(tipologia.help)}" />
</c:if>		

<c:if test="${tipologia.obbligatorieta != false}">
	<c:set var="required" value="true" />
</c:if>		

<c:if test="${tipologia.ripetibile != false}">
	<c:set var="repetable" value="true" />
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

<c:if test="${tipologia.rendering.triview eq 'puntatore'}">
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

<c:if test="${tipologia.rendering.triview eq 'combo'}">
	<c:set var="isCombo" value="true" />
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
	<span class="dynaLabel${tipologia.obbligatorieta?'Required':''}" ${labelStyle}>${tipologia.label}:${help}</span>
<div id="${tipologia.shortName}Div" class="dynaFieldValue">
</c:if>


<c:choose>
	<c:when test="${isText}">	
		<dyna:text propertyPath="${propertyPath}" size="${tipologia.rendering.dimensione.col}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>	
	<c:when test="${isTextWithCollisioni}">		
		<dyna:text propertyPath="${propertyPath}" size="${tipologia.rendering.dimensione.col}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" collision="true" collisionClass="${tipologia.anagraficaHolderClass}" collisionField="${tipologia.shortName}"/>
	</c:when>	
	<c:when test="${isNumero}">	
		<dyna:text propertyPath="${propertyPath}" size="${tipologia.rendering.size}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				cssClass="number"
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isEmail}">	
		<dyna:text propertyPath="${propertyPath}" size="20"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isTextArea}">
		<dyna:textarea propertyPath="${propertyPath}" toolbar="${tipologia.rendering.htmlToolbar}"
				rows="${tipologia.rendering.dimensione.row}" cols="${tipologia.rendering.dimensione.col}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isPuntatore}">
		<dyna:puntatore propertyPath="${propertyPath}" size="${tipologia.rendering.size}" 
				target="${dyna:getTargetClass(tipologia.rendering)}" 
				display="${tipologia.rendering.display}" 
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
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
		<dyna:date propertyPath="${propertyPath}" isTime="${tipologia.rendering.time}"
				required="${required}" repeatable="${repetable}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
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
	<c:when test="${isCombo}">
		<dyna:combo propertyPath="${propertyPath}" repeatable="${repetable}" 
				tipologia="${tipologia}" required="${required}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
</c:choose>
<c:if test="${!subElement}">
</div>
</div>
<c:if test="${tipologia.newline}">
	<div class="dynaClear">&nbsp;</div>
</c:if>
</c:if>