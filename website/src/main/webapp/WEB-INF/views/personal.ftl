<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>蓝源Eloan-P2P平台</title>
    <!-- 包含一个模板文件,模板文件的路径是从当前路径开始找 -->
		<#include "common/links-tpl.ftl" />
    <script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
    <link type="text/css" rel="stylesheet" href="/css/account.css"/>

    <script type="text/javascript">
        $(function () {
            //弹出手机绑定对话框。先必须判断有绑定手机的窗口
            if ($("#showBindPhoneModal").size() > 0) {
                //点击立刻绑定,弹出模式窗口
                $("#showBindPhoneModal").click(function () {
                    //对话框恢复初始
                    $("#bindPhoneForm")[0].reset();
                    $("#bindPhoneModal").modal("show");
                });

                //给发送短信按钮添加时间
                $("#sendVerifyCode").click(function () {
                    //验证手机号格式是否正确
                    var phoneNumber = $("#phoneNumber").val();
                    console.log("phoneNumber = " + phoneNumber);

                    if (!(/^1(3|4|5|7|8)\d{9}$/.test(phoneNumber))) {
                        $.messager.popup("手机号不合法");
                        return;
                    }

                    var _this = $(this);
                    //只要点击那么就禁用，防止反复点击
                    _this.attr("disabled", true);
                    $.ajax({
                        url: "/sendVerifyCode",
                        dateType: "json",
                        type: "post",
                        data: {phoneNumber: phoneNumber},
                        success: function (data) {
                            if (data.success) {
                                //创建定时器
                                var sec = 90;
                                var timer = window.setInterval(function () {
                                    sec--;
                                    if (sec > 0) {
                                        _this.text(sec + "秒重新发送");
                                    } else {
                                        window.clearInterval(timer);
                                        _this.text("重新发送");
                                        _this.attr("disabled", false);
                                    }
                                }, 1000)
                            } else {
                                $.messager.popup(data.msg);
                                _this.attr("disabled", false);
                            }
                        }
                    });
                    //发送ajax，如果请求失败，那么显示失败信息
                    //如果请求成功,那么将发送短信按钮禁用请求定时器
                });

                //给提交绑定窗口按钮添加事件
                $("#bindPhoneForm").ajaxForm(function (data) {
                    if (data.success) {
                        window.location.reload();
                    } else {
                        $.messager.popup(data.msg);
                    }
                });
                $("#bindPhone").click(function () {
                    $("#bindPhoneForm").submit();
                });
            }

            //发送邮件
            if ($("#showBindEmailModal").size() > 0) {
                //点击立刻绑定,弹出模式窗口
                $("#showBindEmailModal").click(function () {
                    $("#bindEmailForm")[0].reset();
                    $("#bindEmailModal").modal("show");
                });
                //将绑定表单装换为ajax表单
                $("#bindEmailForm").ajaxForm(function (data) {
                    if (data.success) {
                        window.location.reload();
                    } else {
                        $.messager.popup(data.msg);
                    }
                });
                //点击绑定邮箱按钮，ajax提交数据
                $("#bindEmail").click(function () {
                    $("#bindEmailForm").submit();
                });
            }
        })
    </script>
</head>
<body>
<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl" />
<!-- 网页导航 -->
<!-- 在当前的freemarker的上下文中,添加一个变量,变量的名字叫nav,变量的值叫personal -->
		<#assign currentNav="personal" />
		<#include "common/navbar-tpl.ftl" />

