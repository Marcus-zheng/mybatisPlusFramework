let vipUser = JSON.parse(sessionStorage.getItem('vipUser'));
let $table = $("#vipView-list");

layer.config({
    extend: 'moon/style.css', //加载新皮肤
    skin: 'layer-ext-moon' //一旦设定，所有弹层风格都采用此主题。
});

$(function () {
    if (vipUser.userPhoto != null && vipUser.userPhoto != undefined && vipUser.userPhoto != undefined) {
        $("#userPhoto").attr("src", vipUser.userPhoto);
    }
    $("#userName").text(vipUser.userName);
    $("#userNickname").text(vipUser.userNickname);
    getVipInfo();
});

function getVipInfo(){
    var phone = vipUser.phone;
    if (phone == null || phone == undefined){
        phone = "";
    }
    var wechat = vipUser.wechat;
    if (wechat == null || wechat == undefined){
        wechat = "";
    }
    var telegram = vipUser.telegram;
    if (telegram == null || telegram == undefined){
        telegram = "";
    }
    var birthday = vipUser.birthday;
    if (birthday == null || birthday == undefined){
        birthday = "";
    }
    var height = vipUser.height;
    if (height == null || height == undefined){
        height = "";
    }
    var weight = vipUser.weight;
    if (weight == null || weight == undefined){
        weight = "";
    }
    var income = vipUser.income;
    if (income == null || income == undefined){
        income = "";
    }
    $table.bootstrapTable('destroy');
    var infoTable = '<tr><td style="text-align: center; width: 150px">手机号码</td>' +
        '<td class="edit-cond" style="text-align: left;">'+phone+'</td></tr>' +
        '<tr><td style="text-align: center; width: 150px">微信号</td>' +
        '<td class="edit-cond" style="text-align: left;">'+wechat+'</td></tr>' +
        '<tr><td style="text-align: center; width: 150px">电报</td>' +
        '<td class="edit-cond" style="text-align: left;">'+telegram+'</td></tr>' +
        '<tr><td style="text-align: center; width: 150px">出生日期</td>' +
        '<td class="edit-cond" style="text-align: left;">'+birthday+'</td></tr>' +
        '<tr><td style="text-align: center; width: 150px">身高（cm）</td>' +
        '<td class="edit-cond" style="text-align: left; ">'+height+'</td></tr>' +
        '<tr><td style="text-align: center; width: 150px">体重（kg）</td>' +
        '<td class="edit-cond" style="text-align: left;">'+weight+'</td></tr>' +
        '<tr><td style="text-align: center; width: 150px">年收入</td>' +
        '<td class="edit-cond" style="text-align: left;">'+income+'</td></tr>';
    $table.html(infoTable);
}

function getFriends(){

    $table.bootstrapTable('destroy').bootstrapTable({
        url: "/vipUser/getVipFriends",
        dataType: "json",
        toolbar: "#toolbar",
        striped: true,
        pageSize: 20,
        pageList: [10, 20, 50, 100],
        pagination: true,
        sidePagination: "server",
        queryParams: function () {
            var params = {};
            params.vipUserId = vipUser.id;
            params.inviterId = vipUser.inviterId;
            return params;
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
                title: '邮箱',
                align: 'center',
                field: 'mail'
            }],
        onLoadSuccess: function (data) {
        }
    });
}

$("#back").on('click',function () {

    sessionStorage.removeItem('vipUser');

    window.location.href='/vipUser/index';
});