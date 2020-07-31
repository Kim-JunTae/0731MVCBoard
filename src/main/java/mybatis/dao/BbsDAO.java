package mybatis.dao;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import mybatis.vo.BbsVO;
import mybatis.vo.CommVO;

public class BbsDAO {
	@Autowired
	SqlSessionTemplate template;
	
	//전체 게시물 수를 반환하는 기능
	public int getTotalCount(String bname) {
		return template.selectOne("bbs.totalCount", bname);
	}//end getTotalCount()
	
	//원하는 페이지의 게시물 목록 기능 - ListController에서 호출하여 list.jsp에서 표현
	public BbsVO[] getList(String begin, String end, String bname) {
		BbsVO[] ar = null;
		
		//맴퍼(bbs.list)를 호출하기 위해 
		Map<String, String> map = new Hashtable<String, String>();
		
		//옛날 Java 형식 -> 5.0부터 AutoBoxing, UnBoxing
		//map.put("begin",new Integer(begin));
		
		map.put("begin", begin);
		map.put("end", end);
		map.put("bname", bname);
		
		//리스트 넣기
		List<BbsVO> list = template.selectList("bbs.list", map);
		
		if(list != null && list.size() > 0) {
			ar = new BbsVO[list.size()];
			list.toArray(ar);
		}
		
		return ar;
	}//end getList()
	
	//게시물 추가 기능 - WriteController에서 호출
	public int insertBoard(BbsVO vo) {
		//boolean value = false;
		
		int cnt = template.insert("bbs.insertBoard", vo);
		
		/*if(cnt > 0) {
			value = true;
		}*/
		return cnt;
	}//end insertBoard()
	
	//게시글 보기 기능 : 이거 나혼자 한듯?
	public BbsVO view(String b_idx) {
		BbsVO board = null;
		
		board = template.selectOne("bbs.view", b_idx);
		
		return board;
	}//end view()
	
	//보기 기능을 위한 게시물 검색 - ViewController에서 호출
	public BbsVO getBbs(Map<String, String> map) {
		//조회수 증가
		//template.update("bbs.upHit", map);
		return template.selectOne("bbs.getBbs", map);
	}//end getBbs()
	
	//인자로 b_idx를 받아 조회수 증가하는 기능 : 오강사님꺼~
	public void hit(String b_idx) {
		template.update("bbs.hit", b_idx);	
	}//end hit()
	
	//댓글 저장
	public boolean addAns(CommVO cvo) {
		boolean value = false;
		int cnt = template.insert("bbs.add_ans", cvo);
		if(cnt < 0)
			value = true;
		
		return value;
	}//end addAns()
	
	
	//게시글 수정화면으로 데이터를 넘겨주는 기능
	public BbsVO editBbs(Map<String, String> map) {
		return template.selectOne("bbs.editBbs", map);
	}
	
	//게시물을 수정하는 기능
	public void editBbs(BbsVO vo) {
		template.update("bbs.updateBbs", vo);
	}
	
}
