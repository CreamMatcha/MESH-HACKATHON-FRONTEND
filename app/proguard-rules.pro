# Add project specific ProGuard rules here.
-keepattributes Signature
-keepattributes *Annotation*
# Retrofit
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
