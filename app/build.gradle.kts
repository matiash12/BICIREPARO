// Usa este bloque de plugins, sin la línea de 'compose'
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.bicireparoapp"
    compileSdk = 34 // Usamos 34 que es una versión estable, 36 es de desarrollo

    defaultConfig {
        applicationId = "com.example.bicireparoapp"
        minSdk = 24
        targetSdk = 34 // Coincide con compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // Solo un bloque buildFeatures con todo lo necesario
    buildFeatures {
        viewBinding = true
        compose = false
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.code.gson:gson:2.9.0")

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // --- Navegación y ViewModel ---
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Para ver logs de conexión

    // --- BASE DE DATOS ROOM
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Para usar corrutinas con Room
    ksp("androidx.room:room-compiler:$roomVersion") // El compilador de Room
    // --- GPS ---
    implementation("com.google.android.gms:play-services-location:21.2.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // --- DEPENDENCIAS PARA PRUEBAS UNITARIAS ---
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0") // Para probar LiveData
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Para probar Corrutinas
    testImplementation("org.mockito:mockito-core:5.3.1") // Para simular el Repositorio
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0") // Ayuda con Mockito en Kotlin

}