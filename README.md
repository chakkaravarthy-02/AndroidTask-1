## 📸 Screenshots

### Contact List Screen
![Contact List]("C:\Users\saran chakkaravarthy\Downloads\Screenshot_2025-02-24-07-37-52-75_bb69cfc368fa8da454b14bdcb1ffe272.jpg")

### Contact Detail Screen
![Contact Detail]("![Image](https://github.com/user-attachments/assets/6a05dfab-3235-4d42-873c-19e706188ecc)")


# Contact List App (MVI + Clean Architecture)

This project is a **Contact List App** built using **MVI architecture** and **Clean Architecture principles**. It includes:

- **Contact List Screen** (with Paging 3, Ktor for API calls, Room for caching, Coil for image loading).
- **Detail Screen** (showing contact details).
- **Material 3 UI** for a modern look and feel.

## Tech Stack

- **Kotlin**
- **Jetpack Compose (Material3)**
- **Paging 3** (for efficient list loading)
- **Room Database** (local storage)
- **Ktor** (API communication)
- **Coil** (image loading)
- **MVI Pattern** (for predictable state management)
- **Clean Architecture** (separation of concerns)

---

## 📌 **Project Structure**

```
com.example.contactapp
│── data
│   ├── local (Room Database, DAO)
│   ├── remote (Ktor API Service)
│   ├── repository (Implements Repository pattern)
│
│── domain
│   ├── model (Contact Data Model)
│   ├── repository (Interfaces for data layer)
│   ├── usecase (Business logic & UseCases)
│
│── presentation
│   ├── contactlist (List Screen, ViewModel, MVI State)
│   ├── detail (Detail Screen, ViewModel, MVI State)
│
│── di (Dependency Injection)
│── MainActivity.kt (Navigation & UI Entry Point)
```

---

## 🌟 **Features**

✅ Contact List with Paging 3 (infinite scrolling)
✅ Offline Caching with Room Database
✅ Contact Detail Screen
✅ Image Loading with Coil
✅ Jetpack Compose UI (Material3)
✅ MVI for State Management
✅ Ktor for API Calls
✅ Dependency Injection with Hilt

---

## 🛠️ **Setup & Installation**

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/contact-app-mvi.git
   ```

2. Open in **Android Studio** and build the project.

3. Run the app on an emulator or a physical device.

---

## 📱 **Screens**

### **1️⃣ Contact List Screen**

- Displays a list of contacts fetched via API.
- Uses **Paging 3** to load data efficiently.
- Caches contacts in **Room database** for offline support.
- Shows profile images using **Coil**.
- Clicking on a contact navigates to the **Detail Screen**.

### **2️⃣ Contact Detail Screen**

- Displays the selected contact’s details.
- Uses **MVI pattern** for managing state.
- Fetches details from **Room (if cached) or API**.
- Material3 UI with modern design.

---

## 🔄 **MVI Pattern Overview**

Each screen follows the **MVI (Model-View-Intent)** pattern.

---

## 🏗 **Architecture Layers**

- **Data Layer:** Handles API calls, database, and repository implementation.
- **Domain Layer:** Defines business logic using **UseCases**.
- **Presentation Layer:** Uses **Jetpack Compose** and **MVI ViewModel** to manage UI state.

---

## 🚀 **Contributing**

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📝 **License**

MIT License. See `LICENSE` for more details.

---

**Happy Coding! 🚀**

