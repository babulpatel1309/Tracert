package com.kid.retro.com.tracerttask.Fragment;


import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.remindeUtils.ReminderActivity;
import com.kid.retro.com.tracerttask.remindeUtils.RemindersDbAdapter;

/**
 * Created by Kid on 1/8/2016.
 */
public class ScheduleFragment extends ListFragment
{
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_scheduling, container, false);

        Calendar cal = Calendar.getInstance();
        Intent reminderIntent = new Intent();
        reminderIntent = new Intent(Intent.ACTION_EDIT);
        reminderIntent.setType("vnd.android.cursor.item/event");
        reminderIntent.putExtra("beginTime", cal.getTimeInMillis());
        reminderIntent.putExtra("allDay", true);
        reminderIntent.putExtra("rrule", "FREQ=YEARLY");
        reminderIntent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        reminderIntent.putExtra("title", "Tracert");
        startActivity(reminderIntent);

        return rootview;
    }*/

    Button btnAdd;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private RemindersDbAdapter mDbHelper;

    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_schedular, container, false);


        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ReminderActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new RemindersDbAdapter(getActivity());
        mDbHelper.open();
        fillData();


        return rootView;
    }

    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.list_menu_item_longpress, menu);
    }

    private void fillData() {
        Cursor remindersCursor = mDbHelper.fetchAllReminders();
        getActivity().startManagingCursor(remindersCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{RemindersDbAdapter.KEY_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter reminders =
                new SimpleCursorAdapter(getActivity(), R.layout.reminder_row, remindersCursor, from, to);
        setListAdapter(reminders);
    }


   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteReminder(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(), ReminderActivity.class);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("Reminders");
    }
}
