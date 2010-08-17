<%@ include file="/common/taglibs.jsp"%>
<%--VALIDATA XHTML 1.1 --%>
<c:if test="${tableId == null}"><c:set var="tableId" value="objectList" /></c:if>
<c:if test="${tableClass == null}"><c:set var="tableClass" value="table" /></c:if>
<div class="subcontenuto">
<display:table sort="external" name="${objectList}" cellspacing="0" cellpadding="0"
	requestURI="" id="objectList" htmlId="${tableId}"  class="${tableClass}" export="true" pagesize="20">
	
	<display:column  property="id" escapeXml="true" sortable="true" sortProperty="${sortPrefix}id"
	titleKey="column.id" url="${baseObjectURL}/details.htm" paramId="id"
	paramProperty="id" />
	
	<c:if test="${showTipologia}">
	<display:column property="tipologia.nome" escapeXml="false" sortable="true" 
		titleKey="column.tipologia.nome" sortProperty="${sortPrefix}tipologia"/>
	</c:if>			
	<c:forEach items="${showInColumnList}" var="tipologia" varStatus="i">
		<display:column sortable="true" title="${tipologia.label}" sortProperty="${sortPrefix}${tipologia.shortName}">		
			<c:forEach items="${objectList.anagrafica4view[tipologia.shortName]}" var="elemento" varStatus="iter">	  		
				<c:if test="${iter.count>1}"><br /></c:if>${elemento.html}
			</c:forEach>			
		</display:column>		
	</c:forEach>
	
</display:table>
</div>

