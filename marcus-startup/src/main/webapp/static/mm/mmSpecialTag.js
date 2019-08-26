let mmSpecialTagOpt = {


    initTable(mmSpecialTagList) {


        $("#createNewSpecialTag").on('click',function () {
            $('.tag-create').each(function () {
                if ($(this).attr('name') !== 'mmId')
                {
                    if ($(this).attr('name') === 'currency'){
                        $(this).val('PHP');
                    }
                    else {
                        $(this).val('');
                    }
                }
            });
            mmSpecialTagOpt.bindCreateEvent();
            $("#tag-create-dialog").modal('show');
        });



        let $table = $('#mmSpecialTag-list');

        $table.bootstrapTable({
            data: mmSpecialTagList,
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
                    title: '标签',
                    align: 'center',
                    field: 'tagName',
                },
                {
                    title: '货币单位',
                    align: 'center',
                    field: 'currency',
                },
                {
                    title: '排序',
                    align: 'center',
                    field: 'sort'
                },
                {
                    title: '是否显示',
                    align: 'center',
                    field: 'visible',
                    formatter:function (cellValue) {

                        if (cellValue === 0)
                        {
                            return "显示";
                        }
                        return "不显示";
                    }
                },
                {
                    title: '操作',
                    align: 'center',
                    formatter: function () {

                        let btn = [];
                        let edit = "<button class=\'mm-specialTag-edit btn btn-primary btn-sm\'>编辑</button>";
                        let deleteBtn = "<button class=\'mm-specialTag-delete btn btn-danger btn-sm\'>删除</button>";

                        btn.push(edit);
                        btn.push(deleteBtn);
                        return btn.join(" ");
                    },
                    events: {
                        'click .mm-specialTag-edit ': edit,
                        'click .mm-specialTag-delete': deleteTag
                    }
                }],
            onLoadSuccess: function (data) {
            }
        });

        function edit(e, val, row, index) {

            mmSpecialTagOpt.bindEditEvent(row, index);
            $(".tag-edit").each(function ()
            {
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

            $("#tag-dialog").modal('show');
        }

        function deleteTag(e, val, row) {


            layer.confirm('确认删除吗?',function (index) {
                layer.close(index);

                let load = layer.load(2);
                $.ajax({
                    url:'/mm/deleteSpecialTag',
                    dataType:'json',
                    method:'post',
                    data:{'id':row.id},
                    success:function (data) {

                        layer.close(load);
                        if (data.code === 0)
                        {
                            $('#mmSpecialTag-list').bootstrapTable('remove',{field:'id',values:[row.id]});
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

    bindEditEvent(row, index) {

        let save = $("#editTag");
        save.unbind();
        save.on('click', {'row': row, 'index': index}, function (e) {
            let load = layer.load(2);
            $.ajax({
                url: '/mm/editSpecialTag',
                dataType: 'json',
                method: 'post',
                data: Plain.getParam('.tag-edit'),
                success: function (data) {
                    layer.close(load);
                    if (data.code === 0)
                    {
                        $("#tag-dialog").modal('hide');
                        $('#mmSpecialTag-list').bootstrapTable('updateRow', {index: e.data.index, row: data.data});
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
    },
    bindCreateEvent(){

        let $saveTag = $("#saveTag");

        $saveTag.unbind();

        $saveTag.on('click',function () {

            let load = layer.load(2);
            $.ajax({
                url: '/mm/batchCreateSpecialTag',
                dataType: 'json',
                method: 'post',
                data: Plain.getParam('.tag-create'),
                success: function (data) {
                    layer.close(load);
                    if (data.code === 0)
                    {
                        $("#tag-create-dialog").modal('hide');
                        $('#mmSpecialTag-list').bootstrapTable('append',  data.data);
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

    }
};