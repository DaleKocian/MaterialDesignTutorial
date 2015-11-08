package bhouse.travellist_starterproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.transition.Transition;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DetailActivity extends Activity implements View.OnClickListener {

    public static final String EXTRA_PARAM_ID = "place_id";
    public static final float ANIMATE_APLHA_VALUE = 1.0f;
    public static final int X_OFFSET = 30;
    public static final int Y_OFFSET = 60;
    public static final int ALPHA_ANIM_DURATION_MILLIS = 100;
    public static final float FROM_ALPHA = 1.0f;
    public static final float TO_ALPHA = 0.0f;
    public static final int SOFT_INPUT_FLAG = 0;
    public static final int START_RADIUS = 0;
    public static final int END_RADIUS = 0;
    int defaultColor;
    private ListView mList;
    private ImageView mImageView;
    private TextView mTitle;
    private LinearLayout mTitleHolder;
    private ImageButton mAddButton;
    private LinearLayout mRevealView;
    private EditText mEditTextTodo;
    private boolean isEditTextVisible;
    private InputMethodManager mInputManager;
    private Place mPlace;
    private ArrayList<String> mTodoList;
    private ArrayAdapter<String> mToDoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mPlace = PlaceData.placeList().get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemLongClickListener(new NoteOnItemLongClickListener(new WeakReference<Activity>(this), mActionModeCallback));
        mImageView = (ImageView) findViewById(R.id.placeImage);
        mTitle = (TextView) findViewById(R.id.textView);
        mTitleHolder = (LinearLayout) findViewById(R.id.placeNameHolder);
        mAddButton = (ImageButton) findViewById(R.id.btn_add);
        mRevealView = (LinearLayout) findViewById(R.id.llEditTextHolder);
        mEditTextTodo = (EditText) findViewById(R.id.etTodo);
        mAddButton.setOnClickListener(this);
        defaultColor = getResources().getColor(R.color.primary_dark);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRevealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;
        setUpAdapter();
        loadPlace();
        windowTransition();
        getPhoto();
    }

    private void setUpAdapter() {
        mTodoList = new ArrayList<>();
        mToDoAdapter = new ArrayAdapter<>(this, R.layout.row_todo, mTodoList);
        mList.setAdapter(mToDoAdapter);
    }

    private void loadPlace() {
        mTitle.setText(mPlace.name);
        mImageView.setImageResource(mPlace.getImageResourceId(this));
    }

    private void windowTransition() {
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mAddButton.animate().alpha(ANIMATE_APLHA_VALUE);
                getWindow().getEnterTransition().removeListener(this);
            }
        });
    }

    private void addToDo(String todo) {
        if (!todo.isEmpty()) {
            mTodoList.add(todo);
            mEditTextTodo.setText("");
        }
    }

    private void getPhoto() {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), mPlace.getImageResourceId(this));
        colorize(photo);
    }

    private void colorize(Bitmap photo) {
        Palette mPalette = Palette.from(photo).generate();
        applyPalette(mPalette);
    }

    private void applyPalette(Palette mPalette) {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColor)));
        mTitleHolder.setBackgroundColor(mPalette.getMutedColor(defaultColor));
        mRevealView.setBackgroundColor(mPalette.getLightVibrantColor(defaultColor));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Animatable mAnimatable;
                if (!isEditTextVisible) {
                    revealEditText(mRevealView);
                    mEditTextTodo.requestFocus();
                    mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);
                    mAddButton.setImageResource(R.drawable.icn_morph);
                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                } else {
                    addToDo(mEditTextTodo.getText().toString());
                    mToDoAdapter.notifyDataSetChanged();
                    mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), SOFT_INPUT_FLAG);
                    hideEditText(mRevealView);
                    mAddButton.setImageResource(R.drawable.icn_morph_reverse);
                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                }
        }
    }

    private void revealEditText(LinearLayout view) {
        int cx = view.getRight() - X_OFFSET;
        int cy = view.getBottom() - Y_OFFSET;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, START_RADIUS, finalRadius);
        view.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        anim.start();
    }

    private void hideEditText(final LinearLayout view) {
        int cx = view.getRight() - X_OFFSET;
        int cy = view.getBottom() - Y_OFFSET;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, END_RADIUS);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        isEditTextVisible = false;
        anim.start();
    }

    @Override
    public void onBackPressed() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(FROM_ALPHA, TO_ALPHA);
        alphaAnimation.setDuration(ALPHA_ANIM_DURATION_MILLIS);
        mAddButton.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAddButton.setVisibility(View.GONE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private ActionMode.Callback mActionModeCallback = new NoteActionModeCallback() {
        @Override
        protected void editNote(final ActionMode mode, final int positionSelected) {
            new EditNoteDialog(DetailActivity.this) {
                @Override
                public void setOnPositiveButton(EditText note) {
                    mTodoList.set(positionSelected, note.getText().toString());
                    mToDoAdapter.notifyDataSetChanged();
                    mode.finish();
                }

                @Override
                public void setOnNegativeButtonClicked() {
                    mode.finish();
                }
            }.show();
        }

        @Override
        protected void deleteNote(ActionMode mode, int positionSelected) {
            mTodoList.remove(positionSelected);
            mToDoAdapter.notifyDataSetChanged();
            mode.finish();
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ((NoteOnItemLongClickListener) mList.getOnItemLongClickListener()).setActionMode(null);
        }
    };
}
