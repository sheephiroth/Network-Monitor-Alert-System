package com.example.project;


public class Servers {
    public int id;
    public String S_name;
    public String S_url;
    public String S_user;
    public String S_pass;

    public Servers(){
    }
    public Servers(String S_name, String S_url, String S_user, String S_pass){
        this.S_name= S_name;
        this.S_url = S_url;
        this.S_user = S_user;
        this.S_pass = S_pass;
    }
    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public String getS_name(){return S_name;}
    public void setS_name(String S_name){this.S_name = S_name;}
    public String getS_url(){return S_url;}
    public void setS_url(String S_url){this.S_url = S_url;}
    public String getS_user(){return S_user;}
    public void setS_user(String S_user){this.S_user = S_user;}
    public String getS_pass(){return S_pass;}
    public void setS_pass(String S_pass){this.S_pass = S_pass;}
}