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
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.common.model.Selectable;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Classe che rappresenta la Classificazione in una classificazione.
 * 
 * @author biondo,pascarelli,giuralarocca
 *
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="dyna_classificazioni", 
		uniqueConstraints = {@UniqueConstraint(columnNames={"codice","alberoclassificatorio_id"})})
@NamedQueries( {
    @NamedQuery(name = "Classificazione.findAll", query = "from Classificazione order by id"),    
    @NamedQuery(name ="Classificazione.uniqueByCodiceInAlbero", query ="from Classificazione where codice=? and alberoClassificatorio = ?"),
    @NamedQuery(name ="Classificazione.uniqueByCodiceInAlberoWithName", query ="from Classificazione where codice=? and alberoClassificatorio.nome = ?"),
    @NamedQuery(name = "Classificazione.findSubClassByPadreCodice", query = "from Classificazione where alberoClassificatorio.id = ? and padre.codice = ?"),
    @NamedQuery(name = "Classificazione.findTopClassificazioni", query = "from Classificazione where alberoClassificatorio.id = ? and padre = null"),
    @NamedQuery(name = "Classificazione.findSubClassAttiveByPadreCodice", query = "from Classificazione where alberoClassificatorio.id = ? and padre.codice = ? and attivo = true"),
    @NamedQuery(name = "Classificazione.findTopClassificazioniAttive", query = "from Classificazione where alberoClassificatorio.id = ? and padre = null and attivo = true"),
    @NamedQuery(name = "Classificazione.findPadri", query = "from Classificazione where padre = null"),
    @NamedQuery(name ="Classificazione.findClassificazioneByCodice", query ="from Classificazione where codice=?"),    
    @NamedQuery(name ="Classificazione.findCodiciInAlberoByName", query ="select codice from Classificazione where alberoClassificatorio.nome = ? and nome = ?")
    
})
public class Classificazione extends IdentifiableObject implements Selectable {
	/** chiave primaria */
	 @Id
	 @GeneratedValue
	 private Integer id;
	
	 /** nome della categoria */	
	private String nome;
	
	/** padre della sottoarea */
	@ManyToOne (fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)	
  	private Classificazione padre;
	
	
	/** se selezionabile o meno*/
	private boolean selezionabile;
	
	/** se la categoria e' utilizzabile per nuovi inserimenti/modifiche */	
	private boolean attivo;
	
	
	/** codice descrittivo della categoria*/	
	private String codice;
	
	/** lista di categorie */
	@OneToMany (mappedBy="padre", fetch = FetchType.EAGER)
	@Fetch(value=FetchMode.SELECT)
	@Cascade(value={CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Classificazione> sottoCategorie;

	 @ManyToOne
	 private AlberoClassificatorio alberoClassificatorio;
	 
	 /** i18n key for not selectable error message */
	public static final String NOTSELECTABLE = "classificazione.notselectable";

		
	public Classificazione() {
		sottoCategorie = new LinkedList<Classificazione>();
	}

	public AlberoClassificatorio getAlberoClassificatorio() {
		return alberoClassificatorio;
	}

	public void setAlberoClassificatorio(
			AlberoClassificatorio alberoClassificatorio) {
		this.alberoClassificatorio = alberoClassificatorio;
	}


	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}


	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}


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

	public Classificazione getPadre() {
		return padre;
	}

	public void setPadre(Classificazione padre) {
		this.padre = padre;
	}


	public boolean isSelezionabile() {
		return selezionabile;
	}

	public void setSelezionabile(boolean selezionabile) {
		this.selezionabile = selezionabile;
	}

	public List<Classificazione> getSottoCategorie() {
		return sottoCategorie;
	}


	public String getDisplayValue() {
		return nome;
	}


	public String getIdentifyingValue() {
		return this.alberoClassificatorio.getNome() + ":" + this.codice;
		// return this.codice;
	}

	@Override
	public String toString() {
		if (codice != null)
			return codice + " - " + nome;
		return "";
	}

	/** crea una sotto classificazione */
	public Classificazione createClassificazione() {
		Classificazione classificazione = new Classificazione();
		sottoCategorie.add(classificazione);
		classificazione.setPadre(this);
		classificazione.setAlberoClassificatorio(getAlberoClassificatorio());
		return classificazione;

	}

	@Transient
	public List<Classificazione> getSottoCategorieAttive() {
		List<Classificazione> results = new LinkedList<Classificazione>();
		for (Classificazione classificazione : getSottoCategorie()) {
			if (classificazione.isAttivo()) {
				results.add(classificazione);
			}
		}
		return results;
	}


}
