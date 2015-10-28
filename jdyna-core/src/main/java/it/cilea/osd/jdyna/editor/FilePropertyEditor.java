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
package it.cilea.osd.jdyna.editor;

import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.value.EmbeddedFile;
import it.cilea.osd.jdyna.widget.WidgetFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public class FilePropertyEditor<WF extends WidgetFile>
        extends AdvancedPropertyEditorSupport
{

    /** The logger */
    private final static Log log = LogFactory.getLog(FilePropertyEditor.class);

    private WF widgetFile;

    private String externalAuthority;

    private String internalAuthority;

    public FilePropertyEditor(WF widgetFile)
    {
        this.widgetFile = widgetFile;
    }

    public FilePropertyEditor(WF widgetFile, String service)
    {
        this(widgetFile);
        setMode(service);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        log.debug("FilePropertyEditor - setAsText: " + text);
        // text: 'description|||link'
        if (StringUtils.isEmpty(text))
        {
            setValue(null);
        }
        else
        {
            String[] splitted = text.split("\\|\\|\\|");
            if (splitted.length < 7)
            {
                setValue(null);
                log.error("Invalid text string for file: " + text);
                return;
            }
            EmbeddedFile file = new EmbeddedFile();

            file.setValueFile(splitted.length > 3 ? splitted[3] : null);
            file.setExtFile(splitted.length > 4 ? splitted[4] : null);
            file.setMimeFile(splitted.length > 5 ? splitted[5] : null);
            file.setFolderFile(widgetFile.getCustomFolderByAuthority(
                    getExternalAuthority(), getInternalAuthority()));
            if (splitted.length == 8 && Boolean.parseBoolean(splitted[7]))
            {
                widgetFile.remove(file);
                setValue(null);
            }
            else
            {
                setValue(file);
            }
        }
    }

    @Override
    public String getAsText()
    {
        log.debug("send call to FileConverter - getAsText");

        EmbeddedFile valore = (EmbeddedFile) getValue();

        if (valore == null)
            return "";
        log.debug(
                "isDeletable|||basePath|||servletPath|||valueFile|||ext|||mime|||folderFile");
        log.debug("isDeletable = " + valore.getValueFile() != null ? true
                : false);
        log.debug("basePath = " + widgetFile.getBasePath());
        log.debug("servletPath = " + widgetFile.getServletPath());
        log.debug("valueFile = " + valore.getValueFile());
        log.debug("ext = " + valore.getExtFile());
        log.debug("mime = " + valore.getMimeFile());
        log.debug("folderFile = " + valore.getFolderFile());
        if (MODE_CSV.equals(getMode()))
        {
            return valore == null ? ""
                    : "[LOCAL="
                            + (valore.getValueFile() != null ? "true" : "false")
                            + "]" + widgetFile.getBasePath() + "/"
                            + valore.getFolderFile() + "/"
                            + valore.getValueFile()
                            + (StringUtils.isNotBlank(valore.getExtFile())
                                    ? "." + valore.getExtFile() : "");
        }
        return valore == null ? "false|||"
                : (valore.getValueFile() != null ? true : false) + "|||"
                        + widgetFile.getBasePath() + "|||"
                        + widgetFile.getServletPath() + "|||"
                        + (valore.getValueFile() != null ? valore.getValueFile()
                                : "")
                        + "|||"
                        + (valore.getExtFile() != null ? valore.getExtFile()
                                : "")
                        + "|||"
                        + (valore.getMimeFile() != null ? valore.getMimeFile()
                                : "")
                        + "|||" + (valore.getFolderFile() != null
                                ? valore.getFolderFile() : "");
    }

    @Override
    public void setValue(Object value)
    {
        if (value instanceof MultipartFile)
        {
            MultipartFile multipart = (MultipartFile) value;
            if (multipart.getSize() > 0)
            {
                try
                {
                    EmbeddedFile file = widgetFile.load(
                            multipart.getInputStream(),
                            multipart.getOriginalFilename(),
                            multipart.getContentType(), getExternalAuthority(),
                            getInternalAuthority());
                    super.setValue(new ValoreDTO(file));
                }
                catch (IOException ex)
                {
                    log.warn("Cannot read contents of multipart file", ex);
                    throw new IllegalArgumentException(
                            "Cannot read contents of multipart file: "
                                    + ex.getMessage());

                }
            }
            else
            {
                setValue(null);
            }
        }
        else if (value instanceof File)
        {
            File filedisk = (File) value;
            if (filedisk.exists())
            {
                try
                {
                    FileInputStream fileInputStream = new FileInputStream(
                            filedisk);
                    String mime = new MimetypesFileTypeMap()
                            .getContentType(filedisk);
                    EmbeddedFile file = widgetFile.load(fileInputStream,
                            filedisk.getName(), mime, getExternalAuthority(),
                            getInternalAuthority());
                    super.setValue(file);
                }
                catch (IOException ex)
                {
                    log.warn("Cannot read contents of file", ex);
                    throw new IllegalArgumentException(
                            "Cannot read contents of file: " + ex.getMessage());

                }
            }
            else
            {
                setValue(null);
            }
        }
        else
        {
            super.setValue(value);
        }
    }

    public void setExternalAuthority(String externalAuthority)
    {
        this.externalAuthority = externalAuthority;
    }

    public String getExternalAuthority()
    {
        return externalAuthority;
    }

    public void setInternalAuthority(String internalAuthority)
    {
        this.internalAuthority = internalAuthority;
    }

    public String getInternalAuthority()
    {
        return internalAuthority;
    }

}
