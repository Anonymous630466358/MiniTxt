package cn.ksb.minitxt.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.ksb.minitxt.common.constants.NovelConstants;
import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.Novel;
import cn.ksb.minitxt.server.BaseServiceImpl;
import cn.ksb.minitxt.utils.NovelUtils;

public class UploadService extends BaseServiceImpl<Novel> {

	@Override
	protected void execute() throws IOException {
		int result = NovelUtils.saveNovel(getData());
		DataTransfer<?> dto = new DataTransfer<>();
		dto.setResult(result);
		getOut().writeObject(dto);
		
		if (result == NovelConstants.SAVE_SUCCESS) {
			byte[] buffer = new byte[8192];
			int len = -1;
			OutputStream fos = null;
			File file = NovelUtils.makeNovelFile(getData());
			try {
				fos = new FileOutputStream(file);
				while ((len = getIn().read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();
			} finally {
				if (fos != null)
					fos.close();
			}
		}
	}

}
