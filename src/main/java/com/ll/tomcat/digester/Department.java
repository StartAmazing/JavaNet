package com.ll.tomcat.digester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Department {

    private String name;
    private String code;
    private Map<String, String> extension = new HashMap<>();
    private List<User> users = new ArrayList<User>();

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void putExtension(String name, String code) {
        this.extension.put(name, code);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", extension=" + extension +
                ", users=" + users +
                '}';
    }
}
