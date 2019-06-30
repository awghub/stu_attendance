package com.awg.jwglxt.student.attendance.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.awg.jwglxt.student.attendance.constant.StudentAttendanceConstant;
import com.awg.jwglxt.student.attendance.dao.StudentAttendaceDao;
import com.awg.jwglxt.student.attendance.dao.daoImpl.StudentAttendaceDaoImpl;
import com.awg.jwglxt.student.attendance.pojo.Grade;
import com.awg.jwglxt.student.attendance.pojo.Student;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendance;
import com.awg.jwglxt.student.attendance.pojo.StudentAttendanceType;
import com.awg.jwglxt.student.attendance.reponse.ResponseContentType;
import com.awg.jwglxt.student.attendance.service.StudentAttendaceService;
import com.awg.jwglxt.student.attendance.service.serviceImpl.StudentAttendaceServiceImpl;
import com.awg.jwglxt.student.attendance.util.ResultJSONGenerateUtil;

/**
 * 学生考勤管理控制类
 * @author AWG
 *
 */
public class StudentAttendaceController extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        @SuppressWarnings("unused")
		HttpSession session = req.getSession();
        //获取请求的全路径
        String url = req.getRequestURI();
        //截取路径
        String path = url.substring(url.lastIndexOf("/")+1);
        StudentAttendaceService  sas = new StudentAttendaceServiceImpl();
        // 从session中当前登录教师对象，然后再获取其ID
        // 但是目前session存放的只有教师的姓名
        // Teacher teacher = (Teacher) session.getAttribute("teacher");
        // if (teacher == null) {
        //     resp.sendRedirect("login.html");
        //     return;
        // }
        // Integer teacherId = teacher.getTeacherId();

        // 获取session中存放的教师的姓名
