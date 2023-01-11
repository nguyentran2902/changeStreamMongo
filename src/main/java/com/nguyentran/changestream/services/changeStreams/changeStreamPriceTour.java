package com.nguyentran.changestream.services.changeStreams;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.in;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.nguyentran.changestream.ChangeStreamApplication;
import com.nguyentran.changestream.models.DateOpen;
import com.nguyentran.changestream.models.Person;
import com.nguyentran.changestream.models.PriceOpen;
import com.nguyentran.changestream.models.PriceTour;
import com.nguyentran.changestream.services.DateOpenService;
import com.nguyentran.changestream.services.PriceTourService;

@Service
public class changeStreamPriceTour {

	@Autowired
	private MongoDatabase mongoDatabase;

	@Autowired
	private DateOpenService dateOpenService;

	@Autowired
	private PriceTourService priceTourService;

	private MongoCollection<PriceTour> priceTourCollection;
	private MongoCollection<PriceOpen> priceOpenCollection;

	@Autowired
	public void changeStreamPriceOpenService() {
		
		getEventChange2();

	}

	// get event DateOpen
	public void getEventChange2() {
		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		priceTourCollection = mongoDatabase.getCollection("priceTour", PriceTour.class)
				.withCodecRegistry(pojoCodecRegistry);
		;
		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class)
				.withCodecRegistry(pojoCodecRegistry);
		;

		
		List<Bson> pipeline = singletonList(match(in("operationType", asList("insert", "delete", "update"))));
		
		new Thread(()->{
			MongoChangeStreamCursor<ChangeStreamDocument<PriceTour>> cursor = priceTourCollection.watch(pipeline).fullDocument(FullDocument.UPDATE_LOOKUP).cursor();

			System.out.println("==> Going through the stream a first time priceTour");

			while (cursor.hasNext()) {

				ChangeStreamDocument<PriceTour> event = cursor.next();
				System.out.println("PriceTour");
				System.out.println(event.getFullDocument());
				String tourId = event.getFullDocument().getTourId();
				Double price = event.getFullDocument().getPrice();
				String currency = event.getFullDocument().getCurrency();
				Bson uFilter = Filters.eq("tourId", tourId);
				Bson newValue = null;

				DateOpen dateOpen = dateOpenService.getDateOpenByTourId(tourId);
				if (dateOpen == null) {
					newValue = new Document("currency", currency).append("price", price).append("tourId", tourId);
				} else {
					newValue = new Document("currency", currency).append("tourId", tourId).append("price", price)
							.append("dateAvailable", dateOpen.getDateAvailable());
				}

				Bson updates = new Document("$set", newValue);
				UpdateOptions op = new UpdateOptions().upsert(true);
				priceOpenCollection.updateOne(uFilter, updates, op);
			}
			
		}).start();
		

	}

}
