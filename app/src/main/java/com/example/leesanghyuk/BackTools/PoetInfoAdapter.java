package com.example.leesanghyuk.BackTools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leesanghyuk.POJO.SearchPoemInfo;
import com.example.sf.amap3d.R;

import java.util.List;

public class PoetInfoAdapter extends BaseAdapter{
    private List<SearchPoemInfo> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
    /*
    定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    */
    public PoetInfoAdapter(LayoutInflater inflater, List<SearchPoemInfo> data){
        mInflater = inflater;
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PoetInfoAdapter.ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.poet_list_item,parent, false);
            viewHolder = new PoetInfoAdapter.ViewHolder();
            viewHolder.title= (TextView) convertView.findViewById(R.id.item_poet_title);
            viewHolder.content= (TextView) convertView.findViewById(R.id.content);
            viewHolder.poet_id= (TextView) convertView.findViewById(R.id.poet_id);
            viewHolder.id= (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }
        else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            viewHolder = (PoetInfoAdapter.ViewHolder) convertView.getTag();
        }
        SearchPoemInfo poetInfo=mData.get(position);

        viewHolder.title.setText(poetInfo.getTitle());
        viewHolder.content.setText(poetInfo.getContent());
        viewHolder.poet_id.setText(String.valueOf(poetInfo.getPoet_id()));
        viewHolder.id.setText(String.valueOf(poetInfo.getId()));
       
        return convertView;
    }
    private class ViewHolder{
        TextView title;
        TextView content;
        TextView id;
        TextView poet_id;
    }
}
