<%@ include file="/common/taglibs.jsp"%>
<%--VALIDATA XHTML 1.1 --%>

<script type="text/javascript">
function confermaEliminazione(id){
  	chiediConferma = confirm('Sei sicuro di voler eliminare il soggettario? l\'operazione non potra\' essere annullata');
 
  if (chiediConferma == true){
  	location.href = '${root}/soggettario/delete.htm?id='+id;    
  }
  //altrimenti non fare nulla
}
</script>
<!-- c:set var="addModeType" value="display" scope="request"/-->
<content  tag="breadcrumbs">
<ul class='breadcrumbs'>
<li>
<a href='${root}/soggettario/list.htm' > lista soggettari</a>
</li>
<li>
 ${epiobject.displayValue}
 </li>
 </ul>
</content>
	
<c:set var="commandObject" value="${epiobject}" scope="request" />

<div id="mainButtons">
<c:set var="href">
	<c:url value="/flusso.flow?_flowId=nuovoSoggettario-flow">
		<c:param name="id">${epiobject.id}</c:param>
	</c:url>
</c:set>
<div class="dynaField">
<img src="${root}/images/edit.gif" title="Modifica i metadati della collana" alt="Clicca sull'immagine per modificare l'area corrente" onclick="location.href='<c:out value='${href}' escapeXml='true' />'" />
</div>
<authz:aclTagDeleteOggetti domainObject="${epiobject}"> 	
<div class="dynaField">
	<img type="image" src="${root}/images/delete.jpg"  alt="Cancella il soggettario"  value=" " title="Cancella il soggettario" onclick="confermaEliminazione('${epiobject.id}');"/>
</div>
</authz:aclTagDeleteOggetti>
<div class="dynaField">
<form:form action="export.htm" method="post" id="export">
<p>
		<input type="hidden" name="soggettario_id" value="${epiobject.id}" />
		<img onclick="document.getElementById('export').submit()" src="${root}/images/ico_exp_xml.gif" title="<fmt:message key="esporta.configurazione.descrizione" />" alt="<fmt:message key="esporta.configurazione.descrizione" />"/>
		
</p>
</form:form>
</div>
<div class="dynaClear">
		&nbsp;
</div>
</div>
<div id="fieldDisplayTag">
<fieldset>
	<legend><fmt:message key="title.soggettario.fieldset.dettaglio" /></legend>	
<display:table name="${epiobject}" cellspacing="0" cellpadding="0"
	requestURI="" id="${epiobject.nome}:${epiobject.id}" class="table" export="false" pagesize="10">	
	<display:column property="nome" escapeXml="true" sortable="true"
		titleKey="column.nome"/>
	<display:column property="descrizione" escapeXml="true" sortable="true"
		titleKey="column.descrizione"/>
	<display:column property="soggetti" escapeXml="true" sortable="true"
		titleKey="column.soggetti"/>
</display:table>
</fieldset>
</div>	



