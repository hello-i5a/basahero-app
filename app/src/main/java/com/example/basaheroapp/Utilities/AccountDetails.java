package com.example.basaheroapp.Utilities;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AccountDetails {

    private static AccountDetails instance;
    private String accKey;
    private String name;
    private String username;
    private String email;
    private String id; // Local variable to store the name
    private boolean nameFetched = false;
    private boolean IDFetched = false;// Flag to check if the name is fetched
    private boolean usernameFetched = false;
    private boolean emailFetched = false;

    public static synchronized AccountDetails getInstance(String accKey) {
        if (instance == null) {
            instance = new AccountDetails(accKey);
        }
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public AccountDetails(String accKey) {
        // Call getName during initialization
        this.accKey = accKey;
        try {
            getName(accKey);
            getID(accKey);
            getUsername(accKey);
            getEmail(accKey);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to fetch the name from the database
    public void getName(String accKey) throws InterruptedException {
        if (nameFetched) {
            return; // If the name has already been fetched, no need to call it again
        }

        Database db = new Database();

        // Create a CountDownLatch with a count of 1 (because we are waiting for one callback)
        CountDownLatch latch = new CountDownLatch(1);

        db.getInfo(accKey, "name", new Database.GetCallback() {
            @Override
            public void onResult(String info) {
                name = info; // Store the name in the local variable
                nameFetched = true; // Set the flag to true indicating the name is fetched
                latch.countDown(); // Notify the waiting thread that the callback is done
            }

            @Override
            public void onFailure(IOException e) {
                // Handle failure (you could log or set a default name here)
                name = null;
                nameFetched = false;
                latch.countDown(); // Even in case of failure, we signal completion
            }
        });

        // Wait for the callback to complete (this will block the current thread)
        latch.await();
    }

    public void getID(String accKey) throws InterruptedException {
        if (IDFetched) {
            return; // If the name has already been fetched, no need to call it again
        }

        Database db = new Database();

        // Create a CountDownLatch with a count of 1 (because we are waiting for one callback)
        CountDownLatch latch = new CountDownLatch(1);

        db.getInfo(accKey, "id", new Database.GetCallback() {
            @Override
            public void onResult(String info) {
                id = info; // Store the name in the local variable
                IDFetched = true; // Set the flag to true indicating the name is fetched
                latch.countDown(); // Notify the waiting thread that the callback is done
            }

            @Override
            public void onFailure(IOException e) {
                // Handle failure (you could log or set a default name here)
                id = null;
                IDFetched = false;
                latch.countDown(); // Even in case of failure, we signal completion
            }
        });

        // Wait for the callback to complete (this will block the current thread)
        latch.await();
    }

    public void getUsername(String accKey) throws InterruptedException {
        if (usernameFetched) {
            return; // If the name has already been fetched, no need to call it again
        }

        Database db = new Database();

        // Create a CountDownLatch with a count of 1 (because we are waiting for one callback)
        CountDownLatch latch = new CountDownLatch(1);

        db.getInfo(accKey, "username", new Database.GetCallback() {
            @Override
            public void onResult(String info) {
                username = info; // Store the name in the local variable
                IDFetched = true; // Set the flag to true indicating the name is fetched
                latch.countDown(); // Notify the waiting thread that the callback is done
            }

            @Override
            public void onFailure(IOException e) {
                // Handle failure (you could log or set a default name here)
                username = null;
                IDFetched = false;
                latch.countDown(); // Even in case of failure, we signal completion
            }
        });

        // Wait for the callback to complete (this will block the current thread)
        latch.await();
    }

    public void getEmail(String accKey) throws InterruptedException {
        if (emailFetched) {
            return; // If the name has already been fetched, no need to call it again
        }

        Database db = new Database();

        // Create a CountDownLatch with a count of 1 (because we are waiting for one callback)
        CountDownLatch latch = new CountDownLatch(1);

        db.getInfo(accKey, "email", new Database.GetCallback() {
            @Override
            public void onResult(String info) {
                email = info; // Store the name in the local variable
                IDFetched = true; // Set the flag to true indicating the name is fetched
                latch.countDown(); // Notify the waiting thread that the callback is done
            }

            @Override
            public void onFailure(IOException e) {
                // Handle failure (you could log or set a default name here)
                email = null;
                IDFetched = false;
                latch.countDown(); // Even in case of failure, we signal completion
            }
        });

        // Wait for the callback to complete (this will block the current thread)
        latch.await();
    }

    // Getter for the name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameFetched = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        IDFetched = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.usernameFetched = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.emailFetched = true;
    }
}
