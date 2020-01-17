package com.company;


import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.UUID;
import java.util.WeakHashMap;

public class Main {

    private static WeakHashMap<String, Result> map = new WeakHashMap<>();

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

        String strongly = "strongly"; // いわゆる強参照
        execute(strongly);

        server.start();
    }

    static String execute(String key) {

        var obj = map.get(key);
        if (obj != null) { // オブジェクトが取れたらそれを使う
            return obj.toString();
        }
        var result = new Result();
        map.put(key, result);
        return result.toString();
    }

    static class Result {
        public UUID uuid;
        public Date date;
        Result() {
            uuid = UUID.randomUUID();
            date = new Date();
        }

        @Override
        public String toString() {
            return "Result{ UUID="+uuid+" date="+date+" }\n";
        }
    }
}

