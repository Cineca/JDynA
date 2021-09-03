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
<%@ attribute name="typeDefinition" required="true" type="it.cilea.osd.jdyna.model.ADecoratorTypeDefinition"%>
<%@ attribute name="editmode" required="false" type="java.lang.Boolean"%>
<%@ attribute name="parentID" required="true"%>
<%@ attribute name="specificPartPath" required="true"%>
<%@ attribute name="admin" required="false"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:set var="subTypesSortered" value="${dyna:sortList(typeDefinition.real.mask)}" />																			
<c:set var="showeditbutton" value="false" target="java.lang.Boolean" />
<c:set var="count" value="0" />

<%-- TODO not used showpreferredbutton/showstatusbutton: use to control whetever show or not the visibility checkbox and the preferred button--%>
<c:set var="showpreferredbutton" value="true" target="java.lang.Boolean" />
<c:set var="showstatusbutton" value="false" target="java.lang.Boolean" />	

<c:choose>								  						
	<c:when test="${!typeDefinition.real.inline}">													
												
	<display:table name="${values}" cellspacing="0" cellpadding="0" uid="${typeDefinition.real.id}"
		class="dynaFieldComboValue" requestURI="" sort="list" export="false" pagesize="100">
	<display:setProperty name="paging.banner.no_items_found" value="" />
	<display:setProperty name="paging.banner.one_item_found" value="" />
	<display:setProperty name="paging.banner.all_items_found" value="" />
	<display:setProperty name="paging.banner.page.selected" value="" />
	<display:setProperty name="paging.banner.onepage" value="" />
	
	<c:forEach var="subtip" items="${subTypesSortered}" varStatus="valueStatus">
			<c:set var="subLabelMinWidth" value="" />
			<c:if test="${subtip.labelMinSize > 1}">
				<c:set var="subLabelMinWidth" value="width:${subtip.labelMinSize}${subtip.labelMinSizeUnit};" />
			</c:if>

			<c:if test="${subtip.accessLevel > 1}">
				<c:set var="showeditbutton" value="true" target="java.lang.Boolean"/>
			</c:if>
			<c:set var="hidecolumn" value=""/>
			<c:set var="hidecolumnclass" value=""/>
			<c:if test="${editmode && !admin && subtip.accessLevel == 1}">
				<c:set var="hidecolumn" value="display:none;"/>
				<c:set var="hidecolumnclass" value="hidecolumn"/>
			</c:if>
			<c:if test="${!editmode && !admin}"> 
				<c:set var="hidecolumn" value="display:none;"/>
				<c:set var="hidecolumnclass" value="hidecolumn"/>
				<c:forEach var="valuea" items="${values}">					
				<c:forEach var="valueb" items="${valuea.anagrafica4view[subtip.shortName]}">		
					<c:if test="${valueb.visibility==1}">
						<c:set var="hidecolumn" value=""/>
						<c:set var="hidecolumnclass" value=""/>
					</c:if>
				</c:forEach>
				</c:forEach>
			</c:if>
			<display-el:column style="${subLabelMinWidth}${hidecolumn}" title="${subtip.label}"  
					sortProperty="value.anagrafica4view['${subtip.shortName}'][0].object.sortValue" 
					sortable="false" headerClass="${hidecolumnclass}">
				<c:set var="nameriga" value="${typeDefinition.shortName}_RowNum" scope="request" />
				<c:set var="numtip"
					value="${count % fn:length(typeDefinition.real.mask)}" />
				<c:set var="numriga" 
					value="${(count - count % fn:length(typeDefinition.real.mask))/fn:length(typeDefinition.real.mask)}" />
				<c:set var="count" value="${count+1}" />																			
				<dyna:display hideLabel="true" tipologia="${subtip}" values="${values[numriga].anagrafica4view[subtip.shortName]}" editMode="${editmode}" subElement="true"/>
				
			</display-el:column>
						
	</c:forEach>
	<c:if test="${editmode}">
	
		<display:column>
			<c:choose>
				<c:when test="${values[numriga].preferred != null && values[numriga].preferred}">
					<img id="nested_${typeDefinition.real.id}_notpreferred_${values[numriga].id}" src="${root}/image/jdyna/preferred.png" class="nested_notpreferred_button nested_notpreferred_button_${typeDefinition.real.id} nested_notpreferred_button_${typeDefinition.shortName}"/>
				</c:when>
				<c:otherwise>
					<img id="nested_${typeDefinition.real.id}_preferred_${values[numriga].id}" src="${root}/image/jdyna/notpreferred.png" class="nested_preferred_button nested_preferred_button_${typeDefinition.real.id} nested_preferred_button_${typeDefinition.shortName}"/>
				</c:otherwise>
			</c:choose>	
		<c:if test="${admin || showeditbutton}">
			<img id="nested_${typeDefinition.real.id}_edit_${values[numriga].id}" src="${root}/image/jdyna/edit.gif" class="nested_edit_button nested_edit_button_${typeDefinition.real.id} nested_edit_button_${typeDefinition.shortName}"/>
		</c:if>	
		<c:if test="${admin || (typeDefinition.accessLevel==3)}">		
			<img id="nested_${typeDefinition.real.id}_delete_${values[numriga].id}" src="${root}/image/jdyna/delete_icon.gif" class="nested_delete_button nested_delete_button_${typeDefinition.real.id} nested_delete_button_${typeDefinition.shortName}"/>
		</c:if>					
		</display:column>
	
	</c:if>
	
	</display:table>	
