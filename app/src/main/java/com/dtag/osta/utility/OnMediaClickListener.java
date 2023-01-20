package com.dtag.osta.utility;

import android.view.View;

public interface OnMediaClickListener {
    void openMedia(View view, String mediaUri);

    void removeMedia(int position);
    void removeVideo(int position);
}
