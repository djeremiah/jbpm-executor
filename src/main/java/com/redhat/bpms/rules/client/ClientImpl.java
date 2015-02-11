package com.redhat.bpms.rules.client;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jbpm.executor.ExecutorServiceFactory;
import org.jbpm.executor.impl.wih.AsyncWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.executor.api.ExecutorService;

@Path("/service")
public class ClientImpl {

	/**
	 * These values should be injected.
	 */
	@Inject
	private EntityManagerFactory emf;
	
	@Inject @Named("rm")
	private RuntimeManager rm;

	public ClientImpl() {
	}

	@GET
	@Path("/execute")
	public void executeProcess() {
		RuntimeEngine engine = rm.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();

		ExecutorService executorService = ExecutorServiceFactory
				.newExecutorService(this.emf);
		ksession.getWorkItemManager().registerWorkItemHandler("CallServiceWIH",
				new AsyncWorkItemHandler(executorService));
		executorService.init();

		ksession.startProcess("com.redhat.executor.poc.AsyncWorkflow");

	}
}
