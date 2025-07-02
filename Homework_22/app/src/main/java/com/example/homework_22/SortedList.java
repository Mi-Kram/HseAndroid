package com.example.homework_22;

import androidx.annotation.NonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class SortedList<T extends Comparable<? super T>> implements Iterable<T>, Iterator<T> {
    private ArrayList<T> internalList;


    public SortedList() {
        internalList = new ArrayList<T>();
    }

    public SortedList(SortedList<T> data) {
        internalList = new ArrayList<T>(data.internalList);
    }

    public SortedList(int capacity) {
        internalList = new ArrayList<>(capacity);
    }

    public int insert(T t) {
        for (int i = 0; i < internalList.size(); i++) {
            if (t.compareTo(internalList.get(i)) > 0) {
                internalList.add(i, t);
                return i;
            }
        }

        internalList.add(t);
        return internalList.size() - 1;
    }

    public T remove(int index) {
        return internalList.remove(index);
    }

    public int remove(T t) {
        for (int i = 0; i < internalList.size(); i++) {
            if (t.compareTo(internalList.get(i)) == 0) {
                internalList.remove(i);
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        internalList.clear();
    }

    public void pushEnd(T t) {
        internalList.add(t);
    }

    public T get(int i) {
        return internalList.get(i);
    }

    public int size() {
        return internalList.size();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        return null;
    }

    class MyIterator implements Iterator<T> {

        private int index = 0;

        public boolean hasNext() {
            return index < size();
        }

        public T next() {
            return get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported yet");
        }
    }
}