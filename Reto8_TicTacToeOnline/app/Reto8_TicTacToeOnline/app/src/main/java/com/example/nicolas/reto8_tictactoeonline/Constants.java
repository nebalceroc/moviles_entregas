package com.example.nicolas.reto8_tictactoeonline;

public class Constants {

    public static final String BROADCAST_CHAT_MESSAGE_NOTIFICATION = "chat_message";
    public static final String BROADCAST_APPOINTMENT_STATUS_UPDATE_NOTIFICATION = "appointment_status_update";
    public static final String BROADCAST_DEFAULT_NOTIFICATION = "DEFAULT";
    public static final String JOIN_GAME_NOTIFICATION = "JOIN";
    public static final String ACTION_GAME_NOTIFICATION = "ACTION";


    public static Boolean isStopped;
    public static RequestSingleton mRequestQueue;

    public static final String API_URL = "http://192.168.0.8:8085/";
    public static final String API_TEST_URL = "http://162.208.9.185:8085/api/";

    //public static final String API_URL = "http://192.168.1.104:8081/";
    //public static final String API_TEST_URL = "http://192.168.1.104:8081/api_test/";

    //public static final String API_URL = "http://192.168.43.237:8081/";
    //public static final String API_TEST_URL = "http://192.168.43.237:8081/api_test/";


    public static final String API_URL_GAME_VIEWSET = API_URL + "api/games/";
    public static final String API_URL_GAME_ENTER_VIEWSET = API_URL + "api/games/join_game/";
    public static final String API_URL_GAME_SET_MOVE_VIEWSET = API_URL + "api/games/set_move/";

    public static final int LOGOUT = -1;
    public static final int LOGIN_REQUEST = 0;
    public static final int USER_JOINED = 1;
    public static final int LOGIN_ERROR = 2;
    public static final int START_TTT_ACTIVITY = 3;
    public static final int START_TTT_ACTIVITY_JOIN = 4;
    public static final int USER_REGISTER_ERROR = 5;
    public static final int USER_MAIN = 6;
    public static final int APPOINTMENT_CREATION_REQUEST = 10;
    public static final int APPOINTMENT_CREATION_SUCCESS = 11;
    public static final int APPOINTMENT_CREATION_ERROR = 12;
    public static final int APPOINTMENT_ACTIVITY_REQUEST = 13;
    public static final int APPOINTMENT_ACTIVITY_SUCCESS = 14;
    public static final int APPOINTMENT_ACTIVITY_ERROR = 15;
    public static final int GPS_CONFIG_REQUEST = 16;
    public static final int DOCTOR_DOCUMENTS_ACTIVITY_REQUEST = 17;
    public static final int DIPLOMA_STORAGE_ACCESS_REQUEST = 18;
    public static final int DEGREE_STORAGE_ACCESS_REQUEST = 19;
    public static final int SOCIAL_WORK_STORAGE_ACCESS_REQUEST = 20;
    public static final int RETHUS_STORAGE_ACCESS_REQUEST = 21;
    public static final int CMC_ID_STORAGE_ACCESS_REQUEST = 22;
    public static final int POLICY_STORAGE_ACCESS_REQUEST = 23;
    public static final int CHAT_REQUEST = 24;
    public static final int GENERATE_PQR_REQUEST = 25;
    public static final int DOCUMENT_UPLOAD_ACTIVITY_REQUEST = 26;
    public static final int ICD_SEARCH_REQUEST = 27;
    public static final int PROFILE_IMG_STORAGE_ACCESS_REQUEST = 28;


    public static final String N_APPOINTMENT_STATUS_CHANGE = "STATUS_CHANGE";

    public static int CHAT_MESSAGE_NOTIFICATION = 1;
    public static int APPOINTMENT_STATUS_UPDATE_NOTIFICATION = 2;


}