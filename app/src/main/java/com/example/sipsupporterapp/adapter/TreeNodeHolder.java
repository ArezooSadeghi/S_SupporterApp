package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.NodeHolderBinding;
import com.unnamed.b.atv.model.TreeNode;

public class TreeNodeHolder extends TreeNode.BaseNodeViewHolder<TreeNodeHolder.TreeNode> {
    private NodeHolderBinding binding;

    public TreeNodeHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(com.unnamed.b.atv.model.TreeNode node, TreeNode value) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.node_holder,
                null,
                false);

        binding.imgViewNodeIcon.setImageResource(value.getIcon());
        binding.txtNodeText.setText(value.getText());

        return binding.getRoot();
    }

    public static class TreeNode {
        private int icon;
        private String text;

        public TreeNode(int icon, String text) {
            this.icon = icon;
            this.text = text;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
