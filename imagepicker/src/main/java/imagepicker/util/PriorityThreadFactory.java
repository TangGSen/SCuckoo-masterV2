package imagepicker.util;

import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory implements ThreadFactory {

	private int threadPriorityBackground;
	private String threadName;

	public PriorityThreadFactory(String threadName, int threadPriorityBackground) {
		this.threadPriorityBackground = threadPriorityBackground;
		this.threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {

		Thread t = new Thread(r);
		t.setPriority(threadPriorityBackground);
		t.setName(threadName);
		return t;
	}

}
