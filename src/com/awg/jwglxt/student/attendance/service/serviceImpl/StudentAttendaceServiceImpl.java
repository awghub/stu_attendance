package com.awg.jwglxt.student.attendance.service.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.awg.jwglxt.student.attendance.constant.StudentAttendanceConstant;
import com.awg.jwglxt.student.attendance.dao.StudentAttendaceDao;
import com.awg.jwglxt.student.attendance.dao.daoImpl.StudentAttendaceDaoImpl;
import com.awg.jwglxt.student.attendance.pojo.Grade;
import com.awg.jwglxt.student.attendance.pojo.Student;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendance;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendanceType;
import com.awg.jwglxt.student.attendance.pojo.Teacher;
import com.awg.jwglxt.student.attendance.service.StudentAttendaceService;

/**
 * 学生考勤服务接口实现类
 * @author AWG
 *
 */
public class StudentAttendaceServiceImpl implements StudentAttendaceService {

    @Override
    public List<Grade> findAllGrades() throws Exception {
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        return sad.selectAllGrades();
    }

    @Override
    public List<Student> findAllStudentsByGradeName(String gradeName) throws Exception {
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        return sad.selectAllStudentsByGradeName(gradeName);
    }

    @Override
    public List<StudentAttendanceType> findAllStudentAttendanceTypes() throws Exception {
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        return sad.selectAllStudentAttendanceTypes();
    }

    @Override
    public int addStudentAttendance(String teacherName, String attendanceType, String sutdentId, String actualStartTimeStr, String actualEndTimeStr, String attendanceDescription) throws Exception {
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        // 获取包含学生考勤的默认开始时间和默认结束时间的学生考勤对象
        StudentAttendance defaultStudentAttendance = sad.selectStudentAttendanceOfDefault();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // 获取考勤序号(这里获取数据库中所有的考勤记录总数，包括已删除的)并加1,作为新的考勤序号
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(date)).append("_").append(sad.selectAllCountOfStudentAttendace(null, null) + 1);
        String attendanceId = sb.toString();
        // 学生的ID
        // 学生考勤类型
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        // 解析实际开始时间
        Date actualStartTimeStr2Date = sdf2.parse(actualStartTimeStr);
        // 解析实际结束时间
        Date actualEndTimeStr2Date = sdf2.parse(actualEndTimeStr);
        // 考勤状态时长计算
        Integer attendanceType2Integer = Integer.valueOf(attendanceType);
        Date defaultStartTime = defaultStudentAttendance.getAttendanceDefaultStartTime();
        Date defaultEndTime = defaultStudentAttendance.getAttendanceDefaultEndTime();

