package webserver.http.response;

import java.net.Socket;
import java.util.Map;

public abstract class Response {

	Map<String,String> requestLine, requestHeader;
	String requestBody;
	
	public Response(Socket client,Map<String,String> requestLine, Map<String,String> requestHeader)
	{
		this.requestLine = requestLine;
		this.requestHeader = requestHeader;	
	}
	
	public abstract String getHeader();
	
	public abstract String setHeader();
	
	public abstract void getResponse();
	
	public abstract void setResponse();

}
