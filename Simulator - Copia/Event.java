public class Event{
    
    public Event(){
        status = 0;
        server = 0;
        TypeCategory = null;
	    t_service = (float)0;
    }
    
    	public void setCategory(Category a){
	    TypeCategory = a;
	}
	public Category getCategory(){
	    return TypeCategory;
	}

	public void setT_service(float a){
		t_service =a;
	}
	public float getT_service(){
		return t_service;
	}
	
	public void setStatus(int a){
	    status = a;
	}
	public int getStatus(){
	    return status;
	}
	
	public void setServer(int a){
	    server = a;
	}
	public int getServer(){
	    return server;
	}
    

	//indica il tipo di categoria
	private Category TypeCategory;
		// vale 0 se il job è arrivato e deve essere inserito il una coda e 1 se il job è stato servito
	private int status;
	// indica in che server il job è stato servito va da 0 a k-1 
	private int server;
	//tengo traccia del tempo di servizio
	private float t_service;
}
