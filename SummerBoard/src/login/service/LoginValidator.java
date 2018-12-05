package login.service;

import login.model.LoginSessionModel;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoginValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginSessionModel.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		LoginSessionModel loginModel = (LoginSessionModel)target;
		
		if(loginModel.getUserId() == null || loginModel.getUserId().trim().isEmpty()) {
			errors.rejectValue("userId", "required");
		}
		
		if(loginModel.getUserPw() == null || loginModel.getUserPw().trim().isEmpty()) {
			errors.rejectValue("userPw", "required");
		}
	}
}
