package com.example.liusai.locationtest.Entity;

/**
 * Created by liusai on 16/5/13.
 * 描述基站结构体
 * MCC 移动国家代码
 * MNC 移动网络代码
 * LAC
 * CID 基站编号
 */
public class StationEntity {
  
    private int MCC;
    private int MNC;
    private int LAC;
    private int CID;

    public StationEntity(){}

    public StationEntity(int MCC, int MNC, int LAC, int CID){
        this.MCC = MCC;
        this.MNC = MNC;
        this.LAC = LAC;
        this.CID = CID;
    }

    public int getMCC() {
        return MCC;
    }

    public void setMCC(int MCC) {
        this.MCC = MCC;
    }

    public int getMNC() {
        return MNC;
    }

    public void setMNC(int MNC) {
        this.MNC = MNC;
    }

    public int getLAC() {
        return LAC;
    }

    public void setLAC(int LAC) {
        this.LAC = LAC;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }
}