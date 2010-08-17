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
