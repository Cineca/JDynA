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
<%@ attribute name="subValues" required="false"
	type="java.util.Collection"%>
<%@ attribute name="subtip" required="true"
	type="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<%@ attribute name="subElement" required="false" type="java.lang.Boolean"%>
<%@ attribute name="editMode" required="false" type="java.lang.Boolean"%>
<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<%@tag import="it.cilea.osd.jdyna.model.AValue"%>
<%@tag import="java.text.MessageFormat"%>
<%@tag import="it.cilea.osd.jdyna.widget.WidgetTesto"%>
<%@tag import="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
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

<c:if test="${subtip.rendering.triview eq 'file'}">
	<c:set var="isFile" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'formula'}">
	<c:if test="${subtip.rendering.resultTriview eq 'numero'}">
		<c:set var="isSubNumero" value="true" />
	</c:if>
	<c:if test="${subtip.rendering.resultTriview eq 'link'}">
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
		test="${tipologia.rendering.resultTriview eq 'classificationTree'}">
		<c:set var="isClassificazione" value="true" />
	</c:if>
	<c:set var="isSubFormula" value="true" />
</c:if>


<c:if test="${subtip.rendering.triview eq 'soggettari'}">
	<c:set var="isSubSoggettario" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'classificationTree'}">
	<c:set var="isSubClassificazione" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'checkradio' && (empty subtip.rendering.dropdown || subtip.rendering.dropdown == false) && repetable}">
	<c:set var="isSubCheckbox" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'checkradio' && (empty subtip.rendering.dropdown || subtip.rendering.dropdown == false) && !repetable}">
	<c:set var="isSubRadio" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'checkradio' && !empty subtip.rendering.dropdown && subtip.rendering.dropdown}">
	<c:set var="isSubDropdown" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'pointer'}">
	<c:set var="isSubPuntatore" value="true" />
</c:if>

<c:if test="${subtip.rendering.triview eq 'custompointer'}">
	<c:set var="isSubCustomPointer" value="true" />
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
<c:when test="${editMode}">
	<c:set var="subshowit" value="true" target="java.lang.Boolean" />
</c:when>
<c:otherwise>

		<c:forEach var="subvalue" items="${subValues}" varStatus="valueStatus">

			<c:if test="${subvalue.visibility==1}">
				<c:set var="subshowit" value="true" target="java.lang.Boolean" />
			</c:if>
		</c:forEach>
</c:otherwise>
</c:choose>

<c:if test="${!subElement}">
	<c:set var="fieldMinWidth" value="" />
	<c:set var="fieldMinHeight" value="" />
	<c:set var="fieldStyle" value="" />
	<c:if test="${subtip.fieldMinSize.col > 1}">
		<c:set var="fieldMinWidth"
			value="min-width:${subtip.fieldMinSize.col}${subtip.fieldMinSize.measurementUnitCol};" />
	</c:if>
	<c:if test="${subtip.fieldMinSize.row > 1}">
		<c:set var="fieldMinHeight"
			value="min-height:${subtip.fieldMinSize.row}${subtip.fieldMinSize.measurementUnitRow};" />
	</c:if>
	<c:if test="${!empty fieldMinHeight || !empty fieldMinWidth}">
		<c:set var="fieldStyle" value="style=\" ${fieldMinHeight}${fieldMinWidth}\"" />
	</c:if>
	<c:if test="${subshowit}">
		<div class="dynaField"${fieldStyle}><c:set var="labelMinWidth"
			value="" /> <c:set var="labelStyle" value="" /> <c:if
			test="${subtip.labelMinSize > 1}">
			<c:set var="labelMinWidth" value="width:${subtip.labelMinSize}${subtip.labelMinSizeUnit};" />
		</c:if> <c:if test="${!empty labelMinWidth}">
			<c:set var="labelStyle" value="style=\" ${labelMinWidth}\"" />
		</c:if> <span class="dynaLabel"${labelStyle}>${subtip.label}</span>
		<div class="dynaFieldValue">
	</c:if>
</c:if>

