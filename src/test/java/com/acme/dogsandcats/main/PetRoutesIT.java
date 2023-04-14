package com.acme.dogsandcats.main;

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
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
@QuarkusIntegrationTest
class PetRoutesIT {

    @TestHTTPResource("/v3/pet")
    URL url;

    @Test
    void testAdd() throws IOException, InterruptedException {
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

            Pair<String, Integer> response = httpclient.execute(request, responseHandler);

            assertThat(response, notNullValue());
            assertThat(response.getKey(), notNullValue());
            assertThat(response.getValue(), notNullValue());
            assertThat(response.getValue(), equalTo(HttpStatus.SC_OK));

            Thread.sleep(10000);
        }
    }
}