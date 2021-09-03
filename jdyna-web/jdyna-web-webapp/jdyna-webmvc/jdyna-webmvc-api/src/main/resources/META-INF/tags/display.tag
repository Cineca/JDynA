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
<%@ attribute name="values" required="true" type="java.util.Collection" %>
<%@ attribute name="tipologia" required="true" type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="subElement" required="false" type="java.lang.Boolean"%>
<%@ attribute name="editMode" required="false" type="java.lang.Boolean"%>
<%@ attribute name="hideLabel" required="false" type="java.lang.Boolean"%>
<%@ attribute name="admin" required="false"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>

<%@tag import="java.text.MessageFormat"%>
<%@tag import="it.cilea.osd.jdyna.widget.WidgetTesto"%>
<%@tag import="it.cilea.osd.jdyna.model.PropertiesDefinition"%>

<%@ include file="/META-INF/taglibs4dynatag.jsp"%>


<c:if test="${tipologia.help != null}">
	<c:set var="helpHTML" value="${dyna:escapeHTMLtoJavascript(tipologia.help)}" />
</c:if>		

<c:if test="${tipologia.mandatory != false}">
	<c:set var="required" value="true" />
</c:if>		

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

<c:if test="${tipologia.rendering.triview eq 'collisioni'}">
	<c:set var="isText" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'numero'}">
	<c:set var="isNumero" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'formula'}">
	<c:if test="${tipologia.rendering.resultTriview eq 'numero'}">
		<c:set var="isNumero" value="true" />
	</c:if>
	<c:if
		test="${tipologia.rendering.resultTriview eq 'testo' && !(tipologia.rendering.dimensione.row gt 1)}">
		<c:set var="isText" value="true" />
	</c:if>
	<c:if test="${tipologia.rendering.resultTriview eq 'testo' && (tipologia.rendering.dimensione.row gt 1)}">
		<c:set var="isTextArea" value="true" />
	</c:if>
	<c:if test="${tipologia.rendering.resultTriview eq 'classificationTree'}">	
		<c:set var="isClassificazione" value="true" />
	</c:if>
	<c:if
		test="${tipologia.rendering.resultTriview eq 'checkradio' && repetable}">
		<c:set var="isCheckbox" value="true" />
	</c:if>
	<c:if
		test="${tipologia.rendering.resultTriview eq 'checkradio' && !repetable}">
		<c:set var="isRadio" value="true" />
	</c:if>
	<c:set var="isFormula" value="true" />
</c:if>


<c:if test="${tipologia.rendering.triview eq 'soggettari'}">
	<c:set var="isSoggettario" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'classificationTree'}">	
	<c:set var="isClassificazione" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && (empty tipologia.rendering.dropdown || tipologia.rendering.dropdown == false) && repetable}">
	<c:set var="isCheckbox" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'checkradio' && (empty tipologia.rendering.dropdown || tipologia.rendering.dropdown == false) && !repetable}">
	<c:set var="isRadio" value="true" />
</c:if>


<c:if test="${tipologia.rendering.triview eq 'checkradio' && !empty tipologia.rendering.dropdown && tipologia.rendering.dropdown && !repetable}">
	<c:set var="isDropdown" value="true" />
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
<c:if test="${tipologia.rendering.triview eq 'collisioni'}">
	<!-- Se sono attive le collisioni il display non ne è influenzato! -->
	<c:set var="isText" value="true" />
</c:if>

<c:if test="${tipologia.rendering.triview eq 'calendar'}">
	<c:set var="isCalendar" value="true" />
</c:if>
<c:set var="showit" value="false" target="java.lang.Boolean"/>

<c:choose>
<c:when test="${editMode}">
	<c:set var="showit" value="true" target="java.lang.Boolean"/>
	<c:if test="${subElement}">
		<c:if test="${!admin && tipologia.accessLevel == 1}">
			<c:set var="showit" value="false" target="java.lang.Boolean"/>		
		</c:if>
	</c:if>
</c:when>
<c:otherwise>

		<c:forEach var="value" items="${values}" varStatus="valueStatus">		
				<c:if test="${value.visibility==1}">
					<c:set var="showit" value="true" target="java.lang.Boolean"/>
				</c:if>
		</c:forEach>

</c:otherwise>
</c:choose>

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

<c:if test="${showit}">
<div class="dynaField" ${fieldStyle}>
<c:set var="labelMinWidth" value="" />
<c:set var="labelStyle" value="" />
<c:if test="${tipologia.labelMinSize > 1}">
	<c:set var="labelMinWidth" value="width:${tipologia.labelMinSize}${tipologia.labelMinSizeUnit};" />
</c:if>
<c:if test="${!empty labelMinWidth}">
	<c:set var="labelStyle" value="style=\"${labelMinWidth}\"" />
