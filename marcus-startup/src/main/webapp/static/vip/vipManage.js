
let URL = '/vipUser/list';
let $table = $("#vipUser-list");

layer.config({
    extend: 'moon/style.css', //加载新皮肤
    skin: 'layer-ext-moon' //一旦设定，所有弹层风格都采用此主题。
});
$(function () {
    sessionStorage.removeItem('vipUser');
});

$("#search-btn").on('click',function () {
    $table.bootstrapTable('refresh', {'url': URL});
});

$("#reset-btn").on('click',function () {

    $(".query-cond").each(function () {
        $(this).val("");
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
            title: '用户名',
            align: 'center',
            field: 'userName',
        },
        {
            title: '注册邮箱',
            align: 'center',
            field: 'mail'
        },
        {
            title: '推荐人',
            align: 'center',
            field: 'inviterName',
        },
        {
            title: '最后登录时间',
            align: 'center',
            field: 'loginTime',
            formatter:function (cellValue) {
                return moment(cellValue).format('YYYY-MM-DD HH:mm:ss');
            }
        }
        ,{
            title: '操作',
            align: 'center',
            formatter:function () {

                let btn = [];
                let view = "<button class=\'vipUser-view btn btn-primary btn-sm\'>详情</button>";

                btn.push(view);
                return btn.join(" ");
            },
            events: {
                'click .vipUser-view ': view
            }
        }],
    onLoadSuccess: function (data) {
    }
});

function view(e, val, row) {

    let s = JSON.stringify(row);

    sessionStorage.setItem('vipUser',s);

    window.location.href="/vipUser/view";
}