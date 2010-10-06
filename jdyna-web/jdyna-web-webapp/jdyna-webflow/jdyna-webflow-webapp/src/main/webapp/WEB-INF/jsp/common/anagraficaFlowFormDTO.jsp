<%@ include file="/common/taglibs.jsp"%>


<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<head>
<title>Modifica ${simpleNameAnagraficaObject} </title>


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
</head>
<script type="text/javascript">
 DWREngine.setErrorHandler(null);
 DWREngine.setWarningHandler(null);
</script>
<%-- Serve per triview e la nostra dyna tag library --%>
<c:set var="commandObject" value="${anagraficadto}" scope="request" />
<c:set var="simpleNameAnagraficaObject" value="${simpleNameAnagraficaObject}" scope="page" />
<c:set var="mostraPulsanteFormule" value="${mostraPulsanteFormule}" scope="page" />

<form:form commandName="anagraficadto" action="" method="post">
	 <spring:hasBindErrors name="anagraficadto">
         <div class="error">
         Non sara' possibile salvare i dati contenuti nel form fino a quando non saranno risolti i seguenti problemi (cliccare sul messaggio per raggiungere rapidamente il campo in errore):
         <ul>
            <c:forEach var="errMsgObj" items="${errors.fieldErrors}">
               <li>
                 <c:if test="${errMsgObj.code != 'typeMismatch'}">
                  ${errMsgObj.defaultMessage}: 
                 </c:if>
                 <a href="#errorMsg${errMsgObj.field}">
                  <spring:message arguments="${errMsgObj.arguments}" code="${errMsgObj.code}"/></a>
               </li>
            </c:forEach>
         </ul>
         </div>
      </spring:hasBindErrors>
	
	<dyna:hidden propertyPath="anagraficadto.areaId" />
	<dyna:hidden propertyPath="anagraficadto.objectId" />
	<input type="hidden" id="newAreaId" name="newAreaId" />
<div id='tab'>
    <ul id="tablist">  
  <c:forEach items="${areaList}" var="area">
    <li id="liCorrente${area.id}"<c:if test="${area.id == areaId}">
			class="selected"
			</c:if>>    	    	    	
    	<a onclick="changeArea(${area.id})">${area.title}</a> 
    </li>
  </c:forEach>
  </ul>
</div>

<div id='content'>
	<c:forEach items="${tipologieProprietaInArea}" var="tipologiaDaVisualizzare">

		<% 
		   List<String> parameters = new ArrayList<String>();
		   parameters.add(pageContext.getAttribute("simpleNameAnagraficaObject").toString());
		   parameters.add(((PropertiesDefinition)pageContext.getAttribute("tipologiaDaVisualizzare")).getShortName());
		   pageContext.setAttribute("parameters",parameters);		   
		%>
	 	
	 	 <dyna:edit tipologia="${tipologiaDaVisualizzare}" 
	 	 	propertyPath="anagraficadto.anagraficaProperties[${tipologiaDaVisualizzare.shortName}]" ajaxValidation="validateAnagraficaProperties" validationParams="${parameters}"/>	 	 	
	 	 		 	 	 	 	
	</c:forEach>
<%--	
<c:if test="${showRicalcola}">	
	<input src="${root}/images/icons/formule.jpg" type="image" name="_eventId_calculate" value="Ricalcola" title="Ricalcola le formule" onhelp="Ricalcola le formule presenti nell'area" />
</c:if>
--%> 	
</div>
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	<input type="submit" name="_eventId_cancel" value="Annulla" />
	<c:if test="${mostraPulsanteFormule}">
		<input type="submit" name="_eventId_calculate" value="Ricalcola" />
	</c:if>	
	<input type="submit" name="_eventId_save" value="Salva"/>
</form:form>