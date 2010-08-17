<%--
   attribute:
       - anagraficaObject:     l'oggetto di cui si vuole visualizzare l'anagrafica, deve implementare AnangraficaSupport
       - aree:                 l'elenco delle aree disponibili per l'oggetto
       - tipologieInArea:      l'elenco delle tipologie da mostrare nell'area selezionata
   output:
       - visualizzazione dell'anagrafica con tab per passare da un'area all'altra
   require:
       - tutti i controller di visualizzazione devono accettare due parametri
               - id, l'ID dell'oggetto da visualizzare
               - areaId, l'ID dell'area da visualizzare       --%>
<div id='tab'<c:if test="${cssDetail != null}"> class="${cssDetail}"</c:if>>
<ul class="tablist">
	<c:forEach items="${aree}" var="area">
		<li <c:if test="${area.id == areaId}">
			class="selected"
			</c:if>><a href='?id=${anagraficaObject.id}&amp;areaId=${area.id}'>${area.title}</a></li>
	</c:forEach>
</ul>
</div>

<div id='content' <c:if test="${cssDetail != null}"> class="${cssDetail}"</c:if>>
<c:forEach items="${tipologieInArea}"
	var="tipologiaDaVisualizzare" varStatus="status">
		<dyna:display tipologia="${tipologiaDaVisualizzare}" 
			values="${anagraficaObject.anagrafica4view[tipologiaDaVisualizzare.shortName]}" />
</c:forEach>
<div class="dynaClear">&nbsp;</div>
</div>