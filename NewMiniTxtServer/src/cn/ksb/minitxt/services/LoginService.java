package cn.ksb.minitxt.services;

import java.io.IOException;

import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.User;
import cn.ksb.minitxt.server.BaseServiceImpl;
import cn.ksb.minitxt.utils.UserUtils;

/**
 * µÇÂ¼
 * @author surface
 *
 */
public class LoginService extends BaseServiceImpl<User>{

	@Override
	protected void execute() throws IOException {
		// TODO Auto-generated method stub
		int result=UserUtils.doLogin(getData());
		DataTransfer<?> dt=new DataTransfer<>();
		dt.setResult(result);
		getOut().writeObject(dt);
	}
	
}
