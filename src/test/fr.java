package test;
import org.junit.Test;
public class fr {

	public static void main(String arg[]) throws Exception {
        Thread myThread1 = new MyThread();     // 创建一个新的线程  myThread1  此线程进入新建状态
        myThread1.setName("线程1");
        Thread myThread2 = new MyThread();     // 创建一个新的线程 myThread2 此线程进入新建状态
        myThread2.setName("线程2");
        myThread1.start();                     // 调用start()方法使得线程进入就绪状态
        myThread2.start();                     // 调用start()方法使得线程进入就绪状态
	}
	
	@Test
	public void getUUID(){
		for(int i=0;i<252;i++)
		System.out.println(java.util.UUID.randomUUID().toString().replace("-", ""));
	}
}

 class MyThread extends Thread {
	      
	      private int i = 0;
	  
	      @Override
	      public void run() {
	          for (;;) {
		          System.out.println(Thread.currentThread().getName() + " " + i);
	          }
	     }
 }
	 