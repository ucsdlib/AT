/*
 * The contents of this file, as updated from time to time by the OCLC Office
 * of Research, are subject to the OCLC Office of Research Public License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a current copy of the License at
 * http://alcme.oclc.org:4342/license.html.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * This software consists of voluntary contributions made by many individuals
 * on behalf of the OCLC Office of Research. For more information on the OCLC
 * Office of Research, please see http://www.oclc.org/oclc/research/.
 *
 * This is Original Code.
 *
 * The Initial Developer(s) of the Original Code is (are):
 *  - Ralph LeVan  <levan@oclc.org>
 *
 * Portions created by OCLC are Copyright (C) 2001.
 *
 */

package org.archiviststoolkit.importer;

import java.io.*;
import java.lang.System;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


public class HandleInputFile  {
	
    /**
	 *  Input Stream to read records from.
	 */
    private InputStream input;
	
	
    /**
	 *  Name of the input file
	 */
    private String inputFileName;
	
    private byte[]  buffer, recordTagBytes=null;
    private int     datalen=0, maxlen=10000,
		numBytesUsed=0, offset=0, recordTagLength, numRecordsRead=0;
    private long    fileLength=0, numBytesRead=0;
	
	/**
	 * Sets Datalen
	 *
	 * @param    Datalen             an int
	 */
	public void setDatalen(int datalen) {
		this.datalen = datalen;
	}
	
	/**
	 * Returns Datalen
	 *
	 * @return    an int
	 */
	public int getDatalen() {
		return datalen;
	}
	
	/**
	 * Sets Maxlen
	 *
	 * @param    Maxlen              an int
	 */
	public void setMaxlen(int maxlen) {
		this.maxlen = maxlen;
	}
	
	/**
	 * Returns Maxlen
	 *
	 * @return    an int
	 */
	public int getMaxlen() {
		return maxlen;
	}
	
	/**
	 * Sets NumBytesUsed
	 *
	 * @param    NumBytesUsed        an int
	 */
	public void setNumBytesUsed(int numBytesUsed) {
		this.numBytesUsed = numBytesUsed;
	}
	
	/**
	 * Returns NumBytesUsed
	 *
	 * @return    an int
	 */
	public int getNumBytesUsed() {
		return numBytesUsed;
	}
	
	/**
	 * Sets Offset
	 *
	 * @param    Offset              an int
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * Returns Offset
	 *
	 * @return    an int
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * Sets RecordTagLength
	 *
	 * @param    RecordTagLength     an int
	 */
	public void setRecordTagLength(int recordTagLength) {
		this.recordTagLength = recordTagLength;
	}
	
	/**
	 * Returns RecordTagLength
	 *
	 * @return    an int
	 */
	public int getRecordTagLength() {
		return recordTagLength;
	}
	
	/**
	 * Sets NumRecordsRead
	 *
	 * @param    NumRecordsRead      an int
	 */
	public void setNumRecordsRead(int numRecordsRead) {
		this.numRecordsRead = numRecordsRead;
	}
	
	/**
	 * Returns NumRecordsRead
	 *
	 * @return    an int
	 */
	public int getNumRecordsRead() {
		return numRecordsRead;
	}
	
	
	
