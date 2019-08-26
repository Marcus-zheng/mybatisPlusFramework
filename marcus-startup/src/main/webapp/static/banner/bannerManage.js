
let URL = '/adBanner/list';
let $table = $("#banner-list");

layer.config({
    extend: 'moon/style.css', //加载新皮肤
    skin: 'layer-ext-moon' //一旦设定，所有弹层风格都采用此主题。
});
$(function () {
    sessionStorage.removeItem('banner');
});

$("#search-btn").on('click',function () {
    $table.bootstrapTable('refresh', {'url': URL});
});

$("#reset-btn").on('click',function () {
    $(".query-cond").each(function () {
        $(this).val("");
    });
});

$('#createBanner').on('click',function () {
    window.location.href ='/adBanner/edit';
});

$('#deleteBanner').on('click',function () {
    $.ajax({
        url:'/adBanner/delete',
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
            title: '广告名称',
            align: 'center',
            field: 'name',
        },
        {
            title: '广告位',
            align: 'center',
            field: 'adPlace',
            formatter:function (cellValue) {
                var adPlace = "";
                switch (cellValue) {
                    case "up" : adPlace = "上"; break
                    case "down" : adPlace = "下"; break;
                    case "left" : adPlace = "左"; break;
                    case "right" : adPlace = "右"; break;
                }
                return adPlace;
            }
        },
        {
            title: '展示开始时间',
            align: 'center',
            field: 'startTime',
            formatter:function (cellValue) {

                return moment(cellValue).format('YYYY-MM-DD HH:mm');
            }
        },
        {
            title: '展示结束时间',
            align: 'center',
            field: 'endTime',
            formatter:function (cellValue) {

                return moment(cellValue).format('YYYY-MM-DD HH:mm');
            }
        },
        {
            title: '排序值',
            align: 'center',
            field: 'sort'
        },
        {
            title: '备注',
            align: 'center',
            field: 'remark'
        },
        {
            title: '操作人',
            align: 'center',
            field: 'operatorName'
        },
        {
            title: '添加时间',
            align: 'center',
            field: 'createTime',
            formatter:function (cellValue) {

                return moment(cellValue).format('YYYY-MM-DD HH:mm:ss');
            }
        }
        ,{
            title: '操作',
            align: 'center',
            formatter:function () {
                let btn = [];
                let edit = "<button class=\'banner-edit btn btn-primary btn-sm\'>编辑</button>";
                let deleteBtn= "<button class=\'banner-delete btn btn-danger btn-sm\'>删除</button>";

                btn.push(edit);
                btn.push(deleteBtn);
                return btn.join(" ");
            },
            events: {
                'click .banner-edit ': edit ,
                'click .banner-delete': deleteBanner
            }
        }],
    onLoadSuccess: function (data) {
    }
});

function edit(e, val, row) {

    let s = JSON.stringify(row);

    sessionStorage.setItem('banner',s);

    window.location.href="/adBanner/edit";
}


function deleteBanner(e, val, row) {

    $('#deleteId').val(row.id);
    $('#deleteModel').modal('show');

}


