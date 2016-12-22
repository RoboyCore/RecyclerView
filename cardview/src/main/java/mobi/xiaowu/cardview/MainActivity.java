package mobi.xiaowu.cardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CardView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        cv = (CardView)findViewById(R.id.cv);
//        cv.setMaxCardElevation(100);//必须设置最大值

    }

    public void click(View v){
        switch (v.getId()) {
            case R.id.btn_01:
                System.out.println("v = " + cv.getRadius());
                System.out.println("v = " + cv.getCardElevation());
//                cv.setRadius(cv.getRadius()+20);
//                cv.setCardElevation(cv.getCardElevation()+2);
                break;
            case R.id.btn_02:
                cv.setRadius(cv.getRadius()+20);
//                cv.setCardElevation(cv.getCardElevation()-5);
                break;
        }
    }
}
