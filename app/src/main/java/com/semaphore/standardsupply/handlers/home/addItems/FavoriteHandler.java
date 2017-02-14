package com.semaphore.standardsupply.handlers.home.addItems;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.addItems.CreateFavoriteOperation;
import com.semaphore.network.request.addItems.DeleteFavoriteOperation;
import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.home.addItems.ItemDetailFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.Model;

public class FavoriteHandler extends Handler {

	private Item item;
	public FavoriteHandler(ItemDetailFragment fragment, Item item) {
		super(fragment);
		this.item = item;
	}

	@Override
	public void request() {
		if(item.favorite == true){
			DeleteFavoriteOperation operation = new DeleteFavoriteOperation(this, item.inv_mast_uid);
			HttpAsync.makeRequest(operation);
		}
		else{
			CreateFavoriteOperation operation = new CreateFavoriteOperation(this);
			operation.id =item.inv_mast_uid;
			HttpAsync.makeRequest(operation);
		}
	}
	
	@Override
	public void callback(String result) {
		item.favorite = !item.favorite;
		Model.getInstance().getMyFavoritesCache().invalidate();
		if(item.favorite == true){
			((ItemDetailFragment)fragment).btnFavorite.setImageResource(R.drawable.star_icn_active);
		}
		else{
			((ItemDetailFragment)fragment).btnFavorite.setImageResource(R.drawable.star_icn);
		}
	}

}
