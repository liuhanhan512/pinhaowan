package com.hwand.pinhaowanr.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.CommonViewHolder;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.widget.SwipeRefreshLayout;
import com.hwand.pinhaowanr.widget.hlistview.HListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class FineFragment<T> extends BaseFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ExpandableListView mExpandableListView;

    private ExpandAdapter mExpandAdapter;

    private List<T> mListData = new ArrayList<T>();

    public static FineFragment newInstance(){
        FineFragment fragment = new FineFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fine_layout;
    }

    class ExpandAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public ExpandAdapter(Context context) {

            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return mListData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
//            return mListData.get(groupPosition).getTopic().size();
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return mListData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
//            return mListData.get(groupPosition).getTopic().get(childPosition);
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return groupPosition << 32 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View groupView = null;
            if (convertView == null) {
                groupView = newGroupView(parent);
            } else {
                groupView = convertView;
            }
            bindGroupView(groupPosition, groupView);
            return groupView;
        }

        private View newGroupView(ViewGroup parent) {
            return inflater.inflate(R.layout.fine_group_item_layout, null);
        }

        private void bindGroupView(final int groupPosition, View groupView) {
            TextView title = (TextView)groupView.findViewById(R.id.title);
            ImageView more = (ImageView)groupView.findViewById(R.id.image_more);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        private void onViewMoreClick(int groupPosition){

        }
        @Override
        public View getChildView(int groupPosition, int childPosition,boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View childView = null;
            if (convertView == null) {
                childView = newChildView(parent, groupPosition);
            } else {
                childView = convertView;
            }
            bindChildView(groupPosition, childPosition, childView);
            return childView;
        }

        private View newChildView(ViewGroup parent, final int groupPosition) {
            View v = inflater.inflate(R.layout.fine_child_item_layout, null);

            return v;
        }

        private void bindChildView(final int groupPosition, int childPosition,
                                   View groupView) {

            HListView listView = (HListView) groupView.findViewById(R.id.listview);

            final FineItemAdpater adapter  = new FineItemAdpater();

            listView.setOnItemClickListener(new com.hwand.pinhaowanr.widget.hlistview.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(com.hwand.pinhaowanr.widget.hlistview.AdapterView<?> parent, View view, int position, long id) {

                }
            });

            listView.setAdapter(adapter);// 设置菜单Adapter

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    class FineItemAdpater extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(FineFragment.this.getActivity())
                        .inflate(R.layout.fine_list_item_layout, viewGroup, false);
            }
            ImageView imageView = CommonViewHolder.get(convertView , R.id.image);
            TextView title = CommonViewHolder.get(convertView , R.id.title);
            TextView address = CommonViewHolder.get(convertView , R.id.address);
            TextView ticket = CommonViewHolder.get(convertView , R.id.tickets);
            TextView payment = CommonViewHolder.get(convertView , R.id.payment);
            return convertView;
        }
    }
}
