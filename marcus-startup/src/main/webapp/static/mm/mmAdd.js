let configData = $("#configData");
let validatorParm = {
    param: {
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            nickName: {
                message: '昵称不能为空',
                validators: {
                    notEmpty: {
                        message: '昵称不能为空'
                    }
                }
            },
            realName: {
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    }
                }
            },
            age: {
                validators: {
                    notEmpty: {
                        message: '年龄不能为空'
                    },
                    digits: {
                        message: '年龄必须是正整数'
                    }
                }
            },
            bodyInfo: {
                validators: {
                    notEmpty: {
                        message: '身高·三围不能为空'
                    }
                }
            },
            constellation: {
                validators: {
                    notEmpty: {
                        message: '星座不能为空'
                    }
                }
            },
            country: {
                validators: {
                    notEmpty: {
                        message: '国籍不能为空'
                    }
                }
            },
            workCity: {
                validators: {
                    notEmpty: {
                        message: '工作城市不能为空'
                    }
                }
            },
            selfShotPhoto: {
                validators: {
                    notEmpty: {
                        message: '自拍照未上传'
                    }
                }
            }

        }
    },
    doSubmit() {

        let url = '/mm/save';

        let saveParam = Plain.getParam('.query-cond');

        $.ajax({
            url: url,
            data: saveParam,
            method: 'post',
            dataType: 'json',
            success: function (data) {

                if (data.code === 0) {
                    window.location.href = '/mm/index';
                } else {
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

$('#startTime').datetimepicker({
    locale:'zh-cn',
    format: 'LT',
    defaultDate: new Date().setHours(12,0,0)

});

$('#endTime').datetimepicker({
    locale:'zh-cn',
    format: 'LT',
    defaultDate:new Date().setHours(23,0,0)
});

configData.bootstrapValidator(validatorParm.param).on('success.form.bv', function (e) {
    e.preventDefault();

    console.log("验证通过了......");
    validatorParm.doSubmit();
});

$("#back").on('click',function () {

    sessionStorage.removeItem('user');

    window.location.href='/mm/index';
});