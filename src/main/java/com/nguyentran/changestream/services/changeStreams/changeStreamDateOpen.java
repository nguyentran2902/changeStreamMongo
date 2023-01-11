package com.nguyentran.changestream.services.changeStreams;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.in;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.nguyentran.changestream.models.DateOpen;
import com.nguyentran.changestream.models.PriceOpen;
import com.nguyentran.changestream.models.PriceTour;
import com.nguyentran.changestream.services.DateOpenService;
import com.nguyentran.changestream.services.PriceTourService;

@Service
public class changeStreamDateOpen {

	@Autowired
	private MongoDatabase mongoDatabase;

	@Autowired
	private PriceTourService priceTourService;

	private MongoCollection<DateOpen> dateOpenCollection;
	private MongoCollection<PriceOpen> priceOpenCollection;

	@Autowired
	public void changeStreamPriceOpenService() {

		getEventChange();

	}

	// get event DateOpen
	public void getEventChange() {
		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		dateOpenCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class)
				.withCodecRegistry(pojoCodecRegistry);
		;
		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class)
				.withCodecRegistry(pojoCodecRegistry);
		;

//		Bson filter = Filters.eq("operationType", Arrays.asList("update", "insert"));
//		List<Bson> pipeline = singletonList(Aggregates.match(filter));
		List<Bson> pipeline = singletonList(match(in("operationType", asList("insert", "delete", "update"))));
		
		new Thread(()->{
			MongoChangeStreamCursor<ChangeStreamDocument<DateOpen>> cursor = dateOpenCollection.watch(pipeline).fullDocument(FullDocument.UPDATE_LOOKUP).cursor();

			System.out.println("==> Going through the stream a first time dateOpen");

			while (cursor.hasNext()) {

				ChangeStreamDocument<DateOpen> event = cursor.next();

				System.out.println(event.getFullDocument());
				System.out.println("DateOpen");

				String tourId = event.getFullDocument().getTourId();
				List<String> dateAvailable = event.getFullDocument().getDateAvailable();
				Bson uFilter = Filters.eq("tourId", tourId);
				Bson newValue = null;

				PriceTour priceTour = priceTourService.getPriceTourByTourId(tourId);
				if (priceTour == null) {
					newValue = new Document("dateAvailable", dateAvailable).append("tourId", tourId);
				}
				newValue = new Document("dateAvailable", dateAvailable).append("tourId", tourId)
						.append("currency", priceTour.getCurrency()).append("price", priceTour.getPrice());

				Bson updates = new Document("$set", newValue);
				UpdateOptions op = new UpdateOptions().upsert(true);

				priceOpenCollection.updateOne(uFilter, updates, op);
			}
			
		}).start();
		
		

	}

}
