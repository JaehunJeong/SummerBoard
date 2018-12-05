package member.controller;

import member.model.MemberModel;
import member.service.MemberService;
import member.service.MemberValidator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller//컨트롤러 선언
@RequestMapping("/member") //이매핑들어왔을때 이 컨트롤러 동작
public class MemberController {

	private ApplicationContext context;
	
	@RequestMapping("/join.do")
	public String memberJoin() {
		return "/board/join";
	}
	
	@RequestMapping(value="/join.do", method = RequestMethod.POST)
	//포스트 방식으로 왔을경우
	public ModelAndView addMember(@ModelAttribute("MemberModel") MemberModel memberModel, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		//모델앤뷰 객체생성
		
		new MemberValidator().validate(memberModel, result);//memberModel 객체 유효성 검사
		
		if(result.hasErrors()) {//에러가 있을경우
			mav.setViewName("/board/join");//이쪽으로 이동하게 설정
			return mav;//리턴
		}
		
		context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
		//스프링 컨테이너 수동 생성
		MemberService memberService = (MemberService) context.getBean("memberService");
		//context.xml에 있는 memberservice빈을 가져와서 memberservice에 넣음
		MemberModel checkMemberModel = memberService.findByUserId(memberModel.getUserId());
		//memberservice를 이용해 finbyuserid메소드를 실행시킨 값을 checkmembermodel에 저장
		
		if(checkMemberModel != null) {//checkmembermodel이 널이 아닐경우에만 움직인다.
			mav.addObject("errCode", 1); // 중복된 값이있다고 알려주기위해 만듬
			mav.setViewName("/board/join");
			return mav;
		}
		
		if(memberService.addMember(memberModel)) {//회원가입처리가 잘됬을경우
			mav.addObject("errCode", 3); // alter로 회원가입이 되었다고 처리해준후
			mav.setViewName("/board/login");//로그인 페이지로 이동
			return mav;
		} else {
			mav.addObject("errCode", 2); // 회원가입이 안됬으니 다시해야되며
			mav.setViewName("/board/login");//login페이지 이동
			return mav;
		}
	}
}
