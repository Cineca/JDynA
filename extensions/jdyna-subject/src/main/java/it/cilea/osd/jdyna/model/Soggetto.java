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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;



/**
 * Elemento del soggettario
 * @author pascarelli,biondo
 *
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="dyna_soggetti", 
		uniqueConstraints = {@UniqueConstraint(columnNames={"voce","soggettario_id"})})
@NamedQueries( {
    @NamedQuery(name = "Soggetto.findAll", query = "from Soggetto order by id"),
    @NamedQuery(name = "Soggetto.findBySoggettario", query = "from Soggetto where soggettario.id = ?"),
    @NamedQuery(name = "Soggetto.uniqueInSoggettario", query = "from Soggetto where soggettario.id = ? and voce = ?"),
    @NamedQuery(name = "Soggetto.findLikeBySoggettario", query = "from Soggetto where soggettario.id = ? and voce like ?")
})
public class Soggetto extends IdentifiableObject{
	
	@Id
	@GeneratedValue	 
	private Integer id;
	
	/** nome dell'elemento */
	private String voce;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@Cascade(value={CascadeType.PERSIST}) // necessario per i soggettari aperti in cui i soggetti vengono creati in cascata dai valori
	private Soggettario soggettario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Soggettario getSoggettario() {
		return soggettario;
	}

	public void setSoggettario(Soggettario soggettario) {
		this.soggettario = soggettario;		
		this.soggettario.getSoggetti().add(this);
	}

	public String getVoce() {
		return voce;
	}

	public void setVoce(String voce) {
		this.voce = voce;
	}
	
	@Override
	public String toString() {
		return getVoce();
	}	
	

		
}
