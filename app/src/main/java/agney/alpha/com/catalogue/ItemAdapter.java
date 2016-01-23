package agney.alpha.com.catalogue;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Agney on 17-01-2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.AttributeViewHolder> {

    List<Attributes> list;
    View v;
    String itemBefore = "bleak";

    ItemAdapter(List<Attributes> list) {
        this.list = list;
    }

    @Override
    public AttributeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        AttributeViewHolder avh = new AttributeViewHolder(v);
        return avh;
    }



    @Override
    public void onBindViewHolder(AttributeViewHolder holder, int position) {
        Attributes product = list.get(position);
        if(!itemBefore.equals(product.title)) {
            holder.textTitle.setText(product.title);
            displayProductImage(product.imageUrl, holder.photo);
            itemBefore = product.title;
        }
    }



    public void displayProductImage(String url, ImageView iV) {
        Picasso.with(v.getContext())
                .load(url)
                .resize(400,400)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .centerInside()
                .into(iV);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class AttributeViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textTitle;
        TextView textDesc;
        ImageView photo;

        AttributeViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.item);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            textDesc = (TextView) view.findViewById(R.id.textDesc);
            photo = (ImageView) view.findViewById(R.id.photo);

        }


    }
}

