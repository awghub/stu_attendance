package com.awg.jwglxt.student.attendance.dao;

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
     * 获取勤记录的总记录数<br/>
     * 1.若教师ID为空且状态码为空，则查询总的所有考勤记录总数;<br/>
     * 2.若教师ID为空且状态码不为空，则根据状态码查询考勤记录总数: <br/>
     *      &nbsp;&nbsp;&nbsp;&nbsp;2.1若状态码为空,则查询该教师下所有的考勤记录总数;<br/>
     *      &nbsp;&nbsp;&nbsp;&nbsp;2.2若状态码不为空,则查询该教师下指定状态码的考勤记录总数;
     * @param teaacherId 教师的ID
     * @param studentAttendanceStatus 学生考勤记录的状态码(正常-0,已删除-1),若为null则查询所有
     * @return 学生考勤记录的总记录数
     */
    int selectAllCountOfStudentAttendace(Integer teaacherId, Integer studentAttendanceStatus) throws Exception;

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
     * @param teacherId 当前登录的教师的ID
     * @param pageSize 每页现实的最大记录数
     * @param currentPage 当前页，即要显示的指定页的数据
     * @return 包含多个学生考勤记录的List集合
     */
    List<StudentAttendance> selectStudentAttendances(Integer teacherId, Integer pageSize, Integer currentPage) throws Exception;
    
    /**
     * 查询某个教师下所有的学生的考勤记录
     * @param teacherName 教师的ID
     * @return 包含多个学生考勤记录的List集合
     * @throws Exception 异常
     */
    List<StudentAttendance> selectAllStudentAttendancesByTeacherId(Integer teacherId) throws Exception;
    
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
    List<StudentAttendance> selectAllStudentAttendanceByConditions(Integer teacherId, String stuId, String dateMin, String dateMax, Integer pageSize, Integer currentPage) throws Exception;
    
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
