package yellow5a5.materialdesignlogin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import yellow5a5.materialdesignlogin.View.CatchScrollLayout;
import yellow5a5.materialdesignlogin.View.SignUpContainer;

import com.example.LJJ.Global.MyApp;
import com.example.LJJ.MyUser.User;
import com.example.LJJ.UI.LoadingFrame;
import com.example.sf.CONSTANTS_SF;
import com.example.sf.Server.Server;
import com.example.sf.amap3d.MainActivity;
import com.example.sf.amap3d.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.Connection;

import static com.example.sf.CONSTANTS_SF.URL_ROOT;

public class LoginActivity extends AppCompatActivity {

    private CatchScrollLayout mCatchScrollLayout;
    private SignUpContainer mSignUpContainer;
    private String ERR_MESSAGE="errout";
    private LinearLayout frame;
    private EditText account;
    private EditText password;
    private ImageButton cana;
    private ImageButton canp;
    private Button newaccount;
    private CardView userlogin;
    private Button passengerlogin;
    private User user;
    private Connection connect;
    private Handler handler;
    private boolean isvalid;
    private Dialog dialog;
    private MyApp app;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_1);
        initView();
        app=(MyApp)getApplication();
        isLogin=false;
    }

    private void initView() {
        mCatchScrollLayout = (CatchScrollLayout) findViewById(R.id.catch_sroll_layout);
        mSignUpContainer = (SignUpContainer) findViewById(R.id.sign_up_container);
        account = (EditText) findViewById(R.id.email_edit_input);
        password = (EditText) findViewById(R.id.password_edit_input);
        userlogin = (CardView) findViewById(R.id.confirm_card_v);
        mCatchScrollLayout.setIScrollCallBack(new CatchScrollLayout.IScrollCallBack() {
            @Override
            public void onScrollProcess(int process, boolean isLeft) {
                if (!isLeft) {
                    process = 100 - process;

                }
                mSignUpContainer.setAnimProportion(process);
            }
        });

        mSignUpContainer.setIConfirmCallBack(new SignUpContainer.IConfirmCallBack() {
            @Override
            public void goNext() {
            }
        });

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignUpContainer.getmState() == SignUpContainer.LOGIN_STATE) {
                    isvalid = false;
                    dialog = LoadingFrame.createLoadingDialog(LoginActivity.this, "连接服务器中...");
                    valid(account.getText().toString(), password.getText().toString());

                } else {
                    dialog = LoadingFrame.createLoadingDialog(LoginActivity.this, "请稍候...");
                    android.os.Handler handler = new android.os.Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            LoadingFrame.closeDialog(dialog);
                            System.err.println(msg.obj.toString());
                            if (true) {
                                finish();
                            }
                        }


                    };
                    Server server = new Server(handler, CONSTANTS_SF.URL_ROOT + "insertinfo");
                    JSONObject json = new JSONObject();
                    try {
                        json.put("username", account);
                        json.put("password", password);
                        json.put("neckname", RandomEXP.nextchar());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    server.post(json.toString());

                }

                //change this method and add the parameters you wanted


            }
        });
    }

            private void valid(final String username, final String password) {

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        LoadingFrame.closeDialog(dialog);
                        String result = msg.obj.toString();
                        System.err.println(result);
                        try {
                            JSONObject json = new JSONObject(new JSONTokener(result));
                            if (result.equals("{\"total\":0}")) {
                                showDialog();
                            } else if (username.equals(json.getJSONObject("0").getString("username")) && password.equals(json.getJSONObject("0").getString("password"))) {
                                user = new User();
                                user.setPassword(json.getJSONObject("0").getString("password"));
                                user.setAccount(json.getJSONObject("0").getString("username"));
                                user.setId(json.getJSONObject("0").getString("id"));
                                user.setNickname(json.getJSONObject("0").getString("neckname"));
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                app.setUser(user);
                           /* Bundle b=new Bundle();
                            b.putParcelable("user",user);
                            i.putExtras(b);*/
                                startActivity(i);
                                finish();
                                return;
                            } else {
                                showDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                String url = URL_ROOT + "validation";
                Server server = new Server(handler, URL_ROOT + "/validation");
                server.post(username);

            }

            public void showDialog() {
                AlertDialog ad = new AlertDialog.Builder(LoginActivity.this).setCancelable(true).setMessage("用户名或密码不正确").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }


    }
