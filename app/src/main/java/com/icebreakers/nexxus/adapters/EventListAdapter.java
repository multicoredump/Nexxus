package com.icebreakers.nexxus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icebreakers.nexxus.NexxusApplication;
import com.icebreakers.nexxus.R;
import com.icebreakers.nexxus.models.MeetupEvent;
import com.icebreakers.nexxus.utils.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by radhikak on 4/7/17.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private static final String TAG = NexxusApplication.BASE_TAG + EventListAdapter.class.getName();
    List<MeetupEvent> events;

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvEventName)
        TextView tvName;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.tvVenue)
        TextView tvVenue;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public EventListAdapter(List<MeetupEvent> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_v2, null);
        return new EventViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        MeetupEvent event = events.get(position);
        holder.tvName.setText(event.getName());

        if (event.fakeEvent && event.getId().equals(MeetupEvent.EVENT_ID_CODEPATH)) {
            holder.tvTime.setText("Happening now");
        } else {
            holder.tvTime.setText(DateTimeFormat.formatDateTimeForEvent(event.getTime()));
        }

        // We make sure venue is never null, hence no need to check
        String address = String.format("%s, %s", event.getVenue().getAddress1(), event.getVenue().getCity());
        holder.tvVenue.setText(address);

        String imageURL = null;

        if (event.fakeEvent) {
            imageURL = event.imageUrl;
        } else {
            if (event.getGroup().getKeyPhoto() != null) {
                imageURL = event.getGroup().getKeyPhoto().getHighresLink();
            } else if (event.getGroup().getPhoto() != null) {
                imageURL = event.getGroup().getPhoto().getHighresLink();
            }
        }
        if (imageURL != null) {

            if (imageURL != null) {
                holder.ivImage.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView.getContext())
                        .load(imageURL)
                        .placeholder(R.drawable.layer_list_event)
                        .error(R.drawable.layer_list_event)
                        .into(holder.ivImage);
            } else {
                holder.ivImage.setVisibility(View.GONE);
            }
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
