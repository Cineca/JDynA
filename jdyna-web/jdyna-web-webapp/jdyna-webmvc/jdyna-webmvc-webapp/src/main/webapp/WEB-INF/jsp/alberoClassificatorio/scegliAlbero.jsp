<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/scripts.jsp"%>


<c:set var="commandObject" value="${widget}" scope="request" />

SCEGLI UN ALBERO CLASSIFICATORIO:
<form:form commandName="widget" action="flusso.flow" method="post">

<dyna:select  layoutMode="table" collection="${contenitori}" propertyPath="widget.alberoClassificatorio" labelKey="label.albero" />

<br/>

<%-- pulsanti di submit --%>
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
<input type="submit" name="_eventId_cancel" value="Annulla"/>
<input type="submit" name="_eventId_nuovo" value="Crea Nuovo Albero"/>
<input type="submit" name="_eventId_avanti" value="Avanti"/>

</form:form>