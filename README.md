# Personal Finance Companion Mobile App 💸📱

An Android app to track day-to-day income and expenses, keep an eye on spending habits, and stay motivated with savings goals.

Built with Jetpack Compose + Room + DataStore, with a clean UI and practical features for real-world personal finance tracking.

---

## ✨ Features

- **Dashboard overview**: quick look at total balance, income, expenses, and recent transactions.
- **Transaction management**: add, edit, search, filter, and delete transactions.
- **Category-based tracking**: organize expenses/income by category with a visual selector.
- **Insights screen**: spending breakdown + top spending category.
- **Savings goal**: set/update your monthly target and track progress.
- **Settings**:
  - currency selection (`$`, `€`, `£`, `₹`)
  - light / dark / system theme
  - biometric lock toggle
  - daily reminder notifications
  - CSV export for backups/share

---

## 🧰 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (ViewModel + Repository)
- **Local Database**: Room
- **Preferences**: DataStore
- **Background Work**: WorkManager
- **Security**: BiometricPrompt
- **Navigation**: Navigation Compose

---

## 🧭 Main App Sections

- `Home` → balance summary + recent transactions + savings snapshot
- `Transactions` → full list, filters, search, swipe actions, expense donut chart
- `Insights` → category-wise spending analysis
- `Goal` → monthly savings target
- `Settings` → personalization, biometric lock, notifications, CSV export

---

## 📸 Screenshots

Add your screenshots here before uploading:

```md
![Home](screenshots/home.png)
![Transactions](screenshots/transactions.png)
![Insights](screenshots/insights.png)
![Goal](screenshots/goal.png)
![Settings](screenshots/settings.png)
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable recommended)
- Android SDK installed
- JDK 11
- Android device/emulator (API 32+)

### Clone & Open

```bash
git clone <your-repo-url>
cd PersonalFinanceCompanionMobileApp
```

Open the project in Android Studio, let Gradle sync, then run the `app` module.

### Build Debug APK (optional)

```bash
./gradlew assembleDebug
```

APK output:

`app/build/outputs/apk/debug/app-debug.apk`

---

## ⚙️ App Configuration Notes

- **Minimum SDK**: 32
- **Target SDK**: 36
- **Compile SDK**: 36
- Notification reminders are handled through `WorkManager` as periodic work.
- CSV export uses `FileProvider` and Android share intent.

---

## 🗂️ Project Structure (high-level)

```text
app/src/main/java/com/example/personalfinancecompanionmobileapp
├── data
│   ├── db
│   ├── model
│   └── repository
├── ui
│   ├── components
│   ├── navigation
│   ├── screens
│   └── theme
├── utils
└── MainActivity.kt
```

---

## 🛠️ Future Improvements

- proper import-from-CSV flow
- advanced charts for monthly trends
- recurring transactions
- cloud backup/sync
- language/localization support

---

## 🤝 Contributing

PRs, ideas, and bug reports are welcome.  
If you find something broken or weird, open an issue with steps to reproduce and I will check it out.

---

## 📄 License

You can use the MIT License (or replace this section with your preferred license before publishing).

---

## 👤 Author

Made with care for practical daily money tracking.  
If this project helps you, feel free to ⭐ the repo.

