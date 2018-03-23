package com.physicmaster.modules.discuss.common.infra;

public class TraceTaskScheduler extends WrapTaskScheduler {
	public TraceTaskScheduler(TaskScheduler wrap) {
		super(wrap);
	}

	@Override
	public void reschedule(Task task) {
		trace("reschedule " + task.dump(true));
		
		super.reschedule(task);
	}
	
	private final void trace(String msg) {

	}
}
