package com.slightsite.tutorialloginsederhana.models;

import java.util.HashMap;
import java.util.Map;

public class Admin {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private int status;

    /**
     * Static value for UNDEFINED ID.
     */
    public static final int UNDEFINED_ID = -1;


    public Admin(int id, String name, String email, String password, String phone, int status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.status = status;
    }

    public Admin(String name, String email, String password, String phone, int status) {
        this(UNDEFINED_ID, name, email, password, phone, status);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String setPhone() {
        return phone;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id + "");
        map.put("name", name);
        map.put("email", email);
        map.put("password", ""+ password);
        map.put("phone", phone);
        map.put("status", status + "");

        return map;

    }
}
