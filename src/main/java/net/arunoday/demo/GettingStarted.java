/**
 * 
 */
package net.arunoday.demo;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Demonstrate basic usage of MongoDB Java Driver
 * 
 * @author Aparna
 * 
 */
public class GettingStarted {

	public static void main(String[] args) throws UnknownHostException {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if it doesn't exist
		DBCollection collection = database.getCollection("gettingstarted");

		// ///////////////////////
		// SAVE
		// ///////////////////////
		BasicDBObject document = new BasicDBObject("name", "MongoDB").append("type", "database").append("users", 100);
		collection.insert(document);
		// store one more
		collection.insert(new BasicDBObjectBuilder().append("name", "Oracle").append("type", "database")
				.append("users", 10).get());

		// Show data through HTTP interface
	}
}
