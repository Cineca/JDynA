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