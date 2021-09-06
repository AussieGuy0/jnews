package dev.anthonybruno.jnews;

import dev.anthonybruno.jnews.jep.Jep;
import dev.anthonybruno.jnews.jep.JepService;
import dev.anthonybruno.jnews.twitter.TwitterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
                newJeps.add(jep);
            } catch (IOException e) {
                log.error("Could not write JEP {} to resourceDirectory", jep.number(), e);
            }
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
}
