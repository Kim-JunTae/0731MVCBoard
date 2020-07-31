package com.ict.bbs;

import java.util.Hashtable;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

@Controller
public class EditController {
	//STS에서는 resources까지를 인식하고 있다.
	//private String imgPath ="/resources/editor_img";
	//private String uploadPath ="/resources/upload";
	
	@Autowired
	private BbsDAO bbsDao;
	
	
	//글을 수정하러 가는 길~
	@RequestMapping(value = "/goEdit", method = RequestMethod.POST)
	public ModelAndView edit(String b_idx, String pwd, String nowPage) {
		ModelAndView mv = new ModelAndView();
		
		//게시글 상세보기에서 b_idx와 pwd 받아온 상태
		Map<String, String> map = new Hashtable<String, String>();
		map.put("b_idx", b_idx);
		map.put("pwd", pwd);
		
		BbsVO vo = bbsDao.editBbs(map);
		
		if(vo != null)
			mv.addObject("vo", vo);
		
		mv.addObject("nowPage", nowPage);
		mv.setViewName("edit");
		return mv;
	}//end edit()
	
	//글을 수정완료 하고 update하기
	@RequestMapping(value="/edit", method = RequestMethod.POST)
	public ModelAndView edit(BbsVO vo) {
		ModelAndView mv = new ModelAndView();
		//파일을 수정해서 다시 올리는 건 나중에
		//bbsDao에서 update SQL문장 실행
		bbsDao.editBbs(vo);
		
		mv.addObject("nowPage", vo.getNowPage());
		mv.setViewName("redirect:/list");
		return mv;
	}
	
	//글을 삭제(Status를 1로 변경)하러 가보자
	@RequestMapping(value = "/goDelete", method = RequestMethod.POST)
	public ModelAndView delete(BbsVO vo) {
		ModelAndView mv = new ModelAndView();
		
		//bbsDao.deleteBbs(vo);
		
		//mv.setViewName("");
		
		return mv;
	}
}
