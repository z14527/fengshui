package com.gyq.fengshui;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class DataActivity extends AppCompatActivity {
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();

    private double billAmount = 100; // bill amount entered by the user
    private double percent = 0.15; // initial tip percentage
    private TextView amountTextView; // shows formatted bill amount
    private TextView percentTextView; // shows tip percentage
    private TextView totalTextView; // shows calculated total bill amount
    private String fs24 = "子癸丑艮寅甲卯乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬";
    private int fid = 0;
    private String type = "山";
    private String did= "";
    private double total= 0 ;
    DatabaseHelper mydb = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_layout);
        fid = getIntent().getIntExtra("fs24",0);
        type = getIntent().getStringExtra("type");
        did = getIntent().getStringExtra("id");
        // get references to programmatically manipulated TextViews
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        if(type.indexOf("水")>=0)
            amountTextView.setHint(getString(R.string.enter_amount2));
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        totalTextView = (TextView) findViewById(R.id.totalLabelTextView);
        totalTextView.setText(fs24.substring(fid,fid+1)+" 方向的 "+type+" 为： "+currencyFormat.format(0));
//
//        // set amountEditText's TextWatcher
        EditText amountEditText =
                (EditText) findViewById(R.id.amountEditText);
        amountEditText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
        amountEditText.addTextChangedListener(amountEditTextWatcher);
//
//        // set percentSeekBar's OnSeekBarChangeListener
        SeekBar percentSeekBar = (SeekBar) findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
        ImageButton qdBtn = (ImageButton)findViewById(R.id.imgBtnQD);
        qdBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mydb = new DatabaseHelper(DataActivity.this);
                int fid2=fid+1;
                String fd = ""+fid2;
                if(fid2<10)
                    fd="0"+fd;
                if(type.indexOf("山")>=0)
                    fd="shan"+fd;
                else
                    fd="shui"+fd;
                total=min(total,99);
                total=max(total,-99);
//                Toast.makeText(DataActivity.this,"did="+did+" fd="+fd+" fvalue="+round(total),Toast.LENGTH_SHORT).show();
                mydb.update(did,fd,""+round(total));
                Toast.makeText(DataActivity.this,"已经保存",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        ImageButton qxBtn = (ImageButton)findViewById(R.id.imgBtnQX);
        qxBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(DataActivity.this,"不保存，按返回键返回",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void calculate() {
        // format percent and display in percentTextView
        percentTextView.setText(percentFormat.format(percent));
        // calculate the tip and total
        if(type.indexOf("水")>=0){
            if(percent>=0.5) {
                percentTextView.setText(percentFormat.format((percent - 0.5) * 2));
                total = billAmount * 2 * (percent-0.5);
            }
            else {
                percentTextView.setText("-" + percentFormat.format(percent * 2));
                total = - billAmount * 2 * percent;
            }
            }else
            total = billAmount * percent;
        // display tip and total formatted as currency
        if(total<0)
            totalTextView.setText(fs24.substring(fid,fid+1)+" 方向的 "+type+" 为： -"+ currencyFormat.format(total));
        else
            totalTextView.setText(fs24.substring(fid,fid+1)+" 方向的 "+type+" 为： "+ currencyFormat.format(total));
    }

    // listener object for the SeekBar's progress changed events
    private final SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                // update percent, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    percent = progress/100.0; // set percent based on progress
                    calculate(); // calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            };

    // listener object for the EditText's text-changed events
    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            String REGEX = "[0-9]";
            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(s.toString());
            if(!matcher.find())
                return;
            if(s.length()>0)
                total = Double.parseDouble(s.toString());
            else
                total = 0;
            if(abs(total)>=100){
                Toast.makeText(DataActivity.this,"绝对值最大不超过99！", Toast.LENGTH_SHORT).show();
                total = 99;
            }
            totalTextView.setText(fs24.substring(fid,fid+1)+" 方向的 "+type+" 为： "+currencyFormat.format(total));
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MINUS) {
//            //              startActivity(new Intent(MainActivity.this, Input_Activity.class));
//  //          finish();
//        }
//        return true;//super.onKeyDown(keyCode, event);
//    }
    public void setDb(DatabaseHelper mydb1)             {
        mydb = mydb1;
    }
}
