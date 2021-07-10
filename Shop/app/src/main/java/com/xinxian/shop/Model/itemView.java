package com.xinxian.shop.Model;

public class itemView {

    private String cid,pid,iid,iimage,iname,iquantity,iuom,iprice,idiscount,iinStock;

    public itemView() {
    }

    public itemView(String cid, String pid, String iid, String iimage,
                    String iname, String iquantity, String iuom, String iprice, String idiscount, String iinStock) {
        this.cid = cid;
        this.pid = pid;
        this.iid = iid;
        this.iimage = iimage;
        this.iname = iname;
        this.iquantity = iquantity;
        this.iuom = iuom;
        this.iprice = iprice;
        this.idiscount = idiscount;
        this.iinStock = iinStock;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getIimage() {
        return iimage;
    }

    public void setIimage(String iimage) {
        this.iimage = iimage;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getIquantity() {
        return iquantity;
    }

    public void setIquantity(String iquantity) {
        this.iquantity = iquantity;
    }

    public String getIuom() {
        return iuom;
    }

    public void setIuom(String iuom) {
        this.iuom = iuom;
    }

    public String getIprice() {
        return iprice;
    }

    public void setIprice(String iprice) {
        this.iprice = iprice;
    }

    public String getIdiscount() {
        return idiscount;
    }

    public void setIdiscount(String idiscount) {
        this.idiscount = idiscount;
    }

    public String getIinStock() {
        return iinStock;
    }

    public void setIinStock(String iinStock) {
        this.iinStock = iinStock;
    }
}
