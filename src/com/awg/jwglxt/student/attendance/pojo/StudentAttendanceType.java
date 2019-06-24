package com.awg.jwglxt.student.attendance.pojo;

import java.io.Serializable;
/**
 * 学生考勤类型实体类
 * @author AWG
 *
 */
public class StudentAttendanceType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // 学生考勤类型ID
    private Integer studentAttendanceTypeId;
    
    // 学生考勤类型名称
    private String studentAttendanceTypeName;

    public StudentAttendanceType() {
        super();
    }

    public StudentAttendanceType(Integer studentAttendanceTypeId) {
        super();
        this.studentAttendanceTypeId = studentAttendanceTypeId;
    }

    public StudentAttendanceType(Integer studentAttendanceTypeId, String studentAttendanceTypeName) {
        super();
        this.studentAttendanceTypeId = studentAttendanceTypeId;
        this.studentAttendanceTypeName = studentAttendanceTypeName;
    }

    public Integer getStudentAttendanceTypeId() {
        return studentAttendanceTypeId;
    }

    public void setStudentAttendanceTypeId(Integer studentAttendanceTypeId) {
        this.studentAttendanceTypeId = studentAttendanceTypeId;
    }

    public String getStudentAttendanceTypeName() {
        return studentAttendanceTypeName;
    }

    public void setStudentAttendanceTypeName(String studentAttendanceTypeName) {
        this.studentAttendanceTypeName = studentAttendanceTypeName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "StudentAttendanceType [studentAttendanceTypeId=" + studentAttendanceTypeId
                + ", studentAttendanceTypeName=" + studentAttendanceTypeName + "]";
    }

}
