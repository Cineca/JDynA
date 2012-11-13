<%--
JDynA, Dynamic Metadata Management for Java Domain Object

 Copyright (c) 2008, CILEA and third-party contributors as
 indicated by the @author tags or express copyright attribution
 statements applied by the authors.  All third-party contributions are
 distributed under license by CILEA.

 This copyrighted material is made available to anyone wishing to use, modify,
 copy, or redistribute it subject to the terms and conditions of the GNU
 Lesser General Public License v3 or any later version, as published 
 by the Free Software Foundation, Inc. <http://fsf.org/>.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this distribution; if not, write to:
  Free Software Foundation, Inc.
  51 Franklin Street, Fifth Floor
  Boston, MA  02110-1301  USA
--%>

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
