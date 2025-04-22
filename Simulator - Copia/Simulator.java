import java.util.*;
import java.lang.*;
import java.io.*;

public class Simulator
{
  public static void main (String[] args)
  {
    // Aprire e leggere il file input.txt;
    Scanner console1 = null;
    float[] lamb_arr = null;
    float[] lamb_ser = null;
    long[] sed_ar = null;
    long[] sed_se = null;

    int n_server = 0;
    int n_category = 0;
    int n_job = 0;
    int n_rip = 0;
    int type_policy = 0;

      try
    {
      FileReader myFile = new FileReader (args[0]);
        console1 = new Scanner (myFile);
      // leggere la prima riga del file
        console1.useDelimiter (",|\\n");
        n_server = Integer.parseInt (console1.next ());
        n_category = Integer.parseInt (console1.next ());
        n_job = Integer.parseInt (console1.next ());
        n_rip = Integer.parseInt (console1.next ());
        type_policy = Integer.parseInt (console1.next ());
        lamb_arr = new float[n_category];
        lamb_ser = new float[n_category];
        sed_ar = new long[n_category];
        sed_se = new long[n_category];

      for (int i = 0; i < n_category; i++)
	{
	  lamb_arr[i] = Float.parseFloat (console1.next ());
	  lamb_ser[i] = Float.parseFloat (console1.next ());
	  sed_ar[i] = Long.parseLong (console1.next ());
	  sed_se[i] = Long.parseLong (console1.next ());
	}
      myFile.close ();
      console1.close ();
    }
    catch (FileNotFoundException e)
    {
      System.out.println (e);
      System.exit (1);
    }
    catch (IOException e)
    {
      System.out.println (e);
      System.exit (1);
    }
    
    for (int i = 0; i < n_category; i++)
	{
	  Random r1 = new Random(sed_ar[i]);
	  Random r2 = new Random(sed_se[i]);
	}

    Coda[] servers = new Coda[n_server];
    Category[] type_cat = new Category[n_category];
    for (int i = 0; i < n_category; i++)
      {
    	Random r1 = new Random(sed_ar[i]);
	    Random r2 = new Random(sed_se[i]);
	    type_cat[i] = new Category (i, lamb_arr[i], lamb_ser[i], r1, r2);
      }

    for (int i = 0; i < n_server; i++)
      {
	servers[i] = new Coda ();
      }

    int[] aqt_num = new int[n_category];
    float[] aqt_time = new float[n_category];
    float[] aser_time = new float[n_category];
    float end_time = 0;

    System.out.print (n_server + ",");
    System.out.print (n_category + ",");
    System.out.print (n_job + ",");
    System.out.print (n_rip + ",");
    System.out.println (type_policy);
    
    for (int j = 0; j < n_rip; j++)
      {
        int n_served = 0;
        int n_queued=0;
	    PriorityQueue < Job > pQueue = new PriorityQueue < Job > ();
	    
	for (int t = 0; t < n_category; t++)
	  {
	    Event e = new Event ();
	    e.setCategory (type_cat[t]);
	    float lam_arr = type_cat[t].getLambda_arr ();
	    Random d = type_cat[t].getRand_arr();
	    float s = d.nextFloat ();
	    float f = (float) (1 / lam_arr);
	    float time_arr = -f * (float) Math.log (1 - s);
	    Job first_jobs = new Job (time_arr, e);
	    // inserire nella priority queueu l'arrivo dei primi job
	    pQueue.add (first_jobs);
	  }
	  
	while (!(n_served==n_job))
	  {
	    //estraggo il prossimo evento dalla pQueue
	    Job p = pQueue.poll ();
	    float plus_time = p.getTime ();

	    if (n_rip == 1 && n_job < 11 && type_policy == 0 && ((p.getEvent().getStatus() == 0 && n_queued < n_job) || p.getEvent().getStatus() == 1))
	      {
		System.out.print("time of occurrance: " + p.getTime ()+" ");
		if (p.getEvent ().getStatus () == 1)
		  {
		    System.out.print("service time: " + p.getEvent ().getT_service ()+" ");
		  }
		else
		  {
		    System.out.print(0);
		  }
		    System.out.println("category: " + p.getEvent ().getCategory ().getType ());
	      }

	    if (p.getEvent().getStatus() == 0 && n_queued < n_job)
	      {
		Event o = new Event ();
		o.setStatus (1);
		o.setCategory (p.getEvent ().getCategory ());
		//inserire il riconoscimento della policy
		int position =0;
		if(type_policy==0){ 
		    position = n_queued % n_server;
		}
		else if(type_policy==1) position = find_min_ser(servers, n_server, p.getTime());
		n_queued++;
		//aumento di 1 il numero di job creati di questa categoria
		aqt_num[p.getEvent().getCategory ().getType ()] = aqt_num[p.getEvent().getCategory ().getType ()] + 1;
		o.setServer (position);
		Job new_job = new Job (p.getTime (), o);
		servers[position].enqueue (new_job);
		//		if(n_queued < n_job){}
        //calcolo l'arrivo del prossimo job di questa categoria
		    Event e = new Event ();
		    e = p.getEvent ();
		    e.setStatus (0);
		    Job first_arrivals = new Job (new_time_arr (p, plus_time), e);
		    pQueue.add (first_arrivals);

		//se la coda C( vuota allora 
		if (servers[position].size()== 1)
		  {
		    Job w = servers[position].getFront ();
		    Event u = new Event ();
		    u.setCategory (w.getEvent ().getCategory ());
		    u.setStatus (1);
		    u.setServer (position);
		    float tm = new_time_ser (w, plus_time);
		    aser_time[u.getCategory ().getType ()] = aser_time[u.getCategory ().getType ()] + (tm - plus_time);
		    u.setT_service (tm - plus_time);
		    w.setEvent (u);
		    w.setTime (tm);
		    pQueue.add (w);
		  }
	      }
	    else if (p.getEvent().getStatus () == 1)
	      {//cancello dal server il primo job perché é stato servito
		    servers[p.getEvent ().getServer ()].dequeue();
		    n_served++;
		if (!(servers[p.getEvent ().getServer ()].size () == 0))
		  {
		    Job w = servers[p.getEvent ().getServer ()].getFront ();
		    Event e = new Event ();
		    e = w.getEvent ();
		    e.setStatus (1);
		    float tm = new_time_ser (w, plus_time);
		    e.setT_service (tm - plus_time);
		    aser_time[e.getCategory ().getType ()] = aser_time[e.getCategory ().getType ()] + (tm - plus_time);
		    //inserisco il tempo che ha passato in coda il job
		    aqt_time[e.getCategory ().getType ()] = aqt_time[e.getCategory ().getType ()] + (plus_time - w.getTime ());
		    w.setEvent (e);
		    w.setTime (tm);
		    pQueue.add (w);
		  }
	      }
	    if (n_served==n_job) end_time = end_time + p.getTime ();
	  }
      }
	  
	System.out.println ("average ending time: " + (end_time / n_rip) + "\n");

    float y = 0;
    for (int i = 0; i < n_category; i++)
      {
	    y = y + aqt_time[i];
      }

    System.out.println("average queueing time: " + (y / (n_job*n_rip))+"\n");

    for (int i = 0; i < n_category; i++)
      {
	    System.out.print("category type: " + i+" ");
	    System.out.print("number of jobs of this category: " +((float)aqt_num[i]/n_rip) +" ");
	    System.out.print("average queueing time: " +((float)aqt_time[i]/aqt_num[i])+" ");
        System.out.println("average service time: " +((float)aser_time[i]/(aqt_num[i])));
      }
  }

