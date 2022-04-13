		class StoppableThread extends Thread
		{
			
			private boolean shouldStop = false;
			
			/**
			 * 
			 */
			public StoppableThread() { super(); }
			
			/**
			 * @param arg0
			 */
			public StoppableThread(Runnable arg0) { super(arg0); }
			
			/**
			 * @param arg0
			 * @param arg1
			 */
			public StoppableThread(Runnable arg0, String arg1) { super(arg0, arg1); }
			
			/**
			 * @param arg0
			 */
			public StoppableThread(String arg0) { super(arg0); }
			
			/**
			 * @param arg0
			 * @param arg1
			 */
			public StoppableThread(ThreadGroup arg0, Runnable arg1) { super(arg0, arg1); }
			
			/**
			 * @param arg0
			 * @param arg1
			 * @param arg2
			 */
			public StoppableThread(ThreadGroup arg0, Runnable arg1, String arg2) { super(arg0, arg1, arg2); }
			
			/**
			 * @param arg0
			 * @param arg1
			 * @param arg2
			 * @param arg3
			 */
			public StoppableThread(ThreadGroup arg0, Runnable arg1, String arg2, long arg3) { super(arg0, arg1, arg2, arg3); }
			
			/**
			 * @param arg0
			 * @param arg1
			 */
			public StoppableThread(ThreadGroup arg0, String arg1) { super(arg0, arg1); }
			
			public void stopThread() { shouldStop = true; }
			
			public boolean shouldStop() { return shouldStop; }
			
		}
