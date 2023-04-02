package com.ogz.user.repository;

import com.ogz.user.model.Friends;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FriendsRepository extends MongoRepository<Friends,String> {

    Friends getFriendsByOwnerId(String ownerId);
}
