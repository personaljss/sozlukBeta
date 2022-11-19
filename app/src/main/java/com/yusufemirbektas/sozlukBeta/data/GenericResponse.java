package com.yusufemirbektas.sozlukBeta.data;

public class GenericResponse<T> {
    //constants for http
    public static final int SERVER_FAILED=-5;
    public static final int CONNECTION_FAILED=-4;
    public static final int SUCCESS=0;
    public static final int TOKEN_AVAILABLE=10;
    //attributes
    public int httpStatus;
    public T response;
}
