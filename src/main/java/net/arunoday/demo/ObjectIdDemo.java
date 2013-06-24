package net.arunoday.demo;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Demonstrates use of custom {@link ObjectId}
 * 
 * @author Aparna
 * 
 */
public class ObjectIdDemo {

	private static final Logger logger = LoggerFactory.getLogger(ObjectIdDemo.class);

	public static void main(String[] args) throws UnknownHostException {
		// MongoClient instance represents a pool of connections to the database
		MongoClient client = new MongoClient();
		// Get the database context
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if doesn't exist
		DBCollection collection = database.getCollection("objectiddemo");
		// clean up existing data if any
		collection.drop();

		// ///////////////////////
		// SAVE
		// ///////////////////////
		collection.insert(new BasicDBObject("_id", "aparnac").append("fname", "Aparna").append("lname", "chaudhary"));

		logger.info("Result: " + collection.findOne(new BasicDBObjectBuilder().add("_id", "aparnac").get()));

	}

}
