package member.model;

public class MemberModel {

	private int idx;
	private String userId;
	private String userPw;
	private String userName;
	private String joinDate;
	
	public int getIdx() {
		return idx;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public String getUserName() {
		return userName;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	
}
