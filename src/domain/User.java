package domain;

public class User  {
    private String name;
    private int sodaConsumed = 0;
    
    public User(String p_name)  {
        this.name = p_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String p_name) {
        name = p_name;
    }
    
    public void acceptSodaProduct(SodaProduct soda) {
        ++sodaConsumed;        
    }       
}
