package com.xinxian.shop.Model;

public class Cart {

    private  String iid,iname,iprice,idate,itime,iquantity,iimage,inumber;
    public Cart(){

    }

    public Cart(String iid, String iname, String iprice, String idate, String itime, String iquantity, String iimage, String inumber) {
        this.iid = iid;
        this.iname = iname;
        this.iprice = iprice;
        this.idate = idate;
        this.itime = itime;
        this.iquantity = iquantity;
        this.iimage = iimage;
        this.inumber = inumber;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getIprice() {
        return iprice;
    }

    public void setIprice(String iprice) {
        this.iprice = iprice;
    }

    public String getIdate() {
        return idate;
    }

    public void setIdate(String idate) {
        this.idate = idate;
    }

    public String getItime() {
        return itime;
    }

    public void setItime(String itime) {
        this.itime = itime;
    }

    public String getIquantity() {
        return iquantity;
    }

    public void setIquantity(String iquantity) {
        this.iquantity = iquantity;
    }

    public String getIimage() {
        return iimage;
    }

    public void setIimage(String iimage) {
        this.iimage = iimage;
    }

    public String getInumber() {
        return inumber;
    }

    public void setInumber(String inumber) {
        this.inumber = inumber;
    }
}
