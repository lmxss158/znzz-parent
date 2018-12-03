var setDate = function(){
    for (var key in arguments){
        $("#" + arguments[key]).datepicker({
            language: "zh-CN",
            todayHighlight: true,
            weekStart: 1,
            Integer: 1,
            autoclose: true,//选中之后自动隐藏日期选择框
            clearBtn: true,//清除按钮
            forceParse: true,
            // showMeridian: true,
            todayBtn: "linked",//今日按钮

            format: 'yyyy-mm-dd'//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
        });
    }
}

var setDate2 = function(){
    var d = new Date();
    d.setFullYear(d.getFullYear() + 1);
    d.setMonth(d.getMonth());
    d.setDate(d.getDate());
    for (var key in arguments){
        $("#" + arguments[key]).datepicker({
            language: "zh-CN",
            todayHighlight: true,
            weekStart: 1,
            Integer: 1,
            autoclose: true,//选中之后自动隐藏日期选择框
            clearBtn: true,//清除按钮
            forceParse: true,
            todayBtn: "linked",//今日按钮
            startDate: new Date(),
            endDate: d,
            format: 'yyyy-mm-dd'//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
        });
    }
}

var newLocale = {
    format: 'YYYY/MM/DD HH:mm:ss',
    separator: ' - ',
    applyLabel: '确认',
    cancelLabel: '清除',
    fromLabel: '开始时间',
    toLabel: '结束时间',
    customRangeLabel: '自定义',
    weekLabel: "W",
    daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
    monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
    firstDay: 1
}
$.fn.datepicker.dates['zh-CN'] = {
    days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
    daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
    daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    today: "今天",
    suffix: [],
    meridiem: ["上午", "下午"]
};


var setDateRange = function (id){
    locale = {
        format: 'YYYY/MM/DD',
        separator: ' - ',
        applyLabel: '确认',
        cancelLabel: '清除',
        fromLabel: '开始时间',
        toLabel: '结束时间',
        customRangeLabel: '自定义',
        weekLabel: "W",
        daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        firstDay: 1
    };

    $('#' + id).daterangepicker({
        "autoUpdateInput": false,
        "locale": locale,
        "timePickerSeconds": false,
        "ranges": {
            '本月': [moment().startOf('month'), moment().endOf('month')],
            '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        "opens": "center"
    });
    $('#' + id).on('apply.daterangepicker', function (ev, picker) {
        $('#' + id).val(picker.startDate.format('YYYY/MM/DD') + ' - ' + picker.endDate.format('YYYY/MM/DD'));
    });

    $('#' + id).on('cancel.daterangepicker', function (ev, picker) {
        $('#' + id).val('');
    });

}

