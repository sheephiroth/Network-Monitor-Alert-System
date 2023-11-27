package com.example.project;

public class Historys {
    public String hostname;
    public String problem;
    public String detail;
    public String severity;
    public String status;
    public String time;

    public Historys() {

    }

    public Historys(String hostname, String problem, String detail, String severity, String time, String status) {
        this.hostname = hostname;
        this.problem = problem;
        this.detail = detail;
        this.severity = severity;
        this.time = time;
        this.status = status;
    }


    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSeverity() { return severity; }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
