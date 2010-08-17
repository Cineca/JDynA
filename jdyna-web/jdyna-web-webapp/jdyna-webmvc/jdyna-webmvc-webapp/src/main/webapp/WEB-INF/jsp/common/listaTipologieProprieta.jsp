<%--VALIDATA XHTML 1.1 
<c:if test="${empty dettaglioTipologia}">
	<%@ include file="/WEB-INF/jsp/common/exportPage.jsp" %>
</c:if>
--%>
<display:table name="${tipologiaProprietaList}" cellspacing="0" cellpadding="0"
	requestURI="" id="tipologiaProprietaList" class="table" export="false" pagesize="20">
	<display:column property="id" escapeXml="true" sortable="true"
		titleKey="column.id" url="/admin/${baseObjectURL}/tipologiaProprieta/details.htm" paramId="id"
		paramProperty="id" />		
	<display:column property="label" escapeXml="false" sortable="true"
		titleKey="column.label"/>
	<display:column property="shortName" escapeXml="false" sortable="true"
		titleKey="column.shortName"/>
	<display:column property="obbligatorieta" escapeXml="false" sortable="true"
		titleKey="column.obbligatorieta"/>
	<display:column property="ripetibile" escapeXml="false" sortable="true"
		titleKey="column.ripetibile"/>
	<display:column property="showInList" escapeXml="false" sortable="true"
		titleKey="column.showInList"/>
	<display:column property="priorita" escapeXml="false" sortable="true"
		titleKey="column.priorita"/>
	<display:column property="rendering.class.simpleName" escapeXml="true" sortable="true"
		titleKey="column.rendering"/>
</display:table>
