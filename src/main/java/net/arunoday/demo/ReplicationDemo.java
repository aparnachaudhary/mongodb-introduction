/**
 * 
 */
package net.arunoday.demo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * Demonstrate replica set usage with MongoDB
 * 
 * @author Aparna
 * 
 */
public class ReplicationDemo {

	public static void main(String[] args) throws UnknownHostException {

		String urls = "localhost:27017,localhost:27018,localhost:27019";
		ArrayList<ServerAddress> addr = new ArrayList<ServerAddress>();
		for (String hostString : urls.split(",")) {
			addr.add(new ServerAddress(hostString));
		}
		// MongoClient instance represents a pool of connections to the database
		MongoClient client = new MongoClient(addr);

		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if it doesn't exist
		DBCollection collection = database.getCollection("gettingstarted");

		// ///////////////////////
		// SAVE
		// ///////////////////////
		collection.insert(new BasicDBObject("name", "MongoDB").append("type", "database").append("users",
				new Random().nextInt(100)));
		// store one more
		collection.insert(new BasicDBObjectBuilder().append("name", "Oracle").append("type", "database")
				.append("users", new Random().nextInt(5)).get());

	}
}
