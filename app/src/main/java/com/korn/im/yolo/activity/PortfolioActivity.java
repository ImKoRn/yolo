package com.korn.im.yolo.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.PicturesAdapter;
import com.korn.im.yolo.fragments.PortfolioListFragment;
import com.korn.im.yolo.objects.Portfolio;

import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {
    private static final String ORDER_SUBJECT = "Test";
    private static final String CONTACTS_FIELD = "Contacts:\n";
    private static final Uri TARGET_URI = Uri.parse("mailto:kor.wolf13@gmail.com");


    private TextView descriptionView;
    private PicturesAdapter picturesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            finish();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        initUi();

        showPhotographer();
    }

    private void showPhotographer() {
        Portfolio portfolio = getIntent().getParcelableExtra(PortfolioListFragment.SELECTED_PORTFOLIO);

        getSupportActionBar().setTitle(portfolio.getTitle());

        descriptionView.setText(portfolio.getContent());

        picturesAdapter.imageUrlList = portfolio.getListOfPhotoReferences();
        picturesAdapter.notifyDataSetChanged();
    }

    private void initUi() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        descriptionView = (TextView) findViewById(R.id.descriptionView);
        FloatingActionButton sendBtn = (FloatingActionButton) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBottomSheet();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(picturesAdapter = new PicturesAdapter(this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void createBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PortfolioActivity.this);
        bottomSheetDialog.setContentView(LayoutInflater.from(PortfolioActivity.this).inflate(R.layout.options, null));
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        ((NavigationView) bottomSheetDialog.findViewById(R.id.navView))
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.emailOnly : {
                                sendOrderEmail(false);
                                break;
                            }
                            case R.id.emailAndPhone : {
                                sendOrderEmail(true);
                                break;
                            }
                        }
                        bottomSheetDialog.cancel();
                        return true;
                    }
                });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    private void sendOrderEmail(boolean appendPhoneAndEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(TARGET_URI);
        intent.putExtra(Intent.EXTRA_SUBJECT, ORDER_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, createText(appendPhoneAndEmail));
        startActivity(Intent.createChooser(intent, "Choice email client"));
    }

    private String createText(boolean appendPhoneAndEmail) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(CONTACTS_FIELD)
                     .append("Email: ").append(getEmail().get(0)).append('\n');
        if (appendPhoneAndEmail) stringBuilder.append("Phone: ").append(getPhoneNumber());

        return stringBuilder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    private ArrayList<String> getEmail() {
        Account[] accounts = AccountManager.get(this).getAccounts();

        ArrayList<String> emails = new ArrayList<>();

        for (Account account : accounts) {
            if(Patterns.EMAIL_ADDRESS.matcher(account.name).matches())
                emails.add(account.name);
        }

        return emails;
    }
}
