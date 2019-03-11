package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Intent;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Intent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntentRepository extends MongoRepository<Intent,String> {
    void deleteByTag(String tag);
    Intent findByTag(String tag);
}
