/**
 * Created by Administrator on 2017/7/11.
 */
    function loadHtml(url,a){
        $('.sidebar-menu').find('a').removeClass("sidebar-focus");
        $('#page-wrapper').load(url);
        $(a).addClass("sidebar-focus");
    }
