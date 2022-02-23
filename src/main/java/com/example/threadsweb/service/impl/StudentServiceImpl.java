package com.example.threadsweb.service.impl;

import com.example.threadsweb.entity.Student;
import com.example.threadsweb.mapper.StudentMapper;
import com.example.threadsweb.service.IStudentService;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
