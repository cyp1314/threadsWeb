package com.example.threadsweb.mapper;

import com.example.threadsweb.entity.School;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cyp
 * @since 2022-02-23
 */
public interface SchoolMapper extends BaseMapper<School> {

    @MapKey("name")
    Map<String, Object> s1();

    int insertMy(School school);

    School MygetById (@Param("i") int i);
}
