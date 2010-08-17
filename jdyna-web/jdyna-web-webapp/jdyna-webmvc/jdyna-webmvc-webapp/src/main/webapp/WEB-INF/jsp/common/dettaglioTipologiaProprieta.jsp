<%@ include file="/common/taglibs.jsp"%>
<%--VALIDATA XHTML 1.1 --%>

<c:set var="commandObject" value="${tipologiaProprieta}" scope="request" />
<fieldset><legend>Metaproprietà di presentazione e indicizzazione</legend>
	<div class="dynaField">
		<span class="dynaLabel">
			Shortname:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.shortName}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Label:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.label}
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Help:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.help}
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Obbligatorietà:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.obbligatorieta?'Si':'No'}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Ripetibilità:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.ripetibile?'Si':'No'}
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Priorità:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.priorita}
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>
	<div class="dynaField">
		<span class="dynaLabel">
			Mostrato negli elenchi:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.showInList?'Si':'No'}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			In creazione:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.onCreation?'Si':'No'}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Ricerca semplice:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.simpleSearch?'Si':'No'}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			Ricerca avanzata:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.advancedSearch?'Si':'No'}
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			<fmt:message key='column.labelMinSize'/>:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.labelMinSize}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			<fmt:message key='column.fieldMinSize.col'/>:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.fieldMinSize.col}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			<fmt:message key='column.fieldMinSize.row'/>:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.fieldMinSize.row}
		</div>
	</div>

	<div class="dynaField">
		<span class="dynaLabel">
			<fmt:message key='label.tipologiaProprieta.newline'/>:
		</span>
		<div class="dynaFieldValue">
			${tipologiaProprieta.newline?'Si':'No'}
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>

</fieldset>
	
	
<fieldset><legend>Metaproprietà di rendering e validazione</legend>

<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetTesto'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
		<span class="dynaLabel">Collisioni:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.collisioni?'Si':'No'}
			</div>
		
		<span class="dynaLabel">Regex:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.regex}
			</div>
	<span class="dynaLabel">Multilinea:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.multilinea?'Si':'No'}
			</div>
		
		<div class="dynaClear" />
				
	<span class="dynaLabel">Html toolbar:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.htmlToolbar}
			</div>
	<span class="dynaLabel">Dimensione riga:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.dimensione.row}
			</div>
	<span class="dynaLabel">Dimensione colonna:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.dimensione.col}
			</div>			
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetCheckRadio'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
		<span class="dynaLabel">Albero classificatorio:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.alberoClassificatorio.nome}
			</div>
		<span class="dynaLabel">Soggettario:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.soggettario.nome}
			</div>
		<span class="dynaLabel">Opzioni per riga:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.option4row}
			</div>
	
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetClassificazione'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
		<span class="dynaLabel">Albero classificatorio:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.alberoClassificatorio.nome}
			</div>
			
			<c:if test="${empty tipologiaProprieta.rendering.alberoClassificatorio.topClassificazioni}">
			
 	 		<c:set var="href">
				<c:url value="/flusso.flow?_flowId=gestioneAlbero-flow">
					<c:param name="id">${tipologiaProprieta.rendering.alberoClassificatorio.id}</c:param>				
				</c:url>
			</c:set>

			<input type="image" src="${root}/images/icons/warning-giallo-30.png" title="L'albero non contiene nessuna classificazione clicca qui per crearne" 
 	 			alt="Creare classificazioni per l'albero" onhelp="Clicca sull'immagine per modificare l'area corrente" onclick="location.href='${href}'" />
			</c:if>

</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetData'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
		<span class="dynaLabel">Min year:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.minYear}
			</div>
	<span class="dynaLabel">Max year:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.maxYear}
			</div>
	<span class="dynaLabel">Time:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.time?'Si':'No'}
			</div>
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetEmail'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}

	Nessuna configurazione aggiuntiva richiesta.
	<%-- 	<span class="dynaLabel">Regex:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.regex}
			</div>
	--%>
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetFormulaTesto'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
	
		<span class="dynaLabel">Expression:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.expression}
			</div>
		<span class="dynaLabel">Regola di ricalcolo:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.regolaDiRicalcolo?'Si':'No'}
			</div>
		<span class="dynaLabel">Result number:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.resultNumber}
			</div>			
		<div class="dynaClear" />
		<span class="dynaLabel">Variabili:</span>
		
			<div class="dynaFieldValue">
			<c:forEach items="${tipologiaProprieta.rendering.variabili}" var="variabile">
			${variabile}<br/>
			</c:forEach>
				
			</div>
			<span class="dynaLabel">Configurazione:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.configuration}
			</div>	
	    	<span class="dynaLabel">Dimensione riga:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.dimensione.row}
			</div>
			<div class="dynaClear" />
		    <span class="dynaLabel">Dimensione colonna:</span>
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.dimensione.col}
			</div>			
