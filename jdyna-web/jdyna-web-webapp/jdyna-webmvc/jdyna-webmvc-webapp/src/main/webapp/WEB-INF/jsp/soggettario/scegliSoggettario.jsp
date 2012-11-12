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
<%@ include file="/common/scripts.jsp"%>


<c:set var="commandObject" value="${widget}" scope="request" />

SCEGLI SOGGETTARI O CREANE UNO NUOVO:
<form:form commandName="widget" action="flusso.flow" method="post">

<c:forEach items="${soggettari}" var="elemento">			
<dyna:checkbox  value="${elemento.id}"  propertyPath="widget.soggettari" labelKey="${elemento.nome}" /><br/>
</c:forEach>

<%-- pulsanti di submit --%>
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
<input type="submit" name="_eventId_cancel" value="Annulla"/>
<input type="submit" name="_eventId_nuovo" value="Crea Nuovo Soggettario"/>
<input type="submit" name="_eventId_avanti" value="Avanti"/>

</form:form>