  public static float new_time_arr (Job p, float plus_time)
  {
    float lam_arr = p.getEvent ().getCategory ().getLambda_arr ();
    Random r = p.getEvent ().getCategory ().getRand_arr ();
    float e = r.nextFloat();
    float f = (float) (1 / lam_arr);
    float time_arr = -f * (float) Math.log (1 - e);
    return time_arr + plus_time;
  }
  public static float new_time_ser (Job w, float plus_time)
  {
    float lam_ser = w.getEvent ().getCategory ().getLambda_ser ();
    Random r = w.getEvent ().getCategory ().getRand_ser ();
    float a = r.nextFloat ();
    float b = (float) (1 / lam_ser);
    float time_ser = -b * (float) Math.log (1 - a);
    return time_ser + plus_time;
  }
  public static int find_min_ser(Coda[] j, float j_size, float curr_time){
  float min = Float.POSITIVE_INFINITY;
  float curr = 0;
  int i = 0;
  int min_ser = 0;

  boolean done = false;
  while (!done)
    {
      if (!j[i].isEmpty())
	{
	  int size = j[i].size();
	  Job job = (Job)j[i].dequeue();
	  j[i].enqueue(job);
	  curr = job.getTime() - curr_time;
	  for (int t = 0; t < (size - 1); t++)
	    {
	      job = (Job)j[i].dequeue();
	      j[i].enqueue(job);
	      curr = curr + (1 / job.getEvent().getCategory().getLambda_ser());
	    }
	  if (curr < min)
	    {
	      min = curr;
	      min_ser = i;
	    }
	  i++;
	  if (i == j_size) done = true;
	}
      else
	{
	  min_ser = i;
	  done = true;
	}
    }
  return min_ser;
  }
}
