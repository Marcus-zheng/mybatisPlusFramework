layer.config({
    extend: 'moon/style.css', //加载新皮肤
    skin: 'layer-ext-moon' //一旦设定，所有弹层风格都采用此主题。
});

let fileInputOpt = {

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

        let preview = fileInputOpt.getInitialPreviewAndConfig(url);


        let reInitialParam = fileInputOpt.param;

        reInitialParam['layoutTemplates'] = {};

        reInitialParam['initialPreview'] = preview.initialPreview;
        reInitialParam['initialPreviewConfig'] = preview.initialPreviewConfig;

        selector.fileinput('destroy').fileinput(reInitialParam).fileinput('enable');
        $('.btn-file').attr('disabled',false);
    }

};

let uploader = $("#mainPicFile");
let mainPic = $("input[name='selfShotPhoto']");

uploader.fileinput(fileInputOpt.param);

uploader.on('fileuploaded', function(event, data, previewId, index) {

    let result = data.response;

    if (result.code === 0)
    {

        let urlResult = result.data;

        if (urlResult !== undefined && urlResult !== null && urlResult.url !== undefined)
        {
            mainPic.val(urlResult.url);
            //刷新已上传文件
            fileInputOpt.refreshFileInput(uploader,$("input[name='selfShotPhoto']").val());

            //重新校验
            $("#configData").bootstrapValidator('updateStatus','selfShotPhoto','NOT_VALIDATED').bootstrapValidator('validateField', 'selfShotPhoto');
        }
    }
    else
    {
        layer.msg('上传失败', {icon: 2,shift:6});//弹框提示
    }
});

uploader.on('filedeleted', function(event, key, jqXHR, data) {
    mainPic.val('');
});

