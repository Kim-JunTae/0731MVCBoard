package com.ict.bbs;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileUploadUtil;

@Controller
public class WriteController {
	//STS에서는 resources까지를 인식하고 있다.
	private String imgPath ="/resources/editor_img";
	private String uploadPath ="/resources/upload";
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private BbsDAO bbsDao;
	
	/*
	 * @RequestMapping(value = "/write", method = RequestMethod.GET) public String
	 * writerForm() { return "write"; }
	 */
	
	//0730 추가
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public ModelAndView writerForm(String nowPage, String bname) {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("nowPage", nowPage);
		mv.addObject("bname", bname);
		mv.setViewName("write");
		return mv;
	}
	
	//쓰기기능 추가
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public ModelAndView write(BbsVO vo) throws Exception{
		ModelAndView mv = new ModelAndView();
		//첨부파일 확인 후 저장(upload)
		String filename = null;
		MultipartFile upload = vo.getFile();
		
		//첨부파일 확인
		if(upload != null && upload.getSize() > 0) {
			//절대 경로 얻기
			String path = application.getRealPath(uploadPath);
			//실제파일명 얻기
			vo.setOri_name(upload.getOriginalFilename()); //파일 원래 이름 vo에 저장
			//동일한 파일명이 있다면 변경한다.
			filename = FileUploadUtil.checkSameFileName(vo.getOri_name(), path);
			//파일 올리기
			upload.transferTo(new File(path, filename));
			//변경된 파일명 얻기
			vo.setFile_name(filename);	//파일 변경된 이름 vo에 저장
		}else{
			vo.setOri_name("nofile");
			vo.setFile_name("nofile");
		}
		
		//ip 지정
		vo.setIp(request.getRemoteAddr());
		
		//bname 지정
		if(vo.getBname() == null) {
			vo.setBname("BBS");
		}
			
		//b_idx, write_date, hit, status는 mapper에서 지정
		//dao를 통해서 bbs_t에 insert작업!!
		//boolean chk = bbsDao.insertBoard(vo);
		int chk = bbsDao.insertBoard(vo);
		//insert 되었는지 확인 값
		//mv.addObject("chk", chk); X 이렇게 보내는게 아니라 get방식? 쿼리방식으로 보낸다!
		//보내서 글이 저장되었는지 판단하고 액션을 할 수 있다.
		
		
		//ListController 다시 호출
		//mv.setViewName("redirect:/list?chk="+chk); : ListController에서 받아야 된다.
		mv.setViewName("redirect:/list");
		
		return mv;
	}//end write()
	
	
	//클라이언트에서 서버로 필요한 데이터를 전송하기 위해서 JSON이라는 데이터를 요청본문에 담아서 서버로 보내면,
	//서버에서는 @ResponseBody 어노테이션을 사용하여 HTTP 요청 본문에 담긴 값들을,
	//자바객체로 변환 시켜서 객체에 저장합니다.
	//서버에서 클라이언트로 응답데이터를 전송하기 위해서
	//@ResponseBody를 사용하여 자바 객체를 HTTP 응답 본문의 객체로 변환하여,
	//클라이언트로 전송시키는 역할을 합니다.
	@RequestMapping(value = "/saveImage", method = RequestMethod.POST,
			        produces = "application/JSON;charset=utf-8")
	@ResponseBody
	public Map<String, String> saveImage(BbsVO vo) throws Exception{
		//write.jsp에서 비동기식 통신을 이용하여 이미지를 서버에 저장하기 위해 호출되는 곳!
		//그리고 끝나기 전에 반드시 저장된 이미지 경로를 보내야 한다.
		Map<String, String> map = new Hashtable<String, String>();
		
		//파일을 서버에 저장
		MultipartFile upload = vo.getFile();
		//전역변수 설정
		String f_name = null;
		
		//파일의 사이즈나 파일의 본이름이 0보다 클 때 : 파일이 존재한다
		if(upload.getSize() > 0 && upload != null) {
			//파일을 저장할 정확한 위치 지정
			String path = application.getRealPath(imgPath);
			
			//파일 이름 얻기
			f_name = upload.getOriginalFilename();
			
			//이미지 저장 전 같은 이름의 파일 존재 여부 확인하여 중복되지 않는 파일명 확정
			f_name = FileUploadUtil.checkSameFileName(f_name, path);
			
			//서버에 파일 저장(파일올리기)
			upload.transferTo(new File(path, f_name));
			
			//write.jsp로 반환할 값 (url)
			map.put("url", request.getContextPath() + imgPath + "/" + f_name);
		}
		
		return map;
	}//end saveImage()
}
