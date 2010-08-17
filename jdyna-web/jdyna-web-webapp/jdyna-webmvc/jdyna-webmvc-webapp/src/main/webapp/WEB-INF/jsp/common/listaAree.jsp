<display:table name="listaAree" cellspacing="0" cellpadding="0"
	requestURI="" id="listaAree" class="table" export="false" pagesize="10">
	
	<display:column property="id" escapeXml="true" sortable="true"
		titleKey="column.id" url="/${baseObjectURL}/aree/type/details.htm" paramId="areaId"
		paramProperty="id" />
	
	<display:column property="title" escapeXml="true" sortable="true"
		titleKey="column.tipologia.title"/>
	<display:column property="priorita" escapeXml="true" sortable="true"
		titleKey="column.tipologia.priorita"/>
</display:table>

