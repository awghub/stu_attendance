package com.awg.jwglxt.student.attendance.constant;
/**
 * 学生考勤常量类
 * @author AWG
 *
 */
public interface StudentAttendanceConstant {
    
    /**
     * 学生考勤记录的状态--正常(0)
     */
    Integer STUDENT_ATTENDANCE_STATUS_NORMAL = 0;
    
    /**
     * 学生考勤记录的状态--已删除(1)
     */
    Integer STUDENT_ATTENDANCE_STATUS_DELETED = 1;
    
    /**
     * 考勤类型--正常(0)
     */
    Integer STUDENT_ATTENDANCE_TYPE_NORMAL = 0;
    
    /**
     * 考勤类型--请假(1)
     */
    Integer STUDENT_ATTENDANCE_TYPE_HOLIDAY = 1;
    
    /**
     * 考勤类型--迟到(2)
     */
    Integer STUDENT_ATTENDANCE_TYPE_LATE = 2;
    
    /**
     * 考勤类型--早退(3)
     */
    Integer STUDENT_ATTENDANCE_TYPE_LEAVE_EARLY = 3;
    
    /**
     * 考勤类型--旷课(4)
     */
    Integer STUDENT_ATTENDANCE_TYPE_TRUANCY = 4;
    
    /**
     * 考勤类型--其他(5)
     */
    Integer STUDENT_ATTENDANCE_TYPE_OTHER = 5;
    
    /**
     * 数据表格分页大小，每页显示的记录数
     */
    Integer PAGE_SIZE = 5;
    
    /**
     * 数据表格的初始页码,第1页
     */
    Integer DAFAULT_PAGE = 1;
    
    /**
     * 数据表格分页的正则表达式
     */
    String PAGE_REGX = "^[1-9][0-9]*$";
    
    /**
     * 考勤流水号的格式的正则表达式
     */
    String STUDENT_ATTENDANCE_ID_REGEX = "^(20[1-9][0-9])((0[1-9])|(1[0-2]))((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))((20|21|22|23|[0-1][0-9])[0-5][0-9][0-5][0-9])_[1-9][0-9]*$";

}
