package com.awg.jwglxt.student.attendance.pojo;

import java.io.Serializable;

/**
 * 学生实体类
 * @author AWG
 *
 */
public class Student implements Serializable{

    private static final long serialVersionUID = 1L;
    
    // 学生ID
    private String stuId;
    
    // 学生姓名
    private String stuName;
    
    // 学生性别,暂定0-女,1-男
    private Integer stuGender;
    
    // 学生年龄
    private Integer stuAge;
    
    // 学生电话
    private String stuTel;
    
    // 学生籍贯
    private String stuWhere;
    
    // 学生状态,暂定0-正常,1-退学,2-开除
    private Integer stuType;
    
    // 学生班级名称
    private String gradeName;
    
    // 学生所在班级的老师的姓名
    private String teacherName;

    public Student() {
        super();
    }

    public Student(String stuId) {
        super();
        this.stuId = stuId;
    }

    public Student(String stuId, String stuName) {
        super();
        this.stuId = stuId;
        this.stuName = stuName;
    }

    public Student(String stuId, String stuName, String gradeName) {
        super();
        this.stuId = stuId;
        this.stuName = stuName;
        this.gradeName = gradeName;
    }

    public Student(String stuId, String stuName, Integer stuGender, Integer stuAge, String stuTel, String stuWhere,
            Integer stuType, String gradeName, String teacherName) {
        super();
        this.stuId = stuId;
        this.stuName = stuName;
        this.stuGender = stuGender;
        this.stuAge = stuAge;
        this.stuTel = stuTel;
        this.stuWhere = stuWhere;
        this.stuType = stuType;
        this.gradeName = gradeName;
        this.teacherName = teacherName;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Integer getStuGender() {
        return stuGender;
    }

    public void setStuGender(Integer stuGender) {
        this.stuGender = stuGender;
    }

    public Integer getStuAge() {
        return stuAge;
    }

    public void setStuAge(Integer stuAge) {
        this.stuAge = stuAge;
    }

    public String getStuTel() {
        return stuTel;
    }

    public void setStuTel(String stuTel) {
        this.stuTel = stuTel;
    }

    public String getStuWhere() {
        return stuWhere;
    }

    public void setStuWhere(String stuWhere) {
        this.stuWhere = stuWhere;
    }

    public Integer getStuType() {
        return stuType;
    }

    public void setStuType(Integer stuType) {
        this.stuType = stuType;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Student [stuId=" + stuId + ", stuName=" + stuName + ", stuGender=" + stuGender + ", stuAge=" + stuAge
                + ", stuTel=" + stuTel + ", stuWhere=" + stuWhere + ", stuType=" + stuType + ", gradeName=" + gradeName
                + ", teacherName=" + teacherName + "]";
    }

}
