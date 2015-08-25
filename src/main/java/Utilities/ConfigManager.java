package Utilities;


/*
 * This class manages all the Configuration Related queries. Writing to config files,
 * reading from them is done using this class.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import webserver.http.Server;

public class ConfigManager {
	
	
	public final String PORT = "PORT";
	public final String COREPOOLSIZE = "COREPOOLSIZE";
	public final String MAXPOOLSIZE = "MAXPOOLSIZE";
	public final String MAXNUMBEROFTASKS = "MAXNUMBEROFTASKS";
	public final String DIRECTORY = "DIRECTORY";
	public final String ERRORDIRECTORY= "ERRORDIRECTORY";
	public final String DEFAULTFILE = "DEFAULTFILE";
	public final String LASTUSED = "LASTUSED";
	public final String configFile = "config"+ File.separator + "server.properties";
	public final String statusRepo = "config"+ File.separator + "status.repo";
	private static Logger log = Logger.getLogger(Server.class.getName());

	
	Properties props;
	OutputStream out = null;
	public ConfigManager()
	{
		props = new Properties();
	}
	
	public void setPortNumber(int port)
	{
		
		props.put(PORT, Integer.toString(port));
		props.put(DIRECTORY, "ht_docs");
		props.put(DEFAULTFILE, "index.html");
		
		try {
			out = new FileOutputStream(configFile);
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			log.error("Config File Not Found!");
			e.printStackTrace();
		}
	}
	
	//In case the server cannot bind to the given port, new port that is used is written into the config file.
	public void setLastUsedPortNumber(int port)
	{
		try {
			out = new FileOutputStream(configFile);
			props.put(LASTUSED, Integer.toString(port));
			props.store(out, null);
		} catch (FileNotFoundException e) {
			log.error("Config File Now Found!");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Now Found!");
			e.printStackTrace();
		}
	}
	
	public int getPortNumber()
	{
		InputStream input;
		try {
			input = new FileInputStream(configFile);
			props.load(input);
		} catch (FileNotFoundException e) {
			log.error("Config File Now Found!");
		} catch (IOException e) {
			log.error("Config File Now Found!");
			e.printStackTrace();
		}
		
		try{
			return (Integer.parseInt((String)props.get(PORT)));
		}catch(Exception e)
		{
			log.debug("Config File Written For the First time");
			setPortNumber(8990);
			return 8990;
		}
	}
	
	
	/*public void writeStatus()
	{
		try{
			
			out = new FileOutputStream(statusRepo);
			props.put("200", "OK");
			props.put("202","Accepted");
			props.put("302", "Found");
			props.put("400", "Bad Request");
			props.put("403", "Forbidden");
			props.put("404", "Not Found");
			props.put("500", "Internal Server Error");
			props.put("501", "Not Implemented");
			
			props.store(out, null);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
	
	
	/*
	 * Get a list of all the status mappings. The code with its meaning.
	 */
	public Map<String,String> getStatusMap()
	{
		Map<String,String> statusMap = new HashMap<String, String>();
		
		try {
			FileInputStream in = new FileInputStream(statusRepo);
			props.load(in);
			Set<Object> keys = props.keySet();
			for (Object key : keys) 
			{
				statusMap.put((String)key, (String)props.get(key));
			}
			in.close();
		} catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		
		return statusMap;
	}
	
	/*
	 * General Get Functions
	 */
	
	
	public String getDefaultDirectory()
	{
		FileInputStream in;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			return props.get(DIRECTORY).toString();
			
		}catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		return "ht_docs";
	}
	
	public String getErrorDirectory()
	{
		FileInputStream in;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			return props.get(ERRORDIRECTORY).toString();
		} catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		return "error";
	}
	
	public String getDefaultFileName()
	{
		FileInputStream in;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			return props.get(DEFAULTFILE).toString();
		} catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		return "index.html";
	}
	public int getCorePoolSize()
	{
		FileInputStream in;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			return Integer.parseInt(props.get(COREPOOLSIZE).toString());
		}catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		return 5;
	}
	public int getMaxPoolSize()
	{
		FileInputStream in;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			return Integer.parseInt(props.get(MAXPOOLSIZE).toString());
		} catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		return 10;
	}
	public int getMaxNumberOfTasks()
	{
		FileInputStream in;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			return Integer.parseInt(props.get(MAXNUMBEROFTASKS).toString());
		} catch (FileNotFoundException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Config File Not Found");
			e.printStackTrace();
		}
		return 50;
	}
}
