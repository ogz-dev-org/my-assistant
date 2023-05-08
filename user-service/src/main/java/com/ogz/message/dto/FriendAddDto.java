package com.ogz.message.dto;

public class FriendAddDto {
    private String friendsId;

    public FriendAddDto(String friendsId) {
        this.friendsId = friendsId;
    }

    public String getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(String friendsId) {
        this.friendsId = friendsId;
    }
}
