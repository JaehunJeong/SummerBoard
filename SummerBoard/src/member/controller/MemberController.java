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

@Controller//��Ʈ�ѷ� ����
@RequestMapping("/member") //�̸��ε������� �� ��Ʈ�ѷ� ����
public class MemberController {

	private ApplicationContext context;
	
	@RequestMapping("/join.do")
	public String memberJoin() {
		return "/board/join";
	}
	
	@RequestMapping(value="/join.do", method = RequestMethod.POST)
	//����Ʈ ������� �������
	public ModelAndView addMember(@ModelAttribute("MemberModel") MemberModel memberModel, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		//�𵨾غ� ��ü����
		
		new MemberValidator().validate(memberModel, result);//memberModel ��ü ��ȿ�� �˻�
		
		if(result.hasErrors()) {//������ �������
			mav.setViewName("/board/join");//�������� �̵��ϰ� ����
			return mav;//����
		}
		
		context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
		//������ �����̳� ���� ����
		MemberService memberService = (MemberService) context.getBean("memberService");
		//context.xml�� �ִ� memberservice���� �����ͼ� memberservice�� ����
		MemberModel checkMemberModel = memberService.findByUserId(memberModel.getUserId());
		//memberservice�� �̿��� finbyuserid�޼ҵ带 �����Ų ���� checkmembermodel�� ����
		
		if(checkMemberModel != null) {//checkmembermodel�� ���� �ƴҰ�쿡�� �����δ�.
			mav.addObject("errCode", 1); // �ߺ��� �����ִٰ� �˷��ֱ����� ����
			mav.setViewName("/board/join");
			return mav;
		}
		
		if(memberService.addMember(memberModel)) {//ȸ������ó���� �߉������
			mav.addObject("errCode", 3); // alter�� ȸ�������� �Ǿ��ٰ� ó��������
			mav.setViewName("/board/login");//�α��� �������� �̵�
			return mav;
		} else {
			mav.addObject("errCode", 2); // ȸ�������� �ȉ����� �ٽ��ؾߵǸ�
			mav.setViewName("/board/login");//login������ �̵�
			return mav;
		}
	}
}
