package com.leesky.ezframework.mybatis.query;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/*
 * M2m出查询时 向 leeskyMapper.xml 之 findM2M方法  传递参数用
 * 例如： SELECT r.* FROM cbm_child a
 *          LEFT JOIN cbm_l_student_course l  ON a.id = l.student_id
 *          LEFT JOIN cbm_course  r ON r.id = l.course_id
 *       where a.id = '678368c05c7b05596a6e3a1c5d9a5eca'
 * 这里：
 *   selectContent = r.*
 *   mainName   = cbm_child，
 *   middleName = cbm_l_student_course
 *   resultName = cbm_course
 *   condition  = 678368c05c7b05596a6e3a1c5d9a5eca
 * @author： 魏来
 * @date： 2021/12/17 上午10:59
 */
@Data
@NoArgsConstructor
public class M2mParam {

    private String selectContent;// select 内容

    private String mainName;// 发起人=学生表【学生表 -中间表 - 课程表】

    private String middleName;// 关系维护=中间表【学生表 -中间表 - 课程表】

    private String resultName;// 结果集=课程表【学生表 -中间表 - 课程表】

    private Object condition;// where 条件= 一般是发起人主键

    private String mainKey;// 发起人的 查询条件（在上述例子中 =id）

    private String joinCol;//发起人在中间表的字段名称 （在上述例子中=student_id）

    private String inverseCol;// 结果集在中间表的字段名称 （在上述例子中= course_id）

    private String resultId;// 结果集主键（在这里=r.id）


    public M2mParam(String selectContent, String mainName, String middleName, String resultName, Object condition, String mainKey, String joinCol, String inverseCol, String resultId) {
        this.mainName = mainName;
        this.middleName = middleName;
        this.resultName = resultName;
        this.condition = condition;

        this.mainKey = mainKey;
        this.joinCol = joinCol;
        this.inverseCol = inverseCol;

        this.resultId = resultId;

        if (StringUtils.isBlank(selectContent))
            this.selectContent = "r.*";
        else {
            List<String> result = Lists.newArrayList();
            String[] array = StringUtils.split(selectContent, ",");
            Lists.newArrayList(array).forEach(e -> result.add("r." + e));
            this.selectContent = StringUtils.join(result, ",");
        }
    }


}
