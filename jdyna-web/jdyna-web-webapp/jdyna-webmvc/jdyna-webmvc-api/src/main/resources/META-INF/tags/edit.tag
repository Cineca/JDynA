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
<%@ attribute name="visibility" required="true"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>
<%@ attribute name="isCreation" required="false" type="java.lang.Boolean" description="active all functionality only for creation of object"%>
<%@ attribute name="hideLabel" required="false" type="java.lang.Boolean" %>
<%@ attribute name="lock" required="false" type="java.lang.Boolean" %>

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

<c:if test="${tipologia.rendering.triview eq 'classificationTree'}">	
	<c:set var="isClassificazione" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && (empty tipologia.rendering.dropdown || tipologia.rendering.dropdown==false) && repetable}">
	<c:set var="isCheckbox" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && (empty tipologia.rendering.dropdown || tipologia.rendering.dropdown==false) && !repetable}">
	<c:set var="isRadio" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && !empty tipologia.rendering.dropdown && tipologia.rendering.dropdown && !repetable}">
	<c:set var="isDropdown" value="true" />
</c:if>


<c:if test="${tipologia.rendering.triview eq 'formula'}">
	<c:set var="isFormula" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'pointer'}">
	<c:set var="isPuntatore" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'custompointer'}">
	<c:set var="isCustomPointer" value="true" />
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
	<c:set var="fieldMinWidth" value="min-width:${tipologia.fieldMinSize.col}${tipologia.fieldMinSize.measurementUnitCol};" />
</c:if>
<c:if test="${tipologia.fieldMinSize.row > 1}">
	<c:set var="fieldMinHeight" value="min-height:${tipologia.fieldMinSize.row}${tipologia.fieldMinSize.measurementUnitRow};" />
</c:if>
<c:if test="${!empty fieldMinHeight || !empty fieldMinWidth}">
	<c:set var="fieldStyle" value="style=\"${fieldMinHeight}${fieldMinWidth}\"" />
</c:if>

<div class="dynaField" ${fieldStyle}>
<c:set var="labelMinWidth" value="" />
<c:set var="labelStyle" value="" />
<c:if test="${tipologia.labelMinSize > 1}">
	<c:set var="labelMinWidth" value="width:${tipologia.labelMinSize}${tipologia.labelMinSizeUnit};" />
</c:if>
<c:if test="${!empty labelMinWidth}">
	<c:set var="labelStyle" value="style=\"${labelMinWidth}\"" />
</c:if>
<c:if test="${!empty tipologia.help}">
	<c:set var="help">
	&nbsp;<img class="help" src="${root}/image/jdyna/help.gif" onclick="showHelp('help${tipologia.shortName}')" />
	</c:set>
	<div class="helpTip" id="help${tipologia.shortName}">
	<img class="close" src="${root}/image/jdyna/delete_icon.gif" onclick="hideHelp('help${tipologia.shortName}')" />
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
				validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}" lock="${lock}"/>		
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
		<dyna:puntatore propertyPath="${propertyPath}" 
				id="${tipologia.id}" display="${tipologia.rendering.display}"
				required="${required}" repeatable="${repetable}"
				validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>
	</c:when>
	<c:when test="${isCustomPointer}">
		<dyna:custompointer propertyPath="${propertyPath}" 
				id="${tipologia.id}"
				required="${required}" repeatable="${repetable}"
				validationParams="${validationParams}" propertydefinition="${tipologia}"/>
	</c:when>	
	<c:when test="${isClassificazione}">
		<dyna:classificationtree propertyPath="${propertyPath}" 
				required="${required}" repeatable="${repetable}"
				treeObjectType="${tipologia.rendering.treeObjectType}"
				rootResearchObject="${tipologia.rendering.rootResearchObject.id}"
				metadataBuilderTree="${tipologia.rendering.metadataBuilderTree.id}"
				chooseOnlyLeaves="${tipologia.rendering.chooseOnlyLeaves}"
				display="${tipologia.rendering.display}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" id="${tipologia.id}"/>
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
		<dyna:checkbox propertyPath="${propertyPath}" id="${tipologia.id}"
				collection="${dyna:getResultsFromWidgetCheckRadio(tipologia.rendering.staticValues)}" 
				required="${required}" option4row="${tipologia.rendering.option4row}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isRadio}">
		<dyna:radio propertyPath="${propertyPath}" id="${tipologia.id}"
				collection="${dyna:getResultsFromWidgetCheckRadio(tipologia.rendering.staticValues)}" 
				required="${required}" option4row="${tipologia.rendering.option4row}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isDropdown}">
		<dyna:dropdown propertyPath="${propertyPath}" id="${tipologia.id}"
				collection="${dyna:getResultsFromWidgetCheckRadio(tipologia.rendering.staticValues)}" 
				required="${required}" option4row="${tipologia.rendering.option4row}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}"/>
	</c:when>
	<c:when test="${isBoolean}">
		<dyna:boolean propertyPath="${propertyPath}" required="${required}" 
				onchange="${onchange}" ajaxValidation="${ajaxValidation}" 
				validationParams="${validationParams}" checkedAsDefault="${tipologia.rendering.checked}"/>
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
