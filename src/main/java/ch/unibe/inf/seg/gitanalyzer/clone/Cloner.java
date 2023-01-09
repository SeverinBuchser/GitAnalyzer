package ch.unibe.inf.seg.gitanalyzer.clone;

public interface Cloner<T> {
    void call(T toClone);
}
