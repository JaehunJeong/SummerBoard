package board.model;

public class BoardCommentModel {//´ñ±Û ÀÚ¹Ùºó

	private int idx;
	private String writer;
	private String content;
	private String writeDate;
	private int linkedArticleNum;
	private String writerId;
	
	public int getIdx() {
		return idx;
	}
	public String getWriter() {
		return writer;
	}
	public String getContent() {
		return content;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public int getLinkedArticleNum() {
		return linkedArticleNum;
	}
	public String getWriterId() {
		return writerId;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public void setLinkedArticleNum(int linkedArticleNum) {
		this.linkedArticleNum = linkedArticleNum;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
	
}
