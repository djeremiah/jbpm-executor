package com.redhat.bpms.async.wih;

import org.kie.api.runtime.process.WorkItem;
import org.kie.internal.executor.api.Command;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;

public class LongRunningProcess implements Command {

	protected ExecutionResults executionResults;

	public ExecutionResults execute(CommandContext commandContext) {

		System.out.println("Command executed on executor with data "
				+ commandContext.getData());
		WorkItem workItem = (WorkItem) commandContext.getData("workItem");
		String callService = (String) workItem.getParameter("callService");

		System.out.println("***************** Begin Calling Service : "
				+ callService);

		executionResults = new ExecutionResults();

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("***************** Completed Calling Service : "
				+ callService);

		return executionResults;
	}
}