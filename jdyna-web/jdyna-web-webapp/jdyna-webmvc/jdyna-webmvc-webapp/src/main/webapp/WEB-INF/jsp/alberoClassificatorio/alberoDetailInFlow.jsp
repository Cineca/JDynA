<%@ include file="/common/taglibs.jsp"%>

<head>
<script type="text/javascript">
function openAll(){
	var formClassificazione = document.getElementById('formclassificazione');	
	formClassificazione.style.display = 'block';
	mostraForm();
}

function popolaForm(codice,nome){
	
	var formClassificazione = document.getElementById('formclassificazione');	
	formClassificazione.style.display = 'block';
	var message = document.getElementById('alberoClassificazioneEditFormHelp');
	message.style.display = 'none';
	var elementDettaglio3 = document.getElementById('editID');
	var elementDettaglio4 = document.getElementById('editSelezionabile');

	var elementDettaglio5 = document.getElementById('editAttivo');
	
	var elementDettaglio6 = document.getElementById('editCodice');
	var elementDettaglio7 = document.getElementById('editNome');
	
	AjaxService.getClassificazioneByCodice(codice, nome, function(options) {	
		
	elementDettaglio3.value = options.id;	
		
	elementDettaglio4.value = options.selezionabile;
	document.getElementById('checkeditSelezionabile').value = options.selezionabile;
	if(options.selezionabile==true) {
		document.getElementById('checkeditSelezionabile').checked="checked";		
	}		
	else {
		document.getElementById('checkeditSelezionabile').checked = "";
	}
	
	elementDettaglio5.value = options.attivo;
	document.getElementById('checkeditAttivo').value = options.attivo;
	if(options.attivo==true) {
		document.getElementById('checkeditAttivo').checked="checked";
		
	}
	else {
		document.getElementById('checkeditAttivo').checked = "";
	}
		
	elementDettaglio6.value = options.codice;		
	elementDettaglio7.value = options.displayValue;
	
	});
	
	
}


function mostraForm() {
	var messageEditClassificazioni = document.getElementById('alberoClassificazioneEditFormHelp');
	messageEditClassificazioni.style.display = 'none';
	var formSubClassificazioni = document.getElementById('nuovaSottoClassificazione');
	formSubClassificazioni.style.display = 'block';
	var pulsanteMeno = document.getElementById('alberoRemoveSubClassificazione');
	pulsanteMeno.style.display = 'block';	
	var pulsantePiu = document.getElementById('alberoAddSubClassificazione');
	pulsantePiu.style.display = 'none';	
	document.getElementById('createSubClassificazione').value = true;
}

function nascondiForm() {
	
	var formSubClassificazioni = document.getElementById('nuovaSottoClassificazione');
	formSubClassificazioni.style.display = 'none';	
	var pulsantePiu = document.getElementById('alberoAddSubClassificazione');
	pulsantePiu.style.display = 'block';	
	var pulsanteMeno = document.getElementById('alberoRemoveSubClassificazione');
	pulsanteMeno.style.display = 'none';
	document.getElementById('createSubClassificazione').value = false;	
}
function confermaEliminazione(id){
  	chiediConferma = confirm('Sei sicuro di voler eliminare l\'albero? l\'operazione non potra\' essere annullata');
 
  if (chiediConferma == true){
  	location.href = '${root}/albero/delete.htm?id='+id;
    //location.href = '${root}/flusso.flow?_flowExecutionKey=${flowExecutionKey}&_eventId=cancellaAlbero';
  }
  //altrimenti non fare nulla
}

function confermaEliminazioneClassificazione(id,codice){
  	chiediConferma = confirm('Sei sicuro di voler eliminare la classificazione con codice '+ codice +' ? l\'operazione non potra\' essere annullata');
 
  if (chiediConferma == true){
  	//location.href = '${root}/classificazione/delete.htm?id='+id;
    location.href = '${root}/flusso.flow?_flowExecutionKey=${flowExecutionKey}&_eventId=eliminaClassificazione&editID='+id;
  }
  //altrimenti non fare nulla
}

</script>
<c:if test="${empty daVettore}"> 
<content  tag="breadcrumbs">
<ul class='breadcrumbs'>
<li>
<a href='${root}/albero/list.htm' >lista alberi classificatori</a>  
</li>
<li>
${gestioneAlbero.nome}
</li> 
</ul>
</content>
</c:if>
<c:if test="${!empty daVettore}"> 
<content  tag="breadcrumbs">
<ul class='breadcrumbs'>
<li>
<a href='${root}/amministrazione/vettore/list.htm' >lista vettori</a>  
</li>
<li>
${gestioneAlbero.nome}
</li> 
</ul>
</content>
</c:if>
</head>
<body>
<c:set var="commandObject" value="${gestioneAlbero}" scope="request" />
<c:set var="pulsanteDiUscita" value="${pulsanteDiUscita}" scope="request" />
<c:set var="noExportingAlbero" value="${noExportingAlbero}" scope="request" />
	
