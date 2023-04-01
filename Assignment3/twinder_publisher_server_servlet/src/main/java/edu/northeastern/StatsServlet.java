package edu.northeastern;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
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

@WebServlet(name = "StatsServlet", value = "/StatsServlet")
@Log4j2
public class StatsServlet extends HttpServlet {
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        ConnectionString connectionString = new ConnectionString("mongodb+srv://xiazid:<password>@cluster0.lhvylio.mongodb.net/?retryWrites=true&w=majority");
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .serverApi(ServerApi.builder()
//                        .version(ServerApiVersion.V1)
//                        .build())
//                .build();
//        MongoClient mongoClient = MongoClients.create(settings);
//        MongoDatabase database = mongoClient.getDatabase("cs6650-distributed-sys");
//        MongoCollection<Document> collection  = database.getCollection("twinder-data");
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Replace <query_field> and <query_value> with actual values
//        String queryField = "<query_field>";
//        String queryValue = "<query_value>";
//
//        Document query = new Document(queryField, queryValue);
//        MongoCursor<Document> cursor = collection.find(query).iterator();
//
//        // Process the result set...
//        StringBuilder resultBuilder = new StringBuilder();
//        while (cursor.hasNext()) {
//            Document document = cursor.next();
//            resultBuilder.append(document.toJson());
//            resultBuilder.append("\n");
//        }

//        String result = resultBuilder.toString();
        response.getWriter().write("StatsServlet");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

    }

//    @Override
//    public void destroy() {
//        super.destroy();
//        cursor.close();
//        client.close();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/plain");
//
//        // check we have a URL!
//        final String urlPath = request.getPathInfo();
//        if (urlPath == null || urlPath.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            response.getWriter().write("missing parameters");
//            return;
//        }
//
//        String[] urlParts = urlPath.split("/");
//        if (!isUrlValid(urlParts)) {
//            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, "Required path parameter is bad.");
//        } else {
//            responseHandler(response, HttpServletResponse.SC_OK, "It works!");
//
//        }
//    }
//
//    private boolean isUrlValid(String[] parts) {
//        final String leftOrRight = parts[1];
//        return leftOrRight.equals("left") || leftOrRight.equals("right");
//    }
//
//    private void responseHandler(final HttpServletResponse response,
//                                 final int statusCode,
//                                 final String outputBody) throws IOException {
//        response.setStatus(statusCode);
//        response.getOutputStream().print(outputBody);
//        response.getOutputStream().flush();
//    }
}
