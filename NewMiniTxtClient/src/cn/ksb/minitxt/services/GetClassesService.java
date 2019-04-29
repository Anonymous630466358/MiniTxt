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
import cn.ksb.minitxt.common.entity.Classification;
import cn.ksb.minitxt.common.entity.DataTransfer;

public class GetClassesService extends BaseServiceImpl<java.io.Serializable> {
	private String OUTPUT_SIGN_LINE = "-------------------------------------\n";
	private String OUTPUT_SIGN_SELECT = "ÇëÑ¡Ôñ£º";
	private String OUTPUT_TEXT_RETURN = "0.ÍË³öµÇÂ¼";
	private StringBuilder MENU_TEXT_SORT_BEGIN = new StringBuilder(
			OUTPUT_SIGN_LINE).append(OUTPUT_TEXT_RETURN);
	private StringBuilder MENU_TEXT_SORT_END = new StringBuilder(
			OUTPUT_SIGN_LINE).append(OUTPUT_SIGN_SELECT);

	@Override
	public Service<? extends Serializable> execute() {
		DataTransfer<Serializable> dto = new DataTransfer<>();
		dto.setKey(Constants.COMMAND_GETCLASSES);

		DefaultCommunicatorImpl<Serializable, Classification[]> comm = new DefaultCommunicatorImpl<>();
		DataTransfer<Classification[]> response = null;
		try {
			comm.init(Init.getProperty("socket.server.ip"),
					Integer.parseInt(Init.getProperty("socket.server.port")));
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
		Classification[] cls = response.getData();
		System.out.println(MENU_TEXT_SORT_BEGIN);
		int i = 0;
		for (Classification c : cls) {
			System.out.println((++i) + "." + c.getClassname());
		}
		System.out.println(MENU_TEXT_SORT_END);
		Scanner scanner = new Scanner(System.in);
		int choice = -1;
		while (true) {
			try {
				choice = Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println(OUTPUT_TEXT_INVALIDINPUT);
				continue;
			}
			if (choice < 0 || choice > cls.length) {
				System.out.println(OUTPUT_TEXT_INVALIDINPUT);
				continue;
			}
			break;
		}
		if (choice == 0) {
			return ServiceFactory.getService(Constants.COMMAND_START);
		} else {
			Service<Classification> next = null;
			next = ServiceFactory.getService(Constants.COMMAND_GETNOVELS);
			next.setInputData(cls[choice - 1]);
			return next;
		}
	}

}
