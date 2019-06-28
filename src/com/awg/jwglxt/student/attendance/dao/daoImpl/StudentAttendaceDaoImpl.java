package com.awg.jwglxt.student.attendance.dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.awg.jwglxt.student.attendance.constant.StudentAttendanceConstant;
import com.awg.jwglxt.student.attendance.dao.StudentAttendaceDao;
import com.awg.jwglxt.student.attendance.pojo.Grade;
import com.awg.jwglxt.student.attendance.pojo.Student;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendance;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendanceType;
import com.awg.jwglxt.student.attendance.util.C3P0Utils;
/**
 * 学生数据访问接口实现类
 * @author AWG
 *
 */
public class StudentAttendaceDaoImpl implements StudentAttendaceDao {

    @Override
    public List<Grade> selectAllGrades() throws Exception {
        List<Grade> grades = new ArrayList<Grade>();
        String sql = "SELECT grade_name,nowstudent,teacher_name FROM tb_grade";
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        PreparedStatement psmt = connection.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery(); 
        while (rs.next()) {
            Grade grade = new Grade(rs.getString("grade_name"), rs.getInt("nowstudent"), rs.getString("teacher_name"));
            grades.add(grade);
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return grades;
    }

    @Override
    public List<Student> selectAllStudentsByGradeName(String gradeName) throws Exception {
        List<Student> students = new ArrayList<Student>();
        String sql = "SELECT stu_id,stu_name,stu_gender,stu_age,stu_tel,stu_where,stu_type,grade_name,teacher_name FROM tb_student WHERE grade_name=?";
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        PreparedStatement psmt = connection.prepareStatement(sql);
        psmt.setString(1, gradeName);
        ResultSet rs = psmt.executeQuery(); 
        while (rs.next()) {
            Student student = new Student(rs.getString("stu_id"), rs.getString("stu_name"), rs.getInt("stu_gender"), rs.getInt("stu_age"), rs.getString("stu_tel"), rs.getString("stu_where"), rs.getInt("stu_type"), rs.getString("grade_name"), rs.getString("teacher_name"));
            students.add(student);
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return students;
    }

    @Override
    public List<StudentAttendanceType> selectAllStudentAttendanceTypes() throws Exception {
        List<StudentAttendanceType> studentAttendanceTypes = new ArrayList<StudentAttendanceType>();
        String sql = "SELECT pk_student_attendance_type_id,student_attendance_type_name FROM tb_student_attendance_type";
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        PreparedStatement psmt = connection.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery(); 
        while (rs.next()) {
            StudentAttendanceType studentAttendanceType = new StudentAttendanceType(rs.getInt("pk_student_attendance_type_id"),rs.getString("student_attendance_type_name"));
            studentAttendanceTypes.add(studentAttendanceType);
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return studentAttendanceTypes;
    }

    @Override
    public int selectAllCountOfStudentAttendace(Integer studentAttendanceStatus) throws SQLException {
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return 0;
        }
        int count = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(pk_attendance_id) AS nowCount FROM tb_student_attendance WHERE ");
        PreparedStatement psmt = null;
        // 根据状态码查询考勤记录总数: 
        // 若状态码不为空,则查询指定状态码的考勤记录总数;
        // 若状态码为空,则查询所有的考勤记录总数; 
        if (studentAttendanceStatus == null) {
            sb.append(" attendance_staus IS NOT NULL ");
            psmt = connection.prepareStatement(sb.toString());
        }else {
            sb.append(" attendance_staus = ?");
            psmt = connection.prepareStatement(sb.toString());
            psmt.setInt(1, studentAttendanceStatus);
        }
        ResultSet rs = psmt.executeQuery(); 
        if (rs.next()) {
            count = rs.getInt("nowCount");
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return count;
    }

    @Override
    public int insertStudentAttendance(StudentAttendance studentAttendance) throws Exception {
        // 考勤流水号
        String attendanceId = studentAttendance.getAttendanceId();
        // 教师ID
        Integer teacherId = selectTeacherIdByTeacherName(studentAttendance.getTeacher().gettName());
        // 学生ID
        String studentId = studentAttendance.getStudent().getStuId();
        // 考勤类型
        Integer studentAttendanceTypeId = studentAttendance.getStudentAttendanceType().getStudentAttendanceTypeId();
        // 实际开始时间
        Date actualStartTime = studentAttendance.getAttendanceActualStartTime();
        // 实际结束时间
        Date actualEndTime = studentAttendance.getAttendanceActualEndTime();
        // 考勤时长
        long attendanceActualTimeLength = studentAttendance.getAttendanceActualTimeLength();
        // 考勤描述
        String attendanceDescription = studentAttendance.getAttendanceDescription();
        // 获取当前时间
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 考勤记录状态
        Integer attendanceStatus = studentAttendance.getAttendanceStatus();
        // SQL语句
        String sql = "INSERT INTO tb_student_attendance(pk_attendance_id,fk_teacher_id,fk_stu_id,fk_student_attendance_type_id,attendance_actual_start_time,attendance_actual_end_time,attendance_actual_time_length,attendance_description,attendance_add_date,attendance_staus) VALUES(?,?,?,?,?,?,?,?,?,?)";
        Connection connection = C3P0Utils.getConn();
        PreparedStatement psmt = connection.prepareStatement(sql);
        psmt.setString(1, attendanceId);
        psmt.setInt(2, teacherId);
        psmt.setString(3, studentId);
        psmt.setInt(4, studentAttendanceTypeId);
        // 将java.util.Date类型的值转为java.sql.Date类型的值
        //psmt.setDate(5, new java.sql.Date(actualStartTime.getTime()));
        //psmt.setDate(6, new java.sql.Date(actualEndTime.getTime()));
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String actualStartTimeStr = sdfDateFormat.format(actualStartTime);
        String actualEndTimeStr = sdfDateFormat.format(actualEndTime);
        psmt.setTime(5, Time.valueOf(actualStartTimeStr.substring(actualStartTimeStr.indexOf(" ") + 1)));
        psmt.setTime(6, Time.valueOf(actualEndTimeStr.substring(actualEndTimeStr.indexOf(" ") + 1)));
        psmt.setLong(7, attendanceActualTimeLength);
        psmt.setString(8, attendanceDescription);
        psmt.setString(9, currentDate);
        psmt.setInt(10, attendanceStatus);
        int result = psmt.executeUpdate();
        C3P0Utils.closeAll(null, psmt, connection);
        return result;
    }

    @Override
    public StudentAttendance selectStudentAttendanceOfDefault() throws Exception {
        String sql = "SELECT attendance_default_start_time, attendance_default_end_time FROM tb_student_attendance LIMIT 1";
        StudentAttendance studentAttendance = null;
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        PreparedStatement psmt = connection.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery(); 
        if (rs.next()) {
            // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            //studentAttendance = new StudentAttendance(sdf.parse(rs.getString("attendance_default_start_time")), sdf.parse(rs.getString("attendance_default_end_time")));
            //studentAttendance = new StudentAttendance(rs.getDate("attendance_default_start_time"), rs.getDate("attendance_default_end_time"));
            studentAttendance = new StudentAttendance(rs.getTime("attendance_default_start_time"), rs.getTime("attendance_default_end_time"));
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return studentAttendance;
    }

    @Override
    public List<StudentAttendance> selectStudentAttendances(Integer pageSize, Integer currentPage) throws Exception{
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" stu.stu_id, ");
        sb.append(" stu.stu_name, ");
        sb.append(" sa.pk_attendance_id, ");
        sb.append(" sa.fk_student_attendance_type_id, ");
        sb.append(" sa.attendance_default_start_time, ");
        sb.append(" sa.attendance_default_end_time, ");
        sb.append(" sa.attendance_actual_start_time, ");
        sb.append(" sa.attendance_actual_end_time, ");
        sb.append(" sa.attendance_actual_time_length, ");
        sb.append(" sa.attendance_description, ");
        sb.append(" sat.pk_student_attendance_type_id, ");
        sb.append(" sat.student_attendance_type_name ");
        sb.append(" FROM ");
        sb.append(" tb_student AS stu ");
        sb.append(" LEFT JOIN( ");
        sb.append(" tb_student_attendance AS sa ");
        sb.append(" LEFT JOIN ");
        sb.append(" tb_student_attendance_type AS sat ");
        sb.append(" ON ");
        sb.append(" sa.fk_student_attendance_type_id=sat.pk_student_attendance_type_id ");
        sb.append(") ON ");
        sb.append(" sa.fk_stu_id=stu.stu_id WHERE sa.attendance_staus!=? LIMIT ?,?");
        PreparedStatement psmt = connection.prepareStatement(sb.toString());
        psmt.setInt(1, StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_DELETED);
        psmt.setInt(2, (currentPage - 1)*pageSize);
        psmt.setInt(3, pageSize);
        ResultSet rs = psmt.executeQuery();
        
        List<StudentAttendance> studentAttendances = new ArrayList<StudentAttendance>();
        StudentAttendance studentAttendance = null;
        StudentAttendanceType studentAttendanceType = null;
        Student student = null;
        
        while (rs.next()) {
            student = new Student(rs.getString("stu_id"), rs.getString("stu_name"));
            studentAttendanceType = new StudentAttendanceType(rs.getInt("pk_student_attendance_type_id"), rs.getString("student_attendance_type_name"));
            studentAttendance = new StudentAttendance(rs.getString("pk_attendance_id"), student, studentAttendanceType, rs.getTime("attendance_default_start_time"), rs.getTime("attendance_default_end_time"), rs.getTime("attendance_actual_start_time"), rs.getTime("attendance_actual_end_time"), rs.getLong("attendance_actual_time_length"), rs.getString("attendance_description"));
            studentAttendances.add(studentAttendance);
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return studentAttendances;
    }

    @Override
    public StudentAttendance selectStudentAttendanceByAttendanceId(String attendanceId) throws Exception {
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" stu.stu_id, ");
        sb.append(" stu.stu_name, ");
        sb.append(" stu.grade_name, ");
        sb.append(" sa.pk_attendance_id, ");
        sb.append(" sa.fk_student_attendance_type_id, ");
        sb.append(" sa.attendance_default_start_time, ");
        sb.append(" sa.attendance_default_end_time, ");
        sb.append(" sa.attendance_actual_start_time, ");
        sb.append(" sa.attendance_actual_end_time, ");
        sb.append(" sa.attendance_actual_time_length, ");
        sb.append(" sa.attendance_description, ");
        sb.append(" sat.pk_student_attendance_type_id, ");
        sb.append(" sat.student_attendance_type_name ");
        sb.append(" FROM ");
        sb.append(" tb_student AS stu ");
        sb.append(" LEFT JOIN( ");
        sb.append(" tb_student_attendance AS sa ");
        sb.append(" LEFT JOIN ");
        sb.append(" tb_student_attendance_type AS sat ");
        sb.append(" ON ");
        sb.append(" sa.fk_student_attendance_type_id=sat.pk_student_attendance_type_id ");
        sb.append(") ON ");
        sb.append(" sa.fk_stu_id=stu.stu_id WHERE sa.pk_attendance_id=?");
        PreparedStatement psmt = connection.prepareStatement(sb.toString());
        psmt.setString(1, attendanceId);
        ResultSet rs = psmt.executeQuery();
        StudentAttendance studentAttendance = null;
        StudentAttendanceType studentAttendanceType = null;
        Student student = null;
        if (rs.next()) {
            student = new Student(rs.getString("stu_id"), rs.getString("stu_name"), rs.getString("grade_name"));
            studentAttendanceType = new StudentAttendanceType(rs.getInt("pk_student_attendance_type_id"), rs.getString("student_attendance_type_name"));
            studentAttendance = new StudentAttendance(rs.getString("pk_attendance_id"), student, studentAttendanceType, rs.getTime("attendance_default_start_time"), rs.getTime("attendance_default_end_time"), rs.getTime("attendance_actual_start_time"), rs.getTime("attendance_actual_end_time"), rs.getLong("attendance_actual_time_length"), rs.getString("attendance_description"));
        }
        C3P0Utils.closeAll(rs, psmt, connection);
        return studentAttendance;
    }

    @Override
    public int updateStudentAttendance(StudentAttendance studentAttendance) throws Exception {
        // 考勤流水号
        String attendanceId = studentAttendance.getAttendanceId();
        // 考勤类型
        Integer studentAttendanceTypeId = studentAttendance.getStudentAttendanceType().getStudentAttendanceTypeId();
        // 实际开始时间
        Date actualStartTime = studentAttendance.getAttendanceActualStartTime();
        // 实际结束时间
        Date actualEndTime = studentAttendance.getAttendanceActualEndTime();
        // 考勤时长
        long attendanceActualTimeLength = studentAttendance.getAttendanceActualTimeLength();
        // 考勤描述
        String attendanceDescription = studentAttendance.getAttendanceDescription();
        
        // SQL语句
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_student_attendance ");
        sb.append(" SET fk_student_attendance_type_id=?, ");
        sb.append(" attendance_actual_start_time=?, ");
        sb.append(" attendance_actual_end_time=?, ");
        sb.append(" attendance_actual_time_length=?, ");
        sb.append(" attendance_description=? ");
        sb.append(" WHERE pk_attendance_id=? ");
        Connection connection = C3P0Utils.getConn();
        PreparedStatement psmt = connection.prepareStatement(sb.toString());
        psmt.setInt(1, studentAttendanceTypeId);
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String actualStartTimeStr = sdfDateFormat.format(actualStartTime);
        String actualEndTimeStr = sdfDateFormat.format(actualEndTime);
        psmt.setTime(2, Time.valueOf(actualStartTimeStr.substring(actualStartTimeStr.indexOf(" ") + 1)));
        psmt.setTime(3, Time.valueOf(actualEndTimeStr.substring(actualEndTimeStr.indexOf(" ") + 1)));
        psmt.setLong(4, attendanceActualTimeLength);
        psmt.setString(5, attendanceDescription);
        psmt.setString(6, attendanceId);
        
        int result = psmt.executeUpdate();
        C3P0Utils.closeAll(null, psmt, connection);
        return result;
    }

    @Override
    public int delStudentAttendance(String attendanceId) throws Exception {
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_student_attendance ");
        sb.append(" SET attendance_staus=? ");
        sb.append(" WHERE pk_attendance_id=? ");
        PreparedStatement psmt = connection.prepareStatement(sb.toString());
        psmt.setInt(1, StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_DELETED);
        psmt.setString(2, attendanceId);
        
        int result = psmt.executeUpdate();
        C3P0Utils.closeAll(null, psmt, connection);
        return result;
    }

    @Override
    public int delStudentAttendanceByBatch(String[] attendanceIds) throws Exception {
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_student_attendance SET attendance_staus=? WHERE pk_attendance_id in (");
        sb.append(Arrays.toString(attendanceIds).replaceAll("\\[|\\]", "'").replaceAll(", ?", "','"));
        sb.append(")");
        PreparedStatement psmt = connection.prepareStatement(sb.toString());
        psmt.setInt(1, StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_DELETED);
        int result = psmt.executeUpdate();
        C3P0Utils.closeAll(null, psmt, connection);
        return result;
    }

    @Override
    public Integer selectTeacherIdByTeacherName(String teacherName) throws Exception {
        Connection connection = C3P0Utils.getConn();
        if (connection == null) {
            return null;
        }
        String sql = "SELECT teacher_id AS tid FROM tb_teacher WHERE teacher_name=? LIMIT 1";
        PreparedStatement psmt = connection.prepareStatement(sql);
        psmt.setString(1, teacherName);
        ResultSet rs = psmt.executeQuery();
        int result = 0;
        if (rs.next()) {
            result = rs.getInt("tid");
        }
        C3P0Utils.closeAll(null, psmt, connection);
        return result;
    }

}
