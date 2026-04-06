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

!Start
![Screenshot_20260406_212737_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/fd7f2f0e-ffb9-48f5-ae65-a288988cd33e)

![Home]
![Screenshot_20260406_212744_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/f0fe107f-8117-4c0e-8d19-86ef648efa32)

![Transactions]
![Screenshot_20260406_212811_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/dd0aedb2-3b67-403e-af37-ec44125b3c3a)

![Insights, Edits, Delete]
![Screenshot_20260406_212828_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/f955c75e-e759-4bcf-bd73-91d5fbe48a31)

![Screenshot_20260406_212816_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/08865a14-f28a-4f9a-b7bb-a72674ec3e35)

![Screenshot_20260406_212819_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/4d7cef51-0605-4974-bfe6-79e4b631dd0b)

![Screenshot_20260406_212822_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/dd11ae56-dda9-485a-b9e7-4aae25f31de0)

![Goal]
![Screenshot_20260406_212831_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/daa77114-e987-47a7-8214-2889a9413013)

![Screenshot_20260406_212833_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/bd42101b-cdea-4ae2-9641-36ef3464d543)

![Settings]
![Screenshot_20260406_212840_Personal Finance Companion Mobile App](https://github.com/user-attachments/assets/1b097f3d-52da-4739-87b5-c64978e10fe6)

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable recommended)
- Android SDK installed
- JDK 11
- Android device/emulator (API 32+)

### Clone & Open

```bash
git clone <https://github.com/Soumyajit-Chatterjee/PersonalFinanceCompanionMobileApp.git>
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

## 👤 Author

Made with care for practical daily money tracking.  
If this project helps you, feel free to ⭐ the repo.

