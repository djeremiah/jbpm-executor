package com.redhat.bpms.rules.client;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.jbpm.kie.services.api.IdentityProvider;

@ApplicationScoped
public class CustomIdentityProvider implements IdentityProvider {

    public String getName() {
        return "dummy";
    }

    public List<String> getRoles() {

        return Collections.emptyList();
    }

}