<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>商品列表</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/bootstrap.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/3.css"/>
</head>
<body>
<div class="page-header">
    <h1>商品列表</h1>
	<h1 id="itemInfo" style="color: red"></h1>
</div>
<form id="form" action="${pageContext.request.contextPath}/item/list" method="get">
    <table class="table table-hover">
        <tr>
            <th class="active">
                <span style="font-size: 26px;">商品名称:</span>
            </th>
			<input type="hidden" id="getPage" name="page" value="" />
            <td class="warning">
                <input type="text" id="name" value="${name}" name="name" class="form-control" placeholder="商品名称" />
            </td>
            <td class="info">
                <button type="submit"  class="btn btn-success">查询</button>
            </td>
            <td class="danger">
                <button type="button" class="btn btn-info" onclick="goToAdd()">添加商品</button>
            </td>
        </tr>
    </table>
</form>
<c:if test="${pageInfo.list.size()>0}">
<table class="table">
	<tr class="info">
		<td colspan="7">商品列表:</td>
	</tr>
	<tr class="active">
		<th>商品名称</th>
		<th>商品价格</th>
		<th>生产日期</th>
		<th>商品描述</th>
		<th>展示图片</th>
		<th>操作</th>
	</tr>
	<c:forEach items="${pageInfo.list}" var="item">
		<input type="hidden" name="_method" value="delete">
		<tr class="success">
			<td style="width:20%;">
				${item.name }
			</td>
			<td>
				${item.price }
			</td>
			<td>
				<fmt:formatDate value="${item.productionDate }" pattern="yyyy-MM-dd"/>
			</td>
			<td style="width:40%;">${item.description }</td>
			<td>
				<img src="${item.pic }" style="width:80px;height:80px;" />
			</td>
			<td>
				<button type="button" class="btn btn-warning" onclick="goToUpdate(this,${item.id})">修改</button>
				<button type="button" class="btn btn-warning" onclick="del(this,${item.id})">删除</button>
			</td>
		</tr>
	</c:forEach>
</table>
</c:if>
<c:if test="${pageInfo.list.size()==0}">
	<h1>抱歉！暂无数据！</h1>
</c:if>
<div id="page">
    <span style="font-size: 20px;">当前第 ${pageInfo.pageNum} 页 / 共 ${pageInfo.pages} 页  (${pageInfo.total}条)</span>
    <div style="float: right;">
		<c:if test="${pageInfo.hasPreviousPage}" >
        <button class="btn btn-warning" onclick="goTo(1)">首页</button>
        <button class="btn btn-warning" onclick="goTo(${pageInfo.pageNum-1})">上一页</button>
		</c:if>
		<c:if test="${pageInfo.hasNextPage}" >
		<button class="btn btn-warning" onclick="goTo(${pageInfo.pageNum+1})">下一页</button>
        <button class="btn btn-warning" onclick="goTo(${pageInfo.pages})">尾页</button>
		</c:if>
	</div>
</div>

</body>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery.min.js"></script>
<script type="text/javascript">
	function goTo(page) {
		$("input[name = 'page']").val(page);

		$("#form").submit();

	}

	function goToAdd() {
		location.href="${pageContext.request.contextPath}/item/add-ui"
	}
	
	function del(obj,id) {
		$.ajax({
			url:"${pageContext.request.contextPath}/item/del/"+id,
			data:null,
			type:"delete",
			dataType:"json",
			success:function (result) {
				if (result.code==0){
					$(obj).parent().parent().remove();
				} else {
					$("#itemInfo").html(result.msg)
				}
			},
			error:function () {
				alert("删库跑路啦啦啦~")
			}
		});
	}
	function goToUpdate(obj,id) {
		location.href="${pageContext.request.contextPath}/item/update-ui?id="+id;
	}
</script>
</html>