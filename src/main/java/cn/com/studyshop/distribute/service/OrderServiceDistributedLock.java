package cn.com.studyshop.distribute.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

import cn.com.studyshop.api.OrderService;
import cn.com.studyshop.distribute.lock.DistributedLock;

/**
 * 分布式锁（多微服务）
 * 
 * 
 * @author LIU
 *
 */
public class OrderServiceDistributedLock implements OrderService {
	private static Lock lock = new DistributedLock("/lock", "47.*:2181");
	private static volatile Integer num = 0;

	/*
	 * 创建工单号
	 */
	public void createOrderNO() {
		System.out.println("加锁处理开始----");
		lock.lock(); // 阻塞自旋
		num++;
		String dateT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-").format(new Date());

		System.out.println(dateT + num);
		System.out.println("解锁处理开始");
		lock.unlock();
	}
}
