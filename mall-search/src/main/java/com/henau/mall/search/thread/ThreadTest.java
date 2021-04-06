package com.henau.mall.search.thread;

import org.apache.ibatis.executor.ExecutorException;

import java.util.concurrent.*;

public class ThreadTest {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main....start....");
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).whenComplete((res,excption) -> {
            //虽然能得到异常信息，但是没法修改返回数据。
            System.out.println("异步任务成功完成了...结果是："+res+"；异常是：" + excption);
        }).exceptionally(throwable -> {
            //可以感知异常，同时返回默认值。
            return 10;
        });
        Integer integer = future.get();
        System.out.println("main....end...." + integer);
    }

    public static void thread(String[] args) {
        System.out.println("main....start....");
        /**
         * 1).继承Thread
         *      Thread01 thread = new Thread();
         *      thread.start();//启动线程
         *
         * 2）、实现Runable接口
         *      Runable01 renable = new Runable01();
         *      new Thread(renable01).start();
         *
         * 3).实现Callable接口 + FutureTask （可以拿到返回结果，可以处理异常）
         *      FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
         *      new Thread(futureTask).start();
         *      //阻塞等待整个线程执行完成，获取返回结果
         *      Integer integer = futureTask.get();
         *
         * 4).线程池[ExecutorService]
         *      给线程池直接提交任务。
         *      1.创建：
         *          1).Executors
         *          2).new ThreadPoolExecutor()
         *
         *          Future可以获取到异步结果
         *
         * 区别：
         *      1、2不能得到返回值。3可以得到返回值
         *      2、2、3都不能控制资源
         *      4可以控制资源，性能稳定。
         *
         */
        //我们以后在业务代码里面，以上三种启动线程的方式都不用。【将所有的多线程异步任务都交给我线程池执行】
        //new Thread（（）->System.out.pringln("hello")).start();

        //当前系统中池只有一两个，每个异步任务，提交给线程池让他自己去执行就行。
        /**
         * 七大参数
         *   int corePoolSize[5],核心线程数[一直存在，除非设置了（allowCoreThreadTimeOut]；线程池，创建好以后就准备就绪的线程数量，就等待来就受异步任务去执行,相当于new了5个Thread Thread thread = new Thread();
         *   int maximumPoolSize,最大线程数量，控制资源并发
         *   long keepAliveTime,存活时间，如果当前正在运行的线程数量大于核心数量core，释放空闲的线程，只要线程空闲大于指定的存活时间keepAliveTime
         *   TimeUnit unit,实践单位
         *   BlockingQueue<Runnable> workQueue,阻塞队列。如果任务有很多，就会将目前多的任务放在队列里面。只要有线程空闲，就会去队列里面去除新的任务继续执行。
         *   ThreadFactory threadFactory,现成的创建工厂。
         *   RejectedExecutionHandler handler, 如果队列满了，按照我们指定的拒绝策略拒绝执行任务
         *
         *   工作顺序：
         *   1）、线程池创建，准备好core数量的核心线程，准备接受任务
         *   2）、1.1、core满了，就将再进来的任务放入阻塞队列中，空闲的core就会自己去阻塞队列获取任务执行
         *       1.2、阻塞队列满了，就直接开新线程执行，最大只能开到max指定的数量
         *       1.3、max满了就用拒绝策略RejectedExecutionHandler拒绝任务；
         *       1.4、max都执行完成，有很多空闲，在指定的时间keepAliveTime以后，释放max-core这些线程
         *
         * 一个线程池core 7， max 20， queue 50， 100并发进来怎么分配的；
         * 7个会立即得到执行，50个会进入队列，再开13个进行执行。剩下的30个就使用拒绝策略，一般呢我们使用的是丢弃策略、
         * 如果不想抛弃还要执行，CallerRunsPolicy;
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
        100,100,TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000000),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

//        Executors.newCachedThreadPool(); core是0,所有都可以回收
//        Executors.newFixedThreadPool() 固定大小的，core=max，都不可回收
//        Executors.newScheduledThreadPool() 定时任务的线程池
//        Executors.newSingleThreadExecutor() 单线程的线程池，后台从队列里面获取任务，挨个执行

        System.out.println("main....end....");
    }

    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("运行结果：" +i);
        }
    }
}
