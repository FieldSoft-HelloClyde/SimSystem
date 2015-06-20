package ProcessSystem;

import java.util.LinkedList;
import java.util.Queue;


public class ProcessManager {
	//����id����Ϊϵͳʱ��Ms+���С�
	long SerialNumber;
	//
	public Queue<Process> ReadQueue;
	public Queue<Process> ExecutionQueue;
	public Queue<Process> BlockingQueue;
	
	
	public ProcessManager(){
		SerialNumber = 0;
		ReadQueue = new LinkedList<Process>();
		ExecutionQueue = new LinkedList<Process>();
		BlockingQueue = new LinkedList<Process>();
	}
	
	public void CreateProcess(String srcName){
		Process TempProcess = new Process(srcName,new String(String.valueOf(System.currentTimeMillis()) + this.GetSerialNumber()));
		AddProcessToReadQueue(TempProcess);
		System.out.println("�½�����Ϊ" + srcName + "�Ľ��̡�");
	}
	
	public void AddProcessToReadQueue(Process srcProcess){
		ReadQueue.offer(srcProcess);
		if (ExecutionQueue.size() == 0){
			ExecutionQueue.offer(srcProcess);
			ReadQueue.remove(srcProcess);
		}
	}
	
	public void ExecutionProcess(Process srcProcess){
		if (ExecutionQueue.size() != 0){
			System.out.println("ִ�ж���������ֻ����һ��ִ�н��̡�");
		}
		else{
			ExecutionQueue.offer(srcProcess);
			ReadQueue.remove(srcProcess);
		}
	}
	
	public void ExecutionProcess(){
		if (ExecutionQueue.size() != 0){
			System.out.println("ִ�ж���������ֻ����һ��ִ�н��̡�");
		}
		else{
			Process TempProcess;
			TempProcess = ReadQueue.poll();
			if (TempProcess == null){
				System.out.println("��������Ϊ�գ�����ִ�н��̡�");
			}
			else{
				ExecutionQueue.offer(TempProcess);
			}
		}
	}
	
	public void BlockingProcess(){
		if (ExecutionQueue.size() == 0){
			System.out.println("ִ�ж���Ϊ�ա�");
		}
		else{
			Process TempProcess;
			TempProcess = ExecutionQueue.poll();
			BlockingQueue.offer(TempProcess);
			System.out.println("����Ϊ" + TempProcess.name + "����ȴ�IO��������");
			ExecutionProcess();
		}
	}
	
	public void WakeProcess(Process srcProcess){
		ReadQueue.offer(srcProcess);
		BlockingQueue.remove(srcProcess);
		ExecutionProcess();
	}
	
	public void TimeOver(){
		if (ExecutionQueue.size() == 0){
			System.out.println("ִ�ж���Ϊ�ա�");
		}
		else{
			Process TempProcess;
			TempProcess = ExecutionQueue.poll();
			ReadQueue.offer(TempProcess);
			ExecutionProcess();
			System.out.println("����Ϊ" + TempProcess.name + "�Ľ���ʱ��Ƭ����");
		}
	}
	
	public void ExitProcess(){
		if (ExecutionQueue.size() == 0){
			System.out.println("ִ�ж���Ϊ�ա�");
		}
		else{
			Process TempProcess;
			TempProcess = ExecutionQueue.poll();
			TempProcess.Destory();
			System.out.println("����Ϊ" + TempProcess.name + "�Ľ����Ѿ��˳���");
			ExecutionProcess();
		}
	}
	
	private long GetSerialNumber(){
		long returnN;
		returnN = SerialNumber;
		SerialNumber ++;
		if (SerialNumber >= 10){
			SerialNumber = 0;
		}
		return returnN;
	}
	
	public void ShowProcessQueue(){
		System.out.println("�������У�");
		for (Process TempProcess:ReadQueue){
			System.out.println(TempProcess.GetName());
		}
		System.out.println("ִ�ж��У�");
		for (Process TempProcess:ExecutionQueue){
			System.out.println(TempProcess.GetName());
		}
		System.out.println("�������У�");
		for (Process TempProcess:BlockingQueue){
			System.out.println(TempProcess.GetName());
		}
	}
}
