package bhouse.travellist_starterproject;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by dkocian on 11/7/2015.
 */
public abstract class NoteActionModeCallback implements ActionMode.Callback {
    private int positionSelected;

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.notes_context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteNote(mode, positionSelected);
                return true;
            case R.id.action_edit:
                editNote(mode, positionSelected);
                return true;
            default:
                return false;
        }
    }

    protected abstract void editNote(ActionMode mode, int positionSelected);

    protected abstract void deleteNote(ActionMode mode, int positionSelected);
}
