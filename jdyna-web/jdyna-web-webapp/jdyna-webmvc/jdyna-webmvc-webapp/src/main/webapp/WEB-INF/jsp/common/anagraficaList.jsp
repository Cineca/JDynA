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

