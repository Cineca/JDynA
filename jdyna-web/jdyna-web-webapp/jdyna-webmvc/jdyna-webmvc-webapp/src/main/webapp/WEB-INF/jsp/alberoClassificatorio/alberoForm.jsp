<%@ include file="/common/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" media="screen,projection,print"
	href="<c:url value='/css/style_autocomplete.css'/>" />

<c:set var="commandObject" value="${albero}" scope="request" />

<content  tag="breadcrumbs">
<ul class='breadCumps'>
<c:forEach items="${breadCumbs}" var="link">
<li>
<c:if test='${!empty link.key}'> 
<a href='${link.key}'>${link.value}</a>
</c:if>
<c:if test='${empty link.key}'> 
${link.value}
</c:if>
</li>
</c:forEach>
</ul>
</content>
<form:form commandName="albero" action="flusso.flow" method="post" >
	<spring:bind path="albero.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="error"><c:forEach var="error"
				items="${status.errorMessages}">
	               ${error}<br />
			</c:forEach></div>
		</c:if>
	</spring:bind>

	<c:if test="${not empty status.errorMessages}">
		<div class="error"><c:forEach var="error"
			items="${status.errorMessages}">
                 ${error}<br />
		</c:forEach></div>
	</c:if>
	
	
	<fieldset><legend><fmt:message key="title.albero.fieldset.inserimento" /></legend>
	
		

	<div style="display: none;" class="helpTip" id="nome">
		<img src="${root}/images/delete.gif" onclick="hideHelp('nome')" class="close"/>
			
			<p><fmt:message key="label.albero.help.nome" /></p>
	
	</div>  
	<div class="dynaField">
		<span class="dynaLabel" style="min-width: 21em;">
		<fmt:message key="label.albero.nome"/>
		<img src="${root}/images/help.gif" onclick="showHelp('nome')" class="help"/>
		</span>
		
	</div>
	<div class="dynaFieldValue">
	<dyna:text  propertyPath="albero.nome" label=" " required="true"
				size="30"/>
	</div>
	<div class="dynaClear">
		&nbsp;
		</div>
		

	<div style="display: none;" class="helpTip" id="descrizione">
		<img src="${root}/images/delete.gif" onclick="hideHelp('descrizione')" class="close"/>
			
			<p><fmt:message key="label.albero.help.descrizione" /></p>
	
	</div>  
	<div class="dynaField">
		<span class="dynaLabel" style="min-width: 21em;">
		<fmt:message key="label.albero.descrizione"/>
		<img src="${root}/images/help.gif" onclick="showHelp('descrizione')" class="help"/>
		</span>
	</div>

	<div class="dynaFieldValue">
	<dyna:textarea  propertyPath="albero.descrizione" label=" " cols="50" rows="10"/>										
	</div>
	<div class="dynaClear">
		&nbsp;
	</div>
		<dyna:boolean propertyPath="albero.codiceSignificativo" labelKey="label.albero.isCodiceSignificativo" helpKey="help.albero.isCodiceSignificativo"></dyna:boolean>
	<c:if test="${albero.flat}">
		<dyna:boolean propertyPath="albero.flat" labelKey="label.albero.flat" helpKey="help.albero.flat"></dyna:boolean>
	</c:if>
	
<%--<div style="display: none;" class="helpTip" id="attivo">
		<img src="${root}/images/delete.gif" onclick="hideHelp('attivo')" class="close"/>
			
			<p><fmt:message key="label.albero.help.attivo" /></p>
	
	</div>
  
	<div class="dynaField">
		<span class="dynaLabel" style="min-width: 21em;">
		<fmt:message key="label.attivo"/>
		<img src="${root}/images/help.gif" onclick="showHelp('attivo')" class="help"/>
		</span>
		
	</div>

	<div class="dynaFieldValue">
		<dyna:checkbox  propertyPath="albero.attiva" label=""/>	
	</div>  --%>
	</fieldset>

	<%-- pulsanti di submit --%>

	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}" />
	<input type="submit" name="_eventId_exit" value="Annulla"/>
	<input type="submit" name="_eventId_avanti" value="Salva" />

</form:form>