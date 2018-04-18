package cn.com.studyshop.lock.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import cn.com.studyshop.api.OrderService;
import cn.com.studyshop.distribute.service.OrderServiceSyn;

public class CycleBarrierDemo {

	public static void main(String[] args) {

		int currs = 20;

		// 循环屏障
		CyclicBarrier cb = new CyclicBarrier(currs);

		for (int i = 0; i < currs; i++) {
			new Thread(new Runnable() {
				OrderService orderService = new OrderServiceSyn();

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						System.out.println("wait");
						cb.await();
						System.out.println("notify");
						orderService.createOrderNO();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();
		}

	}
}
