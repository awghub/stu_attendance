package com.awg.jwglxt.student.attendance.dao;

import java.sql.SQLException;
import java.util.List;
import com.awg.jwglxt.student.attendance.pojo.Grade;
import com.awg.jwglxt.student.attendance.pojo.Student;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendance;

/**
 * 学生数据访问接口
 * @author AWG
 *
 */
import com.awg.jwglxt.student.attendance.pojo.StudentAttendanceType;
public interface StudentAttendaceDao {

    /**
     * 查询所有班级
     * @return 所有班级组成的List集合
     * @exception 异常
     */
    List<Grade> selectAllGrades() throws Exception;

    /**
     * 根据班级名称查询该班级下的所有的学生
     * @param gradeName 班级名称
     * @return 该班级下的所有的学生组成的List集合
     * @throws Exception 异常
     */
    List<Student> selectAllStudentsByGradeName(String gradeName) throws Exception;

    /**
     * 查询学生考勤的所有类型
     * @return 学生考勤的所有类型
     * @throws Exception 异常
     */
    List<StudentAttendanceType> selectAllStudentAttendanceTypes() throws Exception;

    /**
     * 获取学生考勤记录的总记录数
     * @param studentAttendanceStatus 学生考勤记录的状态码(正常-0,已删除-1),若为null则查询所有
     * @return 学生考勤记录的总记录数
     */
    int selectAllCountOfStudentAttendace(Integer studentAttendanceStatus) throws SQLException;

    /**
     * 获取包含学生考勤的默认开始时间和默认结束时间的学生考勤对象
     * @return
     */
    StudentAttendance selectStudentAttendanceOfDefault() throws Exception;

    /**
     * 添加学生考勤记录
     * @param studentAttendance 学生考勤记录对象
     * @return 受影响的行数
     * @throws Exception 异常
     */
    int insertStudentAttendance(StudentAttendance studentAttendance) throws Exception;

    /**
     * 分页查看所有学生的考勤记录
     * @param pageSize 每页现实的最大记录数
     * @param currentPage 当前页，即要显示的指定页的数据
     * @return 包含多个学生考勤记录的List集合
     */
    List<StudentAttendance> selectStudentAttendances(Integer pageSize, Integer currentPage) throws Exception;

    /**
     * 根据指定的考勤流水号来查询考勤记录
     * @param AttendanceId 考勤流水号
     * @return 指定的考勤流水号对应的考勤记录
     * @throws Exception 异常
     */
    StudentAttendance selectStudentAttendanceByAttendanceId(String attendanceId) throws Exception;

    /**
     * 修改指定的学生考勤记录
     * @param studentAttendance 学生考勤记录对象
     * @return 受影响的行数
     * @throws Exception 异常
     */
    int updateStudentAttendance(StudentAttendance studentAttendance) throws Exception;

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

    /**
     * 根据教师姓名来查询教师ID
     * @param teacherName 教师姓名
     * @return 教师ID
     */
    Integer selectTeacherIdByTeacherName(String teacherName) throws Exception;

}
