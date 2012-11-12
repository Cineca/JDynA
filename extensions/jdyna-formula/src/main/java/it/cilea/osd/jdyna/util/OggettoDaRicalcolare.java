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
package it.cilea.osd.jdyna.util;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.event.JPAEvent;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ProprietaDaRicalcolare e' l'oggetto che si occupa di mantenere al suo interno
 * le informazioni necessarie per calcolare con i giusti parametri una formula.
 * 
 * @see FormulaManager, WidgetFormula
 * 
 * 
 * @author pascarelli
 * 
 */
@Entity
@Table(name="event_formule_oggettidaricalcolare")
@NamedQueries( {		
		@NamedQuery(name = "OggettoDaRicalcolare.findAll", query = "from OggettoDaRicalcolare order by id"),			
		@NamedQuery(name = "OggettoDaRicalcolare.findFormuleByOggetto", query = "select oggettoDaRicalcolare from OggettoDaRicalcolare oggettoDaRicalcolare where oggettoDaRicalcolare.oggettoId = ? and oggettoDaRicalcolare.oggettoClass = ?"),
		//@NamedQuery(name = "OggettoDaRicalcolare.findFormuleByOggetto", query = "select oggettoDaRicalcolare from OggettoDaRicalcolare oggettoDaRicalcolare where oggettoDaRicalcolare.oggettoIdentificatore = ? and oggettoDaRicalcolare.oggettoClass = ?"),
		@NamedQuery(name = "OggettoDaRicalcolare.findOggettoDaRicalcolareByDatiEvento", query = "from OggettoDaRicalcolare oggettoDaRicalcolare where event.clazz = ? and event.id = ?"),
		@NamedQuery(name = "OggettoDaRicalcolare.deleteOggettoDaRicalcolareByOggetto", query = "delete from OggettoDaRicalcolare where oggettoId = ? and oggettoClass = ?")
		//@NamedQuery(name = "OggettoDaRicalcolare.deleteOggettoDaRicalcolareByIdentificatore", query = "delete from OggettoDaRicalcolare where oggettoIdentificatore = ? and oggettoClass = ?") ,
		//@NamedQuery(name = "OggettoDaRicalcolare.countOggettoDaRicalcolareByIdentificatore", query = "select count(*) from OggettoDaRicalcolare where oggettoIdentificatore = ? and oggettoClass = ?")
})
public class OggettoDaRicalcolare implements Identifiable {
	@Transient
	protected Log log = LogFactory.getLog(OggettoDaRicalcolare.class);

	/** Chiave primaria di accesso */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer oggettoId;
	
	private String oggettoClass;

	@Embedded
	/**
	 * NB: al momento la tipologia di evento non viene utilizzata, il dato e'
	 * stato memorizzato solo per la comodita' di sfruttare la defininizione del
	 * JPAEvent all'interno della definizione di OggettoDaRicalcolare
	 */
	private JPAEvent event;
	
	public Integer getId() {
		return id;
	}

	public Integer getOggettoId() {
		return oggettoId;
	}

	public void setOggettoId(Integer oggettoId) {
		this.oggettoId = oggettoId;
	}

	public String getOggettoClass() {
		return oggettoClass;
	}

	public void setOggettoClass(String oggettoClass) {
		this.oggettoClass = oggettoClass;
	}

	public OggettoDaRicalcolare(Integer oggettoId,String oggettoClass, JPAEvent event) {
		super();
		this.oggettoId = oggettoId;
		this.oggettoClass = oggettoClass;
		this.event = event;
	}

	public OggettoDaRicalcolare() {
	}

//	@Override
//	public int hashCode() {
//		HashCodeBuilder hash = new HashCodeBuilder(17,37); 
//		if(evento!=null) hash.append(evento.hashCode());
//		if(oggettoClass!=null) hash.append(oggettoClass.hashCode());
//		if(oggettoId!=null) hash.append(oggettoId.hashCode());		
//		return hash.toHashCode();
//	}
}
