/**
 * Created by Administrator on 2018/3/20.
 */
var menuid = undefined;
var editIndex = undefined;
var editPPIndex = undefined;

function endEditing() {
    if (editIndex == undefined) {
        return true
    }
    if ($('#cszydz').datagrid('validateRow', editIndex)) {
        $('#cszydz').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}

function endPPEditing() {
    if (editPPIndex == undefined) {
        return true
    }
    if ($('#ptzy').datagrid('validateRow', editPPIndex)) {
        $('#ptzy').datagrid('endEdit', editPPIndex);
        editPPIndex = undefined;
        return true;
    } else {
        return false;
    }
}

//单元格点击事件
function onClickCell(index, field) {
    var row = $(this).datagrid('getData').rows[index];
    if (editIndex != index) {
        if (endEditing()) {
            $(this).datagrid('selectRow', index).datagrid('beginEdit', index);
            if (row.id != null && row.id != undefined) {
                var ed = $(this).datagrid('getEditor', {index: index, field: 'companyRangeCode'});
                $(ed.target).combobox('disable');
            } else {
                var ed = $(this).datagrid('getEditor', {index: index, field: field});
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
            }
            editIndex = index;
        } else {
            setTimeout(function () {
                $(this).datagrid('selectRow', editIndex);
            }, 0);
        }
    }
}

//单元格点击事件
function onPPClickCell(index, field) {
    if (editPPIndex != index) {
        if (endPPEditing()) {
            $(this).datagrid('selectRow', index).datagrid('beginEdit', index);
            var ed = $(this).datagrid('getEditor', {index: index, field: field});
            if (ed) {
                ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            }
            var radio = $("input[name='selected']")[index].disabled;
            //如果当前的单选框不可选，则不让其选中
            if (radio != true) {
                //让点击的行单选按钮选中
                $("input[name='selected']")[index].checked = true;
            } else {
                $("input[name='selected']")[index].checked = false;
            }
            editPPIndex = index;
        } else {
            setTimeout(function () {
                $(this).datagrid('selectRow', editPPIndex);
            }, 0);
        }
    }
}

//新增
function append() {
    if (endEditing()) {
        var flRow = $('#ptfltype').datagrid('getSelected');
        if (flRow == null || flRow == undefined) {
            $.messager.alert("提示信息", "请先在【系统管理->平台字典】模块添加平台值域分类！");
            return;
        }
        $('#cszydz').datagrid('appendRow', {
            companyCode: menuid, platformRangeCode: flRow.platformCode,
            platformRangeName: flRow.platformName
        });
        editIndex = $('#cszydz').datagrid('getRows').length - 1;
        $('#cszydz').datagrid('selectRow', editIndex)
            .datagrid('beginEdit', editIndex);

    }
}

//移除
function removeit() {
    var row = $('#cszydz').datagrid('getSelected');
    if (row == null || row == undefined) {
        $.messager.alert("提示信息", "请先选择一条数据！");
        return;
    }
    $.messager.confirm('操作提示', '确定删除该数据?', function (r) {
        if (r) {
            if (editIndex == undefined) {
                return
            }
            $('#cszydz').datagrid('cancelEdit', editIndex)
                .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
    });
}

// 保存操作
function accept() {
    if (endEditing()) {
        if ($('#cszydz').datagrid('getChanges').length > 0) {
            var inserted = $('#cszydz').datagrid('getChanges', 'inserted');
            var updated = $('#cszydz').datagrid('getChanges', 'updated');
            var deleted = $('#cszydz').datagrid('getChanges', 'deleted');
            for (var i = 0; i < inserted.length; i++) {
                for (var key in inserted[i]) {
                    if (inserted[i]["companyRangeCode"] == "" || inserted[i]["companyRangeName"] == "") {
                        $.messager.alert("提示信息", "厂商值域编码和名称不能为空，请输入！");
                        return;
                    }
                }
            }
        }
        var changes = {
            "inserted": inserted,
            "updated": updated,
            "deleted": deleted
        };
        $.ajax({
            url: 'contrast/updateRangeContrast',
            type: 'post',
            async: true,
            contentType: 'application/json;charset=utf-8',
            dataType: 'text',
            data: JSON.stringify(changes),
            error: function (data) {
                $.messager.alert("操作提示", JSON.stringify(data));
            },
            success: function (data) {
                if (data == "ok") {
                    $.messager.alert("操作提示", "保存成功!");
                    var ppFlag = $('input[name="ppFlag"]:checked').val();
                    var row = $('#ptfltype').datagrid('getSelected');
                    $('#cszydz').datagrid('reload', {
                        companyCode: menuid,
                        platformRangeCode: row.platformCode,
                        ppFlag: ppFlag
                    });
                } else {
                    $.messager.alert("操作提示", data);
                }
            }
        });
    }

}

// 保存操作
function acceptPP() {
    debugger;
    var dzRow = $('#cszydz').datagrid('getSelected');
    var ptzyRow = $('#ptzy').datagrid('getSelected');
    var platformDetailCode = undefined;
    /*  var score = undefined;
     var id = undefined;
     var platformDetailName = undefined;*/
    platformDetailCode = ptzyRow.platformDetailCode
    /*    platformDetailCode = $("input[type='radio']:checked").attr("platformDetailCode");
     score = $("input[type='radio']:checked").attr("score");
     id = $("input[type='radio']:checked").attr("tempid");
     platformDetailName = $("input[type='radio']:checked").attr("platformDetailName");*/
    if (platformDetailCode == undefined) return;
    var changes = {
        "inserted": undefined,
        "updated": [{
            "companyCode": menuid,
            "platformRangeCode": dzRow.platformRangeCode,
            "companyRangeCode": dzRow.companyRangeCode,
            "platformDetailCode": platformDetailCode,
            "id": ptzyRow.id,
            "score": ptzyRow.score,
            "selected": 1,
            "platformDetailName": ptzyRow.platformDetailName
        }],
        "deleted": undefined
    }
    $.ajax({
        url: 'contrast/updateContrast',
        type: 'post',
        async: true,
        contentType: 'application/json;charset=utf-8',
        dataType: 'text',
        data: JSON.stringify(changes),
        error: function (data) {
            $.messager.alert("操作提示", JSON.stringify(data));
        },
        success: function (data) {
            if (data == "ok") {
                $.messager.alert("操作提示", "保存成功!");
                var ppFlag = $('input[name="ppFlag"]:checked').val();
                /*                $('#cszydz').datagrid('reload', {
                                    companyCode: menuid,
                                    platformRangeCode: dzRow.platformRangeCode,
                                    platformDetailName: ptzyRow.platformDetailName,
                                    ppFlag: ppFlag
                                });*/
            } else {
                $.messager.alert("操作提示", data);
            }
        }
    });
}

// 撤销
function reject() {
    $('#cszydz').datagrid('rejectChanges');
    editIndex = undefined;
}

function filesave() {
    var url = "/file/contrastDownload";
    var flRow = $('#ptfltype').datagrid('getSelected');
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "platformRangeCode").attr("value", flRow.platformCode));
    form.append($("<input></input>").attr("type", "hidden").attr("name", "companyCode").attr("value", menuid));
    form.appendTo('body').submit().remove();
}

