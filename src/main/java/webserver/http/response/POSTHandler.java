package webserver.http.response;

import java.net.Socket;
import java.util.Map;

public class POSTHandler extends Response {

	
	byte[] requestBody;
	boolean isAFile;
	public POSTHandler(Socket client, Map<String, String> requestLine,Map<String, String> requestHeader, byte[] requestBody,boolean isAFile) 
	{
		super(client, requestLine, requestHeader);
		//System.out.println("Post Method Used\nRequest Line : " + requestLine.toString() + "\nRequest header : "+requestHeader.toString() + "\nRequest  Body : "+requestBody);
		this.requestBody = requestBody;
		this.isAFile = isAFile;
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

	public void sendResponse() {
		// TODO Auto-generated method stub
		
	}

}
