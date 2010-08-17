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

import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import java.util.List;

public interface TypeDaoSupport <A extends ATipologia<TP>, TP extends PropertiesDefinition> {
	/**
	 * Restituisce la lista di tipologie proprietà ammissibili per la sottotipologia individuata da 
	 * <code>tipologiaOggetto</code> ({@link ATipologia}).
	 * 
	 * @param tipologiaOggetto, la Tipologia
	 * @return lista di proprietà ammesse dal type
	 */
	public List<TP> findTipologieProprietaInTipologia(ATipologia<TP> tipologiaOggetto);
	public List<TP> findTipologieProprietaInTipologiaAndShowInList(ATipologia<TP> tipologiaOggetto);
	public List<TP> findTipologieProprietaInTipologiaAndArea(ATipologia<TP> tipologiaOggetto,Integer area);
	public A uniqueByNome(String nome);
	
}
