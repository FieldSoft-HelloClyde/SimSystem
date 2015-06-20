package deviceSystem;

import java.util.ArrayList;

import ����ϵͳʵ��.VirtualSystem;
import ProcessSystem.*;
import ProcessSystem.Process;

public class DeviceSystem {
	/*
	 * DCT�б��豸
	 */
	ArrayList <DCT> DCTArray = new ArrayList<DCT>();
	/*
	 * COCT�б�������
	 */
	ArrayList <COCT> COCTArray = new ArrayList<COCT>();
	/*
	 * CHCT�б�ͨ��
	 */
	ArrayList<CHCT> CHCTArray = new ArrayList<CHCT>();
	
	/**
	 * Ϊ�������еĽ��̷����豸
	 * @param DeviceName
	 * @return
	 */
	public boolean ApplyDevice(String DeviceName){
		/*
		 * ��DCT�б��������豸
		 */
		for (DCT TempD:DCTArray){
			if (TempD.name.equals(DeviceName)){
				Process TempProcess;
				TempProcess = VirtualSystem.MainProcessManager.ExecutionQueue.peek();
				if (TempProcess != null){
					if (TempD.process == null){
						TempD.process = TempProcess;
						COCT TempCO;
						TempCO = (COCT) TempD.parent;
						if (TempCO.process == null){
							TempCO.process = TempProcess;
							CHCT TempCH;
							TempCH = (CHCT) TempCO.parent;
							if (TempCH.process == null){
								TempCH.process = TempProcess;
								VirtualSystem.MainProcessManager.BlockingProcess();
								System.out.println("�豸����ɹ���");
								return true;
							}
							else{
								TempCH.waitinglist.add(TempProcess);
								VirtualSystem.MainProcessManager.BlockingProcess();
								return false;
							}
						}
						else{
							TempCO.waitinglist.add(TempProcess);
							VirtualSystem.MainProcessManager.BlockingProcess();
							return false;
						}
					}
					else{
						TempD.waitinglist.add(TempProcess);
						VirtualSystem.MainProcessManager.BlockingProcess();
						return false;
					}
				}
				else{
					System.out.println("δ��ִ�ж����ҵ����̡�");
					return false;
				}
			}
		}
		System.out.println("û���ҵ��豸��");
		return false;
	}
	
	public boolean RecoverDevice(String ProcessName,String DeviceName){
		/*
		 * ��������
		 */
		Process DesProcess;
		DesProcess = null;
		for (Process TempProcess:VirtualSystem.MainProcessManager.BlockingQueue){
			if (TempProcess.GetName().equals(ProcessName)){
				DesProcess = TempProcess;
				break;
			}
		}
		if (DesProcess == null){
			System.out.println("δ�����������ҵ��ý��̡�");
			return false;
		}
		/*
		 * �����豸
		 */
		for (DCT TempDCT:DCTArray){
			if (TempDCT.name.equals(DeviceName)){
				if (TempDCT.process == DesProcess){
					if (TempDCT.waitinglist.isEmpty()){
						TempDCT.process = null;
					}
					else{
						TempDCT.process = TempDCT.waitinglist.poll();
					}
					COCT TempCO;
					TempCO = (COCT) TempDCT.parent;
					if (TempCO.process == DesProcess){
						if (TempCO.waitinglist.isEmpty()){
							TempCO.process = null;
						}
						else{
							TempCO.process = TempCO.waitinglist.poll();
						}
						CHCT TempCH;
						TempCH = (CHCT) TempCO.parent;
						if (TempCH.process == DesProcess){
							if (TempCH.waitinglist.isEmpty()){
								TempCH.process = null;
							}
							else{
								TempCH.process = TempCH.waitinglist.poll();
							}
							System.out.println("�豸���ճɹ���");
							VirtualSystem.MainProcessManager.WakeProcess(DesProcess);
							return true;
						}
						else{
							System.out.println("�豸���ճɹ���");
							VirtualSystem.MainProcessManager.WakeProcess(DesProcess);
							return true;
						}
					}
					else{
						System.out.println("�豸���ճɹ���");
						VirtualSystem.MainProcessManager.WakeProcess(DesProcess);
						return true;
					}
				}
				else{
					System.out.println("�豸δ������̡�");
					return false;
				}
			}
		}
		System.out.println("δ�ҵ����豸��");
		return false;
	}
	
	/**
	 * ��ʾͨ�������������豸�ṹ
	 */
	public void ShowDevieSystem(){
		for (CHCT TempCHCT:CHCTArray){
			System.out.println("ͨ����" + TempCHCT.name);
			for (COCT TempCOCT:COCTArray){
				if (TempCOCT.parent == TempCHCT){
					System.out.println("\t��������" + TempCOCT.name);
					for (DCT TempDCT:DCTArray){
						if (TempDCT.parent == TempCOCT){
							System.out.println("\t\t�豸��" + TempDCT.name);
						}
					}
				}
			}
		}
	}
	
