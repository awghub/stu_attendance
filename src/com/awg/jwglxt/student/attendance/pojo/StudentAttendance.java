package com.awg.jwglxt.student.attendance.pojo;

import java.io.Serializable;
import java.util.Date;
/**
 * 学生考勤实体类
 * @author AWG
 *
 */
public class StudentAttendance implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // 学生考勤的ID
    private String attendanceId;
    
    // 学生实体
    private Student student;
    
    // 学生考勤类型实体
    private StudentAttendanceType studentAttendanceType;
    
    // 学生考勤的默认开始时间
    private Date attendanceDefaultStartTime;
    
    // 学生考勤的默认结束时间
    private Date attendanceDefaultEndTime;
    
    // 学生考勤的实际开始时间
    private Date attendanceActualStartTime;
    
    // 学生考勤的实际结束时间
    private Date attendanceActualEndTime;
    
    // 考勤类型对应的时长
    private long attendanceActualTimeLength;
    
    // 考勤类型描述或说明
    private String attendanceDescription;
    
    // 考勤记录的状态
    private Integer attendanceStatus;

    public StudentAttendance() {
        super();
    }

    public StudentAttendance(Date attendanceDefaultStartTime, Date attendanceDefaultEndTime) {
        super();
        this.attendanceDefaultStartTime = attendanceDefaultStartTime;
        this.attendanceDefaultEndTime = attendanceDefaultEndTime;
    }

    public StudentAttendance(String attendanceId, Student student, StudentAttendanceType studentAttendanceType,
            Date attendanceActualStartTime, Date attendanceActualEndTime, long attendanceActualTimeLength, String attendanceDescription,
            Integer attendanceStatus) {
        super();
        this.attendanceId = attendanceId;
        this.student = student;
        this.studentAttendanceType = studentAttendanceType;
        this.attendanceActualStartTime = attendanceActualStartTime;
        this.attendanceActualEndTime = attendanceActualEndTime;
        this.attendanceActualTimeLength = attendanceActualTimeLength;
        this.attendanceDescription = attendanceDescription;
        this.attendanceStatus = attendanceStatus;
    }

    public StudentAttendance(String attendanceId, Student student, StudentAttendanceType studentAttendanceType,
            Date attendanceDefaultStartTime, Date attendanceDefaultEndTime, Date attendanceActualStartTime,
            Date attendanceActualEndTime, long attendanceActualTimeLength, String attendanceDescription) {
        super();
        this.attendanceId = attendanceId;
        this.student = student;
        this.studentAttendanceType = studentAttendanceType;
        this.attendanceDefaultStartTime = attendanceDefaultStartTime;
        this.attendanceDefaultEndTime = attendanceDefaultEndTime;
        this.attendanceActualStartTime = attendanceActualStartTime;
        this.attendanceActualEndTime = attendanceActualEndTime;
        this.attendanceActualTimeLength = attendanceActualTimeLength;
        this.attendanceDescription = attendanceDescription;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentAttendanceType getStudentAttendanceType() {
        return studentAttendanceType;
    }

    public void setStudentAttendanceType(StudentAttendanceType studentAttendanceType) {
        this.studentAttendanceType = studentAttendanceType;
    }

    public Date getAttendanceDefaultStartTime() {
        return attendanceDefaultStartTime;
    }

    public void setAttendanceDefaultStartTime(Date attendanceDefaultStartTime) {
        this.attendanceDefaultStartTime = attendanceDefaultStartTime;
    }

    public Date getAttendanceDefaultEndTime() {
        return attendanceDefaultEndTime;
    }

    public void setAttendanceDefaultEndTime(Date attendanceDefaultEndTime) {
        this.attendanceDefaultEndTime = attendanceDefaultEndTime;
    }

    public Date getAttendanceActualStartTime() {
        return attendanceActualStartTime;
    }

    public void setAttendanceActualStartTime(Date attendanceActualStartTime) {
        this.attendanceActualStartTime = attendanceActualStartTime;
    }

    public Date getAttendanceActualEndTime() {
        return attendanceActualEndTime;
    }

    public void setAttendanceActualEndTime(Date attendanceActualEndTime) {
        this.attendanceActualEndTime = attendanceActualEndTime;
    }

    public long getAttendanceActualTimeLength() {
        return attendanceActualTimeLength;
    }

    public void setAttendanceActualTimeLength(long attendanceActualTimeLength) {
        this.attendanceActualTimeLength = attendanceActualTimeLength;
    }

    public String getAttendanceDescription() {
        return attendanceDescription;
    }

    public void setAttendanceDescription(String attendanceDescription) {
        this.attendanceDescription = attendanceDescription;
    }

    public Integer getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(Integer attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "StudentAttendance [attendanceId=" + attendanceId + ", student=" + student + ", studentAttendanceType="
                + studentAttendanceType + ", attendanceDefaultStartTime=" + attendanceDefaultStartTime
                + ", attendanceDefaultEndTime=" + attendanceDefaultEndTime + ", attendanceActualStartTime="
                + attendanceActualStartTime + ", attendanceActualEndTime=" + attendanceActualEndTime
                + ", attendanceActualTimeLength=" + attendanceActualTimeLength + ", attendanceDescription="
                + attendanceDescription + ", attendanceStatus=" + attendanceStatus + "]";
    }

}
