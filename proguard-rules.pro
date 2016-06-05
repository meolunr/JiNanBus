#保持Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#保持OkHttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#保持RxJava
-dontwarn rx.**
-keep class rx.internal.util.** { *; }

#保持JavaBean
-keep class com.leon.jinanbus.domain.** {*;}