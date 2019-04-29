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
import cn.ksb.minitxt.common.entity.Novel;

public class GetNovelsByClassService extends BaseServiceImpl<Classification> {
	private String OUTPUT_TEXT_READDOWN = "阅读和下载请选择文件序号，上传TXT请输入-1，返回请输入0：";
	private String OUTPUT_SIGN_BEGINLINE = "------------------%1$s列表开始------------------";
	private String OUTPUT_SIGN_HEAD = "序号\t\t名称\t\t作者\t\t简介";
	private String OUTPUT_SIGN_ENDLINE = "------------------小说列表结束------------------";
	private String OUTPUT_SIGN_LINE = "-------------------------------------\n";
	private String STORY_TEXT_RETURN = "0.返回上一级菜单\n";
	private String STORY_TEXT_READ = "1.在线阅读\n";
	private String STORY_TEXT_DOWNLOAD = "2.下载TXT\n";
	private String OUTPUT_SIGN_SELECT = "请选择： ";
	private StringBuffer MENU_FUNCTION = new StringBuffer(OUTPUT_SIGN_LINE)
			.append(STORY_TEXT_RETURN).append(STORY_TEXT_READ)
			.append(STORY_TEXT_DOWNLOAD).append(OUTPUT_SIGN_LINE)
			.append(OUTPUT_SIGN_SELECT);

	@Override
	public Service<? extends Serializable> execute() {
		DataTransfer<Classification> dto = new DataTransfer<>();
		dto.setKey(Constants.COMMAND_GETNOVELS);
		dto.setData(this.getInputData());

		DefaultCommunicatorImpl<Classification, Novel[]> comm = new DefaultCommunicatorImpl<>();
		DataTransfer<Novel[]> response = null;
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
		// java.util.Formatter
		System.out.println(String.format(OUTPUT_SIGN_BEGINLINE, this
				.getInputData().getClassname()));
		System.out.println(OUTPUT_SIGN_HEAD);
		int i = 0;
		StringBuilder novelInfo = null;
		for (Novel nvl : response.getData()) {
			novelInfo = new StringBuilder(++i + "").append("\t\t")
					.append(nvl.getName()).append("\t\t").append(nvl.getAuthor())
					.append("\t\t").append(nvl.getDesc());
			System.out.println(novelInfo);
		}
		System.out.println(OUTPUT_SIGN_ENDLINE);
		System.out.println(OUTPUT_TEXT_READDOWN);
		Scanner scanner = new Scanner(System.in);
		int choice = -2;
		while (true) {
			try {
				choice = Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println(OUTPUT_TEXT_INVALIDINPUT);
				continue;
			}
			if (choice < -1 || choice > response.getData().length) {
				System.out.println(OUTPUT_TEXT_INVALIDINPUT);
				continue;
			}
			break;
		}
		if (choice == -1) {
			// 执行上传功能
			// 设定上传的分类
			Service<Classification> upload = null;
			upload = ServiceFactory.getService(Constants.COMMAND_UPLOAD);
			upload.setInputData(this.getInputData());
			return upload;
		} else if (choice == 0) {
			return ServiceFactory.getService(Constants.COMMAND_GETCLASSES);
		} else {
			Novel novel = response.getData()[choice - 1];
			System.out.println(MENU_FUNCTION);
			while (true) {
				try {
					choice = Integer.parseInt(scanner.nextLine().trim());
				} catch (NumberFormatException e) {
					System.out.println(OUTPUT_TEXT_INVALIDINPUT);
					continue;
				}
				if (choice < 0 || choice > 2) {
					System.out.println(OUTPUT_TEXT_INVALIDINPUT);
					continue;
				}
				break;
			}
			switch (choice) {
			case 0:
				// 重新获得小说列表
				// 传入用户选择的分类信息
				Service<Classification> getNovels = null;
				getNovels = ServiceFactory.getService(Constants.COMMAND_GETNOVELS);
				getNovels.setInputData(this.getInputData());
				return getNovels;
			case 1:
				// 调用预览功能
				// 传入用户选择的小说, novel
				Service<Novel> getPreview = null;
				getPreview = ServiceFactory.getService(Constants.COMMAND_GETPREVIEW);
				getPreview.setInputData(novel);
				return getPreview;
			default:
				// 调用下载功能
				// 传入用户选择的小说, novel
				Service<Novel> download = null;
				download = ServiceFactory.getService(Constants.COMMAND_DOWNLOAD);
				download.setInputData(novel);
				return download;
			}
		}
	}

}
