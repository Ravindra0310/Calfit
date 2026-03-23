# CalFit - Ultimate Fitness Companion 🏋️‍♂️

![CalFit Banner](assets/banner.png)

CalFit is a high-performance, modern Android application built with Kotlin, designed to empower users on their fitness journey. It combines real-time activity tracking, AI-powered pose detection, and comprehensive health monitoring to provide a seamless workout and wellness experience.

---

## 🚀 Key Features

- **📊 Comprehensive Activity Tracking**: Real-time monitoring of steps, calories, and active minutes using Health Connect and Google Fit APIs.
- **🧘‍♂️ AI Pose Detection**: Leverages **Google ML Kit** for real-time posture analysis and workout form correction.
- **❤️ Heart Rate Monitoring**: Integrated heart rate tracking through Health Connect for a holistic view of your cardiovascular health.
- **🧬 BMI & Health Metrics**: Instant BMI calculation and personalized health insights.
- **🗺️ Interactive Workout Maps**: Track your runs and outdoor activities with **Google Maps** integration.
- **🔐 Secure Authentication**: Seamless onboarding with **Firebase Auth** and Google Sign-In.
- **🌙 Dynamic UI/UX**: Premium design featuring **Lottie animations**, glassmorphism elements, and smooth transitions.
- **📺 Workout Library**: Integration with **Android YouTube Player** for guided exercise videos.

---

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles.
- **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/)
- **Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & OkHttp
- **Asynchronous Work**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & Flow
- **Media & ML**: [CameraX](https://developer.android.com/training/camerax) & [ML Kit Pose Detection](https://developers.google.com/ml-kit/vision/pose-detection)
- **UI Components**: 
  - Jetpack Navigation
  - Data/View Binding
  - Glide (Image loading)
  - WorkManager (Background processing)
  - Health Connect client

---

## 🏗 Project Structure

```text
com.example.calfit/
├── activity/          # Main Screens and Fragments
├── dataModel/         # Data layer (Entities, Mappers)
├── di/                # Hilt Dependency Injection modules
├── heartRate/         # Health Connect & Fitness logic
├── maps/              # Google Maps implementation
├── model/             # Domain Models
├── utils/             # Helper classes and extensions
└── action/            # Background services and receivers
```

---

## 📦 Getting Started

### Prerequisites

- Android Studio Flamingo or later.
- JDK 11+.
- Minimum SDK: 26.
- A Google Cloud Console project with Maps and Fit API enabled.

### Installation

1. **Clone the Repo**:
   ```bash
   git clone https://github.com/Ravindra0310/Calfit.git
   ```

2. **Add API Keys**:
   - Create a `secrets.properties` file in the root or add your key to `local.properties`:
     ```properties
     MAPS_API_KEY=YOUR_GCP_API_KEY
     ```
   - Add your `google-services.json` to the `app/` directory.

3. **Build & Run**:
   Open the project in Android Studio, sync Gradle, and run on a physical device or emulator.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

---

*CalFit — Stay Fit, Stay Healthy.* 🏃‍♀️💨