function load() {
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height()
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("转码中，请稍候。。。").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2
    });
}

//取消加载层
function disLoad() {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();

}

//转码
function transcode() {
    var flRow = $('#ptfltype').datagrid('getSelected');
    var rowindex = $('#ptfltype').datagrid('getRowIndex', flRow);
    if (flRow == null || flRow == undefined) {
        $.messager.alert("提示信息", "请先在【系统管理->平台字典】模块添加平台值域分类！");
        return;
    }
    $.ajax({
        url: '/contrast/transcode?companyCode=' + menuid + '&platformRangeCode=' + encodeURIComponent(flRow.platformCode),
        type: 'GET',
        async: true,
        contentType: 'application/json;charset=utf-8',
        dataType: 'text',
        error: function (data) {
            $.messager.alert("操作提示", JSON.stringify(data));
        },
        beforeSend: function () {
            load();
        },
        complete: function () {
            disLoad();
        },
        success: function (data) {
            if (data == "ok") {
                $.messager.alert("操作提示", "保存成功!");
                // $('#ptfltype').datagrid('reload');
                $('#ptfltype').datagrid("scrollTo", rowindex);
                $('#ptfltype').datagrid("selectRow", rowindex);
            } else {
                $.messager.alert("操作提示", data);
            }
        }
    });
}

