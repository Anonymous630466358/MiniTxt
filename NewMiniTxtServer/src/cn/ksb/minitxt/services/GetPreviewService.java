package cn.ksb.minitxt.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import cn.ksb.minitxt.common.constants.NovelConstants;
import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.Novel;
import cn.ksb.minitxt.server.BaseServiceImpl;
import cn.ksb.minitxt.utils.NovelUtils;

public class GetPreviewService extends BaseServiceImpl<Novel> {

	@Override
	protected void execute() throws IOException {
		DataTransfer<String> dto = new DataTransfer<>();
		try {
			dto.setData(NovelUtils.getPreview(getData()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			dto.setResult(NovelConstants.FILE_NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			dto.setResult(NovelConstants.FILE_CANNOT_READ);
		}
		getOut().writeObject(dto);
	}

}
