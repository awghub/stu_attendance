package com.awg.jwglxt.student.attendance.service;

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
    int addStudentAttendance(String teacherName, String attendanceType, String sutdentId, String actualStartTimeStr, String actualEndTimeStr, String attendanceDescription) throws Exception;
    
    /**
     * 分页查看所有学生的考勤记录
     * @param teacherName 教师名称（应该为teacherId）
     * @param pageSize 每页现实的最大记录数
     * @param currentPage 当前页，即要显示的指定页的数据
     * @return 包含多个学生考勤记录的List集合
     */
    List<StudentAttendance> findAllStudentAttendances(String teacherName, Integer pageSize, Integer currentPage) throws Exception;
    
    /**
     * 查询某个教师下所有的学生的考勤记录
     * @param teacherId 教师ID
     * @return 包含多个学生考勤记录的List集合
     * @throws Exception 异常
     */
    List<StudentAttendance> findAllStudentAttendancesByTeacherId(Integer teacherId) throws Exception;
    
    /**
     * 分页查询某个教师下所有的学生的考勤记录(附带指定条件)
     * @param teacherId 教师的ID
     * @param stuId 学生的ID
     * @param dateMin 查询指定日期范围的起始范围
     * @param dateMax 查询指定日期范围的结束范围
     * @param pageSize 每页的数据量
     * @param currentPage 当前页
     * @return 包含多个学生考勤记录的List集合
     * @throws Exception 异常
     */
    List<StudentAttendance> findAllStudentAttendanceByConditions(Integer teacherId, String stuId, String dateMin, String dateMax, Integer pageSize, Integer currentPage) throws Exception;
    
    /**
     * 获取勤记录的总记录数<br/>
     * 1.若教师名字为空且状态码为空，则查询总的所有考勤记录总数;<br/>
     * 2.若教师名字为空且状态码不为空，则根据状态码查询考勤记录总数: <br/>
     *      &nbsp;&nbsp;&nbsp;&nbsp;2.1若状态码为空,则查询该教师下所有的考勤记录总数;<br/>
     *      &nbsp;&nbsp;&nbsp;&nbsp;2.2若状态码不为空,则查询该教师下指定状态码的考勤记录总数;
     * @param teaacherId 教师的名字
     * @param studentAttendanceStatus 学生考勤记录的状态码(正常-0,已删除-1),若为null则查询所有
     * @return 学生考勤记录的总记录数
     */
    int getAllCountOfStudentAttendace(String teacherName, Integer studentAttendanceStatus) throws Exception;
    
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
