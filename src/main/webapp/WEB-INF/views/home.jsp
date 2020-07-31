<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  2020.07.30 과제 중
</h1>

<P>  The time on the server is ${serverTime}. </P>
<br>
<input type="button" value="목록으로" onclick="goList()">
<script>
	function goList() {
		location.href="list";
	}
</script>
</body>
</html>
