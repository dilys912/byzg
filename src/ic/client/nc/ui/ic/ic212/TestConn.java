package nc.ui.ic.ic212;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

public class TestConn {

	static Pointer pointer;

	/**
	 *������
	 * 
	 * @param port
	 *            ���ںţ�ȡֵΪ0��3
	 * @param baud
	 *            ͨѶ������9600��115200
	 * @return ͨѶ�豸��ʶ��
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public static final int TestConnect(int port, long baud)
			throws NativeException, IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("iccrf.dll", "rf_init"); // ���غ�������
			n.setRetVal(Type.INT); // ���ú�������ֵ����
			int i = 0; // ���ò���˳��
			n.setParameter(i++, Type.INT, "" + port); // ��ֵ
			n.setParameter(i++, Type.INT, "" + baud); // ����
			System.out.println("���õ�DLL�ļ���Ϊ��" + n.getDLLName());
			System.out.println("���õķ�����Ϊ��" + n.getFunctionName());

			n.invoke(); // ���ú���

			// System.out.println(n.getRetVal());

			return Integer.parseInt(n.getRetVal()); // ��������ֵ
		} finally {
			if (n != null)
				n.dispose(); // �ر�
		}
	}

	/**
	 * ����ָ��,���ڽ��յ��ú����ķ���ֵ
	 * 
	 * @return
	 * @throws NativeException
	 */
	public Pointer creatPointer() throws NativeException {

		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(10000));// ������ڴ��С
		return pointer;

	}

	/**
	 * ��������
	 * 
	 * @param icdev
	 *            ͨѶ�豸��ʶ��
	 * @param msec
	 *            ����ʱ��,��λ10����
	 * @return
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public String testBeep(int icdev, int msec) throws NativeException,
			IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("iccrf.dll", "rf_beep"); // ���غ�������
			n.setRetVal(Type.INT); // ���ú�������ֵ����
			int i = 0; // ���ò���˳��
			n.setParameter(i++, icdev);
			n.setParameter(i++, msec); // ��ֵ
			System.out.println("���õ�DLL�ļ���Ϊ��" + n.getDLLName());
			System.out.println("���õķ�����Ϊ��" + n.getFunctionName());

			n.invoke(); // ���ú���
			return n.getRetVal(); // ��������ֵ
		} finally {
			if (n != null)
				n.dispose(); // �ر�
		}
	}

	/**
	 * �˳�,�ָ�����
	 * 
	 * @param icdev
	 * @return
	 * @throws Exception
	 */
	public String exit(int icdev) throws Exception {
		JNative n = null;
		try {
			n = new JNative("iccrf.dll", "rf_exit"); // ���غ�������
			n.setRetVal(Type.INT); // ���ú�������ֵ����
			int i = 0; // ���ò���˳��
			n.setParameter(i++, icdev);
			// n.setParameter(i++, msec); // ��ֵ
			System.out.println("���õ�DLL�ļ���Ϊ��" + n.getDLLName());
			System.out.println("���õķ�����Ϊ��" + n.getFunctionName());

			n.invoke(); // ���ú���
		} finally {
			if (n != null)
				n.dispose(); // �ر�
		}
		return n.getRetVal(); // ��������ֵ
	}

	// ���� 02085669719
	/**
	 * @param handle
	 * @param Mode
	 *            Ѱ��ģʽ(0Ϊ��һ�ſ�����,1Ϊ�Զ��ſ�caozu)
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
			// 1.����JNative����
			n = new JNative("iccrf.dll", "rf_card");
			// 2.���ú�������ֵ����
			n.setRetVal(Type.INT);
			// 3.���ò�������
			// ����ָ���ڴ�ռ�
			Pointer NKey = new Pointer(MemoryBlockFactory
					.createMemoryBlock(100));
			n.setParameter(0, handle);
			n.setParameter(1, Mode);
			n.setParameter(2, NKey);
			// 4.ִ�к���
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
					System.out.println("�����ɹ�!");
				} else {
					System.out.println("����ʧ��!");
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
			System.out.println("�����ɹ�!");
		} else {
			System.out.println("����ʧ��!");
		}
		String exit = tc.exit(icdev);
		System.out.println("exit=" + exit);

	}
}