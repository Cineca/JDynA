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
package it.cilea.osd.jdyna.dto;

import it.cilea.osd.jdyna.model.Classificazione;

import java.io.Serializable;

import org.directwebremoting.annotations.RemoteProperty;


public class DTOClassificazione implements Serializable {
		
	 /** nome della categoria */	
	private String nome;
	
	/** padre della sottoarea */	
 	private Classificazione padre;
	
	@RemoteProperty
	/** se selezionabile o meno*/
	private boolean selezionabile;
	
	/** se la categoria è utilizzabile per nuovi inserimenti/modifiche */
	private boolean attivo;
	
	
	/** codice descrittivo della categoria*/	
	private String codice;


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
			
}
