package deviceSystem;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import ProcessSystem.Process;

public class IONode {
	public String    name;
	/*
	 * ��ʹ������ķ�ʽ��ֱ��ʹ��ArrayList<>
	 */
	//public IONode   next;
	
	public Process   process;
	public Queue<Process>  waitinglist = new LinkedList<Process>();
	public IONode   parent;
}
