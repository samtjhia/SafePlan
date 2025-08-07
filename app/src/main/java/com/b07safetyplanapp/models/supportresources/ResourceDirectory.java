package com.b07safetyplanapp.models.supportresources;

import java.util.List;
import java.util.Map;

/**
 * Represents a directory of categorized support resources.
 * Each category (e.g., "mental_health", "housing") maps to a list of support resources.
 */
public class ResourceDirectory {

    /**
     * A map where the key is a category name and the value is a list of support resources under that category.
     * Example: "mental_health" → [SupportResource1, SupportResource2, ...]
     */
    private Map<String, List<SupportResource>> resourceMap;

    /**
     * Default constructor required for deserialization.
     */
    public ResourceDirectory() {}

    /**
     * Returns the map of support resources categorized by topic.
     *
     * @return a map where the key is a category name and the value is a list of support resources
     */
    public Map<String, List<SupportResource>> getResourceMap() {
        return resourceMap;
    }
}
