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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="dyna_soggettari")
@NamedQueries({
		@NamedQuery(name="Soggettario.findAll", query="from Soggettario order by id"),
		@NamedQuery(name="Soggettario.uniqueByNome", query="from Soggettario where nome = ?")
})
public class Soggettario extends IdentifiableObject implements Selectable {

	@Id
	@GeneratedValue	 
	private Integer id;
	
	/** Nome del soggettario*/
	private String nome;
	
	/** Descrizione del soggettario*/
	private String descrizione;
		
	/** elementi del soggettario */
	@OneToMany  (fetch = FetchType.EAGER , mappedBy="soggettario")
	@Fetch (value = FetchMode.SELECT)
	@Cascade (value = {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	@JoinTable(name="dyna_soggettari2soggetti")
	private List<Soggetto> soggetti;
	
	private boolean chiuso;
	
	private boolean attivo;

	public Soggettario() {
		this.soggetti = new LinkedList<Soggetto>();
	}
	/** 
     * Crea un soggetto associandolo al soggettario 
     * 
     * @return il soggetto creato
     **/
    public Soggetto createSoggetto()
    {
        Soggetto soggetto = new Soggetto();
        soggetto.setSoggettario(this);
        if (soggetti == null)
            soggetti = new LinkedList<Soggetto>();
        soggetti.add(soggetto);
        return soggetto;
    }
	
	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public boolean isChiuso() {
		return chiuso;
	}

	public void setChiuso(boolean chiuso) {
		this.chiuso = chiuso;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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


	public String getDisplayValue() {
		return nome;
	}

	public String getIdentifyingValue() {
		return id.toString();
	}

	public List<Soggetto> getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(List<Soggetto> soggetti) {
		this.soggetti = soggetti;
	}	
	
	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof Soggettario) {
			Soggettario soggettario = (Soggettario)object;
			if(this.getNome().equals(soggettario.getNome()))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return nome != null ? nome.hashCode() : 0;
	}
}