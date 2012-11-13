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

<!-- BUTTONS -->
<c:set var="href">
	<c:url value="/${baseObjectURL}/aree/type/list.htm"/>	
</c:set>
<!--  
<input type="button" class="buttonBack" value=" " onclick="location.href='${href}'" />
-->
<content  tag="breadcrumbs">
<ul class='breadcrumbs'>
<li>
<a href='${href}' > lista aree ${baseObjectURL}</a>
</li>
<li>
 ${area.title}
 </li>
 </ul>
</content>
<%-- pulsante per modificare la tipologia progetto --%>
<c:set var="href">
	<c:url value="/${baseObjectURL}/aree/type/form.htm">
		<c:param name="areaId">${area.id}</c:param>
	</c:url>
</c:set>
<input type="button" class="buttonEdit" value=" " onclick="location.href='${href}'" />
	
<%-- pulsante per eliminare area --%>
<c:set var="href">
	<c:url value="/${baseObjectURL}/aree/type/delete.htm">
		<c:param name="areaId">${area.id}</c:param>
	</c:url>
</c:set>
<input type="button" class="buttonDelete" value=" " onclick="location.href='${href}'" /><br/><br/>
	
<br />	
<span style="font-weight: bold"><fmt:message key="label.area.title" /></span>: ${area.title}<br/>
<span style="font-weight: bold"><fmt:message key="label.area.priorita" /></span>: ${area.priorita}<br/>

<br />
<br />

<c:set var="tipologiaProprietaList" value="${listaTipologieArea}" />
<fieldset class="lista"><legend><fmt:message key="title.area.fieldset.dettaglio" /></legend>	
 <%@ include file="/WEB-INF/jsp/common/listaTipologieProprieta.jsp" %>
</fieldset>
