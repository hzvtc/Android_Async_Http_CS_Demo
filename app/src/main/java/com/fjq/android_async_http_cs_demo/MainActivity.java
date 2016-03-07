package com.fjq.android_async_http_cs_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    private TextView tvUsername;
    private TextView tvPassword;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();
        Init();
        setListener();
    }

    /*界面的初始化工作*/
    private void Init() {
        findViewById(R.id.login_btn).setOnClickListener(this);
    }

    /*为控件设置事件监听*/
    private void setListener() {

    }

    /*实例化布局文件的控件*/
    private void findViewById() {
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvPassword = (TextView) findViewById(R.id.tv_password);

        result = (TextView) findViewById(R.id.result);
    }

    private EditText getEtUsername(){
        return (EditText) findViewById(R.id.et_username);
    }

    private EditText getEtPassword(){
        return (EditText) findViewById(R.id.et_password);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                String username = getEtUsername().getText().toString();
                String password = getEtPassword().getText().toString();
                if(TextUtils.isEmpty(username.trim())||TextUtils.isEmpty(password.trim())){
                     showToast("用户名和密码不能为空");
                }
                else {
                    //如果用户名和密码已经输入 调用此方法
                    //loginByAsyncHttpClientPost(username, password);
                    loginByAsyncHttpClientGet(username,password);
                }
                break;
        }
    }
   //post请求
    private void loginByAsyncHttpClientPost(String username,String password){
        //创建异步请求对象
        final AsyncHttpClient client=new AsyncHttpClient();
        //请求的url 只能使用IP地址 不能使用localhost
        String ipAddress = "192.168.155.1";
        String url = "http://"+ipAddress+":8080/HelloWorld/login?";
        //请求的参数
        RequestParams requestParams = new RequestParams();
        requestParams.put("username",username);
        requestParams.put("password",password);
        //进行post请求 前面部分是请求 后面部分是响应
        client.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                //成功的状态码
                if (i == 200) {
                    result.setText(new String(bytes));
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(TAG, throwable + "");
                Toast.makeText(MainActivity.this, "Post请求失败 状态码为" + i, Toast.LENGTH_LONG).show();
            }
        });
    }

    //get请求 容易出现乱码
    private void loginByAsyncHttpClientGet(String username,String password){
        //创建异步请求对象
        AsyncHttpClient client=new AsyncHttpClient();
        //请求的url 只能使用IP地址 不能使用localhost
        String ipAddress = "192.168.155.1";
        String url = "http://"+ipAddress+":8080/HelloWorld/login?";
        //请求的参数
        RequestParams requestParams = new RequestParams();
        requestParams.put("username",username);
        requestParams.put("password",password);
        //进行get请求
        client.get(url, requestParams,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d("请求响应码",i+"");
                for (int j = 0;j<headers.length;j++){
                    Header header = headers[j];
                    Log.d("values","header name:"+header.getName()+" header value: "+header.getValue());
                }
                result.setText(new String(bytes));

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(TAG, throwable + "");
                Toast.makeText(MainActivity.this,"Get请求失败 状态码为"+i,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });
    }
    //提示信息
    private void showToast(String showText) {
        Toast.makeText(getApplicationContext(), showText, Toast.LENGTH_SHORT).show();
    }
}
