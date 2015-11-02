import java.util.Set;

/**
 * Created by jmitch on 11/1/2015.
 */
public interface OnFollowSetUpdatedListener {
    void onFollowSetDetermined(String symbol,Set<String> followSymbols);
    void onFollowSetNotDetermined(String callbackFollowSymbol,String ruleToUpdate);
}
