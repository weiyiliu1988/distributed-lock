package cn.com.studyshop.distribute.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.com.studyshop.api.OrderService;

/**
 * App层锁(指令锁 轻量级)
 * 
 * @author LIU
 *
 */
public class OrderServiceLock implements OrderService {

	private static Lock lock = new ReentrantLock();
	private static volatile Integer num = 0;

	/*
	 * 创建工单号
	 */
	public void createOrderNO() {
		// if (lock.tryLock()) { // 注意无法自旋
		lock.lock(); // 阻塞自旋
		// }
		num++;
		String dateT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-").format(new Date());

		System.out.println(dateT + num);
		lock.unlock();
	}
}
