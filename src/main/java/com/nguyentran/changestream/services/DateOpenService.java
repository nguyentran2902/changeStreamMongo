package com.nguyentran.changestream.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nguyentran.changestream.models.DateOpen;
import com.nguyentran.changestream.models.PriceTour;
import com.nguyentran.changestream.repository.DateOpenRepository;

@Service
public class DateOpenService {

	@Autowired
	private DateOpenRepository dateOpenRepository;
	@Autowired
	private MongoDatabase mongoDatabase;
	private MongoCollection<DateOpen> dateOpenCollection;

	@Autowired
	public void PersonServiceImpl() {
		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		dateOpenCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class)
				.withCodecRegistry(pojoCodecRegistry);
		;

	}

	// get by id
	public DateOpen getDateOpenById(String id) {
		return dateOpenRepository.findById(id).get();
	}

	// get by tourId
	public DateOpen getDateOpenByTourId(String tourId) {
		List<Bson> pipeline = new ArrayList();
		Bson match = new Document("$match",new Document("tourId", tourId));

		pipeline.add(match);

		return dateOpenCollection.aggregate(pipeline, DateOpen.class).first();
	}

}
