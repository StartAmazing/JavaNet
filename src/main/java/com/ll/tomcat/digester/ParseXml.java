package com.ll.tomcat.digester;

import org.apache.commons.digester3.Digester;

import java.io.File;

public class ParseXml {
    public static void main(String[] args) {
        Digester digester = new Digester();
        digester.setValidating(false);
//        digester.setRulesValidation(true);
        // 匹配department节点时，创建Department对象
        digester.addObjectCreate("department", Department.class);
        // 匹配department节点时，设置对象属性
        digester.addSetProperties("department");
        // 匹配到department/user时创建User对象
        digester.addObjectCreate("department/user", User.class);
        // 匹配到department/user时设置对象属性
        digester.addSetProperties("department/user");
        // 匹配到department/user时，调用department的addUser方法
        digester.addSetNext("department/user", "addUser");
        // 匹配到department/extension时，调用department的putExtension()方法
        digester.addCallMethod("department/extension", "putExtension", 2);
        // 调用方法的第一个参数为节点department/extension/property-name的内容
        digester.addCallParam("department/extension/property-name", 0);
        // 调用方法的第二个参数为节点department/extension/property-value的内容
        digester.addCallParam("department/extension/property-value", 1);

        try {
            Department department = (Department) digester.parse(new File("test.xml"));
            System.out.println(department.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
