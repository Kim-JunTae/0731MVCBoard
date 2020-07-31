<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글 수정 페이지</title>
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
<!-- include libraries(jQuery, bootstrap) -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">

<!-- include summernote css/js -->
<link rel="stylesheet" href="resources/css/summernote-lite.min.css"/>
<!-- <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet"> -->
<!-- <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script> -->
</head>
<body>
	<div id="bbs">
	<form action="edit" method="post" 
	encType="multipart/form-data">
		<table summary="게시판 글쓰기">
			<caption>게시판 글쓰기</caption>
			<tbody>
				<tr>
					<th>제목:</th>
					<td><input type="text" name="subject" size="45" value="${vo.subject}"/></td>
				</tr>
				<tr>
					<th>이름:</th>
					<td><input type="text" name="writer" size="12" value="${vo.writer}"/></td>
				</tr>
				<tr>
					<th>내용:</th>
					<td><textarea name="content" id="content" cols="50" 
							      rows="8">${vo.content}</textarea></td>
				</tr>
				<c:if test="${vo.file_name ne nofile and fn:length(vo.file_name)>4}">
				<tr>
					<th>첨부파일:</th>
					<td><a href="javascript:fDown('${vo.file_name}')"><!-- 다운로드는 Servlet -->
						${vo.ori_name}
					</a></td>
				</tr>
				</c:if>
				<!--<tr>
					<th>첨부파일:</th>
					<td><input type="file" name="file"/></td>
				</tr> -->

				<tr>
					<th>비밀번호:</th>
					<td><input type="password" name="pwd" size="12" value="${vo.pwd}"/></td>
				</tr>
				
				<tr>
					<td colspan="2">
						<input type="hidden" id="b_idx" name="b_idx" value="${vo.b_idx}"/>
						<input type="hidden" id="nowPage" name="nowPage" value="${nowPage}"/>
						<input type="hidden" id="bname" name="bname" value="${vo.bname}"/>
						<input type="button" value="수정"
						onclick="editData()"/>
						<input type="button" value="다시"/>
						<input type="button" value="목록"
						onclick="goList()"/>
					</td>
				</tr>
			</tbody>
		</table>
		<h5>현재 글의 비밀번호 ${vo.pwd} 현재 글의 nowPage ${nowPage} 현재 글의 b_idx ${vo.b_idx} 현재글의 bname ${vo.bname}</h5>
	</form>
	</div>
	
	<!--0730 추가-->
	<form method="post" name="f">
		<input type="hidden" id="nowPage" name="nowPage" value="${nowPage}"/>
		<input type="hidden" id="bname" name="bname" value="${bname}"/>
	</form>
	
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	
<!-- summernote JS -->
<script src="resources/js/summernote-lite.min.js"></script>
<script src="resources/js/lang/summernote-ko-KR.js"></script>
<script>
	$(function(){
		//에디터 설정
		$("#content").summernote({
			height: 300,
			width: 450,
			minHeight: null,
			maxHeight: null,
			focus: true,
			lang:'ko-KR',
			callbacks:{
				onImageUpload: function(files, editor){
					//이미지가 에디터에 추가될 때마다 수행하는 곳!
					//console.log('Image upload:' + files);
					//이미지가 배열로 인식된다. 이것을 서버로 비동기식 통신을 하는 함수를 호출하자! 
					//여러개 이미지를 넣을수 있다!
					/* for(file : files){
						sendFile(file, editor)
					} */
					//sendFile(files[0], editor);
					
					for(var i=0; i<files.length; i++){
						sendFile(files[i], editor);
					}
				}
			}
		});
		$("#content").summernote('lineHeight', 0.5);
	});
	
	function sendFile(file, editor){
		//이미지를 서버로 업로드 시키기 위해 비동기식 통신을 수행하자!
		//파일 파라미터를 전달하기 위해 폼 객체 준비!
		var frm = new FormData(); //<form encType="multipart/form-data" 를 생성한 것이다.
		
		//보내고자 하는 파일을 등록한다.
		frm.append("file", file);
		
		//비동기식 통신
		//파일을 보낼 때는 일반적인 데이터 전송이 아님을 증명해야 한다. + 내용 찾아보자(contentType, processData)
		$.ajax({
			url: "saveImage",
			type: "POST",
			dataType: "JSON",
			contentType: false,
			processData: false,
			data: frm
			
		}).done(function(data){
			//에디터에 image 경로를 넣어준다.
			$("#content").summernote(
					"editor.insertImage", data.url);
			
		}).fail(function(err){
			console.log(err);
		});
	}
	
	function editData(){
		// 필요한 유효성 검사!
		//현재 body 태그의 폼들 중 1번째 안에 subject라는 파라미터 이름을 가지고 있는 값
		var subject = document.forms[0].subject.value;
		
		if(subject.trim().length < 1){
			//제목을 입력하지 않은 경우
			alert("제목을 입력하세요!");
			document.forms[0].subject.focus();
			return;
		}
		
		document.forms[0].submit();
	}
	
	<!--0730-->
	function goList() {
		document.forms[0].action = "list";
		document.forms[0].submit();
	}
</script>	
	
</body>
</html>












