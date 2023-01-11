package com.nguyentran.changestream.repository;

import org.bson.conversions.Bson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.nguyentran.changestream.models.DateOpen;
import com.nguyentran.changestream.models.PriceOpen;
import com.nguyentran.changestream.models.PriceTour;

@Repository
public interface PriceOpenRepository extends MongoRepository<PriceOpen, String>{

	

}
