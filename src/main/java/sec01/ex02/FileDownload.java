package sec01.ex02;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/download.do")
public class FileDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public FileDownload() {
        super();
      
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String file_repo = "C:\\File_repo";
		String fileName = (String) request.getParameter("fileName");
		
		//file full path 파일경로
		String downFile = file_repo + System.getProperty("file.separator") + fileName;
		System.out.println("폴더 구분자 1 : "+ System.getProperty("file.separator"));
		System.out.println("폴더 구분자 2 : "+ File.separator);
		
		//지정한 파일 그 자체
		File f = new File(downFile);
		
		//파일을 읽을 흐름을 열어서 준비
		//java가 해당 파일을 사용 중
		FileInputStream in = new FileInputStream( f );
		
		//브라우저가 cache를 사용하지 않도록
		response.setHeader("Cache-Control", "no-cache");
		
		//전달받은 내용을 파일로 인식하도록
		response.addHeader("Content-disposition", "attachment; fileName="+fileName);

		//파일을 내보낼 수 있는 흐름을 열어서 준비
		OutputStream out = response.getOutputStream();//OutputStream 는 추상클래스// 추상클래스, 인터페이스는 new 할수 없음
		
		byte[] buf = new byte[1024 * 8];//1024=1kB
		while(true) {
			//배열의 크기만큼 읽기
			int count = in.read(buf);
			System.out.println("읽은 크기 : count : "+ count);
			
			//읽은 내용이 더이상 없으면 -1을 반환
			if(count == -1) {
				break;
			}
			
			//응답의 흐름에 읽은 만큼 보내기
			out.write(buf, 0, count);
		}
		
		in.close();
		out.close();
	}
}
