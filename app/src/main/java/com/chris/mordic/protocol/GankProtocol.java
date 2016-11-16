package com.chris.mordic.protocol;

import com.chris.mordic.base.BaseProtocol;
import com.chris.mordic.data.GankBean;
import com.google.gson.Gson;


/**
 * @author  Administrator
 * @time 	2015-7-16 下午4:14:05
 * @des	TODO
 *
 * @version $Rev: 25 $
 * @updateAuthor $Author: admin $
 * @updateDate $Date: 2015-07-16 17:13:38 +0800 (星期四, 16 七月 2015) $
 * @updateDes TODO
 */
public class GankProtocol extends BaseProtocol<GankBean> {


	@Override
	public String getInterfaceKey() {
		return "gank";
	}

	@Override
	public GankBean parseJson(String jsonString) {
		Gson gson = new Gson();
		//http://localhost:8080/GooglePlayServer/home?index=0
		return gson.fromJson(jsonString, GankBean.class);
	}

}
