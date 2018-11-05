<%--@elvariable id="partFilter" type="dto.PartFilterDto"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US"/>
<html>
<head>
    <title>Part</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="application/javascript">
        function setSortField(field) {
            var sortFieldInput = document.getElementById('sortField');
            var previousSortField = sortFieldInput.value;
            sortFieldInput.value = field;
            var sortOrderInput = document.getElementById('sortOrder');
            var sortOrder = sortOrderInput.value;
            if (previousSortField === field && sortOrder && sortOrder === 'asc') {
                sortOrderInput.value = 'desc';
            } else {
                sortOrderInput.value = 'asc';
            }
            document.getElementById('filter-form').submit();
        }
    </script>
</head>
<body>

<form id="filter-form" action="${pageContext.request.contextPath}/part" method="get">
    <table class="filter">
        <tr>
            <th colspan="2">Filter</th>
        </tr>
        <tr>
            <td>PN</td>
            <td><input name="number" value="${partFilter.number}"></td>
        </tr>
        <tr>
            <td>Part Name</td>
            <td><input name="name" value="${partFilter.name}"/></td>
        </tr>
        <tr>
            <td>Vendor</td>
            <td><input name="vendor" value="${partFilter.vendor}"/></td>
        </tr>
        <tr>
            <td>Qty</td>
            <td><input name="qty" value="${partFilter.qty}"/></td>
        </tr>
        <tr>
            <td>Shipped</td>
            <td>
                <span>after</span>
                <input name="shippedAfter" value="<fmt:formatDate value="${partFilter.shippedAfter}" pattern="MMM dd, yyyy" />">
                <span>before</span>
                <input name="shippedBefore" value="<fmt:formatDate value="${partFilter.shippedBefore}" pattern="MMM dd, yyyy" />"/>
            </td>
        </tr>
        <tr>
            <td>Received</td>
            <td>
                <span>after</span>
                <input name="receivedAfter" value="<fmt:formatDate value="${partFilter.receivedAfter}" pattern="MMM dd, yyyy" />"/>
                <span>before</span>
                <input name="receivedBefore" value="<fmt:formatDate value="${partFilter.receivedBefore}" pattern="MMM dd, yyyy" />"/>
            </td>
        </tr>
    </table>
    <input type="hidden" id="sortField" name="sortField" value="${partFilter.sortField}"/>
    <input type="hidden" id="sortOrder" name="sortOrder" value="${partFilter.sortOrder}"/>
    <div class="text-center">
        <input type="submit" class="filter-btn" value="Filter"/>
    </div>
    <table class="parts">
        <tr>
            <th>
                <a href="javascript:void(0)" onclick="setSortField('number')">PN</a>
            </th>
            <th class="part-name">
                <a href="javascript:void(0)" onclick="setSortField('name')">Part Name</a>
            </th>
            <th class="vendor">
                <a href="javascript:void(0)" onclick="setSortField('vendor')">Vendor</a>
            </th>
            <th>
                <a href="javascript:void(0)" onclick="setSortField('qty')">Qty</a>
            </th>
            <th>
                <a href="javascript:void(0)" onclick="setSortField('shipped')">Shipped</a>
            </th>
            <th>
                <a href="javascript:void(0)" onclick="setSortField('received')">Received</a>
            </th>
        </tr>
        <c:forEach items="${parts}" var="part">
            <tr>
                <td>${part.number}</td>
                <td>${part.name}</td>
                <td>${part.vendor}</td>
                <td class="text-center">${part.qty}</td>
                <td class="text-center"><fmt:formatDate value="${part.shipped}" pattern="MMM dd, yyyy" /></td>
                <td class="text-center"><fmt:formatDate value="${part.received}" pattern="MMM dd, yyyy" /></td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>
</html>
