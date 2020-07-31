package spring.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileDownload
 */
public class FileDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /*
     *  Spring STS에서는 서블릿을 web.xml에 등록되어야 한다.
     *  현 이클립스 버전에서는 자동화 되어있다. 가서 확인만 하자!
     */
    public FileDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//요청한 곳으로부터 파일명이 담긴 파리미터 값을 받는다.
		String fname = request.getParameter("f_name");
		
		//파일들이 저장된 곳(upload)을 절대경로로 만든다.
		String path = getServletContext().getRealPath("/resources/upload");
		
		//위의 절대경로(path)와 파일명(fname)을 연결시킨다.
		//String fullPath = path+"/"+fname;
		String fullPath = path + System.getProperty("file.separator") + fname;
		
		//절대경로와 파일명이 합쳐진 전체경로가 준비되었으니
		//이것을 가지고 File객체를 생성한다.
		File f = new File(fullPath);
		
		//파일이 존재할 때만 수행!
		if(f.exists()) {
			//System.out.println(fullPath);
			byte[] buf = new byte[2048];//바구니
			
			//응답할(전송할) 데이터가 스트림 처리될 때 문자셋을 지정해야함!
			response.setContentType(
					"application/octet-stream;charset=8859_1");
			
			//다운로드할 대화상자 처리
			response.setHeader("Content-Disposition", 
					           "attachment;filename="
					           + new String(fname.getBytes("utf-8"), "8859_1"));
			
			//전송타입이 이진데이터(바이너리 데이터, binary)로 지정!
			response.setHeader("Content-Transfer-Encoding", "binary");
			
			//요청한 것이 파일인지 다시 확인
			if(f.isFile()) {
				//요청한 곳으로 스트림에서 얻은 자원을 보낸다. : 파일에 빨대 꼳음
				BufferedInputStream bis = 
						new BufferedInputStream(new FileInputStream(f));
				
				//요청한 곳으로 보내기 위해(응답하기 위해) 스트림을 응답객체로 부터 얻어낸다.
				BufferedOutputStream bos =
						new BufferedOutputStream(response.getOutputStream());
				
				int size = -1;
				try {
					//읽어 오자마자 보내기를 수행한다.
					while((size=bis.read(buf)) != -1) {
						//보내기
						bos.write(buf, 0, size);
						//스트림 비우기 : 스트림을 바깥으로 보내줘
						bos.flush();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}finally{
					//스트림 자원 닫음
					if(bos != null)
						bos.close();
					if(bis != null)
						bis.close();
				}
			}//end if
		}//end if
	}//end doGet()

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
