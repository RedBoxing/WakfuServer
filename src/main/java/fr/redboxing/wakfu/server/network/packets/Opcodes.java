package fr.redboxing.wakfu.server.network.packets;

public class Opcodes {
    public static int VERSION = 22;
    public static int AUTHENTICATION = 428;
    public static int REQUEST_RSA = 546;
    public static int PROXIES_REQUEST = 462;
    public static int AUTHENTICATION_TOKEN_REQUEST = 433;
    public static int AUTHENTICATION_TOKEN_REDEEM = 442;
    public static int LANGUAGE = 17054;
    public static int CREATE_CHARACTER = 17857;

    public static int VERSION_RESULT = 17;
    public static int RSA_RESULT = 460;
    public static int SET_IP = 364;
    public static int PROXY_RELAY_ERROR = 32;
    public static int DISPATCH_AUTHENTICATION_RESPONSE = 496;
    public static int AUTHENTICATION_RESPONSE = 547;
    public static int PROXIES_RESPONSE = 564;
    public static int AUTHENTICATION_TOKEN_RESPONSE = 595;
    public static int WORLD_SELECTION_RESPONSE = 579;
    public static int SERVER_TIME = 17611;
    public static int CHARACTERS_LIST = 17794;
    public static int CREATE_CHARACTER_RESPONSE = 0;
}
