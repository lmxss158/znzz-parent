//去除首尾空格
var autoDeleteSpace = function (obj) {
    $(document).on('blur', obj, function () {
        $(this).val($(this).val().trim());
    });
};

//改变disabled状态
var checkDisable = function () {
    $("select[disabled=disabled]").each(function () {
        if (parseInt($(this).val()) != -1) {
            $(this).attr("disabled", false);
        }
    });
};