package login.model;

public class LoginSessionModel {

	private String userId;
	private String userPw;
	private String userName;
	private boolean auth;
	
	public String getUserId() {
		return userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public String getUserName() {
		return userName;
	}
	public boolean isAuth() {
		return auth;
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
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	
	
}
