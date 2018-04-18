package cn.com.studyshop.lock.demo;

import java.util.concurrent.CountDownLatch;

import cn.com.studyshop.api.OrderService;
import cn.com.studyshop.distribute.service.OrderServiceDistributedLock;
//import cn.com.studyshop.distribute.service.OrderServiceLock;

/**
 * 
 * @author LIU
 *
 */
public class CountDownLatchDemo {

	public static void main(String[] args) {
		// 并发数
		int currs = 100;
		OrderService orderService = new OrderServiceDistributedLock();
		CountDownLatch cdl = new CountDownLatch(currs);

		for (int i = 0; i < currs; i++) {
			new Thread(new Runnable() {
				// OrderService orderService = new OrderServiceLock();

				@Override
				public void run() {

					cdl.countDown();
					try {
						cdl.await();
						Long startCurrent = System.currentTimeMillis();
						System.out.println("--------------" + startCurrent);
						orderService.createOrderNO();
						Long endTime = System.currentTimeMillis();
						System.out.println("costTime:" + (endTime - startCurrent));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();
		}

	}
}