<c:set var="appendPreviousBR" value="true" target="java.lang.Boolean"/>
<c:if test="${subshowit}">
	<c:choose>
		<c:when test="${isFile}">
			
		<c:forEach var="value" items="${subValues}" varStatus="valueStatus">
		
		<c:choose>
		<c:when test="${value.visibility == 1 || editMode}">
			<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
			<%--<c:set var="minheight" value="" />--%>
			<c:set var="minwidth" value="" />
			<c:set var="style" value="" />
			<%--<c:if test="${tipologia.rendering.dimensione.row > 1}">
				<c:set var="minheight" value="min-height: ${tipologia.rendering.dimensione.row}em;" />
			</c:if>--%>
			<c:if test="${subtip.rendering.size > 1}">
				<c:set var="minwidth" value="min-width: ${subtip.rendering.size}em;" />
			</c:if>
			<%--<c:if test="${!empty minheight || !empty minwidth}">
				<c:set var="style" value="style=\"${minheight}${minwidth}\"" />
			</c:if>--%>
			<c:if test="${!empty minwidth && !subElement}">
				<c:set var="style" value="style=\"${minwidth}\"" />
			</c:if>
			<c:set var="displayObject" value="${dyna:display(subtip,value.object)}" />
			<c:choose>
				<c:when test="${!empty dyna:getFileIsOnServer(displayObject)}">			
				
				<c:choose>
				<c:when test="${subtip.rendering.showPreview}">
				
					<div class="image">
						<img id="picture" name="picture"
							alt="${dyna:getFileName(displayObject)} picture"
							src="<%=request.getContextPath()%>/${subtip.rendering.servletPath}/${dyna:getFileFolder(displayObject)}?filename=${dyna:getFileName(displayObject)}"
							title="A preview ${dyna:getFileName(displayObject)} picture" />
					</div>
					
												
				</c:when>				
				<c:otherwise>
					<a target="_blank" href="<%=request.getContextPath()%>/${subtip.rendering.servletPath}/${dyna:getFileFolder(displayObject)}?filename=${dyna:getFileName(displayObject)}">
						<span ${style}>${subtip.rendering.labelAnchor}</span>
					</a>
				</c:otherwise>
				</c:choose>
				
				</c:when>
				<c:otherwise>
				
						<img id="picture" name="picture" alt="No image" src="<%=request.getContextPath() %>/image/authority/noimage.jpg"
							title="No picture for ${subtip.shortName}" />
							
				
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
			<c:forEach var="value" items="${subValues}" varStatus="valueStatus">

				<c:choose>
				<c:when test="${value.visibility == 1 || editMode}">
					<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
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
						value="${dyna:display(subtip,value.object)}" />
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
		<c:when test="${isSubText}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
					<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
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
					<c:set var="style" value="style=\" ${minwidth}\"" />
				</c:if>
				<c:choose>
				<c:when test="${!empty subtip.rendering.displayFormat}">
					${dyna:messageFormat(subtip.rendering.displayFormat, subValue.object)}
				</c:when>
				<c:otherwise>
					<span ${style}>${subValue.object}</span>
				</c:otherwise>
				</c:choose>					
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
		<c:when test="${isSubTextArea}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
					<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
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
				<div ${style}>${subtip.rendering.htmlToolbar eq
				'nessuna'?dyna:nl2br(subValue.object):subValue.object}</div>
				
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
		<c:when
			test="${isSubRadio || isSubCheckbox || isSubDropdown}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
				<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
				<c:set var="displayObject" value="${dyna:getCheckRadioDisplayValue(tipologia.rendering.staticValues, value.value.real)}" />
				${displayObject}
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
		<c:when
			test="${isSubClassificazione}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
				<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
				${subValue.object.nome}
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
		<c:when test="${isSubPuntatore}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
				<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>	
				<c:set var="displayObject" value="${subValue.value.real}" />						
				<c:set var="displayPointer" value="${dyna:getDisplayValue(displayObject,subtip.rendering.display)}" />
				<c:choose>
				<c:when test="${!empty subtip.rendering.urlPath}">						
					<a href="${root}/${dyna:getDisplayValue(displayObject,subtip.rendering.urlPath)}">${displayPointer}</a>
				</c:when>
				<c:otherwise>
					${displayPointer}
				</c:otherwise>
				</c:choose>
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
		<c:when test="${isSubCustomPointer}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
				<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>	
				<c:set var="displayObject" value="${subValue.value.real}" />						
				<c:set var="displayPointer" value="${dyna:displayAdvanced(displayObject,subValue.value.real)}" />
				${displayPointer}
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
		<c:when test="${isSubBoolean}">
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
				<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
								${subValue.object?'Si':'No'}
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
					<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
				<div class="number"${style}>${dyna:display(subtip,subValue.object)}</div>
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
			<c:forEach var="subValue" items="${subValues}"
				varStatus="valueStatus">
				<c:choose>
				<c:when test="${subValue.visibility == 1 || editMode}">
				<c:if test="${(valueStatus.count != 1 && appendPreviousBR)}"><br/></c:if>
				${dyna:display(subtip,subValue.object)}
				<c:if test="${editMode}">
  				<c:choose>
  				<c:when test="${subValue.visibility==1}">
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
	</div>
	</div>
	<c:if test="${subtip.newline}">
		<div class="dynaClear">&nbsp;</div>
	</c:if>
</c:if>
