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
											
<c:choose>								  						
	<c:when test="${!typeDefinition.real.inline}">
	<c:set var="count" value="0" />													
												
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
			
				<display-el:column style="${subLabelMinWidth}" title="${subtip.label}"  
					sortProperty="value.anagrafica4view['${subtip.shortName}'][0].object.jQueryortValue" 
					sortable="false">
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
		<img id="nested_${typeDefinition.real.id}_edit_${values[numriga].id}" src="${root}/image/jdyna/edit.gif" class="nested_edit_button nested_edit_button_${typeDefinition.real.id} nested_edit_button_${typeDefinition.shortName}"/>
		<img id="nested_${typeDefinition.real.id}_delete_${values[numriga].id}" src="${root}/image/jdyna/delete_icon.gif" class="nested_delete_button nested_delete_button_${typeDefinition.real.id} nested_delete_button_${typeDefinition.shortName}"/>					
	</display:column>
	</c:if>
	</display:table>	
</c:when>
<c:otherwise>													
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
			
			<dyna:display-combo-inline subValues="${value.anagrafica4view[subtip.shortName]}" subtip="${subtip}" subElement="false" editMode="${editmode}"/>
			
			<%-- FINE DEL COPIA INCOLLA --%>
		</c:forEach>
		<c:if test="${editmode}">
		<div class="nested_actions">		
		<img id="nested_${typeDefinition.real.id}_edit_${value.id}" src="${root}/image/jdyna/edit.gif" class="nested_edit_button nested_edit_button_${typeDefinition.real.id} nested_edit_button_${typeDefinition.shortName}"/>
		<img id="nested_${typeDefinition.real.id}_delete_${value.id}" src="${root}/image/jdyna/delete_icon.gif" class="nested_delete_button nested_delete_button_${typeDefinition.real.id} nested_delete_button_${typeDefinition.shortName}"/>					
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

</c:otherwise>
</c:choose>						
