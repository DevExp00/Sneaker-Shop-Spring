package com.marketplace.demo.beans;

public class FirstBean {
    private String name ;
    private int age ;
    public FirstBean(){
        System.out.println("First Bean");
        this.name = "no name";
        this.age = 0;
    }

    public FirstBean(String name, int age){
        System.out.println("Using parametrized constructor FirstBean");
        this.name = name;
        this.age = age;
    }

    public String getText(){
        return this.name + " " + this.age + "years old";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
