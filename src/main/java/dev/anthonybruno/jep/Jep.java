package dev.anthonybruno.jep;

import org.jetbrains.annotations.Nullable;

import java.net.URI;

public record Jep(int number, String name, URI link, String status, String type, @Nullable String release, @Nullable String component) {

}
