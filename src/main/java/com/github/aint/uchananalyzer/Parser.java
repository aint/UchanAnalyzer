package com.github.aint.uchananalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Tyshkovets
 */
public class Parser {

    public static void main(String[] args) throws IOException {
        Document page = Jsoup.connect("http://uchan.to/lp").get();
        Set<String> topics = new HashSet<>(getThreadTopics(page));
        topics.forEach(System.out::println);
    }

    private static List<String> getThreadTopics(Document page) {
        Elements topics = page.getElementsByAttributeValueContaining("title", "Тред:");
        return topics.stream().map(t -> t.attr("title")).collect(Collectors.toList());
    }

}
