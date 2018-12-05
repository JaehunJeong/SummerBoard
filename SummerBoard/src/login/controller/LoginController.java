package login.controller;

import login.model.LoginSessionModel;
import login.service.LoginService;
import login.service.LoginValidator;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	private ApplicationContext context; // �� ��ü�� ������ �����̳ʸ� �������� ����� ��ü.
	
	@RequestMapping("/login.do")
	public String login() {
		return "/board/login";
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView loginProc(@ModelAttribute("LoginModel") LoginSessionModel loginModel, BindingResult result, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		//form validation
		new LoginValidator().validate(loginModel, result);//��ȿ���˻�
		
		if(result.hasErrors()) {//��ȿ���˻翡 ������������� login�������� ����
			mav.setViewName("/board/login");
			return mav;
		}
		
		//������ ���� ��� ����Ѵ�.
		String userId = loginModel.getUserId();
		String userPw = loginModel.getUserPw();
		
		context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
		//�̰� �������� applicationContext.xml ��ü�� �����ؼ� �����̳ʿ� ����ִ� ���.
		
		//context�� �ִ� loginService ���� �����ͼ� ����Ѵ�.
		LoginService loginService = (LoginService) context.getBean("loginService");
		LoginSessionModel loginCheckResult = loginService.checkUserId(userId);
		
		//������ �α����ߴ��� äũ
		if(loginCheckResult == null) {
			mav.addObject("userId", userId);
			mav.addObject("errorCode", 1);
			mav.setViewName("/board/login");
			return mav;
		}
		
		//��й�ȣ äũ
		if(loginCheckResult.getUserPw().equals(userPw)) {//������� id, name������ ����� list�� ����
			session.setAttribute("userId", userId);
			session.setAttribute("userName", loginCheckResult.getUserName());
			mav.setViewName("redirect:/board/list.do");
			return mav;
		} else {//�ƴҰ�� �����ڵ� 2�� �����ָ� login�������� ����
			mav.addObject("userId", userId);
			mav.addObject("errCode", 2);
			mav.setViewName("/board/login");
			return mav;
		}
	}
	
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {//�α׾ƿ��� ������ �� ������ �����ͼ� ������ ���ְ� login�������� ����
		session.invalidate();
		return "redirect:login.do";
	}
}
