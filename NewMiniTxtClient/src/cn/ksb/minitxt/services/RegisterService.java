package cn.ksb.minitxt.services;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Scanner;

import cn.ksb.minitxt.client.BaseServiceImpl;
import cn.ksb.minitxt.client.Init;
import cn.ksb.minitxt.client.Service;
import cn.ksb.minitxt.client.ServiceFactory;
import cn.ksb.minitxt.clientutils.DefaultCommunicatorImpl;
import cn.ksb.minitxt.common.constants.Constants;
import cn.ksb.minitxt.common.constants.UserConstants;
import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.User;

public class RegisterService extends BaseServiceImpl<Serializable> {
	private String OUTPUT_TEXT_USERNAME = "请输入登录名:";
	private String OUTPUT_TEXT_PASSWORD = "请输入密码:";
	private String OUTPUT_TEXT_PASSWORD2 = "请再次输入密码:";
	private String OUTPUT_TEXT_USEREXIST = "用户名已存在，请重新注册！";
	private String OUTPUT_TEXT_PASSWORDNOTEQUAL = "两次密码不一样！";
	private String OUTPUT_USER_SAVESUCESS = "用户注册成功，请登录！";
	private String OUTPUT_USER_SAVEFAIL = "用户注册失败，请重新注册！";

	@Override
	public Service<? extends Serializable> execute() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println(OUTPUT_TEXT_USERNAME);
			String username = scanner.nextLine().trim();
			System.out.println(OUTPUT_TEXT_PASSWORD);
			String password = scanner.nextLine().trim();
			System.out.println(OUTPUT_TEXT_PASSWORD2);
			String repwd = scanner.nextLine().trim();
			
			if (username.length() == 0 || password.length() == 0) {
				System.out.println(OUTPUT_TEXT_INVALIDINPUT);
				continue;
			}
			if (!password.equals(repwd)) {
				System.out.println(OUTPUT_TEXT_PASSWORDNOTEQUAL);
				continue;
			}
			
			User user = new User();
			user.setPassword(password);
			user.setUsername(username);
			
			DataTransfer<User> dto = new DataTransfer<>();
			dto.setData(user);
			dto.setKey(Constants.COMMAND_REGISTER);
			
			DefaultCommunicatorImpl<User, ?> comm = new DefaultCommunicatorImpl<>();
			DataTransfer<?> response = null;
			try {
				comm.init(Init.getProperty("socket.server.ip"), Integer
						.parseInt(Init.getProperty("socket.server.port")));
				response = comm.communicate(dto);
			} catch (NumberFormatException | UnknownHostException
					| ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println(OUTPUT_TEXT_ERROR);
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(OUTPUT_TEXT_SERVERERROR);
				return ServiceFactory.getService(Constants.COMMAND_START);
			} finally {
				try {
					comm.destroy();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (UserConstants.SUCCESS == response.getResult()) {
				System.out.println(OUTPUT_USER_SAVESUCESS);
				return ServiceFactory.getService(Constants.COMMAND_LOGIN);
			} else if (UserConstants.USERNAME_EXSITS == response.getResult()) {
				System.out.println(OUTPUT_TEXT_USEREXIST);
				continue;
			} else {
				System.out.println(OUTPUT_USER_SAVEFAIL);
				continue;
			}
		}
	}

}
