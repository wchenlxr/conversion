var editIndex = undefined;
var editDIndex = undefined;
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
function onClickRow(index, row){
	loadDetailData(row.platformCode );
}
// 单元格点击事件
function onClickCell(index, field) {
	var row = $(this).datagrid('getData').rows[index];
    if (editIndex != index) {
        if (endEditing()) {
        	$(this).datagrid('selectRow', index).datagrid('beginEdit', index);
        	if(row.id!=null&&row.id!=undefined){
    			var ed = $(this).datagrid('getEditor', {index: index, field: 'platformCode'});
        		$(ed.target).combobox('disable');
        	}else{
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
			if(row.id!=null&&row.id!=undefined){
				var ed = $(this).datagrid('getEditor', {index: index, field: 'detailCode'});
				$(ed.target).combobox('disable');
			}else{
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
    	$('#classTable').datagrid('appendRow',{status:'1'});
    	
        editIndex = $('#classTable').datagrid('getRows').length-1;
        $('#classTable').datagrid('selectRow', editIndex)
            .datagrid('beginEdit', editIndex);
        
    }
}
// 分类明细表格新增
function detailAppend() {
	if (endDEditing()) {
			var ctRow = $('#classTable').datagrid('getSelected');
			if(ctRow==null||ctRow==undefined){
				$.messager.alert('操作提示', "请先添加平台值域分类！");
				return;
			}
			if(!ctRow.platformCode){
				$.messager.alert('操作提示', "请先保存平台值域分类数据！");
				return;
			}
			$('#detailTable').datagrid('appendRow',{platformCode:ctRow.platformCode,platformName:ctRow.platformName});
			editDIndex = $('#detailTable').datagrid('getRows').length-1;
		$('#detailTable').datagrid('selectRow', editDIndex)
		.datagrid('beginEdit', editDIndex);
		
	}
}
// 分类表移除
function removeit() {
	
	var row = $('#classTable').datagrid('getSelected');
	if(row==null||row==undefined){
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
	
	var row = $('#detailTable').datagrid('getSelected');
	if(row==null||row==undefined){
		$.messager.alert('操作提示', "请先选择一条数据！");
		return;
	}
	$.messager.confirm('操作提示', '确定删除该数据?', function (r) {
		if (r) {
			if (editDIndex == undefined) {
				return
			}
			$('#detailTable').datagrid('cancelEdit', editDIndex)
			.datagrid('deleteRow', editDIndex);
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
                for(var i=0;i<inserted.length;i++){
                    for(var key in inserted[i]){
                        if(inserted[i]["platformCode"]==""||inserted[i]["platformName"]==""){
                        	$.messager.alert('操作提示', "分类编码和名称不能为空，请输入！");
                        	return;
                        }
                    }
                 }
                
    		}
    		var url="dictionary/updateRangeClass";
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
                        $('#classTable').datagrid('load',{});
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
			for(var i=0;i<inserted.length;i++){
                for(var key in inserted[i]){
                    if(inserted[i]["detailCode"]==""||inserted[i]["detailName"]==""){
                    	$.messager.alert('操作提示', "值编码和名称不能为空，请输入！");
                    	return;
                    }
                }
             }
		}
		var url="dictionary/updateRangeDetail";
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
				$('#classTable').datagrid('load',{});
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
	var rows = $(obj).datagrid('getChanges');
    $.messager.alert("操作提示", "修改了" + rows.length + "记录");
}
$('#classTable').datagrid({onLoadSuccess : function(data){
    var dataObj = $('#classTable').datagrid('getData')
    if(dataObj.total>0){
    	$('#classTable').datagrid('selectRow',0);
    	loadDetailData(dataObj.rows[0].platformCode);
    	editIndex=0;
    }
}});
// 加载分类详细列表数据
function loadDetailData(platformCode){
	$('#detailTable').datagrid({   
        url:'dictionary/queryRangeDetail?platformCode='+ platformCode
    }); 
}

function doSearch(value, name) {
    $('#classTable').datagrid('load', {
        type: name,
        value: value
    });
}

$(document).ready(function(){
	
});