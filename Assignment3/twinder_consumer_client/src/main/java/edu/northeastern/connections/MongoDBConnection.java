package edu.northeastern.connections;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static edu.northeastern.utils.Constants.MONGO_DB_CONNECTION_STRING;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
public class MongoDBConnection {

    private static volatile MongoClient mongoClient;

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private MongoDBConnection() {
    }

    public static MongoClient getInstance() {
        if (mongoClient == null) {
            synchronized (MongoDBConnection.class) {
                if (mongoClient == null) {
                    log.info("Creating new mongo client instance...");
                    final CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
                    final CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
                    mongoClient = MongoClients.create(MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(MONGO_DB_CONNECTION_STRING))
                            .codecRegistry(codecRegistry)
                            .build());
                }
            }
        }
        return mongoClient;
    }

    public static TransactionOptions getDefaultTransactionOptions() {
        return txnOptions;
    }
}
