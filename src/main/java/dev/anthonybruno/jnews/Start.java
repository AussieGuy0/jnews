package dev.anthonybruno.jnews;

import com.github.redouane59.twitter.signature.TwitterCredentials;
import dev.anthonybruno.jnews.jep.DefaultHtmlJepClient;
import dev.anthonybruno.jnews.jep.HtmlJepService;
import dev.anthonybruno.jnews.twitter.TwitterClient;
import dev.anthonybruno.jnews.twitter.TwitteredTwitterClient;
import dev.anthonybruno.jnews.util.PropertiesUtil;

import java.net.http.HttpClient;
import java.nio.file.Path;

public class Start {
    public static void main(String[] args) {
        var jepService = new HtmlJepService(new DefaultHtmlJepClient(HttpClient.newHttpClient()));
        var properties = PropertiesUtil.readProperties(JepChanges.class.getResourceAsStream("/app.properties"));
        var twitterImpl = properties.getProperty("dev.anthonybruno.jnews.twitter.impl", "fake");
        TwitterClient twitterClient;
        if (twitterImpl.equals("fake")) {
            twitterClient = tweet -> null;
        } else {
            twitterClient = TwitteredTwitterClient.create(TwitterCredentials.builder()
                    .accessToken(properties.getProperty("dev.anthonybruno.jnews.twitter.access.token"))
                    .accessTokenSecret(properties.getProperty("dev.anthonybruno.jnews.twitter.access.token.secret"))
                    .apiKey(properties.getProperty("dev.anthonybruno.jnews.twitter.api.key"))
                    .apiSecretKey(properties.getProperty("dev.anthonybruno.jnews.twitter.api.secret.key"))
                    .build());

        }
        var jepChanges = new JepChanges(jepService, twitterClient, Path.of("data"));
        jepChanges.process();
    }
}
