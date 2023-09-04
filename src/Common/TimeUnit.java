package Common;

public enum TimeUnit {
	 Microsecond,
	 Millisecond, 
	 Second;
	 public int toInt(){
		 return this.ordinal();
	 }
}