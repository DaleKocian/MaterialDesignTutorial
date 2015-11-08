package bhouse.travellist_starterproject;

import android.app.Activity;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.View;
import android.widget.AdapterView;

import java.lang.ref.WeakReference;

/**
 * Created by dkocian on 11/7/2015.
 */
public class NoteOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
    private ActionMode mActionMode;
    private Activity mActivity;
    private Callback mCallback;

    public NoteOnItemLongClickListener(final WeakReference<Activity> mReference, Callback callback) {
        mActivity = mReference.get();
        mCallback = callback;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mActionMode != null) {
            return false;
        }
        mActionMode = mActivity.startActionMode(mCallback);
        view.setSelected(true);
        return true;
    }

    public void setActionMode(ActionMode actionMode) {
        this.mActionMode = actionMode;
    }
}
