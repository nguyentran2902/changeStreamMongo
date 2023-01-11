package com.nguyentran.changestream.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.nguyentran.changestream.models.DateOpen;
import com.nguyentran.changestream.models.PriceOpen;
import com.nguyentran.changestream.models.PriceTour;
import com.nguyentran.changestream.repository.DateOpenRepository;
import com.nguyentran.changestream.repository.PriceOpenRepository;


@Service
public class PriceOpenService {
	
	
	@Autowired
	private PriceOpenRepository priceOpenRepository;
	@Autowired
	private MongoDatabase mongoDatabase;
	private MongoCollection<PriceOpen> priceOpenCollection;
	
	@Autowired
	public void PersonServiceImpl() {
		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class)
				.withCodecRegistry(pojoCodecRegistry);
		;

	}
	
	//get by id
	public PriceOpen getPriceOpenById(String id) {
		return priceOpenRepository.findById(id).get();
	}
	
	// get by tourId
		public PriceOpen getPriceOpenByTourId(String tourId) {
			List<Bson> pipeline = new ArrayList();
			Bson match = new Document("$match",new Document("tourId", tourId));

			pipeline.add(match);

			return priceOpenCollection.aggregate(pipeline, PriceOpen.class).first();
		}
	
		//save
	public void savePriceOpen(PriceOpen priceOpen) {
		priceOpenRepository.save(priceOpen);
	}

	public void updatePriceOpen(PriceOpen priceOpen) {
		
		 
		 Bson filter = Filters.eq("tourId",priceOpen.getTourId());
		Bson update = Updates.combine(Updates.set("dateAvailable", priceOpen.getDateAvailable())
					,Updates.set("currency", priceOpen.getCurrency()),Updates.set("price", priceOpen.getPrice()));
			UpdateOptions uo = new UpdateOptions().upsert(true);

//		
			UpdateResult ur = priceOpenCollection.updateOne(filter, update,uo);
	}
	
	

}
