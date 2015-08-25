package webserver.http.response;

import java.util.Map;

import webserver.http.Server;
import Utilities.ConfigManager;


public class ResponseLine 
{	
	int status;
	String statusString;
	Map<String,String> statusMap;
	public ResponseLine(int status)
	{
		statusMap = Server.statusMap;
		this.status = status;
		statusMap = new ConfigManager().getStatusMap();
		statusString = statusMap.get(Integer.toString(status));
	}
	
	public String getRequestLine(String version)
	{
		String line = "HTTP/"+version + " " + status + " " + statusString + "\r\n";
		return line;
	}
	
	public void setStatus(int status)
	{
		this.status =  status;
		this.statusString = Server.statusMap.get(this.status);
	}
	
}