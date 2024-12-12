package com.example.basaheroapp.Utilities;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GetBooks {

    private String newBooks;

    public String getNewBooks() {
        return newBooks;
    }

    public GetBooks() {
        try {
            getNew();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void getNew() throws InterruptedException {

        // Create a CountDownLatch with a count of 1 (because we are waiting for one callback)
        CountDownLatch latch = new CountDownLatch(1);

        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("storage").callAttr("getNewArrival");
        newBooks = pyObject.toString();
        latch.countDown();

        // Wait for the callback to complete (this will block the current thread)
        latch.await();
    }



}
