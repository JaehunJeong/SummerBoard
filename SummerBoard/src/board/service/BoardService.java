package board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import board.dao.BoardDao;
import board.model.BoardCommentModel;
import board.model.BoardModel;

public class BoardService implements BoardDao{//인터페이스를 구현해둔 클래스.

	private SqlMapClientTemplate sqlMapClientTemplate;//sql을 사용하기위해 선언
	private HashMap<String, Object> valueMap = new HashMap<String, Object>();
	//이제 값을 hashmap을 사용해서 넣는다.
	
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
	
	@Override
	public List<BoardModel> getBoardList(int startArticleNum, int endArticleNum) {
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.getBoardList", valueMap);
	}

	@Override
	public BoardModel getOneArticle(int idx) {
		return (BoardModel) sqlMapClientTemplate.queryForObject("board.getOneArticle", idx);
	}

	@Override
	public List<BoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.searchArticle", valueMap);
	}

	@Override
	public List<BoardCommentModel> getCommentList(int idx) {
		return sqlMapClientTemplate.queryForList("board.getCommentList", idx);
	}

	@Override
	public BoardCommentModel getOneComment(int idx) {
		return (BoardCommentModel) sqlMapClientTemplate.queryForObject("board.getOneComment", idx);
	}

	@Override
	public boolean modifyArticle(BoardModel board) {
		sqlMapClientTemplate.update("board.modifyArticle", board);
		return true;
	}

	@Override
	public boolean writeArticle(BoardModel board) {
		sqlMapClientTemplate.insert("board.writeArticle", board);
		return true;
	}

	@Override
	public boolean writeComment(BoardCommentModel comment) {
		sqlMapClientTemplate.insert("board.writeComment", comment);
		return true;
	}

	@Override
	public void updateHitcount(int hitcount, int idx) {
		valueMap.put("hitcount", hitcount);
		valueMap.put("idx", idx);
		sqlMapClientTemplate.update("board.updateHitcount", valueMap);
	}

	@Override
	public void updateRecommendCount(int recommendcount, int idx) {
		valueMap.put("recommendcount", recommendcount);
		valueMap.put("idx", idx);
		sqlMapClientTemplate.update("board.updateRecommendcount", valueMap);
	}

	@Override
	public int getTotalNum() {
		return (Integer) sqlMapClientTemplate.queryForObject("board.getTotalNum");
	}

	@Override
	public int getSearchTotalNum(String type, String keyword) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		return (Integer) sqlMapClientTemplate.queryForObject("board.getSearchTotalNum", valueMap);
	}

	@Override
	public void deleteComment(int idx) {
		sqlMapClientTemplate.delete("board.deleteComment", idx);
		
	}

	@Override
	public void deleteArticle(int idx) {
		sqlMapClientTemplate.delete("board.deleteArticle", idx);
		
	}

	
}
