package com.awg.jwglxt.student.attendance.pojo;

import java.io.Serializable;

public class Teacher implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer teacherId;
    private String tName;
    private Integer tGender;
    private String tTel;
    private String address;
    private Integer type;
    private Grade grade;
    
    public Teacher(Integer teacherId, String tName, Integer tGender, String tTel, String address, Integer type, Grade grade) {
        super();
        this.teacherId = teacherId;
        this.tName = tName;
        this.tGender = tGender;
        this.tTel = tTel;
        this.address = address;
        this.type = type;
        this.grade = grade;
    }
    
    public Teacher() {}
    
    public Teacher(Integer teacherId) {
        super();
        this.teacherId = teacherId;
    }

    public Teacher(String tName) {
        super();
        this.tName = tName;
    }
    
    public Teacher(String tName, Integer tGender, String tTel, String address) {
        super();
        this.tName = tName;
        this.tGender = tGender;
        this.tTel = tTel;
        this.address = address;
    }
    
    public Integer getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
    
    public String gettName() {
        return tName;
    }
    
    public void settName(String tName) {
        this.tName = tName;
    }
    
    public Integer gettGender() {
        return tGender;
    }
    public void settGender(Integer tGender) {
        this.tGender = tGender;
    }
    
    public String gettTel() {
        return tTel;
    }
    
    public void settTel(String tTel) {
        this.tTel = tTel;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public Grade getGrade() {
        return grade;
    }
    
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    
    @Override
    public String toString() {
        return "Teacher [teacherId=" + teacherId + ", tName=" + tName + ", tGender=" + tGender + ", tTel=" + tTel
                + ", address=" + address + ", type=" + type + ", grade=" + grade + "]";
    }

}
