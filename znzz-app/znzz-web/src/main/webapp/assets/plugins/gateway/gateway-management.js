$(function () {
    $('.select2').select2({
//            allowClear: true
    });
//        查看详情
    $(".search-detail").on('click', function () {
        $("#detailModal").modal('show');
    });
//        (function clearTime() {
//            $("#timeRange").on('cancel.daterangepicker', function (ev, picker) {
//                $("#timeRange").val('')
//            })
//        })()
    var locale = {
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
    $('#timeRange').daterangepicker({
//            language: 'zh-CN',
        "autoUpdateInput": true,
        "locale": locale,
        "ranges": {
            // '今日': [moment(), moment()],
            // '昨日': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            // '最近7日': [moment().subtract(6, 'days'), moment()],
            // '最近30日': [moment().subtract(29, 'days'), moment()],
            '本月': [moment().startOf('month'), moment().endOf('month')],
            '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        "opens": "left",
        "timePicker":true,
        "timePicker24Hour": true
//            "timePicker":true,
//            "timePickerIncrement" : 60,
//            "showDropdowns": true,
//            "linkedCalendars": true

    });
    $("#timeRange").on('cancel.daterangepicker', function (ev, picker) {
        $("#timeRange").val('')
    })
    function showSuccess() {
        $("#success-alert").removeClass('dis-none')
        $("#success-alert").addClass('dis-block')
        setTimeout(function () {
            $("#success-alert").removeClass('dis-block')
            $("#success-alert").addClass('dis-none')
        },2000);
    }
    function showDel() {
        $("#del-alert").removeClass('dis-none')
        $("#del-alert").addClass('dis-block')
        setTimeout(function () {
            $("#del-alert").removeClass('dis-block')
            $("#del-alert").addClass('dis-none')
        },2000);
    }
    $("#btn-success-close").on('click', function () {
        $("#success-alert").removeClass('dis-block');
        $("#success-alert").addClass('dis-none');
    });
    $("#btn-del-close").on('click', function () {
        $("#del-alert").removeClass('dis-block');
        $("#del-alert").addClass('dis-none');
    });
//        编辑
    $(".btn-edit").unbind('click');
    $(".btn-edit").on('click', function () {
        $("#editModal").modal('show');
    });
    $("#confirmEdit").on('click', function () {
        $("#editModal").modal('hide');
        showSuccess();
    });
//        新增框
    $("#addManagement").on('click', function () {
        $("#addModal")[0].reset();
        $("#addModal").modal('show');
    });
//        点击确认后判断是否成功出现弹框
    $("#confirmAdd").on('click', function () {
        $("#addModal").modal('hide');
        showSuccess();
    });
    
//        删除框
    $("#delManagement").on('click', function () {
    	alert(123);
       $("#delModal").modal('show');
    });
    // $(".btn-del").unbind('click');
    // $(".btn-del").on('click', function () {
    //     $("#delModal").modal('show');
    // });
    $("#confirmDel").on('click', function () {
        $("#delModal").modal('hide');
        showDel();
    });
//        $("#success-block").on('click', function () {
//            $("#tipModal").modal('show')
//        });
    //iCheck for checkbox and radio inputs
    $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    //Red color scheme for iCheck
    $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
        checkboxClass: 'icheckbox_minimal-red',
        radioClass: 'iradio_minimal-red'
    })
    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    })
    $("#addTimeRange").datepicker({
        language: "zh-CN",
        todayHighlight: true,
        weekStart: 1,
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
//            todayBtn: true,//今日按钮
        format: "yyyy/mm/dd"//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
    });
});