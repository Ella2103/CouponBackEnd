package jb.CouponsBack.exceptions;

public class ExpiredCouponException extends Exception{
    public ExpiredCouponException() {
        super("Sorry!It seems to be expired.");
    }
}
