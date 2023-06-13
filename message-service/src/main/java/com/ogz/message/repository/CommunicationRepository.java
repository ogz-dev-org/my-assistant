package com.ogz.message.repository;

import com.ogz.message.model.Communication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends MongoRepository<Communication,String> {

    @Query("{ 'userIds' : { $elemMatch: { $eq: ?0 } } }")
    List<Communication> findByListContainingElement(String element);

}
