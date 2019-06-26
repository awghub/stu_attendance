$(function() {
    // 初始化加载面板依赖(element)模块, 表单依赖form模块, 时间组件依赖laydate模块
    layui.use(['element', 'form', 'laydate', 'layer'], function(){
      // 模块加载
      var element = layui.element;
      var form = layui.form;
      var laydate = layui.laydate;
      var layer = layui.layer;
      // 定义变量
      var gradeVal = -1;
      var studentVal = -1;
      var attendanceTypeVal = -1;
      var actualStartTimeVal = "";
      var actualEndTimeVal = "";
      var defaultStartTimeVal = $("input[name='default-start-time']").val();
      var defaultEndTimeVal = $("input[name='default-end-time']").val();
      var attendanceDescriptionVal = $("#attendance-description").val();
      
      // 班级下拉列表监听(传统的change事件在layui中无效)
      form.on('select(grades)', function(data){
          gradeVal = data.value;
          // 根据所选班级来查询该班级所具有的学生
          findStudentsByGradeName(data.value);
      });
      // 学生下拉列表监听(传统的change事件在layui中无效)
      form.on('select(students)', function(data){
          studentVal = data.value;
      });
      
      // 学生考勤类型下拉列表监听
      form.on('select(attendance-types)', function(data) {
          attendanceTypeVal = data.value;
          if(actualStartTimeVal != -1 && actualEndTimeVal != null && defaultStartTimeVal != null && defaultEndTimeVal != null && attendanceTypeVal != -1){
        	  $("#attendance-time-length").val(attendanceTimeFormatShow(calcTwoTimeMillis(attendanceTypeVal, defaultStartTimeVal, defaultEndTimeVal, actualStartTimeVal, actualEndTimeVal)));
          }else{
        	  $("#attendance-time-length").val(attendanceTimeFormatShow(0));
          }
      });
      
      //实际开始时间选择器
      laydate.render({ 
        elem: '#actual-start-time',
        type: 'time',
        change: function(value, date){ 
            //监听日期被切换
            actualStartTimeVal = value;
            if(actualStartTimeVal != -1 && actualEndTimeVal != null && defaultStartTimeVal != null && defaultEndTimeVal != null && attendanceTypeVal != -1){
          	  $("#attendance-time-length").val(attendanceTimeFormatShow(calcTwoTimeMillis(attendanceTypeVal, defaultStartTimeVal, defaultEndTimeVal, actualStartTimeVal, actualEndTimeVal)));
            }else{
          	  $("#attendance-time-length").val(attendanceTimeFormatShow(0));
            }
        }
      });
      
      // 实际结束时间选择器
      laydate.render({ 
          elem: '#actual-end-time',
          type: 'time',
          change: function(value, date){ 
             //监听日期被切换
             actualEndTimeVal = value;
             if(actualStartTimeVal != -1 && actualEndTimeVal != null && defaultStartTimeVal != null && defaultEndTimeVal != null && attendanceTypeVal != -1){
           	  $("#attendance-time-length").val(attendanceTimeFormatShow(calcTwoTimeMillis(attendanceTypeVal, defaultStartTimeVal, defaultEndTimeVal, actualStartTimeVal, actualEndTimeVal)));
             }else{
           	  $("#attendance-time-length").val(attendanceTimeFormatShow(0));
             }
          }
      });
      
      // 规则校验
      form.verify({
            // 班级选择校验
            // value：表单的值、item：表单的DOM对象
          gradesVerify: function(gradeVal, item){
              if(gradeVal == -1){
                return '请选择班级';
              }
            },
            // 学生选择校验
          studentsVerify: function(studentVal, item) {
              if(studentVal == -1){
                    return '请选择学生';
              }
          },
          // 实际开始上课时间校验
          actualStartTimeVerify: function(actualStartTimeVal, item) {
              if (!/^\d{2}:\d{2}:\d{2}$/.test(actualStartTimeVal)) {
                  return '实际开始上课时间格式不正确';
              }
          },
          // 实际开始下课时间校验
          actualEndTimeVerify: function(actualEndTimeVal, item) {
              if (!/^\d{2}:\d{2}:\d{2}$/.test(actualEndTimeVal)) {
                  return '实际开始下课时间格式不正确';
              }
          },
          // 学生考勤类型校验
          attendanceTypesVerify: function(attendanceTypeVal, item) {
              if(attendanceTypeVal == -1){
                    return '请选择学生考勤类型';
              }
          },
          // 考勤状态描述校验
          attendanceDescription: function(attendanceDescriptionVal, item) {
              if (attendanceDescriptionVal != null && attendanceDescriptionVal != "" && attendanceDescriptionVal.length > 200) {
                  return '考勤状态描述过长(200个字以内)';
              }
          }
      });
      // 表单提交
      form.on('submit(attendanceAddBtn)', function(data){
            // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
            // return false; //阻止表单跳转
       $.ajax({
              type: "post",
              url: "teacherMaster/addStudentAttendance",
              data:data.field,
              dataType: "json",
              success: function(result) {
                  layer.msg(result.respObj.respObjMsg);
                  // 重置表单
                  setTimeout(function(){
                      $("#myform")[0].reset();
                      $("#students").empty().append("<option value='-1'>-请选择-</option>");
                      form.render();
                }, 200);
              }
          });
          return false;
      });
    });

    // 初始化班级
    initGrades();
    // 初始化考勤类型信息
    initStudentAttendanceTypes();

});

