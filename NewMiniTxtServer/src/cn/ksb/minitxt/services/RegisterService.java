package cn.ksb.minitxt.services;

import java.io.IOException;

import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.User;
import cn.ksb.minitxt.server.BaseServiceImpl;
import cn.ksb.minitxt.utils.UserUtils;

public class RegisterService extends BaseServiceImpl<User> {

	@Override
	public void execute() throws IOException {
		// TODO 注册相关的代码
		int result = UserUtils.doRegister(getData());
		DataTransfer<?> dto = new DataTransfer<>();
		dto.setResult(result);
		getOut().writeObject(dto);
	}
	
}