    private int FindEndRecTag(int offset) {
		int t=offset-1;
		while( (t=strbyte(buffer, t+1, datalen, '<')) >= 0 )
			if(buffer[t+1]=='/')
				if(strnicmp(buffer, t+2, recordTagBytes, 0, recordTagLength)==0 &&
					   buffer[t+recordTagLength+2]=='>') {
					return t+recordTagLength+3;
				}
		
		return -1;
    }
	
	
    private int FindRecTag(int offset) {
		int t=offset-1;
		while( (t=strbyte(buffer, t+1, datalen, '<')) >= 0 )
			if(strnicmp(buffer, t+1, recordTagBytes, 0, recordTagLength)==0 &&
				   (buffer[t+recordTagLength+1]=='>' || buffer[t+recordTagLength+1]==' ')) {
				return t;
			}
		
		return -1;
    }
	
	
    private byte[] getRecord() throws IOException, EOFException {
		int endOffset, len, taglen;
		
		
		if (offset > datalen) { // buffer completely emptied, refill
			datalen=input.read(buffer, 0, 4096);  /* fill in buffer */
		} else if(datalen>0) { // move remainder of buffer to the front
			datalen-=offset;
			System.arraycopy(buffer, offset, buffer, 0, datalen);
		}
		
		/* is the begin tag in the buffer? */
		taglen=0;
		while( (offset=FindRecTag(0))<0 ) {
			len=datalen;
			if(len>20)
				len=20;
			if(datalen>20)         /* move last 20 bytes to front */
				System.arraycopy(buffer, datalen-20, buffer, 0, 20);
			datalen=len+input.read(buffer, len, 4096);  /* fill in buffer */
			if(datalen<=len) {  /* didn't read anything; normal end of file */
				return null;
			}
		}
		
		/* move data to beginning of buffer */
		datalen-=offset;
		System.arraycopy(buffer, offset, buffer, 0, datalen);
		
		
		/* ok, at this point, we've found a begin tag, and moved the data
		 following it to the beginning of the record. Now to find the
		 end of the record */
		offset=2;
		while( (endOffset=FindEndRecTag(offset)) < 0) {
			/* reduce the number of bytes to search next time... going back */
			/* 20 bytes should eliminate missing tag overlaps!!! */
			if (datalen > 20)
				offset = datalen - 20;
			
			if(datalen+4096>maxlen) {
				maxlen+=10000;
				byte[] newrec=new byte[maxlen];
				System.arraycopy(buffer, 0, newrec, 0, datalen);
				buffer=newrec;
			}
			
			len=input.read(buffer, datalen, 4096);
			if(len<=0)
				throw(new EOFException("Missing end tag"));
			datalen+=len;
		}
		
		offset=endOffset;
		byte[] theRec=new byte[offset];
		System.arraycopy(buffer, 0, theRec, 0, offset);
		numBytesUsed+=offset;
		return theRec;
    }
    /**
	 * Initializes HandlerObject.
	 *
	 * @param inputFileName the file to read from,<br>
	 * @param tagsFileName the tagsFileName
	 *
	 * @throws IOException if error loading or parsing Tags file.
	 *
	 * @since 1.0
	 */
    public void initializeInput(String inputFileName)
		throws IOException {
		
		numBytesRead=0;
		this.inputFileName=inputFileName;
		try {
			if(inputFileName!=null) {
				File f = new File(inputFileName);
				fileLength=f.length();
				input=null;
				initializeInput(new FileInputStream(inputFileName));
			}
			else
				initializeInput((FileInputStream)null);
		}
		catch (IOException e){
			throw e;
		}
    }
	
	
    /**
	 * Initializes HandlerObject.
	 *
	 * @param  inputStream the file to read from,<br>
	 * @param  tagsFileName the tagsFileName
	 *
	 * @throws IOException if error loading or parsing Tags file.
	 *
	 * @since 1.0
	 */
    public void initializeInput(InputStream inputStream) {
		numBytesRead=0;
		this.inputFileName=inputFileName;
		if(inputStream!=null && input==null) {
			input = new BufferedInputStream(inputStream);
			buffer=new byte[maxlen];
		}
		
		if(recordTagBytes==null) {
			String rectag="reprec";
			recordTagBytes=rectag.getBytes();
			recordTagLength=recordTagBytes.length;
		}
    }
	
    /**
	 * Loads a record from an input stream into a byte array.
	 *
	 * @return a null at normal end-of-file
	 *
	 * @throws EOFException  if abnormal end of file .
	 *
	 * @since 1.0
	 */
    public byte[] loadRecord(String recordTag) throws IOException {
		byte[] record=null;
		recordTagBytes=recordTag.getBytes();
		recordTagLength=recordTagBytes.length;
		
		record=getRecord();
		if(record==null)
			return null;
		numRecordsRead++;
		numBytesRead+=record.length;
		
		return record;
    }
    
	
    public float percentDone() {
		if(fileLength>0)
			return ((numBytesRead*100F)/fileLength);
		return numBytesRead;
    }
	
	
    private int strbyte(byte[] bytes, int offset, int endOffset, char b) {
		int i;
		for(i=offset; i<endOffset; i++) {
			if(bytes[i]==(byte)b) {
				return i;
			}
		}
		return -1;
    }
	
	
    private int strnicmp(byte[] a, int aOffset, byte[] b, int bOffset,
						 int len) {
		int i, val;
		for(i=0; i<len; i++) {
			if( (val = tolower(a[aOffset++])-tolower(b[bOffset++])) != 0)
				return val;
		}
		return 0;
    }
	
	
    private static final byte tolower(byte b) {
		if(b>0x40 && b<0x5b)
			return (byte)(b+0x20);
		return b;
    }
	
	
    public boolean makesRecords() {
		return true;
    }
	
	
    /**
	 * returns the number of record(s) and bytes converted to(importing) or
	 * From(exporting) a DataDir object.
	 *
	 * @return a String message.
	 *
	 * @since 1.0
	 */
    public String reportStats() {
		StringBuffer sb=new StringBuffer();
		sb.append(numRecordsRead)
			.append(" VIA records read with a total of ")
			.append(numBytesUsed).append(" bytes of data\n");
		if(inputFileName!=null)
			sb.append("From file '").append(inputFileName)
				.append("' which was ").append(numBytesRead)
				.append(" bytes long.\n");
		return sb.toString();
    }
}


