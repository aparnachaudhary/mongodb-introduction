package net.arunoday.demo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Demonstrate TTL collection feature of MongoDB. TTL collection needs date/time field. TTL collections need minimum
 * expiration period of 60 seconds
 * 
 * @author Aparna
 * 
 */
public class TTLCollectionDemo {

	private static final Logger logger = LoggerFactory.getLogger(TTLCollectionDemo.class);

	public static void main(String[] args) throws Exception {

		// MongoClient instance represents a pool of connections to the database
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if doesn't exist
		DBCollection ttlCollection = database.getCollection("ttldemo");
		ttlCollection.drop();

		// Create index on date field with expireAfterSeconds option
		ttlCollection.ensureIndex(new BasicDBObject("createdOn", 1), new BasicDBObject("expireAfterSeconds", 60));

		// Save document into collection
		BasicDBObject document = new BasicDBObject("name", "MongoDB").append("type", "database").append("count", 1)
				.append("createdOn", new Date());
		ttlCollection.insert(document);

		// check that document is stored in the collection
		logger.info(String.format("Total Records [%s] Saved Object [%s]: ", ttlCollection.count(),
				ttlCollection.findOne()));

		Thread.sleep(70000);

		// document should be deleted now
		logger.info(String.format("Total Records [%s] Saved Object [%s]: ", ttlCollection.count(),
				ttlCollection.findOne()));
	}
}
