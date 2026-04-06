# Personal Finance Companion Mobile App 💸📱

An Android app to track day-to-day income and expenses, keep an eye on spending habits, and stay motivated with savings goals.

Built with **Jetpack Compose + Room + DataStore**, featuring a clean UI and practical features for real-world personal finance tracking.

---

## ✨ Features

- **Dashboard overview**: Quick look at total balance, income, expenses, and recent transactions.
- **Transaction management**: Add, edit, search, filter, and delete transactions.
- **Category-based tracking**: Organize expenses/income by category with a visual selector.
- **Insights screen**: Spending breakdown + top spending category analysis.
- **Savings goal**: Set/update your monthly target and track progress in real-time.
- **Settings**:
  - Currency selection (`$`, `€`, `£`, `₹`)
  - Light / Dark / System theme support
  - Biometric lock toggle for privacy
  - Daily reminder notifications
  - CSV export for backups and sharing

---

## 📸 Screenshots

<table style="width: 100%;">
  <tr>
    <td align="center" width="20%">
      <b>Home</b><br/>
      <img src="https://github.com/user-attachments/assets/fd7f2f0e-ffb9-48f5-ae65-a288988cd33e" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Transactions</b><br/>
      <img src="https://github.com/user-attachments/assets/f0fe107f-8117-4c0e-8d19-86ef648efa32" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Insights</b><br/>
      <img src="https://github.com/user-attachments/assets/dd0aedb2-3b67-403e-af37-ec44125b3c3a" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Edit/Delete</b><br/>
      <img src="https://github.com/user-attachments/assets/f955c75e-e759-4bcf-bd73-91d5fbe48a31" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Category Picker</b><br/>
      <img src="https://github.com/user-attachments/assets/08865a14-f28a-4f9a-b7bb-a72674ec3e35" width="200"/>
    </td>
  </tr>
  <tr>
    <td align="center" width="20%">
      <b>Date Filter</b><br/>
      <img src="https://github.com/user-attachments/assets/4d7cef51-0605-4974-bfe6-79e4b631dd0b" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Type Filter</b><br/>
      <img src="https://github.com/user-attachments/assets/dd11ae56-dda9-485a-b9e7-4aae25f31de0" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Savings Goal</b><br/>
      <img src="https://github.com/user-attachments/assets/daa77114-e987-47a7-8214-2889a9413013" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Set Goal</b><br/>
      <img src="https://github.com/user-attachments/assets/bd42101b-cdea-4ae2-9641-36ef3464d543" width="200"/>
    </td>
    <td align="center" width="20%">
      <b>Settings</b><br/>
      <img src="https://github.com/user-attachments/assets/1b097f3d-52da-4739-87b5-c64978e10fe6" width="200"/>
    </td>
  </tr>
</table>

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

- `Home` → Balance summary + recent transactions + savings snapshot.
- `Transactions` → Full list, filters, search, swipe actions, and expense donut chart.
- `Insights` → Category-wise spending analysis.
- `Goal` → Monthly savings target management.
- `Settings` → Personalization, biometric lock, notifications, and CSV export.

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (Ladybug or newer recommended)
- Android SDK installed
- JDK 17+
- Android device/emulator (API 32+)

### Clone & Open

```bash
git clone [https://github.com/Soumyajit-Chatterjee/PersonalFinanceCompanionMobileApp.git](https://github.com/Soumyajit-Chatterjee/PersonalFinanceCompanionMobileApp.git)
cd PersonalFinanceCompanionMobileApp
```

Open the project in Android Studio, let Gradle sync, then run the `app` module.

### Build Debug APK

```bash
./gradlew assembleDebug
```
The APK can be found at: `app/build/outputs/apk/debug/app-debug.apk`

---

## ⚙️ App Configuration Notes

- **Min SDK**: 32
- **Target SDK**: 36
- **Compile SDK**: 36
- **Reminders**: Periodic background tasks handled via `WorkManager`.
- **Exporting**: Uses `FileProvider` and Android Share Intent for secure CSV handling.

---

## 🗂️ Project Structure

```text
app/src/main/java/com/example/personalfinancecompanionmobileapp
├── data
│   ├── db          # Room Database and DAOs
│   ├── model       # Data classes and Entities
│   └── repository  # Single source of truth for data
├── ui
│   ├── components  # Reusable UI elements
│   ├── navigation  # Compose Navigation graph
│   ├── screens     # Individual feature screens
│   └── theme       # M3 Color schemes and typography
├── utils           # Formatters, Converters, and Extensions
└── MainActivity.kt
```

---

## 🛠️ Future Improvements

- [ ] Proper import-from-CSV flow.
- [ ] Advanced charts for multi-month trends.
- [ ] Support for recurring transactions.
- [ ] Optional cloud backup/sync.
- [ ] Multi-language/Localization support.

---

## 🤝 Contributing

PRs, ideas, and bug reports are welcome! If you find a bug or have a feature request, please open an issue.

---

## 👤 Author

Developed by **Soumyajit Chatterjee**.  
If this project helps you track your finances, feel free to ⭐ the repo!
