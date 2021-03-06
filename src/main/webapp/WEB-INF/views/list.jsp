<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.Date"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시판 목록</title>
<style type="text/css">
	#bbs table {
	    width:580px;
	    margin-left:10px;
	    border:1px solid black;
	    border-collapse:collapse;
	    font-size:14px;
	    
	}
	
	#bbs table caption {
	    font-size:20px;
	    font-weight:bold;
	    margin-bottom:10px;
	}
	
	#bbs table th,#bbs table td {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	.no {width:15%}
	.subject {width:30%}
	.writer {width:20%}
	.reg {width:20%}
	.hit {width:15%}
	.title{background:lightsteelblue}
	
	.odd {background:silver}
	
	/* paging */
	
	table tfoot ol.paging {
	    list-style:none;
	}
	
	table tfoot ol.paging li {
	    float:left;
	    margin-right:8px;
	}
	
	table tfoot ol.paging li a {
	    display:block;
	    padding:3px 7px;
	    border:1px solid #00B3DC;
	    color:#2f313e;
	    font-weight:bold;
	}
	
	table tfoot ol.paging li a:hover {
	    background:#00B3DC;
	    color:white;
	    font-weight:bold;
	}
	
	.disable {
	    padding:3px 7px;
	    border:1px solid silver;
	    color:silver;
	}
	
	.now {
	   padding:3px 7px;
	    border:1px solid #ff4aa5;
	    background:#ff4aa5;
	    color:white;
	    font-weight:bold;
	}
		
	.empty{
		height: 40px;
		text-align: center;
		font-size: large;
		font-weight: bold;
	}	
</style>
</head>
<body>
	<div id="bbs">
		<table summary="게시판 목록">
			<caption>게시판 목록</caption>
			<thead>
				<tr class="title">
					<th class="no">번호</th>
					<th class="subject">제목</th>
					<th class="writer">글쓴이</th>
					<th class="reg">날짜</th>
					<th class="hit">조회수</th>
				</tr>
			</thead>
			
			<tfoot>
                     <tr>
                         <td colspan="4">
                          <%-- 페이징 코드 --%>
                          ${pageCode}   
               
				<%-- 자바에서 넣어줄꺼다~ 페이징 기법
				<ol class="paging">
					<li><a href="#">&lt;</a></li>
				
					<li class="now">1</li>
				         
					<li><a href="#">2</a></li>
 
					<li><a href="#">&gt;</a></li>
				 </ol>
				--%>
                           
                         </td>
					  <td>
						<input type="button" value="글쓰기"
		onclick="javascript:location.href='write?nowPage=${nowPage}&bname=BBS'"/>
					  </td>
                     </tr>
           	</tfoot>
			<tbody>
			<c:if test="${list ne null and not empty list}">
				<c:forEach items="${list}" var="vo" varStatus="st">
				<tr>
					<td>${rowTotal - ((nowPage-1)*blockList+st.index)}</td>
					<td style="text-align:left">
						<%-- <a href="view?b_idx=${vo.b_idx}&nowPage=${nowPage}&bname=BBS">
							${vo.subject}
						</a> --%>
						<a href="javascript:viewData('${vo.b_idx}',
												     '${nowPage}',
												     'BBS')">
							${vo.subject}
						</a>
					</td>
					<td>${vo.writer}</td>
					<td>${fn:substring(vo.write_date,0,10)}</td>
					<%-- <td>${vo.write_date.substring(0,10)}</td> --%>
					<%-- <td><fmt:parseDate value="${vo.write_date}"
									   var="date"
									   pattern="yyyy-MM-dd"/>
						${date}</td> --%>
					<td>${vo.hit}</td>
				</tr>
				</c:forEach>
			</c:if>
			
			<c:if test="${list eq null or empty list}">
				<tr>
					<td colspan="5" class="empty">
						등록된 게시물이 없습니다.
					</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	
	<%--보기 기능을 위한 폼--%>
	<form action="view" method="post" name="f">
		<input type="hidden" id="b_idx" name="b_idx"/>
		<input type="hidden" id="nowPage" name="nowPage"/>
		<input type="hidden" id="bname" name="bname"/>
	</form>
	
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>	
<script>
	function viewData(b_idx, nowPage, bname){
		document.f.b_idx.value = b_idx;						//파라미터 name으로 값 지정
		document.getElementById("nowPage").value = nowPage;	//파라미터 id로 값 지정
		$("#bname").val(bname);								//JQuery로 값 지정
		
		document.f.submit();
	}

</script>
</body>
</html>