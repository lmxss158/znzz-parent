/**
 * 日期控件部分公共方法
 */

// 设置背景颜色
function setColor() {
    $('ul li').each(function (index) {
        var xx = $(this).attr("data-date");
        var myArrs = myApps.split(",");
        var otherArrs = otherApps.split(",");
        var forbidArrs = forbidApps.split(",");
        var delayArrs = delayApps.split(",");
        var delayMax = null;
        for (var index in myArrs) {
            // 我的申请
            if (xx == myArrs[index]) {
                $(this).addClass("my_date");
            }
        }
        for (var index in otherArrs) {
            // 其他人的申请
            if (xx == otherArrs[index]) {
                $(this).addClass("other_date");
            }
        }
        // 逾期未还
        for (var index in delayArrs) {
            if (xx == delayArrs[index]) {
                $(this).addClass("over_date")
            }
            if ((delayArrs[index] != null) && (delayArrs[index] != "")) {
                delayMax = delayArrs[index];//取得最大的逾期未还日期
            }
        }
        // 设置禁用显示
        for (var i = 0; i < forbidArrs.length; i++) {
            if ((xx == forbidArrs[i]) && ((delayMax == null) || (delayMax == 0)|| (new Date(forbidArrs[i]) > new Date(delayMax)))) {
                $(this).addClass("stop_date");
            }
            if (forbidArrs[0] != "0") {
                forbidStart = forbidArrs[0];
            }
        }

        $(this).css("cursor", 'pointer');
    });
}

/*悬浮提示*/
function toolTips() {
    var tip;
    //Tooltips
    $(".tip_trigger").mouseover(function () {
        tip = $(this).parent().find('.tip_show');
        tip.show();
    }).mouseleave(function () {
        tip = $(this).parent().find('.tip_show');
        tip.hide();
    }).mousemove(function (e) {
        tip = $(this).parent().find('.tip_show');
        //计算tooltip显示div的left值
        var mousex = e.pageX + 10;

        //计算tooltip显示div的top值
        var mousey = e.pageY + 10;

        //获取tooltip显示div的宽度
        var tipWidth = tip.width();

        //获取tooltip显示div的高度
        var tipHeight = tip.height();

        //计算鼠标左位置加上tooltip弹出div宽度后当前窗口是否够显示
        var tipVisX = $(window).width() - mousex - tipWidth;

        //计算鼠标上(top)位置加上tooltip弹出div高度后当前窗口是否够显示
        var tipVisY = $(window).height() - mousey - tipHeight;

        //放到鼠标右边水平不够显示的，直接放到鼠标左边显示
        if (tipVisX < 10) {
            mousex = e.pageX - tipWidth - 10;
        }

        //放到鼠标下方垂直不够显示的，直接放到鼠标上方显示
        if (tipVisY < 10) {
            mousey = e.pageY - tipHeight - 10;
        }

        //将计算好的水平(left)和垂直位置(top)的参数值写入css修饰
        tip.css({top: mousey, left: mousex});
    });
}

// 悬浮提示信息
function setValue() {
    $('ul li').each(function (index) {
        $(this).on('mouseover', function () {
            // 获取当前的申请人信息
            var current = $(this).attr("data-date");
            var code = $("#assetCode").val();
            var tip = $('.tip_show');
            $.ajax({
                type: "post",
                url: infoUrl,
                dataType: "json",
                data: {
                    "date": current,
                    "assetCode": code
                },
                async: true,
                success: function (data) {
                    if (data.code == 0) {
                        $("#person").append(data.data.proposer);
                        $("#num").append(data.data.applyId);
                        $("#phone").append(data.data.number);
                        $("#to").append(data.data.lendDate.split(" ")[0]);
                        $("#return").append(data.data.returnDate.split(" ")[0]);
                    }
                    else {
                        //tip.hide();
                        $("#person").text("--");
                        $("#num").text("");
                        $("#phone").text("");
                        $("#to").text("");
                        $("#return").text("");
                    }
                },
                error: function (data) {
                    tip.hide();
                }
            });
        });
        $(this).on('mouseout', function () {
            $("#person").text("申请人:");
            $("#num").text("申请编号：");
            $("#phone").text("联系电话：");
            $("#to").text("借出日期：");
            $("#return").text("归还日期：");
        });
    });
}