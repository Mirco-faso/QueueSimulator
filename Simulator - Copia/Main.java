import java.util.*;
import java.lang.*;
import java.io.*;

public class Main{
    public static void main(String[] args){
        // Aprire e leggere il file input.txt;
		Scanner console1 = null;
		Coda[] servers;
		int n_server=0;
		int n_category=0;
		Category[] type_cat;
		int n_job=0;
		int n_rip=0;
		int type_policy=0;
		
		try
		{
			FileReader myFile = new FileReader("input.txt");
			console1 = new Scanner(myFile);
			myFile.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		// leggere la prima riga del file
		console1.useDelimiter(",|\\n");
		console1.nextLine();
      	        n_server = Integer.parseInt(console1.next());
      		    servers = new Coda[n_server];
      		    n_category = Integer.parseInt(console1.next());
      		    type_cat = new Category[n_category];
      		    n_job = Integer.parseInt(console1.next());
      		    n_rip = Integer.parseInt(console1.next());
      		    type_policy = Integer.parseInt(console1.next());
      		    
      	int[] aqt_num = new  int[n_category];
      	float[] aqt_time = new float[n_category];
      	float end_time = 0;
      	
      	
      	for(int i = 0; i<n_category;i++){
      	    type_cat[i].setType(i);
      	    type_cat[i].setLambda_arr(Float.parseFloat(console1.next()));
      	    type_cat[i].setLambda_ser(Float.parseFloat(console1.next()));
      	    type_cat[i].setSeed_arr(Long.parseLong(console1.next()));
      	    type_cat[i].setSeed_ser(Long.parseLong(console1.next()));
      	    i++;
      	    console1.nextLine();
      	}
      	
      	console1.close();
      	
      	for(int j=0; j<n_rip;j++){
      	    PriorityQueue<Job> pQueue = new PriorityQueue<Job>();
      	    //tengo traccia delle medie dei tempi e dei job per categoria
      	    int[] cat = new  int[n_category];
      	    float[] tim = new float[n_category];
      	    
      	    for(int i =0; i<n_category;i++){
      	    cat[i]= 2;
      	    }
      	    
      	    int n_arrival = 0;
      	    for(int t=0; t<n_category; t++){
      	        Event e = new Event();
      	        e.setCategory(type_cat[t]);
      	        Job first_jobs = new Job((float)0, e);
      	        first_jobs.setTime(new_time_arr(first_jobs, (float)0));
      	        
      	        //inserire il riconoscimento della policy
      	        e.setServer(n_arrival%n_server);
      	        first_jobs.setEvent(e);
      	        servers[n_arrival%n_server].enqueue(first_jobs);
      	        n_arrival++;
      	        
      	        // inserire nella priority queueu il fine del job
      	        pQueue.add(first_jobs);
      	    }
      	    
      	    while(!pQueue.isEmpty()){
      	    Job p = pQueue.poll();
      	    float plus_time=p.getTime();
      	    if(n_rip==1 && n_job<11 && type_policy==0){
      	        System.out.println("time of occurrance: "+p.getTime());
      	        if(p.getEvent().getStatus()==1){System.out.println("service time: " + p.getEvent().getT_service());}
      	        else System.out.println(0);
      	        System.out.println("category: "+ p.getEvent().getCategory().getType());
      	    }
      	        
      	    if(p.getEvent().getStatus()==0){
      	        Event o = new Event();
      	        o.setStatus(1);
      	        o.setCategory(p.getEvent().getCategory());
      	        //inserire il riconoscimento della policy
      	        int position = n_arrival%n_server;
      	        o.setServer(position);
      	        Job new_job = new Job(p.getTime(), o);
      	        servers[position].enqueue(new_job);
      	        
      	        if(n_arrival<n_job){
      	            
      	            Event e = new Event();
      	            e = p.getEvent();
      	            e.setStatus(0);
      	            Job first_arrivals = new Job(new_time_arr(p, plus_time), p.getEvent());
      	            pQueue.add(first_arrivals);
      	            //aumento di 1 il numero di job creati di questa categoria
      	            aqt_num[e.getCategory().getType()] = aqt_num[e.getCategory().getType()]+1;
      	            n_arrival++;
      	        }
      	        
      	        //se la coda è vuota allora 
      	        if(servers[position].size()==1){
      	        
      	        Job w = servers[p.getEvent().getServer()].getFront();
      	        Event u = new Event();
      	        u.setCategory(w.getEvent().getCategory());
      	        u.setStatus(1);
      	        float tm = new_time_ser(w, plus_time);
      	        u.setT_service(tm);
      	        w.setEvent(u);
      	        w.setTime(tm);
      	        pQueue.add(w);
      	        }
      	    }
      	    
      	    else if(p.getEvent().getStatus()==1){
      	        //cancello dal server il primo job perchè è stato servito
      	        servers[p.getEvent().getServer()].dequeue();
      	        Job w = servers[p.getEvent().getServer()].getFront();
      	        
      	        
      	        Event e = new Event();
      	        e = w.getEvent();
      	        e.setStatus(1);
      	        float tm = new_time_ser(w, plus_time);
      	        e.setT_service(tm-plus_time);
      	        w.setEvent(e);
      	        //inserisco il tempo che ha passato in coda il job
      	        aqt_time[e.getCategory().getType()] = aqt_time[e.getCategory().getType()]+(tm-w.getTime());
      	        w.setTime(tm-plus_time);
      	        pQueue.add(w);
      	        
      	    }
      	    if(pQueue.isEmpty()) end_time = end_time + p.getTime();
      	    }
      	    
      	    for(int i =0; i<n_category;i++){
      	        aqt_num[i] += cat[i];
      	        aqt_time[i] += tim[i];
      	    }
      	    
      	}
      	
      	System.out.println("average ending time: " + (end_time/n_rip)+"\n");
      	int z =0;
      	for(int i=0; i<n_category;i++){
      	    z = z + aqt_num[i];
      	}
      	float y=0;
      	for(int i=0; i<n_category;i++){
      	    y = y +  aqt_time[i];
      	}
      	
      	System.out.println("average queueing time: " +(y/z));
      	
      	for(int i=0; i<n_category;i++){
      	    System.out.println("category type: "+i);
      	    System.out.println("number of jobs of this category: "+ (aqt_num[i]/n_rip));
      	    System.out.println("average queueing time: " + (aqt_time[i]/aqt_num[i]));
      	    
      	}
      	
      	
    }
    
    public static float new_time_arr(Job p, float plus_time){
        float lam_arr = p.getEvent().getCategory().getLambda_arr();
      	long seed_arr = p.getEvent().getCategory().getSeed_arr();
      	Random d = new Random(seed_arr);
      	float e = d.nextFloat();
      	float f = (float)(1/lam_arr);
      	float time_arr = -f*(float)Math.log(1-e);
      	return time_arr+plus_time;
    }
    public static float new_time_ser(Job w, float plus_time){
        float lam_ser = w.getEvent().getCategory().getLambda_ser();
      	long seed_ser = w.getEvent().getCategory().getSeed_ser();
        Random r = new Random(seed_ser);
      	float a = r.nextFloat();
      	float b = (float)(1/lam_ser);
      	float time_ser = -b*(float)Math.log(1-a);
      	return time_ser+plus_time;
    }
}








