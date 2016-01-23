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
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.AttrViewHolder> {

    List<Attributes> list;
    View v;

    CustomAdapter(List<Attributes> list) {
        this.list = list;
    }

    @Override
    public AttrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        AttrViewHolder avh = new AttrViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(AttrViewHolder holder, int position) {
        Attributes product = list.get(position);
        holder.textTitle.setText(product.title);
        holder.textWebsite.setText(product.website);
        holder.textPrice.setText(String.valueOf(product.sellingPrice));
        displayProductImage(product.imageUrl,holder.photo);

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

    public static class AttrViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textTitle;
        TextView textWebsite;
        TextView textPrice;
        ImageView photo;

        AttrViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.item);
            textTitle = (TextView) view.findViewById(R.id.prodTitle);
            textWebsite = (TextView) view.findViewById(R.id.website);
            textPrice = (TextView) view.findViewById(R.id.textPrice);
            photo = (ImageView) view.findViewById(R.id.imageView);

        }
    }
}
