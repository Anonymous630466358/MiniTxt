package cn.ksb.minitxt.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;

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

public class DownloadService extends BaseServiceImpl<Novel> {
	private String OUTPUT_SIGN_ASTERISK = "**********************************************\n";
	private String OUTPUT_TEXT_FILENOTFOUND = "文件未找到，可能已被删除！";
	private String OUTPUT_TEXT_FILECANNOTREAD = "文件无法读取，下载失败！";
	private String OUTPUT_TEXT_DOWNLOADSTART = "文件开始下载！";
	private String OUTPUT_TEXT_DOWNLOADFAIL = "文件下载失败！";
	private String OUTPUT_TEXT_DOWNLOADSUCCESS = "文件下载成功！\n下载后的文件路径是：%1$s\n下载后的文件名是：%2$s";
	private String OUTPUT_TEXT_SELECTLIST = "按回车键返回：";

	@Override
	public Service<? extends Serializable> execute() {
		DataTransfer<Novel> dto = new DataTransfer<>();
		dto.setKey(Constants.COMMAND_DOWNLOAD);
		dto.setData(getInputData());

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
		boolean tbc = true; // to be continue
		System.out.println(OUTPUT_SIGN_ASTERISK);
		switch (response.getResult()) {
		case NovelConstants.FILE_CANNOT_READ:
			tbc = false;
			System.out.println(OUTPUT_TEXT_FILECANNOTREAD);
			break;
		case NovelConstants.FILE_NOT_FOUND:
			tbc = false;
			System.out.println(OUTPUT_TEXT_FILENOTFOUND);
			break;
		default:
			System.out.println(OUTPUT_TEXT_DOWNLOADSTART);
			break;
		}
		if (tbc) {
			// 开启独立的下载线程进行下载，
			new Thread() {
				public void run() {
					String path = Init.getProperty("client.download.path");
					if (!path.endsWith(File.separator)) {
						path += File.separator;
					}
					String filename = getInputData().getName() + ".txt";
					File file = new File(path + filename);

					byte[] buffer = new byte[8192];
					int len = -1;
					OutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
						while ((len = comm.getIn().read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						fos.flush();
					} catch (IOException e) {
						file.delete();
						System.err.print(OUTPUT_SIGN_ASTERISK);
						System.err.println(OUTPUT_TEXT_DOWNLOADFAIL);
						System.err.print(OUTPUT_SIGN_ASTERISK);
						e.printStackTrace();
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						try {
							comm.destroy();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					System.err.print(OUTPUT_SIGN_ASTERISK);
					System.err.println(String.format(
							OUTPUT_TEXT_DOWNLOADSUCCESS, path, filename));
					System.err.print(OUTPUT_SIGN_ASTERISK);
				}
			}.start();
		} else {
			try {
				comm.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.err.print(OUTPUT_SIGN_ASTERISK);
		System.out.println(OUTPUT_TEXT_SELECTLIST);
		new java.util.Scanner(System.in).nextLine();
		Service<Classification> next = ServiceFactory
				.getService(Constants.COMMAND_GETNOVELS);
		next.setInputData(this.getInputData().getCls());
		return next;
	}

}
