package member.dao;

import member.model.MemberModel;

public interface MemberDao {

	boolean addMember(MemberModel memberModel);
	MemberModel findByUserId(String userId);
}
