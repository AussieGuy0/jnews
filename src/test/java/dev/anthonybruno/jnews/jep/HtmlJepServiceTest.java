package dev.anthonybruno.jnews.jep;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlJepServiceTest {

    private final HtmlJepClient htmlJepClient = new InMemoryHtmlJepClient(readTestResource("example.html"));
    private final HtmlJepService htmlJepService = new HtmlJepService(htmlJepClient);

    @Test
    void returnsCorrectAmountOfJeps() {
        assertThat(htmlJepService.getJeps()).hasSize(363);
    }

    @Test
    void allJepsHaveName() {
        assertThat(htmlJepService.getJeps()).allMatch(jep -> jep.name() != null);
    }

    @Test
    void test() {
        htmlJepService.getJeps().forEach(System.out::println);
    }

    @Test
    void checkTextBlockJep() {
        var textBlockJep = htmlJepService.getJeps().stream()
                .filter(jep -> jep.number() == 378)
                .findFirst()
                .orElse(null);

        var expected = new Jep(378, "Text Blocks", URI.create("https://openjdk.java.net/jeps/378"), "Closed / Delivered", "Feature", "15", "spec/lang");
        assertThat(textBlockJep).isNotNull();
        assertThat(textBlockJep)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private String readTestResource(String path) {
        var classLoader = this.getClass().getClassLoader();
        try (var inputStream = classLoader.getResourceAsStream(path)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
