package dispatchSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class DispatchSystem {
	
	static public void main(String[] args){
		DispatchSystem MainDispatchSystem = new DispatchSystem(0);
		MainDispatchSystem.AddProcess("A", 0, 3);
		MainDispatchSystem.AddProcess("B", 0, 2);
		MainDispatchSystem.AddProcess("C", 3, 2);
		MainDispatchSystem.AddProcess("D", 4, 4);
		MainDispatchSystem.CalRunResult(-1);
	}
	/*
	 * �������ͣ�0,�����ȷ���1������ҵ���ȣ�2��ʱ��Ƭ����
	 */
	int DispatchType;
	//��ʼ���̶���
	PriorityQueue<Process> ProcessQueue;
	//��������
	Queue<Process> RunningProcessQueue;
	//��ҵ�Ѿ���ɶ���
	ArrayList<Process> FinishedProcessQueue = new ArrayList<Process>();
	
	//���ݽ��̵���ʱ����С��������
	Comparator<Process> ProcessArrTimeComper =  new Comparator<Process>(){  
        public int compare(Process arg0, Process arg1) {  
        	Process Process1 = (Process)arg0;
    		Process Process2 = (Process)arg1;
    		if (Process1.ArrivalTime > Process2.ArrivalTime){
    			return 1;
    		}
    		else if (Process1.ArrivalTime < Process2.ArrivalTime){
    			return -1;
    		}
    		else{
    			return 0;
    		}
        }
	};
	
	//���ݽ��̷���ʱ����С��������
	Comparator<Process> ProcessBurTimeComper =  new Comparator<Process>(){  
        public int compare(Process arg0, Process arg1) {  
        	Process Process1 = (Process)arg0;
    		Process Process2 = (Process)arg1;
    		if (Process1.BurstTime > Process2.BurstTime){
    			return 1;
    		}
    		else if (Process1.BurstTime < Process2.BurstTime){
    			return -1;
    		}
    		else{
    			return 0;
    		}
        }
	};
	
	public DispatchSystem(int SrcDispatchType){
		this.DispatchType = SrcDispatchType;
		this.ProcessQueue = new PriorityQueue<Process>(ProcessArrTimeComper);
		if (this.DispatchType == 0){
			this.RunningProcessQueue = new PriorityQueue<Process>(ProcessArrTimeComper);
		}
		else if (this.DispatchType == 1){
			this.RunningProcessQueue = new PriorityQueue<Process>(ProcessBurTimeComper);
		}
		else if (this.DispatchType == 2){
			this.RunningProcessQueue = new LinkedList<Process>();
		}
	}
	
	public void AddProcess(String Name,int ArrTime,int BurTime){
		Process TempProcess = new Process(Name, ArrTime, BurTime);
		this.ProcessQueue.add(TempProcess);
	}
	
	/**
	 * 
	 * @param TimeN -1����ʱ��Ƭ����ת
	 */
	public void CalRunResult(int TimeN){
		int TimeNow;
		int TimeOverN;
		Process RunProcess = null;
		TimeNow = 0;
		TimeOverN = 0;
		while (!(this.ProcessQueue.isEmpty() && this.RunningProcessQueue.isEmpty())){
			//�ѵ���ʱ��Ľ��̼����������
			Process ReadProcess = this.ProcessQueue.peek();
			while (ReadProcess != null && ReadProcess.ArrivalTime <= TimeNow){
				this.RunningProcessQueue.add(this.ProcessQueue.poll());
				ReadProcess = this.ProcessQueue.peek();
			}
			//���о�����������һ��������ʱ���1
			if (RunProcess == null){
				RunProcess = this.RunningProcessQueue.peek();
				//��ʼ��ʱ��Ƭ
				TimeOverN = 0;
			}
			else{
				//�ж�ʱ��Ƭ�Ƿ񵽴�
				if (TimeN == -1){
					//�������ʱ��Ƭ����
				}
				else{
					if (TimeOverN == TimeN){
						//��ԭ���Ǹ��������
						this.RunningProcessQueue.remove(RunProcess);
						this.RunningProcessQueue.add(RunProcess);
						RunProcess = this.RunningProcessQueue.peek();
						TimeOverN = 0;
					}
				}
			}
			System.out.println("Name:" + RunProcess.ProcessName + " Time:" + TimeNow);
			RunProcess.RunnedTime ++;
			TimeNow ++;
			TimeOverN ++;
			
			//��������ɵ�ת����ɶ���
			if (RunProcess.RunnedTime >= RunProcess.BurstTime){
				RunProcess.FinishedTime = TimeNow;
				this.RunningProcessQueue.remove(RunProcess);
				this.FinishedProcessQueue.add(RunProcess);
				RunProcess = null;
			}
		}
		System.out.println("������\t\t\t����ʱ��\t\t\t����ʱ��\t\t\t���ʱ��\t\t\t��תʱ��\t\t\t��Ȩ��תʱ��");
		double ExistTimeTotal = 0;
		double SuperExistTimeTotal = 0;
		for (Process TempProcess:this.FinishedProcessQueue){
			double ExistTime = TempProcess.FinishedTime - TempProcess.ArrivalTime;
			double SuperExistTime = ExistTime / TempProcess.BurstTime;
			System.out.println(TempProcess.ProcessName + "\t\t\t" + TempProcess.ArrivalTime + "\t\t\t" + TempProcess.BurstTime + "\t\t\t" + TempProcess.FinishedTime
					+ "\t\t\t" + ExistTime + "\t\t\t" + SuperExistTime);
			ExistTimeTotal += ExistTime;
			SuperExistTimeTotal += SuperExistTime;
		}
		System.out.println("ƽ����תʱ��:" + ExistTimeTotal / this.FinishedProcessQueue.size() + "\t\t\t" + "ƽ����Ȩ��תʱ��:" + SuperExistTimeTotal / this.FinishedProcessQueue.size());
	}
}
