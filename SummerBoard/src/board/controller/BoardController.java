package board.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.model.*;
import board.service.BoardService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller//컨트롤러 선언
@RequestMapping("/board")//매핑이 /board가 들어왔을때 이 컨트롤러를 사용하게 만듬.
public class BoardController {
	
	private ApplicationContext context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
	//context객체를 프레임워크에 올리기 위해 만듬(셀프로 넣는것)
	private BoardService boardService = (BoardService) context.getBean("boardService");
	//그리하여 service객체를 만들 수 있다.
	
	//페이징 작업을 위해 만든 변수들
	private int currentPage = 1;
	private int showArticleLimit = 10;
	private int showPageLimit = 10;
	private int startArticleNum = 0;
	private int endArticleNum = 0;
	private int totalNum = 0;
	
	//파일 업로드를 위해 만든 경로
	private String uploadPath = "C:\\Java\\App\\SummerBoard\\WebContent\\files\\";
	
	
	@RequestMapping("/list.do")//이 매핑이 들어왔을때 이 메서드 실행
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) {
		//request와 response를 매개변수로 받음
		
		//검색 종류와 내용을 위해 만든 변수
		String type = null;
		String keyword = null;
		
		if(request.getParameter("page")==null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
			//페이지에 아무것도 없으면 커런트페이지를 1로 설정해주고
			currentPage = 1;
		} else {
			//아닐경우 그에 맞는 페이지를 설정해줌.
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		if(request.getParameter("type") != null) {
			//타입이 널이 아니면
			type = request.getParameter("type").trim();
			//타입의 값을 설정해주고
		}
		
		if(request.getParameter("keyword") != null) {
			//키워드의 값이 널이 아니면
			keyword = request.getParameter("keyword").trim();
			//키워드값을 키워드에 저장
		}
		
		startArticleNum = (currentPage - 1) * showArticleLimit + 1;//시작 글 넘버 설정
		endArticleNum = startArticleNum + showArticleLimit - 1;//끝 글 넘버 설정
		
		List<BoardModel> boardList; //보드모델 보드리스트 객체 생성
		
		if(type != null && keyword != null) {//타입과 키워드가 널이 아닐때.
			boardList = boardService.searchArticle(type, keyword, startArticleNum, endArticleNum);
			//보드서비스의 searcharticle을 통해 그에 해당하는 값을 가져와서 보드리스트에 저장
			totalNum = boardService.getSearchTotalNum(type, keyword);
			//또한 검색한 게시글이 몇개인지에 대한 값을 가져와 totalnum에 저장
		} else {//타입과 키워드가 널일경우
			boardList = boardService.getBoardList(startArticleNum, endArticleNum);
			//리스트와
			totalNum = boardService.getTotalNum();
			//총 게시글 갯수를 가져옴
		}
		
		StringBuffer pageHtml = getPageHtml(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);
		//페이징 작업을 해준다.
		
		ModelAndView mav = new ModelAndView();//모델앤 뷰 객체를 생성하여
		mav.addObject("boardList", boardList);
		mav.addObject("pageHtml", pageHtml);
		mav.setViewName("/board/list");//값을 넣어주고
		
		return mav;//리턴해준다.
	}

	private StringBuffer getPageHtml(int currentPage, int totalNum, int showArticleLimit, int showPageLimit, String type, String keyword) {
		//페이징을 위한 메소드
		StringBuffer pageHtml = new StringBuffer();
		int startPage = 0;
		int lastPage = 0;
		
		startPage = ((currentPage - 1) / showPageLimit) * showPageLimit + 1;//시작페이지 설정
		lastPage = startPage + showPageLimit - 1;//마지막페이지 설정
		
		if(lastPage > totalNum / showArticleLimit) {//마지막페이지가 총게시글번호 나누기 보여주는 게시글설정보다 클때
			lastPage = (totalNum / showArticleLimit) + 1; //이렇게 설정해준다
		}
		
		if(type == null && keyword == null) {//검색값이 없고
			if(currentPage == 1) {//currentpage가 1일경우에 이걸실행해라
				pageHtml.append("<span>");
			} else {//currentpage가 1이 아닐경우에 실행
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage - 1) + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage; i<=lastPage; i++) {//lastpage 크기만큼 반복
				if(i == currentPage) {//i와 currentpage가 같다면, 그에 해당하는 번호에 <strong>효과를 준다.
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" + i + "\" class=\"page\">" + i + "</a>");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" + i + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
			}
			if(currentPage == lastPage) {//currentpage와 lastpage가 같을경우
				pageHtml.append(".</span>");
			} else {//아닐경우 다음버튼이 활성화될수있게해준다.
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage + 1) +"\"><다음></a></span");
			}
		} else {//검색값이 있을경우
			if(currentPage == 1) { //currentpage가 1일경우
				pageHtml.append("<span>");
			} else {//아닐경우
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage-1) + "&type=" + type + "&keyword=" + keyword + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage; i <= lastPage ; i++) {//lastpage만큼 반복
				if(i == currentPage) {//i와 currentpage가 같다면, 그에 해당하는 번호를 <strong> 효과를준다.
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" + i + "&type=" + type + "&keyword" + keyword + "\"><이전></a>&nbsp;&nbsp;");
					pageHtml.append("&nbsp;</strong>");
				} else {//아닐경우 
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" + i + "&type=" + type + "&keyword" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
			}
			
			if(currentPage == lastPage) {
				pageHtml.append("</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage+1) + "&type=" + type + "&keyword=" + keyword + "\"><다음></a></span>" );
			}
		}
		
		return pageHtml;
	}
	
	@RequestMapping("/view.do")//글 상세보기를 했을때 이 메서드를 동작시킴
	public ModelAndView boardView(HttpServletRequest request) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		//글번호를 가져온다.
		BoardModel board = boardService.getOneArticle(idx);
		//글번호 읽어오기
		boardService.updateHitcount(board.getHitcount()+1, idx);
		//조회수를 증가시킨다.
		
		List<BoardCommentModel> commentList = boardService.getCommentList(idx);
		//글번호에 해당하는 comment를 가져온다.
		
		ModelAndView mav = new ModelAndView();//모델엔뷰객체를 생성하여
		mav.addObject("board", board);
		mav.addObject("commentList", commentList);
		mav.setViewName("/board/view");//값을 넣고
		return mav;//리턴
	}

	@RequestMapping("/write.do")//글 쓰기를 했을때 이 메서드를 동작시킴
	public String boardWrite(@ModelAttribute("BoardModel") BoardModel boardModel) {
		return "/board/write";//글쓰기 폼으로 이동.
	}
	
	@RequestMapping(value="write.do", method=RequestMethod.POST)//post형식으로 왔을때 이 메서드를 실행
	public String boardWriteProc(@ModelAttribute("BoardModel") BoardModel boardModel, MultipartHttpServletRequest request) {
		
		MultipartFile file = request.getFile("file");//파일객체 생성
		String fileName = file.getOriginalFilename();//오리지널 파일이음을 filename에 저장
		File uploadFile = new File(uploadPath + fileName);//경로와 파일내임을 합쳐 uploadfile에 저장
		
		if(uploadFile.exists()) {//파일이 존재한다면
			fileName = new Date().getTime() + fileName;//시간+filename을 해주고
			uploadFile = new File(uploadPath + fileName);//경로를 저장하여 새로운파일을 uploadfile에 저장
		}
		
		try {
			file.transferTo(uploadFile);//업로드 파일 전송
		} catch(Exception e) {
			
		}
		
		boardModel.setFileName(fileName);//파일 이름을 db에 저장
		
		String content = boardModel.getContent().replaceAll("\r\n", "<br />");
		//게시글에 적힌 내용중에 \r\n의 내용을 <br/>로 바꿔준다.
		boardModel.setContent(content);
		//그리고 값을 자바빈에 저장해준다.
		boardService.writeArticle(boardModel);
		//그리고 쿼리문에 넣어주고
		
		return "redirect:list.do";//리스트로 돌아가게해준다.
	}
	
	@RequestMapping("/commentWrite.do")//댓글 작성이 들어왔을경우
	public ModelAndView commentWriteProc(@ModelAttribute("CommentModel") BoardCommentModel commentModel) {
		
		String content = commentModel.getContent().replaceAll("\r\n", "<br />");
		//댓글에 적힌 내용중 \r\n을 <br/>로 바꿔줌
		commentModel.setContent(content);
		//자바빈에 값 저장
		
		boardService.writeComment(commentModel);
		//쿼리문에 넣어준다.
		
		ModelAndView mav = new ModelAndView();
		//모델앤뷰 객체 생성
		
		mav.addObject("idx", commentModel.getLinkedArticleNum());//값을 넣어주고
		mav.setViewName("redirect:view.do");
		
		return mav;//리턴
	}
	
	@RequestMapping("/modify.do")//글수정
	public ModelAndView boardModify(HttpServletRequest request, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		//세션으로부터 userid를 가져옴
		int idx = Integer.parseInt(request.getParameter("idx"));
		//글번호도 가져온다.
		
		BoardModel board = boardService.getOneArticle(idx);
		//쿼리문 실행 저장 값을 보드모델객체 board에 저장
		
		String content = board.getContent().replaceAll("<br />", "\r\n");
		//글 내용에 적힌 <br/>을 \r\n으로 변경
		
		board.setContent(content);//board객체에 저장
		
		ModelAndView mav = new ModelAndView();
		
		if(!userId.equals(board.getWriterId())) {//세션 유저 id와 글쓴이가 같지 않을경우
			mav.addObject("errCode", "1");//에러코드 발생
			mav.addObject("idx", idx);//글번호
			mav.setViewName("redirect:view.do");//상세보기로 다시 보내고
		} else {//아닐경우
			mav.addObject("board", board);//board객체를 넣어주고
			mav.setViewName("/board/modify");//글수정 페이지로 넘겨주게한다.
		}
		
		return mav;
	}
	
	@RequestMapping(value = "modify.do", method=RequestMethod.POST)//글수정이 post로 들어왔을경우
	public ModelAndView boardModifyProc(@ModelAttribute("BoardModel") BoardModel boardModel, MultipartHttpServletRequest request) {
		
		String orgFileName = request.getParameter("orgFile");
		//수정된 파일 이름을 저장
		MultipartFile newFile = request.getFile("newFile");
		//새로운 파일을 만듬
		String newFileName = newFile.getOriginalFilename();
		//새로운 파일에 수정된 파일이름을 저장
		
		boardModel.setFileName(orgFileName);//boardmodel에 저장시킨다.
		
		if(newFile != null && !newFileName.equals("")) {//파일이 널이아닐때
			if(orgFileName != null || !orgFileName.equals("")) {//orgfile이 널이 아닐때
				
				File removeFile = new File(uploadPath + orgFileName);//원래파일을 가져와
				removeFile.delete();//삭제
			}
			
			File newUploadFile = new File(uploadPath + newFileName);//그러고 새로운 파일을 저장
			
			try {
				newFile.transferTo(newUploadFile);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			boardModel.setFileName(newFileName);
		}
		
		String content = boardModel.getContent().replaceAll("\r\n", "<br />");
		boardModel.setContent(content);
		
		boardService.modifyArticle(boardModel);//서비스 실행
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("idx", boardModel.getIdx());
		mav.setViewName("redirect:/board/view.do");
		return mav;//상세보기로 리턴
	}
	
	@RequestMapping("/delete.do")//글삭제시 이 메소드 실행
	public ModelAndView boardDelete(HttpServletRequest request, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		int idx = Integer.parseInt(request.getParameter("idx"));
		
		BoardModel board = boardService.getOneArticle(idx);
		
		ModelAndView mav = new ModelAndView();
		
		if(!userId.equals(board.getWriterId())) {//글쓴이가 안맞을때
			mav.addObject("errCode", "1");
			mav.addObject("idx", idx);
			mav.setViewName("redirect:view.do");//상세보기로 리턴
		} else {//아닐경우
			List<BoardCommentModel> commentList = boardService.getCommentList(idx);
			
			if(commentList.size() > 0) {//댓글이 남겨져있을경우
				mav.addObject("errCode", "2");
				mav.addObject("idx", idx);
				mav.setViewName("redirect:view.do");//에러를띄우고 뷰로 리턴
			} else {
				if(board.getFileName() != null) {//파일이 있을경우
					File removeFile = new File(uploadPath + board.getFileName());
					removeFile.delete();//파일을 삭제한후
				}
				
				boardService.deleteArticle(idx);//글을 삭제시켜주고
				
				mav.setViewName("redirect:list.do");//리스트로 리턴
			}
		}
		
		return mav;
	}
	
	@RequestMapping("/commentDelete.do")//댓글삭제시 이 메소드가 동작
	public ModelAndView commendDelete(HttpServletRequest request, HttpSession session) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		int linkedArticleNum = Integer.parseInt(request.getParameter("linkedArticleNum"));
		
		String userId = (String)session.getAttribute("userId");
		BoardCommentModel comment = boardService.getOneComment(idx);
		
		ModelAndView mav = new ModelAndView();
		
		if(!userId.equals(comment.getWriterId())) {//댓글작성자가 맞지않을경우
			mav.addObject("errCode", "1");//에러를 저장시키고
		} else {
			boardService.deleteComment(idx);//아닐경우 삭제시킨다.
		}
		
		mav.addObject("idx", linkedArticleNum);
		mav.setViewName("redirect:view.do");
		
		return mav;//리턴
	}
	
	@RequestMapping("/recommend.do")//추천했을경우.
	public ModelAndView updateRecommendcount(HttpServletRequest request, HttpSession session) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		String userId = (String)session.getAttribute("userId");
		BoardModel board = boardService.getOneArticle(idx);
		
		ModelAndView mav = new ModelAndView();
		
		if(userId.equals(board.getWriterId())) {
			mav.addObject("errCode", "1");
		} else {
			boardService.updateRecommendCount(board.getRecommendcount()+1, idx);
		}
		
		mav.addObject("idx", idx);
		mav.setViewName("redirect:/board/view.do");
		
		return mav;
	}
}
