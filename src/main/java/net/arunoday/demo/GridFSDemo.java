package net.arunoday.demo;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Demonstrate use of GridFS for binary storage and retrieval.
 * 
 * @author Aparna
 * 
 */
public class GridFSDemo {

	private static final Logger logger = LoggerFactory.getLogger(GridFSDemo.class);

	public static void main(String[] args) throws Exception {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");

		// ///////////////////////
		// Store
		// ///////////////////////

		final String fileName = "health_record.xml";
		InputStream content = GridFSDemo.class.getClassLoader().getResourceAsStream(fileName);
		// Get GridFS Bucket
		GridFS gridfsBucket = new GridFS(database, "testbucket");
		// Creates a file entry
		GridFSInputFile gfsFile = gridfsBucket.createFile(content);
		gfsFile.setFilename(fileName);
		// Save the file
		gfsFile.save();

		// ///////////////////////
		// Find All
		// ///////////////////////

		final DBCursor cursor = gridfsBucket.getFileList();
		while (cursor.hasNext()) {
			logger.info("Stored File: " + cursor.next());
		}

	}

}
