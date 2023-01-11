package com.nguyentran.changestream.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nguyentran.changestream.models.DateOpen;

@Repository
public interface DateOpenRepository extends MongoRepository<DateOpen, String>{

}
