
# 📚 FlashCards

**FlashCards** is an Android application built with **Kotlin** and **Jetpack Compose** that allows users to generate, study, and quiz themselves on flashcards. The app supports topic-based flashcards, PDF upload for automatic card creation, and interactive quiz features. It uses **Room** for local data storage, **Supabase** for authentication, and a backend API for PDF processing and flashcard generation.

---

## ✨ Features

- 🗂️ **Topic-Based Flashcards:** Organize your study materials by topic.
- 📄 **PDF Upload and Parsing:** Upload a PDF document to automatically generate flashcards and quizzes from its content.
- 🔍 **Search and Navigation:** Intuitive navigation with search bars for quickly finding topics and cards.
- 🧠 **Quizzes and Exams:** Practice and test your knowledge using flashcards in quiz mode.
- 🔐 **Authentication:** Secure login and sign-up using Supabase authentication.
- 💾 **Local and Remote Data:** Flashcards are stored locally using Room and can be synchronized with the backend.

---

## 🛠️ Tech Stack

- **Language:** Kotlin 🧑‍💻
- **UI:** Jetpack Compose, Material3 🎨
- **Persistence:** Room Database 🗄️
- **Networking:** Retrofit, OkHttp, kotlinx.serialization 🌐
- **Dependency Injection:** Dagger Hilt 🧩
- **Authentication:** Supabase 🔐
- **PDF Processing:** Backend API (see `BASE_URL` in `NetworkModule.kt`) 📄

---

## 🚀 Setup Instructions

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/pranav5127/FlashCards.git
   cd FlashCards
   ```

2. **Configure Supabase and API Keys:**
   Create a `local.properties` file in the project root with the following fields:
   ```properties
   SUPABASE_URL=your_supabase_url
   SUPABASE_KEY=your_supabase_key
   SUPABASE_WEB_CLIENT_ID=your_web_client_id
   SUPABASE_ANDROID_CLIENT_ID=your_android_client_id
   ```
   These keys are used for authentication and API access.

3. **Open in Android Studio:**
   Open the project in Android Studio (Electric Eel or newer recommended) 🛠️

4. **Build and Run:**
   Sync Gradle and run the app on an emulator or physical device 📱

---

## 📲 Usage

- 🔑 **Sign Up / Log In:** Start the app and create an account or log in using your Supabase credentials.
- ➕ **Add Topics:** Use the interface to add new topics for your study materials.
- 📤 **Upload PDF:** On the PDF upload screen, select and upload a PDF to generate flashcards and quizzes.
- 🔍 **Search & Study:** Use the search bar to filter topics or cards, and review flashcards.
- 📝 **Quiz Mode:** Select a topic and start a quiz to test your memory and retention.

---

## 🧭 Project Structure

```
📦 app/src/main/java/com/sugardevs/flashcards/
├── 📁 data/local         - Room database and local repositories
├── 📁 data/network       - Retrofit API and network repositories
├── 📁 ui/components      - Jetpack Compose UI components
├── 📁 navigation         - Navigation and scaffold logic
└── 📁 ui/screens         - Main app screens (PDF upload, quiz, card grid, etc)
```

---

## 🤝 Contribution Guidelines

1. **Fork the Repository** and create your branch.
2. **Open a Pull Request** with a clear description of your changes.
3. **Follow Kotlin and Jetpack Compose best practices.**
4. **Write clear commit messages** and document your code where necessary.

---

📬 **For questions or support**, open an issue in the [GitHub repository](https://github.com/pranav5127/FlashCards).
