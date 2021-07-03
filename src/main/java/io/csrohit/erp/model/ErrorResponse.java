package io.csrohit.erp.model;

public class ErrorResponse {
    private String msg;

    public ErrorResponse(String msg) {
        this.msg = msg;
    }

    public ErrorResponse() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
