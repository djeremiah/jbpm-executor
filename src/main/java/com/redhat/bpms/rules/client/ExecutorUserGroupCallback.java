package com.redhat.bpms.rules.client;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Alternative;

import org.kie.api.task.UserGroupCallback;

@Alternative
public class ExecutorUserGroupCallback implements UserGroupCallback {

    public boolean existsUser(String userId) {
        return userId.equals("dtucker") || userId.equals("Administrator");
    }

    public boolean existsGroup(String groupId) {
        return groupId.equals("Admin");
    }

    public List<String> getGroupsForUser(String userId,
            List<String> groupIds, List<String> allExistingGroupIds) {
        List<String> groups = new ArrayList<String>();
        if (userId.equals("dtucker"))
            groups.add("Admin");
        return groups;
    }
}