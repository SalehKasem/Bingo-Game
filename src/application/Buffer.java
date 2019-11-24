package application;

public interface Buffer{
	//set x to buffer
	public void set(int val) throws InterruptedException;
	//get x from buffer 
	public int get() throws InterruptedException;
}
