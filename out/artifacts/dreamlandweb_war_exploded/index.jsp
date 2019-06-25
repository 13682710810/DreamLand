<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>梦境网</title>
    <link href="${ctx}/css/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"/>

    <link href="${ctx}/css/zui/css/zui.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui-theme.min.css" rel="stylesheet"/>

    <link rel="stylesheet" href="${ctx}/css/reply/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/reply/css/comment.css">

    <style>
        body,html{
            background-color: #EBEBEB;
            padding: 0;
            margin: 0;
            height:100%;
        }
        .stats-buttons  a{
            text-decoration:none;
        }
        .commentAll a{
            text-decoration:none;
        }

        .comment-show a{
            text-decoration:none;
        }
        a {
            color: inherit;
            outline: 0;
        }
        a:-webkit-any-link {
            cursor: pointer;

        }
        .container{
            min-height:100%;
            position: relative;

        }
        #content{
            min-height:100%;
            position: relative;
        }
        .col-md-9{
            padding-bottom: 80px;
        }
        .content-text{
            padding: 20px;

        }
        .single-share{
            float: right;
        }
        .stats-buttons {
            float: left;
        }
        .foot-nav-col li{
            list-style: none;
            margin-left: 100px;
        }
        .foot-nav-col h3{
            margin-left:120px;
        }
        .foot-nav-col a{
            text-decoration:none;
            color:grey;

        }
        .foot-nav-col a:link,a:visited { color:grey;}
        .foot-nav-col a:hover,a:active { color: #6318ff;}

        .foot-nav-col{
            margin-top: 20px;
            float: left;
        }

        .author img {
            width: 35px;
            height: 35px;
            border-radius: 35px;
            padding: 0;
            margin-right: 10px;
        }
        fieldset, img {
            border: 0;
        }
        .author a, .author span {
            float: left;
            font-size: 14px;
            font-weight: 700;
            line-height: 35px;
            color: #9b8878;
            text-decoration: none;
        }

        .author-h2 {
            display: block;
            font-size: 1.5em;
            -webkit-margin-before: 0.83em;
            -webkit-margin-after: 0.83em;
            -webkit-margin-start: 0px;
            -webkit-margin-end: 0px;
            font-weight: bold;
            font-size: 100%;
            font-weight: 400;
        }

        .commentAll{
            margin-left: 5px;
        }

    </style>
</head>
<body>
<div class="container">
    <div>
        <h1>Dreamland-梦境网</h1>
    </div>
    <div style="position: absolute;margin-left: 980px;margin-top: -40px;">
        <c:if test="${empty user}">
            <a href="to_login"><img src="${ctx}/images/Connect_logo_7.png"></a>
            &nbsp;&nbsp;
            <a name="tj_login" class="lb" href="login?error=login" style="color: black">[登录]</a>
            &nbsp;&nbsp;
            <a name="tj_login" class="lb" href="register" style="color: black">[注册]</a>
        </c:if>
        <c:if test="${not empty user}">
            <a name="tj_loginp" href="javascript:void(0);"   class="lb" onclick="personal('${user.id}');" style="color: black"><font color="#9370db">${user.nickName}, 欢迎您！</font></a>
            &nbsp;&nbsp;
            <a name="tj_login" class="lb" href="${ctx}/loginout" style="color: black">[退出]</a>
        </c:if>

    </div>



    <nav class="navbar navbar-inverse">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-menu" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="javascript:void(0);">首页</a>
        </div>
        <div id="navbar-menu" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">最新梦</a></li>
                <li><a href="#">最热梦</a></li>
                <li><a href="#">梦诗词</a></li>
                <li><a href="#">梦问答</a></li>
                <li><a href="#">我的梦</a></li>
                <li><a href="${ctx}/list?id=${user.id}">个人空间</a></li>
            </ul>
        </div>
         <!--搜索框-->
        <form method="post" action="${ctx}/index_list"  id="indexSearchForm"  class="navbar-form navbar-right" role="search" style="margin-top: -35px;margin-right: 10px">
            <div class="form-group">
                <input type="text" id="keyword" name="keyword" value="${keyword}" class="form-control" placeholder="搜索">
            </div>
            &nbsp; &nbsp;<i onclick="searchForm();" class="icon icon-search" style="color: white"></i>
        </form>

    </nav>

    <div id="content" class="row-fluid">
        <div class="col-md-9"  style="background-color: white;">
            <div id="content_col" class="content-main">

                <c:forEach var="cont" items="${page.result}" varStatus="i">
                    <!-- 正文开始 -->

                    <div class="content-text">

                        <div class="author clearfix">
                            <div>
                                <a href="#" target="_blank" rel="nofollow" style="height: 35px">
                                     <img src="${cont.imgUrl}">
                                </a>
                            </div>
                            <div class="author-h2">
                                <div style="float: left;font-size: 15px;color: #9b8878">
                                        ${cont.nickName}
                                </div>
                                <div style="float: left;margin-left: 10px;color: grey;margin-top: 2px;font-size: 12px">
                                        ${cont.formatDate}
                                </div>
                            </div>
                        </div>

                        <h2>${cont.title}</h2>
                        <div style="padding: 0px; float: none;margin-bottom: 10px;" class="xxlib content editormd-preview-theme-dark" id="content-text_${cont.id}">${cont.content}</div>
                        <div style="height: 5px"></div>
                        <div class="stats">
                            <!-- 笑脸、评论数等 -->
                            <span class="stats-vote"><i id="${cont.id}" class="number">${cont.upvoteNum}</i> 赞</span>
                            <span class="stats-comments">
                            <span class="dash"> · </span>
                                <!--点击评论数也会出现评论框-->
                            <a nclick="reply(${cont.id},${cont.uId});">
                               <i class="number" id="comment_num_${cont.id}">${cont.commentNum}</i> 评论
                            </a>
                            </span>
                        </div>
                        <div style="height: 5px"></div>
                        <div class="stats-buttons bar clearfix">
                            <!--点赞 a标签，传递的常量cont是1 -->
                            <a style="cursor: pointer;" onclick="upvote_click(${cont.id},1);">
                                <i class="icon icon-thumbs-o-up icon-2x"></i>
                            </a>
                            &nbsp;
                            <!--踩 a标签，传递的常量cont是-1 -->
                            <a style="cursor: pointer;" onclick="upvote_click(${cont.id},-1);">
                                <i class="icon icon-thumbs-o-down icon-2x"></i>
                            </a>
                            &nbsp;
                            <!--评论小图标，点击出现评论框-->
                            <a style="cursor: pointer;" onclick="reply(${cont.id},${cont.uId});">
                                <i class="icon icon-comment-alt icon-2x"></i>
                            </a>
                        </div>
                        <div class="single-share">
                            <a class="share-wechat" data-type="wechat" title="分享到微信" rel="nofollow" style="margin-left:18px;color: grey;cursor: pointer; text-decoration:none;">
                                <i class="icon icon-wechat icon-2x"></i>
                            </a>
                            <a class="share-qq" data-type="qq" title="分享到QQ" rel="nofollow" style="margin-left:18px;color: grey;cursor: pointer; text-decoration:none;">
                                <i class="icon icon-qq icon-2x"></i>
                            </a>
                            <a  class="share-weibo" data-type="weibo" title="分享到微博" rel="nofollow" style="margin-left:18px;color: grey;cursor: pointer; text-decoration:none;">
                                <i class="icon icon-weibo icon-2x"></i>
                            </a>
                        </div>
                        <br/>

                        <div class="commentAll" style="display:none" id="comment_reply_${cont.id}">
                            <!--评论区域-->
                            <div class="reviewArea clearfix">
                                <textarea style="padding: 10px 15px 10px 15px;" id="comment_input_${cont.id}" class="content comment-input" placeholder="Please enter a comment&hellip;" onkeyup="keyUP(this)"></textarea>
                                <a class="plBtn" id="comment_${cont.id}" onclick="_comment(${cont.id},${user.id==null?0:user.id},${cont.uId})" style="color: white;cursor: pointer;">评论</a>
                            </div>

                            <!--添加评论内容的空div-->
                            <div class="comment-show-first" id="comment-show-${cont.id}">
                            </div>
                        </div>

                        <div class="single-clear">
                        </div>
                    </div>
                    <!-- 正文结束 -->
                    <div style="position: absolute;width:900px;background-color: #EBEBEB;height: 10px;left: 0px">
                    </div>
                </c:forEach>

            </div>




            <!--分页信息-->
            <div id="page-info" style="position: absolute;width:900px;background-color: #EBEBEB;height: 80px;left: 0px;">
                <ul class="pager pager-loose">
                    <c:if test="${page.pageNum <= 1}">
                        <li><a href="javascript:void(0);">« 上一页</a></li>
                    </c:if>
                    <c:if test="${page.pageNum > 1}">
                        <li class="previous"><a href="${ctx}/index_list?pageNum=${page.pageNum-1}&&keyword=${keyword}">« 上一页</a></li>
                    </c:if>
                    <c:forEach begin="${page.startPage}" end="${page.endPage}" var="pn">
                        <c:if test="${page.pageNum==pn}">
                            <li class="active"><a href="javascript:void(0);">${pn}</a></li>
                        </c:if>
                        <c:if test="${page.pageNum!=pn}">
                            <li ><a href="${ctx}/index_list?pageNum=${pn}&&keyword=${keyword}">${pn}</a></li>
                        </c:if>
                    </c:forEach>

                    <c:if test="${page.pageNum>=page.pages}">
                        <li><a href="javascript:void(0);">下一页 »</a></li>
                    </c:if>
                    <c:if test="${page.pageNum<page.pages}">
                        <li><a href="${ctx}/index_list?pageNum=${page.pageNum+1}&&keyword=${keyword}">下一页 »</a></li>
                    </c:if>

                </ul>
            </div>
        </div>


    <div class="col-md-3" style="position:absolute;top:0px;left: 880px;width: 268px;">
		<div style="background-color: white;width: 250px;height: 440px">
			<iframe name="weather_inc" src="http://i.tianqi.com/index.php?c=code&id=82" width="250" height="440" frameborder="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>
		</div>
	</div>


    </div>


</div>
<script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>


<script type="text/javascript" src="${ctx}/css/reply/js/jquery.flexText.js"></script>

</body>
<script language="JavaScript">
    function  showImg(){
        document.getElementById("wxImg").style.display='block';
    }
    function hideImg(){
        document.getElementById("wxImg").style.display='none';
    }
    function personal(uId) {
        this.location =  "${ctx}/list?id="+uId;
    }
    <!--upvote_click的两个参数:文章id；（2）cont常量1（赞）或者-1（踩）-->
    function upvote_click(id, cont) {
        $.ajax({
            type:'post',
            url:'${ctx}/upvote',
            data: {"id":id, "uid":'${user.id}', "upvote":cont},   <!-- 请求参数为文章id、用户id、1或者-1 -->
            dataType:'json',   <!--返回类型为json-->
            success:function (data) {
                var up = data['data'];
                console.log(data)
                if (up == "success"){
                    var num = document.getElementById(id).innerHTML;
                    var value = parseInt(num) + cont;
                    if (value < 0){
                        value = 0;
                    }
                    document.getElementById(id).innerHTML = value;
                } else if (up == "up"){
                    alert("已点赞！")
                } else if(up == "down"){
                    alert("已踩！")
                } else {
                      window.location.href = "${ctx}/login.jsp";
                }
            }
        });
    }

    function FormatDate(myDate) {
        var year = myDate.getFullYear();
        <!--getmonth()的返回值是 0(一月) 到 11(十二月) 之间的一个整数-->
        var month = myDate.getMonth() + 1;
        var date = myDate.getDate();
        var hour = myDate.getHours();
        var minute = myDate.getMinutes();
        if (minute < 10)
            minute = '0' + minute;
        var second = myDate.getSeconds();
        if (second < 10)
            second = '0' + second;
        return year + '-' +month + "-" + date + " " + hour + ':' + minute + ":" + second;
    }

    <!--点击评论数或者评论小图标操作-->
    function reply(id, uid){
        <!--每次展开都要先移除评论列表，然后再添加评论列表到 div，否则会发现每次展开都会增加一倍的评论-->
        $("div").remove("#comment_reply_" + id + " .comment-show");
        $("div").remove("#comment_reply_" + id + " .comment-show-con");
        if (showdiv_display = document.getElementById('comment_reply_' + id).style.display == 'none') {
            <!--判断评论回复列表是显示还是隐藏的，如果显示就将其隐藏，如果隐藏的就将其显示。-->
            document.getElementById('comment_reply_' + id).style.display = 'block';
            $.ajax({
                type:'post',
                url:'${ctx}/reply',
                data: {"content_id" : id},  <!--请求参数就是文章id-->
                dataType:'json',
                success:function (data) {
                    var list = data["list"];
                    var okHtml;
                    if (list != null && list != ""){
                        <!--如果评论列表 list 不为空，对其进行遍历-->
                        $(list).each(function () {
                            <!--一级评论列表-->
                            var chtml = "<div class='comment-show'>"+
                                "<div class='comment-show-con clearfix'>"+
                                "<div class='comment-show-con-img pull-left'><img src='"+this.user.imgUrl+"' alt=''></div>"+
                                "<div class='comment-show-con-list pull-left clearfix'>"+
                                "<div class='pl-text clearfix'>"+
                                "<a  class='comment-size-name'>"+this.user.nickName+" :</a>"+
                                "<span class='my-pl-con'>&nbsp;"+this.comContent+"</span>"+
                                "</div> <div class='date-dz'><span class='date-dz-left pull-left comment-time'>"+FormatDate(this.commTime)+"</span>"+
                                "<div class='date-dz-right pull-right comment-pl-block'>"+
                                "<a onclick='deleteComment("+id+","+uid+","+this.id+",null)' id='comment_dl_"+this.id+"' style='cursor:pointer' class='removeBlock'>删除</a>"+
                                "<a style='cursor:pointer' onclick='comment_hf("+id+","+this.id+","+null+","+this.user.id+","+uid+")' id='comment_hf_"+this.id+"' class='date-dz-pl pl-hf hf-con-block pull-left'>回复</a>"+
                                "<span class='pull-left date-dz-line'>|</span>"+
                                "<a onclick='reply_up("+this.id+")' style='cursor:pointer' class='date-dz-z pull-left' id='change_color_"+this.id+"'><i class='date-dz-z-click-red'></i>赞 (<i class='z-num' id='comment_upvote_"+this.id+"'>"+this.upvoteNum+"</i>)</a>"+
                                "</div> </div> <div class='hf-list-con' id='hf-list-con-"+this.id+"'>";
                            var parentComm_id = this.id;
                            okHtml = chtml;
                            if (this.children != null && this.children != ''){
                                 var commentList = this.comList;
                                 $(commentList).each(function () {
                                     <!--子评论列表-->
                                     var oHtml = "<div class='all-pl-con'><div class='pl-text hfpl-text clearfix'>"+
                                         "<a class='comment-size-name'>"+this.user.nickName+"<a class='atName'>@"+this.byUser.nickName+" :</a> </a>"+
                                         "<span class='my-pl-con'>"+this.comContent+"</span>"+
                                         "</div><div class='date-dz'> <span class='date-dz-left pull-left comment-time'>"+FormatDate(this.commTime)+"</span>"+
                                         "<div class='date-dz-right pull-right comment-pl-block'>"+
                                         "<a style='cursor:pointer' onclick='deleteComment("+id+","+uid+","+this.id+","+parentComm_id+")' id='comment_dl_"+this.id+"' class='removeBlock'>删除</a>"+
                                         "<a onclick='comment_hf("+id+","+this.id+","+parentComm_id+","+this.user.id+","+uid+")' id='comment_hf_"+this.id+"' style='cursor:pointer' class='date-dz-pl pl-hf hf-con-block pull-left'>回复</a> <span class='pull-left date-dz-line'>|</span>"+
                                         "<a onclick='reply_up("+this.id+")' id='change_color_"+this.id+"' style='cursor:pointer' class='date-dz-z pull-left'><i class='date-dz-z-click-red'></i>赞 (<i class='z-num' id='comment_upvote_"+this.id+"'>"+this.upvoteNum+"</i>)</a>"+
                                         "</div></div> </div>";
                                     okHtml = okHtml + oHtml;
                                 });
                            }
                            var ehtml = "</div></div></div></div>";
                            okHtml = okHtml + ehtml;
                            <!--将评论回复列表 div 追加在当前文章的可添加评论内容的空 div 下。该 div 的 id 根据文章 id 区分-->
                            $("#comment-show-" + id).append(okHtml);
                        })
                    }
                }
            });
        }else{
            document.getElementById('comment_reply_' + id).style.display = 'none';
        }
    }
    <!--keyUp 事件，键盘弹起时触发，主要是限制输入内容的长度-->
    function keyUP(t) {
        var len = $(t).val().length;
        if (len > 139){
            $(t).val($(t).val.substring(0, 140));
        }
    }

    <!--点击评论按钮-->
    function _comment(content_id, uid, cuid){
        var myDate = new Date();
        var now = FormatDate(myDate);
        //获取输入内容
        var oSize = $("#comment_input_" + content_id).val();
        console.log(oSize);
        //动态创建模块
        if (oSize.replace(/(^\s*)|(\s*$)/g, "") != '') {
            $.ajax({
                type:'post',
                url:'${ctx}/comment',
                //发送 AJAX 请求，请求参数有：文章 id、评论用户 id、评论内容 oSize、评论时间 now
                data:{"content_id":content_id, "uid": uid, "oSize": oSize, "comment_time": now},
                dataType:'json',
                success:function (data) {
                    var comm_data = data["data"];
                    if(comm_data == "fail"){
                        window.location.href = "${ctx}/login.jsp";
                    }else {
                        //评论成功后创建评论块
                        var id = comm_data.id;
                        var oHtml = '<div class="comment-show-con clearfix"><div class="comment-show-con-img pull-left"><img src="${user.imgUrl}" alt=""></div> <div class="comment-show-con-list pull-left clearfix"><div class="pl-text clearfix"> <a  class="comment-size-name">${user.nickName} : </a> <span class="my-pl-con">&nbsp;'+ oSize +'</span> </div> <div class="date-dz"> <span class="date-dz-left pull-left comment-time">'+now+'</span> <div class="date-dz-right pull-right comment-pl-block"><a style="cursor:pointer"  onclick="deleteComment('+content_id+','+cuid+','+id+','+null+')" id="comment_dl_'+id+'"  class="removeBlock">删除</a> <a style="cursor:pointer" onclick="comment_hf('+content_id+','+id+','+null+','+comm_data.user.id+','+cuid+')" id="comment_hf_'+id+'" class="date-dz-pl pl-hf hf-con-block pull-left">回复</a> <span class="pull-left date-dz-line">|</span> <a onclick="reply_up('+id+')" id="change_color_'+id+'" style="cursor:pointer"  class="date-dz-z pull-left" ><i class="date-dz-z-click-red"></i>赞 (<i class="z-num" id="comment_upvote_'+id+'">0</i>)</a> </div> </div><div class="hf-list-con"></div></div> </div>';
                        //parent()返回被选元素的所有父元素，siblings()方法返回被选元素的所有共享相同父元素的元素，prepend() 方法在被选元素的开头（仍位于内部）插入指定内容
                        $("#comment_" + content_id).parents('.reviewArea').siblings('.comment-show-first').prepend(oHtml);
                        //find()方法获得当前元素集合中每个元素的所有后代，prop()方法设置或返回被选元素的属性和值
                        $("#comment_" + content_id).siblings('.flex-text-wrap').find('.comment-input').prop('value', '').siblings('pre').find('span').text('');
                        $("#comment_input_" + content_id).val('');
                        //innerHTML在JS是双向功能：获取对象的html内容 或 向对象插入html内容
                        var num = document.getElementById("comment_num_" + content_id).innerHTML;
                        document.getElementById("comment_num_"+content_id).innerHTML = (parseInt(num) + 1)+"";
                    }
                }
            })
        }
    }

    <!--删除评论-->
    function deleteComment(con_id, uid, id, fid){
        if ('${user.id}' == uid){
            if (!confirm("确定要删除？")) {
                <!--判断用户是否是文章作者，如果不是就不做处理，如果是则提示是否确认删除-->
                window.event.returnValue = false;
            }else {
                $.ajax({
                    type:'post',
                    url:'${ctx}/deleteComment',
                    <!--请求参数主要有评论 id、文章作者 uid、文章 con_id 和父评论 fid-->
                    data: {"id": id, "uid":uid, "con_id":con_id, "fid":fid},
                    dataType:'json',
                    success:function (data) {
                        var comm_data = data['data'];
                        if (comm_data == "fail"){
                            window.location.href = "${ctx}/login.jsp";
                        }else if (comm_data == "no-access"){
                            alert("没有权限!");
                        }else {
                            var oThis = $("#comment_dl_" + id);
                            var oT = oThis.parents('.date-dz-right').parents('.date-dz').parents('.all-pl-con');
                            if (oT.siblings('.all_pl-con').length >= 1){
                                oT.remove();
                            }else {
                                oThis.parents('.date-dz-right').parents('.date-dz').parents('.all-pl-con').parents('.hf-list-con').css('display','none')
                                oT.remove();
                            }
                            oThis.parents('.date-dz-right').parents('.date-dz').parents('.comment-show-con-list').parents('.comment-show-con').remove();
                            document.getElementById("comment_num" + con_id).innerHTML = parseInt(comm_data) + "";
                        }
                    }
                })
            }
        }
    }


    <!--点击评论回复创建回复块-->
    <!--文章 id、评论 id、一级评论 id（这里为 null，因为一级评论没有父评论)、被评论者 id、评论者 id-->
    function comment_hf(content_id, comment_id, fid, by_id, cuid) {
        var oThis = $("#comment_hf_" + comment_id);
        //根据回复块对象的父节点的父节点获取之前评论人的名字，赋值给 fhName
        var fhName = oThis.parents('.date-dz-right').parents('.date-dz').siblings('.pl-text').find('.comment-size-name').html();
        //将“回复@”与被回复人的名字进行拼接，赋值给 fhN。
        var fhN = '回复@' + fhName;
        //创建回复 input 框和评论按钮 div，赋值给 fhHtml，input 框内显示 回复@被回复人名字
        var fhHtml = '<div class="hf-con pull-left"> <textarea id="plcaceholder_'+comment_id+'"  class="content comment-input " placeholder="'+fhN+'" onkeyup="keyUP(this)"></textarea> <a id="comment_pl_'+comment_id+'" onclick="comment_pl('+content_id+','+comment_id+','+fid+','+by_id+','+cuid+')" class="hf-pl" style="color: white;cursor:pointer">评论</a></div>';
        //if 和 else 主要判断是显示还是隐藏。点击回复 显示 input 框和评论按钮，再次点击则隐藏
        if (oThis.is('.hf-con-block')){
            oThis.parents('.date-dz-right').parents('.date-dz').append(fhHtml);
            oThis.removeClass('hf-con-block');
            $('.content').flexText();
            oThis.parents('.date-dz-right').siblings('.hf-con').find('.pre').css('padding', '6px 15px');
            //input框自动聚焦 ,val() 方法返回或设置被选元素的值
            oThis.parents('.date-dz-right').siblings('.hf-con').find('.hf-input').val('').focus().val(fhN);
        }else {
            oThis.addClass('.hf-con-block');
            oThis.parents('.date-dz-right').siblings('.hf-con').remove();
        }
    }


    <!--评论点赞-->
    <!--id: 评论id-->
    function reply_up(id) {
        <!--获取点赞数赋值给 num-->
        var num = document.getElementById("comment_upvote_" + id).innerHTML;
        <!--如果已经点赞，再次点击则点赞数-1，移除文字和点赞红心效果-->
        if ($("#change_color_" + id).is('.date-dz-z-click')) {
            num--;
            $("#change_color_" + id).removeClass('date-dz-z-click red');
            <!--html();设置或者获取所选元素的内容（包括html标记）；-->
            $("#change_color_" + id).find('.z-num').html(num);
            $("#change_color_" + id).find('.date-dz-z-click-red').removeClass('red');
        }else {
            <!--如果没有点赞，或取消赞后再次点击，则点赞数+1，添加文字和点赞红心效果-->
            num++;
            $("#change_color_"+id).addClass('date-dz-z-click');
            $("#change_color_"+id).find('.z-num').html(num);
            $("#change_color_"+id).find('.date-dz-z-click-red').addClass('red');
        }
        $.ajax({
            type:'post',
            url:'${ctx}/comment',
            data:{"id":id, "upvote":num}, <!--请求参数为评论 id 和点赞数-->
            dataType:'json',
            success:function (data) {
                var comm_data = data["data"];
                if (comm_data == "fail"){
                    window.location.href = "/login.jsp";
                }
            }
        });
    }


    <!--评论块的评论操作-->
    function comment_pl(content_id, comment_id, fid, by_id, cuid) {
        if (fid == null){
            fid = comment_id;
        }
        var oThis = $("#comment_pl_" + comment_id);
        var myDate = new Date();
        var now = FormatDate(myDate);
        //获取输入内容
        var oHfVal = oThis.siblings('.flex-text-wrap').find('.comment-input').val();
        console.log(oHfVal);
        var oHfName = oThis.parents('.hf-con').parents('.date-dz').siblings('.pl-text').find('.comment-size-name').html();
        var oAllVal = '回复@' + oHfName;
        if (oHfVal.replace(/^ +| +$/g,'') == '' || oHfVal == oAllVal) {
            console.log("fail");
        }else {
            $.ajax({
                type:'post',
                url:'${ctx}/comment_child',
                <!--请求参数为文章 id、评论用户 id、回复内容 oHfVal、评论时间 now、被评论者 id和一级评论 id。-->
                data:{"content_id":content_id,"uid":'${user.id}',"oSize":oHfVal,"comment_time":now,"by_id":by_id,"id":fid},
                dataType:'json',
                success:function (data) {
                    var comm_data =  data["data"];
                    //alert(comm_data);
                    if(comm_data=="fail") {
                        window.location.href = "/login.jsp";
                    }else {
                        //创建评论块赋值给 oHtml，然后添加在第一行
                        var id = comm_data.id;
                        var oAt = '回复<a class="atName">@'+oHfName+'</a>  '+oHfVal;
                        var oHtml = '<div class="all-pl-con"><div class="pl-text hfpl-text clearfix"><a class="comment-size-name">${user.nickName} : </a><span class="my-pl-con">'+oAt+'</span></div><div class="date-dz"> <span class="date-dz-left pull-left comment-time">'+now+'</span> <div class="date-dz-right pull-right comment-pl-block"> <a style="cursor:pointer" onclick="deleteComment('+content_id+','+cuid+','+id+','+fid+')" id="comment_dl_'+id+'" class="removeBlock">删除</a> <a onclick="comment_hf('+content_id+','+id+','+fid+','+comm_data.user.id+','+cuid+')" id="comment_hf_'+id+'" style="cursor:pointer" class="date-dz-pl pl-hf hf-con-block pull-left">回复</a> <span class="pull-left date-dz-line">|</span> <a onclick="reply_up('+id+')" id="change_color_'+id+'" style="cursor:pointer" class="date-dz-z pull-left"><i class="date-dz-z-click-red"></i>赞 (<i class="z-num" id="comment_upvote_'+id+'">0</i>)</a> </div> </div></div>';
                        $("#comment_pl_" + comment_id).parents('.hf-con').parents('.comment-show-con-list').find('.hf-list-con').css('display','block').prepend(oHtml) && oThis.parents('.hf-con').siblings('.date-dz-right').find('.pl-hf').addClass('hf-con-block') && oThis.parents('.hf-con').remove();
                        //获取评论数，将评论数+1，赋值到页面上
                        var num = document.getElementById("comment_num_"+content_id).innerHTML;
                        document.getElementById("comment_num_"+content_id).innerHTML = (parseInt(num) + 1)+"";
                    }
                }
            })
        }
    }

    //搜索
    function searchForm(){
        var keyword =  $("#keyword").val();
        if(keyword!=null && keyword.trim()!=""){
            $("#indexSearchForm").submit();
        }
    }
</script>



</html>