<form:form commandName="gestioneAlbero" action="flusso.flow" method="post" id="sa">

	 <spring:hasBindErrors name="gestioneAlbero">
         <div class="error">
         Non sarà possibile salvare i dati contenuti nel form fino a quando non saranno risolti i seguenti problemi (cliccare sul messaggio per raggiungere rapidamente il campo in errore):
         <ul>
            <c:forEach var="errMsgObj" items="${errors.fieldErrors}">
               <li>
                  ${errMsgObj.defaultMessage}: <a href="#errorMsg${errMsgObj.field}">
                  <spring:message arguments="${errMsgObj.arguments}" code="${errMsgObj.code}"/></a>
               </li>
            </c:forEach>
         </ul>
         </div>
      </spring:hasBindErrors>

<div id="mainButtons">
<div class="dynaField">
<a href="${root}/flusso.flow?_flowExecutionKey=${flowExecutionKey}&_eventId=modifica"><input type="image" src="${root}/images/edit.gif" title="Modifica i metadati dell'opera" onhelp="Clicca sull'immagine per modificare l'anagrafica dell'albero"/></a>
</div>
<%-- <input type="image" src="${root}/images/delete.jpg"  alt="Cancella l'albero"  onclick="confermaEliminazione('${gestioneAlbero.id}');"/> --%>

<authz:aclTagDeleteOggetti domainObject="${gestioneAlbero}"> 	
<div class="dynaField">
	<img type="image" src="${root}/images/delete.jpg"  alt="Cancella l'albero"  value=" " title="Cancella l'albero" onclick="confermaEliminazione('${gestioneAlbero.id}');"/>
</div>
</authz:aclTagDeleteOggetti>

<c:if test="${empty noExportingAlbero}">
<div class="dynaField">	
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	<input type="submit" name="_eventId_export" alt="Export" value=" " class="buttonExport"/>		
</div>
</c:if>
</div>

<fieldset id="alberodetail">	
	<legend><fmt:message key="label.albero.descrizione" /></legend>	
	<div class="dynaField">
		<span class="dynaLabel" id="alberonomelabel">
			<fmt:message key="label.albero.nome"/>
		</span>
		<div class="dynaFieldValue">
			  ${gestioneAlbero.nome}
		</div>
	</div>
	<div class="dynaClear">
		&nbsp;
	</div>
	  
	<div class="dynaField">
		<span class="dynaLabel" id="alberodescrizionelabel">
			<fmt:message key="label.albero.descrizione"/>
		</span>
		<div class="dynaFieldValue">
			${gestioneAlbero.descrizione}		
		</div>
	</div>

	<div class="dynaClear">
		&nbsp;
	</div>		
		<div class="dynaField">
		<span class="dynaLabel" id="codicesignificativolabel">
			<fmt:message key="label.albero.isCodiceSignificativo"/>
		</span>
		<div class="dynaFieldValue">
			  ${gestioneAlbero.codiceSignificativo}
		</div>
	</div>
	<div class="dynaClear">
		&nbsp;
	</div>
</fieldset>

<div class="dynaField">

<fieldset id="alberostructure">
<legend><fmt:message key="label.struttura.albero"></fmt:message></legend>


<c:set var="padri" value="${padri}" />
<c:set var="js" value="popolaForm('$1','${commandObject.nome}');" />
<%@ include file="/WEB-INF/jsp/alberoClassificatorio/riepilogoAlbero.jsp" %>

</fieldset>

<c:if test="${!empty pulsanteDiUscita}">
	<input type="submit" name="_eventId_close" value="Termina" />	
</c:if>

</div>

