package webserver.http;
/*
 * The class where the server as well the Thread Pool are started.
 */
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import webserver.http.request.ThreadPool;
import webserver.http.request.WorkerThread;
import Utilities.ConfigManager;

public class Server {

	private static Logger log = Logger.getLogger(Server.class.getName());
	int port = 8080;
	public static Map<String, String> statusMap;
	Socket clientSocket;
	ServerSocket serverSocket;
	PrintWriter out;
	BlockingQueue<Runnable> taskQueue;
	String request = "";
	ThreadPool threadPool;
	int corePoolSize, maxPoolSize, maxNumberOfTasks;
	long keepAliveTime;
	ConfigManager configManager;
	int requestNumber = 0;
	/*
	 * Creates a server object. A thread pool is created with a maximum limit specified within the config file which can be edited by the user
	 * according to his needs. Thread pool waits on the Blocking Queue. Whenever a thread is free. It picks up a request from the Blocking Queue
	 * and serves it.
	 */
	public Server(int port) 
	{
		ConfigManager configManager = new ConfigManager();
		statusMap = configManager.getStatusMap();
		this.port = port;
		configManager = new ConfigManager();
		corePoolSize = configManager.getCorePoolSize();
		maxPoolSize = configManager.getMaxPoolSize();
		maxNumberOfTasks = configManager.getMaxNumberOfTasks();
		taskQueue = new LinkedBlockingDeque<Runnable>(maxNumberOfTasks);
		threadPool = new ThreadPool(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, taskQueue);
		Runtime.getRuntime().addShutdownHook(new Thread(new GracefulServerShutdown()));	
		}
	
	/*
	 * 
	 */
	public int startListening() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started at Port : " + port);
			System.out.println("Please open http://localhost:"+port +" on your browser");
			log.debug("Server started at port" + port);
			while (true) 
			{
				//Blocking call
				clientSocket = serverSocket.accept();
				
				clientSocket.setKeepAlive(true);
				//Book-keeping purposes
				requestNumber++;
				
				//Creating a thread and inserting it into the Blocking Queue.
				threadPool.execute(new WorkerThread(clientSocket,taskQueue));
			}
		} catch (BindException e) {
			log.error("Could not bind to port number" + port);
			return -1;
		}
		catch(Exception e1)
		{
			log.error(e1.getMessage());
			return 0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		//Exiting the program closes the socket
		super.finalize();
		log.debug("Closing the socket now");
		serverSocket.close();
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void stop()
	{
		new Thread(new GracefulServerShutdown()).run();
		System.exit(1);
	}
	/*
	 * 
	 */
	class GracefulServerShutdown implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Closing server now!");
			try {
				threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
				threadPool.shutdownNow();
			}
		}
		
	}
}	