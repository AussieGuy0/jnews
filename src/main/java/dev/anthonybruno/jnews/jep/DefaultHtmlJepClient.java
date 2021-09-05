package dev.anthonybruno.jnews.jep;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultHtmlJepClient implements HtmlJepClient {
    private final HttpClient httpClient;

    public DefaultHtmlJepClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getHtml(URI url) {
        try {
            var req = HttpRequest.newBuilder(url).GET().build();
            var res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() < 200 || res.statusCode() > 299) {
                throw new RuntimeException("Not 200 status code " + res.statusCode());
            }
            return res.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
