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
package it.cilea.osd.jdyna.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HashUtil {
	
	/** Logger for this class and subclasses */
    protected static final Log log = LogFactory.getLog(HashUtil.class);
    
	public HashUtil(){
	}
	
	public static String hashMD5(String passw){
		String passwHash="";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passw.getBytes());
			byte[] result = md.digest();
			StringBuffer sb = new StringBuffer();
			  for( int i = 0 ; i < result.length ; i++ ) {
			        String tmpStr = "0"+Integer.toHexString( (0xff & result[i]));
			        sb.append(tmpStr.substring(tmpStr.length()-2));
			        } 
			 passwHash=sb.toString();
		}
		catch(NoSuchAlgorithmException ecc)	{
			log.error("Errore algoritmo " + ecc);
		}
		return passwHash;
	}
}
