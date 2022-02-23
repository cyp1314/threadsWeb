package com.example.threadsweb.controller;
import java.time.LocalDateTime;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.threadsweb.config.ExecutorConfig;
import com.example.threadsweb.entity.School;
import com.example.threadsweb.entity.Student;
import com.example.threadsweb.mapper.SchoolMapper;
import com.example.threadsweb.service.ISchoolService;
import com.example.threadsweb.service.IStudentService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RestController
public class AllController {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private ISchoolService schoolService;

    @Autowired
    ThreadPoolTaskExecutor taskExe;

    @Autowired
            @Qualifier()
    SchoolMapper schoolMapper;

    @GetMapping("/list")
    public Map list(){
        long start = System.currentTimeMillis();
        Map map = new HashMap();

        List<FutureTask<List>> taskList=new ArrayList<FutureTask<List>>();

        FutureTask<List> futureTask=new FutureTask<List>(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return studentService.page(new Page<Student>(1000,20)).getRecords();
            }
        });
        //执行的结果装回原来的FutureTask中,后续直接遍历集合taskList来获取结果即可
        taskList.add(futureTask);
        taskExe.submit(futureTask);

        FutureTask<List> futureTask2=new FutureTask<List>(new Callable<List>() {
            @Override
            public List call() throws Exception {
                return schoolService.page(new Page<School>(1000,20)).getRecords();
            }
        });
        //执行的结果装回原来的FutureTask中,后续直接遍历集合taskList来获取结果即可
        taskList.add(futureTask2);
        taskExe.submit(futureTask2);


        //获取结果
        try{
            map.put("data1",taskList.get(0).get());
            map.put("data2",taskList.get(1).get());
        } catch (InterruptedException e) {
            System.out.println("线程执行被中断");
        } catch (ExecutionException e) {
            System.out.println("线程执行出现异常");
        }
        long end = System.currentTimeMillis();

        BigInteger cc = new BigInteger(String.valueOf(end)).subtract(new BigInteger(String.valueOf(start)));
        System.out.println(cc);
        return map;
    }

    @GetMapping("/list2")
    public Map list2(){
        long start = System.currentTimeMillis();
        Map map = new HashMap();
        List<Student> list = studentService.page(new Page<>(1000,20)).getRecords();
        List<School> list1 = schoolService.page(new Page<>(1000,20)).getRecords();
        map.put("data1",list);
        map.put("data2",list1);
        long end = System.currentTimeMillis();

        BigInteger cc = new BigInteger(String.valueOf(end)).subtract(new BigInteger(String.valueOf(start)));
        System.out.println(cc);
        return map;
    }

    @GetMapping("/insert")
    public String insert(){
        for (int ij = 0; ij < 100; ij++) {
            List<Student> students = new ArrayList<>();
            List<School> schools = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                Student student = new Student();
                student.setName(UUID.randomUUID().toString());
                student.setAddress(UUID.randomUUID().toString());
                student.setSex(UUID.randomUUID().toString());
                student.setCreateTime(LocalDateTime.now());
                student.setCreateUser(UUID.randomUUID().toString());
                student.setUpdateTime(LocalDateTime.now());
                student.setUpdateUser(LocalDateTime.now());
                student.setDeleted("1");
                students.add(student);

                School school = new School();
                school.setName(UUID.randomUUID().toString());
                school.setAddress(UUID.randomUUID().toString());
                school.setDeleted("1");
                school.setCreateUser("1");
                school.setCreateTime(LocalDateTime.now());
                school.setUpdateUser(LocalDateTime.now());
                school.setUpdateTime(LocalDateTime.now());
                schools.add(school);
            }
            studentService.saveBatch(students);
            schoolService.saveBatch(schools);
        }
        return "hello";
    }

    @GetMapping("/test1")
    public Map test1(){
        Map<String, Object> map = schoolMapper.s1();
        return map;
    }

    @GetMapping("/test2")
    public int test2(){
        School school = new School();
        int id = schoolMapper.insertMy(school);
        System.out.println(school);
        return id;
    }

    @GetMapping("/test3")
    public Object selectByid(){
        School s = schoolService.MygetById(111);
        return s;
    }
}
