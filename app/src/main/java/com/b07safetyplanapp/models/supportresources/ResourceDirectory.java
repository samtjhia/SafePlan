package com.b07safetyplanapp.models.supportresources;

import java.util.List;
import java.util.Map;

public class ResourceDirectory {

    private Map<String, List<SupportResource>> resourceMap;

    public ResourceDirectory() {}

    public Map<String, List<SupportResource>> getResourceMap() {
        return resourceMap;
    }

}