        long attendanceActualTimeLength = 0L;
        if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_NORMAL.equals(attendanceType2Integer)) {
            // 考勤类型--正常
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_NORMAL, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_HOLIDAY.equals(attendanceType2Integer)) {
            // 考勤类型--请假
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_HOLIDAY, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LATE.equals(attendanceType2Integer)) {
            // 考勤类型--迟到
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LATE, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LEAVE_EARLY.equals(attendanceType2Integer)) {
            // 考勤类型--早退
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LEAVE_EARLY, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_TRUANCY.equals(attendanceType2Integer)) {
            // 考勤类型--旷课
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_TRUANCY, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_OTHER.equals(attendanceType2Integer)) {
            // 考勤类型--其他
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_OTHER, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }
        // 考勤状态描述
        // 获取默认的考勤记录状态
        Integer attendanceStatus = StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_NORMAL;
        // 封装学生考勤记录对象 
        StudentAttendance studentAttendance = new StudentAttendance(attendanceId, new Teacher(teacherName), new Student(sutdentId), new StudentAttendanceType(Integer.valueOf(attendanceType)), actualStartTimeStr2Date, actualEndTimeStr2Date, attendanceActualTimeLength, attendanceDescription, new Date(), attendanceStatus);
        return sad.insertStudentAttendance(studentAttendance);
    }

    /**
     * 计算不同考勤类型时长的相差的毫秒数
     * @param attendanceType 考勤类型代码
     * @param defaultStartTime 默认的开始时间
     * @param defaultEndTime 默认的结束时间
     * @param actualStartTime 实际的开始时间
     * @param actualEndTime 实际的结束时间
     * @return 指定考勤类型时长的毫秒数
     */
   private static long calcTwoTimeMillis(Integer attendanceType, Date defaultStartTime, Date defaultEndTime, Date actualStartTime, Date actualEndTime) {
       if (attendanceType == null || defaultStartTime == null || defaultEndTime == null || actualStartTime == null || actualEndTime == null) {
           return 0;
       }
       long diffTime = 0L;
       switch (attendanceType) {
           case 0:
                // 0-正常, 默认结束-默认开始
                diffTime = defaultEndTime.getTime() - defaultStartTime.getTime();
                break;
           case 1:
                // 1-请假, 实际结束-实际开始
                diffTime = actualEndTime.getTime() - actualStartTime.getTime();
                break;
           case 2:
                // 2-迟到, 实际开始-默认开始
                diffTime = actualStartTime.getTime() - defaultStartTime.getTime();
                break;
           case 3:
                // 3-早退, 默认结束-实际结束
                diffTime = defaultEndTime.getTime() - actualEndTime.getTime();
                break;
           case 4:
                // 4-旷课, 默认结束-默认开始
                diffTime = defaultEndTime.getTime() - defaultStartTime.getTime();
                break;
           case 5:
                // 5-其他(只计算实际上课时间), 实际结束-实际开始
                diffTime = actualEndTime.getTime() - actualStartTime.getTime();
                break;
           default:
                diffTime = 0L;
                break;
       }
       return diffTime;
   }

    @Override
    public List<StudentAttendance> findAllStudentAttendances(String teacherName, Integer pageSize, Integer currentPage) throws Exception {
        if (!String.valueOf(pageSize).matches(StudentAttendanceConstant.PAGE_REGX)) {
            pageSize = StudentAttendanceConstant.PAGE_SIZE;
        }
        if (!String.valueOf(currentPage).matches(StudentAttendanceConstant.PAGE_REGX)) {
            currentPage = StudentAttendanceConstant.DAFAULT_PAGE;
        }
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        Integer teacherId = sad.selectTeacherIdByTeacherName(teacherName);
        return sad.selectStudentAttendances(teacherId, pageSize, currentPage);
    }

    @Override
    public int getAllCountOfStudentAttendace(String teacherName, Integer studentAttendanceStatus) throws Exception {
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        Integer teacherId = sad.selectTeacherIdByTeacherName(teacherName);
        return sad.selectAllCountOfStudentAttendace(teacherId, studentAttendanceStatus);
    }

    @Override
    public StudentAttendance getStudentAttendanceByAttendanceId(String attendanceId) throws Exception {
        if (attendanceId == null || !attendanceId.matches(StudentAttendanceConstant.STUDENT_ATTENDANCE_ID_REGEX)) {
            return null;
        }
        return new StudentAttendaceDaoImpl().selectStudentAttendanceByAttendanceId(attendanceId);
    }

    @Override
    public int updateStudentAttendance(String attendanceId, String attendanceType, String sutdentId,
            String actualStartTimeStr, String actualEndTimeStr, String attendanceDescription) throws Exception {

        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        // 获取包含学生考勤的默认开始时间和默认结束时间的学生考勤对象
        StudentAttendance defaultStudentAttendance = sad.selectStudentAttendanceOfDefault();
        // 学生考勤类型
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        // 解析实际开始时间
        Date actualStartTimeStr2Date = sdf2.parse(actualStartTimeStr);
        // 解析实际结束时间
        Date actualEndTimeStr2Date = sdf2.parse(actualEndTimeStr);
        // 考勤状态时长计算
        Integer attendanceType2Integer = Integer.valueOf(attendanceType);
        Date defaultStartTime = defaultStudentAttendance.getAttendanceDefaultStartTime();
        Date defaultEndTime = defaultStudentAttendance.getAttendanceDefaultEndTime();
        long attendanceActualTimeLength = 0L;
        if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_NORMAL.equals(attendanceType2Integer)) {
            // 考勤类型-正常
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_NORMAL, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_HOLIDAY.equals(attendanceType2Integer)) {
            // 考勤类型--请假
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_HOLIDAY, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LATE.equals(attendanceType2Integer)) {
            // 考勤类型--迟到
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LATE, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LEAVE_EARLY.equals(attendanceType2Integer)) {
            // 考勤类型--早退
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_LEAVE_EARLY, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_TRUANCY.equals(attendanceType2Integer)) {
            // 考勤类型--旷课
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_TRUANCY, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }else if (StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_OTHER.equals(attendanceType2Integer)) {
            // 考勤类型--其他
            attendanceActualTimeLength = calcTwoTimeMillis(StudentAttendanceConstant.STUDENT_ATTENDANCE_TYPE_OTHER, defaultStartTime, defaultEndTime, actualStartTimeStr2Date, actualEndTimeStr2Date);
        }
        // 获取默认的考勤记录状态
        Integer attendanceStatus = StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_NORMAL;
        // 封装学生考勤记录对象 
        StudentAttendance studentAttendance = new StudentAttendance(attendanceId, null, new Student(sutdentId), new StudentAttendanceType(Integer.valueOf(attendanceType)), actualStartTimeStr2Date, actualEndTimeStr2Date, attendanceActualTimeLength, attendanceDescription, new Date(), attendanceStatus);
        return sad.updateStudentAttendance(studentAttendance);
    }

    @Override
    public int delStudentAttendance(String attendanceId) throws Exception {
        if (attendanceId == null || !attendanceId.matches(StudentAttendanceConstant.STUDENT_ATTENDANCE_ID_REGEX)) {
            return 0;
        }
        return new StudentAttendaceDaoImpl().delStudentAttendance(attendanceId);
    }

	@Override
	public int delStudentAttendanceByBatch(String[] attendanceIds) throws Exception {
		if (attendanceIds == null || attendanceIds.length < 1) {
			return 0;
		}
		return new StudentAttendaceDaoImpl().delStudentAttendanceByBatch(attendanceIds);
	}

    @Override
    public List<StudentAttendance> findAllStudentAttendancesByTeacherId(Integer teacherId) throws Exception {
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        return sad.selectAllStudentAttendancesByTeacherId(teacherId);
    }

    @Override
    public List<StudentAttendance> findAllStudentAttendanceByConditions(Integer teacherId, String stuId, String dateMin,
            String dateMax, Integer pageSize, Integer currentPage) throws Exception {
        if (!String.valueOf(pageSize).matches(StudentAttendanceConstant.PAGE_REGX)) {
            pageSize = StudentAttendanceConstant.PAGE_SIZE;
        }
        if (!String.valueOf(currentPage).matches(StudentAttendanceConstant.PAGE_REGX)) {
            currentPage = StudentAttendanceConstant.DAFAULT_PAGE;
        }
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        return sad.selectAllStudentAttendanceByConditions(teacherId, stuId, dateMin, dateMax, pageSize, currentPage);
    }
    
}
