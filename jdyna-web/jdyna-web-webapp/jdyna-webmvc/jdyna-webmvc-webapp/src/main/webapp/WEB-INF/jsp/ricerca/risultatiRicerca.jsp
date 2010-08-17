<%@ include file="/common/taglibs.jsp"%>

<h2>Risultati della ricerca:</h2>
<br/>
<h3><span class="query"><i>${query} &nbsp su &nbsp</i></span><span class="scope"><i><fmt:message key="search.${clazz}" /></i></span></h3>
<br/>
<br/>
<c:forEach var="entry" items="${results}" >
	<c:set var="baseObjectURL" value="/${entry.key}" />
	<c:set var="tableId" value="${entry.key}List" />
	<c:set var="objectList" value="${entry.value}" />
	<c:if test="${not empty entry.value}">
	<div class="resultSet">
	<h3><fmt:message key="search.${entry.key}" /></h3>
	<c:set var="entrykey" value="${entry.key}" />
	<%
		String entrykey = (String) pageContext.getAttribute("entrykey");		
		pageContext.setAttribute("showInColumnList",request.getAttribute(entrykey+"showInColumnList"));
	%>

	
	<c:set var="sortPrefix" value="${entry.key}_"></c:set>
	<%@ include file="/WEB-INF/jsp/common/anagraficaList.jsp" %>
	</div>
	</c:if>
</c:forEach>
<form action="flusso.flow" method="post">
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">	
</form>