function doSearch(value, name) {
    $('#ptfltype').datagrid('load', {
        type: name,
        value: value,
        status: 1
    });
}

function mmDoSearch(value, name) {
    var flRow = $('#ptfltype').datagrid('getSelected');
    var ppFlag = $('input[name="ppFlag"]:checked').val();
    $('#cszydz').datagrid('load', {
        companyCode: menuid,
        platformRangeCode: flRow.platformCode,
        type: name,
        value: value,
        ppFlag: ppFlag
    });
}

function ppFlagChange() {
    var flRow = $('#ptfltype').datagrid('getSelected');
    var ppFlag = $('input[name="ppFlag"]:checked').val();
    var scVal = $('#sc').val();
    var name = null;
    if (scVal != "") name = $('#sc').searchbox('getName');
    $('#cszydz').datagrid('load', {
        companyCode: menuid,
        platformRangeCode: flRow.platformCode,
        type: name,
        value: scVal,
        ppFlag: ppFlag
    });
}

$(function () {
    var a = $(window.parent.document).find(".tabs-selected").find("a").each(function () {
        var c = $(this).attr("class");
        if (c.indexOf("neumid") >= 0) {
            menuid = c.substr(7, c.length);
        }
    })

    $("#ptfltype").datagrid({
        title: '平台值域分类',
        iconCls: 'icon-ok',
        width: '100%',
        pageSize: 10,
        pageList: [5, 10, 15, 20],
        nowrap: true,
        striped: true,
        collapsible: true,
        singleSelect: true,
        url: 'dictionary/queryRangeClass/',
        queryParams: {
            status: 1
        },
        loadMsg: '数据加载中......',
        fitColumns: true,
        sortName: 'platformCode',
        sortOrder: 'asc',
        remoteSort: false,
        toolbar: [{
            iconCls: 'icon-undo',
            handler: function () {
                $.messager.confirm('操作提示', '确定删除该数据? 确定后会清空该转码记录重新匹配', function (r) {
                    if (r) {
                        transcode();
                    }
                });
            },
            text: '转码匹配'
        }],
        onLoadSuccess: function (data) {
            if (data.rows.length > 0) {
                $('#ptfltype').datagrid("selectRow", 0);
            }
        },
        onSelect: function (index, row) {
            var ppFlag = $('input[name="ppFlag"]:checked').val();
            // $('#cszydz').datagrid('loadData',{total:0,rows:[]});
            $('#ptzy').datagrid('loadData', {total: 0, rows: []});
            $('#cszydz').datagrid('options').url = 'contrast/searchRangeContrast';
            $('#cszydz').datagrid('load', {
                companyCode: menuid,
                platformRangeCode: row.platformCode,
                ppFlag: ppFlag
            });
        },
        columns: [[{
            field: 'platformCode',
            width: '30%',
            title: '分类编码'
        }, {
            field: 'platformName',
            width: '70%',
            title: '分类名称'
        }]],
        pagination: true,
        rownumbers: true
    })

    $("#cszydz").datagrid({
        title: '业务厂商编码值域对照',
        iconCls: 'icon-ok',
        width: '100%',
        pageSize: 10,
        pageList: [5, 10, 15, 20],
        nowrap: true,
        striped: true,
        collapsible: true,
        singleSelect: true,
        loadMsg: '数据加载中......',
        fitColumns: true,
        sortName: 'companyRangeCode',
        sortOrder: 'asc',
        remoteSort: false,
        toolbar: [{
            iconCls: 'icon-add2',
            handler: function () {
                append();
            },
            text: '新增'
        }, {
            iconCls: 'icon-remove',
            handler: function () {
                removeit();
            },
            text: '删除'
        }, {
            iconCls: 'icon-save',
            handler: function () {
                accept();
            },
            text: '保存'
        }, {
            iconCls: 'icon-undo',
            handler: function () {
                reject();
            },
            text: '恢复'
        }, {
            iconCls: 'icon-save',
            handler: function () {
                filesave();
            },
            text: 'excel导出'
        }],
        onClickCell: onClickCell,
        onLoadSuccess: function (data) {
            /*            if (data.rows.length > 0) {
                            $('#cszydz').datagrid("selectRow", 0);
                        }*/
            /*            for (var i = 0; i < data.rows.length; i++) {
            /!*                var index = $('#cszydz').datagrid('getRowIndex', rows[i]);
                            $('#detailTable').datagrid('deleteRow', index);*!/
                        }*/
        },
        rowStyler: function (index, row) {
            if (row.sdStatus == '1') {
                return 'background-color:red;';
            }
        },
        onSelect: function (index, row) {
            $('#ptzy').datagrid('options').url = 'contrast/getContrast';
            $('#ptzy').datagrid('load', {
                companyCode: menuid,
                platformRangeCode: row.platformRangeCode,
                companyRangeCode: row.companyRangeCode
            });
        },
        columns: [[{
            field: 'companyCode',
            width: 75,
            title: '厂商编码',
            hidden: true
        }, {
            field: 'platformRangeCode',
            width: 75,
            title: '平台分类编码',
            hidden: true
        }, {
            field: 'platformRangeName',
            width: 75,
            title: '平台分类名称',
            hidden: true
        }, {
            field: 'companyRangeCode',
            width: 75,
            title: '厂商值域编码',
            editor: 'textbox'
        }, {
            field: 'companyRangeName',
            width: 200,
            title: '厂商值域名称',
            editor: 'textbox'
        }, {
            field: 'platformDetailCode',
            width: 75,
            title: '平台编码'
        }, {
            field: 'platformDetailName',
            width: 200,
            title: '平台编码名称'
        }, {
            field: 'sdStatus',
            width: 200,
            title: '手工对照标志'
        }]],
        pagination: true,
        rownumbers: true
    })

    $("#ptzy").datagrid({
        title: '平台值域',
        iconCls: 'icon-ok',
        width: '100%',
        pageSize: 10,
        pageList: [5, 10, 15, 20],
        nowrap: true,
        striped: true,
        collapsible: true,
        singleSelect: true,
        loadMsg: '数据加载中......',
        fitColumns: true,
        sortName: 'score',
        sortOrder: 'desc',
        remoteSort: false,
        toolbar: [{
            iconCls: 'icon-save',
            handler: function () {
                acceptPP();
            },
            text: '保存'
        }, {
            iconCls: 'icon-undo',
            handler: function () {
                reject();
            },
            text: '恢复'
        }],
        onClickCell: onPPClickCell,
        columns: [[{
            field: 'platformDetailCode',
            width: 75,
            title: '平台编码'
        }, {
            field: 'platformDetailName',
            width: 250,
            title: '平台名称'
        }, {
            field: 'id',
            width: 75,
            title: 'id',
            hidden: true
        }, {
            field: 'score',
            width: 75,
            title: '百分比'
        }, {
            field: 'selected',
            width: 50,
            title: '选中',
            formatter: function (value, row, index) {
                if (row.selected == 1) {
                    var s = '<input name="selected" checked type="radio" platformDetailName="' + row.platformDetailName + '" tempid=' + row.id + ' score=' + row.score + '  platformDetailCode=' + row.platformDetailCode + '></input>';
                } else {
                    var s = '<input name="selected" type="radio" platformDetailName=""' + row.platformDetailName + '" tempid=' + row.id + ' score=' + row.score + '  platformDetailCode=' + row.platformDetailCode + '></input>';
                }
                return s;
            }
        }]],
        pagination: true,
        rownumbers: true
    })
})