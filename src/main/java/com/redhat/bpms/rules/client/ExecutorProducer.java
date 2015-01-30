package com.redhat.bpms.rules.client;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.jbpm.services.task.identity.DefaultUserInfo;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.task.api.UserInfo;

public class ExecutorProducer {

	@PersistenceUnit(unitName = "org.drools.persistence.jpa")
	private EntityManagerFactory emf;

	@Produces
	@Default
	public EntityManagerFactory getEntityManagerFactory() {
		if (this.emf == null) {
			this.emf = Persistence
					.createEntityManagerFactory("org.drools.persistence.jpa");
		}

		EntityManager em = emf.createEntityManager();

		return emf;
	}

	@Produces
	@Singleton
	@PerProcessInstance
	@PerRequest
	public RuntimeEnvironment produceEnvironment(EntityManagerFactory emf) {
		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory
				.get()
				.newDefaultBuilder()
				.entityManagerFactory(emf)
				.addAsset(
						ResourceFactory
								.newClassPathResource("AsyncWorkflow.bpmn"),
						ResourceType.BPMN2).get();
		return environment;
	}

	@Named("pm")
	@Produces
	public RuntimeManager produceManager() {
		KieServices kieServices = KieServices.Factory.get();
		ReleaseId releaseId = kieServices.newReleaseId("com.redhat.capone.poc",
				"AsyncPOC", "1.0.0");
		KieContainer kContainer = kieServices.newKieContainer(releaseId);

		KieBase kbase = kContainer.getKieBase();

		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory
				.get().newDefaultBuilder().entityManagerFactory(this.emf)
				.knowledgeBase(kbase);

		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(
				builder.get());
	}

	@Produces
	public UserInfo produceUserInfo() {
		// default implementation will load userinfo.properties file on the
		// classpath
		return new DefaultUserInfo(true);
	}

	@Produces
	public UserGroupCallback produceUserGroupCallback() {
		return new ExecutorUserGroupCallback();
	}

}