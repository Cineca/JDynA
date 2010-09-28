<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="tipologia" required="true" type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="visibility" required="true"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="repeatable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>


<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>
<%-- 
  --	ATTENZIONE QUESTO TAG FUNZIONA SOLO CON LE DYNAMIC PROPERTIES!!!
  --
  --
  --%>

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
	<c:set var="inputName" value="${status.expression}" />
</spring:bind>
	<c:if test="${repeatable}">
	<c:set var="dynajs_var" value="_dyna_${dyna:md5(propertyPath)}" />
	<script type="text/javascript">
		var tmpShortNames = new Array();
		var tmpLabels = new Array();
		var tmpRepetables = new Array();
		var tmpLabelsSize = new Array();
		var tmpTypes = new Array();
		var tmpNewLines = new Array();
		
		<c:forEach var="subtip" items="${tipologia.rendering.sottoTipologie}" varStatus="subStatus"> 
			tmpShortNames[${subStatus.count - 1}] = '${subtip.shortName}';
			tmpLabels[${subStatus.count - 1}] = '${subtip.label}';
			tmpRepetables[${subStatus.count - 1}] = ${subtip.repeatable};
			tmpLabelsSize[${subStatus.count - 1}] = '${dyna:escapeApici(subtip.labelMinSize)}';
			tmpNewLines[${subStatus.count - 1}] = ${subtip.newline}; 
			tmpTypes[${subStatus.count - 1}] = new Array(); 
			tmpTypes[${subStatus.count - 1}][0] = '${dyna:escapeApici(subtip.rendering.triview)}';
			
			<c:choose>
				<c:when test="${subtip.rendering.triview == 'puntatore'}">
					tmpTypes[${subStatus.count - 1}][1] = '${subtip.rendering.size}';
					tmpTypes[${subStatus.count - 1}][2] = '${dyna:getTargetClass(subtip.rendering)}';
					tmpTypes[${subStatus.count - 1}][3] = '${dyna:escapeApici(subtip.rendering.display)}';
					tmpTypes[${subStatus.count - 1}][4] = '${required}';
					tmpTypes[${subStatus.count - 1}][5] = '${repetable}';
					tmpTypes[${subStatus.count - 1}][6] = '${dyna:escapeApici(onchange)}';
					tmpTypes[${subStatus.count - 1}][7] = '${ajaxValidation}';
					tmpTypes[${subStatus.count - 1}][8] = '${validationParams}';
					tmpTypes[${subStatus.count - 1}][9]	= '${dyna:escapeApici(subtip.rendering.filtro)}';
				</c:when>
				<c:when test="${subtip.rendering.triview == 'testo'}">
					tmpTypes[${subStatus.count - 1}][1] = '${subtip.rendering.dimensione.col}';
					tmpTypes[${subStatus.count - 1}][2] = '${subtip.rendering.dimensione.row}';
					tmpTypes[${subStatus.count - 1}][3] = ${subtip.rendering.multilinea};
					tmpTypes[${subStatus.count - 1}][4] = '${subtip.rendering.htmlToolbar}';
				</c:when>
				<c:when test="${subtip.rendering.triview == 'numero'}">
					tmpTypes[${subStatus.count - 1}][1] = '${subtip.rendering.size}';
				</c:when>
				<c:when test="${subtip.rendering.triview == 'boolean'}">
					tmpTypes[${subStatus.count - 1}][1] = '${ajaxValidation}';
					tmpTypes[${subStatus.count - 1}][2] = '${dyna:escapeApici(dyna:extractParameters(validationParams))}';
					tmpTypes[${subStatus.count - 1}][3] = '${dyna:escapeApici(onchange)}';
				</c:when>
				<c:when test="${subtip.rendering.triview == 'calendar'}">
					tmpTypes[${subStatus.count - 1}][1] = ${subtip.rendering.time};
				</c:when>
				<c:when test="${subtip.rendering.triview == 'alberoClassificatorio'}">
					tmpTypes[${subStatus.count - 1}][1] = '${dyna:escapeApici(subtip.rendering.alberoClassificatorio.nome)}';
					tmpTypes[${subStatus.count - 1}][2] = new Array();
					tmpTypes[${subStatus.count - 1}][3] = new Array();
		
					tmpTypes[${subStatus.count - 1}][2][0] = '';
					tmpTypes[${subStatus.count - 1}][3][0] = '...scegli opzione...';
					<c:forEach var="opt" items="${subtip.rendering.alberoClassificatorio.topClassificazioni}" varStatus="optStatus">
						tmpTypes[${subStatus.count - 1}][2][${optStatus.count}] = '${dyna:escapeApici(subtip.rendering.alberoClassificatorio.nome)}:${dyna:escapeApici(opt.codice)}';
						tmpTypes[${subStatus.count - 1}][3][${optStatus.count}]  = '${dyna:escapeApici(opt.nome)}';
					</c:forEach>
				</c:when>
			</c:choose>
		</c:forEach>
		
		var ${dynajs_var} = new DynaComboInputWithVisibility('${root}','${dynajs_var}',
									'${fn:replace(propertyPath,'anagraficadto.','')}',
									${fn:length(values)},${tipologia.rendering.inline},
									tmpShortNames,tmpLabels,tmpRepetables,tmpLabelsSize,tmpTypes,tmpNewLines,'${visibility}');
	</script>
	</c:if>

