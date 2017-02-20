package com.telerikacademy.meetup.view.sign_in;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.telerikacademy.meetup.BaseApplication;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.config.di.module.ControllerModule;
import com.telerikacademy.meetup.model.User;
import com.telerikacademy.meetup.ui.fragments.base.IToolbar;
import com.telerikacademy.meetup.util.base.*;
import com.telerikacademy.meetup.view.home.HomeActivity;
import com.telerikacademy.meetup.view.sign_up.SignUpActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    @Inject
    IHttpRequester httpRequester;
    @Inject
    IJsonParser jsonParser;
    @Inject
    IUserSession userSession;
    @Inject
    IHashProvider hashProvider;
    @Inject
    FragmentManager fragmentManager;

    @BindView(R.id.username)
    EditText usernameEditText;
    @BindView(R.id.password)
    EditText passwordEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        injectDependencies();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        IToolbar toolbar = (IToolbar)
                fragmentManager.findFragmentById(R.id.fragment_home_header);

        if (toolbar != null) {
            toolbar.inflateMenu(R.menu.main, menu, getMenuInflater());
        }

        return true;
    }

    @OnClick(R.id.btn_sign_in)
    void signInUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals("")) {
            this.usernameEditText.setError(getString(R.string.enter_username));
            return;
        }

        if (password.equals("")) {
            this.passwordEditText.setError(getString(R.string.enter_password));
            return;
        }

        String passHash = this.hashProvider.providePasswordHash(password);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("passHash", passHash);

        String url = this.getString(R.string.auth_login_post);

        final Context context = getApplicationContext();
        final Activity currentActivity = this;
        final IUserSession userSession = this.userSession;
        final IJsonParser jsonParser = this.jsonParser;
        final EditText usernameEditText = this.usernameEditText;
        final EditText passwordEditText = this.passwordEditText;

        this.httpRequester.post(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IHttpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(IHttpResponse value) {
                        String responseBody = value.getBody().toString();
                        String userJsonObject;
                        User resultUser;
                        try {
                            userJsonObject = jsonParser.toJsonFromResponseBody(responseBody);
                            resultUser = jsonParser.fromJson(userJsonObject, User.class);
                        } catch (IllegalStateException e) {
                            usernameEditText.setError(getString(R.string.invalid_details));
                            passwordEditText.setError(getString(R.string.invalid_details));
                            return;
                        }

                        userSession.setUsername(resultUser.getUsername());
                        userSession.setId(resultUser.getId());
                        Toast.makeText(context, getString(R.string.sign_in_successfull) + " " + resultUser.getUsername(), Toast.LENGTH_LONG).show();
                        Intent homeIntent = new Intent(currentActivity, HomeActivity.class);
                        startActivity(homeIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @OnClick(R.id.link_signup)
    void redirectToSignUp() {
        Intent signUpIntent = BaseApplication.createIntent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    private void injectDependencies() {
        BaseApplication
                .from(this)
                .getComponent()
                .getControllerComponent(new ControllerModule(
                        this, getSupportFragmentManager()
                ))
                .inject(this);

        ButterKnife.bind(this);
    }
}
