<%@ include file="/common/taglibs.jsp"%>


<head>
	<title>Lista degli Alberi Classificatori </title>
</head>


<c:if test="${!empty listaOggetti}">
	<form:form action="export.htm" method="post" id="export">
		<p>		
			<img onclick="document.getElementById('export').submit()" src="${root}/images/ico_exp_xml.gif" title="<fmt:message key="esporta.configurazione.descrizione" />" alt="<fmt:message key="esporta.configurazione.descrizione" />"/>
		</p>		
	</form:form>
</c:if>
<display:table name="listaOggetti" cellspacing="0" cellpadding="0"
	requestURI="" id="listaOggetti" class="table" export="false" pagesize="20">
	<display:column property="id" escapeXml="true" sortable="true"
		titleKey="column.id" url="/flusso.flow?_flowId=gestioneAlbero-flow" paramId="id"
		paramProperty="id" />		
	<display:column property="nome" escapeXml="true" sortable="true"
		titleKey="column.nome"/>
	<display:column property="descrizione" escapeXml="true" sortable="true"
		titleKey="column.descrizione"/>

</display:table>

<script type="text/javascript">
    highlightTableRows("listaOggetti");
</script>


<c:set var="href">
	<c:url value="/flusso.flow?_flowId=nuovoAlberoClassificatorio-flow"/>
</c:set>
	<p>
<input type="button" value="<fmt:message key='button.create'/>"
	onclick="location.href='${href}'" />
	<br/>
	</p>
	
<fieldset>
	<legend><fmt:message key="title.carica.configurazione" /></legend>
 		<p><fmt:message key="title.carica.configurazione.da.filesystem" /></p>
<form:form action="import.htm" method="post" enctype="multipart/form-data" id="import">
<table>
		<tr>
			<td>
				File:
			</td>
			<td>
				<input type="file" name="file" size="80" />
			</td>
		</tr>
</table>	
		<p>	
		<input type="submit" name="upload"
		value="<fmt:message key='button.upload'/>"/>
		</p>
	 		 		
</form:form> 					
</fieldset>

	