package space.arim.api.env.concurrent;

class RunnableExceptionReporter implements Runnable {

	private final Runnable command;

	RunnableExceptionReporter(Runnable command) {
		this.command = command;
	}

	@Override
	public void run() {
		try {
			command.run();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		}
	}
}
