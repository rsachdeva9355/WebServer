/**
 * 
 */
package adobe.webserver;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rsachdev
 *
 */
public class ServerTest {

	int port;
	String defaultDirectory;
	ArrayList<String> validURLs;
	String serverUrl = "http://localhost:";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		/*// Create the client here!
		ConfigManager config = new ConfigManager();
		port = config.getPortNumber();
		defaultDirectory = config.getDefaultDirectory();

		validURLs = getAllValidUrls(defaultDirectory);
		*/
	}

	/**
	 * @throws java.lang.Exception
	 */

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void test() {
		/*for (String urls : validURLs) {
			new Thread(new SendRequests(urls)).run();
		}*/
		/*try {
			sendGetRequest("http://localhost:8990/");
		} catch (Exception e) {
		}*/
	}

	/*
	 * public String createClient(String text) { String USER_AGENT =
	 * "Mozilla-5.0"; try { String url = "http://localhost:8888/test/" + text;
	 * URL obj = new URL(url); HttpURLConnection con = (HttpURLConnection)
	 * obj.openConnection();
	 * 
	 * // optional default is GET con.setRequestMethod("GET");
	 * 
	 * // add request header con.setRequestProperty("User-Agent", USER_AGENT);
	 * // con.setRequestProperty("Connection", "keep-alive");
	 * 
	 * BufferedReader in = new BufferedReader(new InputStreamReader(
	 * con.getInputStream())); String inputLine; StringBuilder response = new
	 * StringBuilder();
	 * 
	 * while ((inputLine = in.readLine()) != null) { response.append(inputLine);
	 * } in.close();
	 * 
	 * return response.toString(); } catch (Exception e) {
	 * System.out.println(e.getMessage()); return "Error"; } }
	 */

	//Testing POST
	
	/*public synchronized void sendPostRequest(String url, String filePath)
	{
		
	}
	
	
	
	
	//Testing GET
	public synchronized void sendGetRequest(String url) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();

		try {
			HttpGet httpget = new HttpGet(url);
			//System.out.println(httpget.getRequestLine());
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					// TODO Auto-generated method stub
				//	System.out.println("Running");
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			client.execute(httpget, responseHandler);
			} finally {
			client.close();
		}
	}
	
	
	
	//Testing Other HTTP Methods
	public synchronized void sendOptionsRequest(String url) throws Exception
	{
		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpOptions httpOptions = new HttpOptions(url);
			HttpPut httpPut = new HttpPut(url);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					// TODO Auto-generated method stub
				//	System.out.println("Running");
					int status = response.getStatusLine().getStatusCode();
					System.out.println("Status : "+status);
					if (status == 501) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			client.execute(httpPut,responseHandler);
			client.execute(httpOptions, responseHandler);			
		}
		finally
		{
			client.close();
		}
	}

	
	
	
	
	public ArrayList<String> getAllValidUrls(String rootDir) {
		Stack<String> folders = new Stack<>();
		ArrayList<String> validUrls = new ArrayList<String>();
		String prefix = "http://localhost:8990/";
		String root = (System.getProperty("user.dir").replaceAll("\\\\", "/")
				+ "/" + rootDir + "/");
		// root.replaceAll("\\\\", "/");
		folders.push(System.getProperty("user.dir") + "/" + rootDir);
		while (!folders.isEmpty()) {
			String folder = folders.pop();
			File file = new File(folder);
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (File fl : files) {
					if (fl.isDirectory()) {
						folders.add(fl.getAbsolutePath());
					} else {
						String fName = fl.getPath().replaceAll("\\\\", "/");
						try {
							fName = fName.split(root)[1];
						} catch (ArrayIndexOutOfBoundsException e) {

						}
						
						try {
							fName = prefix + URLEncoder.encode(fName,"UTF-8");
							validUrls.add(fName);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else if (file.isFile()) {
				String fName = file.getPath().replaceAll("\\\\", "/");
				fName = fName.split(root)[1];
				try {
					fName = prefix + URLEncoder.encode(fName,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				validUrls.add(fName);
			}

		}
		return validUrls;
	}

	public class SendRequests implements Runnable {
		String url;

		public SendRequests(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			for (int i = 0; i < 1; i++) {
				try {
					sendGetRequest(url);
					//System.out.println("Hey");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}*/
}