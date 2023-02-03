package sec01.ex01;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/upload.do")
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String encoding = "utf-8";
		
		try {
			//File : 파일 또는 디렉토리(폴더)를 관리하는 class
			File currentDirPath = new File("C:\\File_repo");
			
			//세팅
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(currentDirPath);//어디에다 저장할지
			factory.setSizeThreshold(1024 * 1024);//처리하는 메모리 용량
			
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(1024*1024*100);//1024*1024=1MB
			
			//request를 분석하라
			List items = upload.parseRequest(request);//
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);

				if (fileItem.isFormField()) {//isFormField() ; form filed 요소인지 판별
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					
				} else {
					//파일영역
					System.out.println("param:" + fileItem.getFieldName());
					System.out.println("file name:" + fileItem.getName());
					System.out.println("file size:" + fileItem.getSize() + "Bytes");

					if (fileItem.getSize() > 0) {
						//아이디어1 : 파일명 추출하는
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}
						String fileName = fileItem.getName().substring(idx + 1);
						
						//아이디어2 : 파일명 중복 방지
						long timestamp = System.currentTimeMillis();
						fileName = timestamp +"_" +fileName;
						
						
						File uploadFile = new File(currentDirPath + "\\" + fileName); 
						// "\\"=> File.separator; 어떤 os에도 맞는 구분자로 출력, System.getProperty("file.separator")이것도 됨
						fileItem.write(uploadFile);
					} // end if
				} // end if
			} // end for
			
			//위쪽 for문 안에서 DTO(VO)를 완성하고
			//DB에 저장할수 있겠다...
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
