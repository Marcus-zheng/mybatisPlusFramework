

$(function () {

    let mmId = sessionStorage.getItem('mmId');
    sessionStorage.removeItem('mmDetail');
    if (mmId === undefined || mmId == null)
    {
        layer.msg('未获取到mm信息',{icon:2});
        return;
    }
    $("input[name='mmId']").val(mmId);

    $.ajax({

        url:'/mm/getMmdetail',
        method:'post',
        dataType:'json',
        data:{"id":mmId},
        success:function (data) {

            if (data.code === 0)
            {
                let result = data.data;
                sessionStorage.setItem('mmDetail',JSON.stringify(result));

                mmWorkOpt.init(result.mmWorkList);
                mmSelfOpt.initDesc(result.mmDesc);
                mmSimpleInfoOp.renderTable(result.mmSimpleInfo);
                mmServiceOpt.initTable(result.mmServiceInfoList);
                mmPicVideoOpt.initTable(result.mmVideoPicList);
                mmSpecialTagOpt.initTable(result.mmSpecialTagList);
                mmAcceptTagOpt.initTable(result.mmAcceptRangeTagList)

            }
            else
            {
                layer.msg(data.message, {icon: 2, shift: 6});
            }
        },
        error:function (data) {
            console.log(data);
            layer.msg('系统异常', {icon: 2, shift: 6});
        }
    });

    $("#back").on('click',function () {

        sessionStorage.removeItem('mmId');
        sessionStorage.removeItem('mmDetail');

        window.location.href = '/mm/index';
    });

    $('#workStartTime').datetimepicker({
        locale:'zh-cn',
        format: 'LT',
        defaultDate: new Date().setHours(12,0,0)

    });

    $('#workEndTime').datetimepicker({
        locale:'zh-cn',
        format: 'LT',
        defaultDate:new Date().setHours(23,0,0)
    });


});