let mmServiceOpt = {


    initTable(mmServiceList){


        let $table = $('#mmService-list');

        $table.bootstrapTable({
            data:mmServiceList,
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
                    title: '服务',
                    align: 'center',
                    field: 'serviceName',
                },
                {
                    title: '价格',
                    align: 'center',
                    field: 'price'
                },
                {
                    title: '货币单位',
                    align: 'center',
                    field: 'currency',
                },
                {
                    title: '操作',
                    align: 'center',
                    formatter:function () {

                        let btn = [];
                        let edit = "<button class=\'mm-service-edit btn btn-primary btn-sm\'>编辑</button>";
                        let deleteBtn= "<button class=\'mm-service-delete btn btn-danger btn-sm\'>删除</button>";

                        btn.push(edit);
                        btn.push(deleteBtn);
                        return btn.join(" ");
                    },
                    events: {
                        'click .mm-service-edit ': edit ,
                        'click .mm-service-delete': deleteService
                    }
                }],
            onLoadSuccess: function (data) {
            }
        });
        mmServiceOpt.bindSaveEvent();

        function edit(e,val,row,index) {

            mmServiceOpt.bindEditEvent(row,index);
            $(".service-edit").each(function () {
                let $this = $(this);
                let name = $this.attr('name');
                if (row[name] !== undefined && row[name] !== null)
                {
                    $this.val(row[name]);
                }
                else
                {
                    $(this).val('');
                }
            });

            $("#service-dialog").modal('show');
        }

        function deleteService(e,val,row,index) {

            layer.confirm('确认删除吗?',function (index) {
                layer.close(index);

                let load = layer.load(2);
                $.ajax({
                    url:'/mm/deleteService',
                    dataType:'json',
                    method:'post',
                    data:{'id':row.id},
                    success:function (data) {
                        layer.close(load);
                        if (data.code === 0)
                        {
                            $('#mmService-list').bootstrapTable('remove',{field:'id',values:[row.id]});
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
    bindEditEvent(row,index){

        let save = $("#editService");
        save.unbind();
        save.on('click',{'row':row,'index':index},function (e) {
            let load = layer.load(2);
            $.ajax({
                url:'/mm/editService',
                dataType:'json',
                method:'post',
                data:Plain.getParam('.service-edit'),
                success:function (data) {
                    layer.close(load);
                    if (data.code === 0)
                    {
                        $("#service-dialog").modal('hide');
                        $('#mmService-list').bootstrapTable('updateRow',{index:e.data.index,row:data.data});
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
    },
    bindSaveEvent(){

        $('#createNewService').on('click',function () {

            let save = $("#editService");
            save.unbind();
            save.on('click',function () {
                let load = layer.load(2);
                $.ajax({
                    url:'/mm/saveService',
                    dataType:'json',
                    method:'post',
                    data:Plain.getParam('.service-edit'),
                    success:function (data) {
                        layer.close(load);
                        if (data.code === 0)
                        {
                            $("#service-dialog").modal('hide');
                            $('#mmService-list').bootstrapTable('append',data.data);
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
            $("#service-dialog").modal('show');
        });

    }
};