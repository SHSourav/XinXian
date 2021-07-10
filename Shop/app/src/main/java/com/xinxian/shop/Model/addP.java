package com.xinxian.shop.Model;

public class addP {
    private String cName,id,NameEn,NameBn;

    public addP() {
    }

    public addP(String cName, String id, String nameEn, String nameBn) {
        this.cName = cName;
        this.id = id;
        NameEn = nameEn;
        NameBn = nameBn;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public String getNameBn() {
        return NameBn;
    }

    public void setNameBn(String nameBn) {
        NameBn = nameBn;
    }
}
