package com.example.project;

public class Datas {
    public String dataname;
    public String lastvalue;

    public String unit;
    public String lastcheck;

    public Datas(){

    }

    public Datas(String dataname, String lastvalue, String unit, String lastcheck ){
        this.dataname = dataname;
        this.lastcheck = lastcheck;
        this.unit = unit;
        this.lastvalue = lastvalue;
    }
    public String getDataname(){return dataname;}
    public void setDataname(String dataname){this.dataname = dataname;}
    public String getLastvalue(){return lastvalue;}
    public void setLastvalue(String lastvalue){this.lastvalue = lastvalue;}

    public String getUnit(){return unit;}
    public void setUnit(String unit){this.unit = unit;}
    public String getLastcheck(){return lastcheck;}
    public void setLastcheck(String lastcheck){this.lastcheck = lastcheck;}
}
