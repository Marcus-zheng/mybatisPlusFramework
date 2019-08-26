layer.config({
    extend: 'moon/style.css', //加载新皮肤
    skin: 'layer-ext-moon' //一旦设定，所有弹层风格都采用此主题。
});


let initPlugins = {

    param :{
        language : 'zh',
        theme: 'fa',
        uploadUrl: "/mm/mainPicUpload", //上传的地址
        deleteUrl: "/mm/delete", //删除地址
        uploadAsync: true, //默认异步上传
        autoReplace: true,
        overwriteInitial: true, //
        showCaption:false,
        showUploadedThumbs: false,
        initialPreviewAsData:true,  //
        maxFileCount: 1,  //最大上传文件数为1
        showRemove: true,  //显示移除
        showClose: false,
        layoutTemplates:{
            actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标
        },
        allowedFileExtensions: ["jpg", "png", "gif"]
    },
    getInitialPreviewParam() {

        let param = {};

        if (parent !== undefined && parent !== null)
        {
            let picUrl = parent.document.getElementById("selfShotPhoto").src;
            if (picUrl !== undefined && picUrl !== '')
            {
                param.main = initPlugins.getInitialPreviewAndConfig(picUrl);
                return param;
            }
        }
        param.main = {initialPreviewConfig: [], initialPreview: []};
        return param;

    },

    getInitialPreviewAndConfig(parseUrl) {

        let param = {};
        if (parseUrl !== undefined && parseUrl !== null && parseUrl !== '')
        {
            param.initialPreview = parseUrl.split(',');
            let config = [];
            for (let i = 0; i < param.initialPreview.length; i++)
            {
                let url = param.initialPreview[i];
                let index = url.indexOf('group');
                if (index === -1)
                {
                    continue;
                }

                let keyId = url.substr(index);
                config.push({width: "120px", key: keyId});
            }
            param.initialPreviewConfig = config;
        }
        return param;
    },
    refreshFileInput(selector,url){

        let preview = initPlugins.getInitialPreviewAndConfig(url);


        let reInitialParam = initPlugins.param;

        reInitialParam['layoutTemplates'] = {};

        reInitialParam['initialPreview'] = preview.initialPreview;
        reInitialParam['initialPreviewConfig'] = preview.initialPreviewConfig;

        selector.fileinput('destroy').fileinput(reInitialParam).fileinput('enable');
        $('.btn-file').attr('disabled',false);
    }

};

let param = initPlugins.getInitialPreviewParam();

let mainPicUploader = $("#mainPicFile");
let mainPic = $("input[name='selfShotPhoto']");
mainPicUploader.fileinput({
    language : 'zh',
    theme: 'fa',
    uploadUrl: "/mm/mainPicUpload", //上传的地址
    deleteUrl: "/mm/delete", //删除地址
    uploadAsync: true, //默认异步上传
    autoReplace: true,
    overwriteInitial: true,
    showCaption:false,
    initialPreviewAsData: true,
    showUploadedThumbs: false,
    maxFileCount: 1,
    initialPreview: param.main.initialPreview,
    initialPreviewConfig:param.main.initialPreviewConfig,
    showRemove: true,
    showClose: false,
    allowedFileExtensions: ["jpg", "png", "gif"]
});


mainPicUploader.on('fileuploaded', function(event, data) {

    let result = data.response;

    if (result.code === 0)
    {
        let urlResult = result.data;

        if (urlResult !== undefined && urlResult !== null && urlResult.url !== undefined)
        {
            mainPic.val(urlResult.url);
            initPlugins.refreshFileInput(mainPicUploader,$("input[name='selfShotPhoto']").val());
        }
    }
    else
    {
        layer.msg('上传失败', {icon: 2,shift:6});//弹框提示
    }
});
mainPicUploader.on('filedeleted', function() {
    mainPic.val('');
});

$("#updatePhoto").on('click',function () {

    let val = mainPic.val();

    if (val === undefined ||val === null || val === '')
    {
        layer.msg('图片未上传不能提交',{icon:2,shift: 6});
        return false;
    }
    let mmId = sessionStorage.getItem('mmId');
    let load = layer.load(2);
    $.ajax({
        url:'/mm/updateSimpleInfo',
        dataType:'json',
        method:'post',
        data:{'id':mmId,'selfShotPhoto':val},
        success:function (data) {

            layer.close(load);
            if (data.code === 0)
            {
                layer.msg('保存成功,请刷新',{icon:1});
                if (parent !== undefined && parent !== null)
                {
                    parent.document.getElementById("selfShotPhoto").src = val;
                }
            }
            else
            {
                layer.msg(data.message, {icon: 2, shift: 6});
            }
        },
        error:function (data) {
            console.log(data);
            layer.close(load);
            layer.msg('系统异常', {icon: 2, shift: 6});
        }
    });


});
