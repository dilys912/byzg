package nc.ui.ic.ic212;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

public class TestConn {

	static Pointer pointer;

	/**
	 *打开连接
	 * 
	 * @param port
	 *            串口号，取值为0～3
	 * @param baud
	 *            通讯波特率9600～115200
	 * @return 通讯设备标识符
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public static final int TestConnect(int port, long baud)
			throws NativeException, IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("iccrf.dll", "rf_init"); // 加载函数对象
			n.setRetVal(Type.INT); // 设置函数返回值类型
			int i = 0; // 设置参数顺序
			n.setParameter(i++, Type.INT, "" + port); // 设值
			n.setParameter(i++, Type.INT, "" + baud); // 设置
			System.out.println("调用的DLL文件名为：" + n.getDLLName());
			System.out.println("调用的方法名为：" + n.getFunctionName());

			n.invoke(); // 调用函数

			// System.out.println(n.getRetVal());

			return Integer.parseInt(n.getRetVal()); // 函数返回值
		} finally {
			if (n != null)
				n.dispose(); // 关闭
		}
	}

	/**
	 * 创建指针,用于接收调用函数的返回值
	 * 
	 * @return
	 * @throws NativeException
	 */
	public Pointer creatPointer() throws NativeException {

		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(10000));// 分配的内存大小
		return pointer;

	}

	/**
	 * 蜂鸣控制
	 * 
	 * @param icdev
	 *            通讯设备标识符
	 * @param msec
	 *            蜂鸣时间,单位10毫秒
	 * @return
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public String testBeep(int icdev, int msec) throws NativeException,
			IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("iccrf.dll", "rf_beep"); // 加载函数对象
			n.setRetVal(Type.INT); // 设置函数返回值类型
			int i = 0; // 设置参数顺序
			n.setParameter(i++, icdev);
			n.setParameter(i++, msec); // 设值
			System.out.println("调用的DLL文件名为：" + n.getDLLName());
			System.out.println("调用的方法名为：" + n.getFunctionName());

			n.invoke(); // 调用函数
			return n.getRetVal(); // 函数返回值
		} finally {
			if (n != null)
				n.dispose(); // 关闭
		}
	}

	/**
	 * 退出,恢复串口
	 * 
	 * @param icdev
	 * @return
	 * @throws Exception
	 */
	public String exit(int icdev) throws Exception {
		JNative n = null;
		try {
			n = new JNative("iccrf.dll", "rf_exit"); // 加载函数对象
			n.setRetVal(Type.INT); // 设置函数返回值类型
			int i = 0; // 设置参数顺序
			n.setParameter(i++, icdev);
			// n.setParameter(i++, msec); // 设值
			System.out.println("调用的DLL文件名为：" + n.getDLLName());
			System.out.println("调用的方法名为：" + n.getFunctionName());

			n.invoke(); // 调用函数
		} finally {
			if (n != null)
				n.dispose(); // 关闭
		}
		return n.getRetVal(); // 函数返回值
	}

	// 刘工 02085669719
	/**
	 * @param handle
	 * @param Mode
	 *            寻卡模式(0为对一张卡操作,1为对多张卡caozu)
	 * @return
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public long card(int handle, int Mode) throws NativeException,
			IllegalAccessException {
		JNative n = null;
		System.gc();
		String aa ;
		try {
			// 1.创建JNative对象
			n = new JNative("iccrf.dll", "rf_card");
			// 2.设置函数返回值类型
			n.setRetVal(Type.INT);
			// 3.设置参数类型
			// 声明指定内存空间
			Pointer NKey = new Pointer(MemoryBlockFactory
					.createMemoryBlock(100));
			n.setParameter(0, handle);
			n.setParameter(1, Mode);
			n.setParameter(2, NKey);
			// 4.执行函数
			n.invoke();
			aa = n.getRetVal();
			System.out.println(aa);
		} finally {
			if (n != null) {
				extracted(n);
			}
		}
		return Long.parseLong(aa);
	}

	private void extracted(JNative n) throws NativeException,
			IllegalAccessException {
		n.dispose();
	}

	public String readICCard() throws Exception {
		TestConn tc = new TestConn();
//		Pointer po = tc.creatPointer();
		int icdev=-1;
		for(int i=0;i<=3;i++){
		   icdev = TestConn.TestConnect(i, 9600);
		   if(icdev>0)break;
		}
		System.out.println("handle===" + icdev);
		if (icdev>0) {			
			tc.testBeep(icdev, 500);
		}
		long a = 0l;
		for(int i = 0;i < 10;i++) {
			a = tc.card(icdev, 1);
			System.out.println("a==" + a);
			if (a != 1l){				
				String beep = tc.testBeep(icdev, 500);
				System.out.println("beep==" + beep);
				if ("0".equals(beep)) {
					System.out.println("蜂鸣成功!");
				} else {
					System.out.println("蜂鸣失败!");
				}
				break;
			}
			Thread.sleep(200);
		}

		String exit = tc.exit(icdev);
		System.out.println("exit=" + exit);
		return String.valueOf(a);

	}

	public static void main(String[] args) throws Exception {
		TestConn tc = new TestConn();
		Pointer po = tc.creatPointer();
		int icdev=-1;
		for(int i=0;i<=3;i++){
			icdev = TestConn.TestConnect(i, 9600);
		   if(icdev>0)break;
		}
		System.out.println("handle===" + icdev);
		long a = 0l;
		for (int i = 0; i < 60; i++) {
			a = tc.card(icdev, 1);
			System.out.println("a==" + a);
			if (a != 1l)
				break;
			Thread.sleep(200);
		}

		String beep = tc.testBeep(icdev, 500);
		System.out.println("beep==" + beep);
		if ("0".equals(beep)) {
			System.out.println("蜂鸣成功!");
		} else {
			System.out.println("蜂鸣失败!");
		}
		String exit = tc.exit(icdev);
		System.out.println("exit=" + exit);

	}
}