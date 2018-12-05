package board.dao;

import java.util.List;

import board.model.BoardCommentModel;
import board.model.BoardModel;

public interface BoardDao {
	//��� �Խñ� ��������
	List<BoardModel> getBoardList(int startArticleNum, int showArticleLimit);
	
	//�󼼺��⸦ ���� �� ���� ���� ��������
	BoardModel getOneArticle(int idx);
	
	//�˻��� �Խñ��� Ÿ�԰� Ű���带 �߰��Ͽ� �׿� �´� �Խñ��� �˻�
	List<BoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum);
	
	//�� �ۿ� ���� ��� ���� ��������
	List<BoardCommentModel> getCommentList(int idx);
	
	//�� ����� ������ ������
	BoardCommentModel getOneComment(int idx);
	
	//�� ������ ���� ���� �޼ҵ�
	boolean modifyArticle(BoardModel board);
	
	//���ۼ��� ���� ���� �޼ҵ�
	boolean writeArticle(BoardModel board);
	
	//��� �ۼ�
	boolean writeComment(BoardCommentModel comment);
	
	//��ȸ�� ����
	void updateHitcount(int hitcount, int idx);
	
	//��õ�� ����
	void updateRecommendCount(int recommendcount, int idx);
	
	//�Խñ��� �� ����
	int getTotalNum();
	
	//�˻���� �Խñ��� �� ����
	int getSearchTotalNum(String type, String keyword);
	
	//��� �����
	void deleteComment(int idx);
	
	//�Խñ� �����
	void deleteArticle(int idx);
}
