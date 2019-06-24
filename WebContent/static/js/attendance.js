$(function() {
    // 初始化加载导航依赖(element)模块
    layui.use('element', function(){
      var element = layui.element;

      //监听导航点击
      element.on('nav(demo)', function(elem){
        $(".iframe-box > iframe").attr("src", elem.attr("data-url"));
      });

    });
});