# Basahero App

**Basahero** is an Android app designed for book lovers. It allows users to browse, add to reading lists, and review books. With features like user registration, personalized reading lists, and book data powered by the Penguin Random House API, Basahero makes it easy to manage your reading journey.

## Features

- **User Registration and Login:** Create an account with name, email, and password, and log in to access your personalized reading experience.
- **Browse and Search Books:** View books by category (e.g., New Arrivals, Top Rated, Recommended), and search by title, author, or keyword.
- **Book Details:** View detailed information for each book, including cover image, synopsis, author info, and ratings.
- **Reading Lists:** Organize your books into personal lists—Want to Read, Reading, and Completed. Rate and review books you’ve finished.
- **User Profile Management:** View and edit personal account details, including name, username, email, and password.
- **Logout:** Securely log out to return to the login screen.

## Tech Stack:

- **Frontend:** Android (Java)
- **Backend:** Supabase (PostgreSQL, Authentication, File Storage)

## Setup Instructions:

### Prerequisites:

- Android Studio
- JDK 8 or higher

### Steps to Set Up:

1. **Clone the repository:**.

   ```bash
   git clone https://github.com/hello-i5a/basahero-app.git
   ```

2. **Open the project in Android Studio**.

3. **Install dependencies:**

   - Ensure that all required dependencies are set up in the `build.gradle.kts` files (both project-level and app-level).

4. **Build and Run the App:**
   - Build the project and run it on an emulator or physical device with **API 24 (Nougat)** or higher.

### Configuration:

- **Package Name:** `com.example.basaheroapp`
- **Minimum SDK:** API 24 (Android 7.0, Nougat)
- **Build Configuration Language:** Kotlin DSL (`build.gradle.kts`)

## Notes:

- **This app is a work in progress**. Features and improvements are continuously being added.
