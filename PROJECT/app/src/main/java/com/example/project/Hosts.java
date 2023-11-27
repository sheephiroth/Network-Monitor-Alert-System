package com.example.project;

public class Hosts {
    public String host_id;
    public String hostname;
    public String ip;
    public String type;
    public String status;
    public String template;
    public String druleid;
    public String drulename;
    public String dupdown;
    public String hup;
    public String hdown;


    public Hosts() {

    }

    public Hosts(String host_id, String hostname,  String ip,  String status,  String type, String template,String druleid
            , String drulename,String dupdown, String hup,String hdown) {
        this.hostname = hostname;
        this.ip = ip;
        this.status = status;
        this.type = type;
        this.template = template;
        this.druleid = druleid;
        this.drulename = drulename;
        this.dupdown = dupdown;
        this.hup = hup;
        this.hdown = hdown;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDruleid() {
        return druleid;
    }

    public void setDruleid(String druleid) {
        this.druleid = druleid;
    }

    public String getDrulename() {
        return drulename;
    }
    public void setDrulename(String drulename) {
        this.drulename = drulename;
    }

    public String getDupdown() {
        return dupdown;
    }
    public void setDupdown(String dupdown) {
        this.dupdown = dupdown;
    }

    public String getHup() {
        return hup;
    }
    public void setHup(String hup) {
        this.hup = hup;
    }

    public String getHdown() {
        return hdown;
    }
    public void setHdown(String hdown) {
        this.hdown = hdown;
    }
}