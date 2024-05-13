plugins {
    id("java")
    id("com.google.cloud.tools.jib") version "3.4.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
}

jib {
    from {
        image = "eclipse-temurin:21"
        platforms {
            platform {
                os = "linux"
                architecture = "amd64"
            }
        }
    }
    to {
        image = "ylemoigne/ezviz-enable-rtsp"
    }
    container {
        jvmFlags = listOf("-Dfile.encoding=UTF8", "-Djna.library.path=/app/lib")
        mainClass = "fr.javatic.ezvizEnableRtsp.Main"
        workingDirectory = "/app"
        extraDirectories {
            paths {
                path {
                    setFrom("lib")
                    into = "/app/lib"
                }
            }
        }
    }
}
