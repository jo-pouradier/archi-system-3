package fr.dtos.common.utils;

public enum EServices {
    USER_SERVICE("user-service","user", "localhost", 8084),
    AUTH_SERVICE("auth-service", "auth", "localhost", 8083),
    CARD_SERVICE("card-service", "card", "localhost", 8081),
    MARKET_SERVICE("market-service", "market", "localhost", 8085),
    FRONT_SERVICE("front-service", "front", "localhost", 8080)

    ;

    private String name;
    private String map;

    EServices(String name, String map, String url, int port) {
        this.name = name;
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public String getMap() {
        return map;
    }
}
