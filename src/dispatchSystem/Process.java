package dispatchSystem;

public class Process {
	String ProcessName;
	//����ʱ��
	int ArrivalTime;
	//����ʱ��
	int BurstTime;
	//����ʱ��
	int FinishedTime;
	//�Ѿ�����ʱ��
	int RunnedTime;
	
	public Process(String Name,int ArrTime,int BurTime){
		this.ProcessName = Name;
		this.ArrivalTime = ArrTime;
		this.BurstTime = BurTime;
	}

}
