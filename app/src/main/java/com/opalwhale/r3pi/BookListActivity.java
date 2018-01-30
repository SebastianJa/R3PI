package com.opalwhale.r3pi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity implements Response.Listener<ListQuery>, Response.ErrorListener  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    static final String BOOKS_URL=
            "https://www.googleapis.com/books/v1/volumes?q=travel&key="+Constants.API_KEY+"&fields=items(selfLink,volumeInfo(title,imageLinks(thumbnail)))&country=CH&maxResults=40";
    static final String START_INDEX = "&startIndex=";
    private boolean fetchMoreData = false;
    private int startIndex = 0;
    RecyclerView recyclerView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        queueRequest();
    }

    private void queueRequest(){
        GsonRequest<ListQuery> request=
                new GsonRequest<ListQuery>(BOOKS_URL+START_INDEX+startIndex,
                        ListQuery.class, null, this, this);

        VolleyManager.get(this).enqueue(request);
        fetchMoreData = true;
    }

    private void setupRecyclerView(ListQuery response) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, response.items, mTwoPane));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.getMessage(),
                Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(),
                "Exception from Volley", error);
    }

    @Override
    public void onResponse(ListQuery response) {

        if(response.items == null){
            fetchMoreData = false;
            return;
        }
        if(recyclerView.getAdapter() == null){
            setupRecyclerView(response);

        }else{
            ((SimpleItemRecyclerViewAdapter)recyclerView.getAdapter()).addList(response.items);

        }
        startIndex += response.items.size();
        fetchMoreData = false;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final BookListActivity mParentActivity;
        private List<Item> items;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //large screen - > load fragment
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(BookDetailFragment.ARG_SELF_LINK, (String)view.getTag());
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();

                } else {//normal screen - > load activity
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_SELF_LINK, (String)view.getTag());
                    context.startActivity(intent);
                }
            }
        };

        public void addList(List<Item> items){
            for(Item item : items){
                this.items.add(item);
            }
            notifyDataSetChanged();
        }

        SimpleItemRecyclerViewAdapter(BookListActivity parent,
                                      List<Item> items,
                                      boolean twoPane) {
            this.items = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            VolleyManager
                    .get(mParentActivity)
                    .loadImage(items.get(position).getVolumeInfo().getImageLinks() == null ? Constants.NO_COVER_IMG :
                                    items.get(position).getVolumeInfo().getImageLinks().getThumbnail(), holder.mImage,
                            R.drawable.no_cover_thumb,
                            R.drawable.no_cover_thumb);
            holder.mContentView.setText(items.get(position).getVolumeInfo().getTitle());

            holder.itemView.setTag(items.get(position).getSelfLink());
            holder.itemView.setOnClickListener(mOnClickListener);

            //request new data
            if(!fetchMoreData && position > getItemCount() - 15){
                queueRequest();
                fetchMoreData = true;
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder  {
            final ImageView mImage;
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mImage = view.findViewById(R.id.icon);
                mIdView =  view.findViewById(R.id.id_text);
                mContentView =  view.findViewById(R.id.content);
            }



        }


    }


}
