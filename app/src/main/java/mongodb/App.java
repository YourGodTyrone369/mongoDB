package mongodb;
import java.util.Arrays;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import static com.mongodb.client.model.Filters.eq;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        
        
        

        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://Silly:Whacky@cluster0.6vjwb.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Product");
            MongoCollection<Document> collection = database.getCollection("Test");

            System.out.println("What product would you like to view?");
            String search = keyboard.nextLine();
            if (!(search.equals("none"))) {
                // Creates instructions to project two document fields
                Bson projectionFields = Projections.fields(
                        Projections.include("name", "price", "description"),
                        Projections.excludeId());
                // Retrieves the first matching document, applying a projection and a descending sort to the results
                Document doc = collection.find(eq("name", search))
                        .projection(projectionFields)
                        .first();

                System.out.println(doc);
            }

            System.out.println("What is the name of this product?");
            String name = keyboard.nextLine();
            System.out.println("What is the price of this product?");
            String price = keyboard.nextLine();
            System.out.println("Describe this product.");
            String description = keyboard.nextLine();
            if (!(name.equals("none"))) {
                try {
                // Inserts a sample document describing a movie into the collection
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("name", name)
                        .append("price", "$" + price)
                        .append("description", description));
                // Prints the ID of the inserted document
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
                // Prints a message if any exceptions occur during the operation
                } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }}

            

            System.out.println("What product would you like to update?");
            String update = keyboard.nextLine();
            if (!(update.equals("none"))) {
                Document query = new Document().append("title",  "Cool Runnings 2");
                // Creates instructions to update the values of three document fields
                Bson updates = Updates.combine(
                        Updates.set("runtime", 99),
                        Updates.addToSet("genres", "Sports"),
                        Updates.currentTimestamp("lastUpdated"));
                // Instructs the driver to insert a new document if none match the query
                UpdateOptions options = new UpdateOptions().upsert(true);
                try {
                    // Updates the first document that has a "title" value of "Cool Runnings 2"
                    UpdateResult result = collection.updateOne(query, updates, options);
                    // Prints the number of updated documents and the upserted document ID, if an upsert was performed
                    System.out.println("Modified document count: " + result.getModifiedCount());
                    System.out.println("Upserted id: " + result.getUpsertedId());
                
                // Prints a message if any exceptions occur during the operation
                } catch (MongoException me) {
                    System.err.println("Unable to update due to an error: " + me);
                }
            }
        }
    }
}
