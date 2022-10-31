package com.example.chatme;

import static com.example.chatme.LoginActivity.currentAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;

    public static final String id_acc= currentAccount.get_id();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ViewPager2 viewPager2 = findViewById(R.id.view_pager2);
        viewPager2.setAdapter(new PagerFragment(this));
        tabLayout = findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Home");
                        tab.setIcon(R.drawable.ic_baseline_home_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.purple_200)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(3);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }
                    case 1: {
                        tab.setText("Chat");
                        tab.setIcon(R.drawable.ic_baseline_chat_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.purple_200)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(64);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }
                    case 2: {
                        tab.setText("Me");
                        tab.setIcon(R.drawable.ic_baseline_perm_identity_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.purple_200)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(6);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }

                }
            }
        });
        tabLayoutMediator.attach();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
            }
        });


    }

}