pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        // có cũng được, không bắt buộc cho dependencies thường
        maven { url = uri("https://artifactory.appodeal.com/appodeal-public") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // ⚠️ Cái QUAN TRỌNG là THÊM DÒNG NÀY
        maven { url = uri("https://artifactory.appodeal.com/appodeal-public") }
        // hoặc cú pháp Groovy:
        // maven { url "https://artifactory.appodeal.com/appodeal-public" }
    }
}

rootProject.name = "My Application"
include(":app")
