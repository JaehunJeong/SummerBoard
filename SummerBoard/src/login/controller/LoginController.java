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

	private ApplicationContext context; // 이 객체가 스프링 컨테이너를 수동으로 만드는 객체.
	
	@RequestMapping("/login.do")
	public String login() {
		return "/board/login";
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView loginProc(@ModelAttribute("LoginModel") LoginSessionModel loginModel, BindingResult result, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		//form validation
		new LoginValidator().validate(loginModel, result);//유효성검사
		
		if(result.hasErrors()) {//유효성검사에 에러가있을경우 login페이지로 리턴
			mav.setViewName("/board/login");
			return mav;
		}
		
		//에러가 없을 경우 계속한다.
		String userId = loginModel.getUserId();
		String userPw = loginModel.getUserPw();
		
		context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
		//이게 수동으로 applicationContext.xml 객체를 생성해서 컨테이너에 집어넣는 방법.
		
		//context에 있는 loginService 빈을 가져와서 사용한다.
		LoginService loginService = (LoginService) context.getBean("loginService");
		LoginSessionModel loginCheckResult = loginService.checkUserId(userId);
		
		//유저가 로그인했는지 채크
		if(loginCheckResult == null) {
			mav.addObject("userId", userId);
			mav.addObject("errorCode", 1);
			mav.setViewName("/board/login");
			return mav;
		}
		
		//비밀번호 채크
		if(loginCheckResult.getUserPw().equals(userPw)) {//맞을경우 id, name세션을 만들고 list로 리턴
			session.setAttribute("userId", userId);
			session.setAttribute("userName", loginCheckResult.getUserName());
			mav.setViewName("redirect:/board/list.do");
			return mav;
		} else {//아닐경우 에러코드 2를 보여주며 login페이지로 리턴
			mav.addObject("userId", userId);
			mav.addObject("errCode", 2);
			mav.setViewName("/board/login");
			return mav;
		}
	}
	
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {//로그아웃을 누르면 그 세션을 가져와서 세션을 없애고 login페이지로 리턴
		session.invalidate();
		return "redirect:login.do";
	}
}
