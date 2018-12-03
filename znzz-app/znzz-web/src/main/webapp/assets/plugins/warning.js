/**
 url:页面地址 必填 <br/>
 org:要向后台传递的参数{ } 可选<br/>
 func:加载完毕后执行的函数 可选<br/>
 option: 模态框参数{  backdrop:boolean或 'static' , width : "30%" 百分比     }   可选<br/>
 id:自己指定模态框id   可选<br/>
 调用实例 openModule("/jsp/dome/module.html");
 */
function openModule(url,org,func,option,id){
    var $load;
    if(id){
        $load = $("#"+id).length > 0 ? $("#"+id):$("<div id='" + id + "'></div>");
    }else{
        $load = $("#moudle_one").length > 0 ? $("#moudle_one"):$("<div id='moudle_one'></div>");
    }
    var backdrop = ((typeof option) =='undefined' || (typeof option.backdrop)=='undefined')?'static':option.backdrop;

    $load.appendTo($("body"))
        .load(url,org, function(){
            option && option.width ? $load.find(".modal-dialog").css({width:option.width}):null;
            $load.find("> .modal").modal({
                show:true,
                backdrop:backdrop
            })
                .on("hidden.bs.modal", function() {
                    $(this).removeData("bs.modal");
                    $(this).remove();
                });
            (typeof func)=="function"?func():"";
        });
}
/**
 * 模态框关闭
 * @param id 打开时候用了id 关闭时候传递id
 */
function closeModule(id,func,defaultid){
    if(id){
        $("#"+id).find("> .modal").on("hidden.bs.modal", function() {
            $(this).removeData("bs.modal");
            if(func){
                func();
            }
        });

        $("#"+id).find("> .modal").modal("hide");
    }else{
        $("#"+id).find("> .modal").on("hidden.bs.modal", function() {
            $(this).removeData("bs.modal");
            if(func){
                func();
            }
        });
        $("#moudle_one").find("> .modal").modal("hide");
    }
    if(defaultid){
        $("#"+defaultid).addClass(""+defaultid);
    }
}
//交易状态
function showMsg(obj) {
    var msg = "";
    switch (obj) {
        case '00':
            msg = "操作成功！";
            break;
        case '01':
            msg = "操作失败，请刷新后重试！";
            break;
        case '02':
            msg = "无记录！";
            break;
        case '03':
            msg = "未注册！";
            break;
        case '04':
            msg = "未授权！";
            break;
        case '05':
            msg = "未登录！";
            break;
        case '06':
            msg = "未查询到相关数据！";
            break;
        case '07':
            msg = "无法上传档案附件,因档案状态不是暂存或审核未通过状态！";
            break;
        case '08':
            msg = "无权限删除档案附件！";
            break;
        case '09':
            msg = "无法删除档案附件,因档案状态不是暂存或审核未通过状态！";
            break;
        default:
            msg = obj;
            break;
    }
    if(msg != null && msg != '00' && msg != '' ){
        openAlert(msg,"提示:",3000);
    }
    if(obj == '05'){
        window.location.href="/manager";
    }
}
/**
 * 警告框
 * @param id 打开时候用了id 关闭时候传递id
 */
function openAlert(text,title,delay){
    delay = delay?delay:3000;
    $("#default_meg").html((text?text:''));
    window.setTimeout(function(){
        $("#default_meg").html("");
    }, delay);
}
/**
 * jquery 依赖 bootstrap 插件扩展
 * pagination: page 为一个json数据 func 为回调函数 同时会回传两个参数 curPage,pageCount
 */
