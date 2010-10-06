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

import java.io.Serializable;


public class DTOGestioneAlberoClassificatore implements Serializable {
	
	//albero	
	/** id dell'albero */
	private Integer id;
	
	/** nome dell'albero */
	private String nome;

	/** descrizione dell'albero */
	private String descrizione;

	//classificazione di primo livello
	 /** nome della top categoria/classificazione */	
	private String topNome;
	
	private boolean codiceSignificativo;

	/** se la top categoria e' selezionabile o meno*/
	private boolean topSelezionabile;
	
	/** se la top categoria e' utilizzabile per nuovi inserimenti/modifiche */
	private boolean topAttivo;
	
	
	/** codice descrittivo della top categoria*/	
	private String topCodice;

	//edit di qualsiasi classificazione
	/** l'id della classificazione da editare */
	private Integer editID;
	
	 /** nome della categoria da editare*/	
	private String editNome;
	
	/** se selezionabile o meno*/
	private boolean editSelezionabile;
	
	/** se la categoria e' utilizzabile per nuovi inserimenti/modifiche */
	private boolean editAttivo;
	
	
	/** codice descrittivo della categoria da editare*/	
	private String editCodice;

	//sotto classificazioni
	 /** nome della sottocategoria da creare*/	
	private String subNome;
	
	/** se la sottocategoria  selezionabile o meno*/
	private boolean subSelezionabile;
	
	/** se la sottocategoria e' utilizzabile per nuovi inserimenti/modifiche */
	private boolean subAttivo;
	
	
	/** codice descrittivo della sottocategoria*/	
	private String subCodice;

	/** per verificare che si vuole creare una sottoclassificazione */
	private boolean createSubClassificazione;
	
	/** indica che l'albero non puo' avere sottoclassificazioni */
	private boolean flat = false;
	
	public String getTopNome() {
		return topNome;
	}

	public void setTopNome(String topNome) {
		this.topNome = topNome;
	}

	public boolean isTopSelezionabile() {
		return topSelezionabile;
	}

	public void setTopSelezionabile(boolean topSelezionabile) {
		this.topSelezionabile = topSelezionabile;
	}

	public boolean isTopAttivo() {
		return topAttivo;
	}

	public void setTopAttivo(boolean topAttivo) {
		this.topAttivo = topAttivo;
	}

	public String getTopCodice() {
		return topCodice;
	}

	public void setTopCodice(String topCodice) {
		this.topCodice = topCodice;
	}

	public Integer getEditID() {
		return editID;
	}

	public void setEditID(Integer editID) {
		this.editID = editID;
	}

	public String getEditNome() {
		return editNome;
	}

	public void setEditNome(String editNome) {
		this.editNome = editNome;
	}

	public boolean isEditSelezionabile() {
		return editSelezionabile;
	}

	public void setEditSelezionabile(boolean editSelezionabile) {
		this.editSelezionabile = editSelezionabile;
	}

	public boolean isEditAttivo() {
		return editAttivo;
	}

	public void setEditAttivo(boolean editAttivo) {
		this.editAttivo = editAttivo;
	}

	public String getEditCodice() {
		return editCodice;
	}

	public void setEditCodice(String editCodice) {
		this.editCodice = editCodice;
	}

	public String getSubNome() {
		return subNome;
	}

	public void setSubNome(String subNome) {
		this.subNome = subNome;
	}

	public boolean isSubSelezionabile() {
		return subSelezionabile;
	}

	public void setSubSelezionabile(boolean subSelezionabile) {
		this.subSelezionabile = subSelezionabile;
	}

	public boolean isSubAttivo() {
		return subAttivo;
	}

	public void setSubAttivo(boolean subAttivo) {
		this.subAttivo = subAttivo;
	}

	public String getSubCodice() {
		return subCodice;
	}

	public void setSubCodice(String subCodice) {
		this.subCodice = subCodice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public boolean isCreateSubClassificazione() {
		return createSubClassificazione;
	}

	public void setCreateSubClassificazione(boolean createSubClassificazione) {
		this.createSubClassificazione = createSubClassificazione;
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
