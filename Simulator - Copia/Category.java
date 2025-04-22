import java.util.*;
public class Category
{
	public Category(){
	    lambda_arr = 0;
	    lambda_ser = 0;
	    seed_arr = 0;
	    seed_ser = 0;
	    type = 0;
	}
	
	public Category(int a, float b, float c, Random arr, Random ser ){
	    type = a;
	    lambda_arr = b;
	    lambda_ser = c;
	    rand_arr = arr;
	    rand_ser = ser;
	}
	
	public void setRand_arr(Random r){
        rand_arr = r;
    }
    public Random getRand_arr(){
        return rand_arr;
    }
    public void setRand_ser(Random r){
        rand_ser = r;
    }
    public Random getRand_ser(){
        return rand_ser;
    }
	
	public void setType(int a){
	    type = a;
	}
	public int getType(){
	    return type;
	}
	
	public void setLambda_arr(float a){
	    lambda_arr = a;
	}
	public float getLambda_arr(){
	    return lambda_arr;
	}
	
	public void setLambda_ser(float a){
	    lambda_ser = a;
	}
	public float getLambda_ser(){
	    return lambda_ser;
	}
	
	public void setSeed_arr(long a){
	    seed_arr = a;
	}
	public long getSeed_arr(){
	    return seed_arr;
	}
	
	public void setSeed_ser(long a){
	    seed_ser = a;
	}
	public long getSeed_ser(){
	    return seed_ser;
	}
	
	//l'oggetto random che produce i valori
    private Random rand_arr;
    private Random rand_ser;
	// indica il tipo della categoria con un numero da 0 a H-1
	private int type;
	// parametro della distribuzione esponenziale per l'arrivo di un job
	private float lambda_arr;
	// parametro della distribuzione esponenziale per il servizio di un job
	private float lambda_ser;
	// seme per la generazione randomica dell'attivo di un job
	private long seed_arr;
	// seme per la generazione randomica del servizio di un job
	private long seed_ser;
}
