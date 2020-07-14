//package org.jzl.android.recyclerview;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ObjectAnimator;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.jzl.android.recyclerview.data.DataManager;
//import org.jzl.lang.util.ObjectUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import jp.co.cyberagent.android.gpuimage.GPUImage;
//import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;
//
//public class MainActivity extends AppCompatActivity {
//
//    private CommonlyAdapter<BitmapData, CommonlyViewHolder> adapter;
//    private DataManager<BitmapData> bitmapDataDataProvider;
//
//    private Button btnUnlock;
//    private RecyclerView recyclerView;
//    private ImageView ivMaskAnimation;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        btnUnlock = findViewById(R.id.btn_unlock);
//        recyclerView = findViewById(R.id.rv_test);
//        ivMaskAnimation = findViewById(R.id.iv_mask_animation);
//
//        CommonlyAdapterConfigurator<BitmapData, CommonlyViewHolder> commonlyAdapterConfigurator = new CommonlyAdapterConfigurator<>((itemView, viewType) -> new CommonlyViewHolder(itemView));
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open("test.png"));
//            commonlyAdapterConfigurator.data(split(bitmap, 2, 2).toArray(new BitmapData[0]));
//            commonlyAdapterConfigurator
//                    .adapter((adapter) -> {
//                        this.adapter = adapter;
//                        this.bitmapDataDataProvider = adapter.getDataManager();
//                    });
//            commonlyAdapterConfigurator.createItemView(R.layout.item_bitmap, false)
//                    .addOnViewAttachedToWindowListener(holder -> {
//                        set(holder.itemView, bitmap);
//                    })
//                    .bindItemData((holder, data, payloads) -> {
//                        if (data.locked) {
//                            holder.provide().setImageBitmap(R.id.iv_test, data.filterBitmap);
//                        } else {
//                            holder.provide().setImageBitmap(R.id.iv_test, data.bitmap);
//                        }
//                    })
//                    .bindItemData((holder, data, payloads) -> {
//                        holder.provide().bindClickListener(R.id.iv_test, v -> {
//                            if (data.locked) {
//                                data.locked = false;
//                                adapter.notifyItemChanged(holder.getAdapterPosition());
//                            }
//                        });
//                    })
//                    .addOnAttachedToRecyclerViewListener((contextProvider, recyclerView, adapter) -> recyclerView.setLayoutManager(new GridLayoutManager(contextProvider.provide(), 2, GridLayoutManager.VERTICAL, false)))
//                    .attachToRecyclerView(recyclerView);
//        } catch (IOException e) {
//        }
//        btnUnlock.setOnClickListener(v -> {
//            for (int i = 0; i < bitmapDataDataProvider.getDataCount(); i++) {
//                BitmapData data = bitmapDataDataProvider.getData(i);
//                if (data.locked) {
//                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
//                    if (ObjectUtils.nonNull(holder)) {
//                        unlock(i, data, holder.itemView, ivMaskAnimation);
//                        return;
//                    }
//                }
//            }
//        });
//
//        findViewById(R.id.btn_test).setOnClickListener(v -> {
//            startActivity(new Intent(this, TestActivity.class));
//        });
//    }
//
//    private void unlock(int adapterPosition, BitmapData bitmapData, View targetView, ImageView animationView) {
//        targetView.post(() -> {
//            ViewGroup.LayoutParams animationParams = animationView.getLayoutParams();
//            animationParams.width = targetView.getWidth();
//            animationParams.height = targetView.getHeight();
//            animationView.setLayoutParams(animationParams);
//            int[] targetLocation = new int[2];
//            int[] animationLocation = new int[2];
//            targetView.getLocationOnScreen(targetLocation);
//            animationView.getLocationOnScreen(animationLocation);
//
//            animationView.setX(animationView.getX() + targetLocation[0] - animationLocation[0]);
//            animationView.setY(animationView.getY() + targetLocation[1] - animationLocation[1]);
//            animationView.setImageBitmap(bitmapData.bitmap);
//            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(animationView, "x", (adapterPosition % 2 == 0 ? -400 : 400) + animationView.getX(), animationView.getX());
//            objectAnimator.setDuration(1000);
//            objectAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    bitmapData.locked = false;
//                    adapter.notifyItemChanged(adapterPosition);
//                }
//            });
//            objectAnimator.start();
//        });
//    }
//
//    private static class BitmapData {
//        private int id;
//        private boolean locked;
//        private Bitmap bitmap;
//        private Bitmap filterBitmap;
//    }
//
//    private void set(View imageView, Bitmap bitmap) {
//        imageView.post(() -> {
//            View parent = (View) imageView.getParent();
//            ViewGroup.LayoutParams params = imageView.getLayoutParams();
//            params.width = parent.getWidth() / 2;
//            params.height = (int) (params.width * (bitmap.getHeight() * 1f / bitmap.getWidth()));
//            imageView.setLayoutParams(params);
//        });
//    }
//
//    public List<BitmapData> split(Bitmap bitmap, int w, int h) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int rowHeight = height / h;
//        int colWidth = width / w;
//        GPUImage gpuImage = new GPUImage(this);
//        gpuImage.setFilter(new GPUImageSketchFilter());
//        Bitmap filterBitmap = gpuImage.getBitmapWithFilterApplied(bitmap, false);
//        ArrayList<BitmapData> bitmaps = new ArrayList<>(w * h);
//        for (int y = 0; y < h; y++) {
//            for (int x = 0; x < w; x++) {
//                BitmapData data = new BitmapData();
//                data.id = y * w + x;
//                data.bitmap = Bitmap.createBitmap(bitmap, x * colWidth, y * rowHeight, colWidth, rowHeight);
//                data.filterBitmap = Bitmap.createBitmap(filterBitmap, x * colWidth, y * rowHeight, colWidth, rowHeight);
//                data.locked = true;
//                bitmaps.add(data);
//            }
//        }
//        return bitmaps;
//    }
//}