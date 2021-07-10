package com.xinxian.shop.Model;

public class addCP {
    private String id,NameEn,NameBn;

    public addCP(){}

    public addCP(String id, String nameEn, String nameBn) {
        this.id = id;
        NameEn = nameEn;
        NameBn = nameBn;
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
