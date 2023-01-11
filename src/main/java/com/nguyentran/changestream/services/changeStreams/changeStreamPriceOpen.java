//package com.nguyentran.changestream.services.changeStreams;
//
//import static java.util.Collections.singletonList;
//
//import java.util.List;
//
//import org.bson.Document;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.bson.codecs.pojo.PojoCodecProvider;
//import org.bson.conversions.Bson;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoChangeStreamCursor;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Aggregates;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.UpdateOptions;
//import com.mongodb.client.model.changestream.ChangeStreamDocument;
//import com.mongodb.client.model.changestream.FullDocument;
//import com.nguyentran.changestream.models.DateOpen;
//import com.nguyentran.changestream.models.PriceOpen;
//import com.nguyentran.changestream.models.PriceTour;
//import com.nguyentran.changestream.services.DateOpenService;
//import com.nguyentran.changestream.services.PriceTourService;
//
//@Service
//public class changeStreamPriceOpen {
//	@Autowired
//	private MongoDatabase mongoDatabase;
//
//	@Autowired
//	private DateOpenService dateOpenService;
////	@Autowired
////	private PriceOpenService priceOpenService;
//	@Autowired
//	private PriceTourService priceTourService;
//
//	private MongoCollection<PriceOpen> priceOpenCollection;
//
//	@Autowired
//	public void changeStreamPriceOpenService() {
//		
//
//		getEventChange();
//
//	}
//
//	// get event DateOpen
//	public void getEventChange() {
//		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
//				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
//						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//
//		
//		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class)
//				.withCodecRegistry(pojoCodecRegistry);
//		;
//
//		List<Bson> pipeline = singletonList(Aggregates.match(Filters.or( Filters.eq("ns.coll", "priceTour"),
//	            Filters.eq("ns.coll", "dateOpen"))));
//		MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor = mongoDatabase.watch(pipeline)
//				.fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
//
//		System.out.println("==> Going through the stream a first time & record a resumeToken");
//
//		while (cursor.hasNext()) {
//
//			ChangeStreamDocument<Document> event = cursor.next();
//			
//			//check operation type
//			if(event.getOperationTypeString().equalsIgnoreCase("insert")
//					||event.getOperationTypeString().equalsIgnoreCase("update") ) {
//				
//				//check collection
//				if (event.getNamespace().getCollectionName().equals("dateOpen")){
//	                System.out.println(event.getFullDocument());
//	                String tourId = event.getFullDocument().get("tourId").toString();
//	                List<String> dateAvailable = event.getFullDocument().getList("dateAvailable", String.class);
//	                PriceTour priceTour = priceTourService.getPriceTourByTourId(tourId);
//
//	                Bson filter = Filters.eq("tourId", tourId);
//	                Bson newValue = new Document("dateAvailable", dateAvailable)
//	                        .append("tourId",  tourId)
//	                        .append("currency", priceTour.getCurrency())
//	                        .append("price", priceTour.getPrice());
//	                Bson updates = new Document("$set", newValue);
//	                UpdateOptions op = new UpdateOptions().upsert(true);
//
//	                priceOpenCollection.updateOne(filter, updates, op);
//	            }
//				//check collection
//	            if (event.getNamespace().getCollectionName().equals("priceTour")){
//	                System.out.println(event.getFullDocument());
//	                String tourId = event.getFullDocument().get("tourId").toString();
//	                Double price = event.getFullDocument().getDouble("price");
//	                String currency = event.getFullDocument().getString("currency");
//	                DateOpen dateOpen = dateOpenService.getDateOpenByTourId(tourId);
//
//	                Bson filter = Filters.eq("tourId",tourId);
//	                Bson newValue = new Document("currency", currency)
//	                        .append("tourId", tourId)
//	                        .append("price", price)
//	                        .append("dateAvailable", dateOpen.getDateAvailable());
//	                Bson updates = new Document("$set", newValue);
//	                UpdateOptions op = new UpdateOptions().upsert(true);
//
//
//	                priceOpenCollection.updateOne(filter, updates, op);
//	            }
//			}
//		
//			 
//
//
//			
//		
//		}
//	}
//
//
//
//}
