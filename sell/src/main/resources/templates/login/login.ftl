
<html>
<head>
    <meta charset="UTF-8">
    <title>登陆</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<!-- 顶部 -->
<div class="body">
    <div class="title">

    </div>
</div>
<!-- 内容 -->
<div class="middle">
    <div class="deng clear">
        <form action ="view" method="post">
            <div class="denglu">
                <div class="first">
                    <a href="javascript:;" class="left">帐号登录</a>
                    <span class="line"></span>

                </div>
                <div class="erweima">
                    <div class="second">
                        <div class="mima">
                            <input type="text" name="username" id ="name" placeholder="账号" class="zh">
                            <input type="password" name="password" id="pwd" placeholder="密码" class="mm">
                            <input type="image" src="image/dl.jpg"  class="tp">
                            <div class="di clear">
                                <a href="" class="zb">
                                    手机短信登录
                                </a>
                                <a href="" class="ybz">
                                    忘记密码?
                                </a>

                            </div>
                        </div>
                    </div>
                    <div class="erwei">
                        <div class="erwei1"><img src="image/shaoma.jpg" alt=""></div>
                        <div class="erweizi">
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>


<script>
    $(".first a").click(function () {
        $(this).css("color","#ff6700").siblings("a").css("color","#ccc");
    });
    $(".first .right").click(function () {
        $(this).parents(".first").next().find(".erwei").css("display","block");
    })
    $(".first .left").click(function () {
        $(this).parents(".first").next().find(".erwei").css("display","none");
    })
</script>


<script>
    // 登录处理
    layui.use(['form','layer','jquery'], function () {


        var form = layui.form;
        var $ = layui.jquery;

        form.on('submit(login)',function (data) {
            var username = $('#username').val();
            var password = $('#password').val();
            var remember = $('input:checkbox:checked').val();
            var imageCode = $('#imgcode').val();
            $.ajax({

                url:"/login",//请求路径
                data:{
                    "username": username,//字段和html页面的要对应  id和name一致
                    "password": password,//字段和html页面的要对应
                    // "remember-me":remember,
                    // "imageCode": imageCode
                },
                dataType:"json",
                type:'post',
                async:false,
                success:function (data) {
                    if (data.code == 402){
                        layer.alert("用户名不存在",function () {
                            window.location.href = "/login/login"
                        });
                    }
                    if (data.code == 403){
                        layer.alert(data.msg,function () {
                            window.location.href = "/login/login"
                        });

                    }
                    // if (data.code == 100){
                    //     layer.alert("该用户已经被冻结，请联系管理员进行解冻",function () {
                    //         window.location.href = "/login_page"
                    //     });
                    // }
                    if(data.code == 200){
                        window.location.href = "/";
                    }
                    // if (data.code == 101){
                    //     layer.alert(data.msg,function () {
                    //         window.location.href = "/login_page"
                    //     });
                    // }


                }
            });

        })

    });

</script>
</body>
</html>