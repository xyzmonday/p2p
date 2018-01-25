package com.yff.p2p.rcp;



public class ReferenceConfig<T> {
    private Class<?> interfaceClass;
    //代理类的引用
    private T ref;

    public synchronized T get() {
        if (ref == null) {
            init();
        }
        return ref;
    }
    private void init() {
        ref = (T) new ProxyFactory(interfaceClass).getProxyObject();
    }


    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }
}
