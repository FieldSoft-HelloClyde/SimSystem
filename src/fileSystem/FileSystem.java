package fileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import RandomFileCpp.RandomAccessFileCpp;

public class FileSystem {
	File VirtualDisk;
	public FCB CurrFile;
	FCB Root;
	
	/**
	 * 
	 * @param FileName
	 * @param srcFileName
	 */
	public void MKCPY(String FileName,String srcFileName){
		try {
			RandomAccessFileCpp srcFileRF = new RandomAccessFileCpp(srcFileName,"rw");
			long FileLength = srcFileRF.length();
			srcFileRF.close();
			this.MakeFile(FileName, (int) FileLength);
			this.CopyFile(FileName, srcFileName);
			srcFileRF.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʾ���νṹ
	 */
	public void ShowTree(){
		ShowTree(CurrFile);
	}
	
	public void ShowFile(String FileName){
		try{
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �жϸ��ļ��Ƿ����
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 1){
					int FStart = 0;
					int pos;
					for (pos = 0;pos < TempFCB.Size;pos ++){
						String HexStr;
						HexStr = Integer.toHexString(this.GetUnsignedByte(FileName, pos)).toUpperCase();
						if (HexStr.length() == 1){
							HexStr = "0" + HexStr;
						}
						System.out.print(HexStr + " ");
						FStart ++;
						if (FStart == 16){
							System.out.print("\t");
							for (int pi = 0;pi < FStart;pi ++){
								char CharStr;
								CharStr = (char)this.GetUnsignedByte(FileName, pos - FStart + pi + 1);
								if (CharStr == 13 || CharStr == 10){
									CharStr = 0;
								}
								System.out.print(CharStr + " ");
							}
							System.out.println();
							FStart = 0;
						}
					}
					for (int i = 0;i < 16 - FStart;i ++){
						System.out.print("   ");
					}
					System.out.print("\t");
					for (int pi = 0;pi < FStart;pi ++){
						char CharStr;
						CharStr = (char)this.GetUnsignedByte(FileName, pos - FStart + pi);
						if (CharStr == 13 || CharStr == 10){
							CharStr = 0;
						}
						System.out.print(CharStr + " ");
					}
					System.out.println();
					return;
				}
			}
			System.out.println("�ļ������ڡ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����������ļ����ݸ��Ƶ���������ļ�
	 * @param FileName
	 * @param srcFileName
	 * @return
	 */
	public boolean CopyFile(String FileName,String srcFileName){
		try {
			RandomAccessFileCpp srcFileRF = new RandomAccessFileCpp(srcFileName,"rw");
			for (long pos = 0;pos < srcFileRF.length();pos ++){
				srcFileRF.seek(pos);
				this.PutUnsignedByte(FileName, (int) pos, srcFileRF.readUnsignedByte());
			}
			srcFileRF.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * д���ļ�����
	 * @param FileName
	 * @param FilePointer
	 * @param ByteDate 0~255
	 * @return
	 */
	public boolean PutUnsignedByte(String FileName,int FilePointer,int ByteDate){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �жϸ��ļ��Ƿ����
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 1){
					if (TempFCB.Name.equals(FileName)){
						if (FilePointer >= TempFCB.Size){
							System.out.println("�ļ�ָ��Խ�硣");
							return false;
						}
						else{
							int BlockIndex;
							int InBlockIndex;
							BlockIndex = FilePointer / 1024;
							InBlockIndex = FilePointer % 1024;
							int TempBlockAdd;
							TempBlockAdd = TempFCB.FirstBlock;
							for (int i = 0;i < BlockIndex;i ++){
								VirtualDiskRF.seek(TempBlockAdd * 2);
								TempBlockAdd = VirtualDiskRF.readUnsignedShort();
							}
							VirtualDiskRF.seek(32 + TempBlockAdd * 1024 + InBlockIndex);
							if (ByteDate >= 0 && ByteDate <= 255){
								VirtualDiskRF.writeByte(ByteDate);
								VirtualDiskRF.close();
								return true;
							}
							else{
								System.out.println("���ݲ�����ȷ��Χ�ڡ�");
								return false;
							}
						}
					}
				}
			}
			System.out.println("�ļ������ڡ�");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * ��ȡĿ���ļ�����
	 * @param FileName
	 * @param FilePointer
	 * @return 0~255 -1��ʾ����
	 */
	public int GetUnsignedByte(String FileName,int FilePointer){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �жϸ��ļ��Ƿ����
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 1){
					if (TempFCB.Name.equals(FileName)){
						if (FilePointer >= TempFCB.Size){
							System.out.println("�ļ�ָ��Խ�硣");
							return -1;
						}
						else{
							int BlockIndex;
							int InBlockIndex;
							BlockIndex = FilePointer / 1024;
							InBlockIndex = FilePointer % 1024;
							int TempBlockAdd;
							TempBlockAdd = TempFCB.FirstBlock;
							for (int i = 0;i < BlockIndex;i ++){
								VirtualDiskRF.seek(TempBlockAdd * 2);
								TempBlockAdd = VirtualDiskRF.readUnsignedShort();
							}
							VirtualDiskRF.seek(32 + TempBlockAdd * 1024 + InBlockIndex);
							return VirtualDiskRF.readUnsignedByte();
						}
					}
				}
			}
			System.out.println("�ļ������ڡ�");
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * �ж����FCB�ǲ��Ǹ�Ŀ¼�����һ��
	 * @param File
	 * @return
	 */
	private boolean IsLastFCB(FCB Dir){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			/*
			 * �ҵ���Ŀ¼
			 */
			VirtualDiskRF.seek(32 + Dir.FirstBlock * 1024);
			byte[] TempByteArray = new byte[32];
			VirtualDiskRF.read(TempByteArray, 0, 32);
			FCB ParentFCB = new FCB(TempByteArray);
			/*
			 * �ҵ�Dir�ڸ�Ŀ¼��λ��
			 */
			int DirIndex = -1;
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + ParentFCB.FirstBlock * 1024 + FCBIndex * 32);
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 2){
					if (TempFCB.Name.equals(Dir.Name)){
						DirIndex = FCBIndex;
						break;
					}
				}
			}
			if (DirIndex == 31){
				return true;
			}
			/*
			 * �ж���dir֮���Ƿ���Ŀ¼
			 */
			for (int FCBIndex = DirIndex + 1;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + ParentFCB.FirstBlock * 1024 + FCBIndex * 32);
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 2){
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return false;
		}
	}
	
	private FCB GetParentFCB(FCB Dir){
		try {
			RandomAccessFileCpp VirtualDiskRF;
			VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			VirtualDiskRF.seek(32 + Dir.FirstBlock * 1024);
			byte[] TempByteArray = new byte[32];
			VirtualDiskRF.read(TempByteArray, 0, 32);
			FCB ParentFCB = new FCB(TempByteArray);
			return ParentFCB;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ������ʾ�ĵݹ麯��
	 * @param Dir
	 */
	private void ShowTree(FCB Dir){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = Dir.FirstBlock;
			
			
			int FCBStartIndex;
			if (Dir.FirstBlock == Root.FirstBlock){
				FCBStartIndex = 0;
			}
			else{
				FCBStartIndex = 1;
			}
			for (int FCBIndex = FCBStartIndex;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 2){
					/*
					 * ������ʾ
					 */
					ArrayList<String> StrArray = new ArrayList<String>();
					StrArray.add(TempFCB.Name);
					if (this.IsLastFCB(TempFCB)){
						StrArray.add("����");
					}
					else{
						StrArray.add("����");
					}
					FCB TempFCB2 = this.GetParentFCB(TempFCB);
					while (TempFCB2.FirstBlock != CurrFile.FirstBlock){
						//StrArray.add(TempFCB2.Name);
						if (this.IsLastFCB(TempFCB2)){
							StrArray.add("  ");
						}
						else{
							StrArray.add("�� ");
						}
						TempFCB2 = this.GetParentFCB(TempFCB2);
					}
					for (int i = StrArray.size() - 1;i >= 0; i --){
						System.out.print(StrArray.get(i));
					}
					System.out.println();
					ShowTree(TempFCB);
				}
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	/**
	 * ɾ���ļ�
	 * @param FileName
	 * @return
	 */
	public boolean DelFile(String FileName){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �����Ƿ������ͬ�ļ�����FCB
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 1){
					if (TempFCB.Name.equals(FileName)){
						/*
						 * ɾ������ļ�FCB
						 */
						TempFCB.Type = 0;
						VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
						VirtualDiskRF.write(TempFCB.ToByteArray());
						/*
						 * ������ļ�����ռ��FAT�����
						 */
						int TempBlockIndex = TempFCB.FirstBlock;
						while (TempBlockIndex != 0xffff){
							int NextBlockIndex;
							VirtualDiskRF.seek(TempBlockIndex * 2);
							NextBlockIndex = VirtualDiskRF.readUnsignedShort();
							VirtualDiskRF.seek(TempBlockIndex * 2);
							VirtualDiskRF.writeShort(0x0000);
							TempBlockIndex = NextBlockIndex;
						}
						return true;
					}
				}
			}
			System.out.println("�ļ������ڡ�");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * �����ļ�
	 * @param FileName
	 * @param FileLength
	 * @return
	 */
	public boolean MakeFile(String FileName,int FileLength){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �����Ƿ������ͬ�ļ�����FCB
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type != 0){
					if (TempFCB.Name.equals(FileName)){
						System.out.println("���ļ��Ѿ����ڡ�");
						return false;
					}
				}
			}
			/*
			 * �½�FCB
			 */
			if (FileName.length() > 8){
				System.out.println("�ļ������ȳ���8�ֽڡ�");
				return false;
			}
			FCB desFCB = new FCB();
			desFCB.Size = FileLength;
			desFCB.SetTime();
			desFCB.Name = FileName;
			desFCB.Type = 1;
			
			/*
			 * ��������ռ�
			 */
			int BlockNum = (int) Math.ceil((double)FileLength / 1024);
			//System.out.println("BlockNum" + BlockNum);
			int[] desIntArray = new int[BlockNum];
			desIntArray = this.GetEmptyBlockIndex(BlockNum);
			if (desIntArray == null){
				System.out.println("���̿ռ䲻�㡣");
				VirtualDiskRF.close();
				return false;
			}
			desFCB.FirstBlock = desIntArray[0];
			
			/*
			 * FCBList�Ƿ��еط����½����ļ�
			 */
			int EmptyFCBIndex = -1;
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 0){
					EmptyFCBIndex = FCBIndex;
					break;
				}
			}
			if (EmptyFCBIndex == -1){
				System.out.println("��Ŀ¼���ļ������Ѿ��������ޡ�");
				return false;
			}
			/*
			 * �����½���FCB���ļ� 
			 */
			byte[] TempByte;
			TempByte = desFCB.ToByteArray();
			VirtualDiskRF.seek(32 + FCBListBlock * 1024 + EmptyFCBIndex * 32);
			VirtualDiskRF.write(TempByte);
			
			/*
			 * �޸�FAT����������½��ļ���ռ�Ŀ�
			 */
			for (int i = 0;i < BlockNum - 1;i ++){
				VirtualDiskRF.seek(32 + desIntArray[i] * 1024);
				for (int j = 0;j < 1024;j ++){
					VirtualDiskRF.writeByte(0);
				}
				VirtualDiskRF.seek(desIntArray[i] * 2);
				VirtualDiskRF.writeShort(desIntArray[i + 1]);
			}
			VirtualDiskRF.seek(32 + desIntArray[BlockNum - 1] * 1024);
			for (int j = 0;j < 1024;j ++){
				VirtualDiskRF.writeByte(0);
			}
			VirtualDiskRF.seek(desIntArray[BlockNum - 1] * 2);
			VirtualDiskRF.writeShort(0xffff);
			
			VirtualDiskRF.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean ChangeDir(String DirName){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			if (DirName.equals(".")){
				CurrFile = Root;
				VirtualDiskRF.close();
				return true;
			}
			else if (DirName.equals("..")){
				if (CurrFile.FirstBlock == Root.FirstBlock){
					System.out.println("�Ѿ������Ŀ¼��");
					VirtualDiskRF.close();
					return false;
				}
				else{
					VirtualDiskRF.seek(32 + FCBListBlock * 1024);
					byte[] TempByteArray = new byte[32];
					VirtualDiskRF.read(TempByteArray, 0, 32);
					FCB TempFCB = new FCB(TempByteArray);
					CurrFile = TempFCB;
					VirtualDiskRF.close();
					return true;
				}
				
			}
			else{
				/*
				 * �����Ƿ������ͬ�ļ�����FCB
				 */
				for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
					//��λFCB
					VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
					byte[] TempByteArray = new byte[32];
					VirtualDiskRF.read(TempByteArray, 0, 32);
					FCB TempFCB = new FCB(TempByteArray);
					if (TempFCB.Type == 2){
						if (TempFCB.Name.equals(DirName)){
							CurrFile = TempFCB;
							VirtualDiskRF.close();
							return true;
						}
					}
				}
				System.out.println("�ļ������ڡ�");
				VirtualDiskRF.close();
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ��ʾ��ǰĿ¼�ļ�
	 */
	public void ShowDir(){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			System.out.println(String.valueOf(Root.DateTime) + (Root.Type == 1 ? "\t\t" : "\t<DIR>\t") + Root.Size + "\t.");
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type != 0){
					/*
					 * ��Ŀ¼�ֿ�����
					 */
					if (CurrFile.FirstBlock == Root.FirstBlock){
						System.out.println(String.valueOf(TempFCB.DateTime) + (TempFCB.Type == 1 ? "\t\t" : "\t<DIR>\t") + TempFCB.Size + "\t" + TempFCB.Name);
					}
					else{
						if (FCBIndex == 0){
							System.out.println(String.valueOf(TempFCB.DateTime) + (TempFCB.Type == 1 ? "\t\t" : "\t<DIR>\t") + TempFCB.Size + "\t..");
						}
						else{
							System.out.println(String.valueOf(TempFCB.DateTime) + (TempFCB.Type == 1 ? "\t\t" : "\t<DIR>\t") + TempFCB.Size + "\t" + TempFCB.Name);
						}
					}
				}
			}
			VirtualDiskRF.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ɾ��Ŀ¼
	 * @param DirName
	 * @return
	 */
	public boolean DelDir(String DirName){
		try {
			if (DirName.equals("..") | DirName.equals(".")){
				System.out.println("����ļ����ܱ�ɾ����");
				return false;
			}
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �����Ƿ������ͬ�ļ�����FCB
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 2){
					if (TempFCB.Name.equals(DirName)){
						/*
						 * �ж��Ƿ�Ϊ��
						 */
						boolean IsEmpty = true;
						for (int FCBIndex2 = 1;FCBIndex2 < 32;FCBIndex2 ++){
							//��λFCB
							VirtualDiskRF.seek(32 + TempFCB.FirstBlock * 1024 + FCBIndex2 * 32);
							byte[] TempByteArray2 = new byte[32];
							VirtualDiskRF.read(TempByteArray2, 0, 32);
							FCB TempFCB2 = new FCB(TempByteArray2);
							if (TempFCB2.Type != 0){
								IsEmpty = false;
								break;
							}
						}
						if (IsEmpty){
							/*
							 * �޸�FCBList
							 */
							TempFCB.Type = 0;
							VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
							VirtualDiskRF.write(TempFCB.ToByteArray());
							/*
							 * �޸�FAT
							 */
							VirtualDiskRF.seek(TempFCB.FirstBlock * 2);
							VirtualDiskRF.writeShort(0x0000);
							return true;
						}
						else{
							System.out.println("���ļ��в�Ϊ�ա�");
							VirtualDiskRF.close();
							return false;
						}
					}
				}
			}
			System.out.println("���ļ������ڡ�");
			VirtualDiskRF.close();
			return false;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * �ڵ�ǰĿ¼�����Ŀ¼
	 * @param DirName ��Ŀ¼����
	 * @return
	 */
	public boolean MakeDir(String DirName){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int FCBListBlock = CurrFile.FirstBlock;
			/*
			 * �����Ƿ������ͬ�ļ�����FCB
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type != 0){
					if (TempFCB.Name.equals(DirName)){
						System.out.println("���ļ��Ѿ����ڡ�");
						return false;
					}
				}
			}
			/*
			 * �½�FCB
			 */
			if (DirName.length() > 8){
				System.out.println("�ļ������ȳ���8�ֽڡ�");
				return false;
			}
			FCB desFCB = new FCB();
			desFCB.SetTime();
			desFCB.Name = DirName;
			desFCB.Type = 2;
			int[] TempIntArray = this.GetEmptyBlockIndex(1);
			if (TempIntArray == null){
				System.out.println("���̿ռ�������");
				return false;
			}
			desFCB.FirstBlock = TempIntArray[0];
			/*
			 * FCBList�Ƿ��еط����½����ļ�
			 */
			int EmptyFCBIndex = -1;
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				//��λFCB
				VirtualDiskRF.seek(32 + FCBListBlock * 1024 + FCBIndex * 32);
				byte[] TempByteArray = new byte[32];
				VirtualDiskRF.read(TempByteArray, 0, 32);
				FCB TempFCB = new FCB(TempByteArray);
				if (TempFCB.Type == 0){
					EmptyFCBIndex = FCBIndex;
					break;
				}
			}
			if (EmptyFCBIndex == -1){
				System.out.println("��Ŀ¼���ļ������Ѿ��������ޡ�");
				return false;
			}
			/*
			 * �����½���FCB���ļ� 
			 */
			byte[] TempByte;
			TempByte = desFCB.ToByteArray();
			VirtualDiskRF.seek(32 + FCBListBlock * 1024 + EmptyFCBIndex * 32);
			VirtualDiskRF.write(TempByte);
			
			/*
			 * �޸�FAT
			 */
			VirtualDiskRF.seek(desFCB.FirstBlock * 2);
			VirtualDiskRF.writeShort(0xffff);
			/*
			 * ���FCBList
			 */
			for (int FCBIndex = 0;FCBIndex < 32;FCBIndex ++){
				VirtualDiskRF.seek(32 + desFCB.FirstBlock * 1024 + FCBIndex * 32);
				FCB TempFCB = new FCB();
				TempFCB.Type = 0;
				byte[] TempByteArray = TempFCB.ToByteArray();
				VirtualDiskRF.write(TempByteArray);
			}
			/*
			 * ��FCBList������ϼ�FCB
			 */
			VirtualDiskRF.seek(32 + desFCB.FirstBlock * 1024);
			byte[] TempByteArray = CurrFile.ToByteArray();
			VirtualDiskRF.write(TempByteArray);
			VirtualDiskRF.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * ��ô����еĿհ׿�index
	 * @param BlockNum
	 * @return null��ʾ��ȡʧ��
	 */
	public int[] GetEmptyBlockIndex(int BlockNum){
		try {
			int[] desIntArray = new int[BlockNum];
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			int j = 0;
			for (int i = 0;i < 16;i ++){
				if (VirtualDiskRF.readShort() == 0x0000){
					desIntArray[j] = i;
					j ++;
					if (j == BlockNum){
						break;
					}
				}
			}
			VirtualDiskRF.close();
			if (j == BlockNum){
				return desIntArray;
			}
			else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public FileSystem(String FilePath){
		VirtualDisk = new File(FilePath);
		if (!VirtualDisk.canRead()){
			System.out.println("�ļ�ϵͳ��ʼ������ָ������������ļ�" + FilePath + "�����ڻ��߲��ɶ���");
			this.Format();
		}
		/*
		 * ��ʼ��rootFCB
		 */
		Root = new FCB();
		Root.Name = new String(".");
		Root.SetTime();
		Root.FirstBlock = 0;
		Root.Type = 2;
		/*
		 * CurrFile����Ϊroot
		 */
		CurrFile = Root;
	}
	
	public boolean Format(){
		try {
			RandomAccessFileCpp VirtualDiskRF = new RandomAccessFileCpp(VirtualDisk.getAbsolutePath(),"rw");
			/**
			 * д��FAT����16*2�ֽڡ�
			 */
			for (int i = 0;i < 16;i ++){
				//д��short 2�ֽ�
				VirtualDiskRF.writeShort(0);
			}
			/**
			 * ����FAT��0������
			 */
			long TempFileP = VirtualDiskRF.getFilePointer();
			VirtualDiskRF.seek(0);
			VirtualDiskRF.writeShort(0xffff);
			VirtualDiskRF.seek(TempFileP);
			/**
			 * д���ļ��飬16*1024�ֽ�
			 */
			for (int i = 0;i < 16;i ++){
				//д��512��short
				for (int j = 0;j < 512;j ++){
					VirtualDiskRF.writeShort(0);
				}
			}
			VirtualDiskRF.close();
			return true;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println(VirtualDisk.getAbsolutePath()+ "�ļ�û���ҵ����߲��ɶ�д��");
			return false;
		}
	}
}
