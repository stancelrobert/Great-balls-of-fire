package business.model;

import java.io.Serializable;

/**
 * Created by Robert on 11.04.2017.
 */
public class ConnectionData<T extends Serializable> implements Serializable {
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    T data;

    public ConnectionData() {}

    public ConnectionData(T data) {
        this.data = data;
    }



}
