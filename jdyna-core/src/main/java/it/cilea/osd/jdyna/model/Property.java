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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;

/**
 * Rappresenta una singola proprieta' dell'oggetto.
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries( {
		@NamedQuery(name = "Property.findPropertyByWidget", query = "from Property where typo.rendering = ?"),		
		@NamedQuery(name = "Property.countValueByPropertiesDefinition", query = "select count(*) from Property where typo.id = ?")		
})
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public abstract class Property <TP extends PropertiesDefinition> extends IdentifiableObject implements Comparable<Property<TP>> {
	
	@Transient
	private Log log = LogFactory.getLog(Property.class);
	
	/** chiave d'accesso primario */
	@Id
	//@GeneratedValue(strategy = GenerationType.TABLE)	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JDYNA_PROP_SEQ")
    @SequenceGenerator(name = "JDYNA_PROP_SEQ", sequenceName = "JDYNA_PROP_SEQ")
	private Integer id;

	/** Position order */
	private int positionDef;
	
	@OneToOne
	@JoinColumn(unique = true)	
	@Cascade (value = {CascadeType.ALL})
	//@NotFound(action=NotFoundAction.IGNORE)
	private AValue value;

	/**
	 * the visibility flags of the field
	 */
	private Integer visibility;
	
	
	/**
	 * setting 1 to update property only with a batch procedure 
	 */
	private Integer lockDef;
	
	
	/**
	 * utility attribute, here you can set for example currency, language or name of subject or classification parent 
	 */
	@OneToOne
	@Cascade (value = {CascadeType.ALL})
	private ScopeDefinition scopeDef;
	
	/**
     * Getter method.
     *
     * 
     * @return the visibility flags of the field
     */
    public Integer getVisibility()
    {
    	if(this.visibility==null) {
    		return 0;
    	}
        return visibility;
    }

    /**
     * Setter method. 
     * 
     * @param visibility
     *            the visibility flags of the field
     */
    public void setVisibility(Integer visibility)
    {	    
		this.visibility = visibility ==null?new Integer(0):visibility;
	}


	/**
     * Restituisce la posizione della proprieta' all'interno della lista di proprieta' della medesima tipologia.
     * La prima proprieta' ha posizione 0.
     * 
     * @see AnagraficaSupport#getProprietaDellaTipologia(PropertiesDefinition)
     * @return la posizione della proprieta'
     */  
	public int getPositionDef() {
		return positionDef;
	}


    /**
	 * Imposta la posizione della proprieta' all'interno della lista di proprieta' della medesima tipologia<br>
	 * <b>Attenzione</b> l'utilizzo di questo metodo potrebbe introdurre disallineamenti nella lista sopra citata.
	 * Utilizzare solamente per implementare i metodi dell'interfaccia <code>AnagraficaSupport</code>
	 * 
	 * @see AnagraficaSupport, AnagraficaSupport#getProprietaDellaTipologia(PropertiesDefinition)
	 * @param posizione, la nuova posizione della proprieta'
	 */
	public void setPositionDef(int posizione) {
		this.positionDef = posizione;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	/**
	 * Restituisce la tipologia di proprieta' associata.
	 * 
	 * @see PropertiesDefinition 
	 * @return la tipologia di proprieta' associata.
	 */
	@Transient
	public abstract TP getTypo();
	
	/**
	 * Setter per la tipologia di proprieta'
	 * 
	 * @see PropertiesDefinition
	 */
	//FIXME originalmente protetto portato a public per import con spring
	public abstract void setTypo(TP propertyDefinition);
	
	
	//FIXME valutare l'opportunita' di introdurre il pattern decorator per gestire i vari getObject, toString, toHTML, etc.
	/**
	 * Permette di restituire per qualsiasi valore della proprieta il generico
	 * oggetto associato; ad esempio per una Stringa il valore della stringa,
	 * per un puntatore ad un contatto, l'oggetto contatto con id passato nella
	 * variabile valore.
	 */
	@Transient
	public Object getObject() {
		return getValue().getObject();
	}

	/** 
	 *  Restituisce la descrizione esatta del valore associato alla tipologia
	 * 	che viene renderizzato tramite widget; e' il widget che sa come visualizzare
	 *  sulla view il valore della proprieta'. 
	 **/
	@Override
	public String toString() {		
		if (getValue() != null && getValue().getObject() != null) {			
			return getTypo().getRendering().toString(getValue().getObject());
		}
		log.warn("to string di Proprieta' con valore null");
		return super.toString();
	}
	
	
	public String toHTML()
	{
		return getTypo().getRendering().toHTML(getValue().getObject());
	}
	
	public String getHtml(){
		return toHTML();
	}
	
	
	/**
	 * Confronta due proprieta' prima in base alla tipologia e poi, nel caso queste coincidano, in base
	 * alle rispettive posizioni. Se anche la posizione coincide vuol dire che si stanno confrontando proprieta'
	 * di oggetti differenti, a questo punto si guarda l'attributo sortValue del valore.
	 * 
	 * @see PropertiesDefinition#compareTo
	 * @see #getPositionDef()
	 * @param o la proprieta' da comparare
	 * @return come disporre in modo ordinato le due proprieta'
	 */
	public int compareTo(Property<TP> o) {
		
		if(this.getTypo().compareTo(o.getTypo())==0) 
		{
			//compara sulla posizione
			if (this.getPositionDef()>o.getPositionDef()) 
				return 1;
			if (this.getPositionDef()==o.getPositionDef())
			{
				if ((value == null || value.getSortValue() == null)
						&& (o.value == null || o.value.getSortValue() == null))
					return 0;
				if (value == null || value.getSortValue() == null)
					return 1;
				if (o.value == null || o.value.getSortValue() == null)
					return -1;
				return value.getSortValue().compareTo(
						o.getValue().getSortValue());
			}				
			if (this.getPositionDef()<o.getPositionDef())
				return -1;
		}
		
		return getTypo().compareTo(o.getTypo());
		
	}
	
	@Override
	/**
	 * Due proprieta sono equivalenti se hanno lo stesso valore, la stessa posizione, e 
	 * la loro tipologia ha il medesimo shortName. 
	 *
	 **/
	//FIXME aggiungere un check sulla classe a RunTime dell'oggetto, attenzione ai proxy di Hibernate
	public boolean equals(Object object) {
		Property<TP> propConfronto;
		
		if (object != null && object instanceof Property) {
			try {
				propConfronto = (Property<TP>) object;
			} catch (ClassCastException e) {
				log.warn("Attenzione, l'oggetto che hai passato al metodo equals non e' una proprieta", e);
				return false;
			}
		}
		else
			return false;
		
		if ((getTypo().getShortName().equals(propConfronto
				.getTypo().getShortName()))			
			&& (positionDef == propConfronto.getPositionDef()))
		{
			if (value != null && value.equals(propConfronto.value))
				return true;		
			else if (value == null && propConfronto.value == null){
				return true;
			}			
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (getTypo() != null ? getTypo().hashCode() : 0)
				+ (getValue() != null && getValue().getObject() != null ? getValue()
						.getObject().hashCode()
						: 0);
	}

	public AValue getValue() {
		return value;
	}

	public void setValue(AValue valore) {
		this.value = valore;
	}
	
	/**
	 * Return true if property has no value or "empty" value.
	 * @see AWidget#isNull(Object)
	 * @return
	 */
	public boolean hasNullValue() {
		if (value == null) {
			return true;
		} else {
			return getTypo().getRendering().isNull(value.getObject());
		}
	}
	
	public abstract void setParent(AnagraficaSupport<? extends Property<TP>, TP> parent);
	public abstract AnagraficaSupport<? extends Property<TP>, TP> getParent();

    public Integer getLockDef()
    {        
        if(this.lockDef==null) {
            return 0;
        }
        return lockDef;
    }
    
    public void setLockDef(Integer lock)
    {
        this.lockDef = lock;
    }

    public ScopeDefinition getScopeDef()
    {
        return scopeDef;
    }

    public void setScopeDef(ScopeDefinition scopeDef)
    {
        this.scopeDef = scopeDef;
    }


}
