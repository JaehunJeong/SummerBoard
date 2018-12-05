package board.model;

public class BoardModel {//게시글 자바빈

	private int rnum;
	private int idx;
	private String writer;
	private String subject;
	private String content;
	private int hitcount = 0;
	private int recommendcount = 0;
	private int comment = 0;
	private String writeDate;
	private String writerId;
	private String fileName;
	
	public int getRnum() {
		return rnum;
	}
	public int getIdx() {
		return idx;
	}
	public String getWriter() {
		return writer;
	}
	public String getSubject() {
		return subject;
	}
	public String getContent() {
		return content;
	}
	public int getHitcount() {
		return hitcount;
	}
	public int getRecommendcount() {
		return recommendcount;
	}
	public int getComment() {
		return comment;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public String getWriterId() {
		return writerId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setRnum(int rnum) {
		this.rnum = rnum;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setHitcount(int hitcount) {
		this.hitcount = hitcount;
	}
	public void setRecommendcount(int recommendcount) {
		this.recommendcount = recommendcount;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
