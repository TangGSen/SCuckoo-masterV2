package imagepicker.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class ThreadManagerPool {
	    private static final String TAG = "ThreadPool";
	    private static ThreadManagerPool instance;
	    private ThreadPoolExecutor threadPool = null;
	    private static final int CORE_POOL_SIZE = 3;
	    private static final int MAX_POOL_SIZE = 8;
	    private static final int KEEP_ALIVE_TIME = 10; // 10 seconds

	    private ThreadManagerPool() {
	        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME,
	                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
	                new PriorityThreadFactory("mthread-pool",
	                android.os.Process.THREAD_PRIORITY_BACKGROUND),new RejectedExecutionHandler() {
						
						@Override
						public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
							executor.submit(runnable);
							Log.e("sen", "增加任务");
						}
					});
	        
	    }
	    

	    public static synchronized ThreadManagerPool getInstance(){
	        if(instance == null){
	            instance = new ThreadManagerPool();
	        }
	        return instance;
	    }

	    public void removeJob(Runnable task){
	        threadPool.remove(task);
	    }

	    /***
	     * 线程池执行command
	     * @param r
	     */
	    public void submmitJob(Runnable r){
	        threadPool.execute(r);
	    }

}
