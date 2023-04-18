package com.acme.dogsandcats.main;

import com.acme.dogsandcats.generated.model.Pet;
import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusIntegrationTest
class PetRoutesIT {

    @TestHTTPResource("/v3/pet")
    URL url;

    DevServicesContext devServicesContext;

    @Test
    void testAdd() throws IOException {
        Pair<String, Integer> response = add();

        assertThat(response, notNullValue());
        assertThat(response.getKey(), notNullValue());
        assertThat(response.getValue(), notNullValue());
        assertThat(response.getValue(), equalTo(HttpStatus.SC_OK));

        assertThat(new JsonObject(response.getKey()).mapTo(Pet.class), notNullValue());
    }

    @Test
    void testIncoming() throws IOException, InterruptedException {
        Map<String, String> props = devServicesContext.devServicesProperties();
        String mappedPort = props.get("amqp-mapped-port");

        Pair<String, Integer> addResponse = add();

        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory(String.format("amqp://localhost:%s", mappedPort));
        try (JMSContext context = jmsConnectionFactory.createContext()) {
            Queue incomingDestination = context.createQueue("incoming");
            JMSProducer producer = context.createProducer();

            producer.send(incomingDestination, addResponse.getKey());
        }
    }

    private Pair<String, Integer> add() throws IOException {
        Pair<String, Integer> answer;

        File file = new File(getClass().getClassLoader().getResource("doggie.json").getFile());
        String jsonStr = IOUtils.toString(new FileReader(file));
        JsonObject jsonObj = new JsonObject(jsonStr);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ClassicHttpRequest request = new HttpPost(url.toString());
            request.setEntity(new StringEntity(jsonObj.toString(), ContentType.APPLICATION_JSON));

            final HttpClientResponseHandler<Pair<String, Integer>> responseHandler = response -> {
                final int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    final HttpEntity entity = response.getEntity();
                    try {
                        String resp = entity != null ? EntityUtils.toString(entity) : null;
                        return Pair.of(resp, status);
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            answer = httpclient.execute(request, responseHandler);
        }

        return answer;
    }

    private JsonObject getDoggieJson() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("doggie.json").getFile());
        String jsonStr = IOUtils.toString(new FileReader(file));

        return new JsonObject(jsonStr);
    }
}