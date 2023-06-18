package ssp.javasp.sp.fundamental.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ThreadHandler {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		//기본 쓰레드 활용
//		testBaseThread();
//		
//		//쓰레드 Pool 활용한 쓰레드 활용
//		testBaseThreadWithPool();
		
		// callable로 비동기 결과 받기        
//		testCallableThread();
		// 쓰레드 Pool 활용한 callable로 비동기 결과 받기
        testCallableThreadWithPool();
	}
	
	public static void testBaseThread() throws InterruptedException {
		
		List<Thread> actionList = new ArrayList<Thread>();
		for(int i=0; i<10; i++) {
			ThreadAction asyncAction = new ThreadAction();
			actionList.add(asyncAction);
			
			asyncAction.start();
		}

		//스레드 종료 시까지 대기 - 앞 실행했던 Thread가 종료 된 후에 main스레드 동작이 있을 때
		for(Thread action : actionList) {
			action.join();
		}

		//메인 스레드 작업
		System.out.println(String.format("[%s] : Main Thread End", Thread.currentThread().getName()));
	}
	
	public static void testBaseThreadWithPool() throws InterruptedException {
		//ExecutorService executorService = Executors.newFixedThreadPool(10);
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for(int i=0; i<10; i++) {
			ThreadAction asyncAction = new ThreadAction();
			executorService.submit(asyncAction);
		}
		
		//스레드 종료 시까지 대기 - 앞 실행했던 Thread가 종료 된 후에 main스레드 동작이 있을 때
		executorService.shutdown();
		System.out.println("ExecutorService.shutdowned");
		//executorService.awaitTermination(10, TimeUnit.SECONDS);
		while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Not yet. Still waiting for termination");
        }
		
		//메인 스레드 작업
		System.out.println(String.format("[%s] : Main Thread End With Pool", Thread.currentThread().getName()));
		
		//executorService.awaitTermination(10, TimeUnit.SECONDS);
		while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Not yet. Still waiting for termination");
        }
	}
	
	
	
	public static void testCallableThread() throws InterruptedException, ExecutionException {
		
		
		FutureTask<String>[] futureTasks = new FutureTask[10];
		
		List<Thread> actionList = new ArrayList<Thread>();
		for(int i=0; i<10; i++) {
			CallableThreadAction callableThread = new CallableThreadAction();
			futureTasks[i] = new FutureTask<>(callableThread);
			Thread asyncAction = new Thread(futureTasks[i]);
			actionList.add(asyncAction);
			
			asyncAction.start();
		}

		//스레드 종료 시까지 대기 - 앞 실행했던 Thread가 종료 된 후에 main스레드 동작이 있을 때
//		for(Thread action : actionList) {
//			action.join();
//		}
		
		for(FutureTask<String> futureTask : futureTasks) {
			//if(futureTask.isDone())
				System.out.println("In Main : " + futureTask.get());
		}

		//메인 스레드 작업
		System.out.println(String.format("[%s] : Main Thread End", Thread.currentThread().getName()));
	}
	
	public static void testCallableThreadWithPool() throws InterruptedException, ExecutionException {	
		//ExecutorService executorService = Executors.newFixedThreadPool(10);
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		Future<String>[] futures = new Future[10];
		FutureTask<String>[] futureTasks = new FutureTask[10];
		for(int i=0; i<10; i++) {
			CallableThreadAction asyncAction = new CallableThreadAction();
			
			// future vs futuretask : Callable Submit이면 future로 결과값 가져오기
			futures[i] = executorService.submit(asyncAction);
			// future vs futuretask : FutureTask Submit이면 FutureTask로 결과값 가져오기, Future로 가져오면 null
			futureTasks[i] = new FutureTask<>(asyncAction);
			executorService.submit(futureTasks[i]);
		}
		
		//스레드 종료 시까지 대기 - 앞 실행했던 Thread가 종료 된 후에 main스레드 동작이 있을 때
		executorService.shutdown();
		System.out.println("ExecutorService.shutdowned");
		//executorService.awaitTermination(10, TimeUnit.SECONDS);
		while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Not yet. Still waiting for termination");
        }
		
		//메인 스레드 작업
		System.out.println(String.format("[%s] : Main Thread End With Pool", Thread.currentThread().getName()));
		
		// future vs futuretask : Callable Submit이면 future로 결과값 가져오기
		for(Future future : futures) {
			System.out.println("In Main Future : " + future.get());
		}
		// future vs futuretask : FutureTask Submit이면 FutureTask로 결과값 가져오기
		for(FutureTask future : futureTasks) {
			System.out.println("In Main FutureTask : " + future.get());
		}
	}

}

class ThreadAction extends Thread {
	
	public ThreadAction() {
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1500);
			System.out.println(String.format("[%s] : Action Thread End", Thread.currentThread().getName()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class CallableThreadAction implements Callable<String> {
	
	public CallableThreadAction() {
	}

	@Override
	public String call() throws Exception {
		Thread.sleep(1500);
		String returnValue = String.format("[%s] : Callable Action Thread End", Thread.currentThread().getName());
		System.out.println(returnValue);
		
		return returnValue;
	}
} 
