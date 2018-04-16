var _menus = undefined;
$(function () {
    $.ajax({
        url: "getMenu",
        type: 'get',
        async: true,
        dataType: 'text',
        error: function () {
            $.messager.alert("操作提示", "获取导航菜单失败!");
        },
        success: function (data) {
            _menus = JSON.parse(data);
            $("#nav").accordion({animate: false});

            $.each(_menus.menus, function (i, n) {
                var menulist = '';
                menulist += '<ul>';
                $.each(n.lists, function (j, o) {
                    menulist += '<li><div><a ref="' + o.menuid + '" href="#" rel="' + o.url + '" ><span class="nav">' + o.menuname + '</span></a></div></li> ';
                })
                menulist += '</ul>';
                $('#nav').accordion('add', {
                    title: n.menuname,
                    content: menulist
                });

            });
            $('.easyui-accordion li a').click(function () {
                var tabTitle = $(this).children('.nav').text();
                var url = $(this).attr("rel");
                var menuid = $(this).attr("ref");
                addTab(tabTitle, url, menuid);
                $('.easyui-accordion li div').removeClass("selected");
                $(this).parent().addClass("selected");
            }).hover(function () {
                $(this).parent().addClass("hover");
            }, function () {
                $(this).parent().removeClass("hover");
            });

            //选中第一个
            var panels = $('#nav').accordion('panels');
            var t = panels[0].panel('options').title;
            $('#nav').accordion('select', t);

        }
    })

})
function addTab(subtitle, url, menuid) {
    if (!$('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('add', {
            title: subtitle,
            content: createFrame(url, menuid),
            closable: true,
            tools: [{
                iconCls: "neumid="+menuid
            }]
        });
    } else {
        $('#tabs').tabs('select', subtitle);
    }
    tabClose();
}
function tabClose() {
    /*双击关闭TAB选项卡*/
    $(".tabs-inner").dblclick(function () {
        var subtitle = $(this).children(".tabs-closable").text();
        $('#tabs').tabs('close', subtitle);
    })
    /*为选项卡绑定右键*/
    $(".tabs-inner").bind('contextmenu', function (e) {
        $('#mm').menu('show', {
            left: e.pageX,
            top: e.pageY
        });
        var subtitle = $(this).children(".tabs-closable").text();

        $('#mm').data("currtab", subtitle);
        $('#tabs').tabs('select', subtitle);
        return false;
    });
}
function createFrame(url, menuid) {
    var s = '<iframe longdesc="' + menuid + '" scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"/>';
    return s;
}