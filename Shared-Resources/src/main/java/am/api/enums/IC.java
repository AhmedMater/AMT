package am.api.enums;

/**
 * Created by ahmed.motair on 9/8/2017.
 */
public enum IC {
    AMT_0001, AMT_0002, AMT_0003, AMT_0004, AMT_0005, AMT_0006, AMT_0007, AMT_0008, AMT_0009, AMT_0010,
    AMT_0011, AMT_0012, AMT_0013, AMT_0014, AMT_0015, AMT_0016, AMT_0017, AMT_0018, AMT_0019, AMT_0020 ;

    private String value;

    IC(String value){
        this.value = value;
    }
    IC(){
    }
    public String value(){
        return value;
    }
}
