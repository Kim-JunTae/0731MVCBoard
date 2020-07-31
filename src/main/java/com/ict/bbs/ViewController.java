package com.ict.bbs;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

@Controller
public class ViewController {
	//String b_idx;
	
	@Autowired
	private BbsDAO bbsDao;
	
	@Autowired
	private HttpSession session;
	
	/*
	 * @RequestMapping(value="/view", method=RequestMethod.GET) public ModelAndView
	 * view(String b_idx) { ModelAndView mv = new ModelAndView();
	 * 
	 * if(b_idx == null) { this.b_idx = "1"; }
	 * 
	 * this.b_idx = b_idx;
	 * 
	 * BbsVO board = bbsDao.view(this.b_idx);
	 * 
	 * mv.addObject("board", board); mv.setViewName("view");
	 * 
	 * return mv; }//end view()
	 */
	
	@RequestMapping(value="/view")
	public ModelAndView view(String b_idx, String nowPage, String bname) {
		//한번이라도 읽기를 한 게시물들은 세션에 read_list라는 이름으로 저장된 ArrayList에 있도록 저장해둔다.
		Object obj = session.getAttribute("read_list"); //ArrayList<BbsVO>
		
		List<BbsVO> r_list = null;
		//r_list를 채워주자
		if(obj != null)
			r_list = (List<BbsVO>) obj;
		else
			r_list = new ArrayList<BbsVO>();
			
		//BbsDAO의 getBbs메서드를 호출하기 위해서 Map구조 생성!
		Map<String, String> map = new Hashtable<String, String>();
		
		map.put("b_idx", b_idx);
		map.put("bname", bname);
		
		BbsVO vo = bbsDao.getBbs(map);
		
		//한번이라도 읽었던 게시물인지? 아닌지? 판단하자!
		boolean chk = false;
		//읽은 게시물들이 모두 모여있는 r_list에서 
		//지금 가져온 vo의 기본키(b_idx)와 같은 것이 있는지를 알아내기 위한 반복문! 
		
		for(BbsVO bvo : r_list) {
			if(bvo.getB_idx().equals(b_idx)) {
				//같은 게시물을 찾은 경우(읽었던 게시물)
				chk = true;
				break; //가장 가까운 반복문 탈출!
			}
		}
		//반복문을 break문에 의해 탈출한 것인지(읽었던 게시물)? 아닌지(안읽었던 게시물)? 판단!!
		//(chk의 값이 false일때만 조회수 증가)
		if(!chk) { //chk == false
			//조회수 증가!!
			bbsDao.hit(b_idx);
			
			//vo를 미리 받아와서 vo는 여전히 증가되지 않은 조회수를 가진다.
			String h = vo.getHit();
			int hit = Integer.parseInt(h);
			++hit;
			vo.setHit(String.valueOf(hit));
			
			//r_list에 vo를 저장!!
			r_list.add(vo);
			
			//세션에 r_list 저장
			session.setAttribute("read_list", r_list);
		}
		
		//view.jsp로 이동할 준비
		ModelAndView mv = new ModelAndView();
		
		if(vo != null)
			mv.addObject("vo", vo);
		
		mv.addObject("nowPage", nowPage);
		mv.setViewName("view");
		return mv;
	}
	
	
}
