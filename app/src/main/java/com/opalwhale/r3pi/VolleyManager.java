package com.opalwhale.r3pi;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Seba on 30-Jan-18.
 */

public class VolleyManager {
    private static volatile VolleyManager INSTANCE;
    private final RequestQueue queue;
    private final ImageLoader imageLoader;

    synchronized static VolleyManager get(Context ctxt) {
        if (INSTANCE==null) {
            INSTANCE=new VolleyManager(ctxt.getApplicationContext());
        }

        return(INSTANCE);
    }

    private VolleyManager(Context ctxt) {
        queue= Volley.newRequestQueue(ctxt);
        imageLoader=new ImageLoader(queue, new LruBitmapCache(ctxt));
    }

    void enqueue(Request<?> request) {
        queue.add(request);
    }

    void loadImage(String url, ImageView iv,
                   int placeholderDrawable, int errorDrawable) {
        imageLoader.get(url,
                ImageLoader.getImageListener(iv, placeholderDrawable,
                        errorDrawable));
    }
}
