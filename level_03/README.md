# Flarebear

> We at Flare have created our own Tamagotchi pet, the flarebear. He is very fussy. Keep him alive and happy and he will give you the flag.

## Init

[flarebear.7z]('./Flarebear.7z') contains an APK file.

### Setup

VS2019 + Xamarin

### Decompile

```shell
~/workspace/flareon6/level_03/flarebear$ grep -irn "flag" .
./FlareBearActivity.java:345:                danceWithFlag();
./FlareBearActivity.java:353:    public final void danceWithFlag() {
```

```java
public final void danceWithFlag() {
    InputStream openRawResource = getResources().openRawResource(R.raw.ecstatic);
    Intrinsics.checkExpressionValueIsNotNull(openRawResource, "ecstaticEnc");
    byte[] readBytes = ByteStreamsKt.readBytes(openRawResource);
    InputStream openRawResource2 = getResources().openRawResource(R.raw.ecstatic2);
    Intrinsics.checkExpressionValueIsNotNull(openRawResource2, "ecstaticEnc2");
    byte[] readBytes2 = ByteStreamsKt.readBytes(openRawResource2);
    String password = getPassword();
    try {
        byte[] decrypt = decrypt(password, readBytes);
        byte[] decrypt2 = decrypt(password, readBytes2);
        dance(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(decrypt, 0, decrypt.length)), new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(decrypt2, 0, decrypt2.length)));
    } catch (Exception unused) {
    }
}
```

## Solved:

F: 8
P: 4
C: 2

flag: th4t_was_be4rly_a_chall3nge@flare-on.com