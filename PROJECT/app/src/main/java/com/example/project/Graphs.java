package com.example.project;

public class Graphs {
    public String graphid;
    public String graphname;
    public String hostids;
    public String color;
    public String graphtype;
    public String drawtype;
    public String yaxisside;
    public String sortorder;
    public String calc_fnc;
    public String value;
    public String unit;
    public String name;
    public String last;
    public String min;
    public String avg;
    public String max;
    public String itemid;

    public Graphs(){

    }

    public Graphs(String graphid, String graphname, String hostids, String color, String graphtype, String drawtype, String yaxisside, String sortorder, String calc_fnc, String value, String unit, String name, String last, String min, String avg, String max, String itemid){ // Modified constructor to include itemid
        this.graphid = graphid;
        this.graphname = graphname;
        this.hostids = hostids;
        this.color = color;
        this.graphtype = graphtype;
        this.drawtype = drawtype;
        this.yaxisside = yaxisside;
        this.sortorder = sortorder;
        this.calc_fnc = calc_fnc;
        this.value = value;
        this.unit = unit;
        this.name = name;
        this.last = last;
        this.min = min;
        this.avg = avg;
        this.max = max;
        this.itemid = itemid;
    }
    public String getGraphid() {
        return graphid;
    }
    public void setGraphid(String graphid) {
        this.graphid = graphid;
    }

    public String getGraphname() {
        return graphname;
    }
    public void setGraphname(String graphname) {
        this.graphname = graphname;
    }

    public String getHostids() {
        return hostids;
    }
    public void setHostids(String hostids) {
        this.hostids = hostids;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getGraphtype() {
        return graphtype;
    }
    public void setGraphtype(String graphtype) {
        this.graphtype = graphtype;
    }

    public String getDrawtype() {
        return drawtype;
    }
    public void setDrawtype(String drawtype) {
        this.drawtype = drawtype;
    }

    public String getYaxisside() {
        return yaxisside;
    }
    public void setYaxisside(String yaxisside) {
        this.yaxisside = yaxisside;
    }

    public String getSortorder() {
        return sortorder;
    }
    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public String getCalc_fnc() {
        return calc_fnc;
    }
    public void setCalc_fnc(String calc_fnc) {
        this.calc_fnc = calc_fnc;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLast() {
        return last;
    }
    public void setLast(String last) {
        this.last = last;
    }

    public String getMin() {
        return min;
    }
    public void setMin(String min) {
        this.min = min;
    }

    public String getAvg() {
        return avg;
    }
    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMax() {
        return max;
    }
    public void setMax(String max) {
        this.max = max;
    }

    public String getItemid() {
        return itemid;
    }
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }


}
