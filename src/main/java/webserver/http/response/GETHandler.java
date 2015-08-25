package webserver.http.response;
/*
 * This class manages the response to be sent to the client for a GET request.
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.log4j.Logger;

import webserver.http.Server;
import webserver.http.parser.Parser;
import Utilities.ConfigManager;
import Utilities.FileManager;

public class GETHandler extends Response {

	ResponseHeader headerManager;
	FileManager fileManager;
	String defaultFileName;
	Socket clientSocket;
	private static Logger log = Logger.getLogger(Server.class.getName());

	
	public GETHandler(Socket client, Map<String, String> requestLine,
			Map<String, String> requestHeader, String requestBody) {
		super(client, requestLine, requestHeader);
		clientSocket = client;
		fileManager = new FileManager();
		defaultFileName = new ConfigManager().getDefaultFileName();
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public String setHeader() {
		return null;
	}

	@Override
	public void getResponse() {

	}

	@Override
	public void setResponse() {

	}

	
	//Function which is called externally that invokes the response sending
	//ie. Looking for the file, creating the header, sending both the header as well as the file as a response.
	public int sendResponse() {
		// Find out the version of HTTP
		String fileRequested = requestLine.get(Parser.REQUESTED_RESOURCE);
		log.info("File requested is : "+fileRequested);
		try 
		{
			fileRequested = URLDecoder.decode(fileRequested, "UTF-8");
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileRequested.equals("/") || fileRequested.equals("\\")) {
			fileRequested += defaultFileName;
		}
		
		File requestedFile = fileManager.getFile(fileRequested);
		
		if (!requestedFile.exists()||!requestedFile.isFile()) 
		{	
			log.error("File does not exist. Sending 404");
			sendResponseToServer(404 , requestLine.get(Parser.HTTPVERSION), 1);	
		}
		else if(!requestedFile.canRead())
		{
			log.error("File is private. Sending 403");
			sendResponseToServer(403, requestLine.get(Parser.HTTPVERSION), 1);
		}
		else 
		{
			log.error("Sending the response");
			return sendResponseToServer(200, requestLine.get(Parser.HTTPVERSION), 0);
		}
		
		return 0;

	}
	
	public int sendResponseToServer(int status, String version, int error)
	{
		ResponseLine responseLine = new ResponseLine(status);
		String line = responseLine.getRequestLine(version);
		String fileRequested = "";
		File requestedFile = null;
		if(error==1)
		{
			fileRequested = Integer.toString(status)+".html";
			try 
			{
				fileRequested = URLDecoder.decode(fileRequested, "UTF-8");
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (fileRequested.equals("/") || fileRequested.equals("\\")) {
				fileRequested += defaultFileName;
			}
			
			requestedFile = fileManager.getErrorFile(fileRequested);
		}
		else
		{
			fileRequested = requestLine.get(Parser.REQUESTED_RESOURCE);
			try 
			{
				fileRequested = URLDecoder.decode(fileRequested, "UTF-8");
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (fileRequested.equals("/") || fileRequested.equals("\\")) {
				fileRequested += defaultFileName;
			}
			
			requestedFile = fileManager.getFile(fileRequested);
			
			if (!requestedFile.exists()||!requestedFile.isFile()) 
			{	
				sendResponseToServer(404 , requestLine.get(Parser.HTTPVERSION), 1);	
			}
			else if(!requestedFile.canRead())
			{
				sendResponseToServer(403, requestLine.get(Parser.HTTPVERSION), 1);
			}
		}
			
		ResponseHeader header = new ResponseHeader(requestedFile);
		//headerManager.setConnectionType(requestHeader.get("Connection").toString());
		String responseHeader = header.getCompleteHeader();
		try{
			OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
			out.write(line.getBytes("ASCII"));
			out.write(responseHeader.getBytes("ASCII"));
			out.write(header.getFileByteArray());
			out.flush();
			out.close();
		try
		{
			if(requestHeader.get("Connection").equals("keep-alive"))
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}catch(NullPointerException e)
		{
			log.error("No Connection type found");
			return 1;
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

}
