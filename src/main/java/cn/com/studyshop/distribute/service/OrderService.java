package cn.com.studyshop.distribute.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderService {

	private static Object monitor = new Object();
	private static volatile Integer num = 0;

	/*
	 * 创建工单号
	 */
	public void createOrderNO() {
		synchronized (monitor) {
			num++;
			String dateT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-").format(new Date());

			System.out.println(dateT + num);
		}
	}
}
