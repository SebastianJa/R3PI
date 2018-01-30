package com.opalwhale.r3pi;

import android.util.Log;

/**
 * Created by Seba on 30-Jan-18.
 */

public class BookVolume {
  

    VolumeInfo volumeInfo;



    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
    
    public String getCoverUri(){
        if(volumeInfo != null && volumeInfo.getImageLinks() != null) {
            String imgUri = null;
            if (volumeInfo.getImageLinks().getSmall() != null) {
                imgUri = volumeInfo.getImageLinks().getSmall();
            } else if (volumeInfo.getImageLinks().getMedium() != null ) {
                imgUri = volumeInfo.getImageLinks().getMedium();
            } else if (volumeInfo.getImageLinks().getThumbnail() != null) {
                imgUri = volumeInfo.getImageLinks().getThumbnail();
            } else if (volumeInfo.getImageLinks().getSmallThumbnail() != null) {
                imgUri = volumeInfo.getImageLinks().getSmallThumbnail();
            } else if (volumeInfo.getImageLinks().getLarge() != null) {
                imgUri = volumeInfo.getImageLinks().getLarge();
            } else if (volumeInfo.getImageLinks().getExtraLarge() != null) {
                imgUri = volumeInfo.getImageLinks().getExtraLarge();
            }
            return imgUri;
        }
        return null;
    }
}
