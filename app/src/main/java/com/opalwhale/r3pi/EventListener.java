package com.opalwhale.r3pi;

import com.google.api.services.books.model.Volume;

/**
 * Created by Seba on 30-Jan-18.
 */

public interface EventListener {

     public void sendDataToActivity(BookVolume volume);


}