<c:choose>
<c:when test="${tipologia.rendering.inline}">
<table id="${tipologia.shortName}Table" class="dynaFieldComboValue">
	<%--<caption>${label}</caption> --%>
	<thead>
		<tr>
	<c:forEach var="subtip" items="${tipologia.rendering.sottoTipologie}">
			<th>${subtip.label}</th>
	</c:forEach>
	<c:if test="${empty disabled}">
	<c:if test="${tipologia.repeatable}">
			<th>
				&nbsp;
			</th>
	</c:if>
	</c:if>
		</tr>
	</thead>
	<tbody>
</c:when>
</c:choose>

	<c:forEach var="value" items="${values}" varStatus="rowStatus"> 
		
	<c:choose>
		<c:when test="${tipologia.rendering.inline}">
		<tr>	
		</c:when>
		<c:otherwise>
		<div class="dynaFieldComboValue">
		</c:otherwise>
	</c:choose>

		<c:forEach var="subtip" items="${tipologia.rendering.sottoTipologie}" varStatus="colStatus">

		<c:choose>
			<c:when test="${tipologia.rendering.inline}">
				<c:set var="labelMinWidth" value="" />
				<c:set var="labelStyle" value="" />
				<c:if test="${tipologia.labelMinSize > 1}">
					<c:set var="labelMinWidth" value="width:${tipologia.labelMinSize}em;" />
				</c:if>
				<c:if test="${!empty labelMinWidth}">
					<c:set var="labelStyle" value="style=\"${labelMinWidth}\"" />
				</c:if>
				
				<td ${labelStyle}>
			
			</c:when>
			
			<c:otherwise>
				<c:set var="fieldMinWidth" value="" />
				<c:set var="fieldMinHeight" value="" />
				<c:set var="fieldStyle" value="" />
				<c:if test="${subtip.fieldMinSize.col > 1}">
					<c:set var="fieldMinWidth" value="min-width:${subtip.fieldMinSize.col}em;" />
				</c:if>
				<c:if test="${subtip.fieldMinSize.row > 1}">
					<c:set var="fieldMinHeight" value="min-height:${subtip.fieldMinSize.row}em;" />
				</c:if>
				<c:if test="${!empty fieldMinHeight || !empty fieldMinWidth}">
					<c:set var="fieldStyle" value="style=\"${fieldMinHeight}${fieldMinWidth}\"" />
				</c:if>
				
				<div class="dynaField" ${fieldStyle}>
				<c:set var="labelMinWidth" value="" />
				<c:set var="labelStyle" value="" />
				<c:if test="${subtip.labelMinSize > 1}">
					<c:set var="labelMinWidth" value="width:${subtip.labelMinSize}em;" />
				</c:if>
				<c:if test="${!empty labelMinWidth}">
					<c:set var="labelStyle" value="style=\"${labelMinWidth}\"" />
				</c:if>
					<span class="dynaLabel${subtip.mandatory?'Required':''}" ${labelStyle}>${subtip.label}</span>
				<div class="dynaFieldValue">
			
			</c:otherwise>
		</c:choose>

				<%-- FIXME ctrl+C/ctrl+V da edit.tag ... --%>
				<c:if test="${subtip.mandatory != false}">
					<c:set var="required" value="true" />
				</c:if>
		
				<c:if test="${subtip.repeatable != false}">
					<c:set var="repetable" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'link'}">
					<c:set var="isLink" value="true" />
				</c:if>
				
				<c:if
					test="${subtip.rendering.triview eq 'testo' && !subtip.rendering.multilinea}">
					<c:set var="isText" value="true"/>
				</c:if>
		
				<c:if
					test="${subtip.rendering.triview eq 'testo' && subtip.rendering.multilinea}">
					<c:set var="isTextArea" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'collisioni'}">
					<c:set var="isText" value="true" />
				</c:if>
				
				<c:if test="${subtip.rendering.triview eq 'numero'}">
					<c:set var="isNumero" value="true"/>
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'soggettari'}">
					<c:set var="isSoggettario" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'alberoClassificatorio'}">
					<c:set var="isClassificazione" value="true" />
				</c:if>
		
				<c:if
					test="${subtip.rendering.triview eq 'checkradio' && repetable}">
					<c:set var="isCheckbox" value="true" />
				</c:if>
		
				<c:if
					test="${subtip.rendering.triview eq 'checkradio' && !repetable}">
					<c:set var="isRadio" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'formula'}">
					<c:set var="isFormula" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'puntatore'}">
					<c:set var="isPuntatore" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'boolean'}">
					<c:set var="isBoolean" value="true" />
				</c:if>

				<c:if test="${subtip.rendering.triview eq 'collisioni'}">
					<c:set var="isCollisione" value="true" />
				</c:if>
				
				<c:if test="${subtip.rendering.triview eq 'email'}">
					<c:set var="isEmail" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'combo'}">
					<c:set var="isCombo" value="true" />
				</c:if>
		
				<c:if test="${subtip.rendering.triview eq 'calendar'}">
					<c:set var="isCalendar" value="true" />
				</c:if>
				<c:choose>
					<c:when test="${isLink}">	
						<dyna:link propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]" 
							size="${subtip.rendering.size}" required="${required}"
							repeatable="${repetable}" onchange="${onchange}" ajaxValidation="${ajaxValidation}"	validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>		
					</c:when>
					<c:when test="${isText}">
						<dyna:text propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							size="${subtip.rendering.dimensione.col}" required="${required}"
							repeatable="${repetable}" onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>
					</c:when>
					<c:when test="${isNumero}">
						<dyna:text propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							size="${subtip.rendering.size}" required="${required}"
							repeatable="${repetable}" onchange="${onchange}" 
							cssClass="number"
							ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" visibility="${visibility}"/>
					</c:when>
					<c:when test="${isEmail}">
						<dyna:text propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							size="20" required="${required}"
							repeatable="${repetable}" onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" visibility="${visibility}"/>
					</c:when>
					<c:when test="${isTextArea}">
						<dyna:textarea propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							toolbar="${subtip.rendering.htmlToolbar}"
							rows="${subtip.rendering.dimensione.row}"
							cols="${subtip.rendering.dimensione.col}" required="${required}"
							repeatable="${repetable}" onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isPuntatore}">
						<dyna:puntatore propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							size="${subtip.rendering.size}"
							target="${dyna:getTargetClass(subtip.rendering)}"
							display="${subtip.rendering.display}" required="${required}"
							repeatable="${repetable}" onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isClassificazione}">
						<dyna:classificazione propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							required="${required}" repeatable="${repetable}"
							tree="${subtip.rendering.alberoClassificatorio}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isSoggettario}">
						<dyna:subject propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							collection="${subtip.rendering.soggettari}"
							size="${subtip.rendering.size}" required="${required}"
							repeatable="${repetable}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isCalendar}">
						<dyna:date propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							isTime="${subtip.rendering.time}"
							required="${required}" repeatable="${repetable}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" visibility="${visibility}" disabled="${disabled}"/>
					</c:when>
					<c:when test="${isCheckbox}">
						<dyna:checkbox propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							collection="${subtip.rendering.alberoClassificatorio.topClassificazioni}"
							required="${required}"
							option4row="${subtip.rendering.option4row}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isRadio}">
						<dyna:radio propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							collection="${subtip.rendering.alberoClassificatorio.topClassificazioni}"
							required="${required}"
							option4row="${subtip.rendering.option4row}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isBoolean}">
						<dyna:boolean propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							required="${required}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" />
					</c:when>
					<c:when test="${isFormula}">
						<dyna:formula propertyPath="${propertyPath}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							tipologia="${subtip}" />
					</c:when>
					<c:when test="${isCombo}">
						<dyna:combo propertyPath="${objectPath}.${propertyName}[${rowStatus.count-1}].object.anagraficaProperties[${subtip.shortName}]"
							repeatable="${repetable}"
							tipologia="${subtip}"
							required="${required}"
							onchange="${onchange}" ajaxValidation="${ajaxValidation}" validationParams="${validationParams}" visibility="${visibility}"/>
					</c:when>
				</c:choose> 
				<%-- FIXME FINE ctrl+C/ctrl+V da edit.tag ... --%>
				<c:set var="isLink" value="false" />
				<c:set var="isText" value="false" />
				<c:set var="isEmail" value="false" />
				<c:set var="isTextArea" value="false" />
				<c:set var="isPuntatore" value="false" />
				<c:set var="isNumero" value="false" />
				<c:set var="isClassificazione" value="false" />
				<c:set var="isSoggettario" value="false" />
				<c:set var="isCalendar" value="false" />
				<c:set var="isCheckbox" value="false" />
				<c:set var="isRadio" value="false" />
				<c:set var="isBoolean" value="false" />
				<c:set var="isFormula" value="false" />
				<c:set var="isCombo" value="false" />
				<c:set var="required" value="false" />
				<c:set var="repetable" value="false" />
				<c:choose>
				<c:when test="${tipologia.rendering.inline}">
			</td>	
				</c:when>
				<c:otherwise>
			</div>
		</div>
