package cn.ksb.minitxt.services;

import java.io.IOException;

import cn.ksb.minitxt.common.entity.Classification;
import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.common.entity.Novel;
import cn.ksb.minitxt.server.BaseServiceImpl;
import cn.ksb.minitxt.utils.NovelUtils;

public class GetNovelsByClassService extends BaseServiceImpl<Classification> {

	@Override
	protected void execute() throws IOException {
		DataTransfer<Novel[]> dto = new DataTransfer<>();
		dto.setData(NovelUtils.getNovels(getData()));
		getOut().writeObject(dto);
	}

}
