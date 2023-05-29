package fr.dtos.common.utils;

public enum EServices {
    USER_SERVICE("user-service","user", "127.0.0.1", 8084),
    AUTH_SERVICE("auth-service", "auth", "127.0.0.1", 8083),
    CARD_SERVICE("card-service", "card", "127.0.0.1", 8081),
    MARKET_SERVICE("market-service", "market", "127.0.0.1", 8085),
    FRONT_SERVICE("front-service", "front", "127.0.0.1", 8080)

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
