package com.lemon.api.auto10.pojo;

public class ParamVariable {
    private String name;
    private String value;
    private  String remarks;
    private String reflectClass;
    private  String reflectMethod;
    private  String reflectValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public String getReflectClass() {
        return reflectClass;
    }

    public void setReflectClass(String reflectClass) {
        this.reflectClass = reflectClass;
    }

    public String getReflectMethod() {
        return reflectMethod;
    }

    public void setReflectMethod(String reflectMethod) {
        this.reflectMethod = reflectMethod;
    }

    public String getReflectValue() {
        return reflectValue;
    }

    public void setReflectValue(String reflectValue) {
        this.reflectValue = reflectValue;
    }

    @Override
    public String toString() {
        return "name"+this.name+",value="+this.value+",remarks="+remarks+",reflectClass="+this.reflectClass+",reflectMethod="+this.reflectMethod+",reflectValue="+this.reflectValue;
    }
}
