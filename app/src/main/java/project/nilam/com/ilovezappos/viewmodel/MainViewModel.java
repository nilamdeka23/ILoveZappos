package project.nilam.com.ilovezappos.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import project.nilam.com.ilovezappos.R;
import project.nilam.com.ilovezappos.ZapposApplication;
import project.nilam.com.ilovezappos.model.Product;
import project.nilam.com.ilovezappos.model.ResultSet;
import project.nilam.com.ilovezappos.model.ZapposService;
import project.nilam.com.ilovezappos.view.MainActivity;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


public class MainViewModel implements ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private static final String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";

    public ObservableInt infoMessageVisibility;
    public ObservableInt progressVisibility;
    public ObservableInt cardViewVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableInt animImageVisibility;
    public ObservableInt fabVisibility;
    public ObservableField<String> infoMessage;

    public ObservableField<String> productName;
    public ObservableField<String> brandName;
    public ObservableField<String> price;
    public ObservableField<String> productId;

    public ObservableField<Drawable> profileImage;
    public ObservableField<Drawable> animImage;
    private BindableFieldTarget bindableFieldTarget;
    private BindableFieldTarget bindableFieldTargetAnim;

    private Context context;
    private Subscription subscription;
    private DataListener dataListener;
    private String editTextSearchValue;
    private Product product;

    public MainViewModel(Context context, DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        progressVisibility = new ObservableInt(View.INVISIBLE);
        cardViewVisibility = new ObservableInt(View.INVISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
        animImageVisibility = new ObservableInt(View.GONE);
        fabVisibility = new ObservableInt(View.GONE);
        infoMessage = new ObservableField<>(context.getString(R.string.default_info_message));

        brandName = new ObservableField<>(context.getString(R.string.default_empty_string));
        price = new ObservableField<>(context.getString(R.string.default_empty_string));
        productName = new ObservableField<>(context.getString(R.string.default_empty_string));
        productId = new ObservableField<>(context.getString(R.string.default_empty_string));
        profileImage = new ObservableField<>();
        animImage = new ObservableField<>();
        bindableFieldTarget = new BindableFieldTarget(profileImage, context.getResources());
        bindableFieldTargetAnim = new BindableFieldTarget(animImage, context.getResources());
    }

    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
    }

    public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String username = view.getText().toString();
            if (username.length() > 0) loadSearchResult(username);
            return true;
        }
        return false;
    }

    public void onClickSearch(View view) {
        loadSearchResult(editTextSearchValue);
    }

    public void onClickCart(View view) {
        addToCart(view);
    }

    public void onClickShare(View view) {
        Toast.makeText(context,
                "Under construction...", Toast.LENGTH_SHORT).show();
    }

    public void addToCart(View view) {
        animImageVisibility.set(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 400, 0, 400);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animImageVisibility.set(View.INVISIBLE);

                Toast.makeText(context,
                        product.getProductName()
                                + " added to cart!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

        // TODO: find workaround(more optimal)-downcasting is bad
        ((MainActivity) context).findViewById(R.id.image_anim).startAnimation(animation);

        YoYo.with(Techniques.ZoomOut)
                .duration(200)
                .playOn(view);

        YoYo.with(Techniques.ZoomIn)
                .duration(500)
                .playOn(view);
    }


    public TextWatcher getSearchEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextSearchValue = charSequence.toString();
                searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void loadSearchResult(String term) {
        progressVisibility.set(View.VISIBLE);
        cardViewVisibility.set(View.INVISIBLE);
        infoMessageVisibility.set(View.INVISIBLE);
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ZapposApplication application = ZapposApplication.get(context);
        ZapposService zapposService = application.getZapposService();
        subscription = zapposService.getSearchResult(term, API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<ResultSet>() {
                    @Override
                    public void onCompleted() {
                        progressVisibility.set(View.INVISIBLE);
                        cardViewVisibility.set(View.VISIBLE);
                        fabVisibility.set(View.VISIBLE);
                        dataListener.onLoad();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error fetching results", error);
                        progressVisibility.set(View.INVISIBLE);
                        if (isHttp404(error)) {
                            infoMessage.set(context.getString(R.string.error_username_not_found));
                        } else {
                            infoMessage.set(context.getString(R.string.error_loading_results));
                        }
                        infoMessageVisibility.set(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ResultSet resultSet) {

                        product = resultSet.getResults().get(0);
                        brandName.set(product.getBrandName());
                        productName.set(product.getProductName());
                        price.set(context.getString(R.string.price, product.getPrice()));
                        productId.set(context.getString(R.string.product_id, product.getProductId()));

                        Picasso.with(context)
                                .load(product.getThumbnailImageUrl())
                                .placeholder(R.mipmap.ic_placeholder)
                                .into(bindableFieldTarget);

                        Picasso.with(context)
                                .load(product.getThumbnailImageUrl())
                                .placeholder(R.mipmap.ic_placeholder)
                                .into(bindableFieldTargetAnim);

                        Log.i(TAG, "Results loaded " + resultSet);
                    }
                });

    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

    public interface DataListener {
        void onLoad();
    }

    public class BindableFieldTarget implements Target {

        private ObservableField<Drawable> observableField;
        private Resources resources;

        public BindableFieldTarget(ObservableField<Drawable> observableField, Resources resources) {
            this.observableField = observableField;
            this.resources = resources;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            observableField.set(new BitmapDrawable(resources, bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            observableField.set(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            observableField.set(placeHolderDrawable);
        }
    }

}
