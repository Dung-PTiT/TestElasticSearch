/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Dell
 */
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.elasticsearch.client.Request;

public class Test2 {

    public static void main(String[] args) throws IOException {
//        RestClient restClient = RestClient.builder(
//                new HttpHost("localhost", 9200, "http")).build();
//
//        Request request = new Request("POST", "/_sql");
//        request.setJsonEntity("{\"query\":\"Show tables\"}");
//        Response response = restClient.performRequest(request);
//        String responseBody = EntityUtils.toString(response.getEntity());
//        System.out.println(responseBody);
//        JsonObject jsonObject = new JsonParser().parse(responseBody).getAsJsonObject();
//        System.out.println(jsonObject.get("rows"));
//        restClient.close();
        List<List<String>> list = new ArrayList<>();

        list.add(Arrays.asList("a", "b", "c"));
        list.add(Arrays.asList("dd"));
        list.add(Arrays.asList("eee", "fff"));
        String[][] array = new String[list.size()][];

        int i = 0;
        for (List<String> nestedList : list) {
            array[i++] = nestedList.toArray(new String[nestedList.size()]);
        }
        System.out.println(list.toString());
    }
}
