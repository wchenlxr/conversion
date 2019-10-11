var serverpath = 'dataimport/excelUpload';
var uploader = WebUploader.create({
    // swf文件路径
    swf: '/webuploader/Uploader.swf',
    // 文件接收服务端。
    server: serverpath,
    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',
    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false,
    accept: {
        title: 'Excel',
        extensions: 'xlsx,xls',
        mimeTypes: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel'
    },
});

var $upload = $("#ctlBtn");
$upload.on('click', function () {
    var dq = $("#dq").val();
    var t = 'file/templateUpload';
    t = t + "?dz=" + dq;
    uploader.options.server = t;
    uploader.upload();
});

//下载模版
var $download = $("#downloadBtn");
$download.on('click', function () {
    $download.submit();
    /*    $.ajax({
     url: 'dataimport/download',
     async: true
     })*/
})

//当有文件被添加进队列的时候
uploader.on('fileQueued', function (file) {
    var $list = $("#thelist");
    $list.append('<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
        '<p class="resultInfo" style="color:red;"></p>' +
        '</div>');
});


//文件上传过程中创建进度条实时显示。
uploader.on('uploadProgress', function (file, percentage) {
    var $li = $('#' + file.id),
        $percent = $li.find('.progress .progress-bar');

    // 避免重复创建
    if (!$percent.length) {
        $percent = $('<div class="progress progress-striped active">' +
            '<div class="progress-bar" role="progressbar" style="width: 0%">' +
            '</div>' +
            '</div>').appendTo($li).find('.progress-bar');
    }

    $li.find('p.state').text('上传中');

    $percent.css('width', percentage * 100 + '%');
});

uploader.on('uploadSuccess', function (file) {
    $('#' + file.id).find('p.state').text('已导入');
});

uploader.on('uploadError', function (file, response) {
    $('#' + file.id).find('p.state').text('导入出错');
});

uploader.on('uploadComplete', function (file) {
    $('#' + file.id).find('.progress').fadeOut();
});
//
uploader.on('uploadAccept', function (file, response) {
//	debugger;
    if (response.success) {
        var $list = $("#thelist");
        $list.find('p.resultInfo').text(response.message);
        return true;
    } else {
        var $list = $("#thelist");
        $list.find('p.resultInfo').text(response.message);
        return false;
    }
});
