package com.nguyentran.changestream;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.text.Document;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.nguyentran.changestream.models.Person;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.changestream.FullDocument.UPDATE_LOOKUP;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SpringBootApplication
@EnableMongoRepositories
public class ChangeStreamApplication extends AbstractMongoClientConfiguration {

	@Value("${spring.data.mongodb.host}")
	private String mongoHost;

	@Value("${spring.data.mongodb.port}")
	private String mongopPort;

	@Value("${spring.data.mongodb.database}")
	private String mongoDB;

	public static void main(String[] args) {

		SpringApplication.run(ChangeStreamApplication.class, args);
	}
	
	@Bean
	public  MongoClient mongoClient() {
		return MongoClients.create();
	}
	
	@Bean
	public  MongoDatabase mongoDatabase() {
		return mongoClient().getDatabase(mongoDB);
	}

//	private static void getEventWithResumeToken(MongoCollection<Person> persons) {
//		List<Bson> pipeline = singletonList(match(in("operationType", asList("insert", "delete", "update"))));
////		ChangeStreamIterable<Person> changeStream = persons.watch(pipeline);
////		MongoChangeStreamCursor<ChangeStreamDocument<Person>> cursor = changeStream.cursor();
//		MongoChangeStreamCursor<ChangeStreamDocument<Person>> cursor = persons.watch(pipeline).cursor();
//		System.out.println("==> Going through the stream a first time & record a resumeToken");
////		int indexOfOperationToRestartFrom = 1;
////		int indexOfIncident = 2;
////		int counter = 0;
//		
//		BsonDocument resumeToken = null;
////		while (cursor.hasNext() && counter != indexOfIncident) {
//		while (cursor.hasNext()) {
//			ChangeStreamDocument<Person> event = cursor.next();
////			if (indexOfOperationToRestartFrom == counter) {
//			resumeToken = event.getResumeToken();
////			}
//
//			System.err.println("Database: " + event.getNamespace());
//			System.err.println("Time: " + event.getClusterTime());
//			System.err.println("ResumeToken: " + event.getResumeToken());
//			System.err.println("OperationType: " + event.getOperationTypeString());
//			BsonObjectId obj= (BsonObjectId)event.getDocumentKey().get("_id");
//			System.err.println("DocumentKey: " + obj.getValue());
//			System.err.println("Event: " + event.getUpdateDescription());
////			counter++;
//
//		}
//
//		persons.watch(pipeline).resumeAfter(resumeToken).forEach(printEvent());
//		
////		        assert resumeToken != null;
//		
//
//	}
//	
//	private static void getEventWithResumeToken2(MongoDatabase db) {
//		List<Bson> pipeline = singletonList(match(in("operationType", asList("insert", "delete", "update"))));
////		ChangeStreamIterable<Person> changeStream = persons.watch(pipeline);
////		MongoChangeStreamCursor<ChangeStreamDocument<Person>> cursor = changeStream.cursor();
//		MongoChangeStreamCursor<ChangeStreamDocument<org.bson.Document>> cursor = db.watch(pipeline).cursor();
//		System.out.println("==> Going through the stream a first time & record a resumeToken");
////		int indexOfOperationToRestartFrom = 1;
////		int indexOfIncident = 2;
////		int counter = 0;
//		
//		BsonDocument resumeToken = null;
////		while (cursor.hasNext() && counter != indexOfIncident) {
//		while (cursor.hasNext()) {
//			ChangeStreamDocument<org.bson.Document> event = cursor.next();
////			if (indexOfOperationToRestartFrom == counter) {
//			resumeToken = event.getResumeToken();
////			}
//
//			System.err.println("Database: " + event.getNamespace());
//			System.err.println("Time: " + event.getClusterTime());
//			System.err.println("ResumeToken: " + event.getResumeToken());
//			System.err.println("OperationType: " + event.getOperationTypeString());
//			BsonObjectId obj= (BsonObjectId)event.getDocumentKey().get("_id");
//			System.err.println("DocumentKey: " + obj.getValue());
//			System.err.println("Event: " + event.getUpdateDescription());
////			counter++;
//
//		}
//
//		db.watch(pipeline).resumeAfter(resumeToken).forEach(printEvent2());
//		
////		        assert resumeToken != null;
//		
//
//	}
//
//	private static Consumer<? super ChangeStreamDocument<Person>> printEvent() {
//		return System.err::println;
//	}
//	
//	private static Consumer<? super ChangeStreamDocument<org.bson.Document>> printEvent2() {
//		return System.err::println;
//	}

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return mongoDB;
	}

}
