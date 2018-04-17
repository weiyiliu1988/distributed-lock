package cn.com.studyshop.distribute.lock;

import java.util.concurrent.CountDownLatch;

import cn.com.studyshop.api.OrderService;
import cn.com.studyshop.distribute.service.OrderServiceLock;

/**
 * 
 * @author LIU
 *
 */
public class CountDownLatchDemo {

	public static void main(String[] args) {
		// 并发数
		int currs = 20;
		CountDownLatch cdl = new CountDownLatch(currs);

		for (int i = 0; i < currs; i++) {
			new Thread(new Runnable() {
				OrderService orderService = new OrderServiceLock();

				@Override
				public void run() {
					// TODO Auto-generated method stub

					cdl.countDown();
					try {
						cdl.await();
						orderService.createOrderNO();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();
		}
	}
}
