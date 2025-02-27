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
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("What is the name of this product?");
        String name = keyboard.nextLine();
        System.out.println("What is the price of this product?");
        String price = keyboard.nextLine();
        System.out.println("Describe this product.");
        String description = keyboard.nextLine();

        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://Silly:Whacky@cluster0.6vjwb.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Product");
            MongoCollection<Document> collection = database.getCollection("Test");
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
            }
        }
    }
}
