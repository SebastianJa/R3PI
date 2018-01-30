package com.opalwhale.r3pi;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.api.services.books.model.Volume;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment implements Response.Listener<BookVolume>, Response.ErrorListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SELF_LINK = "selfLink";
    private EventListener listener;

    private ImageView imgCover;
    private ImageView imgCoverAlt;  //alternative imageView for large screens
    private TextView titleTv;
    private TextView subTitleTv;
    private TextView authorsTv;
    private TextView publisherTv;
    private TextView publishedDateTv;
    private TextView descTv;

    private Volume volume;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_SELF_LINK)) {

            String  selfLink = getArguments().getString(BookDetailFragment.ARG_SELF_LINK);
            bookDetailsRequest(selfLink);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);


        imgCover =   rootView.findViewById(R.id.img_cover);
        imgCoverAlt =   rootView.findViewById(R.id.img_large_screen);

        titleTv =  rootView.findViewById(R.id.title);
        subTitleTv =  rootView.findViewById(R.id.sub_title);
        authorsTv =  rootView.findViewById(R.id.authors);
        publisherTv =  rootView.findViewById(R.id.publisher);
        publishedDateTv =  rootView.findViewById(R.id.publishedDate);
        descTv =  rootView.findViewById(R.id.description);

        return rootView;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    private void bookDetailsRequest(String uri){
        GsonRequest<BookVolume> request =
                new GsonRequest<BookVolume>(uri, BookVolume.class, null, this, this);
        VolleyManager.get(getContext()).enqueue(request);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof EventListener) {
            listener = (EventListener)context;
        }
    }


    @Override
    public void onResponse(BookVolume response) {

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout =  activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && response.volumeInfo != null) {
            appBarLayout.setTitle(response.volumeInfo.getTitle());
        }

        if(response.volumeInfo != null && listener != null) {
            listener.sendDataToActivity(response);
        }else{
            //large screen -> load img inside hidden imageView
            imgCoverAlt.setVisibility(View.VISIBLE);
            String imgUri = response.getCoverUri();
            if(imgUri != null) {
                VolleyManager
                        .get(getActivity())
                        .loadImage(imgUri, imgCoverAlt,
                                0,
                                R.drawable.no_cover_thumb);
            }

        }

       titleTv.setText(response.volumeInfo.getTitle());
        if(response.volumeInfo.getSubtitle() != null && response.volumeInfo.getSubtitle().length() > 0) {
            subTitleTv.setVisibility(View.VISIBLE);
            subTitleTv.setText(response.volumeInfo.getSubtitle());
        }
       String authors = "Authors:";
       if(response.volumeInfo.getAuthors() != null && response.volumeInfo.getAuthors().size() > 0) {
           for(String author : response.volumeInfo.getAuthors()){
               authors += "\n"+author;
           }
       }else{
           authors += " N/A";
       }
       authorsTv.setText(authors);

       if(response.volumeInfo.getPublisher() != null && response.volumeInfo.getPublisher().length() > 0){
           String str = "Publisher: "+response.volumeInfo.getPublisher();
           publisherTv.setText(str);
       }

       if(response.volumeInfo.getPublishedDate() != null && response.volumeInfo.getPublishedDate().length() > 0){
           String str = "Published: "+response.volumeInfo.getPublishedDate();
           publishedDateTv.setText(str);
       }

        descTv.setText(response.volumeInfo.getDescription());


    }
}
