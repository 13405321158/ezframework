/**
 * 
 * @author:weilai
 * @Data:2020-8-199:28:19
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.interfaces.many2may;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class ManyToManyDto {

	private String targetTableName;

	private String targetOnColumn;

	private String shipTableName;

	private String shipOnColumn;

	private String shipWhereColumn;

	private String selectColumn;

	public ManyToManyDto(ManyToMany m) {

		this.targetTableName = m.joinColumns().tableName();
		this.targetOnColumn = m.joinColumns().onColumn();

		this.shipTableName = m.inverseJoinColumns().tableName();
		this.shipOnColumn = m.inverseJoinColumns().onColumn();
		this.shipWhereColumn = m.inverseJoinColumns().whereColumn();

		List<String> selects = Lists.newArrayList();
		if (StringUtils.isNotBlank(m.selectColumn())) {
			String[] columns = StringUtils.split(m.selectColumn(), ",");
			for (String c : columns) {
				if (StringUtils.containsNone(c, "."))//不含有" . " ,则增加“a.” 前缀
					selects.add("a." + c);
				else
					selects.add(c);//否则不做处理，但是 这里只允许有“a.” 前缀，否则后台会报错误。  下一步做验证处理
			}
		}

		this.selectColumn = StringUtils.join(selects, ",");
	}

	public ManyToManyDto() {

	}

}
