/**
 * 
 */
package net.arunoday.demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * 
 * Demonstrates the use of embedded BinData type.
 * 
 * @author Aparna
 * 
 */
public class EmbeddedBinDataDemo {

	public static void main(String[] args) throws IOException {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if it doesn't exist
		DBCollection collection = database.getCollection("bindatademo");
		collection.drop();

		InputStream content = EmbeddedBinDataDemo.class.getClassLoader().getResourceAsStream("mongodb.jpg");

		DBObject binaryObject = new BasicDBObject("bin", IOUtils.toByteArray(content));
		collection.insert(binaryObject);

		DBObject result = collection.findOne();
		FileUtils.writeByteArrayToFile(new File("xyz.jpg"), (byte[]) result.get("bin"));

	}
}
