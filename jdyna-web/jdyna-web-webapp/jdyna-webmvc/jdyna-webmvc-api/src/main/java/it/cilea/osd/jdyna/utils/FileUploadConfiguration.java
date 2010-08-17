package it.cilea.osd.jdyna.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadConfiguration {
	private MultipartFile file;
	public void setFile(MultipartFile file) {
	this.file = file;
	}
	public MultipartFile getFile() {
	return file;
	}
}
