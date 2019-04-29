package cn.ksb.minitxt.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Scanner;

import cn.ksb.minitxt.client.BaseServiceImpl;
import cn.ksb.minitxt.client.Init;
import cn.ksb.minitxt.client.Service;
import cn.ksb.minitxt.client.ServiceFactory;
import cn.ksb.minitxt.clientutils.DefaultCommunicatorImpl;
import cn.ksb.minitxt.common.constants.Constants;
import cn.ksb.minitxt.common.constants.NovelConstants;
import cn.ksb.minitxt.common.entity.Classification;
import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.Novel;

public class UploadService extends BaseServiceImpl<Classification> {
	private String OUTPUT_INPUT_NAME = "请输入小说名：";
	private String OUTPUT_INPUT_AUTHOR = "请输入作者：";
	private String OUTPUT_INPUT_DESC = "请输入简介：";
	private String OUTPUT_INPUT_PATH = "请输入上传的txt(请注意路径用/或者\\\\)：";
	private String OUTPUT_TEXT_REUPLOAD = "继续上传请输入1，返回请输入0：";
	private String OUTPUT_TEXT_FILEEXSITS = "文件已存在，上传终止！ ";
	private String OUTPUT_SIGN_ASTERISK = "**********************************************";
	private String OUTPUT_TEXT_FILENOTFOUND = "文件未找到，请确认后重试！";
	private String OUTPUT_TEXT_UPLOADFAIL = "文件上传失败！";

	@Override
	public Service<? extends Serializable> execute() {
		DataTransfer<Novel> dto = new DataTransfer<>();
		dto.setKey(Constants.COMMAND_UPLOAD);
		Novel novel = new Novel();
		dto.setData(novel);
		novel.setCls(this.getInputData());

		Scanner scanner = new Scanner(System.in);
		System.out.println(OUTPUT_INPUT_NAME);
		novel.setName(scanner.nextLine().trim()); // 可以加验证
		System.out.println(OUTPUT_INPUT_AUTHOR);
		novel.setAuthor(scanner.nextLine().trim());
		System.out.println(OUTPUT_INPUT_DESC);
		novel.setDesc(scanner.nextLine().trim());
		String temp = null;
		while (true) {
			System.out.println(OUTPUT_INPUT_PATH);
			temp = scanner.nextLine().trim();
			if (new File(temp).exists()) {
				break;
			} else {
				System.out.println(OUTPUT_TEXT_FILENOTFOUND);
				continue;
			}
		}

		final String path = temp;

		final DefaultCommunicatorImpl<Novel, ?> comm = new DefaultCommunicatorImpl<>();
		DataTransfer<?> response = null;
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
		}
		if (response.getResult() == NovelConstants.SAVE_SUCCESS) {
			new Thread() {
				public void run() {
					File file = new File(path);
					byte[] buffer = new byte[8192];
					int len = -1;
					InputStream fis = null;
					try {
						fis = new FileInputStream(file);
						while ((len = fis.read(buffer)) != -1) {
							comm.getOut().write(buffer, 0, len);
						}
						comm.getOut().flush();
						comm.getSocket().shutdownOutput();
					} catch (FileNotFoundException e) {
						System.err.println(OUTPUT_SIGN_ASTERISK);
						System.err.println(OUTPUT_TEXT_FILENOTFOUND);
						System.err.println(OUTPUT_SIGN_ASTERISK);
					} catch (IOException e) {
						System.err.println(OUTPUT_SIGN_ASTERISK);
						System.err.println(OUTPUT_TEXT_UPLOADFAIL);
						System.err.println(OUTPUT_SIGN_ASTERISK);
					} finally {
						try {
							if (fis != null)
								fis.close();
							comm.destroy();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		} else {
			System.out.println(OUTPUT_SIGN_ASTERISK);
			System.out.println(OUTPUT_TEXT_FILEEXSITS);
			try {
				comm.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 下一步操作提示
		System.out.println(OUTPUT_SIGN_ASTERISK);
		System.out.println(OUTPUT_TEXT_REUPLOAD);
		String choice = null;
		while (true) {
			choice = scanner.nextLine().trim();
			switch (choice) {
			case "0":
				Service<Classification> getNovels = ServiceFactory
						.getService(Constants.COMMAND_GETNOVELS);
				getNovels.setInputData(this.getInputData());
				return getNovels;
			case "1":
				Service<Classification> upload = ServiceFactory
						.getService(Constants.COMMAND_UPLOAD);
				upload.setInputData(this.getInputData());
				return upload;
			default:
				System.out.println(OUTPUT_TEXT_INVALIDINPUT);
				break;
			}
		}
	}

}
