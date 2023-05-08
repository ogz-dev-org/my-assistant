package com.ogz.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "friend-collection")
public class Friends {
    @Id
    private String id;
    private String ownerId;
    private Set<String> friendsIdList;

    public Friends(String ownerId, Set<String> friendsIdList) {
        this.ownerId = ownerId;
        this.friendsIdList = friendsIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Set<String> getFriendsIdList() {
        return friendsIdList;
    }

    public void setFriendsIdList(Set<String> friendsIdList) {
        this.friendsIdList = friendsIdList;
    }
}
