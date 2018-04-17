package cn.com.studyshop.distribute.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.studyshop.api.OrderService;

/**
 * 不可逆 偏量锁->轻量级锁->重量级锁(底层内核函数切换)
 * 
 * @author LIU
 *
 */
public class OrderServiceSyn implements OrderService {

	private static Object monitor = new Object();
	private static volatile Integer num = 0;

	/*
	 * 创建工单号
	 */
	public void createOrderNO() {
		synchronized (monitor) {// 重量级锁
			num++;
			String dateT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-").format(new Date());

			System.out.println(dateT + num);
		}
	}
}
