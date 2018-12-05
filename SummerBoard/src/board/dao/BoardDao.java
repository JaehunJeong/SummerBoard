package board.dao;

import java.util.List;

import board.model.BoardCommentModel;
import board.model.BoardModel;

public interface BoardDao {
	//모든 게시글 가져오기
	List<BoardModel> getBoardList(int startArticleNum, int showArticleLimit);
	
	//상세보기를 위해 한 글의 정보 가져오기
	BoardModel getOneArticle(int idx);
	
	//검색한 게시글의 타입과 키워드를 추가하여 그에 맞는 게시글을 검색
	List<BoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum);
	
	//그 글에 대한 댓글 수를 가져와줌
	List<BoardCommentModel> getCommentList(int idx);
	
	//한 댓글의 정보를 가져옴
	BoardCommentModel getOneComment(int idx);
	
	//글 수정을 위해 만든 메소드
	boolean modifyArticle(BoardModel board);
	
	//글작성을 위해 만든 메소드
	boolean writeArticle(BoardModel board);
	
	//댓글 작성
	boolean writeComment(BoardCommentModel comment);
	
	//조회수 증가
	void updateHitcount(int hitcount, int idx);
	
	//추천수 증가
	void updateRecommendCount(int recommendcount, int idx);
	
	//게시글의 총 갯수
	int getTotalNum();
	
	//검색결과 게시글의 총 갯수
	int getSearchTotalNum(String type, String keyword);
	
	//댓글 지우기
	void deleteComment(int idx);
	
	//게시글 지우기
	void deleteArticle(int idx);
}
