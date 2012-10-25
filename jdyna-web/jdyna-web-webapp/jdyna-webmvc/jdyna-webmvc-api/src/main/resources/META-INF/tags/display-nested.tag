<%@ attribute name="values" required="true" type="java.util.Collection" %>
<%@ attribute name="typeDefinition" required="true" type="it.cilea.osd.jdyna.model.ADecoratorTypeDefinition"%>
<%@ attribute name="editmode" required="false" type="java.lang.Boolean"%>
<%@ attribute name="parentID" required="true"%>
<%@ attribute name="specificPartPath" required="true"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:set var="subTypesSortered" value="${dyna:sortList(typeDefinition.real.mask)}" />																			
<script type="text/javascript">									

		jQuery(".editNestedButton${typeDefinition.shortName}")
				.click(
						function() {						
							jQuery("#log3").dialog("open");									
							Loader.write("Loading form...");
							jQuery('#nestedfragmentcontent_${typeDefinition.shortName}').html("");
							jQuery(".ui-dialog-titlebar").html("");
							var parameterId = this.id;																	
							var ajaxurlrelations = "<%= request.getContextPath() %>${specificPartPath}/editNested.htm";
							jQuery.ajax( {
								url : ajaxurlrelations,
								data : {																			
									"elementID" : parameterId,
									"parentID" : ${parentID},
									"typeNestedID" : ${typeDefinition.real.id},
									"editmode": ${editmode}
								},
								success : function(data) {																
									jQuery('#nestedfragment_${typeDefinition.shortName}').dialog("open");		
									jQuery(".ui-dialog-titlebar").html("${typeDefinition.label} &nbsp; <a class='ui-dialog-titlebar-close ui-corner-all' href='#' role='button'><span class='ui-icon ui-icon-closethick'>close</span></a>");jQuery(".ui-dialog-titlebar").show();		
									jQuery('#nestedfragmentcontent_${typeDefinition.shortName}').html(data);
									jQuery('#nestedfragment_${typeDefinition.shortName}').dialog('option', 'position', 'center');
									jQuery("#log3").dialog("close");
								},
								error : function(data) {
																												
									Log.write(data.statusText);
									
								}
							});
	
						});
		
		jQuery(".deleteNestedButton${typeDefinition.shortName}")
		.click(
				function() {						
					jQuery("#log3").dialog("open");									
					Loader.write("Delete object...");															
					
					var parameterId = this.id;
					var ajaxurlrelations = "<%= request.getContextPath() %>${specificPartPath}/deleteNested.htm";
					jQuery.ajax( {
						url : ajaxurlrelations,
						data : {																			
							"elementID" : parameterId,
							"parentID" : ${parentID},
							"typeNestedID" : ${typeDefinition.real.id},
							"editmode": ${editmode}							
						},
						success : function(data) {															
							j('#viewnested_${typeDefinition.shortName}').html(data);								
							j("#log3").dialog("close");
						},
						error : function(data) {
																										
							Log.write(data.statusText);
							
						}
					});

				});
	</script>
												
<c:choose>								  						
	<c:when test="${!typeDefinition.real.inline}">
	<c:set var="count" value="0" />													
												
	<display:table name="${values}" cellspacing="0" cellpadding="0" uid="${typeDefinition.shortName}"
		class="dynaFieldComboValue" requestURI="" sort="list" export="false" pagesize="100">
	<display:setProperty name="paging.banner.no_items_found" value="" />
	<display:setProperty name="paging.banner.one_item_found" value="" />
	<display:setProperty name="paging.banner.all_items_found" value="" />
	<display:setProperty name="paging.banner.page.selected" value="" />
	<display:setProperty name="paging.banner.onepage" value="" />
	
	<c:forEach var="subtip" items="${subTypesSortered}" varStatus="valueStatus">
			
								
			<c:set var="subLabelMinWidth" value="" />
			<c:if test="${subtip.labelMinSize > 1}">
				<c:set var="subLabelMinWidth" value="width:${subtip.labelMinSize}em;" />
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
		<img id="edit${typeDefinition.shortName}_${values[numriga].id}" src="${root}/image/jdyna/edit.gif" class="editNestedButton editNestedButton${typeDefinition.shortName}"/>
		<img id="delete${typeDefinition.shortName}_${values[numriga].id}" src="${root}/image/jdyna/delete_icon.gif" class="deleteNestedButton deleteNestedButton${typeDefinition.shortName}"/>					
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
			<img id="edit${typeDefinition.shortName}_${value.id}" src="${root}/image/jdyna/edit.gif" class="editNestedButton editNestedButton${typeDefinition.shortName}"/>
			<img id="delete${typeDefinition.shortName}_${value.id}" src="${root}/image/jdyna/delete_icon.gif" class="deleteNestedButton deleteNestedButton${typeDefinition.shortName}"/>
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