package com.opalwhale.r3pi;

import com.google.api.services.books.model.Volume;

/**
 * Created by Seba on 29-Jan-18.
 */

class Item {

    VolumeInfo volumeInfo;
    String selfLink;

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public String getSelfLink() {
        return selfLink;
    }
}
