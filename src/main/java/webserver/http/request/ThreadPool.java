package webserver.http.request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool extends ThreadPoolExecutor{
	
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> queue)
	{
		super(corePoolSize,maxPoolSize,keepAliveTime,unit,queue);	
	}
	
}
