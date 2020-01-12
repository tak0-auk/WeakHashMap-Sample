package com.company;


import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.WeakHashMap;

public class Main {

    private static WeakHashMap<String, Result> map = new WeakHashMap<>();
    private static String word = "always";

    public static void main(String[] args) throws IOException {

        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        var context = server.createContext("/");

        context.setHandler((httpExchange) -> {
            var result = execute(httpExchange.getRequestURI().getQuery());
            httpExchange.sendResponseHeaders(200, result.getBytes().length);
            var response = httpExchange.getResponseBody();
            response.write(result.getBytes());
            response.close();
        });

        execute(word);

        server.start();
    }

    static String execute(String query) {

        var obj = map.get(query);
        if (obj != null) {
            return obj.toString();
        }
        var result = new Result(query);
        map.put(query, result);
        return result.toString();
    }

    static class Result {
        public Date date;
        Result(String query) {
            date = new Date();
        }

        @Override
        public String toString() {
            return "Result{" +
                    "date=" + date +
                    "}\n";
        }
    }
}

