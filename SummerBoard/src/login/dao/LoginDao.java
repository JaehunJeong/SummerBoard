package login.dao;

import login.model.LoginSessionModel;

public interface LoginDao {

	LoginSessionModel checkUserId(String userId);
}
