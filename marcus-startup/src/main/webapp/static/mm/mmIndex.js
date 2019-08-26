
let URL = '/mm/list';
let $table = $("#mm-list");

layer.config({
    extend: 'moon/style.css', //加载新皮肤
    skin: 'layer-ext-moon' //一旦设定，所有弹层风格都采用此主题。
});
$(function () {
    sessionStorage.removeItem('mmId');
});

$("#search-btn").on('click',function () {
    $table.bootstrapTable('refresh', {'url': URL});
});

$("#reset-btn").on('click',function () {

    $(".query-cond").each(function () {
        $(this).val("");
    });
});

$('#createUser').on('click',function () {

    window.location.href ='/mm/add';
});

$('#deleteUser').on('click',function () {

    $.ajax({

        url:'/mm/deleteMM',
        method:'post',
        data:{'id':$('#deleteId').val()},
        dataType:'json',
        success:function (data) {

            if (data.code === 0)
            {
                $table.bootstrapTable('refresh', {'url': URL});
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
});


$table.bootstrapTable({
    url: URL,
    dataType: "json",
    toolbar: "#toolbar",
    striped: true,
    pageSize: 20,
    pageList: [10, 20, 50, 100],
    pagination: true,
    sidePagination: "server",
    queryParams: function (params) {
        return Plain.getParam('.query-cond',params);
    },
    responseHandler: function (res) {
        return res.data;
    },
    columns: [
        {
            title: '序号',
            align: 'center',
            width: '5%',
            formatter: function (cellValue, record,index) {
                let options = $table.bootstrapTable('getOptions');
                let pageSize = options.pageSize;
                let pageNum = options.pageNumber;

                return pageSize * (pageNum - 1) + index + 1;
            }
        },
        {
            title: '昵称',
            align: 'center',
            field: 'nickName',
        },
        {
            title: '姓名',
            align: 'center',
            field: 'realName'
        },
        {
            title: '年龄',
            align: 'center',
            field: 'age',
        },
        {
            title: '身高·三围',
            align: 'center',
            field: 'bodyInfo'
        }
        ,
        {
            title: '星座',
            align: 'center',
            field: 'constellation'
        }
        ,
        {
            title: '自拍照',
            align: 'center',
            field: 'selfShotPhoto',
            formatter:function (cellvalue) {

                return '<img src=\"'+ cellvalue +'\" style="height: 100px;width: 200px;" alt="暂缺">'
            }
        }
        ,
        {
            title: '工作城市',
            align: 'center',
            field: 'workCity'
        }
        ,
        {
            title: '国籍',
            align: 'center',
            field: 'country'
        },
        {
            title: '是否可见',
            align: 'center',
            field: 'visible',
            formatter:function (cellValue) {

                if (cellValue === 1){
                    return '不可见';
                }
                return '可见';
            },
        }
        ,{
            title: '操作',
            align: 'center',
            formatter:function (cellValue,row) {

                let btn = [];
                let edit = "<button class=\'mm-edit btn btn-primary btn-sm\'>编辑</button>";
                let visibleBtn= "<button class=\'mm-visible btn btn-warning btn-sm\'>可见</button>";
                let unVisibleBtn= "<button class=\'mm-unVisible btn btn-warning btn-sm\'>不可见</button>";
                let deleteBtn= "<button class=\'mm-delete btn btn-danger btn-sm\'>删除</button>";

                btn.push(edit);

                if (row.visible === 0){
                    btn.push(unVisibleBtn);
                }else {
                    btn.push(visibleBtn);
                }


                btn.push(deleteBtn);
                return btn.join(" ");
            },
            events: {
                'click .mm-edit ': edit ,
                'click .mm-visible ': visible,
                'click .mm-unVisible ': unVisible,
                'click .mm-delete': deleteUser
            }
        }],
    onLoadSuccess: function (data) {
    }
});

function edit(e, val, row,index) {


    sessionStorage.setItem('mmId',row.id);

    window.location.href="/mm/edit";
}

function visible(e, val, row,index) {

   submit(row,0,index);
}

function unVisible(e, val, row,index) {

    submit(row,1,index);
}

function submit(row,status,index) {

    $.ajax({
        url: '/mm/modifyVisible',
        data: {'id':row.id,'visible':status},
        method: 'post',
        dataType: 'json',
        success: function (data) {

            if (data.code === 0) {
                row.visible = status;
               $table.bootstrapTable('updateRow',{row:row,index:index});
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


function deleteUser(e, val, row) {

    $('#deleteId').val(row.id);
    $('#deleteModel').modal('show');

}


