# threadsWeb
线程池分担查询接口压力


### 案例一
不涉及到事务的查询方法，如果报表统计查询功能，或者一个接口多次查询数据。可以采用多线程查询数据，最后统一返回数据。

```
@Configuration
@EnableAsync
public class ExecutorConfig {
    @Bean
    public ThreadPoolTaskExecutor myThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();
//        int core = 1;
        //设置核心线程数
        executor.setCorePoolSize(core);
        //设置最大线程数
        executor.setMaxPoolSize(core * 2 + 1);
        //除核心线程外的线程存活时间
        executor.setKeepAliveSeconds(3);
        //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(40);
        //线程名称前缀
        executor.setThreadNamePrefix("thread-execute");
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
```


```
@Autowired
    private IStudentService studentService;
    @Autowired
    private ISchoolService schoolService;

    @Autowired
    ThreadPoolTaskExecutor taskExe;

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
```

