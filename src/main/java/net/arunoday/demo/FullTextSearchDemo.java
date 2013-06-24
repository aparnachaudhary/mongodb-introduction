/**
 * 
 */
package net.arunoday.demo;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Demonstrate full text search feature of MongoDB. Make sure MongoDB is started with <code>--setParameter
 * textSearchEnabled=true </code> setting
 * 
 * @author Aparna
 * 
 */
public class FullTextSearchDemo {

	private static final Logger logger = LoggerFactory.getLogger("logger");

	public static void main(String[] args) throws UnknownHostException {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if doesn't exist
		DBCollection collection = database.getCollection("searchdemo");
		collection.drop();

		collection.ensureIndex(new BasicDBObject("description", "text"));

		// ///////////////////////
		// BASIC FTS
		// ///////////////////////
		basicDemo(database, collection);

		// ///////////////////////
		// STEMMING DEMO
		// ///////////////////////
		stemmingDemo(database, collection);
	}

	private static void basicDemo(DB database, DBCollection collection) {
		BasicDBObject document = new BasicDBObject("name", "MongoDB")
				.append("type", "database")
				.append("description",
						"MongoDB is a Document based database. GridFS is a storage mechanism for persisting large objects in MongoDB.");
		collection.insert(document);

		final DBObject textSearchCommand = new BasicDBObject();
		textSearchCommand.put("text", "searchdemo");
		textSearchCommand.put("search", "MongoDB");
		final CommandResult commandResult = database.command(textSearchCommand);

		logger.info("Result of FTS: " + commandResult);
	}

	private static void stemmingDemo(DB database, DBCollection collection) {
		// insert data
		collection.insert(BasicDBObjectBuilder.start().append("type", "database1")
				.append("description", "Our department is growing fast").get());
		collection.insert(BasicDBObjectBuilder.start().append("type", "database2")
				.append("description", "If you work smart, you can grow fast").get());
		collection.insert(BasicDBObjectBuilder.start().append("type", "database3")
				.append("description", "Baby has grown quite big").get());
		collection.insert(BasicDBObjectBuilder.start().append("type", "database")
				.append("description", "Just learning some new-tech").get());

		// execute search query
		final DBObject textSearchCommand = new BasicDBObject();
		textSearchCommand.put("text", "searchdemo");
		textSearchCommand.put("search", "grow");
		final CommandResult commandResult = database.command(textSearchCommand);

		// fetch results
		BasicDBList results = (BasicDBList) commandResult.get("results");
		for (Object result : results) {
			logger.info("Stemming Result: " + ((BasicDBObject) result).get("obj"));
		}

	}
}
