package cn.ksb.minitxt.services;

import java.io.IOException;
import java.io.Serializable;

import cn.ksb.minitxt.common.entity.Classification;
import cn.ksb.minitxt.common.entity.DataTransfer;
import cn.ksb.minitxt.server.BaseServiceImpl;
import cn.ksb.minitxt.utils.NovelUtils;

public class GetClassesService extends BaseServiceImpl<Serializable> {

	@Override
	protected void execute() throws IOException {
		// TODO Auto-generated method stub
		DataTransfer<Classification[]> dt=new DataTransfer<>();
		dt.setData(NovelUtils.getClasses());
		getOut().writeObject(dt);
	}

}
