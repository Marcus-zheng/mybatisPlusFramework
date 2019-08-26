let mmSelfOpt = {


    initDesc(selfDesc){

        if (selfDesc !== null && selfDesc !== undefined
            && selfDesc.selfDesc !== null && selfDesc.selfDesc !== undefined)
        {
            $("#selfDesc").text(selfDesc.selfDesc);
            $("#createSelfDesc").hide();
            mmSelfOpt.bindEditEvent();
        }
        else
        {
            $("#editSelfDesc").hide();
            mmSelfOpt.bindEditEvent();
            mmSelfOpt.bindNewEvent();
        }


    }
    ,getSelfDesc(){

        let result = sessionStorage.getItem('mmDetail');

        let parse = JSON.parse(result);
        return parse.mmDesc;
    },
    refresh(data){
        let result = sessionStorage.getItem('mmDetail');
        let parse = JSON.parse(result);
        parse['mmDesc'] = data;
        sessionStorage.setItem('mmDetail',JSON.stringify(parse));
    },
    bindNewEvent(){

        let $saveSelfDesc = $('#saveSelfDesc');

        $("#createSelfDesc").on('click',function () {

            $saveSelfDesc.unbind();
            $("#selfTextDesc").val('');
            $saveSelfDesc.on('click',function () {

                let load = layer.load(2);
                $.ajax({
                    url: '/mm/saveSelfDesc',
                    dataType: 'json',
                    method: 'post',
                    data: Plain.getParam('.self-create'),
                    success: function (data) {
                        layer.close(load);
                        if (data.code === 0)
                        {
                            $("#editSelfDesc").show();
                            $("#createSelfDesc").hide();
                            mmSelfOpt.refresh(data.data);
                            $("#selfDesc").text(data.data.selfDesc);
                            $("#selfDescModal").modal('hide');
                        }
                        else
                        {
                            layer.msg(data.message, {icon: 2, shift: 6});
                        }
                    },
                    error: function (data) {
                        console.log(data);
                        layer.close(load);
                        layer.msg('系统异常', {icon: 2, shift: 6});
                    }
                });
            });


            $("#selfDescModal").modal('show');
        });
    },

    bindEditEvent() {

        let $saveSelfDesc = $('#saveSelfDesc');
        $("#editSelfDesc").on('click',function () {

            $saveSelfDesc.unbind();
            $("#selfTextDesc").val($('#selfDesc').text());
            let desc = mmSelfOpt.getSelfDesc();
            if (desc === undefined || desc === null)
            {
                layer.msg("不存在可编辑数据，请新增后再操作",{icon:2,shift:6});
                return false;
            }

            $saveSelfDesc.on('click',{'id':desc.id},function (e) {

                let param = Plain.getParam('.self-create');
                param['id'] = e.data.id;
                let load = layer.load(2);
                $.ajax({
                    url: '/mm/editSelfDesc',
                    dataType: 'json',
                    method: 'post',
                    data: param,
                    success: function (data) {
                        layer.close(load);
                        if (data.code === 0)
                        {
                            $("#selfDesc").text(data.data.selfDesc);
                            $("#selfDescModal").modal('hide');
                        }
                        else
                        {
                            layer.msg(data.message, {icon: 2, shift: 6});
                        }
                    },
                    error: function (data) {
                        console.log(data);
                        layer.close(load);
                        layer.msg('系统异常', {icon: 2, shift: 6});
                    }
                });

            });
            $("#selfDescModal").modal('show');
        });
    }
};