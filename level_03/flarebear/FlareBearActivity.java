package com.fireeye.flarebear;

import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.Random.Default;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u0000 A2\u00020\u0001:\u0001AB\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\u0010\r\u001a\u0004\u0018\u00010\fH\u0002J\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\nH\u0002J\u000e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\b\u0010\u0014\u001a\u00020\nH\u0002J\u000e\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\u0013J\u0006\u0010\u0017\u001a\u00020\nJ\u000e\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u0013J\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\nJ\u0016\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ\u0006\u0010\u001e\u001a\u00020\nJ\u0016\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020 J\u000e\u0010$\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010%\u001a\u00020\nJ\u0006\u0010&\u001a\u00020\"J\u000e\u0010'\u001a\u00020\u00132\u0006\u0010(\u001a\u00020)J\u0016\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\"2\u0006\u0010-\u001a\u00020+J\u0016\u0010*\u001a\u00020\u00132\u0006\u0010,\u001a\u00020\"2\u0006\u0010-\u001a\u00020\u0013J\u0016\u0010*\u001a\u00020\"2\u0006\u0010,\u001a\u00020\"2\u0006\u0010-\u001a\u00020\"J\u0006\u0010.\u001a\u00020\nJ\u0006\u0010/\u001a\u000200J\u0006\u00101\u001a\u000200J\u0012\u00102\u001a\u00020\n2\b\u00103\u001a\u0004\u0018\u000104H\u0014J\u000e\u00105\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u00106\u001a\u00020\nJ\u0006\u00107\u001a\u00020\nJ\u0006\u00108\u001a\u00020\nJ\u000e\u00109\u001a\u00020\n2\u0006\u0010:\u001a\u00020\"J\u0006\u0010;\u001a\u00020\nJ\u0006\u0010<\u001a\u00020\nJ\u0016\u0010=\u001a\u00020\n2\u0006\u0010,\u001a\u00020\"2\u0006\u0010>\u001a\u00020+J\u0016\u0010=\u001a\u00020\n2\u0006\u0010,\u001a\u00020\"2\u0006\u0010>\u001a\u00020\u0013J\u0016\u0010=\u001a\u00020\n2\u0006\u0010,\u001a\u00020\"2\u0006\u0010>\u001a\u00020\"J\u0012\u0010?\u001a\u00020\"*\u00020\"2\u0006\u0010@\u001a\u00020\u0013R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006B"}, d2 = {"Lcom/fireeye/flarebear/FlareBearActivity;", "Landroid/support/v7/app/AppCompatActivity;", "()V", "handler", "Landroid/os/Handler;", "getHandler", "()Landroid/os/Handler;", "setHandler", "(Landroid/os/Handler;)V", "activityUi", "", "drawable", "Landroid/graphics/drawable/Drawable;", "drawable2", "addPooUi", "Landroid/widget/ImageView;", "addPoos", "changeClean", "clean", "", "changeFlareBearImage", "changeHappy", "happy", "changeImageAndTag", "changeMass", "mass", "view", "Landroid/view/View;", "cleanUi", "dance", "danceWithFlag", "decrypt", "", "password", "", "data", "feed", "feedUi", "getPassword", "getStat", "activity", "", "getState", "", "key", "defValue", "incrementPooCount", "isEcstatic", "", "isHappy", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "play", "playUi", "removePoo", "restorePoo", "saveActivity", "activityType", "setFlareBearName", "setMood", "setState", "value", "rotN", "n", "Companion", "app_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: FlareBearActivity.kt */
public final class FlareBearActivity extends AppCompatActivity {
    public static final Companion Companion = new Companion(null);
    @NotNull
    public static final String FLARE_BEAR_NAME = "";
    private HashMap _$_findViewCache;
    @NotNull
    private Handler handler = new Handler();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/fireeye/flarebear/FlareBearActivity$Companion;", "", "()V", "FLARE_BEAR_NAME", "", "app_release"}, k = 1, mv = {1, 1, 15})
    /* compiled from: FlareBearActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    @NotNull
    public final Handler getHandler() {
        return this.handler;
    }

    public final void setHandler(@NotNull Handler handler2) {
        Intrinsics.checkParameterIsNotNull(handler2, "<set-?>");
        this.handler = handler2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_flare_bear);
        setFlareBearName();
        changeFlareBearImage();
        restorePoo();
    }

    public final void setFlareBearName() {
        String state = getState("name", "");
        TextView textView = (TextView) _$_findCachedViewById(R.id.textViewFlareBearName);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewFlareBearName");
        textView.setText(getString(R.string.name_heading_format, new Object[]{state}));
    }

    /* access modifiers changed from: private */
    public final void changeFlareBearImage() {
        this.handler.removeCallbacksAndMessages(null);
        this.handler.postDelayed(new FlareBearActivity$changeFlareBearImage$r$1(this), 500);
    }

    public final void changeImageAndTag() {
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.flareBearImageView);
        String str = "flareBearImageView";
        Intrinsics.checkExpressionValueIsNotNull(imageView, str);
        String str2 = "happy";
        String str3 = "happy2";
        if (Intrinsics.areEqual(imageView.getTag(), (Object) str2)) {
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.happy2, null));
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag(str3);
            return;
        }
        ImageView imageView2 = (ImageView) _$_findCachedViewById(R.id.flareBearImageView);
        Intrinsics.checkExpressionValueIsNotNull(imageView2, str);
        if (Intrinsics.areEqual(imageView2.getTag(), (Object) str3)) {
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.happy, null));
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag(str2);
            return;
        }
        ImageView imageView3 = (ImageView) _$_findCachedViewById(R.id.flareBearImageView);
        Intrinsics.checkExpressionValueIsNotNull(imageView3, str);
        String str4 = "sad";
        String str5 = "sad2";
        if (Intrinsics.areEqual(imageView3.getTag(), (Object) str4)) {
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.sad2, null));
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag(str5);
            return;
        }
        ImageView imageView4 = (ImageView) _$_findCachedViewById(R.id.flareBearImageView);
        Intrinsics.checkExpressionValueIsNotNull(imageView4, str);
        if (Intrinsics.areEqual(imageView4.getTag(), (Object) str5)) {
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.sad, null));
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag(str4);
        }
    }

    public final void feed(@NotNull View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        saveActivity("f");
        changeMass(10);
        changeHappy(2);
        changeClean(-1);
        incrementPooCount();
        feedUi();
    }

    public final void changeMass(int i) {
        String str = "mass";
        setState(str, getState(str, 0) + i);
    }

    public final void changeHappy(int i) {
        String str = "happy";
        setState(str, getState(str, 0) + i);
    }

    public final void changeClean(int i) {
        String str = "clean";
        setState(str, getState(str, 0) + i);
    }

    public final void incrementPooCount() {
        String str = "poo";
        setState(str, getState(str, 0.0f) + 0.34f);
    }

    public final void feedUi() {
        activityUi(ResourcesCompat.getDrawable(getResources(), R.drawable.eating, null), ResourcesCompat.getDrawable(getResources(), R.drawable.eating2, null));
    }

    public final void restorePoo() {
        FlareBearActivityKt.poosList.clear();
        addPoos();
    }

    /* access modifiers changed from: private */
    public final void addPoos() {
        while (Math.floor((double) getState("poo", 0.0f)) > ((double) FlareBearActivityKt.poosList.size())) {
            FlareBearActivityKt.poosList.add(addPooUi());
        }
    }

    @NotNull
    public final ImageView addPooUi() {
        ImageView imageView = new ImageView(this);
        Integer num = null;
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.poop, null);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        imageView.setY((float) Random.Default.nextInt((point.y / 2) - 250, (point.y / 2) + 70));
        if (imageView.getY() >= ((float) ((point.y / 2) - 100))) {
            Default defaultR = Random.Default;
            int i = point.x;
            if (drawable != null) {
                num = Integer.valueOf(drawable.getIntrinsicWidth());
            }
            if (num == null) {
                Intrinsics.throwNpe();
            }
            imageView.setX((float) defaultR.nextInt(0, i - num.intValue()));
        } else if (Random.Default.nextFloat() > 0.5f) {
            Default defaultR2 = Random.Default;
            int i2 = (point.x / 2) - 200;
            if (drawable != null) {
                num = Integer.valueOf(drawable.getIntrinsicWidth());
            }
            if (num == null) {
                Intrinsics.throwNpe();
            }
            imageView.setX((float) defaultR2.nextInt(0, i2 - num.intValue()));
        } else {
            Default defaultR3 = Random.Default;
            int i3 = (point.x / 2) + 200;
            int i4 = point.x;
            if (drawable != null) {
                num = Integer.valueOf(drawable.getIntrinsicWidth());
            }
            if (num == null) {
                Intrinsics.throwNpe();
            }
            imageView.setX((float) defaultR3.nextInt(i3, i4 - num.intValue()));
        }
        imageView.setImageDrawable(drawable);
        ((ConstraintLayout) _$_findCachedViewById(R.id.constraintLayout)).addView(imageView);
        return imageView;
    }

    public final void play(@NotNull View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        saveActivity("p");
        changeMass(-2);
        changeHappy(4);
        changeClean(-1);
        playUi();
    }

    public final void playUi() {
        activityUi(ResourcesCompat.getDrawable(getResources(), R.drawable.playing, null), ResourcesCompat.getDrawable(getResources(), R.drawable.playing2, null));
    }

    private final void activityUi(Drawable drawable, Drawable drawable2) {
        this.handler.removeCallbacksAndMessages(null);
        ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setImageDrawable(drawable);
        ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag("activity1");
        this.handler.postDelayed(new FlareBearActivity$activityUi$1(this, drawable2), 800);
        this.handler.postDelayed(new FlareBearActivity$activityUi$2(this, drawable), 1600);
        this.handler.postDelayed(new FlareBearActivity$activityUi$3(this), 2400);
    }

    public final void clean(@NotNull View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        saveActivity("c");
        removePoo();
        cleanUi();
        changeMass(0);
        changeHappy(-1);
        changeClean(6);
        setMood();
    }

    public final void removePoo() {
        String str = "poo";
        float state = getState(str, 0.0f);
        if (state < 1.0f) {
            Toast.makeText(this, "There's no poo", 0).show();
        } else {
            setState(str, state - ((float) 1));
        }
    }

    public final void cleanUi() {
        if (FlareBearActivityKt.poosList.size() > 0) {
            ((ConstraintLayout) _$_findCachedViewById(R.id.constraintLayout)).removeView((ImageView) FlareBearActivityKt.poosList.remove(FlareBearActivityKt.poosList.size() - 1));
        }
    }

    @NotNull
    public final String getState(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(str2, "defValue");
        String string = PreferenceManager.getDefaultSharedPreferences(this).getString(str, str2);
        Intrinsics.checkExpressionValueIsNotNull(string, "sharedPref.getString(key, defValue)");
        return string;
    }

    public final int getState(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(str, i);
    }

    public final float getState(@NotNull String str, float f) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        return PreferenceManager.getDefaultSharedPreferences(this).getFloat(str, f);
    }

    public final void saveActivity(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "activityType");
        String str2 = "activity";
        String state = getState(str2, "");
        StringBuilder sb = new StringBuilder();
        sb.append(state);
        sb.append(str);
        setState(str2, sb.toString());
    }

    public final void setState(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(str2, "value");
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public final void setState(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public final void setState(@NotNull String str, float f) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putFloat(str, f);
        edit.commit();
    }

    public final void setMood() {
        if (isHappy()) {
            ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag("happy");
            if (isEcstatic()) {
                danceWithFlag();
                return;
            }
            return;
        }
        ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag("sad");
    }

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

    public final void dance(@NotNull Drawable drawable, @NotNull Drawable drawable2) {
        Intrinsics.checkParameterIsNotNull(drawable, "drawable");
        Intrinsics.checkParameterIsNotNull(drawable2, "drawable2");
        this.handler.removeCallbacksAndMessages(null);
        ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setImageDrawable(drawable);
        ((ImageView) _$_findCachedViewById(R.id.flareBearImageView)).setTag("ecstatic");
        Handler handler2 = new Handler();
        handler2.postDelayed(new FlareBearActivity$dance$r$1(this, handler2, drawable2, drawable), 500);
    }

    @NotNull
    public final String getPassword() {
        String str;
        int stat = getStat('f');
        int stat2 = getStat('p');
        int stat3 = getStat('c');
        String str2 = "*";
        String str3 = "&";
        String str4 = "@";
        String str5 = "#";
        String str6 = "+";
        String str7 = "$";
        String str8 = "_";
        String str9 = "";
        switch (stat % 9) {
            case 0:
                str = str8;
                break;
            case 1:
                str = "-";
                break;
            case 2:
                str = str7;
                break;
            case 3:
                str = str6;
                break;
            case 4:
                str = "!";
                break;
            case 5:
                str = str5;
                break;
            case 6:
                str = str4;
                break;
            case 7:
                str = str3;
                break;
            case 8:
                str = str2;
                break;
            default:
                str = str9;
                break;
        }
        switch (stat3 % 7) {
            case 0:
                str2 = str7;
                break;
            case 1:
                str2 = str8;
                break;
            case 2:
                str2 = str6;
                break;
            case 3:
                str2 = str5;
                break;
            case 4:
                str2 = str3;
                break;
            case 5:
                break;
            case 6:
                str2 = str4;
                break;
            default:
                str2 = str9;
                break;
        }
        String repeat = StringsKt.repeat("flare", stat / stat3);
        String repeat2 = StringsKt.repeat(rotN("bear", stat * stat2), stat2 * 2);
        String repeat3 = StringsKt.repeat("yeah", stat3);
        StringBuilder sb = new StringBuilder();
        sb.append(repeat);
        sb.append(str);
        sb.append(repeat2);
        sb.append(str2);
        sb.append(repeat3);
        return sb.toString();
    }

    @NotNull
    public final String rotN(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$this$rotN");
        CharSequence charSequence = str;
        Collection arrayList = new ArrayList(charSequence.length());
        for (int i2 = 0; i2 < charSequence.length(); i2++) {
            char charAt = charSequence.charAt(i2);
            if (Character.isLowerCase(charAt)) {
                charAt = (char) (charAt + i);
                if (charAt > 'z') {
                    charAt = (char) (charAt - (i * 2));
                }
            }
            arrayList.add(Character.valueOf(charAt));
        }
        return ArraysKt.joinToString$default(CollectionsKt.toCharArray((List) arrayList), (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }

    public final boolean isEcstatic() {
        int state = getState("mass", 0);
        int state2 = getState("happy", 0);
        int state3 = getState("clean", 0);
        if (state == 72 && state2 == 30 && state3 == 0) {
            return true;
        }
        return false;
    }

    public final boolean isHappy() {
        double stat = (double) (((float) getStat('f')) / ((float) getStat('p')));
        return stat >= 2.0d && stat <= 2.5d;
    }

    public final int getStat(char c) {
        String string = PreferenceManager.getDefaultSharedPreferences(this).getString("activity", "");
        Intrinsics.checkExpressionValueIsNotNull(string, "act");
        CharSequence charSequence = string;
        int i = 0;
        for (int i2 = 0; i2 < charSequence.length(); i2++) {
            if (charSequence.charAt(i2) == c) {
                i++;
            }
        }
        return i;
    }

    @NotNull
    public final byte[] decrypt(@NotNull String str, @NotNull byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(str, "password");
        Intrinsics.checkParameterIsNotNull(bArr, "data");
        String str2 = "UTF-8";
        Charset forName = Charset.forName(str2);
        String str3 = "Charset.forName(charsetName)";
        Intrinsics.checkExpressionValueIsNotNull(forName, str3);
        byte[] bytes = "pawsitive_vibes!".getBytes(forName);
        String str4 = "(this as java.lang.String).getBytes(charset)";
        Intrinsics.checkExpressionValueIsNotNull(bytes, str4);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
        char[] charArray = str.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
        Charset forName2 = Charset.forName(str2);
        Intrinsics.checkExpressionValueIsNotNull(forName2, str3);
        byte[] bytes2 = "NaClNaClNaCl".getBytes(forName2);
        Intrinsics.checkExpressionValueIsNotNull(bytes2, str4);
        SecretKey generateSecret = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(charArray, bytes2, 1234, 256));
        Intrinsics.checkExpressionValueIsNotNull(generateSecret, "secretKeyFactory.generateSecret(pbKeySpec)");
        SecretKeySpec secretKeySpec = new SecretKeySpec(generateSecret.getEncoded(), "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
        instance.init(2, secretKeySpec, ivParameterSpec);
        byte[] doFinal = instance.doFinal(bArr);
        Intrinsics.checkExpressionValueIsNotNull(doFinal, "cipher.doFinal(data)");
        return doFinal;
    }
}
