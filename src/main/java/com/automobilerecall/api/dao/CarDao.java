package com.automobilerecall.api.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.automobilerecall.api.mongo.entity.Car;

/**
 * carDAO interface to lazy load.
 * 
 * @author imbrute
 *
 */
@Repository
public interface CarDao extends MongoRepository<Car, String> {

	Optional<Car> findByVinnumber(String vinnumber);

}
