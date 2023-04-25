package com.ogz.user.repository;


import org.ogz.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'googleID' : ?0 }")
    User findUserByGoogleID(String googleID);
    List<User> findAllByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(String name, String surname);

    @Query("{'emailAddresses.Google' : {$exists: true} }")
    User findUserByEmail(HashMap<String, String> emailAddresses);

}
