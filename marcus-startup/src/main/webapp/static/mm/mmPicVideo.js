let mmPicVideoOpt = {


    initTable(mmPicVideoList){


        $("#createPicVideo").on('click',function () {

            $('#config-Data').bootstrapValidator('resetForm',true);
            $('#mainPicFile').fileinput('clear');

            $('#pic-video-dialog').modal('show');

        });
        mmPicVideoOpt.initValidator();
        mmPicVideoOpt.initFileUploadPlugin();


        let $table = $('#mmPicVideo-list');

        $table.bootstrapTable({
            data:mmPicVideoList,
            toolbar: "#toolbar",
            striped: true,
            pageSize: 10,
            pageList: [10, 20, 50, 100],
            pagination: true,
            sidePagination: "client",
            columns: [
                {
                    title: 'id',
                    align: 'center',
                    field: 'id',
                    visible:false
                },
                {
                    title: '文件名称',
                    align: 'center',
                    field: 'fileName',
                },
                {
                    title: '文件类型',
                    align: 'center',
                    field: 'fileType',
                    formatter:function (cellvalue) {

                        if (cellvalue === 0)
                        {
                            return "图片";
                        }
                        return "视频";
                    }
                },
                {
                    title: '排序值',
                    align: 'center',
                    field: 'sort',
                },
                {
                    title: '操作',
                    align: 'center',
                    formatter:function () {

                        let btn = [];
                        let edit = "<button class=\'mm-pic-video-preview btn btn-primary btn-sm\'><i class='fa fa-search-plus'></i>预览</button>";
                        let deleteBtn= "<button class=\'mm-pic-video-delete btn btn-danger btn-sm\'>删除</button>";

                        btn.push(edit);
                        btn.push(deleteBtn);
                        return btn.join(" ");
                    },
                    events: {
                        'click .mm-pic-video-preview ': preview,
                        'click .mm-pic-video-delete': deletePicVideo
                    }
                }],
            onLoadSuccess: function (data) {
            }
        });

        function preview(e, val, row) {

            let fileURL = row.fileUrl;
            let sWidth = $(window).width(), sHeight = $(window).height();
            if (row.fileType === 0)
            {
                layer.open({
                    id: 2,
                    type: 1,
                    title: "",
                    area: [sWidth*0.85 + 'px',sHeight*0.85 + 'px'],
                    content: '<div class="text-center" style="margin-top: 10px;height:'+ sHeight*0.8 +'px"><img src=\"' + fileURL + '\" style="width:auto;height:auto;max-width:100%;max-height:100%;" alt="暂缺"></div>',
                });
            }
            else
            {
                layer.open({
                    id: 2,
                    type: 1,
                    title: "",
                    area: [sWidth*0.85 + 'px',sHeight*0.85 + 'px'],
                    content: '<div class="text-center" style="margin-top: 10px;height:'+ sHeight*0.8 +'px"><video src=\"' + fileURL + '\" controls="controls" style="width:auto;height:auto;max-width:100%;max-height:100%;">您的浏览器不支持 video 标签</video></div>',
                });
            }
        }

        function deletePicVideo(e, val, row) {

            layer.confirm('确认删除吗?',function (index) {
                layer.close(index);

                let load = layer.load(2);
                $.ajax({
                    url:'/mm/deletePicVideo',
                    dataType:'json',
                    method:'post',
                    data:{'id':row.id},
                    success:function (data) {

                        layer.close(load);
                        if (data.code === 0)
                        {
                            $('#mmPicVideo-list').bootstrapTable('remove',{field:'id',values:[row.id]});
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
        }
    },
    initValidator(){

        let configData = $("#config-Data");
        let validatorParam = {
            param: {
                message: 'This value is not valid',
                excluded: [':disabled'],
                fields: {
                    fileName: {
                        message: '文件名不能为空',
                        validators: {
                            notEmpty: {
                                message: '昵称不能为空'
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
                    fileUrl: {
                        validators: {
                            notEmpty: {
                                message: '文件未上传'
                            }
                        }
                    }
                }
            },
            doSubmit() {

                let url = '/mm/savePicVideo';

                let saveParam = Plain.getParam('.pic-video-create');

                $.ajax({
                    url: url,
                    data: saveParam,
                    method: 'post',
                    dataType: 'json',
                    success: function (data) {

                        if (data.code === 0)
                        {
                            $('#mmPicVideo-list').bootstrapTable('append',data.data);
                            $('#pic-video-dialog').modal('hide');
                        }
                        else
                        {
                            layer.msg(data.message, {icon: 2, shift: 6});
                        }
                    },
                    error: function (data) {

                        console.log(data);

                        layer.msg('系统异常', {icon: 2, shift: 6});
                    }
                });

            }
        };
        configData.bootstrapValidator(validatorParam.param).on('success.form.bv', function (e) {
            e.preventDefault();

            console.log("验证通过了......");
            validatorParam.doSubmit();
        });
    },
    initFileUploadPlugin(){

        let mainPicUploader = $("#mainPicFile");

        mainPicUploader.fileinput(mmPicVideoOpt.param);


        mainPicUploader.on('fileuploaded', function(event, data,) {

            let mainPic = $("input[name='fileUrl']");

            let result = data.response;

            if (result.code === 0)
            {
                let urlResult = result.data;
                if (urlResult !== undefined && urlResult !== null && urlResult.url !== undefined)
                {
                    mainPic.val(urlResult.url);

                    mmPicVideoOpt.refreshFileInput(mainPicUploader,urlResult.url);

                    $("input[name='fileType']").val(urlResult.fileType);

                    $("#config-Data").bootstrapValidator('updateStatus','fileUrl','NOT_VALIDATED').bootstrapValidator('validateField', 'fileUrl');
                }
            }
            else
            {
                layer.msg('上传失败', {icon: 2,shift:6});//弹框提示
            }
        });

        mainPicUploader.on('filedeleted', function() {
            $("input[name='fileUrl']").val('');
        });


    },
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
        allowedFileExtensions: ["jpg", "png", "gif","mp4","flv","3gp","mpeg","wmv"]
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

        let preview = mmPicVideoOpt.getInitialPreviewAndConfig(url);


        let reInitialParam = mmPicVideoOpt.param;

        reInitialParam['layoutTemplates'] = {};

        reInitialParam['initialPreview'] = preview.initialPreview;
        reInitialParam['initialPreviewConfig'] = preview.initialPreviewConfig;

        selector.fileinput('destroy').fileinput(reInitialParam).fileinput('enable');
        $('.btn-file').attr('disabled',false);
    }


};