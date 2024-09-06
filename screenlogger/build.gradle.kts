plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    `maven-publish`
}

android {
    namespace = "dev.tools.screenlogger"
    compileSdk = 34
    version = "1.0.3"
    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from (components["release"])
                groupId = "dev.tools"
                artifactId = "screenlogger"
                version = "1.0"
            }
        }
        repositories {
            maven {
                name = "screenlogger"
                url = uri(layout.buildDirectory.dir("repo"))
            }
        }
    }
}

tasks.register<Zip>("generateRepo") {
    val publishTask = tasks.named(
        "publishMavenPublicationToScreenloggerRepository",
        PublishToMavenRepository::class.java)
    from(publishTask.map { it.repository.url })
    into("screenlogger")
    archiveFileName.set("screenlogger.zip")
}