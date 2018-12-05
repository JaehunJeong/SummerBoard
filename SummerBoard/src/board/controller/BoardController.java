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

@Controller//��Ʈ�ѷ� ����
@RequestMapping("/board")//������ /board�� �������� �� ��Ʈ�ѷ��� ����ϰ� ����.
public class BoardController {
	
	private ApplicationContext context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
	//context��ü�� �����ӿ�ũ�� �ø��� ���� ����(������ �ִ°�)
	private BoardService boardService = (BoardService) context.getBean("boardService");
	//�׸��Ͽ� service��ü�� ���� �� �ִ�.
	
	//����¡ �۾��� ���� ���� ������
	private int currentPage = 1;
	private int showArticleLimit = 10;
	private int showPageLimit = 10;
	private int startArticleNum = 0;
	private int endArticleNum = 0;
	private int totalNum = 0;
	
	//���� ���ε带 ���� ���� ���
	private String uploadPath = "C:\\Java\\App\\SummerBoard\\WebContent\\files\\";
	
	
	@RequestMapping("/list.do")//�� ������ �������� �� �޼��� ����
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) {
		//request�� response�� �Ű������� ����
		
		//�˻� ������ ������ ���� ���� ����
		String type = null;
		String keyword = null;
		
		if(request.getParameter("page")==null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
			//�������� �ƹ��͵� ������ Ŀ��Ʈ�������� 1�� �������ְ�
			currentPage = 1;
		} else {
			//�ƴҰ�� �׿� �´� �������� ��������.
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		if(request.getParameter("type") != null) {
			//Ÿ���� ���� �ƴϸ�
			type = request.getParameter("type").trim();
			//Ÿ���� ���� �������ְ�
		}
		
		if(request.getParameter("keyword") != null) {
			//Ű������ ���� ���� �ƴϸ�
			keyword = request.getParameter("keyword").trim();
			//Ű���尪�� Ű���忡 ����
		}
		
		startArticleNum = (currentPage - 1) * showArticleLimit + 1;//���� �� �ѹ� ����
		endArticleNum = startArticleNum + showArticleLimit - 1;//�� �� �ѹ� ����
		
		List<BoardModel> boardList; //����� ���帮��Ʈ ��ü ����
		
		if(type != null && keyword != null) {//Ÿ�԰� Ű���尡 ���� �ƴҶ�.
			boardList = boardService.searchArticle(type, keyword, startArticleNum, endArticleNum);
			//���弭���� searcharticle�� ���� �׿� �ش��ϴ� ���� �����ͼ� ���帮��Ʈ�� ����
			totalNum = boardService.getSearchTotalNum(type, keyword);
			//���� �˻��� �Խñ��� ������� ���� ���� ������ totalnum�� ����
		} else {//Ÿ�԰� Ű���尡 ���ϰ��
			boardList = boardService.getBoardList(startArticleNum, endArticleNum);
			//����Ʈ��
			totalNum = boardService.getTotalNum();
			//�� �Խñ� ������ ������
		}
		
		StringBuffer pageHtml = getPageHtml(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);
		//����¡ �۾��� ���ش�.
		
		ModelAndView mav = new ModelAndView();//�𵨾� �� ��ü�� �����Ͽ�
		mav.addObject("boardList", boardList);
		mav.addObject("pageHtml", pageHtml);
		mav.setViewName("/board/list");//���� �־��ְ�
		
		return mav;//�������ش�.
	}

	private StringBuffer getPageHtml(int currentPage, int totalNum, int showArticleLimit, int showPageLimit, String type, String keyword) {
		//����¡�� ���� �޼ҵ�
		StringBuffer pageHtml = new StringBuffer();
		int startPage = 0;
		int lastPage = 0;
		
		startPage = ((currentPage - 1) / showPageLimit) * showPageLimit + 1;//���������� ����
		lastPage = startPage + showPageLimit - 1;//������������ ����
		
		if(lastPage > totalNum / showArticleLimit) {//�������������� �ѰԽñ۹�ȣ ������ �����ִ� �Խñۼ������� Ŭ��
			lastPage = (totalNum / showArticleLimit) + 1; //�̷��� �������ش�
		}
		
		if(type == null && keyword == null) {//�˻����� ����
			if(currentPage == 1) {//currentpage�� 1�ϰ�쿡 �̰ɽ����ض�
				pageHtml.append("<span>");
			} else {//currentpage�� 1�� �ƴҰ�쿡 ����
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage - 1) + "\"><����></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage; i<=lastPage; i++) {//lastpage ũ�⸸ŭ �ݺ�
				if(i == currentPage) {//i�� currentpage�� ���ٸ�, �׿� �ش��ϴ� ��ȣ�� <strong>ȿ���� �ش�.
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" + i + "\" class=\"page\">" + i + "</a>");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" + i + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
			}
			if(currentPage == lastPage) {//currentpage�� lastpage�� �������
				pageHtml.append(".</span>");
			} else {//�ƴҰ�� ������ư�� Ȱ��ȭ�ɼ��ְ����ش�.
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage + 1) +"\"><����></a></span");
			}
		} else {//�˻����� �������
			if(currentPage == 1) { //currentpage�� 1�ϰ��
				pageHtml.append("<span>");
			} else {//�ƴҰ��
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage-1) + "&type=" + type + "&keyword=" + keyword + "\"><����></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage; i <= lastPage ; i++) {//lastpage��ŭ �ݺ�
				if(i == currentPage) {//i�� currentpage�� ���ٸ�, �׿� �ش��ϴ� ��ȣ�� <strong> ȿ�����ش�.
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" + i + "&type=" + type + "&keyword" + keyword + "\"><����></a>&nbsp;&nbsp;");
					pageHtml.append("&nbsp;</strong>");
				} else {//�ƴҰ�� 
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" + i + "&type=" + type + "&keyword" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
			}
			
			if(currentPage == lastPage) {
				pageHtml.append("</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage+1) + "&type=" + type + "&keyword=" + keyword + "\"><����></a></span>" );
			}
		}
		
		return pageHtml;
	}
	
	@RequestMapping("/view.do")//�� �󼼺��⸦ ������ �� �޼��带 ���۽�Ŵ
	public ModelAndView boardView(HttpServletRequest request) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		//�۹�ȣ�� �����´�.
		BoardModel board = boardService.getOneArticle(idx);
		//�۹�ȣ �о����
		boardService.updateHitcount(board.getHitcount()+1, idx);
		//��ȸ���� ������Ų��.
		
		List<BoardCommentModel> commentList = boardService.getCommentList(idx);
		//�۹�ȣ�� �ش��ϴ� comment�� �����´�.
		
		ModelAndView mav = new ModelAndView();//�𵨿��䰴ü�� �����Ͽ�
		mav.addObject("board", board);
		mav.addObject("commentList", commentList);
		mav.setViewName("/board/view");//���� �ְ�
		return mav;//����
	}

	@RequestMapping("/write.do")//�� ���⸦ ������ �� �޼��带 ���۽�Ŵ
	public String boardWrite(@ModelAttribute("BoardModel") BoardModel boardModel) {
		return "/board/write";//�۾��� ������ �̵�.
	}
	
	@RequestMapping(value="write.do", method=RequestMethod.POST)//post�������� ������ �� �޼��带 ����
	public String boardWriteProc(@ModelAttribute("BoardModel") BoardModel boardModel, MultipartHttpServletRequest request) {
		
		MultipartFile file = request.getFile("file");//���ϰ�ü ����
		String fileName = file.getOriginalFilename();//�������� ���������� filename�� ����
		File uploadFile = new File(uploadPath + fileName);//��ο� ���ϳ����� ���� uploadfile�� ����
		
		if(uploadFile.exists()) {//������ �����Ѵٸ�
			fileName = new Date().getTime() + fileName;//�ð�+filename�� ���ְ�
			uploadFile = new File(uploadPath + fileName);//��θ� �����Ͽ� ���ο������� uploadfile�� ����
		}
		
		try {
			file.transferTo(uploadFile);//���ε� ���� ����
		} catch(Exception e) {
			
		}
		
		boardModel.setFileName(fileName);//���� �̸��� db�� ����
		
		String content = boardModel.getContent().replaceAll("\r\n", "<br />");
		//�Խñۿ� ���� �����߿� \r\n�� ������ <br/>�� �ٲ��ش�.
		boardModel.setContent(content);
		//�׸��� ���� �ڹٺ� �������ش�.
		boardService.writeArticle(boardModel);
		//�׸��� �������� �־��ְ�
		
		return "redirect:list.do";//����Ʈ�� ���ư������ش�.
	}
	
	@RequestMapping("/commentWrite.do")//��� �ۼ��� ���������
	public ModelAndView commentWriteProc(@ModelAttribute("CommentModel") BoardCommentModel commentModel) {
		
		String content = commentModel.getContent().replaceAll("\r\n", "<br />");
		//��ۿ� ���� ������ \r\n�� <br/>�� �ٲ���
		commentModel.setContent(content);
		//�ڹٺ� �� ����
		
		boardService.writeComment(commentModel);
		//�������� �־��ش�.
		
		ModelAndView mav = new ModelAndView();
		//�𵨾غ� ��ü ����
		
		mav.addObject("idx", commentModel.getLinkedArticleNum());//���� �־��ְ�
		mav.setViewName("redirect:view.do");
		
		return mav;//����
	}
	
	@RequestMapping("/modify.do")//�ۼ���
	public ModelAndView boardModify(HttpServletRequest request, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		//�������κ��� userid�� ������
		int idx = Integer.parseInt(request.getParameter("idx"));
		//�۹�ȣ�� �����´�.
		
		BoardModel board = boardService.getOneArticle(idx);
		//������ ���� ���� ���� ����𵨰�ü board�� ����
		
		String content = board.getContent().replaceAll("<br />", "\r\n");
		//�� ���뿡 ���� <br/>�� \r\n���� ����
		
		board.setContent(content);//board��ü�� ����
		
		ModelAndView mav = new ModelAndView();
		
		if(!userId.equals(board.getWriterId())) {//���� ���� id�� �۾��̰� ���� �������
			mav.addObject("errCode", "1");//�����ڵ� �߻�
			mav.addObject("idx", idx);//�۹�ȣ
			mav.setViewName("redirect:view.do");//�󼼺���� �ٽ� ������
		} else {//�ƴҰ��
			mav.addObject("board", board);//board��ü�� �־��ְ�
			mav.setViewName("/board/modify");//�ۼ��� �������� �Ѱ��ְ��Ѵ�.
		}
		
		return mav;
	}
	
	@RequestMapping(value = "modify.do", method=RequestMethod.POST)//�ۼ����� post�� ���������
	public ModelAndView boardModifyProc(@ModelAttribute("BoardModel") BoardModel boardModel, MultipartHttpServletRequest request) {
		
		String orgFileName = request.getParameter("orgFile");
		//������ ���� �̸��� ����
		MultipartFile newFile = request.getFile("newFile");
		//���ο� ������ ����
		String newFileName = newFile.getOriginalFilename();
		//���ο� ���Ͽ� ������ �����̸��� ����
		
		boardModel.setFileName(orgFileName);//boardmodel�� �����Ų��.
		
		if(newFile != null && !newFileName.equals("")) {//������ ���̾ƴҶ�
			if(orgFileName != null || !orgFileName.equals("")) {//orgfile�� ���� �ƴҶ�
				
				File removeFile = new File(uploadPath + orgFileName);//���������� ������
				removeFile.delete();//����
			}
			
			File newUploadFile = new File(uploadPath + newFileName);//�׷��� ���ο� ������ ����
			
			try {
				newFile.transferTo(newUploadFile);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			boardModel.setFileName(newFileName);
		}
		
		String content = boardModel.getContent().replaceAll("\r\n", "<br />");
		boardModel.setContent(content);
		
		boardService.modifyArticle(boardModel);//���� ����
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("idx", boardModel.getIdx());
		mav.setViewName("redirect:/board/view.do");
		return mav;//�󼼺���� ����
	}
	
	@RequestMapping("/delete.do")//�ۻ����� �� �޼ҵ� ����
	public ModelAndView boardDelete(HttpServletRequest request, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		int idx = Integer.parseInt(request.getParameter("idx"));
		
		BoardModel board = boardService.getOneArticle(idx);
		
		ModelAndView mav = new ModelAndView();
		
		if(!userId.equals(board.getWriterId())) {//�۾��̰� �ȸ�����
			mav.addObject("errCode", "1");
			mav.addObject("idx", idx);
			mav.setViewName("redirect:view.do");//�󼼺���� ����
		} else {//�ƴҰ��
			List<BoardCommentModel> commentList = boardService.getCommentList(idx);
			
			if(commentList.size() > 0) {//����� �������������
				mav.addObject("errCode", "2");
				mav.addObject("idx", idx);
				mav.setViewName("redirect:view.do");//���������� ��� ����
			} else {
				if(board.getFileName() != null) {//������ �������
					File removeFile = new File(uploadPath + board.getFileName());
					removeFile.delete();//������ ��������
				}
				
				boardService.deleteArticle(idx);//���� ���������ְ�
				
				mav.setViewName("redirect:list.do");//����Ʈ�� ����
			}
		}
		
		return mav;
	}
	
	@RequestMapping("/commentDelete.do")//��ۻ����� �� �޼ҵ尡 ����
	public ModelAndView commendDelete(HttpServletRequest request, HttpSession session) {
		int idx = Integer.parseInt(request.getParameter("idx"));
		int linkedArticleNum = Integer.parseInt(request.getParameter("linkedArticleNum"));
		
		String userId = (String)session.getAttribute("userId");
		BoardCommentModel comment = boardService.getOneComment(idx);
		
		ModelAndView mav = new ModelAndView();
		
		if(!userId.equals(comment.getWriterId())) {//����ۼ��ڰ� �����������
			mav.addObject("errCode", "1");//������ �����Ű��
		} else {
			boardService.deleteComment(idx);//�ƴҰ�� ������Ų��.
		}
		
		mav.addObject("idx", linkedArticleNum);
		mav.setViewName("redirect:view.do");
		
		return mav;//����
	}
	
	@RequestMapping("/recommend.do")//��õ�������.
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
