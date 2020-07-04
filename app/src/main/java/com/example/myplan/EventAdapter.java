package com.example.myplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {
    Context context;
    List<Event> array = new ArrayList<>();

    public EventAdapter(Context context, List<Event> data)
    {
        this.context = context;
        this.array = data;
    }


    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Event getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.your_event_layout, null);//inflate layout in View

        TextView descriptionText, timeText, dateText, uuidText;//for the three textviews of the layout

        //setting required details of the Event with index i
        uuidText = contentView.findViewById(R.id.uuid_text);
        uuidText.setText(getItem(i).uniqueID);

        descriptionText = contentView.findViewById(R.id.description_show);
        descriptionText.setText(getItem(i).description);

        dateText = contentView.findViewById(R.id.date_show);
        dateText.setText(getItem(i).date);

        timeText = contentView.findViewById(R.id.time_show);
        timeText.setText(String.format("%s %s", getItem(i).time, getItem(i).ampm));

        return contentView;//returning the View  for event i where i is the index
    }
}