	/**
	 * ɾ��ͨ������ͨ���µĿ��������������µ��豸
	 * @param CHName
	 * @return
	 */
	public boolean DeleteCH(String CHName){
		/*
		 * ����ͨ����ͨ����������ʧ��
		 */
		for (CHCT TempCHCT:CHCTArray){
			if (TempCHCT.name.equals(CHName)){
				/*
				 * ɾ��parentΪ��ͨ���Ŀ�����
				 */
				int COCTI,COCTL;
				COCTL = COCTArray.size();
				for (COCTI = 0;COCTI < COCTL;COCTI ++){
					if (COCTArray.get(COCTI).parent == TempCHCT){
						/*
						 * ɾ��parentΪ�˿������������豸
						 */
						int DCTI,DCTL;
						DCTL = DCTArray.size();
						for (DCTI = 0;DCTI < DCTL;DCTI ++){
							if (DCTArray.get(DCTI).parent == COCTArray.get(COCTI)){
								DCTArray.remove(DCTI);
								DCTI --;
								DCTL --;
							}
						}
						COCTArray.remove(COCTI);
						COCTI --;
						COCTL --;
					}
				}
				CHCTArray.remove(TempCHCT);
				return true;
			}
		}
		System.out.println("ͨ��" + CHName + "�Ѿ�������,ɾ��ʧ�ܡ�");
		return false;
	}
	
	/**
	 * ɾ����������˿������µ��豸
	 * @param ControlerName
	 * @return
	 */
	public boolean DeleteControler(String ControlerName){
		/*
		 * �����������б�,�����������������ɾ��ʧ��
		 */
		for (COCT TempCOCT:COCTArray){
			if (TempCOCT.name.equals(ControlerName)){
				/*
				 * ɾ��parentΪ�˿������������豸
				 */
				int DCTI,DCTL;
				DCTL = DCTArray.size();
				for (DCTI = 0;DCTI < DCTL;DCTI ++){
					if (DCTArray.get(DCTI).parent == TempCOCT){
						DCTArray.remove(DCTI);
						DCTI --;
						DCTL --;
					}
				}
				COCTArray.remove(TempCOCT);
				return true;
			}
		}
		System.out.println("������" + ControlerName + "�Ѿ�������,ɾ��ʧ�ܡ�");
		return false;
	}
	
	/**
	 * ɾ���豸
	 * @param DeviceName
	 * @return
	 */
	public boolean DeleteDevice(String DeviceName){
		/*
		 * �����豸�б�,����豸��������ɾ��ʧ��
		 */
		for (DCT TempDCT:DCTArray){
			if (TempDCT.name.equals(DeviceName)){
				DCTArray.remove(TempDCT);
				return true;
			}
		}
		System.out.println("�豸" + DeviceName + "�Ѿ�������,ɾ��ʧ�ܡ�");
		return false;
	}
	
	/**
	 * �����ͨ��
	 * @param CHNameͨ������
	 * @return
	 */
	public boolean AddCH(String CHName){
		
		/*
		 * ����ͨ���б�,���ͨ���Ѿ����������ʧ��
		 */
		for (int i = 0;i < CHCTArray.size();i ++){
			if (CHCTArray.get(i).name.equals(CHName)){
				System.out.println("ͨ��" + CHName + "�Ѿ�����,���ʧ�ܡ�");
				return false;
			}
		}
		/*
		 * �½�CH
		 */
		CHCT srcCHCT = new CHCT();
		srcCHCT.name = CHName;
		srcCHCT.parent = null;
		/*
		 * �����DCT�б�
		 */
		CHCTArray.add(srcCHCT);
		return true;
	}
	
	/**
	 * �ѿ�������ӽ�ͨ��
	 * @param CHName���λ��ͨ������
	 * @param ControlerName����ӵĿ���������
	 * @return
	 */
	public boolean AddControler(String CHName,String ControlerName){
		/*
		 * ����ͨ���б�
		 */
		CHCT desCHCT = null;
		for (int i = 0;i < CHCTArray.size();i ++){
			if (CHCTArray.get(i).name.equals(CHName)){
				desCHCT = CHCTArray.get(i);
				break;
			}
		}
		if (desCHCT == null){
			System.out.println("δ�ҵ�ͨ��" + CHName + ",���ʧ�ܡ�");
			return false;
		}
		else{
			/*
			 * �����������б�,����������Ѿ����������ʧ��
			 */
			for (int i = 0;i < COCTArray.size();i ++){
				if (COCTArray.get(i).name.equals(ControlerName)){
					System.out.println("������" + ControlerName + "�Ѿ�����,���ʧ�ܡ�");
					return false;
				}
			}
			/*
			 * �½�COCT
			 */
			COCT srcCOCT = new COCT();
			srcCOCT.name = ControlerName;
			srcCOCT.parent = desCHCT;
			/*
			 * �����DCT�б�
			 */
			COCTArray.add(srcCOCT);
		}
		return true;
	}
	
	/**
	 * �����������豸
	 * @param ControlerName����������
	 * @param DeviceName�豸����
	 * @return ��ӽ��
	 */
	public boolean AddDevice(String ControlerName,String DeviceName){
		/*
		 * �����������б�
		 */
		COCT desCOCT = null;
		for (int i = 0;i < COCTArray.size();i ++){
			if (COCTArray.get(i).name.equals(ControlerName)){
				desCOCT = COCTArray.get(i);
				break;
			}
		}
		if (desCOCT == null){
			System.out.println("δ�ҵ�������" + ControlerName + ",���ʧ�ܡ�");
			return false;
		}
		else{
			/*
			 * �����豸�б�,����豸�Ѿ����������ʧ��
			 */
			for (int i = 0;i < DCTArray.size();i ++){
				if (DCTArray.get(i).name.equals(DeviceName)){
					System.out.println("�豸" + DeviceName + "�Ѿ�����,���ʧ�ܡ�");
					return false;
				}
			}
			/*
			 * �½�DCT
			 */
			DCT srcDCT = new DCT();
			srcDCT.name = DeviceName;
			srcDCT.parent = desCOCT;
			/*
			 * �����DCT�б�
			 */
			DCTArray.add(srcDCT);
		}
		return true;
	}
}
