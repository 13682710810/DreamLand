<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册成功页面</title>
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

            height: auto;
            height: 700px;
            width: auto;
            width: 1100px;
            margin-top: 5px;
            margin-left: 200px;
            margin-right: 180px;
        }
        .register-active{
            background-color: white;
            height: 200px;
            width: 1000px;
        }
        .register-success{
            background-color: white;
            height: 100px;
            width: 1000px;
            text-align: center;
            line-height: 90px;
        }

        .register-question{
            background-color: white;
            height: 350px;
            width: 1000px;
            padding: 30px;
        }
        .single-clear{
            background-color: #EBEBEB;
            height: 5px;
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
            float: left;
        }
    </style>
</head>
<body>
<div class="active-head">
    <div style="line-height: 44px;margin-left: 5px">
        <span style="color: white;font-family: Arial;font-size: 18px">Dreamland - 梦境网注册成功页面</span>
    </div>
    <div class="content">
        <div class="single-clear">
        </div>

        <div class="register-success">
            <i class="icon-2x icon-check-circle-o" style="color: green"></i>&nbsp;&nbsp;<span style="font-size: 25px">恭喜注册成功！</span>
        </div>

        <div class="single-clear">

     </div>
        <!--根据键 message 取出后台存入 model 中的值，然后根据 , 号切割，取第一个元素就是邮箱了。-->
        <div class="register-active">
           <span style="font-size: 15px;font-weight: bold;padding: 20px;line-height: 100px">激活邮件已经发送到您的注册邮箱${message.split(",")[0]},点击邮件里的链接即可激活账号。</span><br/>
            <button style="margin-left: 20px;" class="btn btn-primary" type="button" onclick="lookEmail('${message}');">立即查看邮件</button>
        </div>

        <div class="single-clear">

        </div>

        <div class="register-question">
            <a>还没有收到邮件?</a><br/><br/>
            1.稍等片刻,重新检查您的收件箱（根据网络状况,可能需要2-5分钟您才能收到）<br/><br/>
            2.您可以选择<button style="margin-left: 10px;" class="btn btn-primary" id="reBtn" onclick="reSendEmail('${message}')" type="button">重新发送邮件</button><br/><br/>
            3.邮件地址写错了?抱歉,您需要<button style="margin-left: 10px;" class="btn btn-primary" id="btn" type="button" onclick="reRegist();">重新注册</button><br/><br/>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
</body>
<script language=javascript>
    function lookEmail(message) {
        var arr = message.split(",");  <!--参数 message 是邮箱和激活码通过 , 号拼接的字符串，根据 , 号切割取出邮箱。-->
        var email = arr[0];
        var opt = email.split("@")[1];   <!--根据 @ 切割取出邮箱 @ 后面部分，主要根据 @ 后面的信息进行跳转。-->
        if("qq.com"==opt){   <!--如果后面部分是 qq.com 则链接到 QQ 邮箱，其他同理。-->
            location.href = "https://mail.qq.com/";
        }else if("163.com"==opt){
                location.href = "https://mail.163.com/";
        }else if("162.com"==opt){
            location.href = "https://mail.162.com/";
        }else if("sina.com"==opt){
            location.href = "http://mail.sina.com.cn/";
        }else if("sohu"==opt){
            location.href = "https://mail.sohu.com";
        }
    }

    function reSendEmail(message) {
       var arr = message.split(",");    <!--通过","号切割分别拿到邮箱和激活码。-->
       var email = arr[0];
       var code = arr[1];
        $.ajax({      <!--发送 AJAX 请求，映射 URL 为 /sendEmail，请求参数 data 是邮箱和激活码。-->
            type:'post',
            url:'${ctx}/sendEmail',
            data: {"email":email,"validateCode":code},
            dataType:'json',
            success:function(data){
                //alert(data["success"])
                var s = data["success"];
                if(s=="success"){
                    alert("发送成功！");
                }
            }
        });
    }

    function reRegist() {
        location.href = "../dreamland/register.jsp";
    }
</script>
</html>