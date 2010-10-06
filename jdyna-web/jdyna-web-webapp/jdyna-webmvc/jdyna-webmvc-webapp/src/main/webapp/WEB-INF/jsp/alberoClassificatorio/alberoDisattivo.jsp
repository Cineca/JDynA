<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/scripts.jsp"%>

<form:form commandName="classificazione" action="flusso.flow" method="post">

L'albero selezionato non e' attivo, selezionane un altro!
<br/>

<%-- pulsanti di submit --%>
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
<input type="submit" name="_eventId_avanti" value="Riseleziona"/>

</form:form>