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

  	<!-- Search form -->                  
    

    <div class="searchform">
        <form action="${root}/flusso.flow?_flowId=ricercaSemplice-flow" method="post" class="form">
        <p>
           <input name="query" class="query" id="query"/>
		   <input type="hidden" name="classe" id="classe" value="ovunque"/>
		   <input type="submit" value="Vai" name="button" class="button"/>
		</p>
       </form>
     </div>
        <div class="searchicon">
        	<img alt="search" src="${root}/images/icons/xmag-30.png" />
   		 </div>