$.fn.extend({
    //获取当前页
    getCurPage:function(){
        return this.data("curPage");
    },
    //分页扩展
    pagination:function(page,func){
        var func = func?func:toPage;
        this.empty();
        var pageCount = (page.pageCount > 0)?page.pageCount:20;

        this.data("curPage",page.curPage);

        this.addClass("pagination pagination-sm  navbar-right")

        this.append($("<li><a href='javascript:void(0)' title='首页'><<</a></li>").click(function(){
            func(1,pageCount);
        }));
        this.append($("<li><a href='javascript:void(0)' title='上一页'><</a></li>").click(function(){
            func(page.curPage<=1?1:(page.curPage-1),pageCount);
        }));
        if(page.maxPage <= 6){
            for(var i = 1; i <= page.maxPage ; i++){
                this.append($("<li " + ((i == page.curPage)?"class='active'":'') + " ><a href='javascript:void(0)'>" + i + "</a></li>").click(i,function(e){
                    func(e.data,pageCount);
                }));
            }
        }

        if(page.maxPage > 6){
            for(var i = 1; i <= page.maxPage ; i++){
                if(i <= 2){
                    this.append($("<li " + ((i == page.curPage)?"class='active'":'') + " ><a href='javascript:void(0)'>" + i + "</a></li>").click(i,function(e){
                        func(e.data,pageCount);
                    }));
                }
                if(i > 2 && i <= page.maxPage - 2){
                    this.append($("<li><a href='javascript:void(0)'>...</a></li>"));
                    if(page.curPage > 2 && page.curPage <= page.maxPage - 2){
                        this.append($("<li class='active'><a href='javascript:void(0)'>" + page.curPage + "</a></li>").click(function(){
                            func(page.curPage,pageCount);
                        }));
                    }
                    this.append($("<li><a href='javascript:void(0)'>...</a></li>"));
                    i = page.maxPage - 2;
                }

                if(i > 3 && i > page.maxPage - 2){
                    this.append($("<li " + ((i == page.curPage)?"class='active'":'') + "><a href='javascript:void(0)'>" + i + "</a></li>").click(i,function(e){
                        func(e.data,pageCount);
                    }));
                }
            }
        }
        this.append($("<li><a href='javascript:void(0)' title='下一页'>></a></li>").click(function(){
            func((page.curPage == page.maxPage)?page.curPage:(page.curPage+1),pageCount);
        }));
        this.append($("<li><a href='javascript:void(0)' title='尾页'>>></a></li>").click(function(){
            func(page.maxPage,pageCount);
        }));

        //{"filterRecord":0,"pageCount":15,"curPage":1,"totalRecord":27,"maxPage":2}

        var div = $("<div class='input-group input-group-sm'></div>");

        $("<span class='input-group-addon' style='background:#fff;color:#337ab7;border-left:none'>每页</span>")
            .appendTo(div)

        $("<select class='form-control' style='width: 70px'><option>1</option><option>5</option><option>10</option><option>15</option><option>20</option><option>50</option><option>100</option></select>")
            .appendTo(div)
            .val(page.pageCount)
            .change(function(){
                pageCount = $(this).val();
                func(1,pageCount);
            })

        $("<span class='input-group-addon' style='background:#fff;color:#337ab7;'>条</span>")
            .appendTo(div)

        this.append($("<li class='form-inline'></li>").append(div));

        this.append("<li><span>共" + page.totalRecord + "条</span></li>");

        var div2 = $("<div class='input-group input-group-sm' style='width:90px;'></div>");
        $("<input type='text' class='form-control'>").appendTo(div2);
        $("<span class='input-group-addon' style='color:#337ab7;cursor: pointer;'>GO</span>")
            .appendTo(div2)
            .click(function(){
                var index = $(this).siblings("input").val();
                if(index && parseInt(index) > 0 && parseInt(index) <=  page.maxPage){
                    func(parseInt(index),pageCount);
                }else{
                    openAlert("页码超出范围!","警告:",1000);
                }
            });

        this.append($("<li class='form-inline'></li>").append(div2));
    },
    div2Module:function(title){
        var thiz = this;
        var context = thiz.children();
        var id = "mod" + (new Date().getTime());
        title = title?title:"";
        var mcontent = $("<div class='modal-content'><div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button><h4 class='modal-title'>" + title + "</h4></div></div>");
        mcontent.append(context);
        var mlog = $("<div class='modal-dialog'></div>");
        mlog.append(mcontent);
        var modal = $("<div class='modal fade' id='" + id + "' tabindex='-1' role='dialog'  aria-labelledby='myModalLabel' aria-hidden='true'></div>")
            .append(mlog);
        modal.appendTo($("body")).modal({show:true}).on("hidden.bs.modal", function() {
            thiz.append(context);
            $(this).removeData("bs.modal");
            modal.remove();
            modal = null;
        });
        var close = thiz.close = function(){
            modal.modal("hide");
        }
        thiz.data("dm",close);
        return thiz;
    },
    modClose:function(){
        this.data("dm")();
    },
    eachTab:function(data,func){
        this.data("tabfunc",func);
        var thiz = this;
        $.each(data,function(i,v){
            var $tr = $(func(i,v));
            thiz.data("tabindexdata" + i,v);
            thiz.data("tabindex" + i,$tr);
            thiz.append($tr);
        });
    },
    eachTabUpdate:function(index,data,func){
        var oldata = this.data("tabindexdata" + index);
        $.extend(oldata,data);
        var $tr = $(this.data("tabfunc")(index,oldata));
        this.data("tabindex" + index).replaceWith($tr);
        //再次存储
        this.data("tabindex" + index,$tr);
        this.data("tabindexdata" + index,oldata);
        $tr.trigger("click");
        if(func){
            func($tr);
        }

    }
});
//判断是否存在menu
function menuItem(id){
    if(id instanceof Array){
        $.db_menus = id.sort(function(a,b){return a >b});
    }
    else if((typeof id) == "number" || (typeof id) == "string"){
        return findArray($.db_menus,id);
    }

    function findArray(arr,val){
        if(arr.length <= 1){
            if(arr[0] == val){
                return true;
            }else{
                return false;
            }
        }else{
            var i = Math.round( arr.length/2);
            if(arr[i] == val) return true;
            if(arr[i] > val){
                return findArray(arr.slice(0,i),val);
            }else if(arr[i] < val){
                return findArray(arr.slice(i),val);
            }
        }
    }
}
/**
 * jquery 静态扩展 消息框 默认-1
 * 参数:
 * option:{
		 * 		millis:毫秒数 大于0就会在此时间内自动关闭次框
		 * 		title:标题名 默认为 "消息框"
		 * 		text: 内容
		 * 		top:  距离上部距离
		 * 		width: 宽度 默认的 "300px"
		 * 		buttons: 弹出框的下部按钮为数组格式 例:[ 
		 * 										{
		 * 											name:按钮名称
		 * 										 	handler:点击按钮执行的函数
		 * 											},
		 * 										{
		 * 											name:按钮名称
		 * 											handler:点击按钮执行的函数
		 * 										}	
		 * 									   ]
		 * }
 */
