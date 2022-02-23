package com.example.threadsweb.service;

import com.example.threadsweb.entity.School;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cyp
 * @since 2022-02-23
 */
public interface ISchoolService extends IService<School> {

    School MygetById (int i);
}
