$(function() {
    layui.use([ 'table', 'layer', 'laydate' ], function() {
        // 加载table模块
        var table = layui.table;
        var laydate = layui.laydate;
        // 初始化表格信息
        stuAttInit(table);

        // 头工具栏事件
        table.on('toolbar(test)', function(obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'getCheckData':
                    // 获取选中行数据
                    var data = checkStatus.data;
                    // 定义一个数组,用于存放批量删除考勤记录的ID
                    var ids = [];
                    // 遍历选中项,并存入数组
                    for (var i = 0; i < data.length; i++) {
                        ids[i] = (data[i].attendanceId);
                    }
                    if (data.length < 1) {
                        layer.alert('请至少选择一项数据', {icon: 5});
                        return;
                    }
                    // 删除确认提示
                    layer.confirm('是否删除选中的 ' + data.length + " 条记录?", {icon: 3, title:'提示'}, function(index){
                        $.ajax({
                            type : "post",
                            url : "teacherMaster/delStudentAttendancesByBatch",
                            data : {
                                attendanceIds : ids
                            },
                            traditional:true,
                            dataType : "json",
                            success : function(result) {
                                layer.msg(result.respObj.respObjMsg);
                                // 删除成功
                                if (result.respObj.respObjCode == 1012) {
                                    layer.close(index);
                                }
                                stuAttInit(table);
                                laydate.render({
                                    elem: '#date-start',
                                    type: 'date',
                                    done: function(value, date, endDate){
                                        //监听日期被切换
                                        dateStart = value;
                                    }
                                });
                                laydate.render({
                                    elem: '#date-end',
                                    type: 'date',
                                    done: function(value, date, endDate){
                                        //监听日期被切换
                                        dateEnd = value;
                                    }
                                });
                            }
                        });
                    });
                    break;
            };
        });

        // 监听行工具事件
        table.on('tool(test)', function(obj) {
            var data = obj.data;
            // console.log(obj);
            if (obj.event === 'del') {
                layer.confirm('真的删除行么', function(index) {
                    $.ajax({
                        type : "post",
                        url : "teacherMaster/delStudentAttendance",
                        data : {
                            attendanceId : data.attendanceId
                        },
                        dataType : "json",
                        success : function(result) {
                            layer.msg(result.respObj.respObjMsg);
                            // 删除成功
                            if (result.respObj.respObjCode == 1012) {
                                obj.del();
                                layer.close(index);
                            }
                            stuAttInit(table);
                            laydate.render({
                                elem: '#date-start',
                                type: 'date',
                                done: function(value, date, endDate){
                                    //监听日期被切换
                                    dateStart = value;
                                }
                            });
                            laydate.render({
                                elem: '#date-end',
                                type: 'date',
                                done: function(value, date, endDate){
                                    //监听日期被切换
                                    dateEnd = value;
                                }
                            });
                        }
                    });
                });
            } else if (obj.event === 'edit') {
                // alert(data.attendanceId); // 获取考勤流水号
                layer.open({
                    type : 2,
                    scrollbar : false,
                    title : '修改考勤信息',
                    skin : 'layui-layer-rim', // 加上边框
                    offset : '0px',
                    area : [ '1090px', '660px' ], // 宽高
                    content : 'attendanceEdit.html?attendanceId=' + data.attendanceId
                });
            }
        });

    });

    /**
     * 初始化表格信息
     */
    function stuAttInit(table) {
        table.render({
                    elem : '#demo',
                    url : 'teacherMaster/showStudentsAttendances',
                    toolbar : '#toolbarDemo',
                    page : true, // 开启分页
                    limits : [ 5, 10, 20, 30, 40, 50 ],
                    limit : 10,
                    response : {
                        // 规定成功的状态码，默认为0，如果不重新规定成功的状态码数据将会无法显示在页面上
                        statusCode : 1006
                    },
                    parseData : function(res) {
                        // res 即为原始返回的数据
                        // console.log(res);
                        return {
                            "code" : res.respObj.respObjCode, // 解析接口状态
                            "msg" : res.respObj.respObjMsg, // 解析提示文本
                            "count" : res.data.counts, // 解析数据长度
                            "data" : res.data.items // 解析数据列表
                        };
                    },
                    cols : [ [ // 表头
                            {
                                type : 'checkbox',
                                fixed : 'left'
                            },
                            {
                                field : 'attendanceId',
                                title : '考勤流水号ID',
                                width : 200,
                                sort : true,
                                fixed : 'left'
                            },
                            {
                                field : 'attendanceAddDate',
                                title : '考勤记录添加时间',
                                width : 120,
                                sort : true,
                            },
                            {
                                field : 'stuId',
                                title : '学生学号',
                                width : 120,
                                sort : true,
                                templet : '<div>{{d.student.stuId}}</div>'
                            },
                            {
                                field : 'stuName',
                                title : '学生姓名',
                                width : 120,
                                templet : '<div>{{d.student.stuName}}</div>'
                            },
                            {
                                field : 'attendanceDefaultStartTime',
                                title : '默认的考勤起始时间',
                                width : 160
                            },
                            {
                                field : 'attendanceDefaultEndTime',
                                title : '默认的考勤结束时间',
                                width : 160
                            },
                            {
                                field : 'attendanceActualStartTime',
                                title : '实际的考勤起始时间',
                                width : 160
                            },
                            {
                                field : 'attendanceActualEndTime',
                                title : '实际的考勤结束时间',
                                width : 160
                            },
                            {
                                field : 'studentAttendanceTypeName',
                                title : '考勤类型',
                                width : 120,
                                sort : true,
                                templet : '<div>{{d.studentAttendanceType.studentAttendanceTypeName}}</div>'
                            },
                            {
                                field : 'attendanceActualTimeLength',
                                title : '考勤时长',
                                width : 120,
                                sort : true,
                                templet : function(d) {
                                    return calcAttendanceTime(d.attendanceActualTimeLength);
                                }
                            },
                            {
                                field : 'attendanceDescription',
                                title : '考勤状态的描述'
                            },
                            {
                                title : '操作',
                                toolbar : '#barDemo',
                                width : 150,
                                align : 'center'
                            } 
                        ] ]
                });
    }

    /** 根据给定的毫秒值进行格式化显示 */
    function calcAttendanceTime(diffTimeMillis) {
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

    layui.use(['element', 'form', 'laydate', 'table'], function(){
        // 加载模块
        var element = layui.element;
        var form = layui.form;
        var layer = layui.layer;
        var laydate = layui.laydate;
        var table2 = layui.table;
        // 定义两个筛选开关标识
        var switchTestResult = false;
        var switchTestResult2 = false;
        // 取值
        var stuName = -1
        var dateStart = "";
        var dateEnd = "";
        
      //监听"按学生姓名筛选"开关
        form.on('switch(switchTest)', function(obj){
            switchTestResult = obj.elem.checked ? true : false;
            if (switchTestResult) {
                $("#stu-name").attr("disabled", false);
                // 打开开关时，查询该老师下所有的考勤记录
                $.ajax({
                    type:"GET",
                    url:"teacherMaster/findStudentsByTeacherId",
                    data:{
                        teacherId: $.cookie("teacherId")
                    },
                    dataType:"json",
                    async: false,
                    success:function(result){
                        var students = result.data;
                        var str = "<option value='-1'>-请选择-</option>";
                        for (var i = 0; i < students.length; i++) {
                            str = str + "<option value='" + students[i].student.stuId + "'>" + students[i].student.stuName + "</option>";
                        }
                        $("#stu-name").empty().append(str);
                        layui.use('form', function(){
                            var form = layui.form;
                            // 这里不能加过滤,否则不能渲染成功
                            form.render('select');
                        });
                    }
                });
            }else{
                $("#stu-name").attr("disabled", true);
            }
            form.render('select');
        });

        //监听"按添加日期筛选"开关
        form.on('switch(switchTest2)', function(obj){
            switchTestResult2 = obj.elem.checked ? true : false;
            if (switchTestResult2) {
                $("#date-start").attr("disabled", false);
                $("#date-end").attr("disabled", false);
            }else{
                $("#date-start").attr("disabled", true);
                $("#date-end").attr("disabled", true);
            }
            form.render();
        });
        
        form.on('select(stu-name)', function(data){
            stuName = data.value;
            //alert(data.value + "-" + data.elem[data.elem.selectedIndex].text);
        });

        // 日期筛选-开始日期
        laydate.render({ 
              elem: '#date-start',
              type: 'date',
              done: function(value, date, endDate){
                  dateStart = value; //得到日期生成的值，如：2017-08-18
                }
        });
        
        // 日期筛选-结束日期
        laydate.render({ 
            elem: '#date-end',
            type: 'date',
            done: function(value, date, endDate){
                //监听日期被切换
                dateEnd = value;
            }
        });
        
        // 规则校验
        form.verify({
              // 学生选择校验
            stuNameVerify: function(stuName, item){
                if(stuName == -1){
                  return '请选择学生';
                }
              },
              dateStartVerify: function(dateSart, item){
                  if (!/^(20[1-9][0-9])-((0[1-9])|(1[0-2]))-((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))$/.test(dateSart)) {
                      return '起始时间格式不正确';
                  }
              },
              dateEndVerify: function(dateEnd, item){
                  if (!/^(20[1-9][0-9])-((0[1-9])|(1[0-2]))-((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))$/.test(dateEnd)) {
                      return '结束时间格式不正确';
                  }
              }
        });
        
        // 筛选表单提交事件
        form.on('submit(searchBtn)', function(data){
            // 清空原有表格
            $("#select-box").empty();
            $("#demo").empty();
            
            table2.render({
                elem : '#demo',
                url : 'teacherMaster/findStudentsByConditions',
                toolbar : '#toolbarDemo',
                page : true, // 开启分页
                limits : [ 5, 10, 20, 30, 40, 50 ],
                limit : 10,
                where:{
                    stuId: stuName,
                    date_min: dateStart,
                    date_max: dateEnd,
                },
                response : {
                    // 规定成功的状态码，默认为0，如果不重新规定成功的状态码数据将会无法显示在页面上
                    statusCode : 1006
                },
                parseData : function(res) {
                    // res 即为原始返回的数据
                    // console.log(res);
                    return {
                        "code" : res.respObj.respObjCode, // 解析接口状态
                        "msg" : res.respObj.respObjMsg, // 解析提示文本
                        "count" : res.data.counts, // 解析数据长度
                        "data" : res.data.items // 解析数据列表
                    };
                },
                cols : [ [ // 表头
                        {
                            type : 'checkbox',
                            fixed : 'left'
                        },
                        {
                            field : 'attendanceId',
                            title : '考勤流水号ID',
                            width : 200,
                            sort : true,
                            fixed : 'left'
                        },
                        {
                            field : 'attendanceAddDate',
                            title : '考勤记录添加时间',
                            width : 120,
                            sort : true,
                        },
                        {
                            field : 'stuId',
                            title : '学生学号',
                            width : 120,
                            sort : true,
                            templet : '<div>{{d.student.stuId}}</div>'
                        },
                        {
                            field : 'stuName',
                            title : '学生姓名',
                            width : 120,
                            templet : '<div>{{d.student.stuName}}</div>'
                        },
                        {
                            field : 'attendanceDefaultStartTime',
                            title : '默认的考勤起始时间',
                            width : 160
                        },
                        {
                            field : 'attendanceDefaultEndTime',
                            title : '默认的考勤结束时间',
                            width : 160
                        },
                        {
                            field : 'attendanceActualStartTime',
                            title : '实际的考勤起始时间',
                            width : 160
                        },
                        {
                            field : 'attendanceActualEndTime',
                            title : '实际的考勤结束时间',
                            width : 160
                        },
                        {
                            field : 'studentAttendanceTypeName',
                            title : '考勤类型',
                            width : 120,
                            sort : true,
                            templet : '<div>{{d.studentAttendanceType.studentAttendanceTypeName}}</div>'
                        },
                        {
                            field : 'attendanceActualTimeLength',
                            title : '考勤时长',
                            width : 120,
                            sort : true,
                            templet : function(d) {
                                return calcAttendanceTime(d.attendanceActualTimeLength);
                            }
                        },
                        {
                            field : 'attendanceDescription',
                            title : '考勤状态的描述'
                        },
                        {
                            title : '操作',
                            toolbar : '#barDemo',
                            width : 150,
                            align : 'center'
                        } 
                    ] ],
                    done: function(){
                        laydate.render({
                            elem: '#date-start',
                            type: 'date',
                            done: function(value, date, endDate){
                                //监听日期被切换
                                dateStart = value;
                            }
                        });
                        laydate.render({
                            elem: '#date-end',
                            type: 'date',
                            done: function(value, date, endDate){
                                //监听日期被切换
                                dateEnd = value;
                            }
                        });
                    }
            });
            return false; //阻止表单跳转
         });
    });
    
    function initSearchFlag(f1, f2){
        var submitFlag = (f1 == true || f2 == true);
        if (submitFlag) {
            // 未添加任何筛选条件
            return true;
        }else{
            // 已添加筛选条件
            return false;
        }
    }
    
});