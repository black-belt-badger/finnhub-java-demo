package bbb.finnhub.demo;

import bbb.finnhub.client.ApiClient;
import bbb.finnhub.client.Configuration;
import bbb.finnhub.client.api.DefaultApi;
import bbb.finnhub.client.api.DefaultApiImpl;
import bbb.finnhub.client.auth.ApiKeyAuth;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import static java.util.Optional.ofNullable;

public class DefaultApiExample {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Configuration.setupDefaultApiClient(vertx, new JsonObject());
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath("https://finnhub.io/api/v1");
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("api_key");
        String envVariable = System.getenv("API_KEY");
        String systemProperty = System.getProperty("api_key");
        String apiKey = ofNullable(envVariable).orElse(systemProperty);
        auth.setApiKey(apiKey);
        DefaultApi api = new DefaultApiImpl(client);
        api.symbolSearch("AAPL", "US", ar -> {
                    if (ar.succeeded()) {
                        System.out.println("success: " + ar);
                    } else {
                        System.err.println("failure: " + ar.cause().getMessage());
                    }
                    vertx.close();
                }
        );
    }
}
