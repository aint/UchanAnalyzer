/*
 * Copyright (c) <2016> <Oleksandr Tyshkovets>
 *
 * This file is part of uchan-analyzer.
 *
 *  uchan-analyzer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  uchan-analyzer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with uchan-analyzer.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.aint.uchananalyzer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Tyshkovets
 */
public class Parser {

    private static final int LAST_PAGE = 2700;
    private static final String URL = "http://uchan.to/lp/index.php?board=all&page=";
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String TITLE_ATTRIBUTE_VALUE = "Тред:";
    private static final String POST_ATTRIBUTE = "id";
    private static final String POST_ATTRIBUTE_VALUE = "post_text_";
    private static final String REPLY_CLASS = "reply";
    private static final String FILE_SIZE_CLASS = "filesize";
    private static final String P_TAG = "p";

    public static void main(String[] args) throws IOException {
        Document page = Jsoup.connect(URL + LAST_PAGE).get();
        Set<String> topics = new HashSet<>(getThreadTopics(page));
        topics.forEach(System.out::println);
//        save2Json(topics);

        Map<String, Boolean> map = page.getElementsByClass(REPLY_CLASS).stream()
                .collect(Collectors.toMap(
                        p -> p.getElementsByAttributeValueContaining(POST_ATTRIBUTE, POST_ATTRIBUTE_VALUE).stream()
                                .map(p1 -> p1.getElementsByTag(P_TAG).text())
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.joining()),
                        p -> !p.getElementsByClass(FILE_SIZE_CLASS).isEmpty(), (p1, p2) -> p1, LinkedHashMap::new));
        map.entrySet().stream().forEach(System.out::println);


        List<String> posts = getPosts(page);
        posts.forEach(System.out::println);

    }

    private static void save2Json(Collection<String> collection) {
        JsonArray array = new JsonArray();
        for (String str : collection) {
            JsonObject innerJson = new JsonObject();
            innerJson.addProperty("topic", str);
            array.add(innerJson);
        }
        JsonObject json = new JsonObject();
        json.add(String.valueOf(LAST_PAGE), array);

        try (Writer writer = new FileWriter(String.valueOf(LAST_PAGE) + ".json")) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getThreadTopics(Document page) {
        Elements topics = page.getElementsByAttributeValueContaining(TITLE_ATTRIBUTE, TITLE_ATTRIBUTE_VALUE);
        return topics.stream().map(t -> t.attr("title")).collect(Collectors.toList());
    }

    private static List<String> getPosts(Document page) {
        return page.getElementsByAttributeValueContaining(POST_ATTRIBUTE, POST_ATTRIBUTE_VALUE).stream()
                .map(p -> p.getElementsByTag("p"))
                .collect(Collectors.toList())
                .stream()
                .map(Elements::text)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

}
