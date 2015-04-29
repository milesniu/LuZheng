package com.miles.maipu.luzheng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsCreatActivity;
import com.miles.maipu.util.GalleryData;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UGallery;
import com.miles.maipu.util.UnixTime;
import com.time.ScreenInfo;
import com.time.WheelMain;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class CreatTaskActivity extends AbsCreatActivity
{

    ImageView img_Photo = null;
    // private String imgPath = null;
    private List<HashMap<String, Object>> roadlist = new Vector<HashMap<String, Object>>();
    private List<HashMap<String, Object>> organizalist = new Vector<HashMap<String, Object>>();
    private List<HashMap<String, Object>> personlist = new Vector<HashMap<String, Object>>();
    private List<HashMap<String, Object>> categorylist = new Vector<HashMap<String, Object>>();
    private Spinner sp_road;
    private Spinner sp_lane;
    private Spinner sp_Organization;
    private Spinner sp_Person;
    private Spinner sp_Category;
    private Spinner sp_Project;
    private Spinner sp_Type;
    private boolean isgetorga = false;
    private boolean islines = false;
    private boolean isgetcate = false;
    // private Bitmap bit = null;
    private EditText edit_zhuanghao;
    //	private EditText edit_zhuanghaom;
    private EditText edit_descrtion;
    private EditText edit_jiaoban;
    private String Type = "";
    private EditText edit_k;
    private EditText edit_m;
    private AlertDialog builder;
    private TextView tv_time;
    private boolean isjidu = false;

    // private String uploadurl="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_task);
        Type = getIntent().getStringExtra("type");
        isjidu = getIntent().getBooleanExtra("isjidu",false);
        initView();
        if (!OverAllData.isNeedUploadEvent())// 如果是顶层领导，不允许上报
        {
            sp_Type.setSelection(1);
            sp_Type.setEnabled(false);
        } else if (OverAllData.getPostion() < 2) // 中队人员不允许交办
        {
            sp_Type.setSelection(0);
            sp_Type.setEnabled(false);
        }

    }

    public void initView()
    {
        // TODO Auto-generated method stub
        Btn_Left = (Button) findViewById(R.id.bt_left);
        Btn_Right = (Button) findViewById(R.id.bt_right);
        text_title = (TextView) findViewById(R.id.title_text);
        List_Content = (ListView) findViewById(R.id.list_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        if (Btn_Left != null)
        {
            Btn_Left.setOnClickListener(this);
        }
        if (Btn_Right != null)
        {
            Btn_Right.setOnClickListener(this);
        }
        Btn_Right.setBackgroundResource(R.drawable.btsure);
        text_title.setText("新增案件");

        img_Photo = (ImageView) findViewById(R.id.img_photo);
        img_Photo.setOnClickListener(this);
        sp_road = (Spinner) findViewById(R.id.sp_road);
        sp_lane = (Spinner) findViewById(R.id.sp_lane);
        sp_Category = (Spinner) findViewById(R.id.sp_category);
        sp_Project = (Spinner) findViewById(R.id.sp_project);
        sp_Organization = (Spinner) findViewById(R.id.sp_Organization);
        sp_Person = (Spinner) findViewById(R.id.sp_person);
        edit_zhuanghao = (EditText) findViewById(R.id.edit_zhuanghao);
//		edit_zhuanghaom = (EditText) findViewById(R.id.edit_zhuanghaom);
        sp_Type = (Spinner) findViewById(R.id.sp_type);
        edit_zhuanghao.setOnClickListener(this);
        edit_zhuanghao.setInputType(InputType.TYPE_NULL);
        edit_jiaoban = (EditText) findViewById(R.id.edit_jiaoban);

        // edit_UnitNum.setOnClickListener(this);
        // edit_UnitNum.setInputType(InputType.TYPE_NULL);

        edit_descrtion = (EditText) findViewById(R.id.edid_descrption);
        gallery = (UGallery) findViewById(R.id.gallery_photo);
        tv_time.setOnClickListener(this);

        if (OverAllData.isFirstDepartment()||isjidu)
        {
            tv_time.setVisibility(View.VISIBLE);
        } else
        {
            tv_time.setVisibility(View.GONE);
        }

        ComposGallery(gallery);
        showprogressdialog();
        getspinnerData();
    }

    // 根据机构获取机构下人员
    private void getPerson(String oid)
    {
        showprogressdialog();
        new SendDataTask()
        {

            @Override
            protected void onPostExecute(Object result)
            {
                // TODO Auto-generated method stub
                try
                {
                    hideProgressDlg();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                personlist.clear();

                personlist = (List<HashMap<String, Object>>) result;

                String[] arraystr = new String[personlist.size()];
                for (int i = 0; i < personlist.size(); i++)
                {
                    arraystr[i] = personlist.get(i).get("Name") + "";
                }
                sp_Person.setAdapter(new MySpinnerAdapter(mContext, arraystr));
                super.onPostExecute(result);
            }

        }.execute(new ParamData(ApiCode.GetPersonInformationByOrganization, oid));
    }

    // 根据自己机构的人员 0，下属，1上属
    private void getSubordPerson(String upordown)
    {
        showprogressdialog();
        new SendDataTask()
        {

            @Override
            protected void onPostExecute(Object result)
            {
                // TODO Auto-generated method stub
                hideProgressDlg();
                personlist.clear();

                personlist = (List<HashMap<String, Object>>) result;

                String[] arraystr = new String[personlist.size()];
                for (int i = 0; i < personlist.size(); i++)
                {
                    arraystr[i] = personlist.get(i).get("Name") + "";
                }
                sp_Person.setAdapter(new MySpinnerAdapter(mContext, arraystr));

                super.onPostExecute(result);
            }

        }.execute(new ParamData(ApiCode.GetSubordinate, OverAllData.getLoginId(), upordown));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        // localpath = getCamera(img_Photo, localimg, requestCode, resultCode,
        // data);
        GalleryData imgdata = getCamera(bitlist.size() + "", requestCode, resultCode, data);
        if (imgdata != null)
        {
            bitlist.add(bitlist.size() - 1, imgdata);
            imageAdapter.notifyDataSetChanged();
            compostPoint();
        } else
        {
            Toast.makeText(mContext, "请重新拍照", 0);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.img_photo:
                // goCameargetPhoto();
                goCamera();
                break;
            case R.id.bt_right:
                String zhuanghao = edit_zhuanghao.getText().toString();
                String desc = edit_descrtion.getText().toString();
                if (zhuanghao.equals(""))
                {
                    Toast.makeText(mContext, "请输入桩号", 0).show();
                    return;
                }
                else if(isjidu&&OverAllData.isFirstDepartment()&&tv_time.getText().toString().equals("请选择时间"))
                {
                    Toast.makeText(mContext, "请选择时间", 0).show();
                    return;
                }
                else
                {
                    // uploadEventData();
                    showprogressdialog();
                    uplaodPic();
                }

                break;
            case R.id.edit_zhuanghao:
                selectMark();
//			new SelectMarkDlg(mContext).ShowDlg(edit_zhuanghao);
                break;
            case R.id.tv_time:
                showSelecTime();
                break;
        }
        super.onClick(v);
    }

    WheelMain wheelMain;

    private void showSelecTime()
    {
        LayoutInflater inflater = getLayoutInflater();
        View timepickerview = inflater.inflate(R.layout.timepicker, (ViewGroup) findViewById(R.id.timePicker1));
        ScreenInfo screenInfo = new ScreenInfo(this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
        new AlertDialog.Builder(this)
                .setTitle("选择时间")
                .setView(timepickerview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(Long.parseLong(UnixTime.getStrCurrentUnixTime())>=UnixTime.simpleTime2Unix(wheelMain.getTime()+" 18:30"))
                        {
                            Toast.makeText(mContext,"完成时间必须大于当前时间",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tv_time.setText(wheelMain.getTime()+" 18:30:00");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
    }


    private void selectMark()
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_selectmark, null);
        edit_k = (EditText) layout.findViewById(R.id.edit_zhuanghaok);
        edit_m = (EditText) layout.findViewById(R.id.edit_zhuanghaom);

        builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle("桩号").setPositiveButton("确定", new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                String k = edit_k.getText().toString();
                String m = edit_m.getText().toString();
                edit_zhuanghao.setText("K" + k + "+" + m + "M");
                // Toast.makeText(mContexkt, tid, 0).show();
            }
        }).setNegativeButton("取消", null).show();

    }

    private void selecOrg()
    {
        if (personlist != null && personlist.size() > 0)
        {
            organizalist.clear();
            personlist.clear();
            sp_Person.setAdapter(new MySpinnerAdapter(mContext, new String[]{}));
        }

        // 获取机构
        new SendDataTask()
        {

            @SuppressWarnings("unchecked")
            @Override
            protected void onPostExecute(Object result)
            {
                // TODO Auto-generated method stub
                organizalist.clear();
                isgetorga = true;
                if (islines || isgetcate)
                {
                    hideProgressDlg();
                }
                if (Type.equals("1") && OverAllData.getPostion() == 1) // 中队长交办
                {
                    organizalist.add(OverAllData.getMyOrganization());
                }
//				else if(Type.equals("0") && OverAllData.getPostion() == 0)//巡查员上报
//				{
//					organizalist.add(OverAllData.getMyOrganization());
//				}
                else
                {
                    try
                    {
                        organizalist = (List<HashMap<String, Object>>) result;
                    } catch (Exception e)
                    {
                        organizalist = new Vector<HashMap<String, Object>>();
                    }
                }


                // organizalist.add(0,
                // OverAllData.getMyOrganization());//添加同一级机构，同级机构间可以分配给下属
                String[] arraystr = new String[organizalist.size()];
                for (int i = 0; i < organizalist.size(); i++)
                {
                    arraystr[i] = organizalist.get(i).get("Name") + "";
                }
                sp_Organization.setAdapter(new MySpinnerAdapter(mContext, arraystr));
                sp_Organization.setOnItemSelectedListener(new OnItemSelectedListener()
                {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                    {
                        // TODO Auto-generated method stub
                        if (Type.equals("1") && OverAllData.getPostion() == 1)// 中队长，交办
                        {
                            getSubordPerson("0");
                        }
//						else if(Type.equals("0") && OverAllData.getPostion() == 0)//巡查员上报
//						{
//							getSubordPerson("1");
//						}
                        else
                        {
                            // 获取机构下人员
                            getPerson(organizalist.get(arg2).get("ID").toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0)
                    {
                        // TODO Auto-generated method stub

                    }
                });
                super.onPostExecute(result);
            }

        }.execute(new ParamData(ApiCode.GetOrganizationUpOrDown, OverAllData.getLoginId(), Type.equals("0") ? "1" : "0"));// 0，获取下属机构

    }

    // 获取机构与线路
    private void getspinnerData()
    {

        // 获取巡查分类与巡查项
        new SendDataTask()
        {

            @Override
            protected void onPostExecute(Object result)
            {
                // TODO Auto-generated method stub
                isgetcate = true;
                if (islines && isgetorga)
                {
                    hideProgressDlg();
                }
                categorylist = (List<HashMap<String, Object>>) result;

                String[] arraystr = new String[categorylist.size()];
                for (int i = 0; i < categorylist.size(); i++)
                {
                    arraystr[i] = categorylist.get(i).get("Name") + "";
                }
                sp_Category.setAdapter(new MySpinnerAdapter(mContext, arraystr));
                sp_Category.setOnItemSelectedListener(new OnItemSelectedListener()
                {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                    {
                        // TODO Auto-generated method stub
                        List<HashMap<String, Object>> prolist = (List<HashMap<String, Object>>) (categorylist.get(arg2).get("PatorlItems"));
                        String[] arraystr = new String[prolist.size()];
                        for (int i = 0; i < prolist.size(); i++)
                        {
                            arraystr[i] = prolist.get(i).get("Name") + "";
                        }
                        sp_Project.setAdapter(new MySpinnerAdapter(mContext, arraystr));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0)
                    {
                        // TODO Auto-generated method stub

                    }
                });
                super.onPostExecute(result);
            }

        }.execute(new ParamData(ApiCode.GetAllPatorlCateGoryAndItems, ""));

        selecOrg();

        // 获取线路
        new SendDataTask()
        {

            @Override
            protected void onPostExecute(Object result)
            {
                // TODO Auto-generated method stub
                islines = true;
                if (isgetorga || isgetcate)
                {
                    hideProgressDlg();
                }
                try
                {
                    roadlist = (List<HashMap<String, Object>>) result;
                } catch (Exception e)
                {
                    Toast.makeText(mContext, result.toString(), 0).show();
                    CreatTaskActivity.this.finish();
                    return;
                }
                if (roadlist == null || roadlist.size() == 0)
                    return;
                String[] arraystr = new String[roadlist.size()];
                for (int i = 0; i < roadlist.size(); i++)
                {
                    arraystr[i] = roadlist.get(i).get("Name") + "";
                }
                sp_road.setAdapter(new MySpinnerAdapter(mContext, arraystr));

                super.onPostExecute(result);
            }

        }.execute(new ParamData(ApiCode.GetRoadLinesByPersonID, OverAllData.getLoginId()));

        // 组装上行下行
        String[] arraystr = new String[]
                {"上行", "下行"};
        sp_lane.setAdapter(new MySpinnerAdapter(mContext, arraystr));

        String[] arraytype = new String[]
                {"案件上报", "案件交办"};
        sp_Type.setAdapter(new MySpinnerAdapter(mContext, arraytype));
        sp_Type.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                // TODO Auto-generated method stub
                if (arg2 == 0)
                {
                    Type = "0";
                    edit_jiaoban.setVisibility(View.GONE);
                } else if (arg2 == 1)
                {
                    Type = "1";
                    edit_jiaoban.setVisibility(View.VISIBLE);
                }

                selecOrg();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.creat_task, menu);
        return true;
    }

    @Override
    public void UploadData()
    {
        // TODO Auto-generated method stub

        Map<String, Object> senddata = new HashMap<String, Object>();

        String loginid = OverAllData.getLoginId();

        Map<String, Object> p1 = new HashMap<String, Object>();
        p1.put("ID", loginid);
        senddata.put("SendPersonInformation", p1);// 登陆id

        Map<String, Object> pp = new HashMap<String, Object>();
        pp.put("ID", loginid);
        senddata.put("SubmitPersonInformation", pp);// 登陆id

        String roadid = roadlist.get(sp_road.getSelectedItemPosition()).get("ID") + "";
        Map<String, Object> p2 = new HashMap<String, Object>();
        p2.put("ID", roadid);
        senddata.put("RoadLine", p2);

        String PatorlItem = ((List<HashMap<String, Object>>) categorylist.get(sp_Category.getSelectedItemPosition()).get("PatorlItems")).get(sp_Project.getSelectedItemPosition()).get("ID") + "";

        Map<String, Object> p3 = new HashMap<String, Object>();
        p3.put("ID", PatorlItem);
        senddata.put("PatorlItem", p3);

        senddata.put("LatitudeLongitude", myLocation.getLongitude() + "," + myLocation.getLatitude());
        senddata.put("Mark", edit_zhuanghao.getText().toString());
        if(OverAllData.isFirstDepartment()&&isjidu)
        {
            senddata.put("CutOffTime",tv_time.getText().toString());
        }
        String pictrues = "";
        for (int i = 0; i < bitlist.size() - 1; i++)
        {
            pictrues = pictrues + (bitlist.get(i).getUrlPath() + "|");
        }
        pictrues = pictrues.substring(0, pictrues.length() - 1);

        senddata.put("Picture", pictrues);
        senddata.put("EventContent", edit_descrtion.getText().toString().replaceAll(" ", ""));

        // Map<String, Object> p4 = new HashMap<String, Object>();
        // p4.put("Opinion", edit_jiaoban.getText().toString());
        // senddata.put("EventReceives", p4);

        // senddata.put("EventReceives",
        // URLEncoder.encode(edit_jiaoban.getText().toString()));

        String jiaoban = edit_jiaoban.getText().toString().replaceAll(" ", "");
        if (jiaoban.equals(""))
        {
            jiaoban = "null";
        }
        String pid = personlist.get(sp_Person.getSelectedItemPosition()).get("ID") + "";

        new SendDataTask()
        {

            @Override
            protected void onPostExecute(Object result)
            {
                // TODO Auto-generated method stub
                hideProgressDlg();
                HashMap<String, Object> res = (HashMap<String, Object>) result;
                if (res != null && res.get("IsSuccess").toString().equals("true"))
                {
                    Toast.makeText(mContext, "事件新增成功", 0).show();
                    CreatTaskActivity.this.finish();
                } else
                {
                    Toast.makeText(mContext, res.get("Message").toString(), 0).show();
                    return;
                }
                super.onPostExecute(result);
            }

        }.execute(new ParamData(isjidu?ApiCode.AddEvaluate:ApiCode.AddEventAllot, JSONUtil.toJson(senddata), pid + "/" + (Type.equals("0") ? "null" : URLEncoder.encode(jiaoban)) + "/" + Type));
    }

}
