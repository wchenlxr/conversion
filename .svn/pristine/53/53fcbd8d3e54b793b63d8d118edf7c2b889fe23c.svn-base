/**
 * Created by Administrator on 2018/3/27.
 */
var editIndex = undefined;
function endEditing() {
    if (editIndex == undefined) {
        return true
    }
    if ($('#dg').datagrid('validateRow', editIndex)) {
        $('#dg').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onEndEdit(index, row) {
/*    var ed = $(this).datagrid('getColumnFields', {
        index: index, field: field
    });*/
}
function onClickCell(index, field) {
    if (editIndex != index) {
        if (endEditing()) {
            $('#dg').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            var ed = $('#dg').datagrid('getEditor', {index: index, field: field});
            if (ed) {
                ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            }
            editIndex = index;
        } else {
            setTimeout(function () {
                $('#dg').datagrid('selectRow', editIndex);
            }, 0);
        }
    }
}

function append(){
    if (endEditing()){
        $('#dg').datagrid('appendRow',{status:'1'});
        editIndex = $('#dg').datagrid('getRows').length-1;
        $('#dg').datagrid('selectRow', editIndex)
            .datagrid('beginEdit', editIndex);
    }
}
function removeit() {
    $.messager.confirm('操作提示', '确定删除该数据?', function (r) {
        if (r) {
            if (editIndex == undefined) {
                return
            }
            $('#dg').datagrid('cancelEdit', editIndex)
                .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
    });
}
function accept() {
    if (endEditing()) {
//            $('#dg').datagrid('acceptChanges');
        if ($('#dg').datagrid('getChanges').length > 0) {
            var inserted = $('#dg').datagrid('getChanges', 'inserted');
            var updated = $('#dg').datagrid('getChanges', 'updated');
            var deleted = $('#dg').datagrid('getChanges', 'deleted');
            for(var i=0;i<inserted.length;i++){
                for(var key in inserted[i]){
                    if(inserted[i]["companyName"]==""){
                    	$.messager.alert('操作提示', "厂商名称不能为空，请输入！");
                    	return;
                    }
                }
             }
            var changes = {
                "inserted": inserted,
                "updated": updated,
                "deleted": deleted
            };
            $.ajax({
                url: 'company/updateCompany',
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
                        $('#dg').datagrid('acceptChanges');
                        $.messager.alert("操作提示", "保存成功!");
                    } else {
                        $.messager.alert("操作提示", data);
                    }
                }
            });
        }
    } else {
        alert("123");
    }
}
function reject() {
    $('#dg').datagrid('rejectChanges');
    editIndex = undefined;
}
function getChanges() {
    var rows = $('#dg').datagrid('getChanges');
    $.messager.alert("操作提示", "修改了" + rows.length + "记录");
}
