# Proguard rules for Side by Side
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
