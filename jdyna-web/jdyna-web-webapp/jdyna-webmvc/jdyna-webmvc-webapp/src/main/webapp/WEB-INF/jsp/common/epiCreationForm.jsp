<%--
JDynA, Dynamic Metadata Management for Java Domain Object

 Copyright (c) 2008, CILEA and third-party contributors as
 indicated by the @author tags or express copyright attribution
 statements applied by the authors.  All third-party contributions are
 distributed under license by CILEA.

 This copyrighted material is made available to anyone wishing to use, modify,
 copy, or redistribute it subject to the terms and conditions of the GNU
 Lesser General Public License v3 or any later version, as published 
 by the Free Software Foundation, Inc. <http://fsf.org/>.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this distribution; if not, write to:
  Free Software Foundation, Inc.
  51 Franklin Street, Fifth Floor
  Boston, MA  02110-1301  USA
--%>

<%@ include file="/common/taglibs.jsp"%>


<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.cilea.osd.jdyna.model.PropertiesDefinition"%>

<script type="text/javascript">
 DWREngine.setErrorHandler(null);
 DWREngine.setWarningHandler(null);
</script>

<%-- Serve per triview e la nostra dyna tag library --%>
<c:set var="commandObject" value="${anagraficadto}" scope="request" />
<c:set var="simpleNameAnagraficaObject" value="${simpleNameAnagraficaObject}" scope="page" />
<%--	 breadcumbs  --%>
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


<div id='content'>
	<c:forEach items="${tipologieProprietaInArea}" var="tipologiaDaVisualizzare">

		<% 
		   List<String> parameters = new ArrayList<String>();
		   parameters.add(pageContext.getAttribute("simpleNameAnagraficaObject").toString());
		   parameters.add(((PropertiesDefinition)pageContext.getAttribute("tipologiaDaVisualizzare")).getShortName());
		   pageContext.setAttribute("parameters",parameters);		   
		%>
	 	
	 	 <dyna:edit tipologia="${tipologiaDaVisualizzare}" 
	 	 	propertyPath="anagraficadto.anagraficaProperties[${tipologiaDaVisualizzare.shortName}]" ajaxValidation="validateAnagraficaProperties" validationParams="${parameters}" isCreation="true"/>	 	 	
	 	 		 	 	 	 	
	</c:forEach>
</div>
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}" />
	<input type="submit" name="_eventId_cancel" value="Annulla" />
	<input type="submit" name="_eventId_save" value="Salva"/>
</form:form>
