# Jnews

Jnews is a Java application to periodically parse https://openjdk.java.net/jeps/0 and detect when a new
[JEPs](https://en.wikipedia.org/wiki/JDK_Enhancement_Proposal) has been added. These new JEPs are then posted to the
configured Twitter account.

## Running Locally

### Pre-req

- Docker
- Docker Compose
- Twitter api
  key/token ([guide](https://medium.com/geekculture/how-to-create-multiple-bots-with-a-single-twitter-developer-account-529eaba6a576))

### Steps

1. In project directory `cp src/main/resources/app.example.properties src/main/resources/app.properties`
2. Fill out `app.properties`
3. Run `docker-compose up` in the project directory
4. Enjoy!