<c:if test="${subtip.newline}">
	<div class="dynaClear">&nbsp;</div>
</c:if>
			
				</c:otherwise>
				</c:choose>
			
		</c:forEach>
		<c:if test="${empty disabled}">
		<c:choose>
		<c:when test="${tipologia.rendering.inline}">
		<c:if test="${tipologia.repeatable}">
				<td>
					<c:choose>
					<c:when test="${rowStatus.count == fn:length(values)}">
						<img id="addButton${inputName}" src="${root}/image/jdyna/main_plus.gif" class="addButton"
							onclick="${dynajs_var}.create(this)" />
					</c:when>
					<c:otherwise>
						<img src="${root}/image/jdyna/delete_icon.gif" class="deleteButton"
							onclick="${dynajs_var}.remove(${rowStatus.count-1},this)" />
					</c:otherwise>
					</c:choose>
				</td>
		</c:if>
			</tr>	
		</c:when>
		<c:otherwise>
			</div>
		<c:if test="${tipologia.repeatable}">
			<c:choose>
			<c:when test="${rowStatus.count == fn:length(values)}">
				<img id="addButton${inputName}" src="${root}/image/jdyna/main_plus.gif" class="addButton"
					onclick="${dynajs_var}.create(this)" />
			</c:when>
			<c:otherwise>
				<img src="${root}/image/jdyna/delete_icon.gif" class="deleteButton"
					onclick="${dynajs_var}.remove(${rowStatus.count-1},this)" />
			</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${tipologia.repeatable && !tipologia.rendering.inline}">
			<div class="dynaClear">&nbsp;</div>
		</c:if>
		</c:otherwise>
		</c:choose>
		</c:if>
	</c:forEach>
	
<c:choose>
<c:when test="${tipologia.rendering.inline}">
	</tbody>	
</table>
</c:when>
</c:choose>