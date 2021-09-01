/*
 * @作者: 魏来
 * @日期: 2021/8/21  下午4:05
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.leesky.ezframework.backend.model.DealerOrderItemModel;
import com.leesky.ezframework.backend.model.DealerOrderModel;
import com.leesky.ezframework.backend.service.IdealerOrderItemService;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.query.ParamModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestAction {

//	private final IdealerOrderService idealerOrderService;
	private final IdealerOrderItemService idealerOrderItemService;

	@RequestMapping("/r01")
	@Transactional
	public AjaxJson index01(@RequestBody ParamModel model) {

		AjaxJson json = new AjaxJson();

		try {
			DealerOrderModel order = new DealerOrderModel();

			List<DealerOrderItemModel> list = Lists.newArrayList(new DealerOrderItemModel(order), new DealerOrderItemModel(order), new DealerOrderItemModel(order));

			order.setItems(list);

//			this.idealerOrderService.insert(order, true);

			this.idealerOrderItemService.insert(list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			json.setSuccess(false, e.getLocalizedMessage());
		}
		return json;
	}

}
