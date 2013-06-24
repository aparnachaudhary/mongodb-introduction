package net.arunoday.demo;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Demonstrate how indexing works and use of Explain command.
 * 
 * @author Aparna
 * 
 */
public class IndexDemo {

	private static final Logger logger = LoggerFactory.getLogger(IndexDemo.class);

	public static void main(String[] args) throws UnknownHostException {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if it doesn't exist
		DBCollection collection = database.getCollection("indexdemo");
		collection.drop();

		// ///////////////////////
		// SAVE
		// ///////////////////////
		for (int i = 0; i < 100000; i++) {
			collection.insert(new BasicDBObjectBuilder().append("name", "Oracle").append("type", "database")
					.append("users", i).get());
		}

		// TODO: Create index

		collection.ensureIndex(new BasicDBObject("users", 1));

		// define query
		DBObject query = QueryBuilder.start("users").lessThanEquals(10).get();

		logger.info("Explain Plan: " + collection.find(query).explain());

		long lStartTime = System.currentTimeMillis();
		DBCursor cursor = collection.find(query);
		try {
			while (cursor.hasNext()) {
				logger.info("Query Result: " + cursor.next());
			}
		} finally {
			cursor.close();
		}
		logger.info("Total Query Time msec: " + (System.currentTimeMillis() - lStartTime));

	}
}
