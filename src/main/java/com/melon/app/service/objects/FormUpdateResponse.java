package com.melon.app.service.objects;

import java.util.List;

/**
 * Defines a response object containing form data.
 * Used in determining the state of a form
 * (like if it has been updated since the last poll)
 */
public class FormUpdateResponse {
    private int currentResponseCount;
    private boolean hasNewResponses;
    private List<List<Object>> newResponses;

    public FormUpdateResponse(int currentResponseCount, boolean hasNewResponses, List<List<Object>> newResponses) {
        this.currentResponseCount = currentResponseCount;
        this.hasNewResponses = hasNewResponses;
        this.newResponses = newResponses;
    }

    public int getCurrentResponseCount() { return currentResponseCount; }
    public boolean isHasNewResponses() { return hasNewResponses; }
    public List<List<Object>> getNewResponses() { return newResponses; }

    public void setCurrentResponseCount(int currentResponseCount) { this.currentResponseCount = currentResponseCount; }
    public void setHasNewResponses(boolean hasNewResponses) { this.hasNewResponses = hasNewResponses; }
    public void setNewResponses(List<List<Object>> newResponses) { this.newResponses = newResponses; }
}