</c:if>
<c:if test="${!empty tipologia.label && !hideLabel && !isBoolean}">
<span class="dynaLabel" ${labelStyle}>${tipologia.label}</span>
</c:if>

<div id="${tipologia.shortName}Div" class="dynaFieldValue">
</c:if>
</c:if>

<c:set var="appendPreviousBR" value="true" target="java.lang.Boolean"/>
<c:if test="${showit}">
<c:choose>
	<c:when test="${isFile}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<%--<c:set var="minheight" value="" />--%>
			<c:set var="minwidth" value="" />
			<c:set var="style" value="" />
			<%--<c:if test="${tipologia.rendering.dimensione.row > 1}">
				<c:set var="minheight" value="min-height: ${tipologia.rendering.dimensione.row}em;" />
			</c:if>--%>
			<c:if test="${tipologia.rendering.size > 1}">
				<c:set var="minwidth" value="min-width: ${tipologia.rendering.size}em;" />
			</c:if>
			<%--<c:if test="${!empty minheight || !empty minwidth}">
				<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
			</c:if>--%>
			<c:if test="${!empty minwidth && !subElement}">
				<c:set var="style" value="style=\"${minwidth}\"" />
			</c:if>
			<c:set var="displayObject" value="${(dyna:display(tipologia,value.value.real))}" />			
			<c:choose>
				<c:when test="${!empty dyna:getFileIsOnServer(displayObject)}">			
				
				<c:choose>
				<c:when test="${tipologia.rendering.showPreview}">

					<c:choose>
					<c:when test="${dyna:thumbnailExists(displayObject)}">

						<div class="image">
							<a target="_blank" href="<%=request.getContextPath()%>/${tipologia.rendering.servletPath}/${dyna:getFileFolder(displayObject)}?filename=${dyna:getFileName(displayObject)}">
								<img id="picture" name="picture"
									alt="${dyna:getFileName(displayObject)} picture"
									src="<%=request.getContextPath()%>/${tipologia.rendering.servletPath}/${dyna:getFileFolder(displayObject)}?filename=${dyna:getThumbnailFileName(displayObject)}"
									title="A preview ${dyna:getFileName(displayObject)} picture" />
							</a>
						</div>

					</c:when>
					<c:otherwise>

						<div class="image">
							<img id="picture" name="picture"
								alt="${dyna:getFileName(displayObject)} picture"
								src="<%=request.getContextPath()%>/${tipologia.rendering.servletPath}/${dyna:getFileFolder(displayObject)}?filename=${dyna:getFileName(displayObject)}"
								title="A preview ${dyna:getFileName(displayObject)} picture" />
						</div>

					</c:otherwise>
					</c:choose>
					
												
				</c:when>				
				<c:otherwise>
					<a target="_blank" href="<%=request.getContextPath()%>/${tipologia.rendering.servletPath}/${dyna:getFileFolder(displayObject)}?filename=${dyna:getFileName(displayObject)}">
						<span ${style}>${tipologia.rendering.labelAnchor}</span>
					</a>
				</c:otherwise>
				</c:choose>
				
				</c:when>
				<c:otherwise>
				
						<img id="picture" name="picture" alt="No image" src="<%=request.getContextPath() %>/image/authority/noimage.jpg"
							title="No picture for ${tipologia.shortName}" />
							
				
				</c:otherwise>
			</c:choose>
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isLink}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<%--<c:set var="minheight" value="" />--%>
			<c:set var="minwidth" value="" />
			<c:set var="style" value="" />
			<%--<c:if test="${tipologia.rendering.dimensione.row > 1}">
				<c:set var="minheight" value="min-height: ${tipologia.rendering.dimensione.row}em;" />
			</c:if>--%>
			<c:if test="${tipologia.rendering.size > 1}">
				<c:set var="minwidth" value="min-width: ${tipologia.rendering.size}em;" />
			</c:if>
			<%--<c:if test="${!empty minheight || !empty minwidth}">
				<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
			</c:if>--%>
			<c:if test="${!empty minwidth && !subElement}">
				<c:set var="style" value="style=\"${minwidth}\"" />
			</c:if>
			<c:set var="displayObject" value="${(dyna:display(tipologia,value.value.real))}" />
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
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isText}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<%--<c:set var="minheight" value="" />--%>
			<c:set var="minwidth" value="" />
			<c:set var="style" value="" />
			<%--<c:if test="${tipologia.rendering.dimensione.row > 1}">
				<c:set var="minheight" value="min-height: ${tipologia.rendering.dimensione.row}em;" />
			</c:if>--%>
			<c:if test="${tipologia.rendering.dimensione.col > 1}">
				<c:set var="minwidth" value="min-width: ${tipologia.rendering.dimensione.col}em;" />
			</c:if>
			<%--<c:if test="${!empty minheight || !empty minwidth}">
				<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
			</c:if>--%>
			<c:if test="${!empty minwidth && !subElement}">
				<c:set var="style" value="style=\"${minwidth}\"" />
			</c:if>
			<c:set var="displayObject" value="${value.value.real}" />
			<c:choose>
			<c:when test="${!empty tipologia.rendering.displayFormat}">						
				${dyna:messageFormat(tipologia.rendering.displayFormat, displayObject)}
			</c:when>
			<c:otherwise>
				<span ${style}>${displayObject}</span>
			</c:otherwise>
			</c:choose>			
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isTextArea}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="minheight" value="" />
			<c:set var="minwidth" value="" />
			<c:set var="style" value="" />
			<c:if test="${tipologia.rendering.dimensione.row > 1}">
				<c:set var="minheight" value="min-height: ${tipologia.rendering.dimensione.row}em;" />
			</c:if>
			<c:if test="${tipologia.rendering.dimensione.col > 1}">
				<c:set var="minwidth" value="min-width: ${tipologia.rendering.dimensione.col}em;" />
			</c:if>
			<c:if test="${!empty minheight || !empty minwidth}">
				<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
			</c:if>
			<c:set var="displayObject" value="${value.value.real}" />
			<div ${style}>${(empty tipologia.rendering.htmlToolbar or tipologia.rendering.htmlToolbar eq 'nessuna'?(dyna:nl2br(displayObject)):displayObject)}</div>
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>	
	<c:when test="${isClassificazione}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="displayObject" value="${value.value.real}" />
			<c:set var="displayClassification" value="${dyna:getDisplayValue(displayObject,tipologia.rendering.display)}" />
			${displayClassification}
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isRadio || isCheckbox || isDropdown}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="displayObject" value="${dyna:getCheckRadioDisplayValue(tipologia.rendering.staticValues, value.value.real)}" />
			${displayObject}
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isSoggettario}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="displayObject" value="${value.value.real.voce}" />
			${displayObject}
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>	
		</c:forEach>
	</c:when>
	<c:when test="${isPuntatore}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="displayObject" value="${value.value.real}" />
			<c:set var="displayPointer" value="${dyna:getDisplayValue(displayObject,tipologia.rendering.display)}" />
			<c:choose>
				<c:when test="${!empty tipologia.rendering.urlPath and (admin or displayObject.status)}">
					<a href="${root}/${dyna:getDisplayValue(displayObject,tipologia.rendering.urlPath)}">${displayPointer}</a>
				</c:when>
				<c:otherwise>
					${displayPointer}
				</c:otherwise>
			</c:choose>
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>		
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isCustomPointer}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="propertydefinition" value="${value.typo}" />
			<c:set var="displayPointer" value="${dyna:displayAdvanced(propertydefinition,value.value.real)}" />
			${displayPointer}
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>		
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>	
	<c:when test="${isBoolean}">
		<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="displayObject" value="${value.value.real}" />
			<c:choose>
  				<c:when test="${displayObject}">
  					<div class="label label-success">${tipologia.label}</div>
				</c:when>
				<c:otherwise>
					<c:if test="${!tipologia.rendering.hideWhenFalse}">
						<div class="label label-default">${tipologia.label}</div>
					</c:if>
				</c:otherwise>
			</c:choose>
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:when test="${isNumero}">
	<c:set var="minwidth" value="" />
		<c:set var="style" value="" />
		<c:if test="${tipologia.rendering.size > 1}">
			<c:set var="minwidth" value="width: ${tipologia.rendering.size}em;" />
		</c:if>
		<c:if test="${!empty minwidth && !subElement}">
			<c:set var="style" value="style=\"${minwidth}\"" />
		</c:if>			
	
	  	<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<div class="number" ${style}>${dyna:display(tipologia,value.value.real)}</div>
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>		
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:when>
	<c:otherwise>
	  	<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<c:set var="displayObject" value="${value.value.real}" />
			${dyna:display(tipologia,displayObject)}
			<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${value.visibility==1}">
  					<img src="${root}/image/jdyna/checkbox.png" class="jdyna-icon"/>
				</c:when>
				<c:otherwise>
					<img src="${root}/image/jdyna/checkbox_unchecked.png" class="jdyna-icon"/>
				</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="appendPreviousBR" value="false" target="java.lang.Boolean"/>
		</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:otherwise>
	</c:choose>
<c:if test="${!subElement}">
</div>
</div>
<c:if test="${tipologia.newline}">
	<div class="dynaClear">&nbsp;</div>
</c:if>
</c:if>

</c:if>
