package com.xinxian.shop.Model;

public class Users {
    public String email, name, password, phone, image, address,sq;

    public Users() {

    }

    public Users(String email, String name, String password, String phone, String image, String address, String sq) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.sq = sq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSq() {
        return sq;
    }

    public void setSq(String sq) {
        this.sq = sq;
    }
}