package webserver.http.request;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class WorkerThread implements Runnable{
	
	Socket clientSocket;
	RequestHandler requestHandler;
	BlockingQueue<Runnable> taskQueue;
	public WorkerThread(Socket clientSocket,BlockingQueue<Runnable> taskQueue)
	{
		this.taskQueue = taskQueue; 
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		requestHandler = new RequestHandler(clientSocket);
		//requestHandler.respondToRequest();
	}
}
