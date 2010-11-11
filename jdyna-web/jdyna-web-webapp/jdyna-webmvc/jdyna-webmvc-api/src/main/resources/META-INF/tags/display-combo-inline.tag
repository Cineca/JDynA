<%@ attribute name="subValues" required="false"
	type="java.util.Collection"%>
<%@ attribute name="subtip" required="true"
	type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<%-- //FIXME Copia incolla da display.tag... --%>

<c:if
	test="${subtip.rendering.triview eq 'testo' && !subtip.rendering.multilinea}">
	<c:set var="isSubText" value="true" />
</c:if>

<c:if
	test="${subtip.rendering.triview eq 'testo' && subtip.rendering.multilinea}">
	<c:set var="isSubTextArea" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'collisioni'}">
	<c:set var="isSubText" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'numero'}">
	<c:set var="isSubNumero" value="true" />
</c:if>
<c:if test="${subtip.rendering.triview eq 'link'}">
	<c:set var="isLink" value="true" />
</c:if>
<c:if test="${subtip.rendering.triview eq 'formula'}">
	<c:if test="${subtip.rendering.resultTriview eq 'numero'}">
		<c:set var="isSubNumero" value="true" />
	</c:if>
	<c:if test="${subtip.rendering.triview eq 'link'}">
		<c:set var="isLink" value="true" />
	</c:if>
	<c:if
		test="${subtip.rendering.resultTriview eq 'testo' && !(subtip.rendering.dimensione.row gt 1)}">
		<c:set var="isSubText" value="true" />
	</c:if>
	<c:if
		test="${subtip.rendering.resultTriview eq 'testo' && (subtip.rendering.dimensione.row gt 1)}">
		<c:set var="isSubTextArea" value="true" />
	</c:if>
	<c:if
		test="${subtip.rendering.resultTriview eq 'checkradio' && repetable}">
		<c:set var="isSubCheckbox" value="true" />
	</c:if>
	<c:if
		test="${subtip.rendering.resultTriview eq 'checkradio' && !repetable}">
		<c:set var="isSubRadio" value="true" />
	</c:if>
	<c:if
		test="${tipologia.rendering.resultTriview eq 'alberoClassificatorio'}">
		<c:set var="isClassificazione" value="true" />
	</c:if>
	<c:set var="isSubFormula" value="true" />
</c:if>


<c:if test="${subtip.rendering.triview eq 'soggettari'}">
	<c:set var="isSubSoggettario" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'alberoClassificatorio'}">
	<c:set var="isSubClassificazione" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'checkradio' && repetable}">
	<c:set var="isSubCheckbox" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'checkradio' && !repetable}">
	<c:set var="isSubRadio" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'puntatore'}">
	<c:set var="isSubPuntatore" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'boolean'}">
	<c:set var="isSubBoolean" value="true" />
</c:if>
<c:if test="${subtip.rendering.triview eq 'collisioni'}">
	<!-- Se sono attive le collisioni il display non ne è influenzato! -->
	<c:set var="isSubText" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'calendar'}">
	<c:set var="isSubCalendar" value="true" />
</c:if>


<c:set var="subshowit" value="false" target="java.lang.Boolean" />


<c:choose>
	<c:when test="${!isCombo}">
		<c:forEach var="subvalue" items="${subValues}" varStatus="valueStatus">

			<c:if test="${subvalue.visibility == 1}">
				<c:set var="subshowit" value="true" target="java.lang.Boolean" />
			</c:if>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:set var="subshowit" value="true" target="java.lang.Boolean" />
	</c:otherwise>
</c:choose>

<c:if test="${!subElement}">
	<c:set var="fieldMinWidth" value="" />
	<c:set var="fieldMinHeight" value="" />
	<c:set var="fieldStyle" value="" />
	<c:if test="${subtip.fieldMinSize.col > 1}">
		<c:set var="fieldMinWidth"
			value="min-width:${subtip.fieldMinSize.col}em;" />
	</c:if>
	<c:if test="${subtip.fieldMinSize.row > 1}">
		<c:set var="fieldMinHeight"
			value="min-height:${subtip.fieldMinSize.row}em;" />
	</c:if>
	<c:if test="${!empty fieldMinHeight || !empty fieldMinWidth}">
		<c:set var="fieldStyle" value="style=\" ${fieldMinHeight}${fieldMinWidth}\"" />
	</c:if>
	<c:if test="${subshowit}">
		<div class="dynaField"${fieldStyle}><c:set var="labelMinWidth"
			value="" /> <c:set var="labelStyle" value="" /> <c:if
			test="${subtip.labelMinSize > 1}">
			<c:set var="labelMinWidth" value="width:${subtip.labelMinSize}em;" />
		</c:if> <c:if test="${!empty labelMinWidth}">
			<c:set var="labelStyle" value="style=\" ${labelMinWidth}\"" />
		</c:if> <span class="dynaLabel"${labelStyle}>${subtip.label}:</span>
		<div id="${subtip.shortName}Div" class="dynaFieldValue">
	</c:if>
