package com.awg.jwglxt.student.attendance.service;

import java.sql.SQLException;
import java.util.List;
import com.awg.jwglxt.student.attendance.pojo.Grade;
import com.awg.jwglxt.student.attendance.pojo.Student;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendance;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendanceType;

/**
 * 学生考勤服务接口
 * @author AWG
 *
 */
public interface StudentAttendaceService {
    
    /**
     * 查询所有班级
     * @return 所有班级组成的List集合
     */
    List<Grade> findAllGrades() throws Exception;
    
    /**
     * 根据班级名称查询该班级下的所有的学生
     * @param gradeName 班级名称
     * @return 该班级下的所有的学生组成的List集合
     * @throws Exception 异常
     */
    List<Student> findAllStudentsByGradeName(String gradeName) throws Exception;

    /**
     * 查询学生考勤的所有类型
     * @return 学生考勤的所有类型
     * @throws Exception 异常
     */
    List<StudentAttendanceType> findAllStudentAttendanceTypes() throws Exception;
    
    /**
     * 添加学生考勤记录
     * @param attendanceType 考勤类型的数字代码
     * @param sutdentId 学生ID
     * @param actualStartTimeStr 实际开始时间
     * @param actualEndTimeStr 实际结束时间
     * @param attendanceDescription 考勤状态描述
     * @return 受影响的行数
     * @throws Exception 异常
     */
    int addStudentAttendance(String attendanceType, String sutdentId, String actualStartTimeStr, String actualEndTimeStr, String attendanceDescription) throws Exception;
    
    /**
     * 分页查看所有学生的考勤记录
     * @param pageSize 每页现实的最大记录数
     * @param currentPage 当前页，即要显示的指定页的数据
     * @return 包含多个学生考勤记录的List集合
     */
    List<StudentAttendance> findAllStudentAttendances(Integer pageSize, Integer currentPage) throws Exception;
    
    /**
     * 获取学生考勤记录的总记录数
     * @return 学生考勤记录的总记录数
     */
    int getAllCountOfStudentAttendace() throws SQLException;
    
    /**
     * 根据指定的考勤流水号来查询考勤记录
     * @param AttendanceId 考勤流水号
     * @return 指定的考勤流水号对应的考勤记录
     * @throws Exception 异常
     */
    StudentAttendance getStudentAttendanceByAttendanceId(String AttendanceId) throws Exception;
    
    /**
     * 修改指定的学生考勤记录
     * @param attendanceId 学生考勤记录的流水号
     * @param attendanceType 考勤类型的数字代码
     * @param sutdentId 学生ID
     * @param actualStartTimeStr 实际开始时间
     * @param actualEndTimeStr 实际结束时间
     * @param attendanceDescription 考勤状态描述
     * @return 受影响的行数
     * @throws Exception 异常
     */
    int updateStudentAttendance(String attendanceId, String attendanceType, String sutdentId, String actualStartTimeStr, String actualEndTimeStr, String attendanceDescription) throws Exception;
    
    /**
     * 删除指定的学生考勤记录
     * @param attendanceId 学生考勤记录的流水号
     * @return 受影响的行数
     * @throws Exception 异常
     */
    int delStudentAttendance(String attendanceId) throws Exception;
    
    /**
     * 删批量除指定的学生考勤记录
     * @param attendanceIds 学生考勤记录的流水号组成的字符串数组
     * @return 受影响的行数
     * @throws Exception 异常
     */
    int delStudentAttendanceByBatch(String[] attendanceIds) throws Exception;
}
