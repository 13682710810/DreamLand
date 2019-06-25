<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>激活成功页面</title>
    <link href="${ctx}/css/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"/>

    <link href="${ctx}/css/zui/css/zui.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui-theme.min.css" rel="stylesheet"/>

    <style>
        body,html{
            background-color: #EBEBEB;
            padding: 0;
            margin: 0;
            height:100%;
        }
        .active-head{
            height: 50px;
            width: 100%;
            background-color: #990033;
        }
        .content{
            background-color: white;
            height: auto;
            height: 500px;
            width: auto;
            width: 1000px;
            margin-top: 5px;
            margin-left: 200px;
        }

        .active-success{

            height: 150px;
            width: 1000px;
            text-align: center;
            line-height: 150px;
            margin-top: 50px;
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
    </style>
</head>
<body>
<div class="active-head">
    <div style="line-height: 44px;margin-left: 5px">
        <span style="color: white;font-family: Arial;font-size: 18px">Dreamland - 梦境网激活成功页面</span>
    </div>
    <div class="content">
        <div class="single-clear">

        </div>

        <div class="active-success">
            <i class="icon-2x icon-check-circle-o" style="color: green"></i>&nbsp;&nbsp;<span style="font-size: 25px;color: green">恭喜激活成功！</span><br/>

        </div>
        <div style="margin-left: 340px"> <span style="font-size: 17px;">您登陆梦境网的用户名为：<span style="color: green;font-weight: bold">${email}</span></span></div>
        <div style="margin-left: 420px;margin-top: 40px">
            <button class="btn btn-primary" type="button" id="btn" style="width: 150px">点此登陆</button>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
</body>
<script language=javascript>
    $("#btn").click(function () {
        location.href="${ctx}/login";
    });
</script>
</html>