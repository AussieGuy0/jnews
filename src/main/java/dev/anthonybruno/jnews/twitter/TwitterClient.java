package dev.anthonybruno.jnews.twitter;

public interface TwitterClient {
    record CreateTweet(String body) {
    }

    record TweetId(String id) {
    }

    TweetId postTweet(CreateTweet tweet);

}
