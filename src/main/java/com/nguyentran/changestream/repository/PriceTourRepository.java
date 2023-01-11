package com.nguyentran.changestream.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nguyentran.changestream.models.DateOpen;
import com.nguyentran.changestream.models.PriceOpen;
import com.nguyentran.changestream.models.PriceTour;

@Repository
public interface PriceTourRepository extends MongoRepository<PriceTour, String>{

}
