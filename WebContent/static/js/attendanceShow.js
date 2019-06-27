$(function() {
    layui.use([ 'table', 'layer' ], function() {
        // 加载table模块
        var table = layui.table;
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
                    content : 'attendanceEdit.html?attendanceId='
                            + data.attendanceId
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
                        console.log(res);
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

});