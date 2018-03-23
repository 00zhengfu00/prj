package reco.frame.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import reco.frame.tv.TvBitmap;
/**
 * 此控件禁用焦点
 * @author reco
 *
 */
public class TvImageView extends View {

	public Boolean showLoading = true;

	public TvImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TvImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TvImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	/**
	 * 加载网络图片
	 * @param url
	 */
	public void configImageUrl(String url) {
		
		TvBitmap.create(getContext()).display(this, url);
		
	}

	/**
	 * 可配置网络图片
	 * 
	 * @param url
	 * @param loadingRes
	 */
	public void configImageUrl(String url, int loadingRes) {

		TvBitmap.create(getContext()).display(this, url,loadingRes);

	}
}
