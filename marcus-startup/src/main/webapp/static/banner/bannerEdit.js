let banner;

let configData = $('#configData');


let validator = {
    message: 'This value is not valid',
    feedbackIcons: {
        valid: 'glyphicon glyphicon-ok',
        invalid: 'glyphicon glyphicon-remove',
        validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
        name: {
            validators: {
                notEmpty: {
                    message: '广告名称不能为空'
                }
            }
        },
        sort: {
            validators: {
                notEmpty: {
                    message: '排序值不能为空'
                }
            }
        },
        adPhoto: {
            validators: {
                notEmpty: {
                    message: '广告图片不能为空'
                }
            }
        },
        adLink: {
            validators: {
                notEmpty: {
                    message: '广告链接不能为空'
                }
            }
        }
    }
};
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

        let param = fileInputOpt.param;
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
let mainPic = $("input[name='adPhoto']");



uploader.on('fileuploaded', function(event, data, previewId, index) {

    let result = data.response;

    if (result.code === 0)
    {

        let urlResult = result.data;

        if (urlResult !== undefined && urlResult !== null && urlResult.url !== undefined)
        {
            mainPic.val(urlResult.url);
            //刷新已上传文件
            fileInputOpt.refreshFileInput(uploader,mainPic.val());

            //重新校验
            $("#configData").bootstrapValidator('updateStatus','adPhoto','NOT_VALIDATED').bootstrapValidator('validateField', 'adPhoto');
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

configData.bootstrapValidator(getValidator()).on('success.form.bv', function(e) {
    e.preventDefault();
    doSubmit();
});

function getValidator(){
    let item = sessionStorage.getItem('banner');
    if (item !== undefined && item !== null)
    {
        delete validator.fields.name.validators.remote;
        delete validator.fields.sort.validators.remote;
        delete validator.fields.adPhoto.validators.remote;
        delete validator.fields.adLink.validators.remote;
    }
    return validator;

}

$(function () {
    let item = sessionStorage.getItem('banner');
    if (item !== undefined && item !== null && item !== '') {
        banner = JSON.parse(item);
        $('.query-cond').each(function () {
            let attr = $(this).attr('name');
            if (attr === 'adPhoto') {
                let param = fileInputOpt.getInitialPreviewAndConfig(banner[attr]);
                param['layoutTemplates'] = {};
                uploader.fileinput(param);
            }
            if (attr === 'startTime' || attr === 'endTime') {
                $(this).datetimepicker({
                    locale:'zh-cn',
                    viewMode:'days',
                    defaultDate: banner[attr]
                });
            } else {
                $(this).val(banner[attr]);
            }
        });
        $('.read-cond').each(function () {
            $(this).attr('readonly',true);
        })
    } else {
        uploader.fileinput(fileInputOpt.param);
        $('#startTime').datetimepicker({
            locale:'zh-cn',
            viewMode:'days',
            defaultDate: new Date().setHours(0,0,0)
        });

        $('#endTime').datetimepicker({
            locale:'zh-cn',
            viewMode:'days',
            defaultDate:new Date().setHours(23,59,59)
        });
    }
});

function doSubmit() {
    $.ajax({
        url:'/adBanner/save',
        data:Plain.getParam('.query-cond'),
        method:'post',
        dataType:'json',
        success:function (data) {
            if (data.code === 0)
            {
                sessionStorage.removeItem('banner');
                window.location.href='/adBanner/index';
            }
            else
            {
                layer.msg(data.message,{icon:2,shift:6});
            }
        },
        error:function (data) {
            console.log(data);
            layer.msg('系统异常',{icon:2,shift:6});
        }
    });

}

$("#back").on('click',function () {
    sessionStorage.removeItem('banner');
    window.location.href='/adBanner/index';
});