package member.service;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import member.dao.MemberDao;
import member.model.MemberModel;

public class MemberService implements MemberDao{

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
	
	@Override
	public boolean addMember(MemberModel memberModel) {//회원가입
		sqlMapClientTemplate.insert("member.addMember", memberModel);
		//값을 넣어서
		MemberModel checkAddMember = findByUserId(memberModel.getUserId());
		//값을 넣은 회원의 id를 가져와 checkaddmember에 저장후
		
		if(checkAddMember == null) {//만약 값이 없으면
			return false;//회원가입이 안된거기때문에 false반환
		} else {//아닌경우
			return true;//true리턴
		}
	}
	
	@Override
	public MemberModel findByUserId(String userId) {
		return (MemberModel) sqlMapClientTemplate.queryForObject("member.findByUserId", userId);
	}
}
