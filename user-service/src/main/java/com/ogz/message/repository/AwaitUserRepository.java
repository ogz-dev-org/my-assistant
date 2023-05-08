package com.ogz.message.repository;

import org.ogz.model.AwaitUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AwaitUserRepository extends MongoRepository<AwaitUser, String> {

    AwaitUser findFirstByCreateDate();

}
