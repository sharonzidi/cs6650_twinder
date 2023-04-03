package edu.northeastern;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import edu.northeastern.models.GetMatchesResponse;
import edu.northeastern.models.MessageResponse;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

import static edu.northeastern.utils.ServletHelper.responseHandler;

@WebServlet(name = "MatchesServlet", value = "/MatchesServlet")
@Log4j2
public class MatchesServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request);
        ConnectionString connectionString = new ConnectionString("mongodb+srv://dbuser:EwuPYvnsU2mtLsbt@cluster0.lhvylio.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("cs6650-distributed-sys");
        MongoCollection<Document> collection = database.getCollection("twinder-data"); //the collection of documents to query

        response.setContentType("text/plain");

        System.out.println(request.getPathInfo());

        // check we have a URL!
        final String urlPath = request.getPathInfo();

        if (urlPath == null || urlPath.isEmpty()) {
            MessageResponse messageResponse = MessageResponse.builder()
                    .message("The given input is invalid.")
                    .build();
            responseHandler(response, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(messageResponse));
            return;
        }
        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            MessageResponse messageResponse = MessageResponse.builder()
                    .message("The given input is invalid.")
                    .build();
            responseHandler(response, HttpServletResponse.SC_BAD_REQUEST, gson.toJson(messageResponse));
            return;
        }

        final String userId = urlParts[1];


        // Replace <query_field> and <query_value> with actual values
        String queryField = "firstName";
        String queryValue = "Siyan";

        Document query = new Document(queryField, queryValue);
        MongoCursor<Document> cursor = collection.find(query).iterator();

        // Process the result set...
        StringBuilder resultBuilder = new StringBuilder();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            resultBuilder.append(document.toJson());
            resultBuilder.append("\n");
        }

        String result = resultBuilder.toString();
        System.out.println(result);

        // check if the user exist
        if (result.isEmpty()) {
            MessageResponse messageResponse = MessageResponse.builder()
                    .message("The user doesn't exist.")
                    .build();
            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, gson.toJson(messageResponse));
            return;
        }

        GetMatchesResponse getMatchesResponse = GetMatchesResponse.builder()
                .matchList(new LinkedList<>())
                .build();
        responseHandler(response, HttpServletResponse.SC_OK, gson.toJson(getMatchesResponse));
    }

    private boolean isUrlValid(String[] parts) {
        // get request url: localhost:9988/dev-server/matches/123
        // parts: [, 123]
        return parts.length == 2;
    }
}
