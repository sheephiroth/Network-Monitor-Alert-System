package com.example.project;

public class Maps {

    public String mapid;
    public String mapname;

    public Maps() {

    }

    public Maps(String mapid, String mapname) {
        this.mapid = mapid;
        this.mapname = mapname;
    }

    public String getMapid() {
        return mapid;
    }
    public void setMapid(String mapid) {
        this.mapid = mapid;
    }

    public String getMapname() {
        return mapname;
    }
    public void setMapname(String mapname) {
        this.mapname = mapname;
    }
}
