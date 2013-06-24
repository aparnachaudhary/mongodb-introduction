/**
 * 
 */
package net.arunoday.demo;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Demonstrate query operation
 * 
 * @author Aparna
 * 
 */
public class QueryDemo {

	private static final Logger logger = LoggerFactory.getLogger(QueryDemo.class);

	public static void main(String[] args) throws UnknownHostException {

		// MongoClient instance represents a pool of connections to the database
		MongoClient client = new MongoClient();
		// Get the database context
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if doesn't exist
		DBCollection collection = database.getCollection("querydemo");

		collection.drop();

		// ///////////////////////
		// Save data
		// ///////////////////////
		BasicDBObject document = new BasicDBObject("name", "MongoDB").append("type", "database").append("users", 100);
		collection.insert(document);
		// store one more
		collection.insert(new BasicDBObject("name", "Oracle").append("type", "database").append("users", 10));

		// ///////////////////////
		// Find All
		// ///////////////////////
		findAll(collection);

		// ///////////////////////
		// Use QueryBuilder - Convenient API
		// ///////////////////////
		basicQuery(collection);

	}

	private static void basicQuery(DBCollection collection) {
		DBCursor cursor = collection.find(QueryBuilder.start("name").is("Oracle").get());
		try {
			while (cursor.hasNext()) {
				logger.info("QueryBuilder Result: " + cursor.next());
			}
		} finally {
			cursor.close();
		}
	}

	private static void findAll(DBCollection collection) {
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
