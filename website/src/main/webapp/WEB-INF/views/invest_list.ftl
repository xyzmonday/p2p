<#if pageResult.list?size &gt; 0 >
    <#list pageResult.list as data>
		<tr>
            <td>${data.createUser.username }</td>
            <td>${data.title}</td>
            <td class="text-info">${data.currentRate}%</td>
            <td class="text-info">${data.bidRequestAmount}</td>
            <td>${data.returnTypeDisplay}</td>
            <td>
                <div class="">
                    ${data.percent} %
                </div>
            </td>
            <td><a class="btn btn-danger btn-sm"
                   href="/borrow_info?id=${data.id}">查看</a></td>
        </tr>
    </#list>
<#else>
	<tr>
        <td colspan="7" align="center">
            <p class="text-danger">目前没有符合要求的标</p>
        </td>
    </tr>
</#if>

<script type="text/javascript">
    $(function () {
        $("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
        $("#pagination").twbsPagination({
            totalPages:${pageResult.pages},
            pageNum:${pageResult.pageNum},
            initiateStartPageClick: false,
            onPageClick: function (event, page) {
                $("#pageNum").val(page);
                $("#searchForm").submit();
            }
        });
    });
</script>