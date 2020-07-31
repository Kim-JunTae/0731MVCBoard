package com.ict.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.Paging;

@Controller
public class ListController {
	
	@Autowired
	private BbsDAO bbsDao;
	
	//페이징 기법에 필요한 여러변수들...
	int nowPage;
	public final int BLOCK_LIST = 7;	// 한 페이지 당 보여줄 게시물 수
	public final int BLOCK_PAGE = 3;	// 한 페이지블럭 당 보여줄 페이지 수
	
	int rowTotal; 		//전체 게시물 수
	String pageCode;	//페이징 처리된 HTML코드가 저장될 곳! ★
	
	//나중에 게시물에 대한 검색을 한다면 - 동적쿼리
	String searchType;	// 0이면  제목검색, 1이면 글쓴이 검색, 2이면 내용검색, 3이면 날짜검색..
	String searchValue;	// 검색할 단어!!!
	
	//list 호출 시 목록을 가져오기
	@RequestMapping(value="/list")
	public ModelAndView list(String nowPage, String bname) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		//현재 페이지(nowPage) 값이 없으면 1로 초기화
		if(nowPage == null) {
			this.nowPage = 1;
		}else{	//사용자가 페이지 번호를 누르거나 페이지 블럭을 변경한 경우이다.
			this.nowPage = Integer.parseInt(nowPage);
		}
		//System.out.println(nowPage);
		//System.out.println(this.nowPage);
		
		//게시판을 구별하자!
		if(bname == null) {
			bname = "BBS"; // 일반 게시판
		}
		
		//전체 게시물의 수를 구한다.
		rowTotal = bbsDao.getTotalCount(bname);
		//System.out.println("rowTotal : " + rowTotal);
		//★★★★★ 페이징 처리를 해주는 객체 생성!!!
		Paging page = new Paging(this.nowPage, rowTotal, BLOCK_LIST, BLOCK_PAGE);
		
		pageCode = page.getSb().toString();	//JSP에서 표현할 페이징 코드
		
		int begin = page.getBegin();
		int end = page.getEnd();
		
		//System.out.println(begin);
		//System.out.println(end);
		
		BbsVO[] ar = bbsDao.getList(
				String.valueOf(begin), 
				String.valueOf(end),
				bname);
		
		mv.addObject("list", ar);
		mv.addObject("pageCode", pageCode);
		mv.addObject("nowPage", this.nowPage);
		mv.addObject("blockList", BLOCK_LIST);
		mv.addObject("rowTotal", rowTotal);
		
		mv.setViewName("list");		//	views/list.jsp를 의미
		
		return mv;
	}//end list()
	
	
}
