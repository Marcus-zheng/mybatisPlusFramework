let mmSimpleInfoOp ={


    renderTable(simpleInfo){

        if (simpleInfo !== undefined && simpleInfo !== null)
        {
            $(".edit-cond").each(function () {
                let column = $(this);
                let id = column.attr('id');
                if (id !== 'selfShotPhoto')
                {
                    column.html(simpleInfo[id]);
                    let button = column.next();
                    button.html(mmSimpleInfoOp.createEditButton(id));
                }
                else
                {
                    column.attr('src',simpleInfo[id]);
                    mmSimpleInfoOp.modifyMainPic(column);
                    mmSimpleInfoOp.previewMainPic();
                }
                mmSimpleInfoOp.bindEditEvents();
            });
        }
    },
    createEditButton(id){

        return '<button class="btn btn-sm btn-primary edit-button" data-id=\"' + id + '\">编辑</button>';
    },

    createDeleteButton(id){

        return '<button class="btn btn-sm btn-danger delete-button" style="margin-left:5px;" data-id=\"' + id + '\">删除</button>';
    },
    bindEditEvents(){

        $(".edit-button").on('click',function () {

            let $this = $(this);
            let info = mmSimpleInfoOp.getSimpleInfo();
            let id = $this.attr('data-id');
            let first = $this.parent().siblings('td:eq(0)');
            let columnName = first.text();
            let second = first.next();
            let value = info[id];
            let simpleInfoId = info['id'];
            let innerHtml = '<div class="form-group" style="margin-left:10px;margin-top:5px;">'
                + '<input name=\"' + id + '\" type="text" value=\"' + value + '\" class="input-sm col-xs-10 text-left" />'
                + '</div>';

            layer.open({
                id: 1,
                type: 1,
                title: columnName + "编辑",
                area: ['300px','auto'],
                content: innerHtml,
                btn:['确认','取消'],
                yes:function (index) {

                    layer.close(index);

                    let value = $('input[name=\"' + id +'\"]').val();

                    if (value === null || value === '')
                    {
                        layer.msg('修改' + columnName + '不能为空');
                        return;
                    }
                    let param = {};
                    param['id'] = simpleInfoId;
                    param[id] = value;
                    let load = layer.load(2);
                    $.ajax({
                        url:'/mm/updateSimpleInfo',
                        dataType:'json',
                        method:'post',
                        data:param,
                        success:function (data) {
                            layer.close(load);
                            if (data.code === 0)
                            {
                                second.text(value);
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

                }
            });
        });
    },
    bindDeleteEvents(){

        $(".delete-button").on('click',function () {

            let $this = $(this);
            let info = mmSimpleInfoOp.getSimpleInfo();
            let first = $this.parent().siblings('td:eq(0)');
            let second = first.next();
            let deleteId = info['id'];

            layer.confirm('确认删除？', function(index){

                layer.close(index);
                let load = layer.load(2);
                $.ajax({
                    url:'/mm/deleteProject',
                    dataType:'json',
                    method:'post',
                    data:{'id':deleteId},
                    success:function (data) {
                        layer.close(load);
                        if (data.code === 0)
                        {
                            second.text('');
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
        });
    },
    modifyMainPic(){

        $("#modifyPhoto").on('click',function (e) {
            let sWidth = $(window).width(), sHeight = $(window).height();
            layer.open({
                id: 1,
                type: 2,
                title: "自拍照编辑",
                area: [sWidth*0.85 + 'px',sHeight*0.85 + 'px'],
                content: '/mm/mmPhoto',
                yes:function (index) {
                    layer.close(index);
                }
            });
        });
    },
    refreshImg(url){
        $('#selfShotPhoto').attr('src',url);
    },
    previewMainPic(){

        $("#previewPhoto").on('click',function () {
            let sWidth = $(window).width(), sHeight = $(window).height();
            let url = $('#selfShotPhoto').attr('src');
            layer.open({
                id: 2,
                type: 1,
                title: "",
                area: [sWidth*0.85 + 'px',sHeight*0.85 + 'px'],
                content: '<div class="text-center" style="margin-top: 10px;height:'+ sHeight*0.8 +'px"><img src=\"' + url + '\" style="width:auto;height:auto;max-width:100%;max-height:100%;" alt="暂缺"></div>',
            });
        });
    },
    getSimpleInfo(){

        let result = sessionStorage.getItem('mmDetail');

        let parse = JSON.parse(result);
        return parse.mmSimpleInfo;
    }


};