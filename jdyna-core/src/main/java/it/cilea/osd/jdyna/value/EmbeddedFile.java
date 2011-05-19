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
package it.cilea.osd.jdyna.value;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Embeddable
public class EmbeddedFile {

	@Type(type="text")
	private String valueFile;
		
	private String descriptionFile;
		
	private String mimeFile;
	
	private String extFile;
	
	private String folderFile;
	
	private String suffixFile;
	
	@Transient
	private boolean deleteIt;
	
//	@Transient
//	private MultipartFile file;
	
	@Transient 
	private String basePath;
	
	public EmbeddedFile(String basePath) {
		this.basePath = basePath;
	}

	public String getValueFile() {
		return valueFile;
	}

	public void setValueFile(String valueFile) {
		this.valueFile = valueFile;
	}

	public String getDescriptionFile() {
		return descriptionFile;
	}

	public void setDescriptionFile(String descriptionFile) {
		this.descriptionFile = descriptionFile;
	}

	public String getMimeFile() {
		return mimeFile;
	}

	public void setMimeFile(String mimeFile) {
		this.mimeFile = mimeFile;
	}

	public String getExtFile() {
		return extFile;
	}

	public void setExtFile(String extFile) {
		this.extFile = extFile;
	}

	public void setFolderFile(String folderFile) {
		this.folderFile = folderFile;
	}

	public String getFolderFile() {
		return folderFile;
	}

//	public void setFile(MultipartFile file) {
//		this.file = file;
//	}
//
//	public MultipartFile getFile() {
//		return file;
//	}

	public void setDeleteIt(boolean deleteIt) {
		this.deleteIt = deleteIt;
	}

	public boolean isDeleteIt() {
		return deleteIt;
	}

	public void setSuffixFile(String suffixFile) {
		this.suffixFile = suffixFile;
	}

	public String getSuffixFile() {
		return suffixFile;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getBasePath() {
		return basePath;
	}
	
}
