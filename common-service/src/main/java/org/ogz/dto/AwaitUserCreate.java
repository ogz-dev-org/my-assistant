package org.ogz.dto;

import java.util.List;

public class AwaitUserCreate {
    private String id;
    private List<String> ids;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public AwaitUserCreate() {
    }

    public AwaitUserCreate(String id, List<String> ids) {
        this.id = id;
        this.ids = ids;
    }
}
