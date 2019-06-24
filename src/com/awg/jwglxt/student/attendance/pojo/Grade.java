package com.awg.jwglxt.student.attendance.pojo;

import java.io.Serializable;
/**
 * 班级实体类
 * @author AWG
 *
 */
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // 班级名称
    private String gradeName;
    
    // 班级现有学生人数
    private Integer nowStudent;
    
    // 班级教师姓名
    private String teacherName;

    public Grade() {
        super();
    }

    public Grade(String gradeName, Integer nowStudent, String teacherName) {
        super();
        this.gradeName = gradeName;
        this.nowStudent = nowStudent;
        this.teacherName = teacherName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getNowStudent() {
        return nowStudent;
    }

    public void setNowStudent(Integer nowStudent) {
        this.nowStudent = nowStudent;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "Grade [gradeName=" + gradeName + ", nowStudent=" + nowStudent + ", teacherName=" + teacherName + "]";
    }

}
