let mmWorkOpt = {

    init(mmWorkList){

        mmWorkOpt.initWeekTitle();
        mmWorkOpt.bindAlwaysRest();
        mmWorkOpt.bindAlwaysWork();
        mmWorkOpt.bindTodayRest();
        for (let i = 0; i < mmWorkList.length; i++)
        {
            let work = mmWorkList[i];
            mmWorkOpt.bindWorkEditEvent(work);
            if (work.workStartTime !== work.workEndTime)
            {
                $('#w' + work.dayOfWeek).html(mmWorkOpt.getFormatTime(work));
            }
        }
        mmWorkOpt.bindWorkCreateEvent();

    },
    initWeekTitle(){

        let week = ["星期一","星期二","星期三","星期四","星期五","星期六","星期天"];
        $('.week-title').each(function (i) {
            let date = mmWorkOpt.getWeek(i);
            let day = mmWorkOpt.getDay(date);
            $(this).attr('data-id',day);
            $(this).html('('+ moment(date).format('YYYY-MM-DD') +')</br>' + week[day - 1]);
        });

        $('.week-content').each(function (i) {
            let day = mmWorkOpt.getDay(mmWorkOpt.getWeek(i));
            $(this).attr('id','w' + day);
        })

    },
    bindAlwaysWork(){

        $("#alwaysWork").on('click',function (e) {

            let param = Plain.getParam('.work-control');
            $.ajax({
                url:'/mm/alwaysWork',
                method:'post',
                dataType:'json',
                data:param,
                success:function (data) {

                    if (data.code === 0)
                    {
                        for (let i = 1; i <= 7; i++)
                        {
                            $('#w' + i).html(mmWorkOpt.getFormatTime(data.data[i - 1]));
                            mmWorkOpt.bindWorkEditEvent(data.data[i - 1]);
                        }
                    }
                    else
                    {
                        layer.msg(data.message,{icon:2,shift:6});
                    }
                },
                error:function (data) {

                }
            });
        });

    },
    bindTodayRest(){

        $("#todayRest").on('click',function () {

            let currDate = new Date();
            let day = mmWorkOpt.getDay(currDate);
            let param = Plain.getParam('.work-control');
            param['workDate'] = moment(currDate).format('YYYY-MM-DD');
            param['dayOfWeek'] = day;
            $.ajax({
                url:'/mm/todayRest',
                method:'post',
                dataType:'json',
                data:param,
                success:function (data) {

                    if (data.code === 0)
                    {
                        $('#w' + day).html('休息');
                        $(".btn-week[data-id=\'"+ day + "\']").removeClass('btn-primary').addClass('btn-default');
                    }
                    else
                    {
                        layer.msg(data.message,{icon:2,shift:6});
                    }
                },
                error:function (data) {

                }
            });
        });
    },
    bindAlwaysRest(){

        $("#alwaysRest").on('click',function (e) {


            let param = Plain.getParam('.work-control');

            $.ajax({
                url:'/mm/alwaysRest',
                method:'post',
                dataType:'json',
                data:param,
                success:function (data) {

                    if (data.code === 0)
                    {
                        for (let i = 1; i <= 7; i++)
                        {
                            $('#w' + i).html('休息');
                        }
                        $('.btn-primary.btn-week').removeClass('btn-primary').addClass('btn-default');

                    }
                    else
                    {
                        layer.msg(data.message,{icon:2,shift:6});
                    }
                },
                error:function (data) {

                }
            });
        });
    },
    bindWorkEditEvent(work){

        let $target = $(".btn-week[data-id=\'"+ work.dayOfWeek + "\']");
        $target.unbind();
        $target.removeClass('btn-create').addClass('btn-edit');
        if (work.workStartTime !== work.workEndTime)
        {
            $target.removeClass('btn-default').addClass('btn-primary');
        }
        $target.on('click',{'work':work},function (e) {

                let url = '';
                let type;
                let workData = e.data.work;
                let $target = $(e.target);
                let id = $target.attr('data-id');

                let param = Plain.getParam('.work-control');
                param['id'] = workData.id;
                param['dayOfWeek'] = id;
                param['workDate'] = moment(mmWorkOpt.getWeek(Number(id) - 1)).format('YYYY-MM-DD');

                if ($target.hasClass('btn-primary'))
                {
                    type = 0;
                    url = '/mm/setRest';
                    delete param['workStartTime'];
                    delete param['workEndTime'];

                }
                else
                {
                    type = 1;
                    url = '/mm/setWork';

                }
                $.ajax({
                    url:url,
                    method:'post',
                    dataType:'json',
                    data:param,
                    success:function (data) {
                        if (data.code === 0)
                        {
                            let result = data.data;
                            e.data.work = result;
                            if (type === 0)
                            {
                                $('#w'+ id).html('休息');
                                $target.removeClass('btn-primary').addClass('btn-default');
                            }
                            else
                            {
                                $('#w'+ id).html(mmWorkOpt.getFormatTime(result));
                                $target.removeClass('btn-default').addClass('btn-primary');
                            }

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
    },
    bindWorkCreateEvent(){

        $('.btn-create').on('click',function () {

            let $1 = $(this);
            let id = $1.attr('data-id');
            let param = Plain.getParam('.work-control');
            param['dayOfWeek'] = id;
            param['workDate'] = moment(mmWorkOpt.getWeek(Number(id) - 1)).format('YYYY-MM-DD');
            $.ajax({
                url:'/mm/saveWork',
                method:'post',
                dataType:'json',
                data:param,
                success:function (data) {
                    if (data.code === 0)
                    {
                        let result = data.data;
                        mmWorkOpt.bindWorkEditEvent(result);
                        $('#w'+id).html(mmWorkOpt.getFormatTime(result));
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
    },
    getFormatTime(work){
        return  moment(work.workStartTime).format('HH:mm:ss')
            + "~" + moment(work.workEndTime).format('HH:mm:ss');
    },
    getWeek(n) {

        let oneDayLong = 24*60*60*1000 ;
        let now = new Date();
        let mondayTime = now.getTime() + n * oneDayLong;
        return  new Date(mondayTime);
    },
    getDay(now){

        let nowDay = now.getDay();
        if (nowDay === 0)
        {
            nowDay = 7;
        }
        return nowDay;
    }


};