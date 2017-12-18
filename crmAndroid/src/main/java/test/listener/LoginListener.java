package test.listener;

import java.util.Objects;

/**
 * Created by yueshaojun988 on 2017/9/8.
 */

public interface LoginListener {
    void onSuccess(Object successObj);
    void onError(Object errorObj);
}
