package webserver.http.parser;
/*
 * Gets the request string and extracts all the data required to actually interpret
 * and then serve the request
 * Created maps (key,value pairs) for every part of the request header so that 
 * they can be accessed easily
 */
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import webserver.http.Server;

public class Parser {
	
	public static String REQUEST_METHOD = "REQUESTMETHOD";
	public static String REQUESTED_RESOURCE = "REQUESTEDRESOURCE";
	public static String HTTPVERSION = "HTTPVERSION";
	private static Logger log = Logger.getLogger(Server.class.getName());

	String request;
	public Parser(String request)
	{
		this.request = request;
	}
	
	//Parsing the Request Line
	
	public Map<String, String> getRequestLineMap() throws Exception
	{
		Map<String, String> requestLineMap = new HashMap<String, String>();
		String requestLine = getRequestLine();
		log.debug(requestLine);
		if(requestLine.equals(null))
		{
			//TODO Send error message
		}
		try{
			String requestLineArray[] = requestLine.split(" ");
			requestLineMap.put(REQUEST_METHOD , requestLineArray[0]);
			requestLineMap.put(REQUESTED_RESOURCE, URLDecoder.decode(requestLineArray[1],"UTF-8"));
			requestLineMap.put(HTTPVERSION, requestLineArray[2].split("/")[1]);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return requestLineMap;
	}
	
	private String getRequestLine()
	{
		try{
			return request.split("\n")[0];
		}catch(Exception e)
		{
			log.error("INVALID REQUEST!");
		}
		return null;
	}
	
	
	
	//Parsing the Request Header
	
	public Map<String,String> getRequestHeaderMap()
	{
		Map<String, String> requestHeaderMap = new HashMap<String,String>();
		String requestHeader = getRequestHeader();
		if(requestHeader.equals(null))
		{
			System.out.println("The Request Header Was Empty!");
			return null;
		}
		else
		{
			requestHeaderMap = getMapFromString(requestHeader);
			return requestHeaderMap;
		}
		
	}
	
	
	private String getRequestHeader()
	{
		String lineAndHeader = "";
		String[] header = null;
		try{
			lineAndHeader = request.split("\n\n")[0];
			header = lineAndHeader.split("\n",2);
			return header[1];
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private Map<String,String> getMapFromString(String header)
	{
		Map<String,String> headerMap = new HashMap<String,String>();
		String separatedLines[] = header.split("\n");
		for(int i=0;i<separatedLines.length-1;i++)
		{
			if(separatedLines[i].equals("\r\n\r\n")||separatedLines[i].equals("\n")||separatedLines[i].equals(""))
				break;
			String splitOnColon[] = separatedLines[i].split(": ");
			headerMap.put(splitOnColon[0], splitOnColon[1]);
		}
		
		return headerMap;
	}
	
	
	//Parsing the request body
	
	public String getRequestBody()
	{
		String body = null;
		try{
			body = request.split("\n\n")[1];
		}catch(IndexOutOfBoundsException e)
		{
			System.out.println("**No Body Found In This Request");
			e.printStackTrace();
		}
		return body;
	}
}
