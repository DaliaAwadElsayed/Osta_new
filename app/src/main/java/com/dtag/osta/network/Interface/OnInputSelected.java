package com.dtag.osta.network.Interface;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface OnInputSelected {
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    void sendInput(String lat, String lang, String address);
}
