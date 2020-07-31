<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 상세보기</title>
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
	
	#bbs table th {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	#bbs table td {
	    text-align:left;
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
	
		
</style>

</head>
<body>
	<div id="bbs">
	<form method="post">
		<table summary="게시판 글쓰기">
			<caption>게시판 글쓰기</caption>
			<colgroup>
				<col width="90"/>
				<col width="*"/>
				<col width="80"/>
				<col width="80"/>
			</colgroup>
			<tbody>
			<c:if test="${vo ne null and not empty vo}">
			
				<tr>
					<th>제목:</th>
					<td>${vo.subject}</td>
					<th>조회수:</th>
					<td>${vo.hit}</td>
				</tr>

				<c:if test="${vo.file_name ne nofile and fn:length(vo.file_name)>4}">
				<tr>
					<th>첨부파일:</th>
					<td colspan="3"><a href="javascript:fDown('${vo.file_name}')"><!-- 다운로드는 Servlet -->
						${vo.ori_name}
					</a></td>
				</tr>
				</c:if>
				
				<c:if test="${vo.file_name eq null}">
				<tr>
					<th>첨부파일:</th>
					<td colspan="3">파일이 없습니다.</td>
				</tr>
				</c:if>
				
				<tr>
					<th>이름:</th>
					<td colspan="3">${vo.writer}</td>
				</tr>
				<tr>
					<th>내용:</th>
					<td colspan="3">${vo.content}</td>
				</tr>
					<tr>
						<th>비밀번호:</th>
						<td colspan="3">
							<input type="password" name="pwd" size="12"/>
							<input type="hidden" name="b_idx" value="${vo.b_idx}">
						</td>
					</tr>
				<tr>
					<td colspan="4">
						<input type="hidden" id="nowPage" name="nowPage" value="${nowPage}"/>
						<input type="button" value="수정" onclick="goEdit()"/>
						<input type="button" value="삭제" onclick="goDelete()"/>
						<input type="button" value="목록" onclick="goList()"/>
					</td>
				</tr>
			</c:if>
			<c:if test="${vo eq null and empty vo}">
				<h3>해당 게시글은 존재하지 않습니다.</h3>
				<input type="button" value="목록" onclick="goList()"/>
			</c:if>
			</tbody>
		</table>
	</form>
	<h5>현재 글의 비밀번호 ${vo.pwd} 현재 글의 nowPage ${nowPage} 현재 글의 b_idx ${vo.b_idx}</h5>
	<hr>
	<h3>댓글쓰기</h3>
	<hr>
	<form method="post" action="ans_write.jsp">
		이름:<input type="text" name="writer"/><br/>
		내용:<textarea rows="4" cols="55" name="comm"></textarea><br/>
		비밀번호:<input type="password" name="pwd"/><br/>
		
		
		<input type="hidden" name="b_idx" value="">
		<input type="hidden" name="index" value=""/>
		<input type="submit" value="저장하기"/> 
	</form>
	
	댓글들<hr/>
	
		<div>
			이름:글쓴이 &nbsp;&nbsp;
			날짜:날짜<br/>
			내용:댓글 내용
		</div>
		
	
	</div>
	
	<form method="post" name="f">
		<input type="hidden" id="b_idx" name="b_idx" value="${vo.b_idx}"/>
		<input type="hidden" id="nowPage" name="nowPage" value="${nowPage}"/>
		<input type="hidden" id="bname" name="bname" value="${vo.bname}"/>
		<input type="hidden" id="f_name" name="f_name"/>
	</form>
	
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>	
	<script>
	
		function goEdit(){
			if(document.forms[0].pwd.value == ${vo.pwd}){
				document.forms[0].action = "goEdit";
				document.forms[0].submit();
			}else{
				alert('비밀번호가 맞지않습니다. 수정할 수 없습니다.');
				document.forms[0].pwd.value = "";
			}
		}
		
		function goDelete(){
			if(document.forms[0].pwd.value == ${vo.pwd}){
				document.forms[0].action = "goDelete";
				document.forms[0].submit();
			}else{
				alert('비밀번호가 맞지않습니다. 삭제할 수 없습니다.');
				document.forms[0].pwd.value = "";
			}
		}
		
		function goList(){
			document.f.action = "list";
			document.f.submit();
		}
		
		function fDown(fname){
			//alert(fname);
			//인자로 넘어온 파일 명을
			//현재 문서에 f라는 이름을 가진 form객체에서
			//이름이 f_name인 요소의 값으로 지정하여 서버로 보낸다.
			//그래서 서버가 가지는 파일을 클라이언트에게 다운 받도록 유도한다.
			document.f.f_name.value = fname;
			document.f.action = "FileDownload";
			document.f.submit();
		}
	</script>
</body>
</html>












