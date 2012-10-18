package it.cilea.osd.jdyna.util;

import it.cilea.osd.jdyna.web.Tab;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface TabUtils
{
    
    public void loadTabIcon(Tab tab, String iconName,
            MultipartFile itemImage) throws IOException, FileNotFoundException;
    
    public void removeTabIcon(Tab tab);

}
