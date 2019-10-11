var editIndex = undefined;
var editDIndex = undefined;
var pattern = /^[A-Za-z0-9\s.-]+$/;
function endEditing() {
    if (editIndex == undefined) {
        return true
    }
    if ($('#classTable').datagrid('validateRow', editIndex)) {
        $('#classTable').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function endDEditing() {
    if (editDIndex == undefined) {
        return true
    }
    if ($('#detailTable').datagrid('validateRow', editDIndex)) {
        $('#detailTable').datagrid('endEdit', editDIndex);
        editDIndex = undefined;
        return true;
    } else {
        return false;
    }
}
// 分类列表行点击事件
function onClickRow(index, row) {
    loadDetailData(row.platformCode);
}
// 单元格点击事件
function onClickCell(index, field) {
    var row = $(this).datagrid('getData').rows[index];
    if (editIndex != index) {
        if (endEditing()) {
            $(this).datagrid('selectRow', index).datagrid('beginEdit', index);
            if (row.id != null && row.id != undefined) {
                var ed = $(this).datagrid('getEditor', {index: index, field: 'platformCode'});
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
// 单元格点击事件
function onDClickCell(index, field) {
    var row = $(this).datagrid('getData').rows[index];
    if (editDIndex != index) {
        if (endDEditing()) {
            $(this).datagrid('selectRow', index).datagrid('beginEdit', index);
            if (row.id != null && row.id != undefined) {
                var ed = $(this).datagrid('getEditor', {index: index, field: 'detailCode'});
                $(ed.target).combobox('disable');
            } else {
                var ed = $(this).datagrid('getEditor', {index: index, field: field});
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
            }
            editDIndex = index;
        } else {
            setTimeout(function () {
                $(this).datagrid('selectRow', editIndex);
            }, 0);
        }
    }
}
function onEndEdit(index, field) {
    var ed = $(this).datagrid('getColumnFields', {
        index: index, field: field
    });
}
// 分类表格新增
function append() {
    if (endEditing()) {
        $('#classTable').datagrid('appendRow', {status: '1'});

        editIndex = $('#classTable').datagrid('getRows').length - 1;
        $('#classTable').datagrid('selectRow', editIndex)
            .datagrid('beginEdit', editIndex);

    }
}
// 分类明细表格新增
function detailAppend() {
    if (endDEditing()) {
        var ctRow = $('#classTable').datagrid('getSelected');
        if (ctRow == null || ctRow == undefined) {
            $.messager.alert('操作提示', "请先添加平台值域分类！");
            return;
        }
        if (!ctRow.platformCode) {
            $.messager.alert('操作提示', "请先保存平台值域分类数据！");
            return;
        }
        $('#detailTable').datagrid('appendRow', {platformCode: ctRow.platformCode, platformName: ctRow.platformName});
        editDIndex = $('#detailTable').datagrid('getRows').length - 1;
        $('#detailTable').datagrid('selectRow', editDIndex)
            .datagrid('beginEdit', editDIndex);

    }
}
// 分类表移除
function removeit() {

    var row = $('#classTable').datagrid('getSelected');
    if (row == null || row == undefined) {
        $.messager.alert('操作提示', "请先选择一条数据！");
        return;
    }
    $.messager.confirm('操作提示', '确定删除该数据?', function (r) {
        if (r) {
            if (editIndex == undefined) {
                return
            }
            $('#classTable').datagrid('cancelEdit', editIndex)
                .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
    });
}
// 分类明细表移除
function detailRemoveit() {

    var rows = $('#detailTable').datagrid('getSelections');
    if (rows == null || rows == undefined) {
        $.messager.alert('操作提示', "请先选择一条数据！");
        return;
    }
    $.messager.confirm('操作提示', '确定删除该数据?', function (r) {
        if (r) {
            for (var i = 0; i < rows.length; i++) {
                var index = $('#detailTable').datagrid('getRowIndex', rows[i]);
                $('#detailTable').datagrid('deleteRow', index);
            }
            if (editDIndex == undefined) {
                return
            }
            $('#detailTable').datagrid('cancelEdit', editDIndex);
            editDIndex = undefined;
        }
    });
}
// 分类保存操作
function accept() {
    if (endEditing()) {
        if ($('#classTable').datagrid('getChanges').length > 0) {
            var inserted = $('#classTable').datagrid('getChanges', 'inserted');
            var updated = $('#classTable').datagrid('getChanges', 'updated');
            var deleted = $('#classTable').datagrid('getChanges', 'deleted');
            for (var i = 0; i < inserted.length; i++) {
                for (var key in inserted[i]) {
                    if (inserted[i]["platformCode"] == "" || inserted[i]["platformName"] == "") {
                        $.messager.alert('操作提示', "分类编码和名称不能为空，请输入！");
                        return;
                    }
                    if (!pattern.test(inserted[i]["platformCode"])) {
                        $.messager.alert('操作提示', "分类编码只能为数字、英文大小写、空格、中划线");
                        return;
                    }
                }
            }

        }
        var url = "dictionary/updateRangeClass";
        var changes = {
            "inserted": inserted,
            "updated": updated,
            "deleted": deleted
        };
        $.ajax({
            url: url,
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
                } else {
                    $.messager.alert("操作提示", data);
                }
                $('#classTable').datagrid('load', {});
            }
        });
    }

}
// 分类明细保存操作
function detailAccept() {
    if (endDEditing()) {
        if ($('#detailTable').datagrid('getChanges').length > 0) {
            var inserted = $('#detailTable').datagrid('getChanges', 'inserted');
            var updated = $('#detailTable').datagrid('getChanges', 'updated');
            var deleted = $('#detailTable').datagrid('getChanges', 'deleted');
            for (var i = 0; i < inserted.length; i++) {
                for (var key in inserted[i]) {
                    if (inserted[i]["detailCode"] == "" || inserted[i]["detailName"] == "") {
                        $.messager.alert('操作提示', "值编码和名称不能为空，请输入！");
                        return;
                    }
                }
            }
        }
        var url = "dictionary/updateRangeDetail";
        var changes = {
            "inserted": inserted,
            "updated": updated,
            "deleted": deleted
        };
        $.ajax({
            url: url,
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
                } else {
                    $.messager.alert("操作提示", data);
                }
                $('#detailTable').datagrid('reload', {});
            }
        });
    }

}
// 撤销
function reject(obj) {
    $(obj).datagrid('rejectChanges');
    editIndex = undefined;
    editDIndex = undefined;
}
// 查询修改记录数
function getChanges(obj) {
    // var rows = $(obj).datagrid('getChanges');
    // $.messager.alert("操作提示", "修改了" + rows.length + "记录");
}
$('#classTable').datagrid({
    onLoadSuccess: function (data) {
        var dataObj = $('#classTable').datagrid('getData')
        if (dataObj.total > 0) {
            $('#classTable').datagrid('selectRow', 0);
            loadDetailData(dataObj.rows[0].platformCode);
            editIndex = 0;
        }
    }
});
// 加载分类详细列表数据
function loadDetailData(platformCode) {
    $('#detailTable').datagrid({
        url: 'dictionary/queryRangeDetail?platformCode=' + encodeURIComponent(platformCode)
    });
}

function doSearch(value, name) {
    $('#classTable').datagrid('load', {
        type: name,
        value: value
    });
}

$(function () {
    $("#classTable").datagrid({
        title: '平台值域分类',
        iconCls: 'icon-edit',
        width: '100%',
        onClickRow: onClickRow,
        onClickCell: onClickCell,
        onEndEdit: onEndEdit,
        pageSize: 10,
        pageList: [5, 10, 15, 20],
        nowrap: true,
        striped: true,
        collapsible: true,
        singleSelect: true,
        url: 'dictionary/queryRangeClass/',
/*        queryParams: {
            status: 1
        },*/
        loadMsg: '数据加载中......',
        fitColumns: true,
        remoteSort: false,
        toolbar: '#classTb',
        onLoadSuccess: function (data) {
            if (data.rows.length > 0) {
                $('#ptfltype').datagrid("selectRow", 0);
            }
        },
        onSelect: function (index, row) {
            $('#detailTable').datagrid({
                url: 'dictionary/queryRangeDetail?platformCode=' + row.platformCode
            });
        },
        columns: [[{
            field: 'platformCode',
            width: '20%',
            title: '分类编码',
            editor: 'textbox',
            required: true
        }, {
            field: 'platformName',
            width: '35%',
            title: '分类名称',
            editor: 'textbox',
            required: true
        }, {
            field: 'remark',
            width: '35%',
            editor: 'textbox',
            title: '描述'
        }, {
            field: 'status',
            width: '10%',
            editor: {type: 'checkbox', options: {on: '1', off: '0'}},
            title: '启用状态'
        }]],
        pagination: true,
        rownumbers: true
    })

    $("#detailTable").datagrid({
        title: '平台值域分类明细',
        iconCls: 'icon-edit',
        width: '100%',
        onClickCell: onDClickCell,
        onEndEdit: onEndEdit,
        pageSize: 10,
        pageList: [5, 10, 15, 20],
        nowrap: true,
        striped: true,
        collapsible: true,
        singleSelect: false,
        loadMsg: '数据加载中......',
        fitColumns: true,
        sortName: 'detailCode',
        sortOrder: 'asc',
        remoteSort: false,
        toolbar: '#detailTb',
        onLoadSuccess: function (data) {
            // if (data.rows.length > 0) {
            //     $('#detailTable').datagrid("selectRow", 0);
            // }
        },
        onSelect: function (index, row) {
        },
        columns: [[{
            field: 'detailCode',
            width: '25%',
            required: true,
            editor: 'textbox',
            title: '值编码'
        }, {
            field: 'detailName',
            width: '70%',
            required: true,
            editor: 'textbox',
            title: '值名称'
        }, {
            field: 'platformName',
            hidden: true,
            title: '所属分类'
        }, {
            field: 'platformCode',
            hidden: true,
            title: '所属分类'
        }, {
            field: 'ck',
            checkbox: true
        }]],
        pagination: true,
        rownumbers: true
    })
});