<div class="dynaField">
	<fieldset id="topclassificazioneform">
		<legend><fmt:message
			key="title.classificazione.fieldset.inserimento.top.level" /></legend>

		<dyna:text propertyPath="gestioneAlbero.topNome" required="true"
			labelKey="label.classificazione.nome" 
			helpKey="label.classificazione.help.nome" />
		
		<div class="dynaClear">
			&nbsp;
		</div>
	
		<dyna:text propertyPath="gestioneAlbero.topCodice" required="true" 
			labelKey="label.classificazione.codice" 
			helpKey="label.classificazione.help.codice" />			

		<div class="dynaClear">
			&nbsp;
		</div>

		<dyna:boolean  propertyPath="gestioneAlbero.topSelezionabile" 
			labelKey="label.classificazione.selezionabile" 
			helpKey="label.classificazione.help.selezionabile"/>

		<dyna:boolean propertyPath="gestioneAlbero.topAttivo" 
			labelKey="label.classificazione.attivo" 
			helpKey="label.classificazione.help.attivo" />

		<div class="dynaClear">
			&nbsp;
		</div>
	
		<input type="submit" name="_eventId_crea" value="Crea Top Level" />
	</fieldset>
	
	
	
	<fieldset id="classificazioneeditform">
			
			<div id="alberoClassificazioneEditFormHelp"> 
				Seleziona una classificazione per visualizzarne il dettaglio ed accedere 
				alla form di modifica e/o creazione di sottoclassificazioni		
			</div>
						
			<dyna:hidden propertyPath="gestioneAlbero.editID"/>
					
			<div id="formclassificazione">	
				<div class="dynaField">
					<input type="submit" name="_eventId_edit" value="Salva"/>				
				</div>
				
				<div class="dynaField">
					<img type="image" src="${root}/images/delete20x20.jpg"  alt="Cancella la classificazione"  value=" " title="Cancella la classificazione" onclick="confermaEliminazioneClassificazione(document.getElementById('editID').value,document.getElementById('editCodice').value);"/>
				</div>
					
				<div class="dynaClear">
					&nbsp;
				</div>
					
				<dyna:text propertyPath="gestioneAlbero.editNome"
					required="true" labelKey="label.classificazione.nome" 
					helpKey="label.classificazione.help.nome" />

				<div class="dynaClear">
					&nbsp;
				</div>
		
				<dyna:text propertyPath="gestioneAlbero.editCodice" 
					required="true" labelKey="label.classificazione.codice"
					helpKey="label.classificazione.help.codice" />

				<div class="dynaClear">
					&nbsp;
				</div>
	
				<dyna:boolean propertyPath="gestioneAlbero.editSelezionabile" 
					labelKey="label.classificazione.selezionabile"
					helpKey="label.classificazione.help.selezionabile" />
			
				<dyna:boolean propertyPath="gestioneAlbero.editAttivo"
					labelKey="label.classificazione.attivo" 
					helpKey="label.classificazione.help.attivo" />
			
				<div class="dynaClear">&nbsp;</div>
			<c:if test="${!albero.flat}">
				Nuova sottoclassificazione
				<img id="alberoAddSubClassificazione" src="${root}/images/main_plus.gif" onclick="mostraForm()"/>	
				<img id="alberoRemoveSubClassificazione" src="${root}/images/main_plus_active.gif" onclick="nascondiForm()" />
				
			<fieldset id="nuovaSottoClassificazione">
				<legend><fmt:message key="title.classificazione.sublevel.fieldset.inserimento" /></legend>

				<input type="hidden" id="createSubClassificazione" name="createSubClassificazione" value="false" />
				
				<dyna:text  propertyPath="gestioneAlbero.subNome" required="true"
					helpKey="label.classificazione.help.nome" 
					labelKey="label.classificazione.nome" />
		
				<div class="dynaClear">
					&nbsp;
				</div>
			
				<dyna:text propertyPath="gestioneAlbero.subCodice" 
					required="true" labelKey="label.classificazione.codice"
					helpKey="label.classificazione.help.codice" />
		
				<div class="dynaClear">
					&nbsp;
				</div>
		
				<dyna:boolean propertyPath="gestioneAlbero.subSelezionabile"
					helpKey="label.classificazione.help.selezionabile"
					labelKey="label.classificazione.selezionabile" />
			
				<dyna:boolean propertyPath="gestioneAlbero.subAttivo"
					helpKey="label.classificazione.help.attivo"
					labelKey="label.classificazione.attivo" />
		
				<div class="dynaClear">
					&nbsp;
				</div>	
			</fieldset>
			
			</c:if>		
			<c:if test="${albero.flat}">
				L'albero è di tipo FLAT. 
				Non è permesso l'immissione di sottoclassificazioni.
			</c:if>	
		</div>
	</fieldset>	
	
</div>	

<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}" />

	 	<spring:bind path="gestioneAlbero.editCodice">
		<c:if test="${not empty status.errorMessages}">			
			<script type="text/javascript">onload:openAll();</script>
		</c:if>
		</spring:bind>
		<spring:bind path="gestioneAlbero.editNome">
		<c:if test="${not empty status.errorMessages}">			
			<script type="text/javascript">onload:openAll();</script>
		</c:if>
		</spring:bind>
		<spring:bind path="gestioneAlbero.subNome">
		<c:if test="${not empty status.errorMessages}">			
			<script type="text/javascript">onload:openAll();</script>
		</c:if>
		</spring:bind>
		<spring:bind path="gestioneAlbero.subCodice">
		<c:if test="${not empty status.errorMessages}">			
			<script type="text/javascript">onload:openAll();</script>
		</c:if>
		</spring:bind>
		

</form:form>
</body>