package edu.northeastern;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import edu.northeastern.utils.ServletHelper;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@WebServlet(name = "MatchesServlet", value = "/MatchesServlet")
@Log4j2
public class MatchesServlet extends HttpServlet {

//    @Override
//    public void init() throws ServletException {
//        super.init();
//    }

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
        MongoCollection<Document> collection  = database.getCollection("twinder-data");

        response.setContentType("text/plain");

        // check we have a URL!
        final String urlPath = request.getPathInfo();
        if (urlPath == null || urlPath.isEmpty()) {
            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, "Required path parameter is missing.");
            return;
        }

        // validate path parameter
        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, "Required path parameter is bad.");
        }

        final String userId = urlParts[1];

        // validate string payload parsing
        final String payload = ServletHelper.parsePayloadString(request);


        // Replace <query_field> and <query_value> with actual values
        String queryField = "<query_field>";
        String queryValue = "<query_value>";

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
        response.getWriter().write(result);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

    }

//    @Override
//    public void destroy() {
//        super.destroy();
//        cursor.close();
//        client.close();
//    }

    private boolean isUrlValid(String[] parts) {
        final String leftOrRight = parts[1];
        return leftOrRight.equals("left") || leftOrRight.equals("right");
    }

    private void responseHandler(final HttpServletResponse response,
                                 final int statusCode,
                                 final String outputBody) throws IOException {
        response.setStatus(statusCode);
        response.getOutputStream().print(outputBody);
        response.getOutputStream().flush();
    }
}
