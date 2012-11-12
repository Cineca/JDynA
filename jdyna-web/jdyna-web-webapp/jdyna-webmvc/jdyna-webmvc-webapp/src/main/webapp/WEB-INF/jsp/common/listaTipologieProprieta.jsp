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

<display:table name="${tipologiaProprietaList}" cellspacing="0" cellpadding="0"
	requestURI="" id="tipologiaProprietaList" class="table" export="false" pagesize="20">
	<display:column property="id" escapeXml="true" sortable="true"
		titleKey="column.id" url="/admin/${baseObjectURL}/tipologiaProprieta/details.htm" paramId="id"
		paramProperty="id" />		
	<display:column property="label" escapeXml="false" sortable="true"
		titleKey="column.label"/>
	<display:column property="shortName" escapeXml="false" sortable="true"
		titleKey="column.shortName"/>
	<display:column property="obbligatorieta" escapeXml="false" sortable="true"
		titleKey="column.obbligatorieta"/>
	<display:column property="ripetibile" escapeXml="false" sortable="true"
		titleKey="column.ripetibile"/>
	<display:column property="showInList" escapeXml="false" sortable="true"
		titleKey="column.showInList"/>
	<display:column property="priorita" escapeXml="false" sortable="true"
		titleKey="column.priorita"/>
	<display:column property="rendering.class.simpleName" escapeXml="true" sortable="true"
		titleKey="column.rendering"/>
</display:table>
