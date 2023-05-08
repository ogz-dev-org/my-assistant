package com.ogz.message.repository;

import com.ogz.message.model.Friends;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FriendsRepository extends MongoRepository<Friends,String> {

    Friends getFriendsByOwnerId(String ownerId);
}