$.message = function(option){
    option = option||{};
    var ops = {
        millis:-1,
        title:"消息框",
        text:"",
        top:"100px",
        width:"300px",
        buttons:[{
            name:'确定',
            handler:function(){
                return true;
            }
        },{
            name:'取消',
            handler:function(){
                return true;
            }
        }]
    };
    $.extend(ops,option);
    if($('#mod_message').length > 0){
        $('#mod_message').remove();
    }
    var left = parseInt($("#wrapper").parent().width() - ops.width.replace("px","") )/2 + "px";
    var content = $("<div id='mod_message' class='modal-content'></div>").css({"width":ops.width,"position":"fixed","top":"0px","left":left ,"zIndex":"2051","opacity": 0});
    if(ops.title){
        content.append('<div class="modal-header"><span style="font-size:14px;font-weight: bold;">'+ ops.title +'</span></div>');
    }
    content.append('<div class="modal-body"><div class="bootbox-body">'+ ops.text +'</div></div>');
    if(ops.buttons){
        var footer = $('<div class="modal-footer"></div>');
        $.each(ops.buttons,function(i,o){
            footer.append($('<button class="btn btn-primary" type="button" >' + o.name + '</button>').click(function(){
                if(o.handler()){
                    content.animate({ "top":"0px",opacity:0},300,function(){
                        content.remove();
                    });
                }
            }));
        });
        content.append(footer);
    }
    $("#wrapper").before(content);
    content.animate({ "top":ops.top,opacity:1},300 );
    if(ops.millis > 0){
        window.setTimeout(function(){
            content.animate({ "top":"0px",opacity:0},300,function(){
                content.remove();
            });
        }, ops.millis);
    }
}
/**
 * jquery 静态扩展 警告框
 * 参数:
 * option:{
		 * 		millis:毫秒数 大于0就会在此时间内自动关闭次框 默认-1
		 * 		title:标题名 默认为 "警告框"
		 * 		text: 内容
		 * 		top:  距离上部距离
		 * 		width: 宽度 默认的 "300px"
		 * }
 */
$.alert = function(option){
    option = option||{};
    var ops = {
        title:"警告框",
        buttons:[{
            name:'确定',
            handler:function(){
                return true;
            }
        }]
    };
    $.extend(ops,option);
    $.message(ops);
}
/**
 * jquery 静态扩展 确认框
 * 参数:
 * option:{
		 * 		millis:毫秒数 大于0就会在此时间内自动关闭次框 默认-1
		 * 		title:标题名 默认为 "警告框"
		 * 		text: 内容
		 * 		top:  距离上部距离
		 * 		width: 宽度 默认的 "300px",
		 *      callback： 点击 ‘是’ 或者 ‘否’ 按钮后的回调函数  并传递参数  点是传递 true  点否 传递false
		 * }
 */
$.confirm = function(option){
    option = option||{};
    var ops = {
        title:"确认框",
        buttons:[{
            name:'是',
            handler:function(){
                if(option && option.callback && (typeof option.callback == 'function')){
                    option.callback(true);
                }
                return true;
            }
        },{
            name:'否',
            handler:function(){
                if(option && option.callback && (typeof option.callback == 'function')){
                    option.callback(false);
                }
                return true;
            }
        }]
    };
    $.extend(ops,option);
    $.message(ops);
}
/*提示框、警告框*/
function prompt(tim,tit,tex,numt,numl){
    $.alert({millis:tim,title:tit,text:tex,top:numt,width:numl});
}
function warning(tim,tit,tex,numt,numl,func){
    $.confirm({
        millis:tim,
        title:tit,
        text : tex,
        top:numt,
        width:numl,
        callback : function(flag) {
            if(func){
                func(flag);
            }
        }
    })
}