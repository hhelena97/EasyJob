package de.hbrs.easyjob.controllers;

public class DatabasePersonException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DatabasePersonException( String reason ) {
        this.reason = reason;
    }
}
