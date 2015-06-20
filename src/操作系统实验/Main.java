package ����ϵͳʵ��;

import java.util.Scanner;

import sourceSystem.SourceSystem;
import dispatchSystem.DispatchSystem;

public class Main{
	
	static public void main(String[] arg){
		Scanner MyScanner = new Scanner(System.in);
		while (true){
			System.out.print("$");
			String LineString;
			LineString = MyScanner.nextLine();
			String[] args;
			args = LineString.split(" +");
			if (args[0].equals("help")){
				System.out.println("AddCh CHName --------------------- ���ͨ����ͨ������");
				System.out.println("AddCo CHName COName -------------- ��ӿ�������ͨ������ ����������");
				System.out.println("AddD COName DName ---------------- ����豸������������ �豸����");
				System.out.println("DelCh CHName --------------------- ɾ��ͨ����ͨ������");
				System.out.println("DelCo COName --------------------- ɾ��������������������");
				System.out.println("DelD DName ----------------------- ɾ���豸���豸����");
				System.out.println("ShowDs --------------------------- ��ʾ�����豸�ṹ");
				System.out.println("ApplyD DName---------------------- ���������豸���豸��");
				System.out.println("RecoverD PName DName-------------- ���̹黹�豸�������� �豸��");
				System.out.println("CreateP PName -------------------- �������̣�������");
				System.out.println("ShowP ---------------------------- ��ʾ���̶���");
				System.out.println("TimeOverP ------------------------ ʱ��Ƭ��");
				System.out.println("ExitP ---------------------------- �˳�����");
				System.out.println("MD ------------------------------- ������Ŀ¼��Ŀ¼����");
				System.out.println("CD ------------------------------- �л�Ŀ¼��Ŀ¼����");
				System.out.println("RD ------------------------------- ɾ����Ŀ¼��Ŀ¼����");
				System.out.println("MK ------------------------------- �������ļ����ļ��� �ļ���С");
				System.out.println("DEL ------------------------------ ɾ���ļ��� �ļ���");
				System.out.println("DIR ------------------------------ �г���ǰĿ¼�����ļ���");
				System.out.println("FORMAT --------------------------- ��ʽ������");
				System.out.println("TREE ----------------------------- ��ʾ����Ŀ¼");
				System.out.println("CPY ------------------------------ ������������ļ���������̣���������ļ��� ��������ļ�·��");
				System.out.println("MKCPY ---------------------------- �½��ļ������Ҹ�����������ļ����ݣ� ��������ļ��� ��������ļ�·��");
				System.out.println("SHOWF ---------------------------- ��ʾ�ļ����ݣ� �ļ���");
				System.out.println("DisType -------------------------- ���̵��ȷ�ʽ�� ��������FCFS,SJF,RR");
				System.out.println("DisAddP -------------------------- ��ӽ��̣� ������ ����ʱ�� ����ʱ��");
				System.out.println("ShowDis -------------------------- ��ʾ���Ƚ��������תʱ�䡿");
				System.out.println("SetSource ------------------------ ������Դ����Դ����");
				System.out.println("SourceAddP ----------------------- �����Դ���̣������� ������Դ ������Դ");
				System.out.println("GetSafeSer ----------------------- ��ȡ��ȫ����");
				System.out.println("CanGetSource --------------------- ѯ���Ƿ��ܻ����Դ:������ ��Դ����");
			}
			else if (IsOrder(args,"AddCh",1)){
				VirtualSystem.MainDeviceSystem.AddCH(args[1]);
			}
			else if (IsOrder(args,"AddCo",2)){
				VirtualSystem.MainDeviceSystem.AddControler(args[1], args[2]);
			}
			else if (IsOrder(args,"AddD",2)){
				VirtualSystem.MainDeviceSystem.AddDevice(args[1], args[2]);
			}
			else if (IsOrder(args,"DelCh",1)){
				VirtualSystem.MainDeviceSystem.DeleteCH(args[1]);
			}
			else if (IsOrder(args,"DelCo",1)){
				VirtualSystem.MainDeviceSystem.DeleteControler(args[1]);
			}
			else if (IsOrder(args,"DelD",1)){
				VirtualSystem.MainDeviceSystem.DeleteDevice(args[1]);
			}
			else if (IsOrder(args,"ShowDs",0)){
				VirtualSystem.MainDeviceSystem.ShowDevieSystem();
			}
			else if (IsOrder(args,"ApplyD",1)){
				VirtualSystem.MainDeviceSystem.ApplyDevice(args[1]);
			}
			else if (IsOrder(args,"RecoverD",2)){
				VirtualSystem.MainDeviceSystem.RecoverDevice(args[1], args[2]);
			}
			else if (IsOrder(args,"CreateP",1)){
				VirtualSystem.MainProcessManager.CreateProcess(args[1]);
			}
			else if (IsOrder(args,"ShowP",0)){
				VirtualSystem.MainProcessManager.ShowProcessQueue();
			}
			else if (IsOrder(args,"TimeOverP",0)){
				VirtualSystem.MainProcessManager.TimeOver();
			}
			else if (IsOrder(args,"ExitP",0)){
				VirtualSystem.MainProcessManager.ExitProcess();
			}
			else if (IsOrder(args,"MD",1)){
				VirtualSystem.MainFileSystem.MakeDir(args[1]);
			}
			else if (IsOrder(args,"CD",1)){
				VirtualSystem.MainFileSystem.ChangeDir(args[1]);
			}
			else if (IsOrder(args,"RD",1)){
				VirtualSystem.MainFileSystem.DelDir(args[1]);
			}
			else if (IsOrder(args,"MK",2)){
				VirtualSystem.MainFileSystem.MakeFile(args[1], Integer.valueOf(args[2]));
			}
			else if (IsOrder(args,"DEL",1)){
				VirtualSystem.MainFileSystem.DelFile(args[1]);
			}
			else if (IsOrder(args,"DIR",0)){
				VirtualSystem.MainFileSystem.ShowDir();
			}
			else if (IsOrder(args,"FORMAT",0)){
				VirtualSystem.MainFileSystem.Format();
			}
			else if (IsOrder(args,"TREE",0)){
				VirtualSystem.MainFileSystem.ShowTree();
			}
			else if (IsOrder(args,"CPY",2)){
				VirtualSystem.MainFileSystem.CopyFile(args[1], args[2]);
			}
			else if (IsOrder(args,"SHOWF",1)){
				VirtualSystem.MainFileSystem.ShowFile(args[1]);
			}
			else if (IsOrder(args,"MKCPY",2)){
				VirtualSystem.MainFileSystem.MKCPY(args[1], args[2]);
			}
			else if (IsOrder(args,"DisType",1)){
				if (args[1].equals("FCFS")){
					VirtualSystem.MainDispatchSystem = new DispatchSystem(0);
				}
				else if (args[1].equals("SJF")){
					VirtualSystem.MainDispatchSystem = new DispatchSystem(1);
				}
				else if (args[1].equals("RR")){
					VirtualSystem.MainDispatchSystem = new DispatchSystem(2);
				}
				else{
					System.out.println("δ֪���͡�");
				}
			}
			else if (IsOrder(args,"DisAddP",3)){
				if (VirtualSystem.MainDispatchSystem == null){
					System.out.println("����ϵͳδ��ʼ����");
				}
				else{
					VirtualSystem.MainDispatchSystem.AddProcess(args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]));
				}
			}
			else if (IsOrder(args,"ShowDis",1)){
				VirtualSystem.MainDispatchSystem.CalRunResult(Integer.valueOf(args[1]));
			}
			else if (IsOrder(args,"ShowDis",0)){
				VirtualSystem.MainDispatchSystem.CalRunResult(-1);
			}
			else if (args[0].toLowerCase().equals("SetSource".toLowerCase())){
				int[] TempIntArray = new int[args.length - 1];
				for (int i = 1;i < args.length;i ++){
					TempIntArray[i - 1] = Integer.valueOf(args[i]);
				}
				VirtualSystem.MainSourceSystem = new SourceSystem(TempIntArray);
			}
			else if (args[0].toLowerCase().equals("SourceAddP".toLowerCase())){
				if (VirtualSystem.MainSourceSystem == null){
					System.out.println("��Դδ��ʼ����");
				}
				else{
					int SourceLength = VirtualSystem.MainSourceSystem.GetSourceNum();
					int TempIndex = 2;
					int[] Total = new int[SourceLength];
					for (int i = 0;i < SourceLength;i ++,TempIndex ++){
						Total[i] = Integer.valueOf(args[TempIndex]);
					}
					int[] Exist = new int[SourceLength];
					for (int i = 0;i < SourceLength;i ++,TempIndex ++){
						Exist[i] = Integer.valueOf(args[TempIndex]);
					}
					VirtualSystem.MainSourceSystem.AddProcess(args[1],Total,Exist);
				}
			}
			else if (IsOrder(args,"GetSafeSer",0)){
				if (VirtualSystem.MainSourceSystem == null){
					System.out.println("��Դδ��ʼ����");
				}
				else{
					VirtualSystem.MainSourceSystem.ShowSafeSeries();
				}
			}
			else if (args[0].toLowerCase().equals("CanGetSource".toLowerCase())){
				if (VirtualSystem.MainSourceSystem == null){
					System.out.println("��Դδ��ʼ����");
				}
				else{
					int TempIndex = 2;
					int SourceLength = VirtualSystem.MainSourceSystem.GetSourceNum();
					int[] Total = new int[SourceLength];
					for (int i = 0;i < SourceLength;i ++,TempIndex ++){
						Total[i] = Integer.valueOf(args[TempIndex]);
					}
					System.out.println(VirtualSystem.MainSourceSystem.IsQuest(args[1], Total));
				}
			}
			else{
				System.out.println("δָ֪�");
			}
			
		}
	}
	
	static public boolean IsOrder(String[] args,String Order,int argc){
		if (args.length == argc + 1 && args[0].toLowerCase().equals(Order.toLowerCase())) return true;
		else return false;
	}
}
