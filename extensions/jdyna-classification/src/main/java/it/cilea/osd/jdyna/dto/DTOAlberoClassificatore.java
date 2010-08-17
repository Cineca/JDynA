package it.cilea.osd.jdyna.dto;

import java.io.Serializable;


public class DTOAlberoClassificatore implements Serializable {
	
	private String nome;

	private String descrizione;

	private boolean attiva;
	
	private boolean flat;

	private boolean codiceSignificativo;
	
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

	public boolean isAttiva() {
		return attiva;
	}

	public void setAttiva(boolean attiva) {
		this.attiva = attiva;
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
