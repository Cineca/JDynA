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
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.common.model.Selectable;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

/**
 * Classe che rappresenta un albero classificatorio
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name="dyna_alberi")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries( {
		@NamedQuery(name = "AlberoClassificatorio.findAll", query = "from AlberoClassificatorio order by id"),
		@NamedQuery(name = "AlberoClassificatorio.findTopClassificazioni", query = "from Classificazione where alberoClassificatorio = ? and padre = null"),
		@NamedQuery(name = "AlberoClassificatorio.uniqueByNome", query = "from AlberoClassificatorio where nome = ?") 
	})
public class AlberoClassificatorio extends IdentifiableObject implements
		Selectable {

	/** chiave primaria */
	@Id
	@GeneratedValue
	private Integer id;

	/** nome della categoria */
	@Column(unique = true)
	private String nome;

	//indica se stampare il codice o il nome delle
	//classificazioni
	private boolean codiceSignificativo;
	
	private String descrizione;

	private boolean attiva;
		
	/** classificazioni di primo livello riferite all'albero */
	@OneToMany(mappedBy = "alberoClassificatorio")
	@Where(clause = "padre_id is null")
	@OrderBy(clause="codice asc")
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	private List<Classificazione> topClassificazioni;

	/** indica se a true che non puo' avere classificazioni non top level; 
	 *  l'uso del campo avviene solo via API o import */	
	private boolean flat;
	
	public AlberoClassificatorio() {
		topClassificazioni = new LinkedList<Classificazione>();
	}

	/** crea una classificazione e la inserisce nella top categoria */
	public Classificazione createClassificazione() {
		
		Classificazione classificazione = new Classificazione();
		topClassificazioni.add(classificazione);
		classificazione.setAlberoClassificatorio(this);		
		classificazione.setPadre(null);
		return classificazione;

	}

	// getter e setter

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAttiva() {
		return attiva;
	}

	public void setAttiva(boolean attiva) {
		this.attiva = attiva;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@RemoteProperty()
	public String getDisplayValue() {
		return nome;
	}

	@RemoteProperty()
	public String getIdentifyingValue() {
		return id.toString();
	}

	public List<Classificazione> getTopClassificazioni() {
		return topClassificazioni;
	}

	public void setTopClassificazioni(List<Classificazione> topClassificazioni) {
		this.topClassificazioni = topClassificazioni;
	}
	
	@Transient
	public List<Classificazione> getTopClassificazioniAttive() {
		
			List<Classificazione> results = new LinkedList<Classificazione>();
			for(Classificazione classificazione : getTopClassificazioni()) {
				if(classificazione.isAttivo()) {
					results.add(classificazione);
				}
			}
			return results;
		
	}

	public boolean isFlat() {
		return flat;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
	}

	public boolean isCodiceSignificativo() {
		return codiceSignificativo;
	}

	public void setCodiceSignificativo(boolean codiceSignificativo) {
		this.codiceSignificativo = codiceSignificativo;
	}

}