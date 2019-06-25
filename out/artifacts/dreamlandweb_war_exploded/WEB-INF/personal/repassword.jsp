<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>修改密码</title>

    <link href="${ctx}/css/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui-theme.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
    <style>
        body,html{
            background-color: #EBEBEB;
            padding: 0;
            margin: 0;
            height:100%;
        }
        body {
            font: 14px/1.5 "PingFang SC","Lantinghei SC","Microsoft YaHei","HanHei SC","Helvetica Neue","Open Sans",Arial,"Hiragino Sans GB","微软雅黑",STHeiti,"WenQuanYi Micro Hei",SimSun,sans-serif;
        }


        .title-content a{
            text-decoration:none;
        }
        .stats-buttons  a{
            text-decoration:none;
        }

        a {
            color: inherit;
            outline: 0;
        }

        .designer-card img {
            border-radius: 50%;
            vertical-align: middle;
        }
        img {
            border: 0;
        }
        a {
            color: inherit;
            outline: 0;
        }
        a:-webkit-any-link {
            cursor: pointer;

        }


        .foot-nav-col li{
            list-style: none;
            margin-left: 70px;
        }
        .foot-nav-col h3{
            margin-left:90px;
        }
        .foot-nav-col a{
            text-decoration:none;
            color:grey;

        }
        .foot-nav-col a:link,a:visited { color:grey;}
        .foot-nav-col a:hover,a:active { color: #6318ff;}

        .foot-nav-col{
            margin-top: 10px;
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



        .glyphicon glyphicon-edit{
            float: left;
            width: 43px;
            height: 42px;
            border: 1px solid #d6d6d6;
            border-right: none;
            cursor: pointer;

        }
        .tab li{list-style:none}
        .table tr:hover{background-color: #dafdf3;}

    </style>


</head>
<body>
<nav class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid">
        <!-- 导航头部 -->
        <div class="navbar-header">
            <!-- 移动设备上的导航切换按钮 -->
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-example">
                <span class="sr-only">切换导航</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <!-- 品牌名称或logo -->
            <a class="navbar-brand" href="javascript:void(0);">个人空间</a>
        </div>
        <!-- 导航项目 -->
        <div class="collapse navbar-collapse navbar-collapse-example">
            <!-- 一般导航项目 -->
            <ul class="nav navbar-nav">
                <li class="active"><a href="your/nice/url">我的梦</a></li>
                <li><a href="${ctx}/index_list">首页</a></li>

                <!-- 导航中的下拉菜单 -->
                <li class="dropdown">
                    <a href="your/nice/url" class="dropdown-toggle" data-toggle="dropdown">设置 <b class="caret"></b></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="your/nice/url">任务</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav">
                <li><a href="your/nice/url">消息</a></li>
            </ul>

            <ul class="nav navbar-nav">
                <li><a href="${ctx}/writedream?id=${user.id}">写梦</a></li>
            </ul>
            <ul class="nav navbar-nav" style="margin-left: 680px">
                <li><a href="${ctx}/list?id=${user.id}">${user.nickName}

                </a></li>
            </ul>
            <img src="images/q.png" width="30" style="margin-top: 4px"/>
        </div><!-- END .navbar-collapse -->
    </div>

</nav>
    <!--修改密码 -->
<div style="margin-left: auto;margin-right: auto;width: 460px;height: 550px;background-color: white">

        <div style="text-align: center">
            <div style="margin-top: 50px;float: left;margin-left: 180px">
                    <span style="font-size: 23px"><strong> 修改密码</strong></span>

            </div>
            <div style="float: left;background-color:#EAEAEA;height: 1px;width: 300px;margin-left: 80px;margin-top: 30px"> </div>

            <div style="float: left;margin-top: 40px;margin-left: 50px">

                <form action="${ctx}/updatePassword" method="post" id="update_password">
                    <input onblur="oldPassword();" id="old_password" name="old_password" type="password" placeholder="    输入旧密码" style="width: 350px;height: 50px"/><br/><br/>
                    <span id="old_span" style="color:red;"></span><br/>
                    <input onblur="newPassword();" id="password" name="password" type="password" placeholder="    输入新密码" style="width: 350px;height: 50px"/><br/><br/><br/>
                    <input onblur="rePassword();" id="repassword" type="password" placeholder="    确认新密码" style="width: 350px;height: 50px"/><br/><br/><br/>
                    <br/>
                    <button onclick="surePost();" style="background-color: #0a67fb;height: 50px"  class="btn btn-block " type="button"><span style="color: white">确认</span></button>
                </form>
            </div>
        </div>
</div>

</body>
<script>


 var f1 = false;
 function oldPassword() {
   var old =  $("#old_password").val();

   if(old==null || old.trim()==''){
       document.getElementById("old_span").innerHTML = "请输入密码！";
       f1 = false;
   }else if(old.length < 6){
       document.getElementById("old_span").innerHTML = "密码长度少于6位，请重新输入！";
       f1 = false;
   }
   else {
       document.getElementById("old_span").innerHTML = "";
       f1 = true;
   }
}

 var f2 = false;
function newPassword() {
    var p = $("#password").val();
    var old =  $("#old_password").val();
    if(p==null || p.trim()==''){
        document.getElementById("old_span").innerHTML = "请输入密码！";
        f2 = false;
    }else if(p.length < 6){

            $("#old_span").text("密码长度少于6位，请重新输入！").css("color","red");
            f2 =  false;

    }else if(p==old){
        $("#old_span").text("新密码与旧密码一致，请重新输入！").css("color","red");
        f2 = false;
    }

    else {
        document.getElementById("old_span").innerHTML = "";
        f2 = true
    }
}
     var f3 = false;
    function rePassword() {
        var p = $("#repassword").val();
        var p1 = $("#password").val();
        if(p==null || p.trim()==''){
            document.getElementById("old_span").innerHTML = "请输入密码！";
            f3 = false;
        }else if(p!=p1){
            document.getElementById("old_span").innerHTML = "两次密码不一致！";
            f3 = false;
        }
        else {
            document.getElementById("old_span").innerHTML = "";
            f3 = true
        }
    }

function surePost() {
    if(f1 && f2 && f3){
        $("#update_password").submit();

    }else {
        $("#old_span").text("请重新输入密码！").css("color","red");
    }
}
</script>
</html>