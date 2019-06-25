$(function() {

    // 初始化教师的头像及姓名
	alert(document.URL);
	var tid = document.URL.split('?')[1].split("=")[1];
    initTeacherInfo(tid);
    
    // 初始化加载导航依赖(element)模块
    layui.use('element', function() {
        var element = layui.element;

        // 监听左侧导航点击
        element.on('nav(demo)', function(elem) {
            $(".iframe-box > iframe").attr("src", elem.attr("data-url"));
        });
    });
    /**
     * 初始化教师的头像及姓名
     */
    function initTeacherInfo(tid){
        $.ajax({
            type : "post",
            url : "teacher/teacherInit",
            data : {
            	teacherId: tid
            },
            dataType : "json",
            success : function(result) {
                var data = result.data;
                $(".teacher-img").attr("src", data.img);
                $(".teacher-name").text(data.name);
            }
        });
    }

    // 退出登录
    $(".logout").on("click", function() {
        layui.use('layer', function() {
            var layer = layui.layer;
            layer.confirm('真滴退出登录?', {
                icon : 3,
                title : '提示'
            }, function(index) {
                $.ajax({
                    type : "post",
                    url : "teacher/logout",
                    data : {},
                    dataType : "json",
                    success : function(result) {
                        layer.msg(result.msg);
                        // 注销成功,跳转到登录页面
                        window.location = 'login.html';
                    }
                });
            });
        });
    });
});
