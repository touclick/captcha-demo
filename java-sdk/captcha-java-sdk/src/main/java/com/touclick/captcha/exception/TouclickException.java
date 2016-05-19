package com.touclick.captcha.exception;

public class TouclickException extends Exception {

    private static final long serialVersionUID = 1L;

    public TouclickException(String msg, Throwable e) {
        super(msg, e);
    }

    public TouclickException(String msg) {
        super(msg);
    }

    public TouclickException(Throwable e) {
        super(e);
    }


}
