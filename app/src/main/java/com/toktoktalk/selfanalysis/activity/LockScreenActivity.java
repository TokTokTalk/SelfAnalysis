package com.toktoktalk.selfanalysis.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.adapter.LockScreenGridAdapter;
import com.toktoktalk.selfanalysis.apis.FindOrCreate;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.model.IconVo;
import com.toktoktalk.selfanalysis.model.KeywordIcon;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.utils.DateUtils;
import com.toktoktalk.selfanalysis.utils.Logging;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 8. 9..
 */
public class LockScreenActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private GridView         iconsContainer;
    private ImageButton      btnLockClose;
    private LockScreenGridAdapter mGridAdapter;
    private ComPreference    mPref = new ComPreference(this);

    private List<KeywordIcon> mItems;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lockscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        initComponent();
    }

    private void initComponent(){
        iconsContainer = (GridView) findViewById(R.id.icons_container);
        btnLockClose = (ImageButton) findViewById(R.id.btn_lock_close);

        btnLockClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockScreenActivity.this.finish();
            }
        });

        mItems = getActiveIcons();

        iconsContainer.setAdapter(new LockScreenGridAdapter(this, mItems));
        iconsContainer.setOnItemClickListener(this);

    }


    private List<IconVo> getDummy(){

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/toctoktalk";

        List<IconVo> list = new ArrayList<IconVo>();

        File rootFolder = new File(path);

        if(!rootFolder.isDirectory()){
            return null;
        }

        String[] fileList = rootFolder.list();


        for (int i=0; i < fileList.length; i++)
        {
            list.add(new IconVo("id_"+i,"keyword_"+i, path+"/"+fileList[i]));
        }

        return list;
    }

    private List<KeywordIcon> getActiveIcons(){
        List<KeywordIcon> list = new ArrayList<KeywordIcon>();
        String json = mPref.getValue(Const.PREF_ACTIVE_KEYWORDS, null);

        Map activeIcons = (Map)GsonConverter.fromJson(json, Map.class);

        Iterator it = activeIcons.keySet().iterator();
        while(it.hasNext()){
            Map mapItem = (Map)activeIcons.get(it.next());
            KeywordIcon icon = new KeywordIcon();
            icon.set_id(mapItem.get("_id").toString());
            icon.setKeyword(mapItem.get("keyword").toString());
            icon.setIco_file_path(mapItem.get("icon_file_name").toString());

            list.add(icon);
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        KeywordIcon selectedIcon = mItems.get(position);

        String yyyyMMdd = DateUtils.formatter("yyyyMMdd");

        FindOrCreate params = new FindOrCreate();
        params.putFind("keyword_ref", selectedIcon.get_id());
        params.putFind("record_dt", yyyyMMdd);
        params.putCreate_doc("keyword_ref", selectedIcon.get_id());
        params.putCreate_doc("record_dt", yyyyMMdd);
        params.putCreate_doc("count", 1);
        params.putCreate_doc("timestamp", DateUtils.getTimeStamp());

        HttpClient client = new HttpClient(this);

        client.post("/database/recordKeyword", params, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object object) {
                Toast.makeText(LockScreenActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
            }
        }));

    }
}
