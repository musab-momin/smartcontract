package com.smartcontract.smartcontract.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;


@Entity
public class Contact 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    @Column(length = 20)
    @NotBlank(message = "This field can't be empty...")
    private String c_name;
    @Column(unique = true)
    private String c_email;
    @Column(length = 11)
    private String mobile;
    private String image;
    private String about;
    @ManyToOne
    private User user;

    public Contact() {
    }

    public Contact(int cid, String c_name, String c_email, String mobile, String image, String about, User user) {
        this.cid = cid;
        this.c_name = c_name;
        this.c_email = c_email;
        this.mobile = mobile;
        this.image = image;
        this.about = about;
        this.user = user;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
}
