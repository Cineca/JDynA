/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.dao;

import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import java.util.List;

public interface MultiTypeDaoSupport <A extends ATipologia<TP>, TP extends PropertiesDefinition> {
	/**
	 * Restituisce la lista di tipologie proprieta' ammissibili per l'insieme di tipologie individuate da 
	 * <code>listaTipologie</code>.
	 * 
	 * @author biondo
	 * @param listaTipologie, lista delle tipologie
	 * @param sizeList, la cardinalita' dell'insieme di tipologie passate
	 * Il parametro <code>sizeList</code> e' necessario perche' durante la fase di intersezione delle
	 * tipologie di proprieta' delle varie categorie (tipologie), vengono selezionate quelle tipologie
	 * di proprieta' non comuni a tutte le categorie.
	 * 
	 * @return lista di tipologieproprieta' ammesse dal type
	 */
	public List<TP> findTipologieProprietaAssegnabiliPerMultiType(List<? extends ATipologia<TP>> listaTipologie, long sizeList);
	public List<TP> findTipologieProprietaAssegnabiliPerMultiTypeAndArea(List<? extends ATipologia<TP>> listaTipologie, long sizeList, Integer area);

}
