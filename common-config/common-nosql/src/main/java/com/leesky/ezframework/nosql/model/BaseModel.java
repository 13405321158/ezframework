/**
 *
 * @ver: 1.0.0
 * @Data:下午12:44:52 ,2019年11月16日
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */
package com.leesky.ezframework.nosql.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Data
public class BaseModel {

	@Id
	@Field("_id")
	protected ObjectId id;

	@Field("create_date")
	protected LocalDateTime createDate;

	@Field("modify_date")
	protected LocalDateTime modifyDate;

}
