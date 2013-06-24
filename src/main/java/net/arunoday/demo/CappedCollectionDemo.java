package net.arunoday.demo;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Demonstrate the use of capped collection.
 * 
 * @author Aparna
 * 
 */
public class CappedCollectionDemo {

	private static final Logger logger = LoggerFactory.getLogger(CappedCollectionDemo.class);

	public static void main(String[] args) throws UnknownHostException {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");

		DBCollection collection;
		if (database.collectionExists("cappeddemo")) {
			collection = database.getCollection("cappeddemo");
			collection.drop();
		}
		// Create a capped collection with a size of 3 bytes
		DBObject options = BasicDBObjectBuilder.start().add("capped", true).add("size", 3).get();
		collection = database.createCollection("cappeddemo", options);

		// ///////////////////////
		// SAVE
		// ///////////////////////
		for (int i = 0; i < 100; i++) {
			collection.insert(new BasicDBObjectBuilder().append("name", "Oracle").append("type", "database")
					.append("users", i).get());
		}

		logger.info("Total Documents: " + collection.count());

		DBCursor cursor = collection.find();
		try {
			while (cursor.hasNext()) {
				logger.info("Document: " + cursor.next());
			}
		} finally {
			cursor.close();
		}
	}

}