</c:if>
<c:if test="${subshowit}">
	<c:choose>
		<c:when test="${isLink}">
			<c:forEach var="value" items="${subValues}" varStatus="valueStatus">

				<c:if test="${value.visibility == 1}">
					<c:if test="${valueStatus.count != 1}">
						<br />
					</c:if>
					<%--<c:set var="minheight" value="" />--%>
					<c:set var="minwidth" value="" />
					<c:set var="style" value="" />
					<%--<c:if test="${tipologia.rendering.dimensione.row > 1}">
									<c:set var="minheight" value="min-height: ${tipologia.rendering.dimensione.row}em;" />
								</c:if>--%>
					<c:if test="${subtip.rendering.size > 1}">
						<c:set var="minwidth"
							value="min-width: ${subtip.rendering.size}em;" />
					</c:if>
					<%--<c:if test="${!empty minheight || !empty minwidth}">
									<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
								</c:if>--%>
					<c:if test="${!empty minwidth && !subElement}">
						<c:set var="style" value="style=\" ${minwidth}\"" />
					</c:if>
					<c:set var="displayObject"
						value="${dyna:display(subtip,value.value.real)}" />
					<c:choose>
				<c:when test="${!empty dyna:getLinkValue(displayObject)}">			
				<a target="_blank" href="${dyna:getLinkValue(displayObject)}">
				<c:choose>
				<c:when test="${!empty dyna:getLinkDescription(displayObject)}">
					<span ${style}>${dyna:getLinkDescription(displayObject)}</span>
				</c:when>
				<c:otherwise>
					<span ${style}>${dyna:getLinkValue(displayObject)}</span>
				</c:otherwise>
				</c:choose>
				</a>
				</c:when>
				<c:otherwise>
				
				<c:if test="${!empty dyna:getLinkDescription(displayObject)}">
					<span ${style}>${dyna:getLinkDescription(displayObject)}</span>
				</c:if>
							
				
				</c:otherwise>
				</c:choose>
				</c:if>
			</c:forEach>
		</c:when>
		<c:when test="${isSubText}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:if test="${valueStatus.count != 1}">
					<br />
				</c:if>
				<%--<c:set var="minheight" value="" />--%>
				<c:set var="minwidth" value="" />
				<c:set var="style" value="" />
				<%--<c:if test="${subtip.rendering.dimensione.row > 1}">
									<c:set var="minheight" value="min-height: ${subtip.rendering.dimensione.row}em;" />
								</c:if>--%>
				<c:if test="${subtip.rendering.dimensione.col > 1}">
					<c:set var="minwidth"
						value="min-width: ${subtip.rendering.dimensione.col}em;" />
				</c:if>
				<%--<c:if test="${!empty minheight || !empty minwidth}">
									<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
								</c:if>--%>
				<c:if test="${!empty minwidth && !subElement}">
					<c:set var="style" value=" style=\" ${minwidth}\"" />
				</c:if>
				<span${style}>${subValue.value.real}</span>
			</c:forEach>
		</c:when>
		<c:when test="${isSubTextArea}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:set var="minheight" value="" />
				<c:set var="minwidth" value="" />
				<c:set var="style" value="" />
				<c:if test="${subtip.rendering.dimensione.row > 1}">
					<c:set var="minheight"
						value="min-height: ${subtip.rendering.dimensione.row}em;" />
				</c:if>
				<c:if test="${subtip.rendering.dimensione.col > 1}">
					<c:set var="minwidth"
						value="min-width: ${subtip.rendering.dimensione.col}em;" />
				</c:if>
				<c:if test="${!empty minheight || !empty minwidth}">
					<c:set var="style" value="style=\" ${minheight}${minwidth}\"" />
				</c:if>
				<div${style}>${subtip.rendering.htmlToolbar eq
				'nessuna'?dyna:nl2br(subValue.value.real):subValue.value.real}</div>
			</c:forEach>
		</c:when>
		<c:when
			test="${isSubClassificazione|| isSubSoggettario || isSubRadio || isSubCheckbox}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:if test="${valueStatus.count != 1}">
					<br />
				</c:if>
								${subValue.value.real.nome}
							</c:forEach>
		</c:when>
		<c:when test="${isSubPuntatore}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:if test="${valueStatus.count != 1}">
					<br />
				</c:if>
								${dyna:getDisplayValue(subValue.value.real,subtip.rendering.display)}
							</c:forEach>
		</c:when>
		<c:when test="${isSubBoolean}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:if test="${valueStatus.count != 1}">
					<br />
				</c:if>
								${subValue.value.real?'Si':'No'}
							</c:forEach>
		</c:when>

		<c:when test="${isSubNumero}">
			<c:set var="minwidth" value="" />
			<c:set var="style" value="" />
			<c:if test="${subtip.rendering.size > 1}">
				<c:set var="minwidth" value="width: ${subtip.rendering.size}em;" />
			</c:if>
			<c:if test="${!empty minwidth && !subElement}">
				<c:set var="style" value="style=\" ${minwidth}\"" />
			</c:if>

			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:if test="${valueStatus.count != 1}">
					<br />
				</c:if>
				<div class="number"${style}>${dyna:display(subtip,subValue.value.real)}</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:if test="${valueStatus.count != 1}">
					<br />
				</c:if>
								${dyna:display(subtip,subValue.value.real)}
							</c:forEach>
		</c:otherwise>
	</c:choose>
	</div>
	</div>
	<c:if test="${subtip.newline}">
		<div class="dynaClear">&nbsp;</div>
	</c:if>
</c:if>