public class Job implements Comparable
{
	public Job(){
	    time = 0;
	    stat = null;
	}
	
	public Job(float t, Event a){
	    time = t;
	    stat = a;
	}
	
	public void setTime(float n){
		time = n;
	}
	public float getTime(){
	    return time;
	}
	
	public void setEvent(Event e){
	    stat = e;
	}
	public Event getEvent(){
	    return stat;
	}
	
	public int compareTo(Object o){
	    Job a = (Job)o;
	    if(time == a.getTime()) return 0;
	    else if(time < a.getTime()) return -1;
	    return 1;
	}
	
	private float time;
	private Event stat;
}