<div class="container">
    <div class="row">
        <!--导航菜单-->
        <div class="col-sm-3">
					<#assign currentMenu="personal" />
					<#include "common/leftmenu-tpl.ftl" />
        </div>
        <!-- 功能页面 -->
        <div class="col-sm-9">
            <div class="panel panel-default">
                <div class="panel-body el-account">
                    <div class="el-account-info">
                        <div class="pull-left el-head-img">
                            <img class="icon" src="/images/ms.png"/>
                        </div>
                        <div class="pull-left el-head">
                            <p>用户名：${loginInfo.username}</p>
                            <p>最后登录时间：${loginInfo.lastLoginTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                        </div>
                        <div class="pull-left" style="text-align: center;width: 400px;margin:30px auto 0px auto;">
                            <a class="btn btn-primary btn-lg" href="/recharge">账户充值</a>
                            <a class="btn btn-danger btn-lg" href="/moneyWithdraw">账户提现</a>
                        </div>
                        <div class="clearfix"></div>
                    </div>

                    <div class="row h4 account-info">
                        <div class="col-sm-4">
                            账户总额：<span class="text-primary">${account.totalAmount}元</span>
                        </div>
                        <div class="col-sm-4">
                            可用金额：<span class="text-primary">${account.usableAmount}元</span>
                        </div>
                        <div class="col-sm-4">
                            冻结金额：<span class="text-primary">${account.freezedAmount}元</span>
                        </div>
                    </div>

                    <div class="row h4 account-info">
                        <div class="col-sm-4">
                            待收利息：<span class="text-primary">${account.unReceiveInterest}元</span>
                        </div>
                        <div class="col-sm-4">
                            待收本金：<span class="text-primary">${account.unReceivePrincipal}元</span>
                        </div>
                        <div class="col-sm-4">
                            待还本息：<span class="text-primary">${account.unReturnAmount}元</span>
                        </div>
                    </div>

                    <div class="el-account-info top-margin">
                        <div class="row">
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/shiming.png"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>实名认证</h5>
												<#if userInfo.isRealAuth >
												<p>
                                                    已认证
                                                    <a href="#">查看</a>
                                                </p>
                                                <#else>
												<p>
                                                    未认证
                                                    <a href="/realAuth" id="">立刻绑定</a>
                                                </p>
                                                </#if>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">实名认证之后才能在平台投资</p>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/shouji.jpg"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>手机认证</h5>
												<#if userInfo.isBindPhone >
												<p>
                                                    已认证
                                                    <a href="#">查看</a>
                                                </p>
                                                <#else>
												<p>
                                                    未认证
                                                    <a href="javascript:;" id="showBindPhoneModal">立刻绑定</a>
                                                </p>
                                                </#if>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">可以收到系统操作信息,并增加使用安全性</p>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/youxiang.jpg"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>邮箱认证</h5>
												<#if userInfo.isBindEmail>
												<p>
                                                    已绑定
                                                    <a href="#">查看</a>
                                                </p>
                                                <#else>
												<p>
                                                    未绑定
                                                    <a href="javascript:;" id="showBindEmailModal">去绑定</a>
                                                </p>
                                                </#if>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">您可以设置邮箱来接收重要信息</p>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                <div class="el-accoun-auth">
                                    <div class="el-accoun-auth-left">
                                        <img src="images/baozhan.jpg"/>
                                    </div>
                                    <div class="el-accoun-auth-right">
                                        <h5>VIP会员</h5>
                                        <p>
                                            普通用户
                                            <a href="#">查看</a>
                                        </p>
                                    </div>
                                    <div class="clearfix"></div>
                                    <p class="info">VIP会员，让你更快捷的投资</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
		
		<#if !userInfo.isBindPhone>
		<div class="modal fade" id="bindPhoneModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="exampleModalLabel">绑定手机</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" id="bindPhoneForm" method="post" action="/bindPhone">
                            <div class="form-group">
                                <label for="phoneNumber" class="col-sm-2 control-label">手机号:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"/>
                                    <button id="sendVerifyCode" class="btn btn-primary" type="button"
                                            autocomplate="off">发送验证码
                                    </button>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="verifyCode" class="col-sm-2 control-label">验证码:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="verifyCode" name="verifyCode"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="bindPhone">保存</button>
                    </div>
                </div>
            </div>
        </div>
        </#if>
		
		
		<#if !userInfo.isBindEmail>
		<div class="modal fade" id="bindEmailModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="exampleModalLabel">绑定邮箱</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" id="bindEmailForm" method="post" action="/sendEmail">
                            <div class="form-group">
                                <label for="email" class="col-sm-2 control-label">Email:</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="email" name="email"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="bindEmail">保存</button>
                    </div>
                </div>
            </div>
        </div>
        </#if>
		
		
		<#include "common/footer-tpl.ftl" />
</body>
</html>