/**
 * 初始化班级信息
 * 
 */
function initGrades() {
    $.ajax({
        type:"GET",
        url:"teacherMaster/findAllGrades",
        data:{},
        dataType:"json",
        async: false,
        success:function(result){
            console.log(result);
            var grades = result.data;
            var str = "<option value='-1'>-请选择-</option>";
            for (var i = 0; i < grades.length; i++) {
                str = str + "<option value='" + grades[i].gradeName + "'>" + grades[i].gradeName + "</option>";
            }
            $("#grades").empty().append(str);
            
            layui.use('form', function(){
                var form = layui.form;
                form.render('select', 'grades');
            });
        }
    });
}

/**
 * 初始化考勤类型信息
 * 
 */
function initStudentAttendanceTypes() {
    $.ajax({
        type:"GET",
        url:"teacherMaster/findAllStudentAttendanceTypes",
        data:{},
        dataType:"json",
        async: false,
        success:function(result){
            console.log(result);
            var attendanceTypes = result.data;
            var str = "<option value='-1'>-请选择-</option>";
            for (var i = 0; i < attendanceTypes.length; i++) {
                str = str + "<option value='" + attendanceTypes[i].studentAttendanceTypeId + "'>" + attendanceTypes[i].studentAttendanceTypeName + "</option>";
            }
            $("#attendance-types").empty().append(str);
            
            layui.use('form', function(){
                var form = layui.form;
                form.render('select', 'attendance-types');
            });
        }
    });
}

/**
 * 根据班级名称查询该班级下的所有的学生
 */
function findStudentsByGradeName(gradeName) {
    $.ajax({
        type:"GET",
        url:"teacherMaster/findStudentsByGradeName",
        data:{
            gradeName: gradeName
        },
        dataType:"json",
        async: false,
        success:function(result){
            console.log(result);
            var students = result.data;
            var str = "<option value='-1'>-请选择-</option>";
            for (var i = 0; i < students.length; i++) {
                str = str + "<option value='" + students[i].stuId + "'>" + students[i].stuName + "</option>";
            }
            $("#students").empty().append(str);
            layui.use('form', function(){
                var form = layui.form;
                // 这里不能加过滤,否则不能渲染成功
                // form.render('select', 'students');
                form.render('select');
            });
        }
    });
}

/**
 * 计算不同考勤类型时长的相差的毫秒数
 * @param attendanceType 考勤类型代码
 * @param defaultStartTime0 默认的开始时间
 * @param defaultEndTime0 默认的结束时间
 * @param actualStartTime0 实际的开始时间
 * @param actualEndTime0 实际的结束时间
 * @return 指定考勤类型时长的毫秒数
 */
function calcTwoTimeMillis(attendanceType, defaultStartTime0, defaultEndTime0, actualStartTime0, actualEndTime0) {
   var diffTime = 0;
   var nowDate = new Date();
   var defaultStartTime = new Date(nowDate.getFullYear() +"-"+ nowDate.getMonth() +"-"+ nowDate.getDate() +" "+ defaultStartTime0);
   var defaultEndTime = new Date(nowDate.getFullYear() +"-"+ nowDate.getMonth() +"-"+ nowDate.getDate() +" "+ defaultEndTime0);
   var actualStartTime = new Date(nowDate.getFullYear() +"-"+ nowDate.getMonth() +"-"+ nowDate.getDate() +" "+ actualStartTime0);
   var actualEndTime = new Date(nowDate.getFullYear() +"-"+ nowDate.getMonth() +"-"+ nowDate.getDate() +" "+ actualEndTime0);
   switch (parseInt(attendanceType)) {
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
            diffTime = 0;
            break;
   }
   return Math.abs(diffTime);
}

/** 根据给定的毫秒值进行格式化显示 */
function attendanceTimeFormatShow(diffTimeMillis) {
    // 计算出相差天数
    var days = Math.floor(diffTimeMillis / (24 * 3600 * 1000));
    // 计算出小时数
    var leave1 = diffTimeMillis % (24 * 3600 * 1000); // 计算天数后剩余的毫秒数
    var hours = Math.floor(leave1 / (3600 * 1000));
    // 计算相差分钟数
    var leave2 = leave1 % (3600 * 1000); // 计算小时数后剩余的毫秒数
    var minutes = Math.floor(leave2 / (60 * 1000));
    // 计算相差秒数
    var leave3 = leave2 % (60 * 1000); // 计算分钟数后剩余的毫秒数
    var seconds = Math.round(leave3 / 1000);
    return (days + " 天  " + hours + " 小时  " + minutes + "  分钟 " + seconds + "  秒钟");
}