<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>蓝源Eloan-P2P平台(系统管理平台)</title>
	<#include "../common/header.ftl"/>
    <link rel="stylesheet" href="/css/bank.css">
    <script type="text/javascript" src="/js/bank.js"></script>
    <script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
    <script type="text/javascript" src="/js/plugins/jquery-validation/jquery.validate.js"></script>
    <script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>

    <script type="text/javascript">
        $(function () {
            $('#pagination').twbsPagination({
                totalPages: ${pageResult.pages},
                startPage: ${pageResult.pageNum},
                visiblePages: 5,
                first: "首页",
                prev: "上一页",
                next: "下一页",
                last: "最后一页",
                onPageClick: function (event, page) {
                    $("#pageNum").val(page);
                    $("#searchForm").submit();
                }
            });

            $("#query").click(function () {
                $("[name=pageNum]").val("1");
                $("#searchForm").submit();
            });

            //对某一条账户明细进行修改
            $(".edit_Btn").click(function () {
                //$(".bankType option").attr("selected" ,false);
                var me = $(this);
                var json = me.data("json");
                $("#editForm input[name=id]").val(json.id);
                $("#bankType option[value=" + json.bankName + "]").attr("selected", true);
                $("#editForm input[name=accountName]").val(json.accountName);
                $("#editForm input[name=bankNumber]").val(json.bankNumber);
                $("#editForm input[name=bankForkName]").val(json.bankForkName);
                $("#companyBankModal").modal("show");
            });

            //添加或者修改账户平台后，点击保存
            $("#saveBtn").click(function () {
                $("#editForm").ajaxSubmit(function (data) {
                    if (data.success) {
                        $.messager.confirm("提示", "编辑成功", function () {
                            window.location.reload();
                        })
                    } else {
                        $.messager.popup(data.msg);
                    }

                });
            });

            //点击添加账户平台，弹出对话框
            $("#addCompanyBankBtn").click(function () {
                $("#editForm").resetForm();
                $("#companyBankModal").modal("show");
            });

            for (var k in SITE_BANK_TYPE_NAME_MAP) {
                //通过key来获取对象的属性。注意提交给后端的是银行对应的code
                var v = SITE_BANK_TYPE_NAME_MAP[k];
                $("<option value='" + k + "'>" + v + "</option>").appendTo($("#bankType"));
            }
        });
    </script>
</head>

<body>
<div class="container">
		<#include "../common/top.ftl"/>
    <div class="row">
        <div class="col-sm-3">
				<#assign currentMenu="companyBank" />
				<#include "../common/menu.ftl" />
        </div>
        <div class="col-sm-9">
            <div class="page-header">
                <h3>平台账户管理</h3>
            </div>
            <div class="row">
                <!-- 提交分页的表单 -->
                <form id="searchForm" class="form-inline" method="post" action="/companyBank_list">
                    <input type="hidden" name="pageNum" value="" id="pageNum"/>
                    <div class="form-group">
                        <button id="query" class="btn btn-success"><i class="icon-search"></i> 查询</button>
                        <a href="javascript:void(-1);" class="btn btn-success" id="addCompanyBankBtn">添加平台账户</a>
                    </div>
                </form>
            </div>
            <div class="row">
                <table class="table">
                    <thead>
                    <tr>
                        <th>银行</th>
                        <th>开户人</th>
                        <th>账号</th>
                        <th>开户行</th>
                    </tr>
                    </thead>
                    <tbody>
						<#list pageResult.list as vo>
                        <tr>
                            <td>
                                <div class="bank bank_${vo.bankName}"></div>
                            </td>
                            <td>${vo.accountName}</td>
                            <td>${vo.bankNumber}</td>
                            <td>${vo.bankForkName}</td>
                            <td>
                                <a href="javascript:void(-1);" class="edit_Btn" data-json='${vo.jsonString}'>修改</a>
                                &nbsp;
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>

                <div style="text-align: center;">
                    <ul id="pagination" class="pagination"></ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="companyBankModal" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">编辑/增加</h4>
            </div>
            <div class="modal-body">
                <form id="editForm" class="form-horizontal" method="post" action="/companyBank_update"
                      style="margin: -3px 118px">
                    <input type="hidden" name="id" value=""/>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">选择银行</label>
                        <div class="col-sm-6">
                            <select id="bankType" class="form-control" autocomplete="off" name="bankName">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">开户人</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="accountName"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">账号</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="bankNumber"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">开户行</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="bankForkName"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" class="btn btn-success" id="saveBtn" aria-hidden="true">保存</a>
                <a href="javascript:void(0);" class="btn" data-dismiss="modal" aria-hidden="true">关闭</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>