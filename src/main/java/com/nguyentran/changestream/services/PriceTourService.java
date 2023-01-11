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
import com.nguyentran.changestream.repository.PriceTourRepository;

@Service
public class PriceTourService {

	@Autowired
	private PriceTourRepository priceTourRepository;
	@Autowired
	private MongoDatabase mongoDatabase;
	private MongoCollection<PriceTour> priceTourCollection;

	@Autowired
	public void PersonServiceImpl() {
		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		priceTourCollection = mongoDatabase.getCollection("priceTour", PriceTour.class)
				.withCodecRegistry(pojoCodecRegistry);
		;

	}

	// get by id
	public PriceTour getPriceTourById(String id) {
		return priceTourRepository.findById(id).get();
	}

	// get by tourId
	public PriceTour getPriceTourByTourId(String tourId) {
		List<Bson> pipeline = new ArrayList();
		Bson match = new Document("$match",new Document("tourId", tourId));

		pipeline.add(match);
		return priceTourCollection.aggregate(pipeline, PriceTour.class).first();
	}

}
