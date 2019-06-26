$(function() {

    // 初始化教师的头像及姓名
	//var tid = document.URL.split('?')[1].split("=")[1];
    //initTeacherInfo(tid);
	initTeacherInfo();
    
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
    function initTeacherInfo(){
        $.ajax({
            type : "post",
            url : "summer/getTeacherName",
            data : {},
            dataType : "json",
            success : function(result) {
                //var data = result.data;
                //$(".teacher-img").attr("src", data.img);
                $(".teacher-name").text(result);
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
                    url : "summer/goodBye",
                    data : {},
                    dataType : "json",
                    success : function(result) {
                        layer.msg(result.msg);
                        // 注销成功,跳转到登录页面
                        window.location = 'LoginTest1.html';
                    }
                });
            });
        });
    });
});
