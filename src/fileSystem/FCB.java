package fileSystem;

import java.text.SimpleDateFormat;
import java.util.Date;

import RandomFileCpp.RandomAccessFileCpp;

/**
 * ��������,javaһ������Ҳ��һ��char
 * ��8+4+4+1+15=32�ֽ�
 * @author ط��
 *
 */
public class FCB {
	/**
	 * ���浽�ļ�ʱ��8�ֽ�
	 */
	String Name;
	/**
	 * ���浽�ļ�ʱ��4�ֽ�
	 */
	int Size;
	/**
	 * ���浽�ļ�ʱ��4�ֽڣ���Ȼ2�ֽھ͹�����
	 */
	int FirstBlock;
	/**
	 * ���浽�ļ�ʱ��1�ֽ�
	 * TypeΪ1��ʾ�ļ���Ϊ2��ʾĿ¼
	 * Ϊ0��ʾ�Ѿ�ɾ��Ŀ¼
	 */
	char Type;
	/**
	 * �������ڴ�����14�ֽڣ����浽�ļ�ʱ��15�ֽ�,���һλ��0���
	 * ��ʽΪyyyymmddhhmmss
	 */
	char[] DateTime;
	
	static public void main(String[] args){
		FCB test = new FCB();
		test.SetTime();
	}
	
	/**
	 * ���ļ��л�õ�32bit���ݹ���FCB
	 * @param src
	 */
	public FCB(byte[] src){
		/*
		 * debug
		 */
		/*
		for (int i = 0;i < 32;i ++){
			System.out.print(src[i] + " ");
		}
		System.out.println();
		*/
		/*
		 * ����name
		 */
		StringBuffer TempStrBuffered = new StringBuffer("");
		for (int i = 0;i < 8 && src[i] != 0;i ++){
			TempStrBuffered.append((char)src[i]);
		}
		this.Name = new String(TempStrBuffered);
		/*
		 * ����size
		 */
		this.Size = RandomAccessFileCpp.unsignedByteToInt(src[8]) + RandomAccessFileCpp.unsignedByteToInt(src[9]) * 256 + 
				RandomAccessFileCpp.unsignedByteToInt(src[10]) * 256 * 256 + RandomAccessFileCpp.unsignedByteToInt(src[11]) * 256 * 256 * 256;
		/*
		 * ����FirstBlock
		 */
		this.FirstBlock = RandomAccessFileCpp.unsignedByteToInt(src[12]) + RandomAccessFileCpp.unsignedByteToInt(src[13]) * 256 + 
				RandomAccessFileCpp.unsignedByteToInt(src[14]) * 256 * 256 + RandomAccessFileCpp.unsignedByteToInt(src[15]) * 256 * 256 * 256;
		/*
		 * ����type
		 */
		this.Type = (char) src[16];
		/*
		 * ����DateTime
		 */
		this.DateTime = new char[14];
		for (int i = 0;i < 14;i ++){
			this.DateTime[i] = (char) src[17 + i];
		}
	}
	
	/**
	 * ��FCBת����32�ֽڵ�byte���飬����д���ļ�
	 * @return
	 */
	public byte[] ToByteArray(){
		byte[] desByte = new byte[32];
		/*
		 * ת��name
		 */
		char[] TempCharArray = this.Name.toCharArray();
		for (int i = 0;i < 8 && i < TempCharArray.length;i ++){
			desByte[i] = (byte) TempCharArray[i];
		}
		/*
		 * ת��size
		 */
		desByte[8] = (byte) (this.Size % 256);
		desByte[9] = (byte) (this.Size / 256 % 256);
		desByte[10] = (byte) (this.Size / 256 / 256 % 256);
		desByte[11] = (byte) (this.Size / 256 / 256 / 256 % 256);
		/*
		 * ת��FirstBlock
		 */
		desByte[12] = (byte) (this.FirstBlock % 256);
		desByte[13] = (byte) (this.FirstBlock / 256 % 256);
		desByte[14] = (byte) (this.FirstBlock / 256 / 256 % 256);
		desByte[15] = (byte) (this.FirstBlock / 256 / 256 / 256 % 256);
		/*
		 * ת��type
		 */
		desByte[16] = (byte) this.Type;
		/*
		 * ת��dateTime
		 */
		for (int i = 0;i < 14;i ++){
			desByte[17 + i] = (byte) this.DateTime[i];
		}
		desByte[31] = 0;
		return desByte;
	}
	
	public FCB() {
		this.Name = "";
		this.Size = 0;
		this.FirstBlock = 0;
		this.Type = 0;
		this.SetTime();
	}

	public void SetTime(){
		Date CurrDate = new Date();
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		DateTime = DateFormat.format(CurrDate).toCharArray();
		
		/*
		 * debug
		 */
		/*
		for (int i = 0;i < DateTime.length;i ++){
			System.out.println(DateTime[i]);
		}
		*/
	}
}
