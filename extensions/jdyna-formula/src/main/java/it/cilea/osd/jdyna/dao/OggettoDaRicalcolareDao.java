/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.jdyna.dao;

import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.jdyna.util.OggettoDaRicalcolare;

import java.util.List;

public interface OggettoDaRicalcolareDao extends PaginableObjectDao<OggettoDaRicalcolare,Integer> {	
	public List<OggettoDaRicalcolare> findFormuleByVariabile(Integer variabileId,String variabileClasse);
	public OggettoDaRicalcolare uniqueFormulaByProprietaId(Integer proprietaId);
	public List<OggettoDaRicalcolare> findFormuleByProprieta(Integer proprieta);
	//public List<ProprietaDaRicalcolare> findFormuleByWidget(Integer widget);
	public List<OggettoDaRicalcolare> findOggettoDaRicalcolareByDatiEvento(String clazzNotified, Integer id);
	public void deleteOggettoDaRicalcolareByOggetto(Integer oggettoId, String oggettoClass);
	//public void deleteOggettoDaRicalcolareByIdentificatore(String oggettoIdentificatore, String oggettoClass);
	//public long countOggettoDaRicalcolareByIdentificatore(String oggettoIdentificatore, String oggettoClass);
}
