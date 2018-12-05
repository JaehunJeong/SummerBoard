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
	public boolean addMember(MemberModel memberModel) {//ȸ������
		sqlMapClientTemplate.insert("member.addMember", memberModel);
		//���� �־
		MemberModel checkAddMember = findByUserId(memberModel.getUserId());
		//���� ���� ȸ���� id�� ������ checkaddmember�� ������
		
		if(checkAddMember == null) {//���� ���� ������
			return false;//ȸ�������� �ȵȰű⶧���� false��ȯ
		} else {//�ƴѰ��
			return true;//true����
		}
	}
	
	@Override
	public MemberModel findByUserId(String userId) {
		return (MemberModel) sqlMapClientTemplate.queryForObject("member.findByUserId", userId);
	}
}