</div>
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetFormulaClassificazione'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
	
		<span class="dynaLabel">Expression:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.expression}
			</div>
		<span class="dynaLabel">Regola di ricalcolo:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.regolaDiRicalcolo?'Si':'No'}
			</div>
		<span class="dynaLabel">Result number:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.resultNumber}
			</div>			
		<div class="dynaClear" />
		<span class="dynaLabel">Variabili:</span>
		
			<div class="dynaFieldValue">
			<c:forEach items="${tipologiaProprieta.rendering.variabili}" var="variabile">
			${variabile}<br/>
			</c:forEach>
				
			</div>
			<span class="dynaLabel">Configurazione:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.configuration}
			</div>	
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetFormulaNumero'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
	
		<span class="dynaLabel">Expression:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.expression}
			</div>
		<span class="dynaLabel">Regola di ricalcolo:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.regolaDiRicalcolo?'Si':'No'}
			</div>
		<span class="dynaLabel">Result number:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.resultNumber}
			</div>			
		<div class="dynaClear" />
		<span class="dynaLabel">Variabili:</span>
		
			<div class="dynaFieldValue">
			<c:forEach items="${tipologiaProprieta.rendering.variabili}" var="variabile">
			${variabile}<br/>
			</c:forEach>
				
			</div>
			<span class="dynaLabel">Configurazione:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.configuration}
			</div>	
				<span class="dynaLabel">Cifre decimali:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.cifreDecimali}
			</div>	
					<div class="dynaClear" />
			
				<span class="dynaLabel">Dimensione:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.size}
			</div>		
			
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetNumero'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
	
		<span class="dynaLabel">Minimo:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.min}
			</div>
		<span class="dynaLabel">Massimo:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.max}
			</div>
		<span class="dynaLabel">Cifre Decimali:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.cifreDecimali}
			</div>			
		<div class="dynaClear" />
			<span class="dynaLabel">Dimensione box:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.size}
			</div>					
		
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetPointer'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
	
		<span class="dynaLabel">Target:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.target}
			</div>
		<span class="dynaLabel">Display:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.display}
			</div>
		<span class="dynaLabel">Filtro:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.filtro}
			</div>			
		<div class="dynaClear" />
			<span class="dynaLabel">Dimensione box:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.size}
			</div>					
		
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetSoggettario'}">
<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
	
		<span class="dynaLabel">Configurazione:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.configuration}
			</div>

			<span class="dynaLabel">Dimensione box:</span>
		
			<div class="dynaFieldValue">
				${tipologiaProprieta.rendering.size}
			</div>					
		
</div>
</c:if>
<c:if test="${tipologiaProprieta.rendering.class.simpleName == 'WidgetBoolean'}">
	<div class="dynaField" >
	<span style="font-weight: bold">Widget:</span>
	${tipologiaProprieta.rendering.class.simpleName}
		</div>
</c:if>
	
		


    <c:if test="${commandObject.class == 'class it.cilea.epi.intranet.amministrazione.TipologiaProprietaSchedaOrdineStampa'}">
	<span style="font-weight: bold">Regola precompilazione Pubblicazione</span> : ${tipologiaProprieta.regolaPubblicazione}<br/>
	
	</c:if>

<c:if test="${tipologiaProprieta.rendering.triview == 'combo'}">
<div class="dynaField"> 
<span style="font-weight: bold">Lista sotto tipologie per la combo:</span><br/>
<c:set var="objectList" value="${tipologiaProprieta.rendering.sottoTipologie}"></c:set>
	<display:table name="${objectList}" cellspacing="0" cellpadding="0"
	 requestURI="" id="objectList" class="table" export="false" pagesize="50">

	<display:column property="id" escapeXml="true" sortable="true"
		titleKey="column.id" url="/admin/${path}/tipologiaProprieta/details.htm" paramId="id"
		paramProperty="id" />

	<display:column property="shortName" escapeXml="true" sortable="true"
		titleKey="column.shortName"/>
	<display:column property="label" escapeXml="true" sortable="true"
		titleKey="column.label"/>
	<display:column property="obbligatorieta" escapeXml="true" sortable="true"
		titleKey="column.obbligatorieta"/>
	<display:column property="ripetibile" escapeXml="true" sortable="true"
		titleKey="column.ripetibile"/>
	<display:column property="showInList" escapeXml="true" sortable="true"
		titleKey="column.showInList"/>
	<display:column property="priorita" escapeXml="true" sortable="true"
		titleKey="column.priorita"/>
	<display:column property="labelMinSize" escapeXml="true" sortable="true"
		titleKey="column.labelMinSize"/>
	<display:column property="fieldMinSize.col" escapeXml="true" sortable="true"
		titleKey="column.fieldMinSize.col"/>
	<display:column property="fieldMinSize.row" escapeXml="true" sortable="true"
		titleKey="column.fieldMinSize.row"/>
	<display:column property="rendering.class.simpleName" escapeXml="true" sortable="true"
		titleKey="column.rendering"/>							  
	</display:table>
</div>
</c:if>
</fieldset>