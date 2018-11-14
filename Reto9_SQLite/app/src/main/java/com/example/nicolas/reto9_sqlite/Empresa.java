package com.example.nicolas.reto9_sqlite;

public class Empresa {
    private long empId;
    private String name;
    private String url;
    private String number;
    private String email;
    private String pys;
    private Boolean c;
    private Boolean m;
    private Boolean f;

    public Empresa(long empId, String name, String url, String number, String email, String pys, Boolean c, Boolean m, Boolean f){
        this.empId = empId;
        this.name = name;
        this.url = url;
        this.number = number;
        this.email = email;
        this.pys = pys;
        this.c = c;
        this.m = m;
        this.f = f;
    }

    public Empresa(){

    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPys() {
        return pys;
    }

    public void setPys(String pys) {
        this.pys = pys;
    }

    public Boolean getC() {
        return c;
    }

    public void setC(Boolean c) {
        this.c = c;
    }

    public Boolean getM() {
        return m;
    }

    public void setM(Boolean m) {
        this.m = m;
    }

    public Boolean getF() {
        return f;
    }

    public void setF(Boolean f) {
        this.f = f;
    }


    public String toString(){
        return "Emp id: "+getEmpId()+ "\n" +
                "Name: "+getName() +"\n" +
                "Url: "+getUrl() + "\n" +
                "Number : "+getNumber() + "\n" +
                "Email : "+getEmail() + "\n" +
                "P&S : "+getPys() + "\n" +
                "C : "+getC().toString() + "\n" +
                "M : "+getM().toString() + "\n" +
                "F : "+getF().toString() + "\n";
    }
}