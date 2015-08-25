package webserver.http.response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

public class ResponseHeader {
	
		String completeHeader = null;

		private final String CONTENTTYPE = "Content-Type";
		private final String CONTENTLENGTH = "Content-Length";
		private final String CONNECTION = "Connection";
		private final String LASTMODIFIED = "Last-Modified";
		private final String DATE = "Date";
		
		
		String header = "";
		String contentType,contentLength,lastModified;
		File requestedFile;
		public ResponseHeader(File file)
		{
			requestedFile = file;
			if(file.getName().contains(".css"))
			{
				contentType = "text/css";
			}
			else
				contentType = URLConnection.getFileNameMap().getContentTypeFor(file.getAbsolutePath());
			long lastModifiedLong = file.lastModified();
			Date lastModifiedDate = new Date(lastModifiedLong);
			lastModified = lastModifiedDate.toString();
			
			try {
			
				addHeaderField(CONTENTLENGTH, Integer.toString(getFileByteArray().length));
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			addHeaderField(CONTENTTYPE, contentType);
			addHeaderField(LASTMODIFIED, lastModified);
			
		}
		
		
		public void addHeaderField(String key, String value)
		{
			header+= generateLine(key, value);
		}
		
		public String getCompleteHeader()
		{
			generateCompleteHeader();
			return header;
		}
		
		public void generateCompleteHeader()
		{
			addHeaderField(DATE, Calendar.getInstance().getTime().toString());
			header+="\r\n";
		}
		
		private String generateLine(String key,String value)
		{
			String result = key+": "+value+"\r\n";
			return result;
		}
		
		public void setConnectionType(String value)
		{
			addHeaderField(CONNECTION, value);
		}
		
		public byte[] getFileByteArray() throws IOException
		{
			byte[] byteArray = null;
            FileInputStream fis = null;
			try {
				byteArray = new byte[(int) requestedFile.length()];
				fis = new FileInputStream(requestedFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
	            bis.read(byteArray, 0, byteArray.length);
	            fis.close();
	            bis.close();
	            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				fis.close();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return byteArray;
		}
}