</c:when>
<c:otherwise>								
	<fieldset>					
	<c:choose>		
	<c:when test="${fn:length(values) > 0}">	
	<c:forEach var="value" items="${values}" varStatus="valueStatus">
		<c:choose>
			<c:when test="${valueStatus.count == 1 && fn:length(values)==1}"><div class="dynaFieldComboValueFirstLast"></c:when>
			<c:when test="${valueStatus.count == 1}"><div class="dynaFieldComboValueFirst"></c:when>
			<c:when test="${valueStatus.count == fn:length(values)}"><div class="dynaFieldComboValueLast"></c:when>
			<c:otherwise><div class="dynaFieldComboValue"></c:otherwise>
		</c:choose>
	
		<c:forEach var="subtip" items="${subTypesSortered}">
			<%-- Dovrei richiamare dyna:display per ricorsione ma non funziona... --%>
			<c:if test="${subtip.accessLevel > 1}">
				<c:set var="showeditbutton" value="true" target="java.lang.Boolean"/>
			</c:if>	
			<c:set var="hidecolumn" value=""/>			
			<c:if test="${editmode && !admin && subtip.accessLevel == 1}">
				<c:set var="hidecolumn" value="display:none;"/>
			</c:if>
			<c:if test="${!editmode && !admin}"> 
				<c:set var="hidecolumn" value="display:none;"/>
				<c:forEach var="valuea" items="${value.anagrafica4view[subtip.shortName]}">		
					<c:if test="${valuea.visibility==1}">						
						<c:set var="hidecolumn" value=""/>
					</c:if>
				</c:forEach>
			</c:if>
			<c:set var="count" value="${count+1}" />
			<div style="${hidecolumn}">
				<dyna:display-combo-inline subValues="${value.anagrafica4view[subtip.shortName]}" subtip="${subtip}" subElement="false" editMode="${editmode}"/>
			</div>
			<%-- FINE DEL COPIA INCOLLA --%>
		</c:forEach>
		<c:if test="${editmode}">
		<div class="nested_actions">
			<c:choose>
				<c:when test="${value.preferred != null && value.preferred}">
					<img id="nested_${typeDefinition.real.id}_notpreferred_${value.id}" src="${root}/image/jdyna/preferred.png" class="nested_notpreferred_button nested_notpreferred_button_${typeDefinition.real.id} nested_notpreferred_button_${typeDefinition.shortName}"/>
				</c:when>
				<c:otherwise>
					<img id="nested_${typeDefinition.real.id}_preferred_${value.id}" src="${root}/image/jdyna/notpreferred.png" class="nested_preferred_button nested_preferred_button_${typeDefinition.real.id} nested_preferred_button_${typeDefinition.shortName}"/>
				</c:otherwise>
			</c:choose>
			<c:if test="${admin || showeditbutton}">
				<img id="nested_${typeDefinition.real.id}_edit_${value.id}" src="${root}/image/jdyna/edit.gif" class="nested_edit_button nested_edit_button_${typeDefinition.real.id} nested_edit_button_${typeDefinition.shortName}"/>
			</c:if>	
			<c:if test="${admin || (typeDefinition.accessLevel==3)}">				
				<img id="nested_${typeDefinition.real.id}_delete_${value.id}" src="${root}/image/jdyna/delete_icon.gif" class="nested_delete_button nested_delete_button_${typeDefinition.real.id} nested_delete_button_${typeDefinition.shortName}"/>
			</c:if>					
		</div>
		</c:if>
	</div>
	<div class="dynaClear">&nbsp;</div>
	</c:forEach>
	</c:when>
	<c:otherwise>
	<div class="dynaFieldComboValueFirstLast">
		<c:forEach var="subtip" items="${subTypesSortered}">
			<%-- Dovrei richiamare dyna:display per ricorsione ma non funziona... --%>
			<dyna:display-combo-inline subtip="${subtip}" />
			<%-- FINE DEL COPIA INCOLLA --%>			
		</c:forEach>		
	</div>
	</c:otherwise>
	</c:choose>
	</fieldset>
</c:otherwise>
</c:choose>						