//        String teacherName = (String) session.getAttribute("teacher");
//        if (teacherName == null || "".equals(teacherName)) {
//             resp.sendRedirect("login.html");
//             return;
//         }
        String teacherName = "刘春生";

        //--------------------------------------------
        StudentAttendaceDao sad = new StudentAttendaceDaoImpl();
        Integer teacherId = null;
        try {
            teacherId = sad.selectTeacherIdByTeacherName(teacherName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Cookie cookie = new Cookie("teacherId", String.valueOf(teacherId));
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath(req.getContextPath() + "/");
        resp.addCookie(cookie);
        //--------------------------------------------
        
        if ("findAllGrades".equals(path)) {
            /** 查询所有班级*/
            try {
                List<Grade> grades = sas.findAllGrades();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1001", "查询所有班级成功", grades));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if ("findStudentsByGradeName".equals(path)) {
            /** 根据班级名称查询该班级下的所有的学生*/
            String gradeName = req.getParameter("gradeName");
            try {
                List<Student> students = sas.findAllStudentsByGradeName(gradeName);
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1002", "查询所有学生成功", students));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if ("findAllStudentAttendanceTypes".equals(path)) {
            /** 根据班级名称查询该班级下的所有的学生*/
            try {
                List<StudentAttendanceType> studentAttendanceTypes = sas.findAllStudentAttendanceTypes();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1003", "查询所有学生考勤类型成功", studentAttendanceTypes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if ("addStudentAttendance".equals(path)) {
            /** 添加学生考勤记录*/
            // 获取考勤类型
            String attendanceType = req.getParameter("attendance-types");
            // 获取学生ID
            String sutdentId = req.getParameter("students");
            // 获取实际开始时间
            String actualStartTimeStr = req.getParameter("actual-start-time");
            // 获取实际结束时间
            String actualEndTimeStr = req.getParameter("actual-end-time");
            // 获取考勤状态描述
            String attendanceDescription = req.getParameter("attendance-description");
            
            try {
                int result = sas.addStudentAttendance(teacherName, attendanceType, sutdentId, actualStartTimeStr, actualEndTimeStr, attendanceDescription);
                if (result == 1) {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1004", "添加学生考勤记录成功", result));
                } else {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1005", "添加学生考勤记录失败", null));
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1005", "添加学生考勤记录失败", null));
            }
        }else if ("showStudentsAttendances".equals(path)) {
            /** 查看所有学生的考勤记录(未被删除的,即正常的)*/
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            Integer currentPage = Integer.valueOf(req.getParameter("page"));
            try {
                List<StudentAttendance> studentAttendances = sas.findAllStudentAttendances(teacherName, pageSize, currentPage);
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("items", studentAttendances);
                // 查询考勤记录状态为未删除的记录总数
                dataMap.put("counts", sas.getAllCountOfStudentAttendace(teacherName, StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_NORMAL));
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1006", "查询学生考勤记录成功", dataMap));
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1007", "查询学生考勤记录失败", null));
            }
        }else if ("showStudentAttendanceByAttendanceId".equals(path)) {
            /** 编辑界面显示某条考勤记录的详细信息*/
            String attendanceId = req.getParameter("attendanceId");
            try {
                StudentAttendance studentAttendance = sas.getStudentAttendanceByAttendanceId(attendanceId);
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1008", "查询指定学生考勤记录成功", studentAttendance));
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1009", "查询指定学生考勤记录失败", null));
            }
        }else if ("updateStudentAttendance".equals(path)) {
            /** 修改指定的考勤记录*/
            // 获取实际开始时间
            String actualStartTimeStr = req.getParameter("actual-start-time");
            // 获取实际结束时间
            String actualEndTimeStr = req.getParameter("actual-end-time");
            // 获取考勤状态描述
            String attendanceDescription = req.getParameter("attendance-description");
            // 获取考勤类型
            String attendanceType = req.getParameter("attendance-types");
            // 获取学生ID
            String sutdentId = req.getParameter("stuId");
            // 获取考勤流水号
            String attendanceId = req.getParameter("attendanceId");
            try {
                int result = sas.updateStudentAttendance(attendanceId, attendanceType, sutdentId, actualStartTimeStr, actualEndTimeStr, attendanceDescription);
                if (result == 1) {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1010", "修改学生考勤记录成功", result));
                } else {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1011", "修改学生考勤记录失败", null));
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1011", "修改学生考勤记录失败", null));
            }
        }else if ("delStudentAttendance".equals(path)) {
            /** 删除指定的考勤记录*/
           // 获取考勤流水号
            String attendanceId = req.getParameter("attendanceId");
            try {
                int result = sas.delStudentAttendance(attendanceId);
                if (result == 1) {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1012", "删除学生考勤记录成功", result));
                } else {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1013", "删除学生考勤记录失败", null));
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1013", "删除学生考勤记录失败", null));
            }
        }else if ("delStudentAttendancesByBatch".equals(path)) {
            /** 批量删除指定的考勤记录*/
            // 获取字符数组
            String[] attendanceIds = req.getParameterValues("attendanceIds");
            // attendanceIds = [20190123090000_1, 20190622170002_8, 20190622171640_10]
            System.out.println("attendanceIds = " + Arrays.toString(attendanceIds));
            String ids = Arrays.toString(attendanceIds).replaceAll("\\[|\\]", "");
            // ids = 20190123090000_1, 20190622170002_8, 20190622171640_10
            System.out.println("ids = " + ids);
            try {
                int result = sas.delStudentAttendanceByBatch(attendanceIds);
                if (result == attendanceIds.length) {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1012", "删除学生考勤记录成功", result));
                } else {
                    out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1013", "删除学生考勤记录失败", null));
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1013", "删除学生考勤记录失败", null));
            }
        }else if ("findStudentsByTeacherId".equals(path)) {
            /** 根据教师ID查询该教师所有的考勤记录*/
            // 获取教师ID
            //Integer teacherId = Integer.valueOf(req.getParameter("teacherId"));
            try {
                List<StudentAttendance> studentAttendances = sas.findAllStudentAttendancesByTeacherId(teacherId);
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1006", "查询学生考勤记录成功", studentAttendances));
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1007", "查询学生考勤记录失败", null));
            }
        }else if ("findStudentsByConditions".equals(path)) {
            /** 根据自定义条件查询学生考勤记录*/
            // 1.教师ID
            // 2.学生ID
            String stuId = req.getParameter("stuId");
            // 3.起始日期
            String dateMin = req.getParameter("date_min");
            // 4.结束日期
            String dateMax = req.getParameter("date_max");
            // 分页时的每一页的数据量
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            // 分页时要查询的页码
            Integer currentPage = Integer.valueOf(req.getParameter("page"));
            
            System.out.println("teacherId = " + teacherId + ", stuId = " + stuId + ", dateMin = " + dateMin + ", dateMax = " + dateMax);
            try {
                List<StudentAttendance> studentAttendances = sas.findAllStudentAttendanceByConditions(teacherId, stuId, dateMin, dateMax, pageSize, currentPage);
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("items", studentAttendances);
                // 查询考勤记录状态为未删除的记录总数
                dataMap.put("counts", sas.getAllCountOfStudentAttendace(teacherName, StudentAttendanceConstant.STUDENT_ATTENDANCE_STATUS_NORMAL));
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1006", "查询学生考勤记录成功", dataMap));
            } catch (Exception e) {
                e.printStackTrace();
                out.print(ResultJSONGenerateUtil.object2JSON(ResponseContentType.FLAG_SUCCESS, "1007", "查询学生考勤记录失败", null));
            }
        }
    }

}
 