package com.example.pojo;

import java.util.Date;


public class DailyNum {

    private String dataName;
    private String dataWeek;
    private int totalNum;
    private  int orderNum;

    public DailyNum() {
    }

    public DailyNum(String dataName, String dataWeek, int totalNum, int orderNum) {
        this.dataName = dataName;
        this.dataWeek = dataWeek;
        this.totalNum = totalNum;
        this.orderNum = orderNum;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataWeek() {
        return dataWeek;
    }

    public void setDataWeek(String dataWeek) {
        this.dataWeek = dataWeek;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "DailyNum{" +
                "dataName=" + dataName +
                ", dataWeek='" + dataWeek + '\'' +
                ", totalNum=" + totalNum +
                ", orderNum=" + orderNum +
                '}';
    }
}
