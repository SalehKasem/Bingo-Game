package application;

public class BufferArray implements Buffer{
	public int playersNum;
	int array[]; //reference o array
	private int occupied=0; //key
	
	public BufferArray(int playersNum) {
		this.playersNum= playersNum;
		array = new int[playersNum+1]; // create array with size of players number
	}
	
	@Override
	public synchronized void set(int val) throws InterruptedException{
		while(occupied !=0) {
			wait(); //there is no player wrote to the buffer yet so the manager wait
		}
		array[val] = 1; //writ to buffer
		occupied=1; //unlock the key
		notifyAll(); //wake up all the blocking thread 
	}

	@Override
	public synchronized int get() throws InterruptedException{
		int i;
		while(occupied==0) {
			wait(); //block all the players threads
		}
		//check witch player presses bingo
		for(i=1; i<=playersNum; i++){
			if(array[i] != 0) {
				occupied =0; //release the key
				notifyAll(); //wake up all threads
				return i; //return the player id witch press bingo
			}
				
		}
		//still no one presses bingo so block the manager and release all the players
		occupied=0;
		notifyAll();
		return 0;
	}
	

}
