/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @Desc: 项目启动时自动扫描配置的目录中的model，根据配置的规则自动创建或更新表 该逻辑只适用于mysql，其他数据库尚且需要另外扩展，因为sql的语法不同
 */
package com.leesky.ezframework.mybatis.ddl.service.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.ddl.annotation.ExcludeDDL;
import com.leesky.ezframework.mybatis.ddl.annotation.LengthCount;
import com.leesky.ezframework.mybatis.ddl.command.CreateTableParam;
import com.leesky.ezframework.mybatis.ddl.command.SysMysqlColumns;
import com.leesky.ezframework.mybatis.ddl.constants.Constants;
import com.leesky.ezframework.mybatis.ddl.constants.MySqlTypeConstant;
import com.leesky.ezframework.mybatis.ddl.service.ImysqlCreateTableService;
import com.leesky.ezframework.mybatis.ddl.utils.ClassTools;
import com.leesky.ezframework.mybatis.mapper.CreateTablesMapper;
import com.leesky.ezframework.utils.Hump2underline;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@Transactional
@Service("sysMysqlCreateTableManager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MysqlCreateTableServiceImpl implements ImysqlCreateTableService {


    private final CreateTablesMapper createMysqlTablesMapper;
    private static final Map<String, String> CommentMap = Maps.newHashMap();//数据表注释
    private final Map<String, CreateTableParam> idMap = Maps.newHashMap();//设置表key在第一位
    private static final Map<String, Object> mySqlTypeAndLengthMap = mySqlTypeAndLengthMap();// 获取Mysql的类型，以及类型需要设置几个长度


    /**
     * 如果配置文件配置的是create，表示将所有的表删掉重新创建
     */
    @Override
    public void delTable(String tableName) {
        Set<Class<?>> classes = ClassTools.getClasses(tableName);
        for (Class<?> clas : classes) {
            TableName table = clas.getAnnotation(TableName.class);
            createMysqlTablesMapper.dorpTableByName(table.value());
        }
    }

    /**
     * 遍历全部数据表： 必须带有TableName注解(如果还带有ExcludeDDL注解，则排除在外)
     */
    @Override
    public void createMysqlTable(String packName) {
        try {
            // 从包package中获取所有的Class
            Set<Class<?>> classes = ClassTools.getClasses(packName);
            // 初始化用于存储各种操作表结构的容器
            Map<String, Map<String, List<Object>>> baseTableMap = initTableMap();

            // 循环全部的model
            for (Class<?> clas : classes) {
                TableName tablename = clas.getAnnotation(TableName.class);
                ExcludeDDL exclude = clas.getAnnotation(ExcludeDDL.class);
                if (ObjectUtils.isNotEmpty(tablename) && ObjectUtils.isEmpty(exclude)) // 没有打注解不需要创建表
                    buildTableMapConstruct(clas, baseTableMap); // 构建出全部表的增删改的map
            }

            // 根据传入的map，分别去创建或修改表结构
            createOrModifyTableConstruct(baseTableMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化用于存储各种操作表结构的容器
     *
     * @return 初始化map
     */
    private Map<String, Map<String, List<Object>>> initTableMap() {
        Map<String, Map<String, List<Object>>> baseTableMap = Maps.newHashMap();
        // 1.用于存需要创建的表名+结构
        baseTableMap.put(Constants.NEW_TABLE_MAP, Maps.newHashMap());
        // 2.用于存需要更新字段类型等的表名+结构
        baseTableMap.put(Constants.MODIFY_TABLE_MAP, Maps.newHashMap());
        // 3.用于存需要增加字段的表名+结构
        baseTableMap.put(Constants.ADD_TABLE_MAP, Maps.newHashMap());
        // 4.用于存需要删除字段的表名+结构
        baseTableMap.put(Constants.REMOVE_TABLE_MAP, Maps.newHashMap());
        // 5.用于存需要删除主键的表名+结构
        baseTableMap.put(Constants.DROPKEY_TABLE_MAP, Maps.newHashMap());
        // 6.用于存需要删除唯一约束的表名+结构
        baseTableMap.put(Constants.DROPINDEXANDUNIQUE_TABLE_MAP, Maps.newHashMap());
        // 7.用于存需要增加的索引
        baseTableMap.put(Constants.ADDINDEX_TABLE_MAP, Maps.newHashMap());
        // 8.用于存需要增加的唯一约束
        baseTableMap.put(Constants.ADDUNIQUE_TABLE_MAP, Maps.newHashMap());
        return baseTableMap;
    }

    /**
     * 构建出全部表的增删改的map
     *
     * @param clas         package中的model的Class
     * @param baseTableMap 用于存储各种操作表结构的容器
     */
    private void buildTableMapConstruct(Class<?> clas, Map<String, Map<String, List<Object>>> baseTableMap) {

        // 获取model的table注解
        TableName table = clas.getAnnotation(TableName.class);
        ApiModel comment = clas.getAnnotation(ApiModel.class);

        //获取表注释
        if (ObjectUtils.isNotEmpty(comment) && StringUtils.isNotBlank(comment.value()))
            CommentMap.put(table.value(), comment.value());


        List<Object> allFieldList = getAllFields(clas);

        // 表主键 字段排在第一位
        Map<String, CreateTableParam> idKey = this.getAllFieldMap(allFieldList);
        idKey.forEach((k, v) -> {
            if (v.isFieldIsKey())
                idMap.put(table.value(), v);
        });

        // 1. 用于存表的全部字段
        if (CollectionUtils.isEmpty(allFieldList)) {
            log.warn("扫描model发现" + clas.getName() + "没有建表字段请检查！");
            return;
        }


        // 先查该表是否以存在
        int exist = createMysqlTablesMapper.findTableCountByTableName(table.value());

        // 不存在时
        if (exist == 0) {
            baseTableMap.get(Constants.NEW_TABLE_MAP).put(table.value(), allFieldList);
            baseTableMap.get(Constants.ADDINDEX_TABLE_MAP).put(table.value(), getAddIndexList(null, allFieldList));
            baseTableMap.get(Constants.ADDUNIQUE_TABLE_MAP).put(table.value(), getAddUniqueList(null, allFieldList));
            return;
        }

        // 已存在时理论上做修改的操作，这里查出该表的结构
        List<SysMysqlColumns> tableColumnList = createMysqlTablesMapper.findTableEnsembleByTableName(table.value());

        // 从sysColumns中取出我们需要比较的列的List
        // 先取出name用来筛选出增加和删除的字段
        List<String> columnNames = ClassTools.getPropertyValueList(tableColumnList, SysMysqlColumns.COLUMN_NAME_KEY);

        // 验证对比从model中解析的allFieldList与从数据库查出来的columnList
        // 2. 找出增加的字段
        List<Object> addFieldList = getAddFieldList(table, allFieldList, columnNames);

        // 3. 找出删除的字段
        List<Object> removeFieldList = getRemoveFieldList(table, columnNames, allFieldList);

        // 4. 找出更新的字段
        List<Object> modifyFieldList = getModifyFieldList(table, columnNames, tableColumnList, allFieldList);

        // 5. 找出需要删除主键的字段
        List<Object> dropKeyFieldList = getDropKeyFieldList(table, columnNames, tableColumnList, allFieldList);

        // 查询当前表中全部的索引和唯一约束
        Set<String> allIndexAndUniqueNames = createMysqlTablesMapper.findTableIndexByTableName(table.value());

        // 6. 找出需要删除的索引和唯一约束
        List<Object> dropIndexAndUniqueFieldList = getDropIndexAndUniqueList(allIndexAndUniqueNames, allFieldList);

        // 7. 找出需要新增的索引
        List<Object> addIndexFieldList = getAddIndexList(allIndexAndUniqueNames, allFieldList);

        // 8. 找出需要新增的唯一约束
        List<Object> addUniqueFieldList = getAddUniqueList(allIndexAndUniqueNames, allFieldList);

        if (CollectionUtils.isNotEmpty(addFieldList))
            baseTableMap.get(Constants.ADD_TABLE_MAP).put(table.value(), addFieldList);

        if (CollectionUtils.isNotEmpty(removeFieldList))
            baseTableMap.get(Constants.REMOVE_TABLE_MAP).put(table.value(), removeFieldList);

        if (CollectionUtils.isNotEmpty(modifyFieldList))
            baseTableMap.get(Constants.MODIFY_TABLE_MAP).put(table.value(), modifyFieldList);

        if (CollectionUtils.isNotEmpty(dropKeyFieldList))
            baseTableMap.get(Constants.DROPKEY_TABLE_MAP).put(table.value(), dropKeyFieldList);

        if (CollectionUtils.isNotEmpty(dropIndexAndUniqueFieldList))
            baseTableMap.get(Constants.DROPINDEXANDUNIQUE_TABLE_MAP).put(table.value(), dropIndexAndUniqueFieldList);

        if (CollectionUtils.isNotEmpty(addIndexFieldList))
            baseTableMap.get(Constants.ADDINDEX_TABLE_MAP).put(table.value(), addIndexFieldList);

        if (CollectionUtils.isNotEmpty(addUniqueFieldList))
            baseTableMap.get(Constants.ADDUNIQUE_TABLE_MAP).put(table.value(), addUniqueFieldList);

    }

    /**
     * 找出需要新建的索引
     *
     * @param allIndexAndUniqueNames 当前数据库的索引很约束名
     * @param allFieldList           model中的所有字段
     * @return 需要新建的索引
     */
    private List<Object> getAddIndexList(Set<String> allIndexAndUniqueNames, List<Object> allFieldList) {
        List<Object> addIndexFieldList = Lists.newArrayList();
        if (null == allIndexAndUniqueNames) {
            allIndexAndUniqueNames = Sets.newHashSet();
        }
        for (Object obj : allFieldList) {
            CreateTableParam createTableParam = (CreateTableParam) obj;
            if (null != createTableParam.getFiledIndexName() && !allIndexAndUniqueNames.contains(createTableParam.getFiledIndexName())) {
                addIndexFieldList.add(createTableParam);
            }
        }
        return addIndexFieldList;
    }

    /**
     * 找出需要新建的唯一约束
     *
     * @param allIndexAndUniqueNames 当前数据库的索引很约束名
     * @param allFieldList           model中的所有字段
     * @return 需要新建的唯一约束
     */
    private List<Object> getAddUniqueList(Set<String> allIndexAndUniqueNames, List<Object> allFieldList) {
        List<Object> addUniqueFieldList = new ArrayList<Object>();
        if (null == allIndexAndUniqueNames) {
            allIndexAndUniqueNames = new HashSet<String>();
        }
        for (Object obj : allFieldList) {
            CreateTableParam createTableParam = (CreateTableParam) obj;
            if (null != createTableParam.getFiledUniqueName() && !allIndexAndUniqueNames.contains(createTableParam.getFiledUniqueName())) {
                addUniqueFieldList.add(createTableParam);
            }
        }
        return addUniqueFieldList;
    }

    /**
     * 找出需要删除的索引和唯一约束
     *
     * @param allIndexAndUniqueNames 当前数据库的索引很约束名
     * @param allFieldList           model中的所有字段
     * @return 需要删除的索引和唯一约束
     */
    private List<Object> getDropIndexAndUniqueList(Set<String> allIndexAndUniqueNames, List<Object> allFieldList) {
        List<Object> dropIndexAndUniqueFieldList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(allIndexAndUniqueNames)) {
            return dropIndexAndUniqueFieldList;
        }
        List<String> currentModelIndexAndUnique = new ArrayList<String>();
        for (Object obj : allFieldList) {
            CreateTableParam createTableParam = (CreateTableParam) obj;
            if (null != createTableParam.getFiledIndexName()) {
                currentModelIndexAndUnique.add(createTableParam.getFiledIndexName());
            }
            if (null != createTableParam.getFiledUniqueName()) {
                currentModelIndexAndUnique.add(createTableParam.getFiledUniqueName());
            }
        }
        for (String string : allIndexAndUniqueNames) {
            if (!currentModelIndexAndUnique.contains(string)) {
                dropIndexAndUniqueFieldList.add(string);
            }
        }
        return dropIndexAndUniqueFieldList;
    }

    /**
     * 返回需要删除主键的字段
     *
     * @param table           表
     * @param columnNames     数据库中的结构
     * @param tableColumnList 表结构
     * @param allFieldList    model中的所有字段
     * @return 需要删除主键的字段
     */
    private List<Object> getDropKeyFieldList(TableName table, List<String> columnNames, List<SysMysqlColumns> tableColumnList, List<Object> allFieldList) {
        Map<String, CreateTableParam> fieldMap = getAllFieldMap(allFieldList);
        List<Object> dropKeyFieldList = new ArrayList<Object>();
        for (SysMysqlColumns sysColumn : tableColumnList) {
            // 数据库中有该字段时
            CreateTableParam createTableParam = fieldMap.get(sysColumn.getColumn_name());
            if (createTableParam != null) {
                // 原本是主键，现在不是了，那么要去做删除主键的操作
                if ("PRI".equals(sysColumn.getColumn_key()) && !createTableParam.isFieldIsKey()) {
                    dropKeyFieldList.add(createTableParam);
                }

            }
        }
        return dropKeyFieldList;
    }

    /**
     * 根据数据库中表的结构和model中表的结构对比找出修改类型默认值等属性的字段
     *
     * @param tableColumnList 表结构
     * @param allFieldList    model中的所有字段
     * @return 需要修改的字段
     */
    private List<Object> getModifyFieldList(TableName table, List<String> columnNames, List<SysMysqlColumns> tableColumnList, List<Object> allFieldList) {
        Map<String, CreateTableParam> fieldMap = getAllFieldMap(allFieldList);
        List<Object> modifyFieldList = Lists.newArrayList();
        for (SysMysqlColumns sysColumn : tableColumnList) {
            // 数据库中有该字段时，验证是否有更新
            CreateTableParam createTableParam = fieldMap.get(sysColumn.getColumn_name());
            if (createTableParam != null) {
                // 该复制操作时为了解决multiple primary key defined的同时又不会drop primary key
                CreateTableParam modifyTableParam = createTableParam.clone();
                // 1.验证主键
                // 原本不是主键，现在变成了主键，那么要去做更新
                if (!"PRI".equals(sysColumn.getColumn_key()) && createTableParam.isFieldIsKey()) {
                    modifyFieldList.add(modifyTableParam);
                    continue;
                }
                // 原本是主键，现在依然主键，坚决不能在alter语句后加primary key，否则会报multiple primary
                // key defined
                if ("PRI".equals(sysColumn.getColumn_key()) && createTableParam.isFieldIsKey()) {
                    modifyTableParam.setFieldIsKey(false);
                }
                // 2.验证类型
                if (!sysColumn.getData_type().toLowerCase().equals(createTableParam.getFieldType().toLowerCase())) {
                    System.err.println(createTableParam.getFieldName() + "Column type modified:" + sysColumn.getData_type().toLowerCase() + " → " + createTableParam.getFieldType().toLowerCase());
                    modifyFieldList.add(modifyTableParam);
                    continue;
                }
                // 3.验证长度个小数点位数
                // 4.验证小数点位数
                int length = (Integer) mySqlTypeAndLengthMap.get(createTableParam.getFieldType().toLowerCase());
                String typeAndLength = createTableParam.getFieldType().toLowerCase();
                if (length == 1) {
                    // 拼接出类型加长度，比如varchar(1)
                    typeAndLength = typeAndLength + "(" + createTableParam.getFieldLength() + ")";
                } else if (length == 2) {
                    // 拼接出类型加长度，比如varchar(1)
                    typeAndLength = typeAndLength + "(" + createTableParam.getFieldLength() + "," + createTableParam.getFieldDecimalLength() + ")";
                }

                // 判断类型+长度是否相同
                if (!typeAndLength.equals(StringUtils.replace(sysColumn.getColumn_type().toLowerCase(), " unsigned zerofill", ""))) {
                    System.err.println(createTableParam.getFieldName() + " :Column type and length modified：" + createTableParam.getFieldType().toLowerCase() + " → " + typeAndLength);
                    modifyFieldList.add(modifyTableParam);
                    continue;
                }
                // 5.验证自增
                if ("auto_increment".equals(sysColumn.getExtra()) && !createTableParam.isFieldIsAutoIncrement()) {
                    modifyFieldList.add(modifyTableParam);
                    continue;
                }

                // 7.验证是否可以为null(主键不参与是否为null的更新)
                if (sysColumn.getIs_nullable().equals("NO") && !createTableParam.isFieldIsKey()) {
                    if (createTableParam.isFieldIsNull()) {
                        // 一个是可以一个是不可用，所以需要更新该字段
                        modifyFieldList.add(modifyTableParam);
                        continue;
                    }
                } else if (sysColumn.getIs_nullable().equals("YES") && !createTableParam.isFieldIsKey()) {
                    if (!createTableParam.isFieldIsNull()) {
                        // 一个是可以一个是不可用，所以需要更新该字段
                        modifyFieldList.add(modifyTableParam);
                        continue;
                    }
                }

            }
        }
        return modifyFieldList;
    }

    /**
     * 将allFieldList转换为Map结构
     */
    private Map<String, CreateTableParam> getAllFieldMap(List<Object> allFieldList) {
        Map<String, CreateTableParam> fieldMap = Maps.newHashMap();
        for (Object obj : allFieldList) {
            CreateTableParam createTableParam = (CreateTableParam) obj;
            fieldMap.put(createTableParam.getFieldName(), createTableParam);
        }
        return fieldMap;
    }

    /**
     * 根据数据库中表的结构和model中表的结构对比找出删除的字段
     *
     * @param table        表
     * @param columnNames  数据库中的结构
     * @param allFieldList model中的所有字段
     */
    private List<Object> getRemoveFieldList(TableName table, List<String> columnNames, List<Object> allFieldList) {
        Map<String, CreateTableParam> fieldMap = getAllFieldMap(allFieldList);
        // 用于存删除的字段
        List<Object> removeFieldList = new ArrayList<Object>();
        for (String fieldNm : columnNames) {
            // 判断该字段在新的model结构中是否存在
            if (fieldMap.get(fieldNm) == null) {
                // 不存在，做删除处理
                removeFieldList.add(fieldNm);
            }
        }
        return removeFieldList;
    }

    /**
     * 根据数据库中表的结构和model中表的结构对比找出新增的字段
     *
     * @param table        表
     * @param allFieldList model中的所有字段
     * @param columnNames  数据库中的结构
     * @return 新增的字段
     */
    private List<Object> getAddFieldList(TableName table, List<Object> allFieldList, List<String> columnNames) {
        List<Object> addFieldList = Lists.newArrayList();
        for (Object obj : allFieldList) {
            CreateTableParam createTableParam = (CreateTableParam) obj;
            // 循环新的model中的字段，判断是否在数据库中已经存在
            if (!columnNames.contains(createTableParam.getFieldName())) {
                // 不存在，表示要在数据库中增加该字段
                addFieldList.add(obj);
            }
        }
        return addFieldList;
    }

    /**
     * 迭代出所有model的所有fields
     *
     * @param clas 准备做为创建表依据的class
     * @return 表的全部字段
     */
    private List<Object> getAllFields(Class<?> clas) {
        List<Object> fieldList = Lists.newArrayList();
        Field[] fields = clas.getDeclaredFields();

        // 判断是否有父类，如果有拉取父类的field，这里只支持多层继承
        fields = recursionParents(clas, fields);

        for (Field field : fields) {

            if (field.getName().equals("serialVersionUID"))
                continue;
            CreateTableParam param = new CreateTableParam();
            param.setFileTypeLength(1);
            param.setFieldName(Hump2underline.build(field.getName()));

            TableId tableId = field.getAnnotation(TableId.class);

            TableField fieldName = field.getAnnotation(TableField.class);

            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);

            if (fieldName != null && !fieldName.exist())
                continue;

            if (ObjectUtils.isNotEmpty(apiModelProperty))// 注释
                param.setComment(apiModelProperty.value());

            if (tableId != null) {// 主键

                if (tableId.value() != null)
                    param.setFieldName(tableId.value());
                //主键设置索引
                param.setFieldIsUnique(true);
                param.setFiledUniqueName("FK_" + tableId.value().toUpperCase(Locale.ROOT) + "_" + RandomStringUtils.randomAlphabetic(20));
                param.setFiledUniqueValue(Lists.newArrayList(tableId.value()));

                param.setFieldIsKey(true);
                param.setFieldIsNull(false);
                boolean isAuto = tableId.type().equals(IdType.AUTO);
                boolean isASSIGN_ID = tableId.type().equals(IdType.ASSIGN_ID);

                param.setFieldIsAutoIncrement(isAuto);
                if (isAuto) {
                    param.setFieldType(MySqlTypeConstant.INT);
                    param.setFieldLength(11);
                    param.setKey(true);
                } else if (isASSIGN_ID) {
                    param.setFieldType(MySqlTypeConstant.BIGINT);
                    param.setFieldLength(20);
                    param.setKey(true);
                } else {
                    param.setFieldLength(36);
                    param.setFieldType(MySqlTypeConstant.VARCHAR);
                }

            } else {// 非主键
                String type = field.getType().getName();

                if (fieldName != null && fieldName.value() != null)
                    param.setFieldName(fieldName.value());
                else
                    param.setFieldName(Hump2underline.build(field.getName()));

                switch (type) {
                    case "java.lang.Long":
                        param.setAddPrama(true);
                        param.setFieldLength(20);
                        param.setFieldType(MySqlTypeConstant.BIGINT);
                        break;

                    case "java.lang.Integer":
                        param.setFieldLength(11);
                        param.setAddPrama(true);
                        param.setFieldType(MySqlTypeConstant.INT);
                        break;

                    case "java.util.Date":
                    case "java.time.LocalDateTime":
                        param.setFieldType(MySqlTypeConstant.DATETIME);
                        break;
                    case "java.math.BigDecimal":
                        param.setFieldLength(12);
                        param.setFileTypeLength(2);
                        param.setFieldDecimalLength(2);
                        param.setFieldType(MySqlTypeConstant.DECIMAL);
                        break;
                    case "java.sql.Blob":
                        param.setFileTypeLength(0);
                        param.setFieldType(MySqlTypeConstant.LONGBLOB);
                        break;
                    default: // 默认是String，枚举、自定义实体
                        param.setFieldLength(255);
                        param.setFieldType(MySqlTypeConstant.VARCHAR);
                        break;

                }
            }
            fieldList.add(param);
        }
        return fieldList;
    }

    /**
     * 递归扫描父类的fields
     *
     * @param clas   类
     * @param fields 属性
     */
    @SuppressWarnings("rawtypes")
    private Field[] recursionParents(Class<?> clas, Field[] fields) {
        if (clas.getSuperclass() != null) {
            Class clsSup = clas.getSuperclass();
            List<Field> fieldList = new ArrayList<Field>();
            fieldList.addAll(Arrays.asList(fields));
            fieldList.addAll(Arrays.asList(clsSup.getDeclaredFields()));
            fields = new Field[fieldList.size()];
            int i = 0;
            for (Object field : fieldList.toArray()) {
                fields[i] = (Field) field;
                i++;
            }
            fields = recursionParents(clsSup, fields);
        }
        return fields;
    }

    /**
     * 根据传入的map创建或修改表结构
     *
     * @param baseTableMap 操作sql的数据结构
     */
    private void createOrModifyTableConstruct(Map<String, Map<String, List<Object>>> baseTableMap) {
        // 1. 创建表
        createTableByMap(baseTableMap.get(Constants.NEW_TABLE_MAP));
        // 2. 删除要变更主键的表的原来的字段的主键
        dropFieldsKeyByMap(baseTableMap.get(Constants.DROPKEY_TABLE_MAP));
        // 3. 添加新的字段
        addFieldsByMap(baseTableMap.get(Constants.ADD_TABLE_MAP));
        // 4. 删除字段
        removeFieldsByMap(baseTableMap.get(Constants.REMOVE_TABLE_MAP));
        // 5. 修改字段类型等
        modifyFieldsByMap(baseTableMap.get(Constants.MODIFY_TABLE_MAP));
        // 6. 删除索引和约束
        dropIndexAndUniqueByMap(baseTableMap.get(Constants.DROPINDEXANDUNIQUE_TABLE_MAP));
        // 7. 创建索引
//        addIndexByMap(baseTableMap.get(Constants.ADDINDEX_TABLE_MAP));
        // 8. 创建约束
//        addUniqueByMap(baseTableMap.get(Constants.ADDUNIQUE_TABLE_MAP));

//        modifyComment(CommentMap);

    }

    /**
     * @author: weilai
     * @date: 2021/5/19    下午6:24
     * @desc: 修改数表注释
     **/
    private void modifyComment(Map<String, String> map) {
        for (Entry<String, String> entry : map.entrySet())
            createMysqlTablesMapper.modifyTableCommon(entry.getKey(), entry.getValue());
    }

    private void setIdFirst(Map<String, CreateTableParam> map) {
        map.forEach((k, v) -> createMysqlTablesMapper.setIdFirst(k, v.getFieldName(), v.getFieldType(), v.getFieldLength()));
    }

    /**
     * 根据map结构删除索引和唯一约束
     *
     * @param dropIndexAndUniqueMap 用于删除索引和唯一约束
     */
    private void dropIndexAndUniqueByMap(Map<String, List<Object>> dropIndexAndUniqueMap) {
        if (dropIndexAndUniqueMap.size() > 0) {
            for (Entry<String, List<Object>> entry : dropIndexAndUniqueMap.entrySet()) {
                for (Object obj : entry.getValue()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(entry.getKey(), obj);
                    log.info("---------------------------------------------------------------------------------------");
                    log.info("开始删除表" + entry.getKey() + "中的索引" + obj);
                    try {
                        createMysqlTablesMapper.dorpTabelIndex(map);
                    } catch (Exception e) {
                        System.err.printf("删除索引时出错：" + e.getMessage());
                    }
                    log.info("完成删除表" + entry.getKey() + "中的索引" + obj);
                    log.info("---------------------------------------------------------------------------------------");
                }
            }
        }
    }

    /**
     * 根据map结构修改表中的字段类型等
     *
     * @param modifyTableMap 用于存需要更新字段类型等的表名+结构
     */
    private void modifyFieldsByMap(Map<String, List<Object>> modifyTableMap) {

        for (Entry<String, List<Object>> entry : modifyTableMap.entrySet()) {
            for (Object obj : entry.getValue()) {
                Map<String, Object> map = Maps.newHashMap();
                map.put(entry.getKey(), obj);
                CreateTableParam fieldProperties = (CreateTableParam) obj;
                log.info("---------------------------------------------------------------------------------------");
                log.info("开始修改表" + entry.getKey() + "中的字段" + fieldProperties.getFieldName());
                try {
                    createMysqlTablesMapper.modifyTableField(map);
                } catch (Exception e) {
                    System.err.printf("修改字段出错：" + e.getMessage());
                }

                log.info("完成修改表" + entry.getKey() + "中的字段" + fieldProperties.getFieldName());
                log.info("---------------------------------------------------------------------------------------");
            }
        }

    }

    /**
     * 根据map结构删除表中的字段
     *
     * @param removeTableMap 用于存需要删除字段的表名+结构
     */
    private void removeFieldsByMap(Map<String, List<Object>> removeTableMap) {
        // 做删除字段操作
        if (removeTableMap.size() > 0) {
            for (Entry<String, List<Object>> entry : removeTableMap.entrySet()) {
                for (Object obj : entry.getValue()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(entry.getKey(), obj);
                    String fieldName = (String) obj;
                    log.info("---------------------------------------------------------------------------------------");
                    log.info("开始删除表" + entry.getKey() + "中的字段" + fieldName);
                    try {
                        createMysqlTablesMapper.removeTableField(map);
                    } catch (Exception e) {
                        System.err.printf("删除表出错：" + e.getMessage());
                    }

                    log.info("完成删除表" + entry.getKey() + "中的字段" + fieldName);
                    log.info("---------------------------------------------------------------------------------------");
                }
            }
        }
    }

    /**
     * 根据map结构对表中添加新的字段
     *
     * @param addTableMap 用于存需要增加字段的表名+结构
     */
    private void addFieldsByMap(Map<String, List<Object>> addTableMap) {
        // 做增加字段操作
        if (addTableMap.size() > 0) {
            for (Entry<String, List<Object>> entry : addTableMap.entrySet()) {
                for (Object obj : entry.getValue()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(entry.getKey(), obj);
                    CreateTableParam fieldProperties = (CreateTableParam) obj;
                    log.info("---------------------------------------------------------------------------------------");
                    log.info("开始为表" + entry.getKey() + "增加字段" + fieldProperties.getFieldName());
                    try {
                        createMysqlTablesMapper.addTableField(map);
                    } catch (Exception e) {
                        System.err.printf("增加字段出错：" + e.getMessage());
                    }

                    log.info("完成为表" + entry.getKey() + "增加字段" + fieldProperties.getFieldName());
                    log.info("---------------------------------------------------------------------------------------");
                }
            }
        }
    }

    /**
     * 根据map结构删除要变更表中字段的主键
     *
     * @param dropKeyTableMap 用于存需要删除主键的表名+结构
     */
    private void dropFieldsKeyByMap(Map<String, List<Object>> dropKeyTableMap) {
        // 先去做删除主键的操作，这步操作必须在增加和修改字段之前！
        if (dropKeyTableMap.size() > 0) {
            for (Entry<String, List<Object>> entry : dropKeyTableMap.entrySet()) {
                for (Object obj : entry.getValue()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(entry.getKey(), obj);
                    CreateTableParam fieldProperties = (CreateTableParam) obj;
                    log.info("---------------------------------------------------------------------------------------");
                    log.info("开始为表" + entry.getKey() + "删除主键" + fieldProperties.getFieldName());
                    try {
                        createMysqlTablesMapper.dropKeyTableField(map);
                    } catch (Exception e) {
                        System.err.printf("删除主键出错：" + e.getMessage());
                    }

                    log.info("完成为表" + entry.getKey() + "删除主键" + fieldProperties.getFieldName());
                    log.info("---------------------------------------------------------------------------------------");
                }
            }
        }
    }

    /**
     * 根据map结构创建表
     *
     * @param newTableMap 用于存需要创建的表名+结构
     */
    private void createTableByMap(Map<String, List<Object>> newTableMap) {
        // 1、做创建表操作
        if (MapUtils.isNotEmpty(newTableMap)) {
            for (Entry<String, List<Object>> entry : newTableMap.entrySet()) {
                String tableName = entry.getKey();
                Map<String, List<Object>> map = Maps.newHashMap();
                map.put(tableName, entry.getValue());
                log.info("---------------------------------------------------------------------------------------");
                try {
                    createMysqlTablesMapper.createTable(map, CommentMap.get(tableName));
                } catch (Exception e) {
                    System.err.printf("创建表出错：" + e.getMessage());
                }

                log.info("完成创建表：" + entry.getKey());
                log.info("---------------------------------------------------------------------------------------");
            }
            //2、创建表注释
            modifyComment(CommentMap);
            //3、设置表主键字段 排在第一位
            this.setIdFirst(idMap);
        }
    }

    /**
     * 获取Mysql的类型，以及类型需要设置几个长度，这里构建成map的样式
     * 构建Map(字段名(小写),需要设置几个长度(0表示不需要设置，1表示需要设置一个，2表示需要设置两个))
     */
    public static Map<String, Object> mySqlTypeAndLengthMap() {
        Field[] fields = MySqlTypeConstant.class.getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Field field : fields) {
            LengthCount lengthCount = field.getAnnotation(LengthCount.class);
            map.put(field.getName().toLowerCase(), lengthCount.Length());
        }
        return map;
    }
}
