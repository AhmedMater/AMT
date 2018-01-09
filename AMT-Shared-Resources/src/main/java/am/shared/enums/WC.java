package am.shared.enums;

/**
 * Created by ahmed.motair on 1/7/2018.
 */
public enum WC {
    AMT_0000;

    @Override
    public String toString() {
        return super.toString().replaceAll("_","-");
    }

    public Boolean equals(WC warn) {
        return this.toString().equals(warn.toString());
    }
}
