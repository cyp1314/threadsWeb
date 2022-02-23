package com.example.threadsweb.service.impl;

import com.example.threadsweb.entity.School;
import com.example.threadsweb.mapper.SchoolMapper;
import com.example.threadsweb.service.ISchoolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cyp
 * @since 2022-02-23
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

    @Override
    public School MygetById (int i) {
        return this.baseMapper.MygetById(i);
    }
}
