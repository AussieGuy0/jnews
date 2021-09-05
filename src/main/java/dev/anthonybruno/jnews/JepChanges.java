package dev.anthonybruno.jnews;

import com.github.redouane59.twitter.signature.TwitterCredentials;
import dev.anthonybruno.jnews.jep.DefaultHtmlJepClient;
import dev.anthonybruno.jnews.jep.HtmlJepService;
import dev.anthonybruno.jnews.jep.Jep;
import dev.anthonybruno.jnews.jep.JepService;
import dev.anthonybruno.jnews.twitter.TwitterClient;
import dev.anthonybruno.jnews.twitter.TwitteredTwitterClient;
import dev.anthonybruno.jnews.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JepChanges {
    private static final Logger log = LoggerFactory.getLogger(JepChanges.class);

    private final JepService jepService;
    private final TwitterClient twitterClient;
    private final Path resourceDirectory;

    public JepChanges(JepService jepService, TwitterClient twitterClient, Path resourceDirectory) {
        this.jepService = jepService;
        this.twitterClient = twitterClient;
        this.resourceDirectory = resourceDirectory;
    }

    public void process() {
        var jeps = determineNewJeps();
        postTweets(jeps);
    }

    private List<Jep> determineNewJeps() {
        var newJeps = new ArrayList<Jep>();
        log.info("Getting list of all JEPs");
        var jeps = jepService.getJeps();
        log.info("{} JEPs found", jeps.size());
        for (var jep : jeps) {
            var filePath = resourceDirectory.resolve(toFileName(jep));
            if (Files.isRegularFile(filePath)) {
                continue;
            }
            log.info("New JEP: {} ", jep);
            try {
                Files.writeString(filePath, "placeholder");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newJeps.add(jep);
        }
        log.info("{} new JEPs", newJeps.size());
        return Collections.unmodifiableList(newJeps);
    }

    private void postTweets(List<Jep> jeps) {
        for (var jep : jeps) {
            var body = "New Java Enhancement Proposal - " + jep.number() + ": " + jep.name() + ". " + jep.link();
            var tweet = twitterClient.postTweet(new TwitterClient.CreateTweet(body));
            if (tweet == null) {
                log.error("Could not post tweet with body {}", body);
            } else {
                log.info("Posted tweet {}, id {}", body, tweet.id());
            }
        }
    }

    private static String toFileName(Jep jep) {
        return jep.number() + ".txt";
    }

    public static void main(String[] args) {
        var jepService = new HtmlJepService(new DefaultHtmlJepClient(HttpClient.newHttpClient()));
        var properties = PropertiesUtil.readProperties(JepChanges.class.getResourceAsStream("/app.properties"));
        var twitterClient = TwitteredTwitterClient.create(TwitterCredentials.builder()
                .accessToken(properties.getProperty("dev.anthonybruno.jnews.twitter.access.token"))
                .accessTokenSecret(properties.getProperty("dev.anthonybruno.jnews.twitter.access.token.secret"))
                .apiKey(properties.getProperty("dev.anthonybruno.jnews.twitter.api.key"))
                .apiSecretKey(properties.getProperty("dev.anthonybruno.jnews.twitter.api.secret.key"))
                .build());
        var jepChanges = new JepChanges(jepService, twitterClient, Path.of("data"));
        jepChanges.process();
    }
}
