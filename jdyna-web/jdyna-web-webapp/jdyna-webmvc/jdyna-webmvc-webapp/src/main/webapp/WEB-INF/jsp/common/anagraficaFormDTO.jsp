<%@ include file="/common/taglibs.jsp"%>


<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.cilea.osd.jdyna.model.PropertiesDefinition"%>
<head>
<title>Modifica ${simpleNameAnagraficaObject}</title>


<content tag="breadcrumbs">
<ul class='breadCumps'>
	<c:forEach items="${breadCumbs}" var="link">
		<li><c:if test='${!empty link.key}'>
			<a href='${link.key}'>${link.value}</a>
		</c:if> <c:if test='${empty link.key}'> 
${link.value}
</c:if></li>
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
<c:set var="simpleNameAnagraficaObject"
	value="${simpleNameAnagraficaObject}" scope="page" />
<!--<c:set var="mostraPulsanteFormule" value="${mostraPulsanteFormule}" scope="page" />

-->
<form:form commandName="anagraficadto" action="" method="post">
	<spring:hasBindErrors name="anagraficadto">
		<div class="error">Non sara' possibile salvare i dati contenuti
		nel form fino a quando non saranno risolti i seguenti problemi
		(cliccare sul messaggio per raggiungere rapidamente il campo in
		errore):
		<ul>
			<c:forEach var="errMsgObj" items="${errors.fieldErrors}">
				<li><c:if test="${errMsgObj.code != 'typeMismatch'}">
                  ${errMsgObj.defaultMessage}: 
                 </c:if> <a href="#errorMsg${errMsgObj.field}"> <spring:message
					arguments="${errMsgObj.arguments}" code="${errMsgObj.code}" /></a></li>
			</c:forEach>
		</ul>
		</div>
	</spring:hasBindErrors>

	<dyna:hidden propertyPath="anagraficadto.areaId" />
	<dyna:hidden propertyPath="anagraficadto.objectId" />
	<input type="hidden" id="newAreaId" name="newAreaId" />
	<div id='tab'>
	<ul id="tablist">
		<c:forEach items="${tabList}" var="area">
			<li id="liCorrente${area.id}"
				<c:if test="${area.id == areaId}">
			class="selected"
			</c:if>>
			<a onclick="changeArea(${area.id})">${area.title}</a></li>
		</c:forEach>
	</ul>
	</div>

	<div id='jdynacontent'>
		<c:forEach items="${propertiesHolders}"	var="holder">

		<c:catch var="noCustomJSP">
			<c:import url="${root}/authority/jdyna/custom/${holder.shortName}.jsp"></c:import>
		</c:catch>

			<c:if test="${!empty noCustomJSP}">

				<c:forEach items="${propertiesDefinitionsInTab}"
					var="tipologiaDaVisualizzare">

					<%
						List<String> parameters = new ArrayList<String>();
											parameters.add(pageContext.getAttribute(
													"simpleNameAnagraficaObject")
													.toString());
											parameters
													.add(((PropertiesDefinition) pageContext
															.getAttribute("tipologiaDaVisualizzare"))
															.getShortName());
											pageContext.setAttribute("parameters",
													parameters);
					%>

					<dyna:edit tipologia="${tipologiaDaVisualizzare}"
						propertyPath="anagraficadto.anagraficaProperties[${tipologiaDaVisualizzare.shortName}]"
						ajaxValidation="validateAnagraficaProperties"
						validationParams="${parameters}" />

				</c:forEach>

			</c:if>
		
	</c:forEach></div>

	<input type="submit" name="cancel" value="Annulla" />	
	<input type="submit" name="save" value="Salva" />
</form:form>