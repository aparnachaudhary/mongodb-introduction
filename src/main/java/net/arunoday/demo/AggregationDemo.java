package net.arunoday.demo;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Demonstrate the use of aggregation feature of MongoDB.
 * 
 * $MONGO_HOME/mongoimport -d introduction -c grades < grades.json
 * 
 * 
 * @author Aparna
 * 
 */
public class AggregationDemo {

	private static final Logger logger = LoggerFactory.getLogger(AggregationDemo.class);

	public static void main(String[] args) throws UnknownHostException {

		// MongoClient instance represents a pool of connections to the database
		// Default host is localhost and port is 27017
		MongoClient client = new MongoClient();
		// Get the database
		DB database = client.getDB("introduction");
		// Get collection; creates new collection if it doesn't exist
		DBCollection collection = database.getCollection("grades");

		// create our pipeline operations, first with the $match
		DBObject match = new BasicDBObject("$match", QueryBuilder.start().and("score").greaterThanEquals(65)
				.and("type").is("exam").get());

		// second with the $group
		DBObject groupFields = BasicDBObjectBuilder.start().add("_id", "$student_id")
				.add("average", new BasicDBObject("$avg", "$score")).get();

		DBObject group = new BasicDBObject("$group", groupFields);

		// $sort
		DBObject sortFields = new BasicDBObject("$sort", new BasicDBObject("average", 1));

		// $limit
		DBObject limit = new BasicDBObject("$limit", 1);

		AggregationOutput aggregationOutput = collection.aggregate(match, group, sortFields, limit);
		logger.info("Aggregation Result: " + aggregationOutput);

		// All exam scores greater than or equal to 65. and sort those scores from lowest to highest.
		// db.grades.aggregate( [
		// { $match : { score : { $gte : 65} , type: 'exam'} },
		// {'$group':{'_id':'$student_id', 'average':{$avg:'$score'}}} ,
		// {'$sort':{'average':1}},
		// {'$limit':1}
		// ])

		removeLowestHomeworkScore(collection);

	}

	private static void removeLowestHomeworkScore(DBCollection collection) {

		// create our pipeline operations, first with the $match
		DBObject match = new BasicDBObject("$match", new QueryBuilder().and("type").is("homework").get());

		// second with the $group
		DBObject groupFields = new BasicDBObjectBuilder().add("_id", "$student_id")
				.add("min", new BasicDBObject("$min", "$score")).get();

		DBObject group = new BasicDBObject("$group", groupFields);

		// $sort
		DBObject sortFields = new BasicDBObject("$sort", new BasicDBObject("_id", 1));

		AggregationOutput aggregationOutput = collection.aggregate(match, group, sortFields);

		for (DBObject result : aggregationOutput.results()) {
			DBObject query = new QueryBuilder().and("student_id").is(result.get("_id")).and("score")
					.is(result.get("min")).and("type").is("homework").get();
			collection.findAndRemove(query);
			logger.info("Query Result: " + query);
		}

	}
}
